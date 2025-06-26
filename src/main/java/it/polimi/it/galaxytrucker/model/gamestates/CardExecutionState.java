package it.polimi.it.galaxytrucker.model.gamestates;

import it.polimi.it.galaxytrucker.exceptions.InvalidActionException;
import it.polimi.it.galaxytrucker.messages.servermessages.GameUpdate;
import it.polimi.it.galaxytrucker.messages.servermessages.GameUpdateType;
import it.polimi.it.galaxytrucker.model.adventurecards.cardevents.CardEvent;
import it.polimi.it.galaxytrucker.model.adventurecards.cardevents.CardResolved;
import it.polimi.it.galaxytrucker.model.adventurecards.cardevents.EventVisitor;
import it.polimi.it.galaxytrucker.model.adventurecards.cardevents.InputNeeded;
import it.polimi.it.galaxytrucker.model.adventurecards.cardevents.UpdateStatus;
import it.polimi.it.galaxytrucker.model.adventurecards.cardstates.EndState;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.AdventureCard;
import it.polimi.it.galaxytrucker.model.design.observerPattern.Observer;
import it.polimi.it.galaxytrucker.model.design.observerPattern.Subject;
import it.polimi.it.galaxytrucker.model.design.statePattern.State;
import it.polimi.it.galaxytrucker.model.design.statePattern.StateMachine;
import it.polimi.it.galaxytrucker.model.managers.GameManager;

public class CardExecutionState extends GameState implements Observer, EventVisitor {
    AdventureCard currentCard;
    StateMachine fsm;

    public CardExecutionState(AdventureCard currentCard) {
        this.currentCard = currentCard;
    }

    @Override
    public void enter(StateMachine fsm) {
        this.fsm = fsm;

        ((GameManager) fsm).updateListeners(new GameUpdate.GameUpdateBuilder(GameUpdateType.NEW_STATE)
                .setNewSate(CardExecutionState.class.getSimpleName())
                .setOperationMessage(((GameManager) fsm).getAdventureDeck().getLastDrawnCard().getGraphicPath())
                .build()
        );

        ((Subject) currentCard).addObserver(this);

        try {
            currentCard.play();
        } catch (InvalidActionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(StateMachine fsm) {
        changeState(fsm, new DefeatCheckState());
    }

    @Override
    public void exit(StateMachine fsm) {

    }

    @Override
    public void notify(Object o) {
        if (o instanceof CardEvent e) {
            e.accept(this);
        }
    }

    @Override
    public void visit(InputNeeded event) {
        AdventureCard card = event.getSource();
        State currentState = ((StateMachine) card).getCurrentState();

        if (currentState instanceof EndState) {
            return;
        }

        System.out.println("Current card: " + card.getClass().getSimpleName());
        System.out.println("Current state: " + currentState.getClass().getSimpleName());
        System.out.println("Card needs user input to proceed");
        System.out.println("Player: " + event.getInterestedPlayer().getPlayerName());

        ((GameManager) fsm).updateListenersCardNeedsInput(event);
    }

    @Override
    public void visit(UpdateStatus event) {
        ((GameManager) fsm).updateListenersCardDetails(event);
    }

    @Override
    public void visit(CardResolved event) {
        if (fsm.getCurrentState() instanceof DefeatCheckState || fsm.getCurrentState() instanceof GameEndState) {
            return;
        }
        System.out.println("Card finished execution");

        update(fsm);
    }
}
