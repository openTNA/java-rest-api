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

import java.util.Collection;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opentna.data.model.entity.Attendance;
import org.opentna.data.model.entity.ProximityCard;
import org.opentna.data.model.entity.User;
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
public class AttendanceRepositoryTests {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ProximityCardRepository proximityCardRepository;

  @Autowired
  private AttendanceRepository repository;

  private User user;
  private ProximityCard cardOne;
  private ProximityCard cardTwo;
  private Attendance attendanceOne;
  private Attendance attendanceTwo;
  private Attendance attendanceThree;

  private final long attendanceLoggedAt = 1520755633520L;

  @Test
  public void test() {
    create();
    findByUser();
    findByProximityCard();
    update();
    delete();
  }

  private void create() {
    user = new User("james", "123456", false, true, null, null);
    user.setCreatedAt(System.currentTimeMillis());
    user = userRepository.save(user);

    cardOne = new ProximityCard("111111111", "test card 1", true);
    cardOne.setCreatedAt(System.currentTimeMillis());
    cardOne = proximityCardRepository.save(cardOne);

    cardTwo = new ProximityCard("222222222", "test card 2", true);
    cardTwo.setCreatedAt(System.currentTimeMillis());
    cardTwo = proximityCardRepository.save(cardTwo);

    attendanceOne = new Attendance();
    attendanceOne.setUser(user);
    attendanceOne.setProximityCard(cardOne);
    attendanceOne.setLoggedAt(attendanceLoggedAt);
    attendanceOne.setCreatedAt(System.currentTimeMillis());
    attendanceOne = repository.save(attendanceOne);
    log.info(String.format("CREATES: %s", attendanceOne.toString()));

    attendanceTwo = new Attendance();
    attendanceTwo.setProximityCard(cardTwo);
    attendanceTwo.setLoggedAt(attendanceLoggedAt + 1 * 60 * 1000);
    attendanceTwo.setCreatedAt(System.currentTimeMillis());
    attendanceTwo = repository.save(attendanceTwo);
    log.info(String.format("CREATES: %s", attendanceTwo.toString()));

    attendanceThree = new Attendance();
    attendanceThree.setProximityCard(cardTwo);
    attendanceThree.setLoggedAt(attendanceLoggedAt + 2 * 60 * 1000);
    attendanceThree.setCreatedAt(System.currentTimeMillis());
    attendanceThree = repository.save(attendanceThree);
    log.info(String.format("CREATES: %s", attendanceThree.toString()));

    assertThat(attendanceOne).hasFieldOrPropertyWithValue("loggedAt", attendanceLoggedAt);
    assertThat(attendanceOne).hasFieldOrPropertyWithValue("user", user);
    assertThat(attendanceOne).hasFieldOrPropertyWithValue("proximityCard", cardOne);
    assertThat(attendanceOne).hasFieldOrProperty("createdAt");

    assertThat(attendanceTwo).hasFieldOrPropertyWithValue("proximityCard", cardTwo);
    assertThat(attendanceTwo).hasFieldOrProperty("createdAt");

    assertThat(attendanceThree).hasFieldOrPropertyWithValue("proximityCard", cardTwo);
    assertThat(attendanceThree).hasFieldOrProperty("createdAt");
  }

  private void findByUser() {
    Collection<Attendance> entities = repository.findByUser(user);
    log.info(String.format("RETRIEVES: %s", entities.toString()));
    log.info(String.format("RETRIEVES: size=%s", entities.size()));
    assertThat(entities.size()).isEqualTo(1);
  }

  private void findByProximityCard() {
    Collection<Attendance> entities = repository.findByProximityCard(cardTwo);
    log.info(String.format("RETRIEVES: %s", entities.toString()));
    log.info(String.format("RETRIEVES: size=%s", entities.size()));
    assertThat(entities.size()).isEqualTo(2);
  }

  private void update() {
    attendanceOne.setUser(null);
    attendanceOne = repository.save(attendanceOne);
    log.info(String.format("UPDATES: %s", attendanceOne.toString()));
    assertThat(attendanceOne).hasFieldOrPropertyWithValue("loggedAt", attendanceLoggedAt);
    assertThat(attendanceOne.getUser()).isEqualTo(null);
    assertThat(attendanceOne).hasFieldOrPropertyWithValue("proximityCard", cardOne);
    assertThat(attendanceOne).hasFieldOrProperty("createdAt");
  }

  private void delete() {
    repository.delete(attendanceOne);
    log.info(String.format("DELETES: %s", attendanceOne.toString()));
    assertThat(repository.findAll().size()).isEqualTo(2);
    repository.delete(attendanceTwo);
    log.info(String.format("DELETES: %s", attendanceTwo.toString()));
    assertThat(repository.findAll().size()).isEqualTo(1);
    repository.delete(attendanceThree);
    log.info(String.format("DELETES: %s", attendanceThree.toString()));
    assertThat(repository.findAll().size()).isEqualTo(0);
  }

}