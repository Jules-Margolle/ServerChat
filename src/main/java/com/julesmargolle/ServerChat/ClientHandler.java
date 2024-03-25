package com.julesmargolle.ServerChat;
import java.net.Socket;
import java.util.ArrayList;

class ClientHandler implements Runnable 
{
    private Socket clientSocket;
    private IOCommands io;
    private String clientName;
    
    private ArrayList<String> messages = new ArrayList<>();

    public ClientHandler(Socket clientSocket) 
    {
        this.clientSocket = clientSocket;
        this.io = new IOCommands(clientSocket);
   
    }

    public void run() 
    {
        io.toNetwork("Welcome to the server! Please enter your name :");
        String input = io.fromNetwork();
        
        while(!askUserName(input));

        while(true)
        {
            input = io.fromNetwork();
            inputHandler(input);
        }
    }

    void inputHandler(String input)
    {
        if(checkInput(input))
        {
            if(input.startsWith("SEND:"))
            {
                io.toNetwork("OK: Message sent");
                Server.sendAll(input.substring(5), this.clientName);
            }
            else if(input.startsWith("SEND <"))
            {
                io.toNetwork("OK: Message sent");
                Server.sendToOne(input.substring(6, input.indexOf(">")), input.substring(input.indexOf(":")+1), this.clientName);
            }
            else if(input.equals("MESSAGES"))
            {
                io.toNetwork("OK:" + this.messages.size() + " messages");
                displayMessages();
            }
            else if(input.equals("USERS"))
            {
                io.toNetwork("OK:" + Server.clients.size() + " users connected");
                displayUsers();
            }
            else if(input.equals("QUIT"))
            {
                io.toNetwork("OK : Bye bye " + this.clientName);
                quitCommand();
            }
        }
        else
        {
            io.toNetwork("Err: Problème de saisie");
        }
    }

    void displayUsers()
    {
        for(ClientHandler client : Server.clients)
        {
            String clientName = client.getClientName();
            this.io.toNetwork(clientName);
        }
    }

    void quitCommand()
    {
        try
        {
            this.clientSocket.close();
        }
        catch(Exception e)
        {
            System.err.println("Error : Client socket close failed");
        }
        
    }

    boolean askUserName(String input)
    {
        if(input.startsWith("USER:"))
        {
            this.clientName = input.substring(5).trim();
            io.toNetwork("OK: Bienvenue " + this.clientName);
            return true;
        }
        else
        {
            io.toNetwork("Err: Problème de saisie");
            return false;
        }
    }

    boolean checkInput(String input)
    {
        if(input.startsWith("SEND:"))
        {
            return true;
        }
        else if(input.startsWith("SEND <") && input.indexOf(">") != -1 && input.indexOf(":") != -1)
        {
            return true;
        }
        else if(input.equals("QUIT"))
        {
            return true;
        }
        else if(input.equals("MESSAGES"))
        {
            return true;
        }
        else if(input.equals("USERS"))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    void displayMessages()
    {
        if(this.messages.size() != 0)
        {
            for(String message : this.messages)
            {
                io.toNetwork(message);
            }
            this.messages.clear();
        }
    }
    
    String getClientName(){return this.clientName;}

    ArrayList<String> getClientMessages(){return this.messages;}
    

    IOCommands getIO(){return this.io;}

}
