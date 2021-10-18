package com.example.valuepaljava.ServiceTest;


import com.example.valuepaljava.exceptions.InvalidInputException;
import com.example.valuepaljava.models.User;
import com.example.valuepaljava.repos.UserRepository;
import com.example.valuepaljava.service.UserService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.Assert.assertTrue;
import static org.mockito.MockitoAnnotations.openMocks;

public class UserServiceTest {

    private User testUser;

    @InjectMocks
    private UserService sut;

    @Mock
    private UserRepository mockUserRepository;

    @Before
    public void setUpTest() {
        openMocks(this);
        testUser = new User();
    }

    @After
    public void tearDownTest() {
        mockUserRepository = null;
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
        User fullUser = new User(1, "Everett", "ediuguid", "Password1!", "ediuguid@gmail.com", "");
        assertTrue(sut.validateUser(fullUser));
    }



}

