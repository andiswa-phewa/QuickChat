/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package za.ac.iie.prog5121.quickchatpart2;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author anphewa
 */
public class LoginTest {
    
    // TEST 1: A username with an underscore and 5 characters is valid.
    @Test
    public void testValidUsername() {
        Login user = new Login("Kyle", "Smith", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertTrue(user.checkUserName());
    }

    // TEST 2: A username without an underscore is not valid.
    @Test
    public void testInvalidUsername() {
        Login user = new Login("Kyle", "Smith", "kyle!!!!!!!", "Ch&&sec@ke99!", "+27838968976");
        assertFalse(user.checkUserName());
    }

    // TEST 3: A username longer than 5 characters is not valid.
    @Test
    public void testUsernameTooLong() {
        Login user = new Login("Kyle", "Smith", "kyle_1", "Ch&&sec@ke99!", "+27838968976");
        assertFalse(user.checkUserName());
    }

    // TEST 4: Password contains a capital letter, number and special character.
    @Test
    public void testValidPassword() {
        Login user = new Login("Kyle", "Smith", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertTrue(user.checkPasswordComplexity());
    }

    // TEST 5: An ordinary password does not meet the rules.
    @Test
    public void testInvalidPassword() {
        Login user = new Login("Kyle", "Smith", "kyl_1", "password", "+27838968976");
        assertFalse(user.checkPasswordComplexity());
    }

    // TEST 6: The supplied PoE international cellphone number is accepted.
    @Test
    public void testValidCellNumber() {
        Login user = new Login("Kyle", "Smith", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertTrue(user.checkCellPhoneNumber());
    }

    // TEST 7: A number without +27 is rejected.
    @Test
    public void testInvalidCellNumber() {
        Login user = new Login("Kyle", "Smith", "kyl_1", "Ch&&sec@ke99!", "08966553");
        assertFalse(user.checkCellPhoneNumber());
    }

    // TEST 8: A user with correct information can register.
    @Test
    public void testSuccessfulRegistration() {
        Login user = new Login("Kyle", "Smith", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertEquals("Registration successful.", user.registerUser());
    }

    // TEST 9: Incorrect username produces the required error message.
    @Test
    public void testRegistrationInvalidUsername() {
        Login user = new Login("Kyle", "Smith", "kyle!!!!!!!", "Ch&&sec@ke99!", "+27838968976");
        assertEquals(
            "Username is not correctly formatted; please ensure that your username "
                + "contains an underscore and is no more than five characters in length.",
            user.registerUser());
    }

    // TEST 10: Incorrect password produces the required error message.
    @Test
    public void testRegistrationInvalidPassword() {
        Login user = new Login("Kyle", "Smith", "kyl_1", "password", "+27838968976");
        assertEquals(
            "Password is not correctly formatted; please ensure that the password "
                + "contains at least eight characters, a capital letter, a number, "
                + "and a special character.",
            user.registerUser());
    }

    // TEST 11: Incorrect cellphone number produces the expected error message.
    @Test
    public void testRegistrationInvalidCell() {
        Login user = new Login("Kyle", "Smith", "kyl_1", "Ch&&sec@ke99!", "08966553");
        assertEquals(
            "Cell phone number incorrectly formatted or does not contain international code.",
            user.registerUser());
    }

    // TEST 12: Registered credentials must allow login.
    @Test
    public void testLoginSuccessful() {
        Login user = new Login("Kyle", "Smith", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertTrue(user.loginUser("kyl_1", "Ch&&sec@ke99!"));
    }

    // TEST 13: An incorrect password must reject login.
    @Test
    public void testLoginFailed() {
        Login user = new Login("Kyle", "Smith", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertFalse(user.loginUser("kyl_1", "wrongPassword"));
    }

    // TEST 14: A successful login shows the user's first and last name.
    @Test
    public void testSuccessfulLoginMessage() {
        Login user = new Login("Kyle", "Smith", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertEquals(
            "Welcome Kyle, Smith it is great to see you again.",
            user.returnLoginStatus("kyl_1", "Ch&&sec@ke99!"));
    }

    // TEST 15: A failed login shows the correct error message.
    @Test
    public void testFailedLoginMessage() {
        Login user = new Login("Kyle", "Smith", "kyl_1", "Ch&&sec@ke99!", "+27838968976");
        assertEquals(
            "Username or password incorrect, please try again.",
            user.returnLoginStatus("kyl_1", "wrongPassword"));
    }
}
