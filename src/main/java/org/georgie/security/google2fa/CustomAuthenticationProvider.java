package org.georgie.security.google2fa;

import org.georgie.persistence.dao.UserRepository;
import org.georgie.persistence.model.User;
import org.jboss.aerogear.security.otp.Totp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

//@Component
public class CustomAuthenticationProvider extends DaoAuthenticationProvider
{

    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException
    {
        final User user = userRepository.findByEmail(auth.getName());
        if ((user == null))
        {
            throw new UsernameNotFoundException("User not found");
        }
        final Authentication result = super.authenticate(auth);
        return new UsernamePasswordAuthenticationToken(user, result.getCredentials(), result.getAuthorities());
    }

    private boolean isValidLong(String code)
    {
        try
        {
            Long.parseLong(code);
        }
        catch (final NumberFormatException e)
        {
            return false;
        }
        return true;
    }

    @Override
    public boolean supports(Class<?> authentication)
    {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    @Autowired
    private UserRepository userRepository;
}
