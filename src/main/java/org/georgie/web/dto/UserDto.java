package org.georgie.web.dto;

import org.georgie.validation.PasswordMatches;
import org.georgie.validation.ValidEmail;
import org.georgie.validation.ValidPassword;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@PasswordMatches
public class UserDto
{
    public String getEmail()
    {
        return email;
    }

    public void setEmail(final String email)
    {
        this.email = email;
    }

    public Integer getRole()
    {
        return role;
    }

    public void setRole(final Integer role)
    {
        this.role = role;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(final String firstName)
    {
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(final String lastName)
    {
        this.lastName = lastName;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(final String password)
    {
        this.password = password;
    }

    public String getMatchingPassword()
    {
        return matchingPassword;
    }

    public void setMatchingPassword(final String matchingPassword)
    {
        this.matchingPassword = matchingPassword;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(final String username)
    {
        this.username = username;
    }

    @Override
    public String toString()
    {
        final StringBuilder builder = new StringBuilder();
        builder.append("UserDto [firstName=").append(firstName).append(", lastName=").append(lastName)
                .append(", password=").append(password).append(", matchingPassword=").append(matchingPassword)
                .append(", email=").append(email).append(", username=").append(username)
                .append(", role=").append(role).append("]");
        return builder.toString();
    }
    @NotNull
    @Size(min = 1)
    private String firstName;
    @NotNull
    @Size(min = 1)
    private String lastName;
    @ValidPassword
    private String password;
    @NotNull
    @Size(min = 1)
    private String matchingPassword;
    @ValidEmail
    @NotNull
    @Size(min = 1)
    private String email;
    @NotNull
    @Size(min = 1)
    private String username;
    private Integer role;

}
