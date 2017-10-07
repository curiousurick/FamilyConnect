package org.georgie.security.facebook;

import org.georgie.persistence.dao.UserRepository;
import org.georgie.persistence.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionSignUp;
import org.springframework.social.connect.UserProfile;
import org.springframework.stereotype.Service;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

@Service
public class FacebookConnectionSignup implements ConnectionSignUp
{

    @Autowired
    public FacebookConnectionSignup(UserRepository userRepository)
    {
        this.userRepository = userRepository;
    }

    @Override
    public String execute(Connection<?> connection)
    {
        logger.debug("Signup from facebook");
        UserProfile profile = connection.fetchUserProfile();
        final User user = new User();
        user.setUsername(connection.getDisplayName());
        user.setEmail(profile.getEmail());
        user.setFirstName(profile.getFirstName());
        user.setLastName(profile.getLastName());
        user.setPassword(randomAlphabetic(8));
        userRepository.save(user);
        return user.getUsername();
    }

    private final UserRepository userRepository;

    private final Logger logger = LoggerFactory.getLogger(getClass());
}
