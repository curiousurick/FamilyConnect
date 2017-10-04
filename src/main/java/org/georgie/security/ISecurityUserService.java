package org.georgie.security;

public interface ISecurityUserService {

    String validatePasswordResetToken(long id, String token);

}
