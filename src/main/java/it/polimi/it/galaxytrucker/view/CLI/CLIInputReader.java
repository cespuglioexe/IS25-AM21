package it.polimi.it.galaxytrucker.view.CLI;

import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * This class handles asynchronously reading user input from
 * the command-line, in order to avoid the view and client
 * logic from being blocked.
 */
public class CLIInputReader implements Runnable {

    private static CLIInputReader instance;
    private static final BlockingQueue<String> inputQueue = new LinkedBlockingQueue<>();
    private final Scanner scanner = new Scanner(System.in);

    protected CLIInputReader() {
    }

    public static CLIInputReader getInstance() {
        if (instance == null) {
            synchronized (CLIInputReader.class) {
                instance = new CLIInputReader();
            }
        }
        return instance;
    }

    @Override
    public void run() {
        new Thread(() -> {
            String command;
            boolean added;
            while (true) {
                command = scanner.nextLine().trim();
                do {
                    added = inputQueue.offer(command);
                } while (!added);
            }
        }).start();
    }

    public static synchronized int readInt() {
        int value;

        try {
            value = Integer.parseInt(inputQueue.take());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } catch (NumberFormatException e) {
            System.out.println("Please enter an integer");
            return -1;
        }

        return value;
    }

    public static synchronized String readString() {
        String value;

        try {
            value = inputQueue.take();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return value;
    }

//    private void executeCommand (String command) {
//        List<String> parameters = new ArrayList<>(Arrays.asList(command.split(" ")));
//        String commandType = "unknown";
//        try {
//            commandType = parameters.removeFirst();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        switch (commandType) {
//
//            case "new-component":
//                if (!parameters.isEmpty())
//                    System.out.println(ConsoleColors.RED + "Invalid command syntax, it should be 'new-component'" + ConsoleColors.RESET);
//                else
//                    client.receiveUserInput(new UserInput.UserInputBuilder((VirtualClient) client, UserInputType.SELECT_RANDOM_COMPONENT).build());
//                break;
//
//            case "see-saved":
//                if (!parameters.isEmpty())
//                    System.out.println(ConsoleColors.RED + "Invalid command syntax, it should be 'see-saved'" + ConsoleColors.RESET);
//                else
//                    CLIViewState.getCurrentState().displaySavedComponents();
//                break;
//
//            case "see-discarded":
//                if (!parameters.isEmpty())
//                    System.out.println(ConsoleColors.RED + "Invalid command syntax, it should be 'see-discarded'" + ConsoleColors.RESET);
//                else
//                    CLIViewState.getCurrentState().displayDiscardedComponents();
//                break;
//
//            case "select-saved":
//                if (parameters.size() != 1)
//                    System.out.println(ConsoleColors.RED + "Invalid command syntax, it should be 'select-saved <index>'" + ConsoleColors.RESET);
//                else
//                    client.receiveUserInput(new UserInput.UserInputBuilder((VirtualClient) client, UserInputType.SELECT_SAVED_COMPONENT)
//                            .setSelectedTileIndex(Integer.parseInt(parameters.getFirst()))
//                            .build()
//                    );
//                break;
//
//            case "select-discarded":
//                if (parameters.size() != 1)
//                    System.out.println(ConsoleColors.RED + "Invalid command syntax, it should be 'select-discarded <index>'" + ConsoleColors.RESET);
//                else
//                    client.receiveUserInput(new UserInput.UserInputBuilder((VirtualClient) client, UserInputType.SELECT_DISCARDED_COMPONENT)
//                            .setSelectedTileIndex(Integer.parseInt(parameters.getFirst()))
//                            .build()
//                    );
//                break;
//
//            case "place-component":
//                if (parameters.size() != 3)
//                    System.out.println(ConsoleColors.RED + "Invalid command syntax, it should be 'place-component <column> <row> <rotation>'" + ConsoleColors.RESET);
//                else
//                    client.receiveUserInput(new UserInput.UserInputBuilder((VirtualClient) client, UserInputType.PLACE_COMPONENT)
//                            .setCoords(Integer.parseInt(parameters.get(0)), Integer.parseInt(parameters.get(1)))
//                            .setRotation(Integer.parseInt(parameters.get(2)))
//                            .build()
//                    );
//                break;
//
//            case "save-component":
//                if (!parameters.isEmpty())
//                    System.out.println(ConsoleColors.RED + "Invalid command syntax, it should be 'save-component'" + ConsoleColors.RESET);
//                else
//                    client.receiveUserInput(new UserInput.UserInputBuilder((VirtualClient) client, UserInputType.SAVE_SELECTED_COMPONENT).build());
//                break;
//
//            case "discard-component":
//                if (!parameters.isEmpty())
//                    System.out.println(ConsoleColors.RED + "Invalid command syntax, it should be 'discard-component'" + ConsoleColors.RESET);
//                else
//                    client.receiveUserInput(new UserInput.UserInputBuilder((VirtualClient) client, UserInputType.DISCARD_SELECTED_COMPONENT).build());
//                break;
//
//            case "see-cards":
//                if (parameters.size() != 1)
//                    System.out.println(ConsoleColors.RED + "Invalid command syntax, it should be 'see-cards <pile_index>'" + ConsoleColors.RESET);
//                else
//                    client.receiveUserInput(new UserInput.UserInputBuilder((VirtualClient) client, UserInputType.SEE_CARDS_PILE)
//                            .setCardPileIndex(Integer.parseInt(parameters.getFirst()))
//                            .build()
//                    );
//                break;
//
//            case "restart-timer":
//                if (!parameters.isEmpty())
//                    System.out.println(ConsoleColors.RED + "Invalid command syntax, it should be 'restart-timer'" + ConsoleColors.RESET);
//                else
//                    client.receiveUserInput(new UserInput.UserInputBuilder((VirtualClient) client, UserInputType.RESTART_BUILDING_TIMER).build());
//                break;
//
//            default:
//                System.out.println(ConsoleColors.RED + "Unknown command" + ConsoleColors.RESET);
//        }
//    }
}
