package it.polimi.it.galaxytrucker.model.gameStates;

import it.polimi.it.galaxytrucker.model.design.statePattern.StateMachine;
import it.polimi.it.galaxytrucker.exceptions.InvalidActionException;
import it.polimi.it.galaxytrucker.exceptions.InvalidFunctionCallInState;
import it.polimi.it.galaxytrucker.model.managers.FlightBoard;
import it.polimi.it.galaxytrucker.model.managers.GameManager;
import it.polimi.it.galaxytrucker.model.managers.Player;
import it.polimi.it.galaxytrucker.model.managers.ShipManager;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class ShipFixingState extends GameState {
    private final HashMap<UUID, Boolean> playerHasFinished;

    public ShipFixingState(List<Player> playersWithIllegalShips) {
        playerHasFinished = new HashMap<>();

        for (Player player : playersWithIllegalShips) {
            this.playerHasFinished.put(player.getPlayerID(), false);
        }
    }

    @Override
    public void enter(StateMachine fsm) {

    }

    @Override
    public void update(StateMachine fsm) {
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
    public void deleteComponentTile(StateMachine fsm, UUID playerID, int row, int column) throws InvalidFunctionCallInState {
        GameManager game = (GameManager) fsm;

        if (!playerHasFinished.containsKey(playerID)) {
            throw new InvalidActionException(game.getPlayerByID(playerID).getPlayerName() + " cannot fix his ship: he already has a valid one");
        }

        ShipManager ship = game.getPlayerByID(playerID).getShipManager();
        ship.removeComponentTile(row, column);
    }

    @Override
    public void finishBuilding(StateMachine fsm, UUID playerID) throws InvalidFunctionCallInState {
        GameManager game = (GameManager) fsm;

        if (!playerHasFinished.containsKey(playerID)) {
            throw new InvalidActionException(game.getPlayerByID(playerID).getPlayerName() + " cannot fix his ship: he already has a valid one");
        }

        playerHasFinished.put(playerID, true);

        FlightBoard flightBoard = game.getFlightBoard();
        flightBoard.addPlayerMarker(game.getPlayerByID(playerID));

        update(fsm);
    }
}
