package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private TeacherService teacherService;

    @Test
    @DisplayName("Should return list of teachers")
    void findAll() {

        //Arrange
        Teacher t1 = new Teacher();
        t1.setId(1L);
        t1.setFirstName("John");
        t1.setLastName("Doe");

        Teacher t2 = new Teacher();
        t2.setId(2L);
        t2.setFirstName("Jane");
        t2.setLastName("Smith");

        when(teacherRepository.findAll()).thenReturn(Arrays.asList(t1, t2));

        //Act
        List<Teacher> teachers = teacherService.findAll();

        //Assert
        assertNotNull(teachers);
        assertThat(teachers).hasSize(2).containsExactly(t1, t2);
    }

    @Test
    @DisplayName("Should return teacher by ID")
    void findById() {

        //Arrange
        Teacher t1 = new Teacher();
        t1.setId(1L);
        t1.setFirstName("John");
        t1.setLastName("Doe");

        when(teacherRepository.findById(1L)).thenReturn(Optional.of(t1));

        //Act
        Teacher teacher = teacherService.findById(1L);

        //Assert
        assertNotNull(teacher);
        assertThat(teacher).isEqualTo(t1);
    }
}
