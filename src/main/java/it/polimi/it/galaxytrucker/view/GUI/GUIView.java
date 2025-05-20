package it.polimi.it.galaxytrucker.view.GUI;

import it.polimi.it.galaxytrucker.controller.GenericGameData;
import it.polimi.it.galaxytrucker.model.componenttiles.TileData;
import it.polimi.it.galaxytrucker.model.componenttiles.TileEdge;
import it.polimi.it.galaxytrucker.view.CLI.ConsoleColors;
import it.polimi.it.galaxytrucker.view.GUI.controllers.GUIBuildingController;
import it.polimi.it.galaxytrucker.view.GUI.controllers.GUITitleScreen;
import it.polimi.it.galaxytrucker.view.GUI.controllers.GUIGameCreation;
import it.polimi.it.galaxytrucker.view.GUI.controllers.GUIUsernameSelection;
import it.polimi.it.galaxytrucker.view.View;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GUIView extends View {

    private static GUIView guiView;

    public static Stage stage;
    public static List<Double> screenSize;

    public static GUIView getInstance(){
        if(guiView == null){
            new Thread(() -> Application.launch(GUIApplication.class)).start();
            guiView = new GUIView();

            // Initialize all GUI controllers
            GUITitleScreen.getInstance();
            GUIUsernameSelection.getInstance();
        }
        return guiView;
    }

    @Override
    public void repromptState() {

    }

    @Override
    public void begin() {
        Platform.runLater(() -> stage.show());
        titleScreen();
    }

    @Override
    public void titleScreen() {
        GUITitleScreen.getInstance().displayScene();
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

    public void nameSelectionScene() {
        System.out.println("Username selection");
        GUIUsernameSelection.getInstance().displayScene();
    }

    @Override
    public void nameNotAvailable() {
        GUIUsernameSelection.getInstance().nameError();
    }

    @Override
    public void buildingStarted() {
        GUIBuildingController.getInstance().displayScene();
    }

    @Override
    public void gameSelectionScreen(){
        GUIGameCreation.getInstance().displayScene();
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

    @Override
    public void activeControllers(List<GenericGameData> activeControllers) {
        GUIGameCreation.getInstance().activeControllers(activeControllers);
        System.out.println("AFTER GUI view: get instance gamecreation controllers");

    }

    @Override
    public void shipUpdated(UUID interestedPlayerId) {

    }

    @Override
    public void componentTileReceived(TileData newTile) {

    }

    @Override
    public void savedComponentsUpdated() {

    }

    @Override
    public void discardedComponentsUpdated() {

    }

    @Override
    public void nameSelectionSuccess() {
        GUIUsernameSelection.getInstance().nameSelectionSuccess();
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
