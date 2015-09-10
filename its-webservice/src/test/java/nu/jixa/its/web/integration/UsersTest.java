package nu.jixa.its.web.integration;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.github.springtestdbunit.annotation.DbUnitConfiguration;
import com.github.springtestdbunit.annotation.ExpectedDatabase;
import com.github.springtestdbunit.dataset.ReplacementDataSetLoader;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import java.sql.SQLException;
import javax.json.Json;
import nu.jixa.its.model.User;
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
  private static final String USERS_DATA = "/usersData.xml";
  private static final String USERS_DATA_ADD_EXPECTED = "/usersData-add-expected.xml";
  private static final String USERS_DATA_DELETE_EXPECTED = "/userData-delete-expected.xml";
  private static final String USERS_DATA_UPDATE_EXPECTED = "/userData-update-expected.xml";
  private static final String USER_DB_TABLE = "tblUser";

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
  private static final String UPDATED_USER_FIRSTNAME = "updatedFirstname";
  private static final String UPDATED_USER_LASTNAME = "updatedLastname";

  private static final Long NEW_USER_NUMBER = 3L;
  private static final Long REMOVE_USER_NUMBER = 2L;
  private static final Long INVALID_USER_NUMBER = 9999L;

  private static final String INVALID_USER_JSON = Json.createObjectBuilder()
      .add("firstname", "invalidUserFirstname")
      .add("lastname", "invalidUserLastname")
      .build().toString();

  private static final String BAD_REQUEST_NULL_OR_INVALID =
      "Null or Invalid JSON Data in Request Body";
  private static final String BAD_REQUEST_MISMATCH_BETWEEN_PATH_AND_USER =
      "Usernumber mismatch between path and new user info";

  @Value("${local.server.port}")
  private int serverPort;

  @Autowired
  private ApplicationContext applicationContext;

  @Before
  public void setUp() throws SQLException {
    RestAssured.port = serverPort;
    FULL_USERS_ENDPOINT = BASE_URL + serverPort + USERS_ENDPOINT;

    // Reset autoIncrement on DB so @ExpectedDatabase gets correct ID's
    TestUtil.resetAutoIncrementColumns(applicationContext, USER_DB_TABLE);
  }

  @Test
  @DatabaseSetup(USERS_DATA)
  public void getUserByNumberShouldReturnStatusOk() {
    when()
        .get(USER_ENDPOINT, EXISTING_USER_NUMBER)
    .then().assertThat()
        .statusCode(HttpStatus.SC_OK);
  }

  @Test
  @DatabaseSetup(USERS_DATA)
  public void getUserByNumberShouldReturnUser() {
    when()
        .get(USER_ENDPOINT, EXISTING_USER_NUMBER)
    .then().assertThat()
        .statusCode(HttpStatus.SC_OK)
        .body(FIRSTNAME_FIELD, equalTo(EXISTING_USER_FIRSTNAME))
        .body(LASTNAME_FIELD, equalTo(EXISTING_USER_LASTNAME))
        .body(TEAM_FIELD, isEmptyOrNullString());
  }

  @Test
  @DatabaseSetup(USERS_DATA)
  public void createUserShouldReturnLocationURI() {
    given()
        .body(TestUtil.createUserWithNumber(NEW_USER_NUMBER))
        .contentType(ContentType.JSON)
    .when()
        .post(USERS_ENDPOINT)
    .then().assertThat()
        .statusCode(HttpStatus.SC_CREATED)
        .header(LOCATION_HEADER, is(FULL_USERS_ENDPOINT + NEW_USER_NUMBER));
  }

  @Test
  @DatabaseSetup(USERS_DATA)
  @ExpectedDatabase(value = USERS_DATA_ADD_EXPECTED, table = USER_DB_TABLE)
  public void createUserShouldAddUserToDatabase() {
    given()
        .body(TestUtil.createUserWithNumber(NEW_USER_NUMBER))
        .contentType(ContentType.JSON)
    .when()
        .post(USERS_ENDPOINT);
  }

  @Test
  @DatabaseSetup(USERS_DATA)
  public void createUserShouldReturnBadRequestWithoutBody() {

    // With No Body
    given()
        .contentType(ContentType.JSON)
    .when()
        .post(USERS_ENDPOINT)
    .then().assertThat()
        .statusCode(HttpStatus.SC_BAD_REQUEST)
        .body(equalTo(BAD_REQUEST_NULL_OR_INVALID));

    // And with Invalid Body
    given()
        .body(INVALID_USER_JSON)
        .contentType(ContentType.JSON)
    .when()
        .post(USERS_ENDPOINT)
    .then().assertThat()
        .statusCode(HttpStatus.SC_BAD_REQUEST)
        .body(equalTo(BAD_REQUEST_NULL_OR_INVALID));
  }

  @Test
  @DatabaseSetup(USERS_DATA)
  @ExpectedDatabase(value = USERS_DATA, table = USER_DB_TABLE)
  public void createUserWithInvalidUserDataShouldNotAddUserToDatabase() {
    given()
        .contentType(ContentType.JSON)
    .when()
        .post(USERS_ENDPOINT)
    .then().assertThat()
        .statusCode(HttpStatus.SC_BAD_REQUEST)
        .body(equalTo(BAD_REQUEST_NULL_OR_INVALID));
  }


  @Test
  @DatabaseSetup(USERS_DATA)
  @ExpectedDatabase(value = USERS_DATA_DELETE_EXPECTED, table = USER_DB_TABLE)
  public void deleteUserShouldRemoveUserFromDatabase() {
    when()
        .delete(USER_ENDPOINT, REMOVE_USER_NUMBER);
  }

  @Test
  @DatabaseSetup(USERS_DATA)
  public void deleteUserShouldReturnNoContent() {
    when()
        .delete(USER_ENDPOINT, REMOVE_USER_NUMBER)
    .then().assertThat()
        .statusCode(HttpStatus.SC_NO_CONTENT);
  }

  @Test
  @DatabaseSetup(USERS_DATA)
  public void updateUserShouldReturnNoContent() {
    User updatedUser = TestUtil.createUserWithNumber(EXISTING_USER_NUMBER);
    updatedUser.setFirstname(UPDATED_USER_FIRSTNAME);
    updatedUser.setLastname(UPDATED_USER_LASTNAME);

    given()
        .body(updatedUser)
        .contentType(ContentType.JSON)
    .when()
        .put(USER_ENDPOINT, EXISTING_USER_NUMBER)
    .then().assertThat()
        .statusCode(HttpStatus.SC_NO_CONTENT);
  }

  @Test
  @DatabaseSetup(USERS_DATA)
  @ExpectedDatabase(value = USERS_DATA_UPDATE_EXPECTED, table = USER_DB_TABLE)
  public void updateUserShouldUpdateUserInDatabase() {
    User updatedUser = TestUtil.createUserWithNumber(EXISTING_USER_NUMBER);
    updatedUser.setFirstname(UPDATED_USER_FIRSTNAME);
    updatedUser.setLastname(UPDATED_USER_LASTNAME);

    given()
        .body(updatedUser)
        .contentType(ContentType.JSON)
    .when()
        .put(USER_ENDPOINT, EXISTING_USER_NUMBER)
    .then().assertThat()
        .statusCode(HttpStatus.SC_NO_CONTENT);
  }

  @Test
  @DatabaseSetup(USERS_DATA)
  public void updateUserShouldReturnNotFoundtIfNonExistingID() {
    User updatedUser = TestUtil.createUserWithNumber(EXISTING_USER_NUMBER);
    updatedUser.setNumber(INVALID_USER_NUMBER);

    given()
        .body(updatedUser)
        .contentType(ContentType.JSON)
    .when()
        .put(USER_ENDPOINT, INVALID_USER_NUMBER)
    .then().assertThat()
        .statusCode(HttpStatus.SC_NOT_FOUND);
  }

  @Test
  @DatabaseSetup(USERS_DATA)
  public void updateUserShouldReturnBadRequestAndMessageWithPathUsernumberAndBodyUsernumberMismatch() {
    User updatedUser = TestUtil.createUserWithNumber(EXISTING_USER_NUMBER);
    updatedUser.setNumber(INVALID_USER_NUMBER);

    given()
        .body(updatedUser)
        .contentType(ContentType.JSON)
    .when()
        .put(USER_ENDPOINT, EXISTING_USER_NUMBER)
    .then().assertThat()
        .statusCode(HttpStatus.SC_BAD_REQUEST)
        .body(equalTo(BAD_REQUEST_MISMATCH_BETWEEN_PATH_AND_USER));
  }

  @Test
  @DatabaseSetup(USERS_DATA)
  public void updateUserShouldReturnBadRequestAndMessageWithoutBody() {
    given()
        .contentType(ContentType.JSON)
    .when()
        .put(USER_ENDPOINT, EXISTING_USER_NUMBER)
    .then().assertThat()
        .statusCode(HttpStatus.SC_BAD_REQUEST)
        .body(equalTo(BAD_REQUEST_NULL_OR_INVALID));
  }

}
