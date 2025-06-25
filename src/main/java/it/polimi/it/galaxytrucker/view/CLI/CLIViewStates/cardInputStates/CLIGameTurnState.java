package it.polimi.it.galaxytrucker.view.CLI.CLIViewStates.cardInputStates;

import it.polimi.it.galaxytrucker.view.CLI.CLIViewStates.CLIViewState;

public class CLIGameTurnState extends CLIViewState {
    @Override
    public void executeState() {
        view.printFlightBoard();
    }

    @Override
    public void displayCard() {
        view.printAdventureCard(view.getClient().getModel().getActiveCardGraphicPath());
    }
}
