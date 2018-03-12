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
import org.opentna.data.model.entity.Role;
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
public class RoleRepositoryTests {

  private final String roleName = "USER";
  private final String roleDescription = "user role";
  private final boolean roleEnabled = true;
  @Autowired
  private RoleRepository repository;
  private Role role;

  @Test
  public void test() {
    create();
    findByName();
    update();
    delete();
  }

  private void create() {
    role = new Role(roleName, roleDescription, roleEnabled);
    role.setCreatedAt(System.currentTimeMillis());
    role = repository.save(role);
    log.info(String.format("CREATES: %s", role.toString()));
    assertThat(role).hasFieldOrPropertyWithValue("name", roleName);
    assertThat(role).hasFieldOrPropertyWithValue("description", roleDescription);
    assertThat(role).hasFieldOrProperty("createdAt");
  }

  private void findByName() {
    Role entity = repository.findByName(roleName).orElse(null);
    log.info(String.format("RETRIEVES: %s", entity.toString()));
    assertThat(entity.getName()).isEqualTo(roleName);
    assertThat(entity.getDescription()).isEqualTo(roleDescription);
    assertThat(entity.isEnabled()).isEqualTo(roleEnabled);
  }

  private void update() {
    role.setName("ADMIN");
    role.setLastModifiedAt(System.currentTimeMillis());
    role = repository.save(role);
    log.info(String.format("UPDATES: %s", role.toString()));
    assertThat(role.getName()).isEqualTo("ADMIN");
    assertThat(role.getDescription()).isEqualTo(roleDescription);
    assertThat(role.isEnabled()).isEqualTo(roleEnabled);
    assertThat(role).hasFieldOrProperty("createdAt");
    assertThat(role).hasFieldOrProperty("lastModifiedAt");

  }

  private void delete() {
    repository.delete(role);
    log.info(String.format("DELETES: %s", role.toString()));
    assertThat(repository.findByName(roleName).orElse(null)).isEqualTo(null);
    assertThat(repository.findByName("ADMIN").orElse(null)).isEqualTo(null);
  }

}