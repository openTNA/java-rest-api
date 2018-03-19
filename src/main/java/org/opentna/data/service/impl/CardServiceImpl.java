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

import java.util.Optional;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.opentna.data.model.entity.ProximityCard;
import org.opentna.data.repository.ProximityCardRepository;
import org.opentna.data.service.CardNotFoundException;
import org.opentna.data.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * An implementation of the card service.
 *
 * @author James Ho
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
@Slf4j
public class CardServiceImpl implements CardService {

  @Autowired
  private ProximityCardRepository proximityCardRepository;

  @Override
  @Transactional(propagation = Propagation.REQUIRED)
  public ProximityCard createProximityCard(@Valid ProximityCard card) {
    ProximityCard entity = new ProximityCard();
    entity.setSerialNo(card.getSerialNo());
    entity.setDescription(card.getDescription());
    entity.setEnabled(card.isEnabled());
    entity.setCreatedAt(System.currentTimeMillis());
    log.debug(String.format("%s", entity.toString()));
    return proximityCardRepository.save(entity);
  }

  @Override
  public ProximityCard loadProximityCardById(long proximityCardId) {
    return proximityCardRepository.findById(proximityCardId)
        .orElseThrow(() -> new CardNotFoundException(proximityCardId));
  }

  @Override
  public ProximityCard loadProximityCardBySerialNo(String serialNo) {
    return proximityCardRepository.findBySerialNo(serialNo)
        .orElseThrow(() -> new CardNotFoundException(serialNo));
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED)
  public ProximityCard update(@Valid ProximityCard card) {
    if (card.getId() == null) {
      throw new IllegalArgumentException("The proximity card ID is required");
    }
    if (card.getId() < 1) {
      throw new IllegalArgumentException("Invalid proximity card ID");
    }
    boolean updates = false;
    ProximityCard entity = loadProximityCardById(card.getId());
    if (!Optional.ofNullable(entity.getDescription())
        .equals(Optional.ofNullable(card.getDescription()))) {
      entity.setDescription(card.getDescription());
      updates = true;
    }
    if (entity.isEnabled() != card.isEnabled()) {
      entity.setEnabled(card.isEnabled());
      updates = true;
    }
    if (updates) {
      entity.setLastModifiedAt(System.currentTimeMillis());
      entity = proximityCardRepository.save(entity);
      log.debug("Save");
    } else {
      log.debug("No changes");
    }
    return entity;
  }

}
