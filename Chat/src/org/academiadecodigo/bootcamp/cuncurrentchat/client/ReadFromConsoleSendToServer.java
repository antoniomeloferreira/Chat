package org.academiadecodigo.bootcamp.cuncurrentchat.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ReadFromConsoleSendToServer implements Runnable {

    Socket clientSocket;

    public ReadFromConsoleSendToServer(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }

    public void readFromConsoleSendToServer() throws IOException {

        BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
        String message = consoleReader.readLine();

        PrintWriter serverWrite = new PrintWriter(clientSocket.getOutputStream(), true);
        serverWrite.println(Client.getName() + ": " + message);
    }

    @Override
    public void run() {
        try {
            while (!clientSocket.isClosed()) {
                readFromConsoleSendToServer();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
