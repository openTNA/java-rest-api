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

import javax.validation.Valid;
import org.opentna.data.model.entity.Role;
import org.springframework.validation.annotation.Validated;

/**
 * The role service.
 *
 * @author James Ho
 */
@Validated
public interface RoleService {

  /**
   * Creates new role.
   *
   * @param role the role model
   * @return a role record, never null or empty
   */
  public Role createRole(@Valid Role role);

  /**
   * Retrieves role with the given role ID.
   *
   * @param roleId the role ID
   * @return a role record
   */
  public Role loadRoleById(long roleId);

  /**
   * Retrieves role with the given name.
   *
   * @param name the role name
   * @return a role record
   */
  public Role loadRoleByName(String name);

  /**
   * Partially updates role information and unable to update the role name.
   *
   * @param role the role model
   * @return a role record
   */
  public Role update(@Valid Role role);

}
