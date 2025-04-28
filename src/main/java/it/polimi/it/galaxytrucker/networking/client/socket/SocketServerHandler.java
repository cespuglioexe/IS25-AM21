package it.polimi.it.galaxytrucker.networking.client.socket;


import java.io.BufferedWriter;
import java.io.PrintWriter;


/**
 * Questa classe implementa la logica di interazione tra il client e il server.
 */
public class SocketServerHandler implements VirtualServerSocket {
    final PrintWriter output;

    public SocketServerHandler(BufferedWriter output) {
        this.output = new PrintWriter(output);
    }

    // comunicazione dal client al server
    @Override
    public void add(Integer number) {
        output.println("add");
        output.println(number);
        output.flush();
    }

    @Override
    public void reset() {
        output.println("reset");
        output.flush();
    }
}
