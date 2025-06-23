package it.polimi.it.galaxytrucker.networking.client;

import it.polimi.it.galaxytrucker.messages.clientmessages.UserInput;
import it.polimi.it.galaxytrucker.messages.servermessages.GameUpdate;
import it.polimi.it.galaxytrucker.exceptions.InvalidFunctionCallInState;
import it.polimi.it.galaxytrucker.model.componenttiles.TileData;
import it.polimi.it.galaxytrucker.networking.client.clientmodel.ClientModel;
import it.polimi.it.galaxytrucker.networking.utils.ServerDetails;
import it.polimi.it.galaxytrucker.view.CLI.ConsoleColors;
import it.polimi.it.galaxytrucker.view.View;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public abstract class Client extends UnicastRemoteObject implements Runnable, ClientInterface {
    /**
     * The client-side representation of the game model.
     * It stores the game state as perceived by this client.
     */
    protected final ClientModel model;
    /**
     * The user interface that this client interacts with.
     * The view is responsible for displaying game information and capturing user input.
     */
    protected final View view;
    /**
     * An executor service to send user input messages to the server asynchronously.
     * This ensures that the client's main thread is not blocked while sending data.
     */
    protected final ExecutorService commandSenderExecutor = Executors.newSingleThreadExecutor();
    /**
     * A flag indicating whether the building phase timer is currently active.
     */
    protected boolean buildingTimerIsActive = false;

    protected boolean connectedToServer;

    protected Client(View view) throws RemoteException {
        super();
        this.model = new ClientModel();
        this.view = view;
        view.setClient(this);
    }

    @Override
    public void run() {
        initiateServerConnection();

        connectedToServer = true;

        // Begin heartbeat for maintaining active connection
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(() -> commandSenderExecutor.submit(this::sendHeartbeat), 0, ServerDetails.HEARTBEAT_FREQUENCY, TimeUnit.SECONDS);

        view.begin();
    }

    protected abstract void sendHeartbeat();

    protected abstract void initiateServerConnection();

    @Override
    public ClientModel getModel() {
        synchronized (ClientModel.class) {
            return model;
        }
    }

    @Override
    public boolean isBuildingTimerIsActive() {
        return buildingTimerIsActive;
    }

    @Override
    public abstract void receiveUserInput(UserInput input);

    /**
     * Receives a game update message from the server and processes it.
     * The method updates the client's model and view based on the type of update received.
     *
     * @param update The {@link GameUpdate} object containing information from the server.
     * @throws RemoteException if an RMI communication error occurs (though typically handled by the RMI runtime).
     */
    public void processServerUpdate(GameUpdate update) throws RemoteException {
        System.out.println(ConsoleColors.CLIENT_DEBUG + "received update of type " + update.getInstructionType() + ConsoleColors.RESET);
        switch (update.getInstructionType()) {
            case SET_USERNAME_RESULT:
                if (update.isSuccessfulOperation()) {
                    synchronized (ClientModel.class) {
                        model.getMyData().setNickname(update.getPlayerName());
                    }
                    view.nameSelectionSuccess();
                } else {
                    view.nameNotAvailable();
                }
                break;

            case CREATE_GAME_RESULT:
                if (update.isSuccessfulOperation()) {
                    synchronized (ClientModel.class) {
                        model.getMyData().setMatchId(update.getGameUuid());
                        model.setGameLevel(update.getGameLevel());
                    }
                    try {
                        view.gameCreationSuccess(true);
                    } catch (InvalidFunctionCallInState e) {
                        // This error is only thrown when creating a 1-player game
                        // and can be ignored as it has no adverse effects.
                    }
                } else {
                    view.gameCreationSuccess(false);
                }
                break;

            case JOIN_GAME_RESULT:
                if (update.isSuccessfulOperation()) {
                    synchronized (ClientModel.class) {
                        model.getMyData().setMatchId(update.getGameUuid());
                        model.setGameLevel(update.getGameLevel());
                    }
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
                        synchronized (ClientModel.class) {
                            model.setGameLevel(update.getGameLevel());
                            for (Map.Entry<UUID, List<List<TileData>>> entry : ships.entrySet()) {
                                model.updatePlayerShip(entry.getKey(), entry.getValue());
                            }
                            model.setCardPiles(update.getCardPileCompositions());
                        }
                        view.buildingStarted();
                        break;
                    case "ShipFixingState":
                        if (update.getPlayerIds().stream().anyMatch(id -> id.equals(model.getMyData().getPlayerId()))) {
                            view.shipFixingState();
                        } else {
                            view.waitingForGameState();
                        }
                        break;
                    case "CardExecutionState":
                        //model.setShipManager(update.getShipManager());
                        model.setActiveCardGraphicPath(update.getOperationMessage());
                        view.newCardStartedExecution();
                        break;
                    default:
                        System.out.println(ConsoleColors.CLIENT_DEBUG + "Received unknown state: " + update.getNewSate() + ConsoleColors.RESET);
                        break;
                }
                break;
            case DRAWN_TILE:
                view.componentTileReceived(update.getNewTile());
                break;
            case SAVED_COMPONENTS_UPDATED:
                synchronized (ClientModel.class) {
                    model.setSavedTiles(update.getTileList());
                }
                view.savedComponentsUpdated();
                break;
            case DISCARDED_COMPONENTS_UPDATED:
                synchronized (ClientModel.class) {
                    model.setDiscardedTiles(update.getTileList());
                }
                view.discardedComponentsUpdated();
                break;
            case PLAYER_SHIP_UPDATED:
                synchronized (ClientModel.class) {
                    model.updatePlayerShip(update.getInterestedPlayerId(), update.getShipBoard());
                }
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
            case CARD_DETAILS:
                //TODO
                break;
            case INPUT:
                if (update.getInterestedPlayerId().equals(model.getMyData().getPlayerId())) {
                    view.displayInputOptions(update.getOperationMessage(), update.getNewSate());
                }
            case INVALID_INPUT:
                //TODO
                break;
            default:
                System.out.println(ConsoleColors.CLIENT_DEBUG + "Received unhandled game update instruction type: " + update.getInstructionType() + ConsoleColors.RESET);
                break;
        }
    }
}
