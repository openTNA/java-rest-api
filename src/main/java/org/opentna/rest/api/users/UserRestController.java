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

package org.opentna.rest.api.users;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.HashSet;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.opentna.data.model.entity.Role;
import org.opentna.data.model.entity.User;
import org.opentna.data.model.vo.PasswordRequest;
import org.opentna.data.model.vo.BaseUserRequest;
import org.opentna.data.model.vo.UserRequest;
import org.opentna.data.model.vo.UsernameRequest;
import org.opentna.data.service.RoleNotFoundException;
import org.opentna.data.service.RoleService;
import org.opentna.data.service.UserNotFoundException;
import org.opentna.data.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * The user REST controller.
 *
 * @author James Ho
 */
@RestController
@RequestMapping("/users")
@Slf4j
public class UserRestController {

  @Autowired
  private UserService userService;

  @Autowired
  private RoleService roleService;

  private static final int DEFAULT_PAGE_SIZE = 10;
  private static final int DEFAULT_PAGE_NUMBER = 1;

  /**
   * Creates new user.
   *
   * @param payload the request payload for user
   * @return a user model as JSON
   */
  @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<?> createUser(@RequestBody @Valid UserRequest payload) {
    log.debug(String.format("Payload: %s", payload));
    User user = convertToUser(payload);
    if (user == null) {
      return new ResponseEntity<String>("{}", HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<User>(userService.createUser(user), HttpStatus.CREATED);
  }

  /**
   * Retrieves user with the given user ID.
   *
   * @param id the user ID
   * @return a user model as JSON
   */
  @RequestMapping(value = "/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<?> retrieveUserProfile(@PathVariable("id") long id) {
    log.debug(String.format("User ID: %s", id));
    User user = userService.loadUserById(id);
    if (user == null) {
      throw new UserNotFoundException(id);
    }
    return new ResponseEntity<User>(user, HttpStatus.OK);
  }

  /**
   * Retrieves user with the given username.
   *
   * @param username the username
   * @return a user model as JSON
   */
  @RequestMapping(value = "/{username}/profile", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<?> retrieveUserProfile(@PathVariable("username") String username) {
    log.debug(String.format("Username: %s", username));
    User user = userService.loadUserByUsername(username);
    if (user == null) {
      throw new UserNotFoundException(username);
    }
    return new ResponseEntity<User>(user, HttpStatus.OK);
  }

  /**
   * Retrieves all user with the given page and size.
   *
   * @param page the page number
   * @param size the maximum number of rows
   * @return a collection of user model as JSON
   */
  @RequestMapping(value = "", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<?> retrieveAllUser(
      @RequestParam(value = "page", required = false) Integer page,
      @RequestParam(value = "size", required = false) Integer size) {
    page = (page == null) ? DEFAULT_PAGE_NUMBER - 1 : (page < 1) ? 0 : page - 1;
    size = (size == null) ? DEFAULT_PAGE_SIZE : (size < 1) ? 1 : size;

    Page<User> result = userService.loadAllUserByPaginated(PageRequest.of(page, size));
    log.debug(String.format("Total pages: %s", result.getTotalPages()));
    if (page > result.getTotalPages()) {
      log.debug("Resource not found");
      return new ResponseEntity<String>("{}", HttpStatus.BAD_REQUEST);
    }
    return new ResponseEntity<Collection<User>>(result.getContent(), HttpStatus.OK);
  }

  /**
   * Change username.
   *
   * @param id the user ID
   * @param payload the request payload for username
   * @return a user model as JSON
   */
  @RequestMapping(value = "/{id}/change/username", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<?> changeUsername(@PathVariable("id") long id,
      @RequestBody UsernameRequest payload) {
    log.debug(String.format("User ID: %s", id));
    log.debug(String.format("Payload: %s", payload));
    User user = userService.loadUserById(id);
    if (user == null) {
      throw new UserNotFoundException(id);
    }

    // username is the same as the original username
    if (user.getUsername().equals(payload.getUsername())) {
      log.debug("Nothing to do");
      return new ResponseEntity<String>("{}", HttpStatus.ACCEPTED);
    }

    if (userService.loadUserByUsername(payload.getUsername()) != null) {
      log.debug("Duplicate username");
      return new ResponseEntity<String>("{}", HttpStatus.CONFLICT);
    }

    // set data
    user.setUsername(payload.getUsername());

    return new ResponseEntity<User>(userService.updateUser(user), HttpStatus.OK);
  }

  /**
   * Change password.
   *
   * @param id the user ID
   * @param payload the request payload for password
   * @return a user model as JSON
   */
  @RequestMapping(value = "/{id}/change/password", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<?> changePassword(@PathVariable("id") long id,
      @RequestBody @Valid PasswordRequest payload) {
    log.debug(String.format("User ID: %s", id));
    log.debug(String.format("Payload: %s", payload));
    User user = userService.loadUserById(id);
    if (user == null) {
      throw new UserNotFoundException(id);
    }

    // check original password
    log.debug(String.format("@@ %s", user.getPassword()));
    log.debug(String.format("## %s", getPassword(payload.getOriginalPassword())));
    if (!user.getPassword().equals(getPassword(payload.getOriginalPassword()))) {
      log.debug("Invalid original password");
      return new ResponseEntity<String>("{}", HttpStatus.BAD_REQUEST);
    }

    // password is the same as the original password
    if (user.getPassword().equals(getPassword(payload.getCurrentPassword()))) {
      log.debug("Nothing to do");
      return new ResponseEntity<String>("{}", HttpStatus.ACCEPTED);
    }

    // TODO: check password rule

    // set data
    user.setPassword(getPassword(payload.getCurrentPassword()));

    return new ResponseEntity<User>(userService.updateUser(user), HttpStatus.OK);
  }

  /**
   * Updates user profile.
   *
   * @param id the user ID
   * @param payload the request payload for user profile
   * @return a user model as JSON
   */
  @RequestMapping(value = "/{id}/profile", method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<?> updateUserProfile(@PathVariable("id") long id,
      @RequestBody @Valid BaseUserRequest payload) {
    log.debug(String.format("User ID: %s", id));
    log.debug(String.format("Payload: %s", payload));
    User user = userService.loadUserById(id);
    if (user == null) {
      throw new UserNotFoundException(id);
    }

    boolean isUpdate = false;

    // set data
    if (user.isMustChangePassword() != payload.isMustChangePassword()) {
      user.setMustChangePassword(payload.isMustChangePassword());
      isUpdate = true;
    }
    if (user.isEnabled() != payload.isEnabled()) {
      user.setEnabled(payload.isEnabled());
      isUpdate = true;
    }

    if (!isUpdate) {
      log.debug("Nothing to do");
      return new ResponseEntity<String>("{}", HttpStatus.ACCEPTED);
    }

    return new ResponseEntity<User>(userService.updateUser(user), HttpStatus.OK);
  }

  /**
   * Updates user's role.
   *
   * @param id the user ID
   * @param payloads request payload for a collection of role
   * @return a user model as JSON
   */
  @RequestMapping(value = "/{id}/roles", method = RequestMethod.PUT, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<?> updateUserRole(@PathVariable("id") long id,
      @RequestBody @Valid Collection<Long> payloads) {
    log.debug(String.format("User ID: %s", id));
    log.debug(String.format("Payload: %s", payloads));
    User user = userService.loadUserById(id);
    if (user == null) {
      throw new UserNotFoundException(id);
    }
    Role role = null;
    Collection<Role> roles = null;
    for (Long i : payloads) {
      if (i == null) {
        // TODO: throw exception
      }
      role = roleService.loadRoleById(i);
      if (role == null) {
        throw new RoleNotFoundException(i);
      }
      if (roles == null) {
        roles = new ArrayList<Role>();
      }
      roles.add(role);
    }
    log.debug(String.format("%s", roles));

    // set data
    user.setRoles((roles != null) ? new HashSet<Role>(roles) : null);

    return new ResponseEntity<User>(userService.updateUser(user), HttpStatus.OK);
  }

  private String getPassword(String base64Password) {
    return new String(Base64.getDecoder().decode(base64Password), StandardCharsets.UTF_8);
  }

  private User convertToUser(UserRequest payload) {
    if (payload == null) {
      return null;
    }
    final User user = new User();
    user.setUsername(payload.getUsername());
    user.setPassword(getPassword(payload.getPassword()));
    user.setMustChangePassword(payload.isMustChangePassword());
    user.setEnabled(payload.isEnabled());
    return user;
  }

}
