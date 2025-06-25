package it.polimi.it.galaxytrucker.view.CLI.CLIViewStates.cardInputStates;

import it.polimi.it.galaxytrucker.messages.clientmessages.UserInput;
import it.polimi.it.galaxytrucker.messages.clientmessages.UserInputType;
import it.polimi.it.galaxytrucker.networking.client.clientmodel.ClientModel;
import it.polimi.it.galaxytrucker.view.CLI.CLIInputReader;
import it.polimi.it.galaxytrucker.view.CLI.CLIViewStates.CLIViewState;
import it.polimi.it.galaxytrucker.view.GUI.GUIView;

public class CLICreditAcceptState extends CLIViewState {
    @Override
    public void executeState() {
        view.executorService.submit(() -> {
            int creditReward = view.getClient().getModel().getCardDetail("creditReward", Integer.class);
            System.out.println("You currently have " + view.getClient().getModel().getCredits() + " credits");
            System.out.println("Do you want to accept a reward of " + creditReward + " credits?");

            System.out.print("""
                    \nChoose an option:
                    [1]: Yes
                    [2]: No
                    >\s""");

            int option = CLIInputReader.readInt();

            switch (option) {
                case 1 -> view.getClient().receiveUserInput(
                        new UserInput.UserInputBuilder(UserInputType.CREDIT_REWARD)
                                .setCreditChoice(true)
                                .build()
                );
                case 2 -> view.getClient().receiveUserInput(
                        new UserInput.UserInputBuilder(UserInputType.CREDIT_REWARD)
                                .setCreditChoice(false)
                                .build()
                );
                default -> {
                    System.out.println("That's not a valid option, please choose another");
                    executeState();
                }
            }
        });
    }
}
