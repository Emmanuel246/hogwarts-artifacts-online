package com.hogwart.hogwarts_artifacts_online.hogwartsuser;


import com.hogwart.hogwarts_artifacts_online.system.exception.ObjectNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserService userService;

    List<HogwartsUser> hogwartsUsers;

    @BeforeEach
    void setUp() {
        HogwartsUser u1 = new HogwartsUser();
        u1.setId(1);
        u1.setUsername("John");
        u1.setPassword("123456");
        u1.setEnabled(true);
        u1.setRoles("admin user");

        HogwartsUser u2 = new HogwartsUser();
        u2.setId(2);
        u2.setUsername("eric");
        u2.setPassword("654321");
        u2.setEnabled(true);
        u2.setRoles("user");

        HogwartsUser u3 = new HogwartsUser();
        u3.setId(3);
        u3.setUsername("Emmanuel");
        u3.setPassword("qwerty");
        u3.setEnabled(false);
        u3.setRoles("user");

        this.hogwartsUsers = new ArrayList<>();
        this.hogwartsUsers.add(u1);
        this.hogwartsUsers.add(u2);
        this.hogwartsUsers.add(u3);

    }

    @AfterEach
    void tearDown(){

    }

    @Test
    void testFindAllSuccess() {
        //  Given. Arrange inputs and targets. Define the behavior of Mock
        given(this.userRepository.findAll()).willReturn(this.hogwartsUsers);

        // When. Act on the target behavior. Act steps should cover
        List<HogwartsUser> actualUsers = this.userService.findAll();

        // Then. Assert expected outcomes
        assertThat(actualUsers.size()).isEqualTo(this.hogwartsUsers.size());

        // Verify userRepository findAll() is called exactly once
        verify(this.userRepository, times(1)).findAll();
    }



    @Test
    void testFindByIdSuccess() {
        // Given. Arrange inputs and targets. Define the behavior of the Mock Object
        HogwartsUser u = new HogwartsUser();
        u.setId(1);
        u.setUsername("John");
        u.setPassword("123456");
        u.setEnabled(true);
        u.setRoles("admin user");

        given(this.userRepository.findById(1)).willReturn(Optional.of(u));

        // When.
        HogwartsUser returnedUser = this.userService.findById(1);

        // Then Assert expected Outcomes
        assertThat(returnedUser.getId()).isEqualTo(u.getId());
        assertThat(returnedUser.getUsername()).isEqualTo(u.getUsername());
        assertThat(returnedUser.getPassword()).isEqualTo(u.getPassword());
        assertThat(returnedUser.isEnabled()).isEqualTo(u.isEnabled());
        verify(this.userRepository, times(1)).findById(1);

    }

    @Test
    void testFindByIdNotFound() {
        // Given
        given(this.userRepository.findById(Mockito.any(Integer.class))).willReturn(Optional.empty());

        //  When
        Throwable thrown = catchThrowable(() -> {
            HogwartsUser returnedUser = this.userService.findById(1);
        });

        // Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find user with id: 1 :(");

        verify(this.userRepository, times(1)).findById(1);
    }

    @Test
    void testSaveSuccess(){
        // Given
        HogwartsUser newUser = new HogwartsUser();
        newUser.setUsername("Lily");
        newUser.setPassword("123456789");
        newUser.setEnabled(true);
        newUser.setRoles("user");

        given(this.userRepository.save(newUser)).willReturn(newUser);

        // When
        HogwartsUser returnedUser = this.userService.save(newUser);

        // Then
        assertThat(returnedUser.getUsername()).isEqualTo(newUser.getUsername());
        assertThat(returnedUser.getPassword()).isEqualTo(newUser.getPassword());
        assertThat(returnedUser.isEnabled()).isEqualTo(newUser.isEnabled());
        assertThat(returnedUser.getRoles()).isEqualTo(newUser.getRoles());
        verify(this.userRepository, times(1)).save(newUser);

    }

    @Test
    void testUpdateSuccess() {
        // Given
        HogwartsUser oldUser = new HogwartsUser();
        oldUser.setId(1);
        oldUser.setUsername("John");
        oldUser.setPassword("1234567");
        oldUser.setEnabled(true);
        oldUser.setRoles("admin user");

        HogwartsUser update = new HogwartsUser();
        update.setUsername("John - update");
        update.setPassword("123456");
        update.setEnabled(true);
        update.setRoles("admin user");

        given(this.userRepository.findById(1)).willReturn(Optional.of(oldUser));
        given(this.userRepository.save(oldUser)).willReturn(oldUser);

        //When
        HogwartsUser updateUser = this.userService.update(1, update);
        //Then
        assertThat(updateUser.getId()).isEqualTo(1);
        assertThat(updateUser.getUsername()).isEqualTo(updateUser.getUsername());
        verify(this.userRepository,times(1)).findById(1);
        verify(this.userRepository, times(1)).save(oldUser);
    }

    @Test
    void testUpdateNotFound() {
        // Given

        HogwartsUser update = new HogwartsUser();
        update.setUsername("John - update");
        update.setPassword("123456");
        update.setEnabled(true);
        update.setRoles("admin user");

        given(this.userRepository.findById(1)).willReturn(Optional.empty());

        // When
        Throwable thrown = assertThrows(ObjectNotFoundException.class, () -> {
            this.userService.update(1, update);
        });

        // Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find user with id: 1 :(");
        verify(this.userRepository, times(1)).findById(1);
    }

    @Test
    void testDeleteSuccess() {
        // Given
        HogwartsUser user = new HogwartsUser();
        user.setId(1);
        user.setUsername("John");
        user.setPassword("123456");
        user.setRoles("admin user");

        given(this.userRepository.findById(1)).willReturn(Optional.of(user));
        doNothing().when(this.userRepository).deleteById(1);

        // When
        this.userService.delete(1);

        // Then
        verify(this.userRepository, times(1)).deleteById(1);
    }

    @Test
    void testDeleteNotFound(){
       // Given
       given(this.userRepository.findById(1)).willReturn(Optional.empty());

       // When
        Throwable thrown = assertThrows(ObjectNotFoundException.class, () -> {
            this.userService.delete(1);
        });

        // Then
        assertThat(thrown)
                .isInstanceOf(ObjectNotFoundException.class)
                .hasMessage("Could not find user with id: 1 :(");
        verify(this.userRepository,  times(1)).findById(1);
    }




}
