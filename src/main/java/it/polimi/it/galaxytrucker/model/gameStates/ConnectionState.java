package it.polimi.it.galaxytrucker.model.gameStates;

import it.polimi.it.galaxytrucker.model.design.statePattern.StateMachine;
import it.polimi.it.galaxytrucker.exceptions.InvalidActionException;
import it.polimi.it.galaxytrucker.exceptions.InvalidFunctionCallInState;
import it.polimi.it.galaxytrucker.exceptions.NotFoundException;
import it.polimi.it.galaxytrucker.model.gameStates.fields.ConnectionFields;
import it.polimi.it.galaxytrucker.model.managers.GameManager;
import it.polimi.it.galaxytrucker.model.managers.Model;
import it.polimi.it.galaxytrucker.model.managers.Player;

import java.util.UUID;

public class ConnectionState extends GameState {
    @Override
    public void enter(StateMachine fsm) {
    }

    @Override
    public void update(StateMachine fsm) {
        Model model = (Model) fsm;

        if (areAllFieldsSet(model)) {
            changeState(fsm, new BuildingState());
        }
    }

    private boolean areAllFieldsSet(Model model) {
        for (ConnectionFields field : ConnectionFields.values()) {
            if (!field.isSet(model)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void exit(StateMachine fsm) {
        
    }

    // STATE SPECIFIC FUNCITONALITY

    @Override
    public void addPlayer(StateMachine fsm, Player player) throws InvalidActionException, InvalidFunctionCallInState {
        GameManager gameManager = (GameManager) fsm;
        gameManager.getPlayers().add(player);

        update(fsm);
    }

    @Override
    public void removePlayer(StateMachine fsm, UUID id) throws InvalidFunctionCallInState {
        GameManager game = (GameManager) fsm;

        Player playerToRemove = game.getPlayers().stream()
            .filter(player -> player.getPlayerID().equals(id))
            .findFirst()
            .orElseThrow(() -> new NotFoundException("Cannot find the player"));

        game.getPlayers().remove(playerToRemove);
    }
}
