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

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opentna.data.model.entity.Attendance;
import org.opentna.data.model.entity.ProximityCard;
import org.opentna.data.model.entity.User;
import org.opentna.rest.RestApplication;
import org.opentna.rest.configuration.DataConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {RestApplication.class})
@Import(DataConfiguration.class)
@Slf4j
public class RecordServiceTests {

  @Autowired
  private CardService cardService;

  @Autowired
  private UserService userService;

  @Autowired
  private RecordService recordService;

  private ProximityCard card;

  private User user;

  @Test
  @Transactional
  public void test001() {
    initial();
    Attendance attendance = addRecord(card, user);
    Attendance result = recordService.loadAttendanceById(1l);
    log.info(String.format("%s", attendance));
    assertThat(attendance).hasFieldOrPropertyWithValue("user", user);
    assertThat(attendance).hasFieldOrPropertyWithValue("proximityCard", card);
    assertThat(attendance).hasFieldOrProperty("loggedAt");
    assertThat(attendance).hasFieldOrProperty("createdAt");
    assertThat(attendance).isEqualTo(result);
  }

  @Test
  @Transactional
  public void test002() {
    initial();
    addRecord(card, user);
    addRecord(card, user);
    addRecord(card, user);
    Collection<Attendance> result1 = recordService.loadAttendanceByUser(user);
    Collection<Attendance> result2 = recordService.loadAttendanceByProximityCard(card);
    assertThat(result1.size()).isEqualTo(result2.size());
    for (Attendance att : result1) {
      log.info(String.format("id=%s, logged=%s, created=%s", att.getId(), att.getLoggedAt(),
          att.getCreatedAt()));
    }
  }

  private void initial() {
    card = new ProximityCard("0000000001", "card #1", true);
    card.setCreatedAt(System.currentTimeMillis());
    log.info(String.format("%s", card));

    user = userService.createUser(
        new User("james", "123456", false, true, null,
            new HashSet<ProximityCard>(Arrays.asList(card))));
    log.info(String.format("%s", user));
  }

  private Attendance addRecord(ProximityCard card, User user) {
    try {
      Thread.sleep(100);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    Attendance attendance = new Attendance();
    attendance.setLoggedAt(System.currentTimeMillis());
    attendance.setProximityCard(card);
    attendance.setUser(user);
    return recordService.createAttendance(attendance);
  }

}