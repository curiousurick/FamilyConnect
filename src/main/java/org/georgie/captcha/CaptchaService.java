package org.georgie.captcha;

import org.georgie.web.error.ReCaptchaInvalidException;
import org.georgie.web.error.ReCaptchaUnavailableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestOperations;

import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.util.regex.Pattern;

@Service("captchaService")
public class CaptchaService implements ICaptchaService
{

    @Autowired
    public CaptchaService(HttpServletRequest request,
                          CaptchaSettings captchaSettings,
                          ReCaptchaAttemptService reCaptchaAttemptService,
                          RestOperations restOperations)
    {
        this.request = request;
        this.captchaSettings = captchaSettings;
        this.reCaptchaAttemptService = reCaptchaAttemptService;
        this.restTemplate = restOperations;
    }

    @Override
    public void processResponse(final String response)
    {
        logger.debug("Attempting to validate response {}", response);

        if (reCaptchaAttemptService.isBlocked(getClientIP()))
        {
            throw new ReCaptchaInvalidException("Client exceeded maximum number of failed attempts");
        }

        if (!responseSanityCheck(response))
        {
            throw new ReCaptchaInvalidException("Response contains invalid characters");
        }

        final URI verifyUri = URI.create(String.format("https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s&remoteip=%s", getReCaptchaSecret(), response, getClientIP()));
        try
        {
            final GoogleResponse googleResponse = restTemplate.getForObject(verifyUri, GoogleResponse.class);
            logger.debug("Google's response: {} ", googleResponse.toString());

            if (!googleResponse.isSuccess())
            {
                if (googleResponse.hasClientError())
                {
                    reCaptchaAttemptService.reCaptchaFailed(getClientIP());
                }
                throw new ReCaptchaInvalidException("reCaptcha was not successfully validated");
            }
        }
        catch (RestClientException rce)
        {
            throw new ReCaptchaUnavailableException("Registration unavailable at this time.  Please try again later.", rce);
        }
        reCaptchaAttemptService.reCaptchaSucceeded(getClientIP());
    }

    @Override
    public String getReCaptchaSite()
    {
        return captchaSettings.getSite();
    }

    @Override
    public String getReCaptchaSecret()
    {
        return captchaSettings.getSecret();
    }

    private String getClientIP()
    {
        final String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null)
        {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }

    private boolean responseSanityCheck(final String response)
    {
        return StringUtils.hasLength(response) && RESPONSE_PATTERN.matcher(response).matches();
    }
    private final static Logger logger = LoggerFactory.getLogger(CaptchaService.class);
    private static final Pattern RESPONSE_PATTERN = Pattern.compile("[A-Za-z0-9_-]+");
    private final HttpServletRequest request;
    private final CaptchaSettings captchaSettings;
    private final ReCaptchaAttemptService reCaptchaAttemptService;
    private final RestOperations restTemplate;
}
