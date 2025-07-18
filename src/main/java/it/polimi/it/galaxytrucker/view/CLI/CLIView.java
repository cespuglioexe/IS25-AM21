package it.polimi.it.galaxytrucker.view.CLI;

import it.polimi.it.galaxytrucker.controller.GenericGameData;
import it.polimi.it.galaxytrucker.model.componenttiles.TileData;
import it.polimi.it.galaxytrucker.model.componenttiles.TileEdge;
import it.polimi.it.galaxytrucker.view.CLI.CLIViewStates.*;
import it.polimi.it.galaxytrucker.view.CLI.CLIViewStates.cardInputStates.*;
import it.polimi.it.galaxytrucker.view.CLI.CLIViewStates.generalStates.CLILeaderboardState;
import it.polimi.it.galaxytrucker.view.CLI.CLIViewStates.generalStates.GameSelectionState;
import it.polimi.it.galaxytrucker.view.CLI.CLIViewStates.generalStates.NameSelectionState;
import it.polimi.it.galaxytrucker.view.CLI.CLIViewStates.shipBuildingStates.BuildingMenuState;
import it.polimi.it.galaxytrucker.view.CLI.CLIViewStates.shipBuildingStates.CLICrewmateAddingState;
import it.polimi.it.galaxytrucker.view.CLI.CLIViewStates.shipBuildingStates.ShipFixingState;
import it.polimi.it.galaxytrucker.view.CLI.CLIViewStates.shipBuildingStates.TileActionState;
import it.polimi.it.galaxytrucker.view.View;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CLIView extends View {

    public final ExecutorService executorService = Executors.newSingleThreadExecutor();

    private final AdventureCardASCII cardAscii = new AdventureCardASCII();

    public CLIView() {
        displayGameTitle();
    }

    @Override
    public void repromptState() {

    }

    @Override
    public void begin() {
        CLIInputReader.getInstance().run();

        CLIViewState.setView(this);
        CLIViewState.setCurrentState(new NameSelectionState());
        CLIViewState.getCurrentState().executeState();
    }

    @Override
    public void titleScreen() {
        displayGameTitle();
    }

    @Override
    public void displayShip(List<List<TileData>> ship) {
        printShip(ship);
    }

    @Override
    public void displayComponentTile(TileData newTile) {
        CLIViewState.getCurrentState().displayComponentTile(newTile);
    }

    @Override
    public void tileActions() {
        CLIViewState.setCurrentState(new TileActionState());
        CLIViewState.getCurrentState().executeState();
    }

    @Override
    public void displayTiles(List<TileData> tiles) {
        displayTileList(tiles);
    }

    @Override
    public void displayCards(List<String> cards) {

    }

    @Override
    public void nameNotAvailable() {
        CLIViewState.getCurrentState().nameNotAvailable();
    }

    @Override
    public void buildingStarted() {
        displayBuildingStarted();
        CLIViewState.setCurrentState(new BuildingMenuState());
        CLIViewState.getCurrentState().executeState();
    }

    @Override
    public void gameSelectionScreen() {
        CLIViewState.setCurrentState(new GameSelectionState());
        CLIViewState.getCurrentState().executeState();
    }

    @Override
    public void gameCreationSuccess(boolean success) {
        CLIViewState.getCurrentState().gameCreationSuccess(success);
    }

    @Override
    public void joinedGameIsFull() {
        CLIViewState.getCurrentState().joinedGameIsFull();
    }

    @Override
    public void remoteExceptionThrown() {
        CLIViewState.getCurrentState().remoteExceptionThrown();
    }

    @Override
    public void activeControllers(List<GenericGameData> activeControllers) {
        CLIViewState.getCurrentState().activeControllers(activeControllers);
    }

    @Override
    public void shipUpdated(UUID interestedPlayerId) {
        if (interestedPlayerId.equals(getClient().getModel().getMyData().getPlayerId()))
            CLIViewState.getCurrentState().displayPlayerShip();
    }

    @Override
    public void componentTileReceived(TileData newTile) {
        CLIViewState.getCurrentState().displayComponentTile(newTile);
    }

    @Override
    public void savedComponentsUpdated() {
        // This function is not necessary when using a TUI implementation
    }

    @Override
    public void discardedComponentsUpdated() {
        CLIViewState.getCurrentState().discardedComponentsUpdated();
    }

    @Override
    public void nameSelectionSuccess() {
        CLIViewState.setCurrentState(new GameSelectionState());
        CLIViewState.getCurrentState().executeState();
    }

    @Override
    public void shipFixingState() {
        CLIViewState.setCurrentState(new ShipFixingState());
        CLIViewState.getCurrentState().executeState();
    }

    @Override
    public void waitingForGameState() {
        System.out.println(ConsoleColors.YELLOW_BOLD + "Building finished! Your ship look great, some of your fellow truckers need to fix theirs before we can proceed!");
    }

    @Override
    public void newCardStartedExecution() {
        CLIViewState.getCurrentState().displayCard();
    }

    @Override
    public void displayInputOptions(String card, String cardState) {
        switch (cardState) {
            case "ParticipationState":
                if (card.equals("Planets")) {
                    CLIViewState.setCurrentState(new CLIPlanetSelectionState());
                    CLIViewState.getCurrentState().executeState();
                } else {
                    CLIViewState.setCurrentState(new CLIPartecipationAcceptState());
                    CLIViewState.getCurrentState().executeState();
                }
                break;
            case "CargoRewardState":
                CLIViewState.setCurrentState(new CLICargoChoiceState());
                CLIViewState.getCurrentState().executeState();
                break;
            case "CreditRewardState":
                CLIViewState.setCurrentState(new CLICreditAcceptState());
                CLIViewState.getCurrentState().executeState();
                break;
            case "CrewmatePenaltyState":
                CLIViewState.setCurrentState(new CLICrewmatePenaltyState());
                CLIViewState.getCurrentState().executeState();
                break;
            case "CalculateFirePowerState", "CannonSelectionState", "BigMeteorState":
                CLIViewState.setCurrentState(new CLICannonActivationState());
                CLIViewState.getCurrentState().executeState();
                break;
            case "CalculateEnginePowerState", "EngineSelectionState":
                CLIViewState.setCurrentState(new CLIEngineActivationState());
                CLIViewState.getCurrentState().executeState();
                break;
            case "ActivateShieldState", "SmallMeteorState", "AttackState":
                CLIViewState.setCurrentState(new CLIShieldActivationState());
                CLIViewState.getCurrentState().executeState();
                break;
            default:
                System.out.println("Margarozzo!!!");
                break;
        }
    }

    @Override
    public void showScoreBoard() {
        CLIViewState.setCurrentState(new CLILeaderboardState());
        CLIViewState.getCurrentState().executeState();
    }

    @Override
    public void showSleepView() {

    }

    @Override
    public void addCrewmates() {
        CLIViewState.setCurrentState(new CLICrewmateAddingState());
        CLIViewState.getCurrentState().executeState();
    }

    @Override
    public void displayCardUpdates(String card, String cardState, Map<String, Object> cardDetails) {

    }

    @Override
    public void manageInputError() {

    }

    @Override
    public void startNewTurn() {
        CLIViewState.setCurrentState(new CLIGameTurnState());
        CLIViewState.getCurrentState().executeState();
    }

    @Override
    public void loadingScreen() {

    }


    ////////////////////////////////////


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

    public void printSingleComponent (TileData tile) {
//        System.out.println("COMPONENT: " + tile.getType());
//        System.out.println("EDGES: " + tile.getTop() + ", " + tile.getRight() + ", " + tile.getBottom() + ", " + tile.getLeft());

        System.out.println();
        List<String> tileAscii = getTileAscii(tile);
        for (String s : tileAscii) {
            System.out.println(s);
        }
        System.out.println();
    }

    public void displayBuildingStarted () {
        String text = """
                ▄   ▘▜  ▌▘        ▗     ▗    ▌▌
                ▙▘▌▌▌▐ ▛▌▌▛▌▛▌  ▛▘▜▘▀▌▛▘▜▘█▌▛▌▌
                ▙▘▙▌▌▐▖▙▌▌▌▌▙▌  ▄▌▐▖█▌▌ ▐▖▙▖▙▌▖
                            ▄▌                
                """;

        System.out.print("\n");
        System.out.println(ConsoleColors.YELLOW_BRIGHT + text + ConsoleColors.RESET);
        System.out.print("\n");
    }

    public void displayFixShip () {
        String text = """
                ▄▖▘                ▌ ▘  ▌
                ▙▖▌▚▘  ▌▌▛▌▌▌▛▘  ▛▘▛▌▌▛▌▌
                ▌ ▌▞▖  ▙▌▙▌▙▌▌   ▄▌▌▌▌▙▌▖
                       ▄▌             ▌ \s
                """;

        System.out.print("\n");
        System.out.println(ConsoleColors.YELLOW_BRIGHT + text + ConsoleColors.RESET);
        System.out.print("\n");
    }

    public void printShip (List<List<TileData>> ship) {
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

    private boolean isDouble(TileEdge edge) {
        return edge == TileEdge.DOUBLE || edge == TileEdge.UNIVERSAL;
    }

    private boolean isSingle(TileEdge edge) {
        return edge == TileEdge.SINGLE || edge == TileEdge.UNIVERSAL;
    }

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

    public void printAdventureCard (String cardName) {
        List<String> card = cardAscii.getCardAscii(cardName);

        executorService.submit(() -> {
            System.out.println();
            for (String line : card) {
                System.out.println(line);
            }
            System.out.println();
        });
    }

    private static final int[][] BOARD_POSITIONS_LVL1 = {
            {-1, -1, 4, 3, 2, -1, 1},
            {-1, -1, 0, 1, 2, 3, 4},
            {-1, 5, -1, -1, -1, -1, -1, 6},
            {7, -1, -1, -1, -1, -1, -1, -1, 8},
            {9, -1, -1, -1, -1, -1, -1, -1, 10},
            {-1, 11, -1, -1, -1, -1, -1, 12},
            {-1, -1, 13,14,15,16,17},
    };

    private static final int[][] BOARD_POSITIONS_LVL2 = {
            {-1, -1, 4, 3, -1, 2, -1, -1, 1},
            {-1, -1, 0, 1, 2, 3, 4, 5, 6},
            {-1, 7, -1, -1, -1, -1, -1, -1, -1, 8},
            {9, -1, -1, -1, -1, -1, -1, -1, -1, -1, 10},
            {11, -1, -1, -1, -1, -1, -1, -1, -1, -1, 12},
            {13, -1, -1, -1, -1, -1, -1, -1, -1, -1, 14},
            {-1, 15, -1, -1, -1, -1, -1, -1, -1, 16},
            {-1, -1, 17, 18, 19, 20, 21, 22, 23},
    };

    public void printFlightBoard() {
        executorService.submit(() -> {
            try {
                int[][] layout = getClient().getModel().getGameLevel() == 1 ? BOARD_POSITIONS_LVL1 : BOARD_POSITIONS_LVL2;
                Set<Integer> markers = new HashSet<>(getClient().getModel().getPlayerMarkerPositions().values());

                for (int rowIndex = 0; rowIndex < layout.length; rowIndex++) {
                    int[] row = layout[rowIndex];
                    StringBuilder line = new StringBuilder();

                    for (int cell : row) {
                        if (cell == -1) {
                            line.append("    ");
                        } else if (rowIndex == 0) {
                            // Print raw label values
                            line.append(String.format(" %d  ", cell));
                        } else if (markers.contains(cell)) {
                            line.append("[X] ");
                        } else {
                            line.append("[ ] ");
                        }
                    }

                    System.out.println(line.toString().stripTrailing());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void displayTimerEnded () {
        System.out.println(ConsoleColors.CYAN + "Building timer has ended!" + ConsoleColors.RESET);
    }

    public void displayTimerStarted(){
        System.out.println(ConsoleColors.CYAN + "Building timer has been started!" + ConsoleColors.RESET);
    }
}