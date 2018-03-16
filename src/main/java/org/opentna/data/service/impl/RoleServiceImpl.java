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

import java.util.Optional;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.opentna.data.model.entity.Role;
import org.opentna.data.repository.RoleRepository;
import org.opentna.data.service.RoleNotFoundException;
import org.opentna.data.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * An implementation of the role service.
 *
 * @author James Ho
 */
@Service
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
@Slf4j
public class RoleServiceImpl implements RoleService {

  @Autowired
  private RoleRepository roleRepository;

  @Override
  @Transactional(propagation = Propagation.REQUIRED)
  public Role createRole(@Valid Role role) {
    Role entity = new Role();
    entity.setName(role.getName());
    entity.setDescription(role.getDescription());
    entity.setEnabled(role.isEnabled());
    entity.setCreatedAt(System.currentTimeMillis());
    log.debug(String.format("%s", entity.toString()));
    return roleRepository.save(entity);
  }

  @Override
  public Role loadRoleById(long roleId) {
    return roleRepository.findById(roleId).orElseThrow(() -> new RoleNotFoundException(roleId));
  }

  @Override
  public Role loadRoleByName(String name) {
    return roleRepository.findByName(name).orElseThrow(() -> new RoleNotFoundException(name));
  }

  @Override
  @Transactional(propagation = Propagation.REQUIRED)
  public Role update(@Valid Role role) {
    if (role.getId() == null) {
      throw new IllegalArgumentException("The role ID is required");
    }
    if (role.getId() < 1) {
      throw new IllegalArgumentException("Invalid role ID");
    }
    boolean updates = false;
    Role entity = loadRoleById(role.getId());
    if (!Optional.ofNullable(entity.getDescription())
        .equals(Optional.ofNullable(role.getDescription()))) {
      entity.setDescription(role.getDescription());
      updates = true;
    }
    if (entity.isEnabled() != role.isEnabled()) {
      entity.setEnabled(role.isEnabled());
      updates = true;
    }
    if (updates) {
      entity.setLastModifiedAt(System.currentTimeMillis());
      entity = roleRepository.save(entity);
      log.debug("Save");
    } else {
      log.debug("No changes");
    }
    return entity;
  }

}
