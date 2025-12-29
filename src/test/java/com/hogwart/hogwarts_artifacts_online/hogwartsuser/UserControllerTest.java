package com.hogwart.hogwarts_artifacts_online.hogwartsuser;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.hogwart.hogwarts_artifacts_online.hogwartsuser.dto.UserDto;
import com.hogwart.hogwarts_artifacts_online.system.StatusCode;
import com.hogwart.hogwarts_artifacts_online.system.exception.ObjectNotFoundException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static java.lang.reflect.Array.get;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
//import static org.assertj.core.api.BDDAssumptions.given;


@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    UserService userService;

    List<HogwartsUser> users;


    @Value("/api/v1")
    String baseUrl;

    @BeforeEach
    void setUp() {
        this.users = new ArrayList<>();

        HogwartsUser u1 = new HogwartsUser();
        u1.setId(1);
        u1.setUsername("John");
        u1.setPassword("1234567");
        u1.setEnabled(true);
        u1.setRoles("admin user");
        this.users.add(u1);

        HogwartsUser u2 = new HogwartsUser();
        u2.setId(2);
        u2.setUsername("Eric");
        u2.setPassword("654321");
        u2.setEnabled(true);
        u2.setRoles("user");
        this.users.add(u2);

        HogwartsUser u3 = new HogwartsUser();
        u3.setId(3);
        u3.setUsername("Emma");
        u3.setPassword("12121212");
        u3.setEnabled(false);
        u3.setRoles("user");
        this.users.add(u3);
    }

    @Test
    void testFindAllUsersSuccess() throws Exception {
        //   Given
        given(this.userService.findAll()).willReturn(this.users);

        // When and then
        this.mockMvc.perform(MockMvcRequestBuilders.get(this.baseUrl + "/users").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find All Success"))
                .andExpect(jsonPath("$.data", Matchers.hasSize(this.users.size())))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].username").value("John"));

    }

    @Test
    void testFindUserByIdSuccess()  throws Exception {
        // Given
        given(this.userService.findById(1)).willReturn(this.users.get(0));

        // When and Then
        this.mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/users/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Find One Success"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.username").value("John"));
    }

    @Test
    void  testFindUserByIdNotFound() throws Exception {
        //  Given
        given(this.userService.findById(1)).willThrow(new ObjectNotFoundException("User",  1));


        // When and Then
        this.mockMvc.perform(MockMvcRequestBuilders.get(this.baseUrl + "/users/1").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(false))
                .andExpect(jsonPath("$.code").value(StatusCode.NOT_FOUND))
                .andExpect(jsonPath("$.message").value("Could not find User with id: 1 :("))
                .andExpect(jsonPath("$.data").isEmpty());
    }


    @Test
    void testAddUserSuccess() throws Exception {
        HogwartsUser user =  new HogwartsUser();
        user.setId(4);
        user.setUsername("lily");
        user.setPassword("123456");
        user.setEnabled(true);
        user.setRoles("admin user");  // Tge delimiter space

        String json = this.objectMapper.writeValueAsString(user);

        user.setId(4);

        // Given, Arrange inputs and targets. Define the behaviour of Mock
        given(this.userService.save(Mockito.any(HogwartsUser.class))).willReturn(user);

        // When and Then
        this.mockMvc.perform(MockMvcRequestBuilders.post(this.baseUrl + "/users").contentType(MediaType.APPLICATION_JSON
                ).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Add Success"))
                .andExpect(jsonPath("$.data.id").isNotEmpty())
                .andExpect(jsonPath("$.data.username").value(user.getUsername()))
                .andExpect(jsonPath("$.data.enabled").value(user.isEnabled()))
                .andExpect(jsonPath("$.data.roles").value(user.getRoles()));
    }

    @Test
    void testUpdateUserSuccess() throws Exception {
        UserDto userDto = new UserDto(3,  "jomi123", false, "user");

        HogwartsUser updatedUser = new HogwartsUser();
        updatedUser.setId(3);
        updatedUser.setUsername("jomi123");
        updatedUser.setEnabled(false);
        updatedUser.setRoles("user");

        String json = this.objectMapper.writeValueAsString(userDto);

        // Given. Arrange inputs and targets. Define the behaviour of Mock
        given(this.userService.update(eq(3), Mockito.any(HogwartsUser.class))).willReturn(updatedUser);

        // When and then
        this.mockMvc.perform(put(this.baseUrl + "/users/3").contentType(MediaType.APPLICATION_JSON).content(json).accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Update Success"))
                .andExpect(jsonPath("$.data.id").value(3))
                .andExpect(jsonPath("$.data.username").value(updatedUser.getUsername()))
                .andExpect(jsonPath("$.data.enabled").value(updatedUser.isEnabled()))
                .andExpect(jsonPath("$.data.roles").value(updatedUser.getRoles()));
    }

    @Test
    void testDeleteUserSuccess() throws Exception {
        // Given
        doNothing().when(this.userService).delete(3);

        // When and Then
        this.mockMvc.perform(delete(this.baseUrl + "/users/3").accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.flag").value(true))
                .andExpect(jsonPath("$.code").value(StatusCode.SUCCESS))
                .andExpect(jsonPath("$.message").value("Delete Success"))
                .andExpect(jsonPath("$.data").isEmpty());




    }


}
