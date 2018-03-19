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

import java.util.Collection;
import javax.validation.Valid;
import org.opentna.data.model.entity.Attendance;
import org.opentna.data.model.entity.ProximityCard;
import org.opentna.data.model.entity.User;
import org.springframework.validation.annotation.Validated;

/**
 * The record service.
 *
 * @author James Ho
 */
@Validated
public interface RecordService {

  /**
   * Creates new attendance record.
   *
   * @param attendance the attendance model
   * @return a attendance record
   */
  public Attendance createAttendance(@Valid Attendance attendance);

  /**
   * Retrieves attendance record with the given attendance ID.
   *
   * @param attendanceId the attendance ID
   * @return a attendance record
   */
  public Attendance loadAttendanceById(long attendanceId);

  /**
   * Retrieves attendance record with the given active user.
   *
   * @param user the active user model
   * @return a collection of attendance record
   */
  public Collection<Attendance> loadAttendanceByUser(@Valid User user);

  /**
   * Retrieves attendance record with the given proximity card.
   *
   * @param card the proximity card model
   * @return a collection of attendance record
   */
  public Collection<Attendance> loadAttendanceByProximityCard(@Valid ProximityCard card);

}
