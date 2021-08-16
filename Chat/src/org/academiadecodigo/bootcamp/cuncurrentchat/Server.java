package org.academiadecodigo.bootcamp.cuncurrentchat;

import org.academiadecodigo.bootcamp.cuncurrentchat.client.Client;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private ServerSocket serverSocket;
    private BufferedReader br;
    private PrintWriter pw;
    private List<Socket> userSocketList;

    public Server(int portNumber) {

        userSocketList = new CopyOnWriteArrayList<>();

        try {
            serverSocket = new ServerSocket(portNumber);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void listen() throws IOException {
        while (serverSocket.isBound()) {

            Socket clientSocket = serverSocket.accept();
            System.out.println("Connection accepted");
            userSocketList.add(clientSocket);
            System.out.println("Number of users: " + userSocketList.size());

            ExecutorService cachedPool = Executors.newCachedThreadPool();
            cachedPool.submit(new ServerWorker(clientSocket));
        }
    }


    public static void main(String[] args) {

        Server server = new Server(5051);

        try {
            server.listen();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public class ServerWorker implements Runnable {

        Socket clientSocket;

        public ServerWorker(Socket clientSocket) {
            this.clientSocket = clientSocket;
        }

        public String read(Socket clientSocket) throws IOException {
            br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String string = br.readLine();

            if (string == null) {
                clientSocket.close();
                userSocketList.remove(clientSocket);
                System.out.println("Client left chat.");
                return null;
            }

            System.out.println(string);
            return string;
        }

        public void write(Socket clientSocket, String string) throws IOException {

            pw = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()), true);
            pw.println(string);
        }

        public void broadcast(String string, Socket senderSocket) throws IOException {

            if (string == null) {
                return;
            }

            for (Socket clientSocket : userSocketList) {

                if (clientSocket == senderSocket) {
                    continue;
                }
                write(clientSocket, string);
            }
        }

        @Override
        public void run() {
            try {
                while (!clientSocket.isClosed()) {

                    String string = read(clientSocket);
                    broadcast(string, clientSocket);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
