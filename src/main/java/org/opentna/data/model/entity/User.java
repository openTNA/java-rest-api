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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
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
 * Model for users.
 *
 * @author James Ho
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
@JsonIgnoreProperties(value = {"password"})
public class User extends BaseEntity {

  @NotNull
  @Size(min = 2, message = "minimum of 2 characters")
  @Column(name = "name", unique = true, nullable = false, length = 32)
  private String username;

  @NotNull
  @Column(name = "secret", nullable = false, length = 512)
  private String password;

  @Type(type = "org.hibernate.type.BooleanType")
  @Column(name = "must_change_secret", nullable = false)
  private boolean mustChangePassword;

  @Type(type = "org.hibernate.type.BooleanType")
  @Column(name = "is_active", nullable = false)
  private boolean enabled;

  @ManyToMany(targetEntity = Role.class, cascade = CascadeType.ALL)
  @JoinTable(
      name = "related_users_roles",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "role_id"),
      uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "role_id"})
  )
  private Set<Role> roles;

  @ManyToMany(targetEntity = ProximityCard.class, cascade = CascadeType.ALL)
  @JoinTable(
      name = "related_users_proximity_cards",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "card_id", unique = true),
      uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "card_id"})
  )
  private Set<ProximityCard> proximityCards;

}
