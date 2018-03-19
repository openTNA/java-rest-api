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

import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opentna.data.model.entity.Role;
import org.opentna.rest.RestApplication;
import org.opentna.rest.configuration.DataConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {RestApplication.class})
@Import(DataConfiguration.class)
@Slf4j
public class RoleServiceTests {

  @Autowired
  private RoleService roleService;

  @Test
  public void test001() {
    // testing for a correct name
    Role role = roleService.createRole(new Role("ROLE_ADMIN", "The role of administrator", true));
    log.info(String.format("%s", role.toString()));
    assertThat(role).hasFieldOrPropertyWithValue("name", "ROLE_ADMIN");
    assertThat(role).hasFieldOrPropertyWithValue("description", "The role of administrator");
    assertThat(role).hasFieldOrPropertyWithValue("enabled", true);
    assertThat(role).hasFieldOrProperty("createdAt");
  }

  @Test(expected = ConstraintViolationException.class)
  public void test002() {
    // testing for an incorrect username
    roleService.createRole(new Role("A", null, true));
  }

  @Test(expected = DataIntegrityViolationException.class)
  public void test003() {
    // testing for a duplicate name
    roleService.createRole(new Role("ROLE_ADMIN", null, true));
  }

  @Test
  public void test004() {
    // testing for load by ID
    Role role = roleService.loadRoleById(1);
    log.info(String.format("Role ID: %s", role.getId()));
    assertThat(role).hasFieldOrPropertyWithValue("name", "ROLE_ADMIN");
  }

  @Test(expected = RoleNotFoundException.class)
  public void test005() {
    // testing for a nonexistent ID
    roleService.loadRoleById(2);
  }

  @Test
  public void test006() {
    // testing for a exists username
    Role role = roleService.loadRoleByName("ROLE_ADMIN");
    log.info(String.format("Role ID: %s", role.getId()));
    assertThat(role).hasFieldOrPropertyWithValue("name", "ROLE_ADMIN");
  }

  @Test(expected = RoleNotFoundException.class)
  public void test007() {
    // testing for a nonexistent username
    roleService.loadRoleByName("ROLE_USER");
  }

  @Test
  public void test008() {
    // testing for partially update role information
    Role original = roleService.loadRoleByName("ROLE_ADMIN");
    log.info(String.format("Original: %s", original));
    Role updateRole = new Role();
    updateRole.setId(original.getId());
    updateRole.setName("ROLE_USER");
    updateRole.setDescription(null);
    updateRole.setEnabled(false);
    updateRole = roleService.update(updateRole);
    log.info(String.format("Update: %s", updateRole));
    assertThat(original.getName()).isNotEqualTo("ROLE_USER");
    assertThat(original.getDescription()).isNotEqualTo(updateRole.getDescription());
    assertThat(original.getLastModifiedAt()).isNotEqualTo(updateRole.getLastModifiedAt());
  }

  @Test
  public void test009() {
    // testing for partially update role information with no change
    Role original = roleService.loadRoleByName("ROLE_ADMIN");
    log.info(String.format("Original: %s", original));
    Role updateRole = roleService.update(original);
    log.info(String.format("Update: %s", updateRole));
    assertThat(original).isEqualTo(updateRole);
  }

}