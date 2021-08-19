package ru.azor.client.controllers;

import clientServer.CommandType;
import clientServer.commands.AuthOkCommandData;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import ru.azor.client.ClientChat;
import ru.azor.client.dialogs.Dialogs;
import ru.azor.client.model.Network;
import ru.azor.client.model.ReadCommandListener;

import java.io.IOException;

public class AuthController {

    public Button authButton;
    @FXML
    private TextField loginField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private ReadCommandListener readMessageListener;

    @FXML
    public void executeAuth() {
        String login = loginField.getText();
        String password = passwordField.getText();
        if (login == null || login.isBlank() || password == null || password.isBlank()) {
            Dialogs.AuthError.EMPTY_CREDENTIALS.show();
            return;
        }

        if (!connectToServer()) {
            Dialogs.NetworkError.SERVER_CONNECT.show();
            return;
        }

        try {
            Network.getInstance().sendAuthMessage(login, password);
            ClientChat.INSTANCE.getChatController().setLogin(login);
            ClientChat.INSTANCE.getChatController().setPassword(password);
        } catch (IOException e) {
            Dialogs.NetworkError.SEND_MESSAGE.show();
            e.printStackTrace();
        }
    }

    private boolean connectToServer() {
        Network network = getNetwork();
        return network.isConnected() || network.connect();
    }

    private Network getNetwork() {
        return Network.getInstance();
    }

    public void initMessageHandler() {
        readMessageListener = getNetwork().addReadMessageListener(command -> {
            if (command.getType() == CommandType.AUTH_OK) {
                AuthOkCommandData data = (AuthOkCommandData) command.getData();
                String username = data.getUsername();
                Platform.runLater(() -> ClientChat.INSTANCE.switchToMainChatWindow(username));
            }
//            else if (command.getType() == CommandType.ERROR) {
//                ErrorCommandData data = (ErrorCommandData) command.getData();
//                System.out.println(data.getErrorMessage());
//                Platform.runLater(() -> new Alert(Alert.AlertType.ERROR, data.getErrorMessage()).showAndWait());
//            }
            else {
                Platform.runLater(Dialogs.AuthError.INVALID_CREDENTIALS::show);
            }
        });
    }

    public void close() {
        getNetwork().removeReadMessageListener(readMessageListener);
    }
}
