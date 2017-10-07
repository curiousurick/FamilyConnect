package org.georgie.security.login;

import org.georgie.web.util.UserAgentDetector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Locale;

@Component("authenticationFailureHandler")
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler
{

    @Autowired
    public CustomAuthenticationFailureHandler(MessageSource messageSource,
                                              LocaleResolver localeResolver,
                                              UserAgentDetector userAgentDetector)
    {
        this.messages = messageSource;
        this.localeResolver = localeResolver;
        this.userAgentDetector = userAgentDetector;
    }

    @Override
    public void onAuthenticationFailure(final HttpServletRequest request, final HttpServletResponse response, final AuthenticationException exception) throws IOException, ServletException
    {
        String redirect = userAgentDetector.isMobile(request)
                ? "/user/loginFailure"
                : "/login?error=true";
        setDefaultFailureUrl(redirect);

        super.onAuthenticationFailure(request, response, exception);

        final Locale locale = localeResolver.resolveLocale(request);

        String errorMessage = messages.getMessage("message.badCredentials", null, locale);

        if (exception.getMessage().equalsIgnoreCase("User is disabled"))
        {
            errorMessage = messages.getMessage("auth.message.disabled", null, locale);
        }
        else if (exception.getMessage().equalsIgnoreCase("User account has expired"))
        {
            errorMessage = messages.getMessage("auth.message.expired", null, locale);
        }
        else if (exception.getMessage().equalsIgnoreCase("blocked"))
        {
            errorMessage = messages.getMessage("auth.message.blocked", null, locale);
        }

        request.getSession().setAttribute(WebAttributes.AUTHENTICATION_EXCEPTION, errorMessage);
    }

    private final MessageSource messages;
    private final LocaleResolver localeResolver;
    private final UserAgentDetector userAgentDetector;
}