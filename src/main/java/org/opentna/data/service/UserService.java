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

import javax.validation.Valid;
import org.opentna.data.model.entity.User;
import org.springframework.validation.annotation.Validated;

/**
 * The user service.
 *
 * @author James Ho
 */
@Validated
public interface UserService {

  /**
   * Creates new user.
   *
   * @param user the user model
   * @return a user record, never null or empty
   */
  public User createUser(@Valid User user);

  /**
   * Retrieves user with the given user ID.
   *
   * @param userId the user ID
   * @return a user record
   */
  public User loadUserById(long userId);

  /**
   * Retrieves user with the given username.
   *
   * @param username the username
   * @return a user record
   */
  public User loadUserByUsername(String username);

  /**
   * Change password.
   *
   * @param userId the user ID
   * @param password the new password
   */
  public void changePassword(long userId, String password);

  /**
   * Updates the must change password state.
   *
   * @param userId the user ID
   * @param state the must change password state
   */
  public void updateMustChangePassword(long userId, boolean state);

  /**
   * Updates the enabled state.
   *
   * @param userId the user ID
   * @param state the enabled state
   */
  public void updateEnabled(long userId, boolean state);

}
