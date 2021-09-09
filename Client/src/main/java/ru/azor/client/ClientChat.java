package ru.azor.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ru.azor.client.controllers.AuthController;
import ru.azor.client.controllers.ChatController;
import ru.azor.client.model.Network;

import java.io.IOException;

public class ClientChat extends Application {
    public static ClientChat INSTANCE;

    private static final String CHAT_WINDOW_FXML = "chat.fxml";
    private static final String AUTH_DIALOG_FXML = "authDialog.fxml";

    private Stage primaryStage;
    private Stage authStage;
    private FXMLLoader chatWindowLoader;
    private FXMLLoader authLoader;


    @Override
    public void init() {
        INSTANCE = this;
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        initViews();
        getChatStage().show();
        getAuthStage().show();
        getAuthController().initMessageHandler();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public Stage getAuthStage() {
        return authStage;
    }

    private AuthController getAuthController() {
        return authLoader.getController();
    }

    public ChatController getChatController() {
        return chatWindowLoader.getController();
    }

    private void initViews() throws IOException {
        initChatWindow();
        initAuthDialog();
    }

    private void initChatWindow() throws IOException {
        chatWindowLoader = new FXMLLoader();
        chatWindowLoader.setLocation(ClientChat.class.getResource(CHAT_WINDOW_FXML));
        Parent root = chatWindowLoader.load();
        this.primaryStage.setScene(new Scene(root));
    }


    private void initAuthDialog() throws IOException {
        authLoader = new FXMLLoader();
        authLoader.setLocation(ClientChat.class.getResource(AUTH_DIALOG_FXML));
        Parent authDialogPanel = authLoader.load();
        authStage = new Stage();
        authStage.initOwner(primaryStage);
        authStage.initModality(Modality.WINDOW_MODAL);
        authStage.setScene(new Scene(authDialogPanel));
        authStage.setOnCloseRequest(we -> primaryStage.close());

    }

    public static void main(String[] args) {
        Application.launch(args);
    }

    public Stage getChatStage() {
        return primaryStage;
    }

    public void switchToMainChatWindow(String username) {
        getPrimaryStage().setTitle(username);
        getChatController().initMessageHandler();
        getAuthController().close();
        getAuthStage().close();
    }

    @Override
    public void stop() {
        getChatController().closeWriter();
        Network.getInstance().close();
    }
}