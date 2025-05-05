package it.polimi.it.galaxytrucker.networking.client.socket;

import it.polimi.it.galaxytrucker.networking.messages.GameUpdate;
import it.polimi.it.galaxytrucker.networking.server.socket.SocketVirtualView;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * Questa classe rappresenta la logica del client implementata con tecnologia Socket.
 */
public class SocketClient implements SocketVirtualView {
    final BufferedReader input;
    final SocketServerHandler output;

    protected SocketClient(BufferedReader input, BufferedWriter output) {
        this.input = input;
        this.output = new SocketServerHandler(output);
    }

    private void run() {
        new Thread(() -> {
            try {
                runVirtualServer();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();
        runCli();
    }

    // comunicazione dal server al client
    private void runVirtualServer() throws IOException {
        String line;
        while ((line = input.readLine()) != null) {
            // Da notare che i metodi sono chiamati nello stesso modo del server
            switch (line) {
                case "update" -> this.showUpdate(Integer.parseInt(input.readLine()));
                case "error" -> this.reportError(input.readLine());
                default -> System.err.println("[INVALID MESSAGE]");
            }
        }
    }

    private void runCli()  {
        Scanner scan = new Scanner(System.in);
        while (true) {
            System.out.print("> ");
            int command = scan.nextInt();

            if (command == 0) {
                this.output.reset();
            } else {
                this.output.add(command);
            }
        }
    }

    public void showUpdate(Integer number) {
        // TODO. Attenzione, questo può causare data race con il thread dell'interfaccia o un altro thread!
        System.out.print("\n= " + number + "\n> ");
    }

    public void reportError(String details) {
        // TODO. Attenzione, questo può causare data race con il thread dell'interfaccia o un altro thread!
        System.err.print("\n[ERROR] " + details + "\n> ");
    }

    public static void main(String[] args) throws IOException {
        String host = args[0];
        int port = Integer.parseInt(args[1]);

        Socket serverSocket = new Socket(host, port);

        InputStreamReader socketRx = new InputStreamReader(serverSocket.getInputStream());
        OutputStreamWriter socketTx = new OutputStreamWriter(serverSocket.getOutputStream());

        new SocketClient(new BufferedReader(socketRx), new BufferedWriter(socketTx)).run();
    }

    @Override
    public void sendMessageToClient(GameUpdate update) throws Exception {

    }
}
