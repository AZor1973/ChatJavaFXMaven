package clientServer.commands;

import java.io.Serial;
import java.io.Serializable;

public class UpdateDatabaseCommandData implements Serializable {

    @Serial
    private static final long serialVersionUID = 8721829283925755939L;
    private final String newUsername;
    private final String login;
    private final String password;


    public UpdateDatabaseCommandData(String newUsername, String login, String password) {
        this.newUsername = newUsername;
        this.login = login;
        this.password = password;
    }

    public String getNewUsername() {
        return newUsername;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }
}
