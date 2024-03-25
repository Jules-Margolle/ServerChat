package com.julesmargolle.ServerChat;

 
public class App 
{
    public static void main( String[] args )
    {
        Server server = new Server(8585);
        server.run();
    }
}
