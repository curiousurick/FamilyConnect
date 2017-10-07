package org.georgie.captcha;

import org.georgie.web.error.ReCaptchaInvalidException;

public interface ICaptchaService
{
    void processResponse(final String response) throws ReCaptchaInvalidException;

    String getReCaptchaSite();

    String getReCaptchaSecret();
}
