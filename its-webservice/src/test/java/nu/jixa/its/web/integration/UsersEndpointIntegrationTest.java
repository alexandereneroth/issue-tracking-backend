package nu.jixa.its.web.integration;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.dataset.ReplacementDataSetLoader;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import nu.jixa.its.model.User;
import nu.jixa.its.web.TestUtil;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.WebAppConfiguration;

import static com.jayway.restassured.RestAssured.given;
import static com.jayway.restassured.RestAssured.when;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
    DirtiesContextTestExecutionListener.class,
    TransactionalTestExecutionListener.class, DbUnitTestExecutionListener.class })
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = IntegrationTestContext.class)
@WebAppConfiguration
@IntegrationTest("server.port=0")
@DatabaseSetup(value = "/usersData.xml")
@DbUnitConfiguration(dataSetLoader = ReplacementDataSetLoader.class)
public class UsersEndpointIntegrationTest {
  // @formatter:off
  private static final String BASE_URL = "http://localhost:";
  private static final String USERS_ENDPOINT = "/users/";
  private static final String USER_ENDPOINT = "/users/{userNumber}";
  private static final String LOCATION_HEADER = "location";
  private String FULL_USERS_ENDPOINT;

  private static final Long EXISTING_USER_NUMBER = 1L;
  private static final Long NEW_USER_NUMBER = 3L;

  @Value("${local.server.port}")
  private int serverPort;

  @Before
  public void setUp() {
    RestAssured.port = serverPort;
    FULL_USERS_ENDPOINT = BASE_URL + serverPort + USERS_ENDPOINT;
  }

  @Test
  @ExpectedDatabase(value = "/usersData.xml", table = "tblUser")
  public void getUserByNumber() {
    User returnedUser =
    when()
        .get(USER_ENDPOINT, EXISTING_USER_NUMBER)
    .then()
        .statusCode(is(equalTo(HttpStatus.SC_OK)))
    .extract()
        .body().as(User.class);

    assertThat(returnedUser.getNumber(), is(EXISTING_USER_NUMBER));
  }

  @Test
  public void createUserShouldReturnLocationURIAndAddUser() {
    given()
        .body(TestUtil.createUserWithNumber(NEW_USER_NUMBER))
        .contentType(ContentType.JSON)
    .when()
        .post(USERS_ENDPOINT)
    .then()
        .statusCode(HttpStatus.SC_CREATED)
        .header(LOCATION_HEADER, is(FULL_USERS_ENDPOINT + NEW_USER_NUMBER));
  }
}
