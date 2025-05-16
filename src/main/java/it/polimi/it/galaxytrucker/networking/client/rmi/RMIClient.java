package it.polimi.it.galaxytrucker.networking.client.rmi;

import it.polimi.it.galaxytrucker.commands.GameError;
import it.polimi.it.galaxytrucker.commands.UserInput;
import it.polimi.it.galaxytrucker.commands.UserInputType;
import it.polimi.it.galaxytrucker.exceptions.InvalidFunctionCallInState;
import it.polimi.it.galaxytrucker.model.componenttiles.TileData;
import it.polimi.it.galaxytrucker.commands.servercommands.GameUpdate;
import it.polimi.it.galaxytrucker.networking.client.Client;
import it.polimi.it.galaxytrucker.networking.client.clientmodel.ClientModel;
import it.polimi.it.galaxytrucker.networking.server.rmi.RMIServer;
import it.polimi.it.galaxytrucker.networking.server.rmi.RMIVirtualClient;
import it.polimi.it.galaxytrucker.view.View;
import it.polimi.it.galaxytrucker.view.CLI.ConsoleColors;


import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RMIClient extends UnicastRemoteObject implements RMIVirtualClient, Runnable, Client {

    private final ClientModel model;
    private RMIVirtualServer server;
    private final View view;

    private final ExecutorService commandSenderExecutor = Executors.newSingleThreadExecutor();

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

    // Client interface //

    @Override
    public ClientModel getModel() {
        return model;
    }

    @Override
    public boolean isBuildingTimerIsActive() {
        return buildingTimerIsActive;
    }

    // RMIVirtualClient interface //

    @Override
    public void setHandler(RMIVirtualServer handler) throws RemoteException {
        server = handler;
        receiveUserInput(new UserInput.UserInputBuilder(UserInputType.HANDSHAKE)
                .setPlayerUuid(model.getMyData().getPlayerId())
                .build()
        );
    }

    // Internal logic //

    @Override
    public void sendMessageToClient(GameUpdate update) throws RemoteException {
        System.out.println(ConsoleColors.CLIENT_DEBUG + "received update of type " + update.getInstructionType() + ConsoleColors.RESET);
        switch (update.getInstructionType()) {
            case SET_USERNAME_RESULT:
                if (update.isSuccessfulOperation()) {
                    model.getMyData().setNickname(update.getPlayerName());
                    view.gameSelectionScreen();
                } else {
                    view.nameNotAvailable();
                }
                break;

            case CREATE_GAME_RESULT:
                if (update.isSuccessfulOperation()) {
                    model.getMyData().setMatchId(update.getGameUuid());
                    try {
                        view.gameCreationSuccess(true);
                    } catch (InvalidFunctionCallInState e) {
                        // This error is only thrown when creating a 1 player game
                        // It can be ignored as it has no adverse effects
                    }
                } else {
                    view.gameCreationSuccess(false);
                }
                break;

            case JOIN_GAME_RESULT:
                if (update.isSuccessfulOperation()) {
                    model.getMyData().setMatchId(update.getGameUuid());
                } else if (update.getOperationMessage().equals("The game was full")) {
                    view.joinedGameIsFull();
                }
                break;

            case LIST_ACTIVE_CONTROLLERS:
                view.activeControllers(update.getActiveControllers());
                break;

            case NEW_STATE:
                switch (update.getNewSate()) {
                    case "BuildingState":
                        HashMap<UUID, List<List<TileData>>> ships = update.getAllPlayerShipBoard();
                        System.out.println(ConsoleColors.CLIENT_HANDLER_DEBUG + ships.values().toString());
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
                view.componentTileReceived(update.getNewTile());
                // view.tileActions();
                break;
            case SAVED_COMPONENTS_UPDATED:
                model.setSavedTiles(update.getTileList());
                view.savedComponentsUpdated();
                break;
            case DISCARDED_COMPONENTS_UPDATED:
                model.setDiscardedTiles(update.getTileList());
                view.discardedComponentsUpdated();
                break;
            case PLAYER_SHIP_UPDATED:
                model.updatePlayerShip(update.getInterestedPlayerId(), update.getShipBoard());
                view.shipUpdated(update.getInterestedPlayerId());
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
    public void reportErrorToClient(GameError error) throws RemoteException {

    }

    @Override
    public void receiveUserInput(UserInput input) {
        commandSenderExecutor.submit(() -> {
            try {
                server.receiveUserInput(input);
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }
        });
//        switch (input.getType()) {
//            case SET_PLAYER_USERNAME:
//                try {
//                    if(server.setPlayerName(input.getPlayerName())) {
//                        this.model.getMyData().setNickname(input.getPlayerName());
//                        view.gameSelectionScreen();
//                    } else {
//                        view.nameNotAvailable();
//                    }
//                }
//                catch (Exception e) {
//                    e.printStackTrace();
//                }
//                break;
//
//            case GAME_CREATION:
//                // When a player creates a new game, they are automatically added to that game
//                try {
//                    UUID gameId = server.newGame(input.getGamePlayers(), input.getGameLevel());
//                    UUID playerId = server.addPlayerToGame(gameId);
//
//                    this.model.getMyData().setPlayerId(playerId);
//                    this.model.getMyData().setMatchId(gameId);
//
//                    view.gameCreationSuccess(true);
//                } catch (GameFullException e) {
//                    // Should never happen
//                    view.joinedGameIsFull();
//                    System.out.println(ConsoleColors.RED + "The game you tried to join is already full" + ConsoleColors.RESET); // Should never happen
//                } catch (RemoteException e) {
//                    view.remoteExceptionThrown();
//                }
//                break;
//
//            case GAME_SELECTION:
//                try {
//                    UUID playerId = server.addPlayerToGame(input.getGameId());
//                    UUID gameId = input.getGameId();
//
//                    this.model.getMyData().setPlayerId(playerId);
//                    this.model.getMyData().setMatchId(gameId);
//                } catch (GameFullException e) {
//                    view.joinedGameIsFull();
//                    System.out.println(ConsoleColors.RED + "The game you tried to join is already full" + ConsoleColors.RESET);
//                    view.joinedGameIsFull();
//                } catch (RemoteException e) {
//                    view.remoteExceptionThrown();
//                }
//                break;
//
//            case START_TIMER:
//                try {
//                    this.server.startBuildingPhaseTimer();
//                } catch (RemoteException e) {
//                    e.printStackTrace();
//                }
//                break;
//
//            default:
//                new Thread(() -> {
//                    server.
//                }).start();
//                break;
//        }
    }
}
