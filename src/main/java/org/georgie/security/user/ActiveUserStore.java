package org.georgie.security.user;

import java.util.ArrayList;
import java.util.List;

public class ActiveUserStore
{
    public ActiveUserStore()
    {
        users = new ArrayList<String>();
    }

    public List<String> getUsers()
    {
        return users;
    }

    public void setUsers(List<String> users)
    {
        this.users = users;
    }

    public List<String> users;
}
