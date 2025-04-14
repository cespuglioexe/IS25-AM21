package it.polimi.it.galaxytrucker.networking.rmi.client;

import it.polimi.it.galaxytrucker.controller.GenericGameData;
import it.polimi.it.galaxytrucker.networking.messages.GameUpdate;
import it.polimi.it.galaxytrucker.networking.messages.UserInput;
import it.polimi.it.galaxytrucker.networking.rmi.server.RMIVirtualView;
import it.polimi.it.galaxytrucker.view.CLIView;
import it.polimi.it.galaxytrucker.view.ConsoleColors;
import it.polimi.it.galaxytrucker.view.statePattern.viewstates.ConnectionState;
import it.polimi.it.galaxytrucker.view.statePattern.viewstates.GameSelection;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class RMIClient extends UnicastRemoteObject implements RMIVirtualView {

    private final CLIView view;
    private RMIVirtualServer server;
    private String name;
    private UUID playerID;

    public RMIClient () throws RemoteException {
        super();
        this.view = new CLIView(this);
    }

    public static void main(String[] args) throws RemoteException, NotBoundException {
        new RMIClient().run();
    }

    private void run() throws RemoteException {
        view.start(new ConnectionState(view));
    }

    public List<GenericGameData> getActiveGames () throws RemoteException{
        return server.getControllers();
    }

    @Override
    public String getName() throws RemoteException {
        return name;
    }

    @Override
    public void showUpdate(Integer number) throws RemoteException {
        System.out.println("Server update: " + number);
    }

    @Override
    public void reportError(String message) throws RemoteException {
        System.err.println("Server error: " + message);
    }

    @Override
    public void recieveGameUpdate(GameUpdate update) throws RemoteException {
        switch (update.getInstructionType()) {
            case NEW_STATE:
                System.out.println("New state");
                break;
            case DRAWN_TILE:
                System.out.println("Drawn tile");
                view.displayComponentTile(update.getNewTile());
                break;
        }
    }

    public void receiveUserInput (UserInput input) {
        switch (input.getType()) {

            case CONNECT_SERVER:
                try {
                    Registry registry = LocateRegistry.getRegistry("127.0.0.1", 1234);
                    this.server = (RMIVirtualServer) registry.lookup(input.getServerName());
                    this.server.connect(this);

                    view.updateState(false);
                } catch (Exception e) {
                    System.err.println("Failed to connect to '" + input.getServerName());
                    e.printStackTrace();

                    view.updateState(true);
                }
                break;

            case SET_USERNAME:
                try {
                    if (!server.checkUsernameIsUnique(this, input.getPlayerName())) {
                        view.updateState(true);
                    }
                    name = input.getPlayerName();

                    view.changeState(new GameSelection(view));
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
                break;

            case GAME_CREATION:
                // When a player creates a new game, they are automatically added to that game
                try {
                    String gameNickname = "Game_" + new Random().nextInt(10000); // Generate a random nickname
                    server.newGame(gameNickname, input.getGamePlayers(), input.getGameLevel());

                    this.playerID = server.addPlayerToGame(this, gameNickname);
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;

            case GAME_SELECTION:
                try {
                    this.playerID = server.addPlayerToGame(this, input.getGameIndex());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
