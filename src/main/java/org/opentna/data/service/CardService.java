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
import org.opentna.data.model.entity.ProximityCard;
import org.springframework.validation.annotation.Validated;

/**
 * The card service.
 *
 * @author James Ho
 */
@Validated
public interface CardService {

  /**
   * Creates new proximity card.
   *
   * @param card the proximity card model
   * @return a proximity card record, never null or empty
   */
  public ProximityCard createProximityCard(@Valid ProximityCard card);

  /**
   * Retrieves proximity card with the given proximity card ID.
   *
   * @param proximityCardId the proximity card ID
   * @return a proximity card record
   */
  public ProximityCard loadProximityCardById(long proximityCardId);

  /**
   * Retrieves proximity card with the given serial number.
   *
   * @param serialNo the serial number
   * @return a proximity card record
   */
  public ProximityCard loadProximityCardBySerialNo(String serialNo);

  /**
   * Partially updates proximity card information and unable to update the serial number.
   *
   * @param card the proximity card model
   * @return a proximity card record
   */
  public ProximityCard update(@Valid ProximityCard card);

}
