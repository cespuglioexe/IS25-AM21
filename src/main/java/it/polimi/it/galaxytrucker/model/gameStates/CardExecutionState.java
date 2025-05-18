package it.polimi.it.galaxytrucker.model.gameStates;

import it.polimi.it.galaxytrucker.model.adventurecards.cardevents.CardResolved;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.AdventureCard;
import it.polimi.it.galaxytrucker.model.design.observerPattern.Observer;
import it.polimi.it.galaxytrucker.model.design.observerPattern.Subject;
import it.polimi.it.galaxytrucker.model.design.statePattern.StateMachine;

public class CardExecutionState extends GameState implements Observer {
    AdventureCard currentCard;

    public CardExecutionState(AdventureCard currentCard) {
        this.currentCard = currentCard;
    }

    @Override
    public void enter(StateMachine fsm) {
        ((Subject) currentCard).addObserver(this);
        currentCard.play();
    }

    @Override
    public void update(StateMachine fsm) {

    }

    @Override
    public void exit(StateMachine fsm) {

    }

    @Override
    public void notify(Object o) {
        if (o instanceof CardResolved) {
            System.out.println("Card finished execution");
        }
    }
}
