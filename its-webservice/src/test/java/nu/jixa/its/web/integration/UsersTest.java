package nu.jixa.its.web.integration;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.dataset.ReplacementDataSetLoader;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import java.sql.SQLException;
import nu.jixa.its.web.TestUtil;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isEmptyOrNullString;

@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class,
    TransactionalTestExecutionListener.class, DbUnitTestExecutionListener.class })
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = IntegrationTestContext.class)
@WebAppConfiguration
@IntegrationTest("server.port=0")
@DbUnitConfiguration(dataSetLoader = ReplacementDataSetLoader.class)
public class UsersTest {
  // @formatter:off
  private static final String BASE_URL = "http://localhost:";
  private static final String USERS_ENDPOINT = "/users/";
  private static final String USER_ENDPOINT = "/users/{userNumber}";
  private static final String LOCATION_HEADER = "location";
  private static final String FIRSTNAME_FIELD = "firstname";
  private static final String LASTNAME_FIELD = "lastname";
  private static final String TEAM_FIELD = "team";
  private String FULL_USERS_ENDPOINT;

  private static final Long EXISTING_USER_NUMBER = 1L;
  private static final String EXISTING_USER_FIRSTNAME = "firstname1";
  private static final String EXISTING_USER_LASTNAME = "lastname1";

  private static final Long NEW_USER_NUMBER = 3L;


  @Value("${local.server.port}")
  private int serverPort;

  @Autowired
  private ApplicationContext applicationContext;

  @Before
  public void setUp() throws SQLException {
    RestAssured.port = serverPort;
    FULL_USERS_ENDPOINT = BASE_URL + serverPort + USERS_ENDPOINT;

    // Reset autoIncrement on DB so @ExpectedDatabase gets correct ID's
    TestUtil.resetAutoIncrementColumns(applicationContext, "tblUser");
  }

  @Test
  @DatabaseSetup("/usersData.xml")
  public void getUserByNumber() {
    when()
        .get(USER_ENDPOINT, EXISTING_USER_NUMBER)
    .then()
        .statusCode(is(equalTo(HttpStatus.SC_OK)))
        .body(FIRSTNAME_FIELD, equalTo(EXISTING_USER_FIRSTNAME))
        .body(LASTNAME_FIELD, equalTo(EXISTING_USER_LASTNAME))
        .body(TEAM_FIELD, isEmptyOrNullString());
  }

  @Test
  @DatabaseSetup("/usersData.xml")
  public void createUserShouldReturnLocationURI() {
    given()
        .body(TestUtil.createUserWithNumber(NEW_USER_NUMBER))
        .contentType(ContentType.JSON)
    .when()
        .post(USERS_ENDPOINT)
    .then()
        .statusCode(HttpStatus.SC_CREATED)
        .header(LOCATION_HEADER, is(FULL_USERS_ENDPOINT + NEW_USER_NUMBER));
  }

  @Test
  @DatabaseSetup("/usersData.xml")
  @ExpectedDatabase(value = "/usersData-add-expected.xml", table = "tblUser")
  public void createUserShouldAddUserToDatabase() {
    given()
        .body(TestUtil.createUserWithNumber(NEW_USER_NUMBER))
        .contentType(ContentType.JSON)
    .when()
        .post(USERS_ENDPOINT);
  }
}
