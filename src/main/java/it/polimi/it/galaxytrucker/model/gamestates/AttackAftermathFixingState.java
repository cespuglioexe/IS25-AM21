package it.polimi.it.galaxytrucker.model.gamestates;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import it.polimi.it.galaxytrucker.exceptions.InvalidActionException;
import it.polimi.it.galaxytrucker.exceptions.InvalidFunctionCallInState;
import it.polimi.it.galaxytrucker.messages.servermessages.GameUpdate;
import it.polimi.it.galaxytrucker.messages.servermessages.GameUpdateType;
import it.polimi.it.galaxytrucker.model.design.statePattern.StateMachine;
import it.polimi.it.galaxytrucker.model.managers.GameManager;
import it.polimi.it.galaxytrucker.model.managers.Player;
import it.polimi.it.galaxytrucker.model.managers.ShipManager;

public class AttackAftermathFixingState extends GameState {
    private final HashMap<UUID, Boolean> playerHasIllegalShip;
    private final HashMap<UUID, Boolean> playerHasFinished;

    public AttackAftermathFixingState(List<Player> playersWithIllegalShips) {
        playerHasIllegalShip = new HashMap<>();
        playerHasFinished = new HashMap<>();

        for (Player player : playersWithIllegalShips) {
            playerHasIllegalShip.put(player.getPlayerID(), true);
            playerHasFinished.put(player.getPlayerID(), false);
        }
    }

    @Override
    public void enter(StateMachine fsm) {
        GameManager gameManager = (GameManager) fsm;

        List<Player> playersWithLegalShips = gameManager.getActivePlayers().stream()
            .filter(player -> !playerHasIllegalShip.keySet().contains(player.getPlayerID()))
            .toList();
        
        for (Player player : playersWithLegalShips) {
            this.playerHasIllegalShip.put(player.getPlayerID(), false);
        }

        for (UUID id : playerHasIllegalShip.keySet()) {
            Player player = gameManager.getPlayerByID(id);

            ((GameManager) fsm).updateListeners(new GameUpdate.GameUpdateBuilder(GameUpdateType.PLAYER_SHIP_UPDATED)
                .setShipBoard(player.getShipManager().getShipBoard())
                .build()
            );
        }

        ((GameManager) fsm).updateListeners(new GameUpdate.GameUpdateBuilder(GameUpdateType.NEW_STATE)
                .setNewSate(AttackAftermathFixingState.class.getSimpleName())
                .setPlayerIds(playerHasIllegalShip.keySet().stream()
                    .filter(p -> playerHasIllegalShip.get(p))
                    .toList()
                )
                
                .build()
        );
    }

    @Override
    public void update(StateMachine fsm) {
        for (UUID playerID : playerHasFinished.keySet()) {
            if (!playerHasFinished.get(playerID)) {
                return;
            }
        }
        changeState(fsm, new AttackAftermathCheckState());
    }

    @Override
    public void exit(StateMachine fsm) {
        
    }
    
    // STATE SPECIFIC FUNCITONALITY

    @Override
    public void deleteComponentTile(StateMachine fsm, UUID playerID, int row, int column) throws InvalidFunctionCallInState {
        GameManager game = (GameManager) fsm;

        ShipManager ship = game.getPlayerByID(playerID).getShipManager();
        ship.removeComponentTile(row, column);
    }

    @Override
    public void removeBranch(StateMachine fsm, UUID playerID, Set<List<Integer>> branch) {
        GameManager gameManager = (GameManager) fsm;
        Player player = gameManager.getPlayerByID(playerID);

        if (playerCannotEditTheShip(gameManager, playerID)) {
            throw new InvalidActionException(player.getPlayerName() + " cannot fix his ship");
        }

        ShipManager ship = player.getShipManager();
        ship.removeBranch(branch);
    }
    private boolean playerCannotEditTheShip(GameManager gameManager, UUID playerID) {
        if (!playerHasIllegalShip.get(playerID)) {
            return true;
        }
        if (playerHasFinished.get(playerID)) {
            return true;
        }
        return false;
    }

    @Override
    public void finishBuilding(StateMachine fsm, UUID playerID) throws InvalidFunctionCallInState {
        GameManager gameManager = (GameManager) fsm;
        Player player = gameManager.getPlayerByID(playerID);

        if (playerCannotEditTheShip(gameManager, playerID)) {
            throw new InvalidActionException(player.getPlayerName() + " cannot fix his ship");
        }

        playerHasFinished.put(playerID, true);
        update(fsm);
    }
}
