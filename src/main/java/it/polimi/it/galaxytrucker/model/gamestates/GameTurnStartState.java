package it.polimi.it.galaxytrucker.model.gamestates;

import it.polimi.it.galaxytrucker.messages.servermessages.GameUpdate;
import it.polimi.it.galaxytrucker.messages.servermessages.GameUpdateType;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.AdventureCard;
import it.polimi.it.galaxytrucker.model.design.statePattern.StateMachine;
import it.polimi.it.galaxytrucker.model.managers.GameManager;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

public class GameTurnStartState extends GameState {
    @Override
    public void enter(StateMachine fsm) {
        GameManager game = (GameManager) fsm;

        try {
            AdventureCard card = game.getAdventureDeck().draw();
            fsm.changeState(new CardExecutionState(card));
        }
        catch (NoSuchElementException e) {
            System.out.println(game.getActivePlayers().stream().map(p -> p.getPlayerName()).toList());
            System.out.println(game.getFlightBoard().getPlayerOrder().stream().map(p -> p.getPlayerName()).toList());
            fsm.changeState(new GameEndState());
        }

        HashMap<UUID, Integer> uuidMap = (HashMap<UUID, Integer>) game.getFlightBoard().getPlayerPosition().entrySet().stream()
                .collect(Collectors.toMap(
                        entry -> entry.getKey().getPlayerID(),
                        Map.Entry::getValue
                ));

        game.getFlightBoard().updateListeners(
                new GameUpdate.GameUpdateBuilder(GameUpdateType.PLAYER_MARKER_MOVED)
                        .setPlayerMarkerPositions(uuidMap)
                        .build()
        );

    }

    @Override
    public void update(StateMachine fsm) {

    }

    @Override
    public void exit(StateMachine fsm) {

    }
}
