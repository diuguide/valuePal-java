package com.example.valuepaljava.service;


import com.example.valuepaljava.exceptions.InvalidInputException;
import com.example.valuepaljava.models.User;
import com.example.valuepaljava.repos.UserRepository;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.MockitoAnnotations.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    private User testUser;

    @InjectMocks
    private UserService sut;

    @Mock
    private UserRepository mockUserRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Before
    public void setUpTest() {
        openMocks(this);
        testUser = new User();
    }

    @After
    public void tearDownTest() {
        mockUserRepository = null;
        passwordEncoder = null;
        testUser = null;
    }

    @Test(expected = InvalidInputException.class)
    public void testFirstNameInvalid() {
        testUser.setFirstName("Everett123");
        sut.isNameValid(testUser.getFirstName());
    }

    @Test(expected= InvalidInputException.class)
    public void testUserInvalid() {
        testUser.setUsername("ediuguid12@!");
        sut.isUsernameValid(testUser.getUsername());
    }

    @Test(expected=InvalidInputException.class)
    public void testInvalidPassword() {
        testUser.setPassword("password");
        sut.isPasswordValid(testUser.getPassword());
    }

    @Test(expected=InvalidInputException.class)
    public void testInvalidEmail() {
        testUser.setEmail("ediuguid@hello");
        sut.isEmailValid(testUser.getEmail());
    }

    @Test
    public void validateUser() {
        User fullUser = new User(1, "Everett", "ediuguid", "Password1!", "ediuguid@gmail.com", "", "ROLE_STUDENT");
        when(mockUserRepository.findUserByUsername(anyString())).thenReturn(Optional.empty());
        when(mockUserRepository.findUserByEmail(anyString())).thenReturn(Optional.empty());
        assertTrue(sut.validateUser(fullUser));
    }

    @Test(expected = InvalidInputException.class)
    public void isUsernameUnavailableTest() {
        testUser.setUsername("ediuguid");
        when(mockUserRepository.findUserByUsername(anyString())).thenReturn(Optional.of(testUser));
        sut.isUsernameAvailable(testUser.getUsername());
    }

    @Test(expected = InvalidInputException.class)
    public void emailUnavailableTest() {
        testUser.setEmail("frank.diuguid@gmail.com");
        when(mockUserRepository.findUserByEmail(anyString())).thenReturn(Optional.of(testUser));
        sut.isEmailAvailable(testUser.getEmail());
    }

    @Test
    public void saveValidUserTest() {
        User fullUser = new User(1, "Everett", "ediuguid", "Password1!", "ediuguid@gmail.com", "", "ROLE_STUDENT");
        when(mockUserRepository.findUserByUsername(anyString())).thenReturn(Optional.empty());
        when(mockUserRepository.findUserByEmail(anyString())).thenReturn(Optional.empty());
        when(mockUserRepository.save(any())).thenReturn(fullUser);
        assertEquals(fullUser, sut.saveUser(fullUser));
    }

    @Test(expected = InvalidInputException.class)
    public void saveInvalidUserTest() {
        User fullUser = new User(1, "Everett!", "ediuguid", "password", "ediuguid@gmail.com", "", "ROLE_STUDENT");
        when(mockUserRepository.findUserByUsername(anyString())).thenReturn(Optional.empty());
        when(mockUserRepository.findUserByEmail(anyString())).thenReturn(Optional.empty());
        sut.saveUser(fullUser);
    }

}

