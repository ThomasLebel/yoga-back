package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SessionServiceTest {

    @Mock
    SessionRepository sessionRepository;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    SessionService sessionService;

    private Session session;
    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setId(1L);
        user.setFirstName("test@gmail.com");
        user.setFirstName("John");
        user.setLastName("Doe");

        session = new Session();
        session.setId(1L);
        session.setName("Yoga Session");
        session.setDescription("Yoga Description");
        session.setUsers(new ArrayList<>());
    }

    @Test
    @DisplayName("Should create and save session")
    public void create() {
        //Arrange
        when(sessionRepository.save(session)).thenReturn(session);

        //Act
        Session result = sessionService.create(session);

        //Assert
        assertNotNull(result);
        assertEquals(session.getId(), result.getId());
        assertEquals(session.getName(), result.getName());
        verify(sessionRepository, times(1)).save(session);
    }

    @Test
    @DisplayName("Should delete session by ID")
    public void delete() {
        //Arrange
        Long sessionId = 1L;

        //Act
        sessionService.delete(sessionId);

        //Assert
        verify(sessionRepository, times(1)).deleteById(sessionId);
    }

    @Test
    @DisplayName("Should return list of sessions")
    public void findAll() {
        //Arrange
        Session session2 = new Session();
        session2.setId(2L);
        session2.setName("Pilates Session");
        List<Session> sessions = Arrays.asList(session, session2);

        when(sessionRepository.findAll()).thenReturn(sessions);

        //Act
        List<Session> result = sessionService.findAll();

        //Assert
        assertNotNull(result);
        assertEquals(sessions.size(), result.size());
        verify(sessionRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Should return session by id")
    public void findById() {
        //Arrange
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));

        //Act
        Session result = sessionService.getById(1L);

        //Assert
        assertNotNull(result);
        assertEquals(session.getId(), result.getId());
        assertEquals(session.getName(), result.getName());
        verify(sessionRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should update existing session")
    public void update() {
        //Arrange
        Session updatedSession = new Session();
        updatedSession.setName("Updated Yoga Session");
        updatedSession.setDescription("Updated Yoga Description");

        when(sessionRepository.save(updatedSession)).thenReturn(updatedSession);

        //Act
        Session result = sessionService.update(1L, updatedSession);

        //Assert
        assertNotNull(result);
        assertEquals(updatedSession.getName(), result.getName());
        assertEquals(updatedSession.getDescription(), result.getDescription());
        verify(sessionRepository, times(1)).save(updatedSession);

    }

    @Test
    @DisplayName("Should add user to session")
    public void participate() {
        //Arrange
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(sessionRepository.save(session)).thenReturn(session);

        //Act
        sessionService.participate(1L, 1L);

        //Assert
        assertTrue(session.getUsers().contains(user));
        verify(sessionRepository, times(1)).findById(1L);
        verify(sessionRepository, times(1)).save(session);
        verify(userRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("Should throw NotFoundException if session doesn't exist")
    public void participate_session_not_found() {
        //Arrange
        when(sessionRepository.findById(1L)).thenReturn(Optional.empty());
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        //Assert
        assertThrows(NotFoundException.class, () -> sessionService.participate(1L, 1L));
        verify(sessionRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(1L);
        verify(sessionRepository, never()).save(any());

    }

    @Test
    @DisplayName("Should throw NotFoundException if user dont exist")
    public void participate_user_not_found() {
        //Arrange
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        //Assert
        assertThrows(NotFoundException.class, () -> sessionService.participate(1L, 1L));
        verify(sessionRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(1L);
        verify(sessionRepository, never()).save(any());

    }

    @Test
    @DisplayName("Should throw BadRequestException if user already participate")
    public void participate_already_participate() {
        //Arrange
        session.getUsers().add(user);

        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        //Assert
        assertThrows(BadRequestException.class, () -> sessionService.participate(1L, 1L));
        verify(sessionRepository, times(1)).findById(1L);
        verify(userRepository, times(1)).findById(1L);
        verify(sessionRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should remove user from session")
    public void noLongerParticipate() {
        //Arrange
        User otherUser = new User();
        otherUser.setId(2L);
        session.getUsers().add(user);
        session.getUsers().add(otherUser);

        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));

        //Act
        sessionService.noLongerParticipate(1L, 1L);

        //Assert
        assertFalse(session.getUsers().contains(user));
        assertTrue(session.getUsers().size() == 1);
        verify(sessionRepository, times(1)).findById(1L);
        verify(sessionRepository, times(1)).save(session);
    }

    @Test
    @DisplayName("Should throw a NotFoundException if session doesn't exist")
    public void noLongerParticipate_not_found() {
        //Arrange
        when(sessionRepository.findById(1L)).thenReturn(Optional.empty());

        //Assert
        assertThrows(NotFoundException.class, () -> sessionService.noLongerParticipate(1L, 1L));
        verify(sessionRepository, times(1)).findById(1L);
        verify(sessionRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should throw a BadRequestException if user doesn't participate")
    public void noLongerParticipate_bad_request() {
        //Arrange
        when(sessionRepository.findById(1L)).thenReturn(Optional.of(session));

        //Assert
        assertThrows(BadRequestException.class, () -> sessionService.noLongerParticipate(1L, 1L));
        verify(sessionRepository, times(1)).findById(1L);
        verify(sessionRepository, never()).save(any());
    }
}


