package com.julesmargolle.ServerChat;
import java.net.Socket;

class ClientHandler implements Runnable 
{
    private Socket clientSocket;
    private IOCommands io;
    private String clientName;
    

    public ClientHandler(Socket clientSocket) 
    {
        this.clientSocket = clientSocket;
        this.io = new IOCommands(clientSocket);
   
    }

    public void run() 
    {
        io.toNetwork("Welcome to the server! Please enter your name :");
        String input = io.fromNetwork();
        
        while(!getUserName(input));

        inputHandler(input);
    }

    void inputHandler(String input)
    {
        if(checkInput(input))
        {
            if(input.startsWith("SEND:"))
            {
                sendAll(input.substring(5));
            }
            else if(input.startsWith("SEND <"))
            {
                sendToOne(input.substring(6, input.indexOf(">")), input.substring(input.indexOf(":")+1));
            }
            else if(input.equals("MESSAGES"))
            {
                
            }


        }
    }

    boolean getUserName(String input)
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
        else if(input.startsWith("USERS:"))
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    void sendMessage(String message)
    {
        
    }

}
