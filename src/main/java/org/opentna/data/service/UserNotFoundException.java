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

/**
 * The user not found exception.
 *
 * @author James Ho
 */
public class UserNotFoundException extends RuntimeException {

  public UserNotFoundException(long userId) {
    super(String.format("Could not find user '%s'", userId));
  }

  public UserNotFoundException(String username) {
    super(String.format("Could not find user '%s'", username));
  }

}
