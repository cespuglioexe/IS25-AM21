package it.polimi.it.galaxytrucker.managers;

import it.polimi.it.galaxytrucker.model.managers.FlightBoard;
import it.polimi.it.galaxytrucker.model.managers.Player;
import it.polimi.it.galaxytrucker.model.utility.Color;
import org.junit.jupiter.api.Test;


import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class FlightBoardTest {

    @Test
    public void printFlightBoardState() {
        FlightBoard board = new FlightBoard(18);
        board.setBoard();
        board.printFlightBoardState();
    }

    @Test
    public void movePlayerForward(){
        FlightBoard board = new FlightBoard(18);
        board.setBoard();
        Player player1=new Player(new UUID(0,1), "Margarozzo1",0, Color.RED);
        Player player2=new Player(new UUID(0,2), "Margarozzo2",0, Color.BLUE);
        Player player3=new Player(new UUID(0,3), "Margarozzo3",0, Color.GREEN);
        Player player4=new Player(new UUID(0,4), "Margarozzo4",0, Color.YELLOW);

        board.addPlayerMarker(player1);
        board.addPlayerMarker(player2);
        board.addPlayerMarker(player3);
        board.addPlayerMarker(player4);

        board.movePlayerForward(2, player1);
        assertEquals(board.getPlayerPosition().get(player1),6);
        assertEquals(board.getCompletedLaps().get(player1),0);
        board.printFlightBoardState();
        board.movePlayerForward(5, player2);
        assertEquals(board.getPlayerPosition().get(player2),8);
        assertEquals(board.getCompletedLaps().get(player2),0);
        board.printFlightBoardState();
        board.movePlayerForward(10, player2);
        assertEquals(board.getPlayerPosition().get(player2),2);
        assertEquals(board.getCompletedLaps().get(player2),1);


    }

    @Test
    public void movePlayerBackwards() {

        FlightBoard board = new FlightBoard(18);
        board.setBoard();
        Player player1=new Player(new UUID(0,1), "Margarozzo1",0, Color.RED);
        Player player2=new Player(new UUID(0,2), "Margarozzo2",0, Color.BLUE);
        Player player3=new Player(new UUID(0,3), "Margarozzo3",0, Color.GREEN);
        Player player4=new Player(new UUID(0,4), "Margarozzo4",0, Color.YELLOW);

        board.addPlayerMarker(player1);
        board.addPlayerMarker(player2);
        board.addPlayerMarker(player3);
        board.addPlayerMarker(player4);

        board.movePlayerBackwards(1, player1);
        assertEquals(board.getPlayerPosition().get(player1),3);
        assertEquals(board.getCompletedLaps().get(player1),0);


        board.movePlayerBackwards(2, player4);
        assertEquals(board.getPlayerPosition().get(player4),16);
        assertEquals(board.getCompletedLaps().get(player4),-1);


        board.movePlayerBackwards(1, player1);
        assertEquals(board.getPlayerPosition().get(player1),0);
        assertEquals(board.getCompletedLaps().get(player1),0);

        board.movePlayerBackwards(10, player2);
        assertEquals(board.getPlayerPosition().get(player2),7);
        assertEquals(board.getCompletedLaps().get(player2),-1);
    }

    @Test
    public void getPlayerOrder(){

        FlightBoard board = new FlightBoard(18);
        board.setBoard();
        Player player1=new Player(new UUID(0,1), "Margarozzo1",0, Color.RED);
        Player player2=new Player(new UUID(0,2), "Margarozzo2",0, Color.BLUE);
        Player player3=new Player(new UUID(0,3), "Margarozzo3",0, Color.GREEN);
        Player player4=new Player(new UUID(0,4), "Margarozzo4",0, Color.YELLOW);

        board.addPlayerMarker(player1);
        board.addPlayerMarker(player2);
        board.addPlayerMarker(player3);
        board.addPlayerMarker(player4);

        List<Player> sort= board.getPlayerOrder();
        System.out.println(sort);
        assertEquals(sort.get(0).getPlayerID(),player2.getPlayerID());
        assertEquals(sort.get(1).getPlayerID(),player1.getPlayerID());
        assertEquals(sort.get(2).getPlayerID(),player4.getPlayerID());
        assertEquals(sort.get(3).getPlayerID(),player3.getPlayerID());
    }

    @Test
    public void addPlayerMarker() {

        FlightBoard board = new FlightBoard(18);
        board.setBoard();
        Player player1=new Player(new UUID(0,1), "Margarozzo1",0, Color.RED);
        Player player2=new Player(new UUID(0,2), "Margarozzo2",0, Color.BLUE);
        Player player3=new Player(new UUID(0,3), "Margarozzo3",0, Color.GREEN);
        Player player4=new Player(new UUID(0,4), "Margarozzo4",0, Color.YELLOW);

        board.addPlayerMarker(player1);
        board.addPlayerMarker(player2);
        board.addPlayerMarker(player3);
        board.addPlayerMarker(player4);
        board.printFlightBoardState();

        assertEquals(board.getPlayerPosition().get(player1),4);
        assertEquals(board.getPlayerPosition().get(player2),2);
        assertEquals(board.getPlayerPosition().get(player3),1);
        assertEquals(board.getPlayerPosition().get(player4),0);
    }


}