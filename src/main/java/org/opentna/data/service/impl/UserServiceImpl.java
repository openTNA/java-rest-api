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

package org.opentna.data.service.impl;

import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.opentna.data.model.entity.User;
import org.opentna.data.repository.UserRepository;
import org.opentna.data.service.UserNotFoundException;
import org.opentna.data.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * An implementation of the user service.
 *
 * @author James Ho
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
@Slf4j
public class UserServiceImpl implements UserService {

  @Autowired
  private UserRepository userRepository;

  @Override
  @Transactional(propagation = Propagation.REQUIRED)
  public User createUser(@Valid User user) {
    User entity = new User();
    entity.setUsername(user.getUsername());
    entity.setPassword(encodePassword(user.getPassword()));
    entity.setMustChangePassword(user.isMustChangePassword());
    entity.setEnabled(user.isEnabled());
    entity.setRoles(user.getRoles());
    entity.setProximityCards(user.getProximityCards());
    entity.setCreatedAt(System.currentTimeMillis());
    log.debug(String.format("%s", entity.toString()));
    return userRepository.save(entity);
  }

  @Override
  public User loadUserById(long userId) {
    return userRepository.findById(userId).orElse(null);
  }

  @Override
  public User loadUserByUsername(String username) {
    return userRepository.findByUsername(username).orElse(null);
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED)
  public User updateUser(@Valid User user) {
    long userId = user.getId();
    if (userId < 1) {
      throw new IllegalArgumentException("Invalid user ID");
    }
    User original = loadUserById(userId);
    if (original == null) {
      throw new UserNotFoundException(userId);
    }
    original.setUsername(user.getUsername());
    original.setPassword(encodePassword(user.getPassword()));
    original.setMustChangePassword(user.isMustChangePassword());
    original.setEnabled(user.isEnabled());
    original.setLastModifiedAt(System.currentTimeMillis());
    original.setRoles(user.getRoles());
    original.setProximityCards(user.getProximityCards());
    log.debug(String.format("%s", user));
    return userRepository.save(user);
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED)
  public void changePassword(long userId, String password) {
    if (userId < 1) {
      throw new IllegalArgumentException("Invalid user ID");
    }
    User user = loadUserById(userId);
    if (user == null) {
      throw new UserNotFoundException(userId);
    }
    user.setPassword(encodePassword(password));
    user.setLastModifiedAt(System.currentTimeMillis());
    log.debug(String.format("%s", user));
    userRepository.save(user);
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED)
  public void updateMustChangePassword(long userId, boolean state) {
    if (userId < 1) {
      throw new IllegalArgumentException("Invalid user ID");
    }
    User user = loadUserById(userId);
    if (user == null) {
      throw new UserNotFoundException(userId);
    }
    user.setMustChangePassword(state);
    user.setLastModifiedAt(System.currentTimeMillis());
    userRepository.save(user);
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED)
  public void updateEnabled(long userId, boolean state) {
    if (userId < 1) {
      throw new IllegalArgumentException("Invalid user ID");
    }
    User user = loadUserById(userId);
    if (user == null) {
      throw new UserNotFoundException(userId);
    }
    user.setEnabled(state);
    user.setLastModifiedAt(System.currentTimeMillis());
    userRepository.save(user);
  }

  // TODO: encode password, using PasswordEncoder
  private String encodePassword(String password) {
    return password;
  }

}
