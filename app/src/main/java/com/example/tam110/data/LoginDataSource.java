package com.example.tam110.data;

import android.widget.Toast;

import com.example.tam110.data.model.LoggedInUser;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public Result<LoggedInUser> login(String username, String password) {

        try {
            // TODO: handle loggedInUser authentication
            LoggedInUser hardCodedUser = new LoggedInUser(java.util.UUID.randomUUID().toString(),"ZIGA SEBENIK");
            return new Result.Success<>(hardCodedUser);
            /*if(username.equals("a") && password.equals("a"))
                return new Result.Success<>(hardCodedUser);
            else
                return new Result.Error(new Exception());*/
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}
