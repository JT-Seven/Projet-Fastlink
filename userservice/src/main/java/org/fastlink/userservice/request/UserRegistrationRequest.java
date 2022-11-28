package org.fastlink.userservice.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationRequest
{
    String email;
    String firstName;
    String lastName;
    String username;
    String password;

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (!(o instanceof UserRegistrationRequest)) return false;
        UserRegistrationRequest that = (UserRegistrationRequest) o;
        return email.equals(that.email) && firstName.equals(that.firstName) && lastName.equals(that.lastName) && username.equals(that.username) && password.equals(that.password);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(email, firstName, lastName, username, password);
    }

    @Override
    public String toString()
    {
        return "UserRegistrationRequest{" + "email='" + email + '\'' + ", firstName='" + firstName + '\'' + ", lastName='" + lastName + '\'' + ", username='" + username + '\'' + ", password='" + password + '\'' + '}';
    }

}
