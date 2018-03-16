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
 * The card not found exception.
 *
 * @author James Ho
 */
public class CardNotFoundException extends RuntimeException {

  public CardNotFoundException(long cardId) {
    super(String.format("Could not find card '%s'", cardId));
  }

  public CardNotFoundException(String serialNo) {
    super(String.format("Could not find card '%s'", serialNo));
  }

}
