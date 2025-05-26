package it.polimi.it.galaxytrucker.model.managers;

import java.util.*;

/**
 * The {@code FlightBoard} class represents the flight board used in the Galaxy Trucker game.
 * <p>
 * It manages player positions, tracks laps completed, and provides functionality for movement, board updates,
 * and retrieving player order. The class supports dynamic board dimension initialization based on the game level.
 * </p>
 *
 * @author Thomas Bianconi
 * @author Giacomo Amaducci
 * @version 1.1
 */
public class FlightBoard {

    private final Player[] board;
    private final HashMap<Player,Integer> playerPosition;
    private final HashMap<Player,Integer> completedLaps;

    public FlightBoard(int level) {
        int dimension = 0;
        
        if (level == 1) {
            dimension = 18;
        }
        else if (level == 2) {
            dimension = 24;
        }

        board = new Player[dimension];
        setBoard();
        playerPosition = new HashMap<>();
        completedLaps = new HashMap<>();
    }

    /**
     * Fills the position array with null values.
     * <p>
     * NB: Use only for testing purposes.
     */
    @Deprecated
    public void setBoard() {
        Arrays.fill(board, null);
    }

    public Player[] getBoard() {
        return board;
    }

    public HashMap<Player, Integer> getPlayerPosition() {
        return playerPosition;
    }

    public HashMap<Player, Integer> getCompletedLaps() {
        return completedLaps;
    }

    
    /**
     * Prints the current state of the flight board, including player positions and IDs.
     * <p>
     * This method is intended for testing purposes only and should not be used in production.
     * </p>
     */
    public void printFlightBoardState() {
        System.out.println("Flight Board CLIViewState");
        System.out.println();
        System.out.println();
        int dimension = board.length;

        // 0    1    2   3   4   5   6   7

        // COLOR    B       R       Y

        // 8    9   10  11  12  13  14  15

        // COLOR .......................


        for (int i = 0; i < dimension / 2; i++) {
            System.out.print(i + "\t");
        }
        System.out.println();
        for (int i = 0; i < dimension / 2; i++) {
            if (board[i] != null)
                System.out.print(board[i].getColor().toString().charAt(0) + "\t");
            else System.out.print("\t");
        }
        System.out.println();
        for (int i = dimension / 2; i < dimension; i++) {
            System.out.print(i + "\t");
        }
        System.out.println();
        for (int i = dimension / 2; i < dimension; i++) {
            if (board[i] != null)
                System.out.print(board[i].getColor().toString().charAt(0) + "\t");
            else System.out.print("\t");
        }
        System.out.println();
    }


    /**
     * Moves a player forward by a specified number of spaces on the flight board.
     * <p>
     * If a space is occupied during movement, the player skips it and moves to the next available space.
     * If the player moves past the end of the board, they loop back to the beginning, completing a lap.
     * Updates the player's position and the number of completed turns accordingly.
     * </p>
     *
     * @param progress the number of spaces the player intends to move forward.
     * @param player   the player to move on the board.
     * @throws NullPointerException      if the given player is not found in the current player positions.
     * @throws IndexOutOfBoundsException if progress leads to invalid board manipulation.
     */
    public void movePlayerForward(int progress, Player player) {
        int position = playerPosition.get(player);
        int newPosition = position;
        int laps = 0;

        // If the next space is occupied, the player skips to the one after that.
        // When this happens, the player is effectively moving 2 spaces forward.
        if (progress != 0) {
            board[position] = null;
        }
        for (int i = 0; i < progress; i++) {
            do {
                newPosition++;
                if (newPosition >= board.length) {
                    laps++;
                }
            } while (board[(newPosition) % board.length] != null);
            
            // If newPosition is past the end of the array, reset it to the beginning
            if (newPosition >= board.length) {
                newPosition -= board.length;
            }
        }

        // Update the content of the starting and ending cells
        board[position] = null;
        board[newPosition] = player;

        // Update the hashmaps
        playerPosition.put(player, newPosition);
        completedLaps.put(player, completedLaps.get(player) + laps);
    }

    /**
     * Moves a player backward by a specified number of spaces on the flight board.
     * <p>
     * If a space is occupied during movement, the player skips it and moves to the next available space.
     * If the player moves past the start of the board, they loop back to the end, undoing a lap.
     * Updates the player's position and the number of completed laps accordingly.
     * </p>
     *
     * @param progress the number of spaces the player intends to move forward.
     * @param player   the player to move on the board.
     * @throws NullPointerException      if the given player is not found in the current player positions.
     * @throws IndexOutOfBoundsException if progress leads to invalid board manipulation.
     */
    public void movePlayerBackwards(int progress, Player player) {
        int position = playerPosition.get(player);
        int newPosition = position;

        // If the previous space is occupied, the player skips to the one before that.
        // When this happens, the player is effectively moving 2 spaces backwards.
        for (int i = 0; i < progress; i++) {
            do {
                newPosition--;
            } while (board[(newPosition % board.length + board.length) % board.length] != null);

            // If newPosition is past the start of the array, reset it to the end
            if (newPosition < 0) {
                newPosition += board.length;
            }
        }

        // Update the content of the starting and ending cells
        board[position] = null;
        board[newPosition] = player;

        // Update the hashmaps
        playerPosition.put(player, newPosition);
        completedLaps.put(player, completedLaps.get(player) - (newPosition > position ? 1 : 0));  // {@code newPosition > position} means that the player looped
                                                                                                    // over the array, undoing a lap of the board
    }


    /**
     * Retrieves an ordered list of players currently on the flight board.
     * <p>
     * This method processes the flight board from the last position to the first and
     * collects all active players, preserving their order as they appear on the board.
     * </p>
     *
     * @return a {@code List} containing the {@code Player} objects in reverse order of their positions
     * on the flight board.
     */
    public List<Player> getPlayerOrder() {
        List<Player> order = new ArrayList<>();
        for (int i = board.length - 1; i >= 0; i--) {
            if (board[i] != null) {
                order.add(board[i]);
            }
        }
        return order;
    }

    
    /**
     * Adds a player's marker to the flight board, initializing their position and completed laps based on the board size and
     * the number of players already placed on the board.
     * <p>
     * Players are assigned predefined positions on the board depending on the current size of the board and
     * the existing number of players. This method also ensures there are no duplicate positions for players.
     * </p>
     *
     * @param player the {@code Player} object representing the player to add to the flight board.
     *               If the player already exists, no changes will be made.
     */
    public void addPlayerMarker(Player player) {
        if (playerPosition.get(player) == null) {
            if (board.length == 18) {
                switch (playerPosition.size()) {
                    case 0:
                        board[4] = player;
                        playerPosition.put(player, 4);
                        completedLaps.put(player, 0);
                        break;
                    case 1:
                        board[2] = player;
                        playerPosition.put(player, 2);
                        completedLaps.put(player, 0);
                        break;
                    case 2:
                        board[1] = player;
                        playerPosition.put(player, 1);
                        completedLaps.put(player, 0);
                        break;
                    case 3:
                        board[0] = player;
                        playerPosition.put(player, 0);
                        completedLaps.put(player, 0);
                        break;
                }
            } else if (board.length == 24) {
                switch (playerPosition.size()) {
                    case 0:
                        board[6] = player;
                        playerPosition.put(player, 6);
                        completedLaps.put(player, 0);
                        break;
                    case 1:
                        board[3] = player;
                        playerPosition.put(player, 3);
                        completedLaps.put(player, 0);
                        break;
                    case 2:
                        board[1] = player;
                        playerPosition.put(player, 1);
                        completedLaps.put(player, 0);
                        break;
                    case 3:
                        board[0] = player;
                        playerPosition.put(player, 0);
                        completedLaps.put(player, 0);
                        break;
                }
            }
        }
    }

    public void removePlayerMarker(Player player) {
        int position = playerPosition.get(player);
        board[position] = null;
    }

    public void addPlayerMarker(Player player, int position) {
        if (board[position] == null) {
            board[position] = player;
        } else {
            while (board[position] != null) {
                position--;

                if (position < 0) {
                    position = board.length - 1;
                    completedLaps.put(player, completedLaps.get(player) - 1);
                }
            }

            board[position] = player;
        }
    }
}
