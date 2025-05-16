package it.polimi.it.galaxytrucker.networking.client.socket;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import it.polimi.it.galaxytrucker.commands.UserInput;
import it.polimi.it.galaxytrucker.commands.UserInputType;
import it.polimi.it.galaxytrucker.commands.servercommands.GameUpdate;
import it.polimi.it.galaxytrucker.exceptions.InvalidFunctionCallInState;
import it.polimi.it.galaxytrucker.model.componenttiles.TileData;
import it.polimi.it.galaxytrucker.model.json.Json;
import it.polimi.it.galaxytrucker.networking.client.Client;
import it.polimi.it.galaxytrucker.networking.client.clientmodel.ClientModel;
import it.polimi.it.galaxytrucker.networking.server.socket.SocketVirtualClient;
import it.polimi.it.galaxytrucker.view.CLI.ConsoleColors;
import it.polimi.it.galaxytrucker.view.View;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Questa classe rappresenta la logica del client implementata con tecnologia Socket.
 */
public class SocketClient implements SocketVirtualClient, Runnable, Client {
    final BufferedReader input;
    final PrintWriter output;

    private final ClientModel model;
    private final View view;

    private final ExecutorService commandSenderExecutor = Executors.newSingleThreadExecutor();

    private boolean buildingTimerIsActive;

    public SocketClient(BufferedReader input, BufferedWriter output, View view) {
        this.input = input;
        this.output = new PrintWriter(output);
        this.view = view;
        view.setClient(this);
        this.model = new ClientModel();
    }

    public void run() {
        new Thread(() -> {
            try {
                serverMessageReader();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }).start();

        receiveUserInput(new UserInput.UserInputBuilder(UserInputType.HANDSHAKE)
                .setPlayerUuid(model.getMyData().getPlayerId())
                .build()
        );

        view.begin();
    }

    private void serverMessageReader() throws IOException {
        String line;
        while ((line = input.readLine()) != null) {
            JsonNode node = Json.parse(line);
            GameUpdate command = Json.fromJson(node, GameUpdate.class);

            executeServerMessage(command);
        }
    }

    public void executeServerMessage(GameUpdate update) {
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
    public ClientModel getModel() {
        return model;
    }

    @Override
    public boolean isBuildingTimerIsActive() {
        return buildingTimerIsActive;
    }

    @Override
    public void receiveUserInput(UserInput input) {
        commandSenderExecutor.submit(() -> {
            ObjectWriter ow = new ObjectMapper().writer();
            try {
                String jsonMessage = ow.writeValueAsString(input);
                System.out.println(ConsoleColors.CLIENT_DEBUG + "sending message" + jsonMessage + ConsoleColors.RESET);
                output.println(jsonMessage);
                output.flush();
                System.out.println(ConsoleColors.CLIENT_DEBUG + "printed message to socket" + ConsoleColors.RESET);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
