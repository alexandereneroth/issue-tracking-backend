package nu.jixa.its.web;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import nu.jixa.its.model.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static com.jayway.restassured.RestAssured.get;
import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@IntegrationTest("server.port=0")
@WebAppConfiguration
public class UserEndpointTest {

  private static final String BASE_URL = "http://localhost:";
  private static final String USERS_ENDPOINT = "/users";
  private static final String LOCATION_HEADER = "location";

  @Value("${local.server.port}")
  private int port;
  private String fullUrl;


  private User peter, tom, kurt;
  private Response peterResponse;

  @Before
  public void setUp() {
    RestAssured.port = port;
    fullUrl = BASE_URL + port + USERS_ENDPOINT + "/";
    peter = TestUtil.createUserWithNumber(1);
    tom = TestUtil.createUserWithNumber(2);
    kurt = TestUtil.createUserWithNumber(3);
  }

  @Test
  public void getUserByNumber() {
    peterResponse = postPeterToServer();

    User returnedUser =
        get(peterResponse.header(LOCATION_HEADER))
        .then().assertThat().statusCode(is(equalTo(200)))
        .and().extract().body().as(User.class);

    assertThat(returnedUser, is(equalTo(peter)));
  }

  private Response postPeterToServer() {
    return given().body(peter).contentType(ContentType.JSON)
        .when().post(USERS_ENDPOINT)
        .then().assertThat().statusCode(201)
        .and().assertThat().header(LOCATION_HEADER, equalTo(fullUrl + peter.getNumber()))
        .extract().response();
  }
}
