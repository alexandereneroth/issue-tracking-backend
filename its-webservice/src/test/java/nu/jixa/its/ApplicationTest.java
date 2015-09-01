package nu.jixa.its;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@IntegrationTest("server.port=0")
@WebAppConfiguration
public class ApplicationTest {
  @Value("${local.server.port}")
  private int port;

  private RestTemplate restTemplate = new TestRestTemplate();

  @Test
  public void contextLoads() {
    ResponseEntity<String> entity = this.restTemplate.getForEntity(
        "http://localhost:" + this.port + "/hello", String.class);
    assertEquals(HttpStatus.OK, entity.getStatusCode());
  }

  @Test
  public void reverse() {
    ResponseEntity<String> entity = this.restTemplate.getForEntity(
        "http://localhost:" + this.port + "/reverse?input=olleh", String.class);
    assertEquals(HttpStatus.OK, entity.getStatusCode());
    assertEquals("hello", entity.getBody());
  }

  @Test
  public void validation() {
    ResponseEntity<String> entity = this.restTemplate.getForEntity(
        "http://localhost:" + this.port + "/reverse", String.class);
    assertEquals(HttpStatus.BAD_REQUEST, entity.getStatusCode());
  }
}
