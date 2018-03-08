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

import java.util.Optional;
import org.opentna.data.model.entity.ProximityCard;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * The repository interface for {@link ProximityCard} instances.
 *
 * @author James Ho
 */
public interface ProximityCardRepository extends JpaRepository<ProximityCard, Long> {

  public Optional<ProximityCard> findBySerialNo(String serialNo);

}
