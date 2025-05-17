package it.polimi.it.galaxytrucker.main;

import it.polimi.it.galaxytrucker.networking.client.rmi.RMIClient;
import it.polimi.it.galaxytrucker.networking.client.socket.SocketClient;
import it.polimi.it.galaxytrucker.view.View;
import it.polimi.it.galaxytrucker.view.CLI.CLIView;

import java.io.*;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.Scanner;

import static org.fusesource.jansi.Ansi.ansi;

public class ClientApplication {
    public static void main(String[] args) throws RemoteException {
        Scanner scanner = new Scanner(System.in);
        String connectionType;
        String interfaceType;

        if (args.length >= 2) {
            connectionType = args[0];
            interfaceType = args[1];
        } else {
            do {
                System.out.print("Choose a communication type (RMI / SOCKET)\n> ");
                connectionType = scanner.nextLine().trim().toLowerCase();
            } while (!connectionType.equals("rmi") && !connectionType.equals("socket"));

            do {
                System.out.print("Choose an interface type (CLI / GUI)\n> ");
                interfaceType = scanner.nextLine().trim().toLowerCase();
            } while (!interfaceType.equals("cli") && !interfaceType.equals("gui"));
        }

        View view = null;
        if (interfaceType.equals("cli"))
            view = new CLIView();
//        else if (interfaceType.equals("gui"))
//            view = new GUIView();

        if (connectionType.equals("rmi")) {
            new RMIClient(view).run();
        }
        else if (connectionType.equals("socket")) {
            new SocketClient(view).run();
        }
    }
}
