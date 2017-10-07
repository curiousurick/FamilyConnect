package org.georgie.web.controller.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.georgie.persistence.model.User;
import org.georgie.web.response.GenericError;
import org.georgie.web.response.LoginResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.WebAttributes;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

@RestController
public class RestLoginController
{
    @Autowired
    public RestLoginController(LocaleResolver localeResolver,
                               @Qualifier(value = "messageSource") MessageSource messages)
    {
        objectMapper = new ObjectMapper();
        this.localeResolver = localeResolver;
        this.messages = messages;
    }

    @RequestMapping(value = "/user/loginSuccess", method = RequestMethod.GET)
    public String loginSuccess(final Authentication authentication) throws JsonProcessingException
    {
        User user = (User) authentication.getPrincipal();
        LoginResponse response = LoginResponse.builder()
                .withId(user.getId())
                .withFirstName(user.getFirstName())
                .withLastName(user.getLastName())
                .withEmail(user.getEmail())
                .withEnabled(user.isEnabled())
                .build();
        return objectMapper.writeValueAsString(response);
    }

    @RequestMapping(value = "/user/loginFailure", method = RequestMethod.GET)
    public String loginFailure(final HttpServletRequest httpRequest,
                               final HttpServletResponse httpResponse) throws JsonProcessingException
    {
        String message = (String) httpRequest
                .getSession()
                .getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        int code;
        Locale locale = localeResolver.resolveLocale(httpRequest);
        if (message.equals(messages.getMessage("auth.message.disabled", null, locale)))
        {
            code = HttpServletResponse.SC_PRECONDITION_FAILED;
        }
        else
        {
            code = HttpServletResponse.SC_UNAUTHORIZED;
        }
        httpResponse.setStatus(code);
        GenericError error = new GenericError(message, code);
        return objectMapper.writeValueAsString(error);
    }
    private final ObjectMapper objectMapper;
    private final LocaleResolver localeResolver;
    private final MessageSource messages;
}
