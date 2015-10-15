package nu.jixa.its.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
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
      SerializerProvider serializerProvider) throws IOException {
    depth.set(depth.get() + 1);
    try {
      jsonGenerator.writeStartObject();
      jsonGenerator.writeNumberField("number", user.getNumber());
      jsonGenerator.writeStringField("firstname", user.getFirstname());
      jsonGenerator.writeStringField("lastname", user.getLastname());
      jsonGenerator.writeStringField("username", user.getUsername());
      if (depth.get() == 0) {
        jsonGenerator.writeObjectField("team", user.getTeam());
      } else {
        if (user.getTeam() == null) {
          jsonGenerator.writeNullField("teamnumber");
        } else {
          jsonGenerator.writeNumberField("teamnumber", user.getTeam().getNumber());
        }
      }

      jsonGenerator.writeEndObject();

    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}
