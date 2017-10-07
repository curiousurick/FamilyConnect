package org.georgie.security.google2fa;

import org.springframework.security.web.authentication.WebAuthenticationDetails;

import javax.servlet.http.HttpServletRequest;

public class CustomWebAuthenticationDetails extends WebAuthenticationDetails
{

    public CustomWebAuthenticationDetails(HttpServletRequest request)
    {
        super(request);
    }

    private static final long serialVersionUID = 1L;
}