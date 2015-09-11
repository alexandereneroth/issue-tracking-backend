package nu.jixa.its.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.util.ArrayList;
import jersey.repackaged.com.google.common.collect.Lists;
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
      if (depth.get() > 2) {
        jsonGenerator.writeNull();
      } else {

        jsonGenerator.writeStartObject();
        jsonGenerator.writeNumberField("number", user.getNumber());
        jsonGenerator.writeStringField("firstname", user.getFirstname());
        jsonGenerator.writeStringField("lastname", user.getLastname());
        jsonGenerator.writeStringField("username", user.getUsername());
        if (depth.get() == 0) {
          jsonGenerator.writeObjectField("team", user.getTeam());
        } else {
          ArrayList<User> users = Lists.newArrayList(user.getTeam().getUsers());
          jsonGenerator.writeArrayFieldStart("team");

          for (User u : users) {
            jsonGenerator.writeNumber(u.getNumber());
          }
          jsonGenerator.writeEndArray();
        }
        //User createdByUser = user.getCreatedBy();
        //if (null != createdByUser) {
        //  jsonGenerator.writeStringField(
        //      "createdBy",
        //      createdByUser.getFirstName() + " "
        //          + createdByUser.getLastName());
        //}
        //
        //User lastModifiedByUser = user.getLastModifiedBy();
        //if (null != lastModifiedByUser) {
        //  jsonGenerator.writeStringField("lastModifiedBy",
        //      lastModifiedByUser.getFirstName() + " "
        //          + lastModifiedByUser.getLastName());
        //}

        jsonGenerator.writeEndObject();

        return;
      }
    } catch (Exception ex) {
      ex.printStackTrace();
    }
  }
}
