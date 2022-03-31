package ru.azor.client.model;

public interface ReadCommandListener {

    void processReceivedCommand(Command command);
}