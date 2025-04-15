package it.polimi.it.galaxytrucker.view;

import it.polimi.it.galaxytrucker.model.componenttiles.TileData;
import it.polimi.it.galaxytrucker.networking.messages.GameUpdate;
import it.polimi.it.galaxytrucker.networking.rmi.client.RMIClient;
import it.polimi.it.galaxytrucker.view.statePattern.StateMachine;

import java.util.logging.ConsoleHandler;

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

    public void displayComponentTile (TileData tile) {
        System.out.println(
                "Component type: " + tile.getType() + "\n" +
                "Rotation: " + tile.getRotation() + "\n" +
                "Edges: " + tile.getTop() + ", " + tile.getRight() + ", " + tile.getBottom() + ", " + tile.getLeft()
        );

    }

    public void displayBuildingStarted () {
        String text = """
                 ____    __                    __        __                   ___       __                      __    \s
                /\\  _`\\ /\\ \\__                /\\ \\__    /\\ \\              __ /\\_ \\     /\\ \\  __                /\\ \\   \s
                \\ \\,\\L\\_\\ \\ ,_\\    __     _ __\\ \\ ,_\\   \\ \\ \\____  __  __/\\_\\\\//\\ \\    \\_\\ \\/\\_\\    ___      __\\ \\ \\  \s
                 \\/_\\__ \\\\ \\ \\/  /'__`\\  /\\`'__\\ \\ \\/    \\ \\ '__`\\/\\ \\/\\ \\/\\ \\ \\ \\ \\   /'_` \\/\\ \\ /' _ `\\  /'_ `\\ \\ \\ \s
                   /\\ \\L\\ \\ \\ \\_/\\ \\L\\.\\_\\ \\ \\/ \\ \\ \\_    \\ \\ \\L\\ \\ \\ \\_\\ \\ \\ \\ \\_\\ \\_/\\ \\L\\ \\ \\ \\/\\ \\/\\ \\/\\ \\L\\ \\ \\_\\\s
                   \\ `\\____\\ \\__\\ \\__/.\\_\\\\ \\_\\  \\ \\__\\    \\ \\_,__/\\ \\____/\\ \\_\\/\\____\\ \\___,_\\ \\_\\ \\_\\ \\_\\ \\____ \\/\\_\\
                    \\/_____/\\/__/\\/__/\\/_/ \\/_/   \\/__/     \\/___/  \\/___/  \\/_/\\/____/\\/__,_ /\\/_/\\/_/\\/_/\\/___L\\ \\/_/
                                                                                                             /\\____/  \s
                                                                                                             \\_/__/
                """;

        System.out.print("\n");
        System.out.println(ConsoleColors.YELLOW_BRIGHT + text + ConsoleColors.RESET);
        System.out.print("\n");
    }

    public void displayShip () {
        String text = """
                        
                    """;
        System.out.println("");
    }
}