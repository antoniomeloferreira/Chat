package org.academiadecodigo.bootcamp.cuncurrentchat.client;

import org.academiadecodigo.bootcamp.Prompt;
import org.academiadecodigo.bootcamp.scanners.string.StringInputScanner;

import java.io.IOException;
import java.net.Socket;

public class Client {

    private Prompt prompt;
    public static String NAME;
    private Socket clientSocket;

    public Client(String name) {
        this.NAME = name;
        prompt = new Prompt(System.in, System.out);
    }

    public void setName() {
        StringInputScanner questionName = new StringInputScanner();
        questionName.setMessage("Name? \n");
        NAME = prompt.getUserInput(questionName);
    }

    public static String getName() {
        return NAME;
    }

    public void connect(int port,String localhost) throws IOException {

        clientSocket = new Socket(localhost,port);
        System.out.println("Connected to " + localhost + " and " + port);
    }

    public static void main(String[] args) {

        Client client = new Client("");

        try {
            client.connect(5051,"localhost");
            client.setName();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Thread thread = new Thread(new ReadFromConsoleSendToServer(client.clientSocket));
        thread.start();

        Thread thread1 = new Thread(new ReadFromServerPrintToConsole(client.clientSocket));
        thread1.start();
    }
}

