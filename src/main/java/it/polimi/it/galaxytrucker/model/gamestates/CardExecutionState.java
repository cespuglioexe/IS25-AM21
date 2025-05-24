package it.polimi.it.galaxytrucker.model.gamestates;

import it.polimi.it.galaxytrucker.model.adventurecards.cardevents.CardEvent;
import it.polimi.it.galaxytrucker.model.adventurecards.cardevents.CardResolved;
import it.polimi.it.galaxytrucker.model.adventurecards.cardevents.EventVisitor;
import it.polimi.it.galaxytrucker.model.adventurecards.cardevents.InputNeeded;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.AdventureCard;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.attack.Attack;
import it.polimi.it.galaxytrucker.model.design.observerPattern.Observer;
import it.polimi.it.galaxytrucker.model.design.observerPattern.Subject;
import it.polimi.it.galaxytrucker.model.design.statePattern.State;
import it.polimi.it.galaxytrucker.model.design.statePattern.StateMachine;

public class CardExecutionState extends GameState implements Observer, EventVisitor {
    AdventureCard currentCard;
    StateMachine fsm;

    public CardExecutionState(AdventureCard currentCard) {
        this.currentCard = currentCard;
    }

    @Override
    public void enter(StateMachine fsm) {
        this.fsm = fsm;

        ((Subject) currentCard).addObserver(this);
        currentCard.play();
    }

    @Override
    public void update(StateMachine fsm) {
        if (currentCard instanceof Attack) {
            
        } else {
            fsm.changeState(new GameTurnStartState());
        }
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

        System.out.println("Current card: " + card.getClass().getSimpleName());

        State currentState = ((StateMachine) card).getCurrentState();
        System.out.println("Current state: " + currentState.getClass().getSimpleName());

        System.out.println("Card needs user input to proceed");
    }

    @Override
    public void visit(CardResolved event) {
        System.out.println("Card finished execution");

        update(fsm);
    }
}
