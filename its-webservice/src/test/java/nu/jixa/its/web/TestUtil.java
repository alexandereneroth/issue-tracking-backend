/*
 * Copyright 2012-2014 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package nu.jixa.its.web;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;
import nu.jixa.its.model.User;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;

public class TestUtil {

  private static final String PROPERTY_KEY_RESET_SQL_TEMPLATE = "test.reset.sql.template";

  /**
   * Prevents instantiation.
   */
  private TestUtil() {
  }

  /**
   * This method reads the invoked SQL statement template from a properties file, creates the
   * invoked SQL statements, and invokes them.
   *
   * @param applicationContext The application context that is used by our tests.
   * @param tableNames The names of the database tables which auto-increment column will be
   * reseted.
   * @throws SQLException If a SQL statement cannot be invoked for some reason.
   */
  public static void resetAutoIncrementColumns(ApplicationContext applicationContext,
      String... tableNames) throws SQLException {

    DataSource dataSource = applicationContext.getBean(DataSource.class);
    String resetSqlTemplate = getResetSqlTemplate(applicationContext);

    try (Connection dbConnection = dataSource.getConnection()) {
      //Create SQL statements that reset the auto-increment columns and invoke
      //the created SQL statements.
      for (String resetSqlArgument : tableNames) {
        try (Statement statement = dbConnection.createStatement()) {
          String resetSql = String.format(resetSqlTemplate, resetSqlArgument);
          statement.execute(resetSql);
        }
      }
    }
  }

  private static String getResetSqlTemplate(ApplicationContext applicationContext) {
    //Read the SQL template from the properties file
    Environment environment = applicationContext.getBean(Environment.class);
    return environment.getRequiredProperty(PROPERTY_KEY_RESET_SQL_TEMPLATE);
  }

  static public User createUserWithNumber(long userNumber) {
    return new User(
        userNumber, "username" + userNumber, "firstname" + userNumber, "lastname" + userNumber);
  }
}
