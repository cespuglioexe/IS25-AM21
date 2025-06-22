package it.polimi.it.galaxytrucker.model.adventurecards.interfaces;

import it.polimi.it.galaxytrucker.model.adventurecards.cards.AbandonedShip;
import it.polimi.it.galaxytrucker.model.adventurecards.cards.AbandonedStation;
import it.polimi.it.galaxytrucker.model.adventurecards.cards.BigMeteorSwarm;
import it.polimi.it.galaxytrucker.model.adventurecards.cards.CombatZone;
import it.polimi.it.galaxytrucker.model.adventurecards.cards.MeteorSwarm;
import it.polimi.it.galaxytrucker.model.adventurecards.cards.OpenSpace;
import it.polimi.it.galaxytrucker.model.adventurecards.cards.Pirates;
import it.polimi.it.galaxytrucker.model.adventurecards.cards.Planets;
import it.polimi.it.galaxytrucker.model.adventurecards.cards.Slavers;
import it.polimi.it.galaxytrucker.model.adventurecards.cards.Smugglers;

public interface AdventureCardVisitor {
    public void visit(AbandonedShip card, AdventureCardInputContext context);
    public void visit(AbandonedStation card, AdventureCardInputContext context);
    public void visit(BigMeteorSwarm card, AdventureCardInputContext context);
    public void visit(CombatZone card, AdventureCardInputContext context);
    public void visit(MeteorSwarm card, AdventureCardInputContext context);
    public void visit(OpenSpace card, AdventureCardInputContext context);
    public void visit(Pirates card, AdventureCardInputContext context);
    public void visit(Planets card, AdventureCardInputContext context);
    public void visit(Slavers card, AdventureCardInputContext context);
    public void visit(Smugglers card, AdventureCardInputContext context);
}
