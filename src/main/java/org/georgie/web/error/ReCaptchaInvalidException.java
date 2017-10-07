package org.georgie.web.error;

public final class ReCaptchaInvalidException extends RuntimeException
{

    public ReCaptchaInvalidException()
    {
        super();
    }

    public ReCaptchaInvalidException(final String message, final Throwable cause)
    {
        super(message, cause);
    }

    public ReCaptchaInvalidException(final String message)
    {
        super(message);
    }

    public ReCaptchaInvalidException(final Throwable cause)
    {
        super(cause);
    }
    private static final long serialVersionUID = 5861310537366287163L;

}
