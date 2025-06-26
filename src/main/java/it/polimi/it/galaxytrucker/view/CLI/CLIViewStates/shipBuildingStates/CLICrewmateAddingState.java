package it.polimi.it.galaxytrucker.view.CLI.CLIViewStates.shipBuildingStates;

import it.polimi.it.galaxytrucker.messages.clientmessages.UserInput;
import it.polimi.it.galaxytrucker.messages.clientmessages.UserInputType;
import it.polimi.it.galaxytrucker.model.componenttiles.TileData;
import it.polimi.it.galaxytrucker.model.componenttiles.TileEdge;
import it.polimi.it.galaxytrucker.model.utility.AlienType;
import it.polimi.it.galaxytrucker.model.utility.Coordinates;
import it.polimi.it.galaxytrucker.view.CLI.CLIInputReader;
import it.polimi.it.galaxytrucker.view.CLI.CLIViewStates.CLIViewState;
import it.polimi.it.galaxytrucker.view.CLI.ConsoleColors;
import it.polimi.it.galaxytrucker.view.GUI.GUIView;
import javafx.fxml.FXML;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CLICrewmateAddingState extends CLIViewState {
    private int totalBrownAliens = 0, totalPurpleAliens = 0;
    private boolean canAddBrownAliens = true, canAddPurpleAliens = true;
    private List<TileData> tilesToFill = null;

    @Override
    public void executeState() {
        if (tilesToFill == null) {
            tilesToFill = new ArrayList<>();
            List<List<TileData>> ship = view.getClient().getModel().getPlayerShips(view.getClient().getModel().getMyData().getPlayerId());
            for (List<TileData> row : ship) {
                for (TileData tileData : row) {
                    if (tileData != null && tileData.type().equals("CabinModule")) tilesToFill.add(tileData);
                }
            }
        }

        view.executorService.submit(() -> {
            System.out.print("""
                    \nChoose an option:
                    [1]: Add 2 humans
                    [2]: Add a brown alien
                    [3]: Add a purple alien
                    [4]: Confirm
                    >\s""");

            int option = CLIInputReader.readInt();
            int column;
            int row;

            switch (option) {
                case 1:
                    System.out.println("\nIn which cabin do you want to place the humans?");
                    System.out.print("Column> ");
                    column = CLIInputReader.readInt();
                    System.out.print("Row> ");
                    row = CLIInputReader.readInt();

                    addHuman(row, column);
                    executeState();
                    break;
                case 2:
                    System.out.println("\nIn which cabin do you want to place the alien?");
                    System.out.print("Column> ");
                    column = CLIInputReader.readInt();
                    System.out.print("Row> ");
                    row = CLIInputReader.readInt();

                    addBrownAlien(row, column);
                    executeState();
                    break;
                case 3:
                    System.out.println("\nIn which cabin do you want to place the alien?");
                    System.out.print("Column> ");
                    column = CLIInputReader.readInt();
                    System.out.print("Row> ");
                    row = CLIInputReader.readInt();

                    addPurpleAlien(row, column);
                    executeState();
                    break;
                case 4:
                    endBuilding();
                default:
                    System.out.println(ConsoleColors.YELLOW + "That's not a valid option. Please try again" + ConsoleColors.RESET);
                    executeState();
                    break;
            }
        });
    }

    private void addHuman(int row, int column){
        if (placeCrewmates("human", row, column)) {
            HashMap<String, Coordinates> map = new HashMap<>();
            map.put("HUMAN", new Coordinates(column, row));

            view.getClient().receiveUserInput(
                    new UserInput.UserInputBuilder(UserInputType.PLACE_CREWMATE)
                            .setPlacedCrewmate(map)
                            .build()
            );

            view.getClient().receiveUserInput(
                    new UserInput.UserInputBuilder(UserInputType.PLACE_CREWMATE)
                            .setPlacedCrewmate(map)
                            .build()
            );
        }
    }

    public void addBrownAlien(int row, int column){
        if (canAddBrownAliens && placeCrewmates("brownAlien", row, column)) {
            totalBrownAliens++;

            HashMap<String, Coordinates> map = new HashMap<>();
            map.put(AlienType.BROWNALIEN.toString(), new Coordinates(column, row));

            view.getClient().receiveUserInput(
                    new UserInput.UserInputBuilder(UserInputType.PLACE_CREWMATE)
                            .setPlacedCrewmate(map)
                            .build()
            );
        }

        if(totalBrownAliens > 0) {
            canAddBrownAliens = false;
        }
    }

    public void addPurpleAlien(int row, int column){
        if (canAddPurpleAliens && placeCrewmates("purpleAlien", row, column)) {
            totalPurpleAliens++;

            HashMap<String, Coordinates> map = new HashMap<>();
            map.put(AlienType.PURPLEALIEN.toString(), new Coordinates(column, row));

            view.getClient().receiveUserInput(
                    new UserInput.UserInputBuilder(UserInputType.PLACE_CREWMATE)
                            .setPlacedCrewmate(map)
                            .build()
            );
        }

        if(totalPurpleAliens > 0){
            canAddPurpleAliens = false;
        }
    }

    private boolean placeCrewmates(String type, int row, int column){
        List<List<TileData>> ship = view.getClient().getModel().getPlayerShips(view.getClient().getModel().getMyData().getPlayerId());
        TileData tile = ship.get(column - 5).get(row - 4);

        if(tile == null || (!tile.type().equals("CabinModule") && !tile.type().equals("CentralCabin"))) {
            setText("You can only add a crewmates to a cabin module");
            return false;
        }

        if (tile.crewmates().contains("PURPLEALIEN") || tile.crewmates().contains("BROWNALIEN") || tile.crewmates().size() >= 2 || tile.type().equals("CentralCabin")) {
            setText("This cabin is already full!");
            return false;
        }

        if (type.equals("human")) {
            tilesToFill.remove(tile);
            return true;
        }

        boolean hasBrownLifeSupport = false;
        boolean hasPurpleLifeSupport = false;

        int rowMat = row - 4;
        int colMat = column - 5;
        int maxRows = ship.size();
        int maxCols = ship.get(0).size();

        // UP
        if (rowMat > 0) {
            TileData tempTile = ship.get(rowMat - 1).get(colMat);
            if (tempTile != null && !tempTile.type().equals("OutOfBoundsTile")) {
                hasBrownLifeSupport |= tempTile.type().equals("LifeSupport") && tempTile.alienSupport().equals("BROWNALIEN") && tempTile.bottom().isCompatible(tile.top()) && TileEdge.isAConnector(tile.top());
                hasPurpleLifeSupport |= tempTile.type().equals("LifeSupport") && tempTile.alienSupport().equals("PURPLEALIEN") && tempTile.bottom().isCompatible(tile.top()) && TileEdge.isAConnector(tile.top());
            }
        }

        // DOWN
        if (rowMat < maxRows - 1) {
            TileData tempTile = ship.get(rowMat + 1).get(colMat);
            if (tempTile != null && !tempTile.type().equals("OutOfBoundsTile")) {
                hasBrownLifeSupport |= tempTile.type().equals("LifeSupport") && tempTile.alienSupport().equals("BROWNALIEN") && tempTile.top().isCompatible(tile.bottom()) && TileEdge.isAConnector(tile.bottom());
                hasPurpleLifeSupport |= tempTile.type().equals("LifeSupport") && tempTile.alienSupport().equals("PURPLEALIEN") && tempTile.top().isCompatible(tile.bottom()) && TileEdge.isAConnector(tile.bottom());
            }
        }

        // LEFT
        if (colMat > 0) {
            TileData tempTile = ship.get(rowMat).get(colMat - 1);
            if (tempTile != null && !tempTile.type().equals("OutOfBoundsTile")) {
                hasBrownLifeSupport |= tempTile.type().equals("LifeSupport") && tempTile.alienSupport().equals("BROWNALIEN") && tempTile.right().isCompatible(tile.left()) && TileEdge.isAConnector(tile.left());
                hasPurpleLifeSupport |= tempTile.type().equals("LifeSupport") && tempTile.alienSupport().equals("PURPLEALIEN") && tempTile.right().isCompatible(tile.left()) && TileEdge.isAConnector(tile.left());
            }
        }

        // RIGHT
        if (colMat < maxCols - 1) {
            TileData tempTile = ship.get(rowMat).get(colMat + 1);
            if (tempTile != null && !tempTile.type().equals("OutOfBoundsTile")) {
                hasBrownLifeSupport |= tempTile.type().equals("LifeSupport") && tempTile.alienSupport().equals("BROWNALIEN") && tempTile.left().isCompatible(tile.right()) && TileEdge.isAConnector(tile.right());
                hasPurpleLifeSupport |= tempTile.type().equals("LifeSupport") && tempTile.alienSupport().equals("PURPLEALIEN") && tempTile.left().isCompatible(tile.right()) && TileEdge.isAConnector(tile.right());
            }
        }

        if ((type.equals("brownAlien") && !hasBrownLifeSupport) || (type.equals("purpleAlien") && !hasPurpleLifeSupport)) {
            setText("Aliens need matching life support to be added");
            return false;
        }

        tilesToFill.remove(tile);
        return true;
    }

    private void endBuilding(){
        if (!tilesToFill.isEmpty()) {
            setText("You still have cabins to fill!");
        } else {
            view.getClient().receiveUserInput(
                    new UserInput.UserInputBuilder(UserInputType.CONFIRM_BUILDING_END)
                            .build()
            );
        }
    }

    private void setText(String text) {
        System.out.println(ConsoleColors.YELLOW + text + ConsoleColors.RESET);
    }
}
