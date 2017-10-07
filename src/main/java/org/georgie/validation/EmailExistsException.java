package org.georgie.validation;

@SuppressWarnings("serial")
public class EmailExistsException extends Throwable
{

    public EmailExistsException(final String message)
    {
        super(message);
    }

}
