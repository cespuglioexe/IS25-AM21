package it.polimi.it.galaxytrucker.view.GUI;

import it.polimi.it.galaxytrucker.controller.GenericGameData;
import it.polimi.it.galaxytrucker.model.componenttiles.TileData;
import it.polimi.it.galaxytrucker.model.componenttiles.TileEdge;
import it.polimi.it.galaxytrucker.view.CLI.ConsoleColors;
import it.polimi.it.galaxytrucker.view.GUI.controllers.*;
import it.polimi.it.galaxytrucker.view.View;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class GUIView extends View {

    private static GUIView guiView;
    //private String percorsoFile = "/it/polimi/it/galaxytrucker/graphics/pedine/definitelyNotSuspicious.txt";

    public static Stage stage;
    public static List<Double> screenSize;

    public static GUIView getInstance(){
        if(guiView == null){
            new Thread(() -> Application.launch(GUIApplication.class)).start();
            guiView = new GUIView();

            // Initialize all GUI controllers
            GUITitleScreen.getInstance();
            GUIUsernameSelection.getInstance();
            GUIGameCreation.getInstance();
            GUIBuildingController.getInstance();
            GUIFixingShipController.getInstance();
            GUIScoreBoardController.getInstance();
            GUIAddCrewmateController.getInstance();
            //GUIGameTurn.getInstance();
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
    public void displayCards(List<String> cards) {
        GUIGameTurn.getInstance().displayCard();
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
        GUIBuildingController.getInstance().updateVisibleTile(newTile);
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
    }

    @Override
    public void shipUpdated(UUID interestedPlayerId) {
            GUIBuildingController.getInstance().updateShip();
            GUIFixingShipController.getInstance().updateShip();
    }

    @Override
    public void componentTileReceived(TileData newTile) {
        GUIBuildingController.getInstance().updateVisibleTile(newTile);
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

    @Override
    public void shipFixingState() {
        GUIFixingShipController.getInstance();
        GUIFixingShipController.getInstance().displayScene();
    }

    @Override
    public void waitingForGameState() {
        // Funzione viene chiamata quando si passa in fixing state, ma la tua nave é a posto
        // TODO: waiting for others to fix

    }

    @Override
    public void newCardStartedExecution(){
        GUIGameTurn.getInstance().displayCard();
    }

    @Override
    public void displayInputOptions(String card, String cardState) {
        try {
            GUIView.getInstance().showSleepView();
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            System.out.println("Error while waiting for input options: " + e.getMessage());
        }
        switch (cardState) {
            case "ParticipationState":
                if (card.equals("Planets")) {
                    GUIPlanetsSelectionController.getInstance().displayScene();
                } else {
                    GUIParticipationChoiceController.getInstance().displayScene();
                }
                break;
            case "CargoRewardState":
                GUICargoChoiceController.getInstance().displayScene();
                break;
            case "CreditRewardState":
                GUICreditChoiceController.getInstance().displayScene();
                break;
            case "CrewmatePenaltyState":
                GUICrewmatePenaltyController.getInstance().displayScene();
                break;
            case "CalculateFirePowerState":
            case "CannonSelectionState":
            case "BigMeteorState":
                GUIActivateCannonController.getInstance().displayScene();
                break;
            case "CalculateEnginePowerState":
            case "EngineSelectionState":
                GUIActivateEngineController.getInstance().displayScene();
                break;
            case "ActivateShieldState":
            case "SmallMeteorState":
            case "AttackState":
                GUIActivateShieldController.getInstance().displayScene();
                break;
            default:
                System.out.println("Margarozzo!!!");
                break;
        }
    }

    @Override
    public void manageInputError() {
        GUIViewState currentState = GUIGameTurn.getCurrentState();

        if (currentState instanceof GUIErrorHandler) {
            ((GUIErrorHandler) currentState).inputError();
        }
    }

    @Override
    public void startNewTurn() {
        GUIGameTurn.getInstance().displayScene();
    }

    @Override
    public void loadingScreen() {
        GUILoadingViewController.getInstance().displayScene();
    }

    @Override
    public void showScoreBoard(){
        GUIScoreBoardController.getInstance().displayScene();
    }

    @Override
    public void showSleepView(){
        GUISleepViewController.getInstance().displayScene();
    }

    @Override
    public void addCrewmates(){
        GUIAddCrewmateController.getInstance().displayScene();
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

        if (tile.type().equals("OutOfBoundsTile")) borderColor = ConsoleColors.WHITE.toString();

        switch (tile.type()) {
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
                (isDouble(tile.left()) ? "←" : " ") + " " +
                (isDouble(tile.top()) ? "↑" : " ") + " " +
                (isSingle(tile.top()) ? "↑" : " ") + " " +
                (isDouble(tile.top()) ? "↑" : " ") + " " +
                (isDouble(tile.right()) ? "→" : " ") +
                ConsoleColors.RESET +
                borderColor + "|" + ConsoleColors.RESET);

        lines.add(borderColor + "|" + ConsoleColors.RESET +
                insideColor +
                (isSingle(tile.left()) ? "←" : " ") +
                tileName +
                (isSingle(tile.right()) ? "→" : " ") +
                ConsoleColors.RESET +
                borderColor + "|" + ConsoleColors.RESET);

        lines.add(borderColor + "|" + ConsoleColors.RESET +
                insideColor +
                (isDouble(tile.left()) ? "←" : " ") + " " +
                (isDouble(tile.bottom()) ? "↓" : " ") + " " +
                (isSingle(tile.bottom()) ? "↓" : " ") + " " +
                (isDouble(tile.bottom()) ? "↓" : " ") + " " +
                (isDouble(tile.right()) ? "→" : " ") +
                ConsoleColors.RESET +
                borderColor + "|" + ConsoleColors.RESET);

        lines.add(borderColor + "+---------+" + ConsoleColors.RESET);

        return lines;
    }

    private boolean isSingle(TileEdge edge) {
        return edge == TileEdge.SINGLE || edge == TileEdge.UNIVERSAL;
    }

    @Override
    public void displayCardUpdates(String card, String cardState, Map<String, Object> cardDetails) {
        CardUpdateHandler.handle(card, cardState, cardDetails);
    }

    public String getSuperSecretUsername() {
        return "ingconti";
    }


}
