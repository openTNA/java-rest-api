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

package org.opentna.rest.api.users;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opentna.data.model.entity.Role;
import org.opentna.data.model.vo.BaseUserRequest;
import org.opentna.data.model.vo.PasswordRequest;
import org.opentna.data.model.vo.UserRequest;
import org.opentna.data.model.vo.UsernameRequest;
import org.opentna.data.service.RoleService;
import org.opentna.rest.RestApplication;
import org.opentna.rest.configuration.DataConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {RestApplication.class})
@AutoConfigureMockMvc
@Import(DataConfiguration.class)
@Slf4j
public class UserRestControllerTests {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private RoleService roleService;

  private MockHttpServletRequestBuilder builder;

  private ObjectMapper objectMapper;

  @Before
  public void initial() {
    objectMapper = new ObjectMapper();
  }

  @Test
  public void test001() throws Exception {
    UserRequest payload = new UserRequest("james", encodeBase64("123456"));
    payload.setMustChangePassword(false);
    payload.setEnabled(true);
    log.debug(String.format("Payload: %s", payload));

    builder = post("/users")
        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
        .content(objectMapper.writeValueAsString(payload));
    mockMvc.perform(builder).andDo(print()).andExpect(status().isCreated())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(content().json("{'username':'james','enabled':true}"));
  }

  @Test
  public void test002() throws Exception {
    UserRequest payload = new UserRequest("test", encodeBase64("123456"));
    payload.setMustChangePassword(false);
    payload.setEnabled(false);
    log.debug(String.format("Payload: %s", payload));

    builder = post("/users")
        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
        .content(objectMapper.writeValueAsString(payload));
    mockMvc.perform(builder).andDo(print()).andExpect(status().isCreated())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(content().json("{'username':'test','enabled':false}"));
  }

  @Test
  public void test003() throws Exception {
    builder = get("/users/1")
        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    mockMvc.perform(builder).andDo(print()).andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(content().json("{'id':1,'username':'james','enabled':true}"));
  }

  @Test
  public void test004() throws Exception {
    builder = get("/users/2")
        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    mockMvc.perform(builder).andDo(print()).andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(content().json("{'id':2,'username':'test','enabled':false}"));
  }

  @Test
  public void test005() throws Exception {
    builder = get("/users/james/profile")
        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    mockMvc.perform(builder).andDo(print()).andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(content().json("{'username':'james','enabled':true}"));
  }

  @Test
  public void test006() throws Exception {
    builder = get("/users/eric/profile")
        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    mockMvc.perform(builder).andDo(print()).andExpect(status().isInternalServerError());
  }

  @Test
  public void test007() throws Exception {
    builder = get("/users/?page=1&size=5")
        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
    mockMvc.perform(builder).andDo(print()).andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.length()", is(2)));
  }

  @Test
  public void test008() throws Exception {
    UsernameRequest payload = new UsernameRequest("eric");
    builder = patch("/users/1/change/username")
        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
        .content(objectMapper.writeValueAsString(payload));
    mockMvc.perform(builder).andDo(print()).andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(content().json("{'username':'eric'}"));
  }

  @Test
  public void test009() throws Exception {
    UsernameRequest payload = new UsernameRequest("eric");
    builder = patch("/users/1/change/username")
        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
        .content(objectMapper.writeValueAsString(payload));
    mockMvc.perform(builder).andDo(print()).andExpect(status().isAccepted());
  }

  @Test
  public void test010() throws Exception {
    UsernameRequest payload = new UsernameRequest("test");
    builder = patch("/users/1/change/username")
        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
        .content(objectMapper.writeValueAsString(payload));
    mockMvc.perform(builder).andDo(print()).andExpect(status().isConflict());
  }

  @Test
  public void test011() throws Exception {
    PasswordRequest payload = new PasswordRequest("123456", "654321");
    builder = patch("/users/2/change/password")
        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
        .content(objectMapper.writeValueAsString(payload));
    mockMvc.perform(builder).andDo(print()).andExpect(status().isBadRequest());
  }

  @Test
  public void test012() throws Exception {
    PasswordRequest payload = new PasswordRequest(encodeBase64("123456"), encodeBase64("123456"));
    builder = patch("/users/2/change/password")
        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
        .content(objectMapper.writeValueAsString(payload));
    mockMvc.perform(builder).andDo(print()).andExpect(status().isAccepted());
  }

  @Test
  public void test013() throws Exception {
    PasswordRequest payload = new PasswordRequest(encodeBase64("123456"), encodeBase64("654321"));
    builder = patch("/users/2/change/password")
        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
        .content(objectMapper.writeValueAsString(payload));
    mockMvc.perform(builder).andDo(print()).andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(content().json("{'username':'test'}"));
  }

  @Test
  public void test014() throws Exception {
    BaseUserRequest payload = new BaseUserRequest(false, true);
    builder = patch("/users/1/profile")
        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
        .content(objectMapper.writeValueAsString(payload));
    mockMvc.perform(builder).andDo(print()).andExpect(status().isAccepted());
  }

  @Test
  public void test015() throws Exception {
    BaseUserRequest payload = new BaseUserRequest(false, false);
    builder = patch("/users/1/profile")
        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
        .content(objectMapper.writeValueAsString(payload));
    mockMvc.perform(builder).andDo(print()).andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(content().json("{'username':'eric'}"));
  }

  @Test
  public void test016() throws Exception {
    Collection<Long> payload = new ArrayList<Long>(Arrays.asList(1l, 2l));
    builder = put("/users/1/roles")
        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
        .content(objectMapper.writeValueAsString(payload));
    mockMvc.perform(builder).andDo(print()).andExpect(status().isInternalServerError());
  }

  @Test
  public void test017() throws Exception {
    Role role = roleService.createRole(new Role("ROLE_ADMIN", "The role of administrator", true));
    log.info(String.format("%s", role.toString()));

    Collection<Long> payload = new ArrayList<Long>(Arrays.asList(1l));
    builder = put("/users/1/roles")
        .contentType(MediaType.APPLICATION_JSON_UTF8_VALUE)
        .content(objectMapper.writeValueAsString(payload));
    mockMvc.perform(builder).andDo(print()).andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
        .andExpect(jsonPath("$.id", is(1)))
        .andExpect(jsonPath("$.roles[0].id", is(1)))
        .andExpect(jsonPath("$.roles[0].name", is("ROLE_ADMIN")))
        .andExpect(jsonPath("$.roles[0].description", is("The role of administrator")))
        .andExpect(jsonPath("$.roles[0].enabled", is(true)));
  }

  private String encodeBase64(String s) {
    return new String(Base64.getEncoder().encode(s.getBytes()), StandardCharsets.UTF_8);
  }

}