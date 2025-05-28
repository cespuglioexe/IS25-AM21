package it.polimi.it.galaxytrucker.view.CLI.CLIViewStates;

import it.polimi.it.galaxytrucker.view.CLI.ConsoleColors;

public class ShipFixingState extends CLIViewState {
    @Override
    public void executeState() {
        view.displayFixShip();
        System.out.println(ConsoleColors.YELLOW_BOLD + "Oh no! It seems your ship doesn't follow regulations.\nPlease fix it before we can proceed!" + ConsoleColors.RESET);
    }
}
