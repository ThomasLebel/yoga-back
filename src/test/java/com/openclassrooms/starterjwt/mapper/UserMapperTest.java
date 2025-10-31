package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.UserDto;
import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UserMapperTest {

    @Autowired
    private UserMapper userMapper;

    @Test
    void shouldMapUserToDto() {
        User user = new User();
        user.setId(1L);
        user.setEmail("john@gmail.com");
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setPassword("1234");
        user.setAdmin(true);

        UserDto dto = userMapper.toDto(user);

        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(user.getId());
        assertThat(dto.getEmail()).isEqualTo(user.getEmail());
        assertThat(dto.getFirstName()).isEqualTo(user.getFirstName());
        assertThat(dto.getLastName()).isEqualTo(user.getLastName());
    }

    @Test
    void shouldMapDtoToUser() {
        UserDto dto = new UserDto();
        dto.setId(2L);
        dto.setEmail("jane@gmail.com");
        dto.setFirstName("jane");
        dto.setLastName("doe");
        dto.setPassword("test1234");
        dto.setAdmin(false);

        User user = userMapper.toEntity(dto);

        assertThat(user).isNotNull();
        assertThat(user.getId()).isEqualTo(dto.getId());
        assertThat(user.getEmail()).isEqualTo(dto.getEmail());
        assertThat(user.getFirstName()).isEqualTo(dto.getFirstName());
        assertThat(user.getLastName()).isEqualTo(dto.getLastName());
    }

    @Test
    void shouldMapUsersToDtos() {
        User u1 = new User();
        u1.setId(1L);
        u1.setEmail("john@gmail.com");
        u1.setFirstName("John");
        u1.setLastName("Doe");
        u1.setPassword("1234");
        u1.setAdmin(true);

        User u2 = new User();
        u2.setId(2L);
        u2.setEmail("jane@gmail.com");
        u2.setFirstName("jane");
        u2.setLastName("doe");
        u2.setPassword("test1234");
        u2.setAdmin(false);

        List<UserDto> dtos = userMapper.toDto(java.util.Arrays.asList(u1, u2));

        assertThat(dtos).hasSize(2);
        assertThat(dtos.get(0).getId()).isEqualTo(1L);
        assertThat(dtos.get(1).getEmail()).isEqualTo("jane@gmail.com");
    }

    @Test
    void shouldMapDtosToUsers() {
        UserDto d1 = new UserDto();
        d1.setId(1L);
        d1.setEmail("john@gmail.com");
        d1.setFirstName("John");
        d1.setLastName("Doe");
        d1.setPassword("1234");
        d1.setAdmin(true);

        UserDto d2 = new UserDto();
        d2.setId(2L);
        d2.setEmail("jane@gmail.com");
        d2.setFirstName("jane");
        d2.setLastName("doe");
        d2.setPassword("test1234");
        d2.setAdmin(false);

        List<User> users = userMapper.toEntity(java.util.Arrays.asList(d1, d2));

        assertThat(users).hasSize(2);
        assertThat(users.get(0).getId()).isEqualTo(1L);
        assertThat(users.get(1).getEmail()).isEqualTo("jane@gmail.com");
    }
}