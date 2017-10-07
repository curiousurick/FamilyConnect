package org.georgie.service;

import org.georgie.persistence.model.PasswordResetToken;
import org.georgie.persistence.model.User;
import org.georgie.persistence.model.VerificationToken;
import org.georgie.web.dto.UserDto;
import org.georgie.web.error.UserAlreadyExistException;

import java.io.UnsupportedEncodingException;
import java.util.List;

public interface IUserService
{

    User registerNewUserAccount(UserDto accountDto) throws UserAlreadyExistException;

    User getUser(String verificationToken);

    void saveRegisteredUser(User user);

    void deleteUser(User user);

    void createVerificationTokenForUser(User user, String token);

    VerificationToken getVerificationToken(String VerificationToken);

    VerificationToken generateNewVerificationToken(String token);

    void createPasswordResetTokenForUser(User user, String token);

    User findUserByEmail(String email);

    PasswordResetToken getPasswordResetToken(String token);

    User getUserByPasswordResetToken(String token);

    User getUserByID(long id);

    void changeUserPassword(User user, String password);

    boolean checkIfValidOldPassword(User user, String password);

    String validateVerificationToken(String token);

    String generateQRUrl(User user) throws UnsupportedEncodingException;

    List<String> getUsersFromSessionRegistry();

}
