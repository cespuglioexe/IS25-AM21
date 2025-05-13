package it.polimi.it.galaxytrucker.networking.client.rmi;

import it.polimi.it.galaxytrucker.commands.UserInput;
import it.polimi.it.galaxytrucker.controller.GenericGameData;
import it.polimi.it.galaxytrucker.model.componenttiles.TileData;
import it.polimi.it.galaxytrucker.exceptions.GameFullException;
import it.polimi.it.galaxytrucker.commands.servercommands.GameUpdate;
import it.polimi.it.galaxytrucker.networking.client.Client;
import it.polimi.it.galaxytrucker.networking.client.clientmodel.ClientModel;
import it.polimi.it.galaxytrucker.networking.server.rmi.RMIServer;
import it.polimi.it.galaxytrucker.networking.server.rmi.RMIVirtualClient;
import it.polimi.it.galaxytrucker.view.View;
//import it.polimi.it.galaxytrucker.view.cli.view;
import it.polimi.it.galaxytrucker.view.CLI.ConsoleColors;
//import it.polimi.it.galaxytrucker.view.cli.statePattern.viewstates.BuildingStateMenu;
//import it.polimi.it.galaxytrucker.view.cli.statePattern.viewstates.GameSelection;
//import it.polimi.it.galaxytrucker.view.cli.statePattern.viewstates.NameSelectionState;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;

public class RMIClient extends UnicastRemoteObject implements RMIVirtualClient, Runnable, Client {

    private final ClientModel model;
    private RMIVirtualServer server;
    private final View view;

    private boolean buildingTimerIsActive;

    public RMIClient (View view) throws RemoteException {
        super();
        this.model = new ClientModel();
        this.view = view;
        view.setClient(this);
    }

    @Override
    public void run() {
        boolean connected = false;
        Scanner scanner = new Scanner(System.in);
        System.out.print("Insert server name\n> ");

        do {
            String server = scanner.nextLine();

            try {
                Registry registry = LocateRegistry.getRegistry("localhost", 1234);
                RMIServer connectionServer = ((RMIServer) registry.lookup(server));
                connectionServer.connect(this);
                connected = true;
            } catch (Exception e) {
                System.out.println(ConsoleColors.RED + "Failed to connect to '" + server + "'" + ConsoleColors.RESET);
                System.out.print("Insert server name\n> ");
            }
        } while (!connected);

        view.begin();
    }

    @Override
    public ClientModel getModel() {
        return model;
    }

    @Override
    public boolean isBuildingTimerIsActive() {
        return buildingTimerIsActive;
    }

    @Override
    public List<GenericGameData> getActiveGames() throws RemoteException{
        return server.getControllers();
    }

    @Override
    public void setHandler(RMIVirtualServer handler) throws RemoteException {
        server = handler;
    }

    @Override
    public void sendMessageToClient(GameUpdate update) throws RemoteException {
        System.out.println(ConsoleColors.CLIENT_DEBUG + "received update of type " + update.getInstructionType() + ConsoleColors.RESET);
        switch (update.getInstructionType()) {
            case NEW_STATE:
                switch (update.getNewSate()) {
                    case "BuildingState":
                        HashMap<UUID, List<List<TileData>>> ships = update.getAllPlayersShipboard();
                        for (UUID playerId : ships.keySet()) {
                            model.updatePlayerShip(playerId, ships.get(playerId));
                        }
                        // model.setCardPiles(update.getCardPileCompositions());

                        view.buildingStarted();
                        break;
                    default:
                        break;
                }
                break;
            case DRAWN_TILE:
                view.displayComponentTile(update.getNewTile());
                // view.tileActions();
                break;
            case SAVED_COMPONENTS_UPDATED:
                model.setSavedTiles(update.getTileList());
                break;
            case DISCARDED_COMPONENTS_UPDATED:
                model.setDiscardedTiles(update.getTileList());
                break;
            case PLAYER_SHIP_UPDATED:
                model.updatePlayerShip(update.getInterestedPlayerId(), update.getShipBoard());
                break;
            case TIMER_START:
                buildingTimerIsActive = true;
                view.displayTimerStarted();
                break;
            case TIMER_END:
                buildingTimerIsActive = false;
                view.displayTimerEnded();
                break;
        }
    }

    @Override
    public void receiveUserInput(UserInput input) {
        switch (input.getType()) {
            case SET_USERNAME:
                try {
                    if(server.setPlayerName(input.getPlayerName())) {
                        this.model.getMyData().setNickname(input.getPlayerName());
                        view.gameSelectionScreen();
                    } else {
                        view.nameNotAvailable();
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case GAME_CREATION:
                // When a player creates a new game, they are automatically added to that game
                try {
                    UUID gameId = server.newGame(input.getGamePlayers(), input.getGameLevel());
                    UUID playerId = server.addPlayerToGame(gameId);

                    this.model.getMyData().setPlayerId(playerId);
                    this.model.getMyData().setMatchId(gameId);

                    view.gameCreationSuccess(true);
                } catch (GameFullException e) {
                    // Should never happen
                    view.joinedGameIsFull();
                    System.out.println(ConsoleColors.RED + "The game you tried to join is already full" + ConsoleColors.RESET); // Should never happen
                } catch (RemoteException e) {
                    view.remoteExceptionThrown();
                }
                break;

            case GAME_SELECTION:
                try {
                    UUID playerId = server.addPlayerToGame(input.getGameId());
                    UUID gameId = input.getGameId();

                    this.model.getMyData().setPlayerId(playerId);
                    this.model.getMyData().setMatchId(gameId);
                } catch (GameFullException e) {
                    view.joinedGameIsFull();
                    System.out.println(ConsoleColors.RED + "The game you tried to join is already full" + ConsoleColors.RESET);
                    view.joinedGameIsFull();
                } catch (RemoteException e) {
                    view.remoteExceptionThrown();
                }
                break;

            case START_TIMER:
                try {
                    this.server.startBuildingPhaseTimer();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;

            default:
                try {
                    this.server.sendMessageToGame(input);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
                break;

        }
    }
}
