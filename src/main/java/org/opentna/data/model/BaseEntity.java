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

package org.opentna.data.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Base abstract class for entities.
 *
 * @author James Ho
 */
@Data
@MappedSuperclass
public abstract class BaseEntity implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id", unique = true, nullable = false)
  private Long id;

  @JsonProperty("created")
  @Column(name = "created_unix", nullable = false)
  private Long createdAt;

  @JsonProperty("last_modified")
  @Column(name = "modified_unix")
  private Long lastModifiedAt;

  // XXX: creator
  // @Column(name = "creator", nullable = false)
  // private String createdBy;

  // XXX: modifier
  // @Column(name = "modifier")
  // private String lastModifiedBy;

}
