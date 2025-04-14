package it.polimi.it.galaxytrucker.view;

import it.polimi.it.galaxytrucker.networking.messages.GameUpdate;
import it.polimi.it.galaxytrucker.networking.rmi.client.RMIClient;
import it.polimi.it.galaxytrucker.view.statePattern.StateMachine;

public class CLIView extends StateMachine {

    private final RMIClient client;

    public CLIView(RMIClient client) {
        displayGameTitle();
        this.client = client;
    }

    public RMIClient getClient() {
        return client;
    }

    private void displayGameTitle () {
        String title = """
               ================================================ WELCOME TO =====================================================
               
                 ██████╗  █████╗ ██╗      █████╗ ██╗  ██╗██╗   ██╗    ████████╗██████╗ ██╗   ██╗ ██████╗██╗  ██╗███████╗██████╗
                ██╔════╝ ██╔══██╗██║     ██╔══██╗╚██╗██╔╝╚██╗ ██╔╝    ╚══██╔══╝██╔══██╗██║   ██║██╔════╝██║ ██╔╝██╔════╝██╔══██╗
                ██║  ███╗███████║██║     ███████║ ╚███╔╝  ╚████╔╝        ██║   ██████╔╝██║   ██║██║     █████╔╝ █████╗  ██████╔╝
                ██║   ██║██╔══██║██║     ██╔══██║ ██╔██╗   ╚██╔╝         ██║   ██╔══██╗██║   ██║██║     ██╔═██╗ ██╔══╝  ██╔══██╗
                ╚██████╔╝██║  ██║███████╗██║  ██║██╔╝ ██╗   ██║          ██║   ██║  ██║╚██████╔╝╚██████╗██║  ██╗███████╗██║  ██║
                 ╚═════╝ ╚═╝  ╚═╝╚══════╝╚═╝  ╚═╝╚═╝  ╚═╝   ╚═╝          ╚═╝   ╚═╝  ╚═╝ ╚═════╝  ╚═════╝╚═╝  ╚═╝╚══════╝╚═╝  ╚═╝
                 
               =================================================================================================================
                """;

        System.out.print("\n");
        for (char c : title.toCharArray()) {
            if (c == '█') {
                System.out.print(ConsoleColors.YELLOW_BOLD + "" + c + ConsoleColors.RESET);
            } else if (c == '\n') {
                System.out.print("\n");
            } else {
                System.out.print(ConsoleColors.RED + "" + c + ConsoleColors.RESET);
            }
        }
        System.out.print("\n");
    }

    public void displayComponentTile (GameUpdate.TileData tile) {
        System.out.println(
                "Component type: " + tile.type() + "\n" +
                "Rotation: " + tile.rotation() + "\n" +
                "Edges: " + tile.top() + ", " + tile.right() + ", " + tile.bottom() + ", " + tile.left()
        );

    }
}