/*
 * Copyright 2018 the original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.opentna.data.repository;

import static org.assertj.core.api.Assertions.assertThat;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opentna.data.model.entity.User;
import org.opentna.rest.RestApplication;
import org.opentna.rest.configuration.DataConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {RestApplication.class})
@DataJpaTest
@Import(DataConfiguration.class)
@Slf4j
public class UserRepositoryTests {

  @Autowired
  private UserRepository repository;

  private User user;

  private final String userUsername = "james";
  private final String userPassword = "123456";
  private final boolean userMustChangePassword = false;
  private final boolean userEnabled = true;

  @Test
  public void test() {
    create();
    findByUsername();
    update();
    delete();
  }

  private void create() {
    user = new User(userUsername, userPassword, userMustChangePassword, userEnabled, null, null);
    user.setCreatedAt(System.currentTimeMillis());
    user = repository.save(user);
    log.info(String.format("CREATES: %s", user.toString()));
    assertThat(user).hasFieldOrPropertyWithValue("username", userUsername);
    assertThat(user).hasFieldOrPropertyWithValue("password", userPassword);
    assertThat(user).hasFieldOrPropertyWithValue("mustChangePassword", userMustChangePassword);
    assertThat(user).hasFieldOrPropertyWithValue("enabled", userEnabled);
    assertThat(user).hasFieldOrProperty("createdAt");
  }

  private void findByUsername() {
    User entity = repository.findByUsername(userUsername).orElse(null);
    log.info(String.format("RETRIEVES: %s", entity.toString()));
    assertThat(entity.getUsername()).isEqualTo(userUsername);
    assertThat(entity.getPassword()).isEqualTo(userPassword);
    assertThat(entity.isMustChangePassword()).isEqualTo(userMustChangePassword);
    assertThat(entity.isEnabled()).isEqualTo(userEnabled);
  }

  private void update() {
    user.setUsername("squid");
    user.setPassword("654321");
    user.setLastModifiedAt(System.currentTimeMillis());
    user = repository.save(user);
    log.info(String.format("UPDATES: %s", user.toString()));
    assertThat(user.getUsername()).isEqualTo("squid");
    assertThat(user.getPassword()).isEqualTo("654321");
    assertThat(user.isMustChangePassword()).isEqualTo(userMustChangePassword);
    assertThat(user.isEnabled()).isEqualTo(userEnabled);
    assertThat(user).hasFieldOrProperty("createdAt");
    assertThat(user).hasFieldOrProperty("lastModifiedAt");
  }

  private void delete() {
    repository.delete(user);
    log.info(String.format("DELETES: %s", user.toString()));
    assertThat(repository.findByUsername(userUsername).orElse(null)).isEqualTo(null);
    assertThat(repository.findByUsername("squid").orElse(null)).isEqualTo(null);
  }

}