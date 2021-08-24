package server;

import clientServer.Command;
import clientServer.CommandType;
import clientServer.commands.AuthCommandData;
import clientServer.commands.PrivateMessageCommandData;
import clientServer.commands.PublicMessageCommandData;
import clientServer.commands.UpdateDatabaseCommandData;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

public class ClientHandler {

    private final MyServer server;
    private final Socket clientSocket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private String username;

    public ClientHandler(MyServer server, Socket clientSocket) {
        this.server = server;
        this.clientSocket = clientSocket;
    }

    public void handle() throws IOException {
        inputStream = new ObjectInputStream(clientSocket.getInputStream());
        outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
        server.getExecutorService().execute(() -> {
            System.out.println(Thread.currentThread().getName());
            try {
                authentication();
                readMessages();
            } catch (IOException e) {
                System.err.println("Failed to process message from client");
            } finally {
                try {
                    closeConnection();
                } catch (IOException e) {
                    System.err.println("Failed to close connection");
                }
            }
        });
    }

    private void authentication() throws IOException {
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    closeConnection();
                } catch (IOException e) {
                    System.err.println("Failed to close connection");
                }
            }
        };
        Timer timer = new Timer(true);
        timer.schedule(timerTask, 120000);
        while (true) {
            Command command = readCommand();
            if (command == null) {
                continue;
            }
            if (command.getType() == CommandType.AUTH) {
                AuthCommandData data = (AuthCommandData) command.getData();
                String login = data.getLogin();
                String password = data.getPassword();
                String username = server.getAuthService().getUsernameByLoginAndPassword(login, password);
                if (username == null) {
                    sendCommand(Command.errorCommand("Некорректные логин и пароль!"));
                } else if (server.isUsernameBusy(username)) {
                    sendCommand(Command.errorCommand("Такой юзер уже существует!"));
                } else {
                    this.username = username;
                    sendCommand(Command.authOkCommand(username));
                    server.subscribe(this);
                    timer.cancel();
                    return;
                }
            }
        }
    }

    private Command readCommand() throws IOException {
        Command command = null;
        try {
            command = (Command) inputStream.readObject();
        } catch (ClassNotFoundException e) {
            System.err.println("Failed to read chatClientServser.Command class");
            e.printStackTrace();
        }
        return command;
    }

    private void closeConnection() throws IOException {
        server.unsubscribe(this);
        clientSocket.close();
    }

    private void readMessages() throws IOException {
        while (true) {
            Command command = readCommand();
            if (command == null) {
                continue;
            }
            switch (command.getType()) {
                case END:
                    return;
                case PRIVATE_MESSAGE: {
                    PrivateMessageCommandData data = (PrivateMessageCommandData) command.getData();
                    String recipient = data.getReceiver();
                    if (recipient.equals(this.username)){
                        processMessage(data.getMessage());
                    }
                    String privateMessage = data.getMessage();
                    server.sendPrivateMessage(this, recipient, privateMessage);
                    break;
                }
                case PUBLIC_MESSAGE: {
                    PublicMessageCommandData data = (PublicMessageCommandData) command.getData();
                    processMessage(data.getMessage());
                    break;
                }
                case UPDATE_DATABASE: {
                    UpdateDatabaseCommandData data = (UpdateDatabaseCommandData) command.getData();
                    server.getAuthService().changeUsername(data.getNewUsername(), data.getLogin(), data.getPassword());
                    this.username = data.getNewUsername();
                    server.subscribe(this);
                    break;
                }
            }
        }
    }

    private void processMessage(String message) throws IOException {
        server.broadcastMessage(message, this);
    }

    public void sendCommand(Command command) throws IOException {
        outputStream.writeObject(command);
    }

    public String getUsername() {
        return username;
    }
}
