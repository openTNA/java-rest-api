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

import java.util.Collection;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.opentna.data.model.entity.Attendance;
import org.opentna.data.model.entity.ProximityCard;
import org.opentna.data.model.entity.User;
import org.opentna.data.repository.AttendanceRepository;
import org.opentna.data.service.RecordNotFoundException;
import org.opentna.data.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * An implementation of the record service.
 *
 * @author James Ho
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
@Slf4j
public class RecordServiceImpl implements RecordService {

  @Autowired
  private AttendanceRepository attendanceRepository;

  @Override
  @Transactional(propagation = Propagation.REQUIRED)
  public Attendance createAttendance(@Valid Attendance attendance) {
    Attendance entity = new Attendance();
    entity.setUser(attendance.getUser());
    entity.setProximityCard(attendance.getProximityCard());
    entity.setLoggedAt(attendance.getLoggedAt());
    entity.setCreatedAt(System.currentTimeMillis());
    log.debug(String.format("%s", entity.toString()));
    return attendanceRepository.save(entity);
  }

  @Override
  public Attendance loadAttendanceById(long attendanceId) {
    return attendanceRepository.findById(attendanceId)
        .orElseThrow(() -> new RecordNotFoundException(attendanceId));
  }

  @Override
  public Collection<Attendance> loadAttendanceByUser(User user) {
    if (user.getId() == null || user.getId() < 1) {
      throw new IllegalArgumentException("Invalid user ID");
    }
    log.debug(String.format("username=%s", user.getUsername()));
    return attendanceRepository.findByUser(user);
  }

  @Override
  public Collection<Attendance> loadAttendanceByProximityCard(ProximityCard card) {
    if (card.getId() == null || card.getId() < 1) {
      throw new IllegalArgumentException("Invalid proximity card ID");
    }
    log.debug(String.format("serilaNo=%s", card.getSerialNo()));
    return attendanceRepository.findByProximityCard(card);
  }

}
