package org.georgie.security.login;

import org.georgie.persistence.model.User;
import org.georgie.security.user.ActiveUserStore;
import org.georgie.security.user.LoggedUser;
import org.georgie.web.util.UserAgentDetector;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Component("myAuthenticationSuccessHandler")
public class MyCustomLoginAuthenticationSuccessHandler implements AuthenticationSuccessHandler
{

    @Autowired
    public MyCustomLoginAuthenticationSuccessHandler(ActiveUserStore activeUserStore,
                                                     UserAgentDetector userAgentDetector)
    {
        this.activeUserStore = activeUserStore;
        this.userAgentDetector = userAgentDetector;
    }

    @Override
    public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response, final Authentication authentication) throws IOException
    {
        addWelcomeCookie(gerUserName(authentication), response);
        String redirect = userAgentDetector.isMobile(request)
                ? "/user/loginSuccess"
                : "/homepage.html?user=" + authentication.getName();
        redirectStrategy.sendRedirect(request, response, redirect);

        final HttpSession session = request.getSession(false);
        if (session != null)
        {
            session.setMaxInactiveInterval(30 * 60);
            LoggedUser user = new LoggedUser(authentication.getName(), activeUserStore);
            session.setAttribute("user", user);
        }
        clearAuthenticationAttributes(request);
    }

    private void addWelcomeCookie(final String user, final HttpServletResponse response)
    {
        Cookie welcomeCookie = getWelcomeCookie(user);
        response.addCookie(welcomeCookie);
    }

    private String gerUserName(final Authentication authentication)
    {
        return ((User) authentication.getPrincipal()).getFirstName();
    }

    protected void clearAuthenticationAttributes(final HttpServletRequest request)
    {
        final HttpSession session = request.getSession(false);
        if (session == null)
        {
            return;
        }
        session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }

    private Cookie getWelcomeCookie(final String user)
    {
        Cookie welcomeCookie = new Cookie("welcome", user);
        welcomeCookie.setMaxAge(60 * 60 * 24 * 30); // 30 days
        return welcomeCookie;
    }

    protected RedirectStrategy getRedirectStrategy()
    {
        return redirectStrategy;
    }

    public void setRedirectStrategy(final RedirectStrategy redirectStrategy)
    {
        this.redirectStrategy = redirectStrategy;
    }

    ActiveUserStore activeUserStore;
    private UserAgentDetector userAgentDetector;

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
}