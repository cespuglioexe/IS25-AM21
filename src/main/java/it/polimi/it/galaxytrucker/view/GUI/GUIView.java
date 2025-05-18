package it.polimi.it.galaxytrucker.view.GUI;

import it.polimi.it.galaxytrucker.main.ClientApplication;
import it.polimi.it.galaxytrucker.model.componenttiles.TileData;
import it.polimi.it.galaxytrucker.model.componenttiles.TileEdge;
import it.polimi.it.galaxytrucker.networking.client.Client;
import it.polimi.it.galaxytrucker.networking.client.rmi.RMIClient;
import it.polimi.it.galaxytrucker.view.CLI.CLIViewStates.CLIViewState;
import it.polimi.it.galaxytrucker.view.CLI.ConsoleColors;
import it.polimi.it.galaxytrucker.view.GUI.controllers.GameCreationController;
import it.polimi.it.galaxytrucker.view.View;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GUIView extends View {

    private static GUIView guiView;
    private GUIApplication guiApplication = new GUIApplication();
    private boolean nameIsCorrectCheck = true;

    public boolean getNameIsCorrectCheck() {
        return nameIsCorrectCheck;
    }

    public static GUIView getGUIView(){
        if(guiView == null){
            guiView = new GUIView();
        }
        return guiView;
    }

    @Override
    public void repromptState() {

    }

    @Override
    public void begin() {
        Application.launch(GUIApplication.class);
    }

    @Override
    public void titleScreen() {

    }

    @Override
    public void displayShip(List<List<TileData>> ship) {

    }


    @Override
    public void displayTiles(List<TileData> tiles) {
        displayTileList(tiles);
    }

    @Override
    public void displayCards(List<Integer> cards) {

    }

    @Override
    public void nameNotAvailable() {
        System.out.println("GUI VIEW: name not available");
        nameIsCorrectCheck = false;
    }

    @Override
    public void buildingStarted() {
        System.out.println("GUI VIEW: building started");
        try {
            guiApplication.showBuildingWindow();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void gameSelectionScreen() throws IOException {
        System.out.println("GUI VIEW: game selection screen");
        guiApplication.showGameCreationWindow();

    }

    @Override
    public void gameCreationSuccess(boolean success) {
        System.out.println("GUI VIEW: game creation sucess");
    }

    @Override
    public void joinedGameIsFull() {
        System.out.println("GUI VIEW: joined game is full");
    }

    @Override
    public void remoteExceptionThrown() {

    }

    @Override
    public void displayComponentTile(TileData newTile) {

    }

    @Override
    public void tileActions() {

    }

    @Override
    public void displayTimerStarted() {
        System.out.println("GUI VIEW: display timer started");
    }

    @Override
    public void displayTimerEnded() {
        System.out.println("GUI VIEW: displayTimerEnded");
    }




    //Utili solo per il controllo senza png

    public void displayTileList (List<TileData> tileList) {
        System.out.println("display tile list");


        if (tileList == null || tileList.isEmpty()) {
            System.out.println(ConsoleColors.YELLOW + "There are no tiles." + ConsoleColors.RESET);
            return;
        }

        List<List<String>> listAscii = new ArrayList<>();
        int counter = 1;
        for (TileData tile : tileList) {
            List<String> tileAscii = getTileAscii(tile);
            tileAscii.add("     " + counter + "     ");
            listAscii.add(tileAscii);
            counter++;
        }

        int tileHeight = listAscii.get(0).size();

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

    private boolean isDouble(TileEdge edge) {
        return edge == TileEdge.DOUBLE || edge == TileEdge.UNIVERSAL;
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

    private boolean isSingle(TileEdge edge) {
        return edge == TileEdge.SINGLE || edge == TileEdge.UNIVERSAL;
    }



}
