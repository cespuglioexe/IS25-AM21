package it.polimi.it.galaxytrucker.model.gamestates;

import it.polimi.it.galaxytrucker.messages.servermessages.GameUpdate;
import it.polimi.it.galaxytrucker.messages.servermessages.GameUpdateType;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.AdventureCard;
import it.polimi.it.galaxytrucker.model.componenttiles.ComponentTile;
import it.polimi.it.galaxytrucker.model.componenttiles.LifeSupport;
import it.polimi.it.galaxytrucker.model.componenttiles.TileData;
import it.polimi.it.galaxytrucker.model.design.statePattern.StateMachine;
import it.polimi.it.galaxytrucker.exceptions.IllegalComponentPositionException;
import it.polimi.it.galaxytrucker.exceptions.InvalidActionException;
import it.polimi.it.galaxytrucker.exceptions.InvalidFunctionCallInState;
import it.polimi.it.galaxytrucker.model.managers.FlightBoard;
import it.polimi.it.galaxytrucker.model.managers.GameManager;
import it.polimi.it.galaxytrucker.model.managers.Player;
import it.polimi.it.galaxytrucker.model.managers.ShipManager;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class BuildingState extends GameState {
    private HashMap<UUID, Boolean> playerHasFinished;
    private List<ComponentTile> discardedComponents;
    private int level;

    private int completedTimers = 0;
    private int requiredTimers = 0;

    @Override
    public void enter(StateMachine fsm) {
        GameManager gameManager = (GameManager) fsm;
        gameManager.initializeComponentTiles();

        requiredTimers = gameManager.getLevel() == 2 ? 2 : 0;

        level = gameManager.getLevel();
        // List<List<AdventureCard>> cardStacks = gameManager.getAdventureDeck().getStack();

        this.discardedComponents = new ArrayList<>();
        this.playerHasFinished = new HashMap<>();
        for(Player player : gameManager.getPlayers()) {
            playerHasFinished.put(player.getPlayerID(), false);
        }


        // Probabilmente questa operazione si può spostare da qualche parte
        Map<UUID, List<List<ComponentTile>>> playerShips = gameManager.getPlayers().stream()
                .collect(Collectors.toMap(
                        player -> player.getPlayerID(),
                        player -> player.getShipManager().getShipBoard()
                ));

        HashMap<UUID, List<List<TileData>>> convertedShips = new HashMap<>();

        for (Map.Entry<UUID, List<List<ComponentTile>>> entry : playerShips.entrySet()) {
            List<List<TileData>> tileDataGrid = TileData.createTileDataShipFromComponentTileShip(entry.getValue());
            convertedShips.put(entry.getKey(), tileDataGrid);
        }

        //for test sake
        if (gameManager.getAdventureDeck().getCards().isEmpty()) {
            gameManager.initializeAdventureDeck();
        }

        List<List<String>> cardStacks = gameManager.getAdventureDeck().getStacks().values().stream()
                .map(stack -> stack.stream()
                        .map(AdventureCard::getGraphicPath)
                        .toList())
                .toList();


        ((GameManager) fsm).updateListeners(
                new GameUpdate.GameUpdateBuilder(GameUpdateType.NEW_STATE)
                        .setNewSate(this.getClass().getSimpleName())
                        .setGameLevel(((GameManager) fsm).getLevel())
                        .setPlayerIds(gameManager.getPlayers().stream().map(Player::getPlayerID).toList())
                        .setPlayerColors(new HashMap<>(gameManager.getPlayers().stream().collect(Collectors.toMap(Player::getPlayerID, Player::getColor))))
                        .setAllPlayerShipBoards(convertedShips)
                        .setCardPileCompositions(cardStacks)
                        .build());
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
        int index;
        List<ComponentTile> list = components.stream().toList();
        ComponentTile tile;
        if(level == 1){
            do {
                index = getRandomIndex(components.size());
                tile = list.get(index);
            } while (tile instanceof LifeSupport);
        } else index = getRandomIndex(components.size());
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
        player.resetHeldComponent();
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
        player.resetHeldComponent();
    }

    @Override
    public void discardComponentTile(StateMachine fsm, UUID playerID) throws InvalidFunctionCallInState {
        GameManager game = (GameManager) fsm;

        Player player = game.getPlayerByID(playerID);

        if (Optional.ofNullable(player.getHeldComponent()).isEmpty()) {
            throw new InvalidActionException("No component held by " + game.getPlayerByID(playerID).getPlayerName());
        }
        discardedComponents.add(player.getHeldComponent());
        player.resetHeldComponent();
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

    @Override
    public void startBuildPhaseTimer(GameManager gm) {
        Executors.newScheduledThreadPool(1).schedule(() -> {
            completedTimers++;

            if (completedTimers >= requiredTimers) {
                gm.updateListeners(
                        new GameUpdate.GameUpdateBuilder(GameUpdateType.NEW_STATE)
                                .setNewSate("AddCrewmates")
                                .build()
                );
            } else {
                gm.updateListeners(
                        new GameUpdate.GameUpdateBuilder(GameUpdateType.TIMER_END)
                                .build()
                );
            }
        }, 5, TimeUnit.SECONDS);

        gm.updateListeners(
                new GameUpdate.GameUpdateBuilder(GameUpdateType.TIMER_START)
                        .build()
        );
    }
}