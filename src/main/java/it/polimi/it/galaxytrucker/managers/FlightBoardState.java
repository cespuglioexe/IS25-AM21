package it.polimi.it.galaxytrucker.managers;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;
import java.util.UUID;

public class FlightBoardState {

    private UUID[] board;
    private HashMap<UUID,Integer> playerPosition;
    private HashMap<UUID,Integer> compleatedTurns;


    public FlightBoardState(int dimension) {
        board = new UUID[dimension];
        playerPosition = new HashMap<UUID,Integer>();
        compleatedTurns = new HashMap<UUID,Integer>();
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
            if(board[i].compareTo(new UUID(0,0)) != 0)
                System.out.print(board[i]+"\t");
        }
        System.out.println();
        for (int i = dimension/2; i < dimension; i++) {
            System.out.print(i+ "\t");
        }
        System.out.println();
        for (int i = dimension/2; i < dimension; i++) {
            if(board[i].compareTo(new UUID(0,0)) != 0)
                System.out.print(board[i]+"\t");
        }
        System.out.println();
    }

    public void movePlayerForward(int progress  , UUID playerId){
            int position = playerPosition.get(playerId);
            int newPosition = position;

            while (progress != 0){
                newPosition = newPosition+1;
                if(newPosition<board.length){
                    if(board[newPosition].compareTo(new UUID(0,0)) == 0){
                        progress = progress -1;
                    }
                } else {
                    newPosition = 0;
                    progress = progress -1;
                    compleatedTurns.put(playerId, compleatedTurns.get(playerId)+1);
                }
            }
            board[position] =  new UUID(0,0);
            board[newPosition] = playerId;
            playerPosition.put(playerId, newPosition);
    }

    public void movePlayerBackwards(int progress, UUID playerId ) {
        int position = playerPosition.get(playerId);
        int newPosition = position;

        while (progress != 0){
            newPosition = newPosition-1;
            if(newPosition>0){
                if(board[newPosition].compareTo(new UUID(0,0)) == 0){
                    progress = progress -1;
                }
            } else {
                newPosition = board.length-1;
                progress = progress -1;
                compleatedTurns.put(playerId, compleatedTurns.get(playerId)-1);
            }
        }
        board[position] =  new UUID(0,0);
        board[newPosition] = playerId;
        playerPosition.put(playerId, newPosition);
    }


    public List<UUID> getPlayerOrder(){
        List<UUID> order = new ArrayList<UUID>();
        int i = board.length-1;
        while(i!=-1){
            if(board[i].compareTo(new UUID(0,0)) != 0){
                order.add(board[i]);
            }
            i--;
        }
        return order;
    }

    public void addPlayerMarker(UUID id, int position) {
            if(board.length == 16) {
                if(position == 1){
                    board[4] = id;
                    playerPosition.put(id, 4);
                    compleatedTurns.put(id, 0);
                }
                if(position == 2){
                    board[2] = id;
                    playerPosition.put(id, 2);
                    compleatedTurns.put(id, 0);
                }
                if(position == 3){
                    board[1] = id;
                    playerPosition.put(id, 1);
                    compleatedTurns.put(id, 0);
                }
                if(position == 4){
                    board[0] = id;
                    playerPosition.put(id, 0);
                    compleatedTurns.put(id, 0);
                }
            }else {
                if(position == 1){
                    board[6] = id;
                    playerPosition.put(id, 6);
                    compleatedTurns.put(id, 0);
                }
                if(position == 2){
                    board[3] = id;
                    playerPosition.put(id, 3);
                    compleatedTurns.put(id, 0);
                }
                if(position == 3){
                    board[1] = id;
                    playerPosition.put(id, 1);
                    compleatedTurns.put(id, 0);
                }
                if(position == 4){
                    board[0] = id;
                    playerPosition.put(id, 0);
                    compleatedTurns.put(id, 0);
                }
            }
    }

}
