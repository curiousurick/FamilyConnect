package org.georgie.security.user;

public interface ISecurityUserService
{

    String validatePasswordResetToken(long id, String token);

}
