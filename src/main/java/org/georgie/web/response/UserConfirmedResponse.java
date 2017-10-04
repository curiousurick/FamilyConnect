package org.georgie.web.response;

public class UserConfirmedResponse
{
    public UserConfirmedResponse(boolean confirmed)
    {
        this.confirmed = confirmed;
    }

    public boolean isConfirmed()
    {
        return confirmed;
    }

    public void setConfirmed(boolean confirmed)
    {
        this.confirmed = confirmed;
    }

    private boolean confirmed;
}
