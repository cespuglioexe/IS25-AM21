package it.polimi.it.galaxytrucker.networking.utils;

public abstract class ServerDetails {
    public final static int RMI_DEFAULT_PORT = 5001;
    public final static int SOCKET_DEFAULT_PORT = 5002;
    public final static String DEFAULT_IP = "localhost";
    public final static String DEFAULT_RMI_NAME = "server";
    public final static int HEARTBEAT_FREQUENCY = 5;
    public final static int CONNECTION_TIMEOUT = 15;
}
