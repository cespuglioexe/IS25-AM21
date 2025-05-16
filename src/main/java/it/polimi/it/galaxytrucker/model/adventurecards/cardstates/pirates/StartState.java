package it.polimi.it.galaxytrucker.model.adventurecards.cardstates.pirates;

import it.polimi.it.galaxytrucker.model.adventurecards.cards.Pirates;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.attack.Projectile;
import it.polimi.it.galaxytrucker.model.design.statePattern.State;
import it.polimi.it.galaxytrucker.model.design.statePattern.StateMachine;

public class StartState extends State {

    @Override
    public void enter(StateMachine fsm) {
        Pirates card = (Pirates) fsm;
        card.selectPlayer();
        for (Projectile projectile : card.getProjectiles().stream().toList()) {
            card.aimAtCoordsWith(projectile);
        }
        if(card.getPlayersAndFirePower().size() == card.getNumberOfBoardPlayers())
            fsm.changeState(new EvaluatePlayerState());
        else fsm.changeState(new CalculateFirePowerState());
    }

    @Override
    public void update(StateMachine fsm) {

    }

    @Override
    public void exit(StateMachine fsm) {

    }
}
