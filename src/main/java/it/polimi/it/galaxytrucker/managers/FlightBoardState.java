package it.polimi.it.galaxytrucker.managers;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;
import java.util.UUID;

// quando si genera nuovo ID, deve essere diverso da UUID(0,0)

public class FlightBoardState {

    private Player[] board;
    private HashMap<Player,Integer> playerPosition;
    private HashMap<Player,Integer> compleatedTurns;


    public FlightBoardState(int dimension) {
        board = new Player[dimension];
        setBoard();
        playerPosition = new HashMap<Player,Integer>();
        compleatedTurns = new HashMap<Player,Integer>();
    }

    public void setBoard() {
        for(int i = 0; i < board.length; i++){
            board[i] = new Player(new UUID(0,0));
        }
    }

    public Player[] getBoard() {
        return board;
    }

    public HashMap<Player, Integer> getPlayerPosition() {
        return playerPosition;
    }

    public HashMap<Player, Integer> getCompleatedTurns() {
        return compleatedTurns;
    }

    public void printFlightBoardState() {
        System.out.println( "Flight Board State");
        System.out.println();
        System.out.println();
        int dimension = board.length;

        // 0    1    2   3   4   5   6   7

        // ID   Id       ID      ID

        // 8    9   10  11  12  13  14  15

        // ID(?) .......................


        for (int i = 0; i < dimension/2; i++) {
            System.out.print(i+ "\t");
        }
        System.out.println();
        for (int i = 0; i < dimension/2; i++) {
            if(board[i].getPlayerID().compareTo(new UUID(0,0)) != 0)
                System.out.print(board[i].getPlayerID().getLeastSignificantBits()+"\t");
            else System.out.print("\t");
        }
        System.out.println();
        for (int i = dimension/2; i < dimension; i++) {
            System.out.print(i+ "\t");
        }
        System.out.println();
        for (int i = dimension/2; i < dimension; i++) {
            if(board[i].getPlayerID().compareTo(new UUID(0,0)) != 0)
                System.out.print(board[i].getPlayerID().getLeastSignificantBits()+"\t");
            else System.out.print("\t");
        }
        System.out.println();
    }

    public void movePlayerForward(int progress  , Player player){
            int position = playerPosition.get(player);
            int newPosition = position;


            while (progress != 0){
                newPosition = newPosition+1;
                if(newPosition==board.length){
                    compleatedTurns.put(player, compleatedTurns.get(player)+1);
                    newPosition = 0;
                }
                if(board[newPosition].getPlayerID().compareTo(new UUID(0,0)) == 0)
                    progress = progress -1;
            }
            board[position] =  new Player(new UUID(0,0));
            board[newPosition] = player;
            playerPosition.put(player, newPosition);
    }

    public void movePlayerBackwards(int progress, Player player) {
        UUID playerId = player.getPlayerID();
        int position = playerPosition.get(player);
        int newPosition = position;

        while (progress != 0){
            newPosition = newPosition-1;
            if(newPosition<0){
                newPosition = board.length-1;
                compleatedTurns.put(player, compleatedTurns.get(player)-1);
            }
            if(board[newPosition].getPlayerID().compareTo(new UUID(0,0)) == 0)
                progress = progress -1;
        }
        board[position] =  new Player(new UUID(0,0));
        board[newPosition] = player;
        playerPosition.put(player, newPosition);
    }


    public List<Player> getPlayerOrder(){
        List<Player> order = new ArrayList<Player>();
        int i = board.length-1;
        while(i!=-1){
            if(board[i].getPlayerID().compareTo(new UUID(0,0)) != 0){
                order.add(board[i]);
            }
            i--;
        }
        return order;
    }

    public void addPlayerMarker(Player player, int position) {
            if(board.length == 18) {
                if(position == 1){
                    board[4] = player;
                    playerPosition.put(player, 4);
                    compleatedTurns.put(player, 0);
                }
                if(position == 2){
                    board[2] = player;
                    playerPosition.put(player, 2);
                    compleatedTurns.put(player, 0);
                }
                if(position == 3){
                    board[1] = player;
                    playerPosition.put(player, 1);
                    compleatedTurns.put(player, 0);
                }
                if(position == 4){
                    board[0] = player;
                    playerPosition.put(player, 0);
                    compleatedTurns.put(player, 0);
                }
            }else {
                if(position == 1){
                    board[6] = player;
                    playerPosition.put(player, 6);
                    compleatedTurns.put(player, 0);
                }
                if(position == 2){
                    board[3] = player;
                    playerPosition.put(player, 3);
                    compleatedTurns.put(player, 0);
                }
                if(position == 3){
                    board[1] = player;
                    playerPosition.put(player, 1);
                    compleatedTurns.put(player, 0);
                }
                if(position == 4){
                    board[0] = player;
                    playerPosition.put(player, 0);
                    compleatedTurns.put(player, 0);
                }
            }
    }

}
