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
import org.opentna.data.model.entity.ProximityCard;
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
public class CardServiceTests {

  @Autowired
  private CardService cardService;

  @Test
  public void test001() {
    // testing for a correct serial number
    ProximityCard card = new ProximityCard("0123456789", "card #1", true);
    card = cardService.createProximityCard(card);
    log.info(String.format("%s", card.toString()));
    assertThat(card).hasFieldOrPropertyWithValue("serialNo", "0123456789");
    assertThat(card).hasFieldOrPropertyWithValue("description", "card #1");
    assertThat(card).hasFieldOrPropertyWithValue("enabled", true);
    assertThat(card).hasFieldOrProperty("createdAt");
  }

  @Test(expected = ConstraintViolationException.class)
  public void test002() {
    // testing for an incorrect serial number
    ProximityCard card = new ProximityCard("", "card #1", true);
    cardService.createProximityCard(card);
  }

  @Test(expected = ConstraintViolationException.class)
  public void test003() {
    // testing for an incorrect serial number
    ProximityCard card = new ProximityCard();
    card.setSerialNo("00000000000000000000000000000000000000000000000000000000000000000");
    cardService.createProximityCard(card);
  }

  @Test(expected = DataIntegrityViolationException.class)
  public void test004() {
    // testing for a duplicate name
    ProximityCard card = new ProximityCard("0123456789", "card #1", true);
    cardService.createProximityCard(card);
  }

  @Test
  public void test005() {
    // testing for load by ID
    ProximityCard card = cardService.loadProximityCardById(1);
    log.info(String.format("Card ID: %s", card.getId()));
    assertThat(card).hasFieldOrPropertyWithValue("serialNo", "0123456789");
  }

  @Test(expected = CardNotFoundException.class)
  public void test006() {
    // testing for a nonexistent ID
    cardService.loadProximityCardById(2);
  }

  @Test
  public void test007() {
    // testing for a exists serial number
    ProximityCard card = cardService.loadProximityCardBySerialNo("0123456789");
    log.info(String.format("Card ID: %s", card.getId()));
    assertThat(card).hasFieldOrPropertyWithValue("serialNo", "0123456789");
  }

  @Test(expected = CardNotFoundException.class)
  public void test008() {
    // testing for a nonexistent serial number
    cardService.loadProximityCardBySerialNo("0000000000");
  }

  @Test
  public void test009() {
    // testing for partially update proximity card information
    ProximityCard original = cardService.loadProximityCardBySerialNo("0123456789");
    log.info(String.format("Original: %s", original));
    ProximityCard card = new ProximityCard();
    card.setId(original.getId());
    card.setSerialNo("0000000001");
    card.setDescription(null);
    card.setEnabled(false);
    card = cardService.update(card);
    log.info(String.format("Update: %s", card));
    assertThat(original.getSerialNo()).isNotEqualTo("0000000001");
    assertThat(original.getDescription()).isNotEqualTo(card.getDescription());
    assertThat(original.getLastModifiedAt()).isNotEqualTo(card.getLastModifiedAt());
  }

  @Test
  public void test010() {
    // testing for partially update proximity card information with no change
    ProximityCard original = cardService.loadProximityCardBySerialNo("0123456789");
    log.info(String.format("Original: %s", original));
    ProximityCard card = cardService.update(original);
    log.info(String.format("Update: %s", card));
    assertThat(original).isEqualTo(card);
  }

}