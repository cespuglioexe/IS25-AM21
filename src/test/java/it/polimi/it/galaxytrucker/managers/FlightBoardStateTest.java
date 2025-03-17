package it.polimi.it.galaxytrucker.managers;

import it.polimi.it.galaxytrucker.utility.Color;
import org.junit.jupiter.api.Test;


import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class FlightBoardStateTest {

    @Test
    public void printFlightBoardState() {
        FlightBoardState board = new FlightBoardState(16);
        board.setBoard();
        board.printFlightBoardState();
    }

    @Test
    public void movePlayerForward(){
        FlightBoardState board = new FlightBoardState(16);
        board.setBoard();
        Player player1=new Player(new UUID(0,1), "Margarozzo1",0, Color.RED);
        Player player2=new Player(new UUID(0,2), "Margarozzo2",0, Color.BLUE);
        Player player3=new Player(new UUID(0,3), "Margarozzo3",0, Color.GREEN);
        Player player4=new Player(new UUID(0,4), "Margarozzo4",0, Color.YELLOW);

        board.addPlayerMarker(player1.getPlayerID(),1);
        board.addPlayerMarker(player2.getPlayerID(),2);
        board.addPlayerMarker(player3.getPlayerID(),3);
        board.addPlayerMarker(player4.getPlayerID(),4);

        board.movePlayerForward(2, player1.getPlayerID());
        assertEquals(board.getPlayerPosition().get(player2.getPlayerID()),6);
        assertEquals(board.getCompleatedTurns().get(player2.getPlayerID()),0);
        board.movePlayerForward(5, player2.getPlayerID());
        assertEquals(board.getPlayerPosition().get(player2.getPlayerID()),8);
        assertEquals(board.getCompleatedTurns().get(player2.getPlayerID()),0);
        board.movePlayerForward(10, player2.getPlayerID());
        assertEquals(board.getPlayerPosition().get(player2.getPlayerID()),2);
        assertEquals(board.getCompleatedTurns().get(player2.getPlayerID()),1);

    }

    @Test
    public void movePlayerBackwards() {
        FlightBoardState board = new FlightBoardState(16);
        Player player1=new Player(new UUID(0,1), "Margarozzo1",0, Color.RED);
        Player player2=new Player(new UUID(0,2), "Margarozzo2",0, Color.BLUE);
        Player player3=new Player(new UUID(0,3), "Margarozzo3",0, Color.GREEN);
        Player player4=new Player(new UUID(0,4), "Margarozzo4",0, Color.YELLOW);

        board.addPlayerMarker(player1.getPlayerID(),1);
        board.addPlayerMarker(player2.getPlayerID(),2);
        board.addPlayerMarker(player3.getPlayerID(),3);
        board.addPlayerMarker(player4.getPlayerID(),4);

        board.movePlayerBackwards(1, player1.getPlayerID());
        assertEquals(board.getPlayerPosition().get(player1.getPlayerID()),3);
        assertEquals(board.getCompleatedTurns().get(player1.getPlayerID()),0);

        board.movePlayerBackwards(2, player4.getPlayerID());
        assertEquals(board.getPlayerPosition().get(player4.getPlayerID()),14);
        assertEquals(board.getCompleatedTurns().get(player4.getPlayerID()),-1);

        board.movePlayerBackwards(1, player1.getPlayerID());
        assertEquals(board.getPlayerPosition().get(player1.getPlayerID()),0);
        assertEquals(board.getCompleatedTurns().get(player1.getPlayerID()),0);

        board.movePlayerForward(10, player2.getPlayerID());
        assertEquals(board.getPlayerPosition().get(player2.getPlayerID()),7);
        assertEquals(board.getCompleatedTurns().get(player2.getPlayerID()),-1);
    }

    @Test
    public void getPlayerOrder(){

        FlightBoardState board = new FlightBoardState(16);
        board.setBoard();
        Player player1=new Player(new UUID(0,1), "Margarozzo1",0, Color.RED);
        Player player2=new Player(new UUID(0,2), "Margarozzo2",0, Color.BLUE);
        Player player3=new Player(new UUID(0,3), "Margarozzo3",0, Color.GREEN);
        Player player4=new Player(new UUID(0,4), "Margarozzo4",0, Color.YELLOW);

        board.addPlayerMarker(player1.getPlayerID(),2);
        board.addPlayerMarker(player2.getPlayerID(),1);
        board.addPlayerMarker(player3.getPlayerID(),4);
        board.addPlayerMarker(player4.getPlayerID(),3);

        List<UUID> sort= board.getPlayerOrder();
        System.out.println(sort);
        assertEquals(sort.get(0),player2.getPlayerID());
        assertEquals(sort.get(1),player1.getPlayerID());
        assertEquals(sort.get(2),player4.getPlayerID());
        assertEquals(sort.get(3),player3.getPlayerID());
    }

    @Test
    public void addPlayerMarker() {

        FlightBoardState board = new FlightBoardState(16);
        board.setBoard();
        Player player1=new Player(new UUID(0,1), "Margarozzo1",0, Color.RED);
        Player player2=new Player(new UUID(0,2), "Margarozzo2",0, Color.BLUE);
        Player player3=new Player(new UUID(0,3), "Margarozzo3",0, Color.GREEN);
        Player player4=new Player(new UUID(0,4), "Margarozzo4",0, Color.YELLOW);

        board.addPlayerMarker(player1.getPlayerID(),1);
        board.addPlayerMarker(player2.getPlayerID(),2);
        board.addPlayerMarker(player3.getPlayerID(),3);
        board.addPlayerMarker(player4.getPlayerID(),4);
        board.printFlightBoardState();

        assertEquals(board.getPlayerPosition().get(player1.getPlayerID()),4);
        assertEquals(board.getPlayerPosition().get(player2.getPlayerID()),2);
        assertEquals(board.getPlayerPosition().get(player3.getPlayerID()),1);
        assertEquals(board.getPlayerPosition().get(player4.getPlayerID()),0);
    }


}