/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package za.ac.iie.prog5121.quickchatpart2;

/**
 *
 * @author anphewa
 */
/**
 * PROG5121: Part 1 login reused for Part 2 (QuickChat).
 * This class holds registration details and checks login credentials.
 */
public class Login {
    private final String firstName;
    private final String lastName;
    private final String username;
    private final String password;
    private final String cellPhone;

    public Login(String firstName, String lastName, String username,
                 String password, String cellPhone) {
        this.firstName = firstName.trim();
        this.lastName = lastName.trim();
        this.username = username;
        this.password = password;
        this.cellPhone = cellPhone.trim();
    }

    // Username: an underscore, and a maximum of five characters.
    public boolean checkUserName() {
        return !username.isBlank() && username.length() <= 5 && username.contains("_");
    }

    // Password: eight or more characters, uppercase, digit and special character.
    public boolean checkPasswordComplexity() {
        return password.length() >= 8
                && password.matches("(?s).*\\p{Lu}.*")
                && password.matches("(?s).*\\d.*")
                && password.matches("(?s).*[^a-zA-Z0-9\\s].*");
    }

    // A South African mobile number in international format, e.g. +27838968976.
    public boolean checkCellPhoneNumber() {
        return cellPhone.matches("\\+27[6-8][0-9]{8}");
    }

    // The Part 1 registration validation messages.
    public String registerUser() {
        if (!checkUserName()) {
            return "Username is not correctly formatted; please ensure that your username "
                    + "contains an underscore and is no more than five characters in length.";
        }
        if (!checkPasswordComplexity()) {
            return "Password is not correctly formatted; please ensure that the password "
                    + "contains at least eight characters, a capital letter, a number, "
                    + "and a special character.";
        }
        if (!checkCellPhoneNumber()) {
            return "Cell phone number incorrectly formatted or does not contain international code.";
        }
        return "Registration successful.";
    }

    // The user may send messages only if BOTH login fields match registration.
    public boolean loginUser(String enteredUsername, String enteredPassword) {
        return checkUserName() && checkPasswordComplexity() && checkCellPhoneNumber()
                && username.equals(enteredUsername) && password.equals(enteredPassword);
    }

    public String returnLoginStatus(String enteredUsername, String enteredPassword) {
        if (loginUser(enteredUsername, enteredPassword)) {
            return "Welcome " + firstName + ", " + lastName
                    + " it is great to see you again.";
        }
        return "Username or password incorrect, please try again.";
    }
}
