package nu.jixa.its.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;
import java.util.Set;
import jersey.repackaged.com.google.common.collect.Sets;
import nu.jixa.its.model.User;

public class SimpleUserSerializer extends JsonSerializer<Set<User>> {

  @Override
  public void serialize(final Set<User> users, final JsonGenerator generator,
      final SerializerProvider provider) throws IOException, JsonProcessingException {

    final Set<SimpleUser> simpleUsers = Sets.newHashSet();

    for (final User user : users) {
      simpleUsers.add(new SimpleUser(user.getNumber()));
    }

    generator.writeObject(simpleUsers);
  }

  static class SimpleUser {

    private Long number;

    public SimpleUser(Long number) {
      this.number = number;
    }
  }
}