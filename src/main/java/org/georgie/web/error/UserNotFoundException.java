package org.georgie.web.error;

public final class UserNotFoundException extends RuntimeException
{

    public UserNotFoundException()
    {
        super();
    }

    public UserNotFoundException(final String message, final Throwable cause)
    {
        super(message, cause);
    }

    public UserNotFoundException(final String message)
    {
        super(message);
    }

    public UserNotFoundException(final Throwable cause)
    {
        super(cause);
    }
    private static final long serialVersionUID = 5861310537366287163L;

}
