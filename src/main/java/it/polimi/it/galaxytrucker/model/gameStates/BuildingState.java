package it.polimi.it.galaxytrucker.model.gameStates;
import it.polimi.it.galaxytrucker.model.componenttiles.ComponentTile;
import it.polimi.it.galaxytrucker.model.design.statePattern.State;
import it.polimi.it.galaxytrucker.model.design.statePattern.StateMachine;
import it.polimi.it.galaxytrucker.model.exceptions.IllegalComponentPositionException;
import it.polimi.it.galaxytrucker.model.exceptions.InvalidActionException;
import it.polimi.it.galaxytrucker.model.exceptions.InvalidFunctionCallInState;
import it.polimi.it.galaxytrucker.model.managers.FlightBoard;
import it.polimi.it.galaxytrucker.model.managers.GameManager;
import it.polimi.it.galaxytrucker.model.managers.Player;
import it.polimi.it.galaxytrucker.model.managers.ShipManager;
import it.polimi.it.galaxytrucker.view.cli.ConsoleColors;

import java.util.*;

public class BuildingState extends GameState {
    private HashMap<UUID, Boolean> playerHasFinished;
    private List<ComponentTile> discardedComponents;

    @Override
    public void enter(StateMachine fsm) {
        System.out.println(ConsoleColors.BLUE_UNDERLINED + "\n> " + this.getClass().getSimpleName() + " <\n" + ConsoleColors.RESET);

        GameManager gameManager = (GameManager) fsm;
        gameManager.initializeComponentTiles();

        this.discardedComponents = new ArrayList<>();
        this.playerHasFinished = new HashMap<>();
        for(Player player : gameManager.getPlayers()) {
            playerHasFinished.put(player.getPlayerID(), false);
        }
    }

    @Override
    public void update(StateMachine fsm) throws InvalidActionException {
        for (UUID player : playerHasFinished.keySet()) {
            if (!playerHasFinished.get(player)) {
                return;
            }
        }
        changeState(fsm, new LegalityCheckState());
    }

    @Override
    public void exit(StateMachine fsm) {

    }

    @Override
    public void drawComponentTile(StateMachine fsm, UUID playerID) throws InvalidActionException, InvalidFunctionCallInState {
        GameManager game = (GameManager) fsm;

           if (game.getComponentTiles().isEmpty()) {
            throw new InvalidActionException("There are no components left");
        }

        ComponentTile tile = drawRandomComponentTile(game.getComponentTiles());
        game.getPlayerByID(playerID).setHeldComponent(tile);
    }

    private ComponentTile drawRandomComponentTile(Set<ComponentTile> components) {
        int index = getRandomIndex(components.size());

        return removeComponentTileAtIndex(index, components);
    }

    private int getRandomIndex(int upperBoundExclusive) {
        return new Random().nextInt(upperBoundExclusive);
    }

    private ComponentTile removeComponentTileAtIndex(int index, Set<ComponentTile> components) throws IndexOutOfBoundsException {
        Iterator<ComponentTile> iterator = components.iterator();
        int currentIndex = 0;

        while (iterator.hasNext()) {
            ComponentTile tile = iterator.next();

            if (currentIndex++ == index) {
                iterator.remove();
                return tile;
            }
        }

        throw new IndexOutOfBoundsException("Index " + index + " is out of bounds");
    }

    @Override
    public void placeComponentTile(StateMachine fsm, UUID playerID, int row, int column) throws IndexOutOfBoundsException, IllegalComponentPositionException {
        GameManager game = (GameManager) fsm;

        Player player = game.getPlayerByID(playerID);
        ShipManager ship = player.getShipManager();
        ComponentTile comp = player.getHeldComponent();

        ship.addComponentTile(row, column, comp);
        player.setHeldComponent(null);
        System.out.println(ConsoleColors.GREEN + "Placed " + comp.getClass().getSimpleName() + row + ", " + column);
    }

    public void rotateComponentTile(StateMachine fsm, UUID playerID, int row, int column) throws InvalidFunctionCallInState {
        GameManager game = (GameManager) fsm;

        Player player = game.getPlayerByID(playerID);
        ShipManager ship = player.getShipManager();

        if (ship.isOutside(row, column)) {
            throw new IllegalComponentPositionException("Position (" + row + ", " + column + ") is outside the ship.");
        }

        ship.getComponent(row, column).ifPresentOrElse(
            ComponentTile::rotate,
            () -> {
                throw new IllegalComponentPositionException("No component found at position (" + row + ", " + column + ").");
            }
        );

        System.out.println("Component rotated");
    }

    @Override
    public void finishBuilding(StateMachine fsm, UUID playerID) throws InvalidFunctionCallInState {
        GameManager game = (GameManager) fsm;

        playerHasFinished.put(playerID, true);

        FlightBoard flightBoard = game.getFlightBoard();
        flightBoard.addPlayerMarker(game.getPlayerByID(playerID));

        update(fsm);
    }

    @Override
    public void saveComponentTile(StateMachine fsm, UUID playerID) throws InvalidFunctionCallInState, InvalidActionException {
        GameManager game = (GameManager) fsm;

        Player player = game.getPlayerByID(playerID);

        if (Optional.ofNullable(player.getHeldComponent()).isEmpty()) {
            throw new InvalidActionException("No component held by " + game.getPlayerByID(playerID).getPlayerName());
        }

        player.getShipManager().saveComponentTile(player.getHeldComponent());
        player.setHeldComponent(null);
    }

    @Override
    public void discardComponentTile(StateMachine fsm, UUID playerID) throws InvalidFunctionCallInState {
        GameManager game = (GameManager) fsm;

        Player player = game.getPlayerByID(playerID);

        if (Optional.ofNullable(player.getHeldComponent()).isEmpty()) {
            throw new InvalidActionException("No component held by " + game.getPlayerByID(playerID).getPlayerName());
        }
        discardedComponents.add(player.getHeldComponent());
        System.out.println("Discarded " + player.getHeldComponent().getClass().getSimpleName() + " component");
        player.setHeldComponent(null);
        System.out.println(discardedComponents);
    }

    @Override
    public List<ComponentTile> getDiscardedComponentTiles() {
        return discardedComponents;
    }

    @Override
    public void selectSavedComponentTile (StateMachine fsm, UUID playerID, int index) throws InvalidFunctionCallInState {
        GameManager game = (GameManager) fsm;

        Player player = game.getPlayerByID(playerID);

        if (Optional.ofNullable(player.getHeldComponent()).isPresent()) {
            throw new InvalidActionException(game.getPlayerByID(playerID).getPlayerName() + "is already holding a component");
        }

        ComponentTile comp = player.getShipManager().getSavedComponentTile(index);
        player.setHeldComponent(comp);
    }

    @Override
    public void selectDiscardedComponentTile (StateMachine fsm, UUID playerID, int index) throws InvalidFunctionCallInState {
        GameManager game = (GameManager) fsm;

        Player player = game.getPlayerByID(playerID);

        if (Optional.ofNullable(player.getHeldComponent()).isPresent()) {
            throw new InvalidActionException(game.getPlayerByID(playerID).getPlayerName() + "is already holding a component");
        }

        ComponentTile comp = discardedComponents.get(index);
        discardedComponents.remove(index);
        player.setHeldComponent(comp);
    }
}