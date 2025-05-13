package it.polimi.it.galaxytrucker.networking.server.rmi;

import it.polimi.it.galaxytrucker.commands.UserInput;
import it.polimi.it.galaxytrucker.commands.UserInputType;
import it.polimi.it.galaxytrucker.commands.servercommands.GameUpdate;
import it.polimi.it.galaxytrucker.controller.GenericGameData;
import it.polimi.it.galaxytrucker.listeners.Listener;
import it.polimi.it.galaxytrucker.exceptions.GameFullException;
import it.polimi.it.galaxytrucker.networking.client.rmi.RMIVirtualServer;
import it.polimi.it.galaxytrucker.networking.server.ClientHandler;
import it.polimi.it.galaxytrucker.networking.server.Server;
import it.polimi.it.galaxytrucker.networking.server.ServerInterface;
import it.polimi.it.galaxytrucker.view.CLI.ConsoleColors;

import java.rmi.RemoteException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;

public class RMIClientHandler extends ClientHandler implements Listener, RMIVirtualServer {
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    private final ServerInterface server;
    private final RMIVirtualClient client;
    private final BlockingQueue<GameUpdate> messageQueue = new LinkedBlockingQueue<>();

    private String clientName = ""; // Used exclusively for debug print purposes

    public RMIClientHandler(Server server, RMIVirtualClient client) throws RemoteException {
        super();
        this.server = server;
        this.client = client;
    }

    @Override
    public void notify(GameUpdate update) {
        boolean result = false;
        do {
            result = messageQueue.offer(update);
        } while (!result);
    }

    public void run() {
        while (true) {
            try {
                GameUpdate message = messageQueue.take();
                System.out.println(ConsoleColors.CLIENT_HANDLER_DEBUG.tag(clientName) + "sending update of type " + message.getInstructionType());
                new Thread(() -> {
                    try {
                        client.sendMessageToClient(message); // NEVER RETURNS FROM FUNCTION CALL
                    } catch (RemoteException e) {
                        System.out.println(ConsoleColors.CLIENT_HANDLER_DEBUG.tag(clientName) + ConsoleColors.RED_BOLD + "NO LONGER READING FROM MESSAGE QUEUE" + ConsoleColors.RESET);
                        throw new RuntimeException(e);
                    }
                }).start();
            } catch (InterruptedException e) {
                System.out.println(ConsoleColors.CLIENT_HANDLER_DEBUG.tag(clientName) + ConsoleColors.RED_BOLD + "NO LONGER READING FROM MESSAGE QUEUE" + ConsoleColors.RESET);
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public List<GenericGameData> getControllers() throws RemoteException {
        synchronized (server) {
            return server.getActiveGames();
        }
    }

    @Override
    public boolean setPlayerName(String username) throws RemoteException {
        synchronized (server) {
            clientName = username; // Used exclusively for debug print purposes
            return server.setUsername(this, username);
        }
    }

    @Override
    public UUID addPlayerToGame(UUID gameId) throws RemoteException, GameFullException {
        synchronized (server) {
            return server.addPlayerToGame(this, gameId);
        }
    }

    @Override
    public UUID newGame(int players, int level) throws RemoteException {
        synchronized (server) {
            return server.createNewGame(players, level);
        }
    }

    @Override
    public void sendMessageToGame(UserInput input) throws RemoteException {
        System.out.println(ConsoleColors.CLIENT_HANDLER_DEBUG.tag(clientName) + "received input of type " + input.getType() + (input.getType().equals(UserInputType.REQUEST) ? "- " + input.getRequestType() : ""));
        switch (input.getType()) {
            case PLACE_COMPONENT:
                this.getController().placeComponentTile(this.getUuid(), input.getCoords().get(1), input.getCoords().get(0),input.getRotation());
                break;
            case SAVE_COMPONENT:
                this.getController().saveComponentTile(this.getUuid());
                break;
            case DISCARD_COMPONENT:
                this.getController().discardComponentTile(this.getUuid());
                break;
            case REQUEST:
                switch (input.getRequestType()) {
                    case NEW_TILE:
                        this.getController().requestNewComponentTile(this.getUuid());
                        break;
                    case SAVED_TILES:
                        this.getController().requestSavedComponentTiles(this.getUuid());
                        break;
                    case DISCARDED_TILES:
                        this.getController().requestDiscardedComponentTiles(this.getUuid());
                        break;
                    case CARD_PILE:
                        this.getController().getCardPile(this.getUuid(), input.getCardPileIndex());
                        break;
                    case SHIP_BOARD:
                        this.getController().requestShipBoard(this.getUuid());
                        break;
                    case SELECT_SAVED_TILE:
                        this.getController().selectSavedComponentTile(this.getUuid(), input.getSelectedTileIndex());
                        break;
                    case SELECT_DISCARDED_TILE:
                        this.getController().selectDiscardedComponentTile(this.getUuid(), input.getSelectedTileIndex());
                        break;
                }
                break;
        }
    }

    @Override
    public void startBuildingPhaseTimer() throws RemoteException {
        getController().startBuildPhaseTimer();
    }
}
