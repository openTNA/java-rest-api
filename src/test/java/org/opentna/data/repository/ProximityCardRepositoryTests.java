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

package org.opentna.data.repository;

import static org.assertj.core.api.Assertions.assertThat;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opentna.data.model.entity.ProximityCard;
import org.opentna.rest.RestApplication;
import org.opentna.rest.configuration.DataConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {RestApplication.class})
@DataJpaTest
@Import(DataConfiguration.class)
@Slf4j
public class ProximityCardRepositoryTests {

  @Autowired
  private ProximityCardRepository repository;

  private ProximityCard card;

  private final String cardSerialNo = "111111111";
  private final String cardDescription = "test card 1";
  private final boolean cardEnabled = true;

  @Test
  public void test() {
    create();
    findBySerialNo();
    update();
    delete();
  }

  private void create() {
    card = new ProximityCard(cardSerialNo, cardDescription, cardEnabled);
    card.setCreatedAt(System.currentTimeMillis());
    card = repository.save(card);
    log.info(String.format("CREATES: %s", card.toString()));
    assertThat(card).hasFieldOrPropertyWithValue("serialNo", cardSerialNo);
    assertThat(card).hasFieldOrPropertyWithValue("description", cardDescription);
    assertThat(card).hasFieldOrPropertyWithValue("enabled", cardEnabled);
    assertThat(card).hasFieldOrProperty("createdAt");
  }

  private void findBySerialNo() {
    ProximityCard entity = repository.findBySerialNo(cardSerialNo).orElse(null);
    log.info(String.format("RETRIEVES: %s", entity.toString()));
    assertThat(entity.getSerialNo()).isEqualTo(cardSerialNo);
    assertThat(entity.getDescription()).isEqualTo(cardDescription);
    assertThat(entity.isEnabled()).isEqualTo(cardEnabled);
  }

  private void update() {
    card.setSerialNo("222222222");
    card.setDescription("new test card 1");
    card.setLastModifiedAt(System.currentTimeMillis());
    card = repository.save(card);
    log.info(String.format("UPDATES: %s", card.toString()));
    assertThat(card.getSerialNo()).isEqualTo("222222222");
    assertThat(card.getDescription()).isEqualTo("new test card 1");
    assertThat(card.isEnabled()).isEqualTo(cardEnabled);
    assertThat(card).hasFieldOrProperty("createdAt");
    assertThat(card).hasFieldOrProperty("lastModifiedAt");
  }

  private void delete() {
    repository.delete(card);
    log.info(String.format("DELETES: %s", card.toString()));
    assertThat(repository.findBySerialNo(cardSerialNo).orElse(null)).isEqualTo(null);
    assertThat(repository.findBySerialNo("222222222").orElse(null)).isEqualTo(null);
  }

}