package ru.azor.client.model;

import clientServer.Command;

public interface ReadCommandListener {

    void processReceivedCommand(Command command);
}