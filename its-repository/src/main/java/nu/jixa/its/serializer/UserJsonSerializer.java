package nu.jixa.its.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import nu.jixa.its.model.User;

public class UserJsonSerializer extends JsonSerializer<User> {

  private static ThreadLocal<Integer> depth = new ThreadLocal<Integer>() {

    @Override
    protected Integer initialValue() {
      return 0;
    }
  };

  @Override public void serialize(User user, JsonGenerator jsonGenerator,
      SerializerProvider serializerProvider) throws IOException, JsonProcessingException {

    depth.set(depth.get() + 1);
    try {
      //if (depth.get() > 2) {
      //  jsonGenerator.writeNull();
      //} else {

      jsonGenerator.writeStartObject();
      jsonGenerator.writeNumberField("number", user.getNumber());
      jsonGenerator.writeStringField("firstname", user.getFirstname());
      jsonGenerator.writeStringField("lastname", user.getLastname());
      jsonGenerator.writeStringField("username", user.getUsername());
      if (depth.get() == 0) {
        jsonGenerator.writeObjectField("team", user.getTeam());
      } else {
        if (user.getTeam() == null) {
          jsonGenerator.writeNullField("team-number");
        } else {
          jsonGenerator.writeNumberField("team-number", user.getTeam().getNumber());
        }
      }

      jsonGenerator.writeEndObject();

      return;
      //}
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}
