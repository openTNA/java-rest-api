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

package org.opentna.data.model.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Model for attendance records.
 *
 * @author James Ho
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(
    name = "attendance_records",
    uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "card_id", "logged_unix"})
)
public class Attendance implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", unique = true, nullable = false)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "user_id")
  @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
  @JsonIdentityReference(alwaysAsId = true)
  private User user;

  @ManyToOne
  @JoinColumn(name = "card_id", nullable = false)
  @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "serialNo")
  @JsonIdentityReference(alwaysAsId = true)
  private ProximityCard proximityCard;

  @JsonProperty("logged")
  @Column(name = "logged_unix", nullable = false)
  private Long loggedAt;

  @JsonProperty("created")
  @Column(name = "created_unix", nullable = false)
  private Long createdAt;

}
