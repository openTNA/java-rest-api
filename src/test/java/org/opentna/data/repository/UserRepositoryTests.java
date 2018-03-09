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

import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.opentna.data.model.entity.User;
import org.opentna.rest.RestApplication;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {RestApplication.class})
@DataJpaTest
@Slf4j
public class UserRepositoryTests {

  @MockBean
  private UserRepository repository;

  @Before
  public void setUp() {
    User user = new User("james", "123456", false, true, null, null);
    Mockito.when(repository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));
    Mockito.when(repository.findAll()).thenReturn(Arrays.asList(user));
  }

  @Test
  public void count() {
    Collection<User> users = repository.findAll();
    assertThat(users.size()).isEqualTo(1);
  }

  @Test
  public void findByUsername() {
    User entity = repository.findByUsername("james").orElse(null);

    log.info(String.format("%s", entity.toString()));

    assertThat(entity.getUsername()).isEqualTo("james");
    assertThat(entity.getPassword()).isEqualTo("123456");
    assertThat(entity.isMustChangePassword()).isEqualTo(false);
    assertThat(entity.isEnabled()).isEqualTo(true);
  }

}