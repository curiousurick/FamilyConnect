package org.georgie.security.user;

import org.georgie.persistence.dao.UserRepository;
import org.georgie.persistence.model.Privilege;
import org.georgie.persistence.model.Role;
import org.georgie.persistence.model.User;
import org.georgie.security.LoginAttemptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service("userDetailsService")
@Transactional
public class MyUserDetailsService implements UserDetailsService
{
    @Autowired
    public MyUserDetailsService(UserRepository userRepository,
                                LoginAttemptService loginAttemptService,
                                HttpServletRequest request)
    {
        this.userRepository = userRepository;
        this.loginAttemptService = loginAttemptService;
        this.request = request;
    }

    @Override
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException
    {
        final String ip = getClientIP();
        if (loginAttemptService.isBlocked(ip))
        {
            throw new RuntimeException("blocked");
        }

        try
        {
            final User user = userRepository.findByEmail(email);
            if (user == null)
            {
                throw new UsernameNotFoundException("No user found with username: " + email);
            }

            return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), user.isEnabled(), true, true, true, getAuthorities(user.getRoles()));
        }
        catch (final Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    private final String getClientIP()
    {
        final String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null)
        {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }

    private final Collection<? extends GrantedAuthority> getAuthorities(final Collection<Role> roles)
    {
        return getGrantedAuthorities(getPrivileges(roles));
    }

    // API

    private final List<GrantedAuthority> getGrantedAuthorities(final List<String> privileges)
    {
        final List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        for (final String privilege : privileges)
        {
            authorities.add(new SimpleGrantedAuthority(privilege));
        }
        return authorities;
    }

    // UTIL

    private final List<String> getPrivileges(final Collection<Role> roles)
    {
        final List<String> privileges = new ArrayList<String>();
        final List<Privilege> collection = new ArrayList<Privilege>();
        for (final Role role : roles)
        {
            collection.addAll(role.getPrivileges());
        }
        for (final Privilege item : collection)
        {
            privileges.add(item.getName());
        }

        return privileges;
    }

    private final UserRepository userRepository;
    private final LoginAttemptService loginAttemptService;
    private final HttpServletRequest request;
}