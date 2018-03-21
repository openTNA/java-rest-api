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

package org.opentna.rest.api;

import lombok.extern.slf4j.Slf4j;
import org.opentna.data.service.RoleNotFoundException;
import org.opentna.data.service.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * The REST exception handler.
 *
 * @author James Ho
 */
@RestControllerAdvice(annotations = RestController.class)
@Slf4j
public class RestExceptionHandler {

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<?> handleUserNotFoundException(UserNotFoundException e) {
    log.debug(String.format("%s", e.toString()));
    // TODO: response body
    return new ResponseEntity<String>("{}", HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(RoleNotFoundException.class)
  public ResponseEntity<?> handleRoleNotFoundException(RoleNotFoundException e) {
    log.debug(String.format("%s", e.toString()));
    // TODO: response body
    return new ResponseEntity<String>("{}", HttpStatus.INTERNAL_SERVER_ERROR);
  }

}
