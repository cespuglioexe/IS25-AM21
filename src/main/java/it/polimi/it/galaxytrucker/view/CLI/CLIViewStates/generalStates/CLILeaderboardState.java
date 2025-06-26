package it.polimi.it.galaxytrucker.view.CLI.CLIViewStates.generalStates;

import it.polimi.it.galaxytrucker.view.CLI.CLIViewStates.CLIViewState;
import it.polimi.it.galaxytrucker.view.GUI.GUIView;

import java.util.*;

public class CLILeaderboardState extends CLIViewState {
    @Override
    public void executeState() {
        view.executorService.submit(() -> {
            HashMap<UUID, String> playerList = GUIView.getInstance().getClient().getModel().getAllPlayersNickname();
            List<String> rankings = new ArrayList<>();

            for (Map.Entry<UUID, String> entry : playerList.entrySet()) {
                if(entry!=null){
                    rankings.add(entry.getValue());
                } else {
                    rankings.add("");
                }

            }

            System.out.println("""
                   \n  ____    _    __  __ _____   _____ ___ _   _ ___ ____  _   _ _____ ____  _\s
                     / ___|  / \\  |  \\/  | ____| |  ___|_ _| \\ | |_ _/ ___|| | | | ____|  _ \\| |
                    | |  _  / _ \\ | |\\/| |  _|   | |_   | ||  \\| || |\\___ \\| |_| |  _| | | | | |
                    | |_| |/ ___ \\| |  | | |___  |  _|  | || |\\  || | ___) |  _  | |___| |_| |_|
                     \\____/_/   \\_\\_|  |_|_____| |_|   |___|_| \\_|___|____/|_| |_|_____|____/(_)
                    \n""");
            System.out.println("Everyone is a winner, but some are more winners that others...");
            System.out.println("First position: " + rankings.get(0));
            System.out.println("Second position: " + rankings.get(1));
            System.out.println("Third position: " + rankings.get(2));
            System.out.println("Last position: " + rankings.get(3));
        });
    }
}
