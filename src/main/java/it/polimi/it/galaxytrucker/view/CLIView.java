package it.polimi.it.galaxytrucker.view;

import it.polimi.it.galaxytrucker.model.componenttiles.TileData;
import it.polimi.it.galaxytrucker.model.componenttiles.TileEdge;
import it.polimi.it.galaxytrucker.networking.messages.GameUpdate;
import it.polimi.it.galaxytrucker.networking.rmi.client.RMIClient;
import it.polimi.it.galaxytrucker.view.statePattern.StateMachine;

import java.util.ArrayList;
import java.util.List;
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
        System.out.println("COMPONENT: " + tile.getType());
        System.out.println("EDGES: " + tile.getTop() + ", " + tile.getRight() + ", " + tile.getBottom() + ", " + tile.getLeft());

        List<String> tileAscii = getTileAscii(tile);
        for (String s : tileAscii) {
            System.out.println(s);
        }
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

    public void displayShip (List<List<TileData>> ship) {
        // NAVE: 5x7
        List<List<List<String>>> shipAscii = new ArrayList<>();
        for (List<TileData> row : ship) {
            List<List<String>> rowAscii = new ArrayList<>();
            for (TileData tile : row) {
                rowAscii.add(getTileAscii(tile));
            }
            shipAscii.add(rowAscii);
        }

        System.out.println("       4          5          6          7          8          9         10     ");
        int r = 5;
        for (List<List<String>> rowAscii : shipAscii) {
            for (int i = (r == 5 ? 0 : 1); i < 5 ; i++) {
                System.out.print((i == 2 ? (r + " ") : "  "));

                for (List<String> tileAscii : rowAscii) {
                    System.out.print(tileAscii.get(i));
                }
                System.out.print("\n");
            }
            r++;
        }
    }

    public List<String> getTileAscii(TileData tile) {
        List<String> lines = new ArrayList<>();

        if (tile == null) {
            return List.of(
                    "+---------+" + ConsoleColors.RESET,
                    "|" + ConsoleColors.WHITE_BACKGROUND + "         " + ConsoleColors.RESET + "|",
                    "|" + ConsoleColors.WHITE_BACKGROUND + "         " + ConsoleColors.RESET + "|",
                    "|" + ConsoleColors.WHITE_BACKGROUND + "         " + ConsoleColors.RESET + "|",
                    "+---------+" + ConsoleColors.RESET);
        }

        String tileName = "       ";
        String borderColor = ConsoleColors.RESET.toString();
        String insideColor = ConsoleColors.GREEN.toString();

        if (tile.getType().equals("OutOfBoundsTile")) borderColor = ConsoleColors.WHITE.toString();

        switch (tile.getType()) {
            case "CentralCabin"     -> tileName = "C Cabin";
            case "OutOfBoundsTile"  -> tileName = "       ";
            case "BatteryComponent" -> tileName = "Battery";
            case "CabinModule"      -> tileName = " Cabin ";
            case "CargoHold"        -> tileName = " Cargo ";
            case "DoubleCannon"     -> tileName = "DCannon";
            case "SingleCannon"     -> tileName = "SCannon";
            case "LifeSupport"      -> tileName = "LifeSup";
            case "Shield"           -> tileName = "Shield ";
            case "SpecialCargoHold" -> tileName = "S Cargo";
            case "StructuralModule" -> tileName = "Struct ";
            case "SingleEngine"     -> tileName = "SEngine";
            case "DoubleEngine"     -> tileName = "DEngine";
        }

        lines.add(borderColor + "+---------+" + ConsoleColors.RESET);

        lines.add(borderColor + "|" + ConsoleColors.RESET +
                    insideColor +
                    (isDouble(tile.getLeft()) ? "←" : " ") + " " +
                    (isDouble(tile.getTop()) ? "↑" : " ") + " " +
                    (isSingle(tile.getTop()) ? "↑" : " ") + " " +
                    (isDouble(tile.getTop()) ? "↑" : " ") + " " +
                    (isDouble(tile.getRight()) ? "→" : " ") +
                    ConsoleColors.RESET +
                    borderColor + "|" + ConsoleColors.RESET);

        lines.add(borderColor + "|" + ConsoleColors.RESET +
                    insideColor +
                    (isSingle(tile.getLeft()) ? "←" : " ") +
                    tileName +
                    (isSingle(tile.getRight()) ? "→" : " ") +
                    ConsoleColors.RESET +
                    borderColor + "|" + ConsoleColors.RESET);

        lines.add(borderColor + "|" + ConsoleColors.RESET +
                    insideColor +
                    (isDouble(tile.getLeft()) ? "←" : " ") + " " +
                    (isDouble(tile.getBottom()) ? "↓" : " ") + " " +
                    (isSingle(tile.getBottom()) ? "↓" : " ") + " " +
                    (isDouble(tile.getBottom()) ? "↓" : " ") + " " +
                    (isDouble(tile.getRight()) ? "→" : " ") +
                    ConsoleColors.RESET +
                    borderColor + "|" + ConsoleColors.RESET);

        lines.add(borderColor + "+---------+" + ConsoleColors.RESET);

        return lines;
    }

    private boolean isDouble(TileEdge edge) {
        return edge == TileEdge.DOUBLE || edge == TileEdge.UNIVERSAL;
    }

    private boolean isSingle(TileEdge edge) {
        return edge == TileEdge.SINGLE || edge == TileEdge.UNIVERSAL;
    }

    public void displayTileList (List<TileData> tileList) {
        System.out.println("display tile list");


        if (tileList == null || tileList.isEmpty()) {
            System.out.println("There are no discarded tiles");
            return;
        }

        List<List<String>> listAscii = new ArrayList<>();
        for (TileData tile : tileList) {
            List<String> tileAscii = getTileAscii(tile);
            listAscii.add(tileAscii);
        }

        int tileHeight = listAscii.get(0).size(); // e.g., 5

        for (int i = 0; i < listAscii.size(); i += 5) {
            int end = Math.min(i + 5, listAscii.size());
            List<List<String>> rowTiles = listAscii.subList(i, end);

            for (int line = 0; line < tileHeight; line++) {
                StringBuilder sb = new StringBuilder();
                for (List<String> tile : rowTiles) {
                    sb.append(tile.get(line)).append("   "); // Add spacing between tiles
                }
                System.out.println(sb.toString().stripTrailing());
            }
            System.out.println(); // Extra newline between tile rows
        }
    }
}