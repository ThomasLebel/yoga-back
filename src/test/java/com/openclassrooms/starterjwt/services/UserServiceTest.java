package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("Should find user by id when user exists")
    void testFindById() {

        //Arrange
        User user = new User();
        user.setId(1L);
        user.setEmail("test@gmail.com");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        //Act
        User result = userService.findById(1L);

        //Assert
        assertNotNull(result);
        assertEquals("test@gmail.com", result.getEmail());
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should delete user by id")
    void testDelete() {

        //Arrange
        doNothing().when(userRepository).deleteById(1L);

        //Act
        userService.delete(1L);

        //Assert
        verify(userRepository, times(1)).deleteById(1L);
    }
}
