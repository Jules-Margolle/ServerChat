package com.julesmargolle.ServerChat;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;


public class Server implements Runnable
{
    private ServerSocket serverSocket;
    private IOCommands io = new IOCommands();
    private static ArrayList<ClientHandler> clients = new ArrayList<>();


    public Server(int port)
    {
        try
        {
            this.serverSocket = new ServerSocket(port);
            this.io = new IOCommands();
            io.toScreen("Server is running on port [" + port + "]");

        }
        catch(Exception e)
        {
            System.err.println("Error : Server creation failed");
        }

    }

    public void run()
    {
        while(true)
        {
            Socket clientSocket = null;
            try
            {
                clientSocket = serverSocket.accept();
            }
            catch(Exception e)
            {
                System.err.println("Error : Server accept failed");
            }
            if(clientSocket != null)
            {
                io.toScreen("Client " + clientSocket + " connected");

                ClientHandler clientHandler = new ClientHandler(clientSocket);
                Server.clients.add(clientHandler);
                Thread thread = new Thread(clientHandler);
                thread.start();
            }
            
        }
    }
}





