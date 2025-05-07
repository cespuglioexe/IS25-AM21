package it.polimi.it.galaxytrucker.model.gameStates;

import it.polimi.it.galaxytrucker.model.design.statePattern.StateMachine;
import it.polimi.it.galaxytrucker.model.exceptions.InvalidActionException;
import it.polimi.it.galaxytrucker.model.exceptions.InvalidFunctionCallInState;
import it.polimi.it.galaxytrucker.model.exceptions.NotFoundException;
import it.polimi.it.galaxytrucker.model.gameStates.fields.ConnectionFields;
import it.polimi.it.galaxytrucker.model.managers.GameManager;
import it.polimi.it.galaxytrucker.model.managers.Model;
import it.polimi.it.galaxytrucker.model.managers.Player;
import it.polimi.it.galaxytrucker.model.managers.ShipManager;
import it.polimi.it.galaxytrucker.model.utility.Color;
import it.polimi.it.galaxytrucker.view.cli.ConsoleColors;

import java.util.Arrays;
import java.util.UUID;

public class ConnectionState extends GameState {
    @Override
    public void enter(StateMachine fsm) {
        System.out.println(ConsoleColors.BLUE_UNDERLINED + "\n> " + this.getClass().getSimpleName() + " <\n" + ConsoleColors.RESET);
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
    public UUID addPlayer(StateMachine fsm, String name) throws InvalidActionException, InvalidFunctionCallInState {

        GameManager gameManager = (GameManager) fsm;

        Color playerColor = findFirstAvailableColor(gameManager);
        Player newPlayer = new Player(UUID.randomUUID(), name, playerColor, new ShipManager(((Model) fsm).getLevel()));
        gameManager.getPlayers().add(newPlayer);

        update(fsm);

        return newPlayer.getPlayerID();
    }

    private Color findFirstAvailableColor(GameManager game) throws InvalidActionException {
        return Arrays.stream(Color.values())
            .filter(color -> game.getPlayers().stream()
                    .noneMatch(player -> player.getColor().equals(color)))
            .findFirst()
            .orElseThrow(() -> new InvalidActionException("No available color"));
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
