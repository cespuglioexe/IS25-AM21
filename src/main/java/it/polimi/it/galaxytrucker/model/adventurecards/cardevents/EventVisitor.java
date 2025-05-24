package it.polimi.it.galaxytrucker.model.adventurecards.cardevents;

public interface EventVisitor {
    public void visit(CardResolved event);
    public void visit(InputNeeded event);
}
