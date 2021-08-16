package org.academiadecodigo.bootcamp.cuncurrentchat.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ReadFromServerPrintToConsole implements Runnable{

    Socket clientSocket;

    public ReadFromServerPrintToConsole(Socket clientSocket) {
        this.clientSocket = clientSocket;
    }
    public void readFromServerPrintToConsole() throws IOException {

        BufferedReader serverReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        String message = serverReader.readLine();

        if (message == null) {
            clientSocket.close();
            System.out.println("Server closed");
            return;
        }

        System.out.println(message);
    }
    @Override
    public void run() {
        while(!clientSocket.isClosed()) {
            try {
                readFromServerPrintToConsole();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
