package ru.azor.client.controllers;

import clientServer.CommandType;
import clientServer.commands.ClientMessageCommandData;
import clientServer.commands.UpdateUsersListCommandData;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import ru.azor.client.ClientChat;
import ru.azor.client.dialogs.Dialogs;
import ru.azor.client.history.HistoryService;
import ru.azor.client.model.Network;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

public class ChatController {
    private static final int LAST_HISTORY_ROWS_NUMBER = 100;
    @FXML
    private Button sendButton;
    @FXML
    private MenuItem reconnect;
    @FXML
    private MenuItem changeNick;
    @FXML
    private TextField newNick;

    @FXML
    private ListView<String> usersList;
    @FXML
    private TextArea chatHistory;
    @FXML
    private TextArea messageTextArea;
    private String login;
    private String password;
    private HistoryService historyService;

    @FXML
    private void sendMessage() {
        String message = messageTextArea.getText().trim();
        if (message.isEmpty()) {
            messageTextArea.clear();
            return;
        }

        String recipient = null;
        if (!usersList.getSelectionModel().isEmpty()) {
            recipient = usersList.getSelectionModel().getSelectedItem();
        }
        try {
            if (recipient != null) {
                Network.getInstance().sendPrivateMessage(recipient, message);
            } else {
                Network.getInstance().sendMessage(message);
            }
        } catch (IOException e) {
            Dialogs.NetworkError.SEND_MESSAGE.show();
        }
        appendMessageToChat("Я", message);
    }

    private void appendMessageToChat(String sender, String message) {
        String curMessage = chatHistory.getText();
        chatHistory.appendText(DateFormat.getDateTimeInstance().format(new Date()));
        chatHistory.appendText(System.lineSeparator());
        if (sender != null) {
            chatHistory.appendText(sender + ":");
            chatHistory.appendText(System.lineSeparator());
        }
        chatHistory.appendText(message);
        chatHistory.appendText(System.lineSeparator());
        chatHistory.appendText(System.lineSeparator());
        messageTextArea.clear();
        String newMessage = chatHistory.getText(curMessage.length(), chatHistory.getLength());
        historyService.keepText(newMessage);
    }

    @FXML
    public void sendTextAreaMessage(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            event.consume();
            if (event.isShiftDown()) {
                messageTextArea.appendText(System.lineSeparator());
            } else {
                sendMessage();
            }
        }
    }

    public void initMessageHandler() {
        Network.getInstance().addReadMessageListener(command -> {
            if (historyService == null){
                historyService = new HistoryService(login);
                historyService.keepHistory();
                chatHistory.setText(historyService.loadLastRows2(LAST_HISTORY_ROWS_NUMBER));
            }
            if (command.getType() == CommandType.CLIENT_MESSAGE) {
                ClientMessageCommandData data = (ClientMessageCommandData) command.getData();
                Platform.runLater(() -> ChatController.this.appendMessageToChat(data.getSender(), data.getMessage()));
            } else if (command.getType() == CommandType.UPDATE_USERS_LIST) {
                UpdateUsersListCommandData data = (UpdateUsersListCommandData) command.getData();
                updateUsersList(data.getUsers());
            }
        });
    }

    public void updateUsersList(List<String> users) {
        Platform.runLater(() -> usersList.setItems(FXCollections.observableArrayList(users)));
    }

    public void reconnectToServer() {
        Network network = Network.getInstance();
        if (!network.isConnected()) {
            network.connect();
            try {
                network.sendAuthMessage(login, password);
            } catch (IOException e) {
                Dialogs.NetworkError.SEND_MESSAGE.show();
                e.printStackTrace();
            }
        }
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void changeUsername() {
        Network network = Network.getInstance();
        String nick = newNick.getText();
        try {
            network.sendNewUsername(nick, login, password);
        } catch (IOException e) {
            Dialogs.NetworkError.SEND_MESSAGE.show();
            e.printStackTrace();
        }
        ClientChat.INSTANCE.getPrimaryStage().setTitle(nick);
    }

    public void closeWriter(){
        if (historyService != null)
        historyService.close();
    }
}