package it.polimi.it.galaxytrucker.gameStates.prova;

public class View {
    private Controller controller;

    public View(Controller controller) {
        this.controller = controller;
    }

    public void askForLevel() {
        System.out.println("Welcome to Galaxy Trucker!");
        System.out.println("Choose game level: ");
        System.out.println("Chosen level: 2");
    
        controller.setLevel(2);
    }

    public void askForNumberOfPlayers() {
        System.out.println("Choose number of players: ");
        System.out.println("Chosen players: 4");
    
        controller.setNumberOfPlayers(4);
    }

    public void askForName() {
        System.out.println("New player connected!: ");
        System.out.println("Choose name: ");
        System.out.println("Chosen name: Margarozzo");
    
        controller.addNewPlayer("Margarozzo");
    }
}
