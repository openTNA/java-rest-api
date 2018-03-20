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

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Type;
import org.opentna.data.model.BaseEntity;

/**
 * Model for proximity cards.
 *
 * @author James Ho
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity
@Table(name = "proximity_cards", uniqueConstraints = @UniqueConstraint(columnNames = "serial_no"))
public class ProximityCard extends BaseEntity {

  @NotNull
  @JsonProperty("serial_no")
  @Size(min = 1, max = 64)
  @Column(name = "serial_no", unique = true, nullable = false, length = 64)
  private String serialNo;

  @Type(type = "text")
  @Column(name = "description")
  private String description;

  @Type(type = "org.hibernate.type.BooleanType")
  @Column(name = "is_active", nullable = false)
  private boolean enabled;

}
