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

package org.opentna.data.service;

import static org.assertj.core.api.Assertions.assertThat;

import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opentna.data.model.entity.User;
import org.opentna.rest.RestApplication;
import org.opentna.rest.configuration.DataConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {RestApplication.class})
@Import(DataConfiguration.class)
@Slf4j
public class UserServiceTests {

  @Autowired
  private UserService userService;

  @Test
  public void test001() {
    // testing for a correct username
    User user = userService.createUser(new User("james", "123456", false, true, null, null));
    log.info(String.format("CREATES: %s", user.toString()));
    assertThat(user).hasFieldOrPropertyWithValue("username", "james");
    assertThat(user).hasFieldOrPropertyWithValue("mustChangePassword", false);
    assertThat(user).hasFieldOrPropertyWithValue("enabled", true);
    assertThat(user).hasFieldOrProperty("password");
    assertThat(user).hasFieldOrProperty("createdAt");
  }

  @Test(expected = ConstraintViolationException.class)
  public void test002() {
    // testing for an incorrect username
    userService.createUser(new User("a", "123456", false, true, null, null));
  }

  @Test(expected = DataIntegrityViolationException.class)
  public void test003() {
    // testing for a duplicate username
    userService.createUser(new User("james", "123456", false, true, null, null));
  }

  @Test
  public void test004() {
    // testing for load by ID
    User user = userService.loadUserById(1);
    log.info(String.format("RETRIEVES: User ID is '%s'", user.getId()));
    assertThat(user).hasFieldOrPropertyWithValue("username", "james");
  }

  @Test(expected = UserNotFoundException.class)
  public void test005() {
    // testing for a nonexistent ID
    userService.loadUserById(2);
  }

  @Test
  public void test006() {
    // testing for a exists username
    User user = userService.loadUserByUsername("james");
    log.info(String.format("RETRIEVES: User ID is '%s'", user.getId()));
    assertThat(user).hasFieldOrPropertyWithValue("username", "james");
  }

  @Test(expected = UserNotFoundException.class)
  public void test007() {
    // testing for a nonexistent username
    userService.loadUserByUsername("test");
  }

  @Test
  public void test008() {
    // testing for change password with correct user ID
    User original = userService.loadUserById(1);
    userService.changePassword(1, "654321");
    User user = userService.loadUserById(1);
    assertThat(original.getPassword()).isNotEqualTo(user.getPassword());
    assertThat(original.getLastModifiedAt()).isNotEqualTo(user.getLastModifiedAt());
  }

  @Test(expected = IllegalArgumentException.class)
  public void test009() {
    // testing for change password with incorrect user ID
    userService.changePassword(-1, "654321");
  }

  @Test(expected = UserNotFoundException.class)
  public void test010() {
    // testing for change password with incorrect user ID
    userService.changePassword(9, "654321");
  }

  @Test
  public void test011() {
    // testing for update must change password state with correct user ID
    User original = userService.loadUserById(1);
    userService.updateMustChangePassword(1, true);
    User user = userService.loadUserById(1);
    assertThat(original.isMustChangePassword()).isNotEqualTo(user.isMustChangePassword());
    assertThat(original.getLastModifiedAt()).isNotEqualTo(user.getLastModifiedAt());
  }

  @Test(expected = IllegalArgumentException.class)
  public void test012() {
    // testing for update must change password state with incorrect user ID
    userService.updateMustChangePassword(-1, true);
  }

  @Test(expected = UserNotFoundException.class)
  public void test013() {
    // testing for update must change password state with incorrect user ID
    userService.updateMustChangePassword(9, true);
  }

  @Test
  public void test014() {
    // testing for update the enabled state with correct user ID
    User original = userService.loadUserById(1);
    userService.updateEnabled(1, false);
    User user = userService.loadUserById(1);
    assertThat(original.isEnabled()).isNotEqualTo(user.isEnabled());
    assertThat(original.getLastModifiedAt()).isNotEqualTo(user.getLastModifiedAt());
  }

  @Test(expected = IllegalArgumentException.class)
  public void test015() {
    // testing for update the enabled state with incorrect user ID
    userService.updateEnabled(-1, false);
  }

  @Test(expected = UserNotFoundException.class)
  public void test016() {
    // testing for update the enabled state with incorrect user ID
    userService.updateEnabled(9, false);
  }

}