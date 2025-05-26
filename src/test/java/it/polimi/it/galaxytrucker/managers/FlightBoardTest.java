package it.polimi.it.galaxytrucker.managers;

import it.polimi.it.galaxytrucker.model.managers.FlightBoard;
import it.polimi.it.galaxytrucker.model.managers.Player;
import it.polimi.it.galaxytrucker.model.managers.ShipManager;
import it.polimi.it.galaxytrucker.model.utility.Color;
import org.junit.jupiter.api.Test;


import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class FlightBoardTest {

    @Test
    public void printFlightBoardState() {
        FlightBoard board = new FlightBoard(1);
        board.setBoard();
        board.printFlightBoardState();
    }

    @Test
    public void movePlayerForward(){
        FlightBoard board = new FlightBoard(1);

        Player player1 = new Player(new UUID(0,1), "Margarozzo1", Color.RED, new ShipManager(1, Color.BLUE));
        Player player2 = new Player(new UUID(0,2), "Margarozzo2", Color.BLUE, new ShipManager(1, Color.BLUE));
        Player player3 = new Player(new UUID(0,3), "Margarozzo3", Color.GREEN, new ShipManager(1, Color.BLUE));
        Player player4 = new Player(new UUID(0,4), "Margarozzo4", Color.YELLOW, new ShipManager(1, Color.BLUE));

        board.addPlayerMarker(player1);
        board.addPlayerMarker(player2);
        board.addPlayerMarker(player3);
        board.addPlayerMarker(player4);

        board.movePlayerForward(2, player1);
        assertEquals(6, board.getPlayerPosition().get(player1));
        assertEquals(0, board.getCompletedLaps().get(player1));

        board.printFlightBoardState();

        board.movePlayerForward(5, player2);
        assertEquals(8, board.getPlayerPosition().get(player2));
        assertEquals(0, board.getCompletedLaps().get(player2));

        board.printFlightBoardState();

        board.movePlayerForward(10, player2);
        assertEquals(2, board.getPlayerPosition().get(player2));
        assertEquals(1, board.getCompletedLaps().get(player2));
    }

    @Test
    public void movePlayerBackwards() {

        FlightBoard board = new FlightBoard(1);

        Player player1 = new Player(new UUID(0,1), "Margarozzo1", Color.RED, new ShipManager(1, Color.BLUE));
        Player player2 = new Player(new UUID(0,2), "Margarozzo2", Color.BLUE, new ShipManager(1, Color.BLUE));
        Player player3 = new Player(new UUID(0,3), "Margarozzo3", Color.GREEN, new ShipManager(1, Color.BLUE));
        Player player4 = new Player(new UUID(0,4), "Margarozzo4", Color.YELLOW, new ShipManager(1, Color.BLUE));

        board.addPlayerMarker(player1);
        board.addPlayerMarker(player2);
        board.addPlayerMarker(player3);
        board.addPlayerMarker(player4);

        board.movePlayerBackwards(1, player1);

        assertEquals(3, board.getPlayerPosition().get(player1));
        assertEquals(0, board.getCompletedLaps().get(player1));


        board.movePlayerBackwards(2, player4);
        assertEquals(16, board.getPlayerPosition().get(player4));
        assertEquals(-1, board.getCompletedLaps().get(player4));


        board.movePlayerBackwards(1, player1);
        assertEquals(0, board.getPlayerPosition().get(player1));
        assertEquals(0, board.getCompletedLaps().get(player1));

        board.movePlayerBackwards(10, player2);
        assertEquals(7, board.getPlayerPosition().get(player2));
        assertEquals(-1, board.getCompletedLaps().get(player2));
    }

    @Test
    public void getPlayerOrder(){

        FlightBoard board = new FlightBoard(1);

        Player player1=new Player(new UUID(0,1), "Margarozzo1", Color.RED, new ShipManager(1, Color.BLUE));
        Player player2=new Player(new UUID(0,2), "Margarozzo2", Color.BLUE, new ShipManager(1, Color.BLUE));
        Player player3=new Player(new UUID(0,3), "Margarozzo3", Color.GREEN, new ShipManager(1, Color.BLUE));
        Player player4=new Player(new UUID(0,4), "Margarozzo4", Color.YELLOW, new ShipManager(1, Color.BLUE));

        board.addPlayerMarker(player1);
        board.addPlayerMarker(player2);
        board.addPlayerMarker(player3);
        board.addPlayerMarker(player4);

        List<Player> sort = board.getPlayerOrder();

        System.out.println(sort);

        assertEquals(sort.get(0).getPlayerID(), player1.getPlayerID());
        assertEquals(sort.get(1).getPlayerID(), player2.getPlayerID());
        assertEquals(sort.get(2).getPlayerID(), player3.getPlayerID());
        assertEquals(sort.get(3).getPlayerID(), player4.getPlayerID());
    }

    @Test
    public void addPlayerMarker() {

        FlightBoard board = new FlightBoard(1);

        Player player1 = new Player(new UUID(0,1), "Margarozzo1", Color.RED, new ShipManager(1, Color.BLUE));
        Player player2 = new Player(new UUID(0,2), "Margarozzo2", Color.BLUE, new ShipManager(1, Color.BLUE));
        Player player3 = new Player(new UUID(0,3), "Margarozzo3", Color.GREEN, new ShipManager(1, Color.BLUE));
        Player player4 = new Player(new UUID(0,4), "Margarozzo4", Color.YELLOW, new ShipManager(1, Color.BLUE));

        board.addPlayerMarker(player1);
        board.addPlayerMarker(player2);
        board.addPlayerMarker(player3);
        board.addPlayerMarker(player4);

        board.printFlightBoardState();

        assertEquals(4, board.getPlayerPosition().get(player1));
        assertEquals(2, board.getPlayerPosition().get(player2));
        assertEquals(1, board.getPlayerPosition().get(player3));
        assertEquals(0, board.getPlayerPosition().get(player4));
    }

    @Test
    public void addDisconnectedPlayerMarker() {

        FlightBoard board = new FlightBoard(1);

        Player player1 = new Player(new UUID(0,1), "Margarozzo1", Color.RED, new ShipManager(1, Color.BLUE));
        Player player2 = new Player(new UUID(0,2), "Margarozzo2", Color.BLUE, new ShipManager(1, Color.BLUE));
        Player player3 = new Player(new UUID(0,3), "Margarozzo3", Color.GREEN, new ShipManager(1, Color.BLUE));
        Player player4 = new Player(new UUID(0,4), "Margarozzo4", Color.YELLOW, new ShipManager(1, Color.BLUE));

        board.addPlayerMarker(player1);
        board.addPlayerMarker(player2);
        board.addPlayerMarker(player3);
        board.addPlayerMarker(player4);

        board.printFlightBoardState();

        board.removePlayerMarker(player2);

        board.addPlayerMarker(player2,board.getPlayerPosition().get(player2));
        board.printFlightBoardState();
    }

    @Test
    public void addDisconnectedPlayerMarkerWithBusyBox() {

        FlightBoard board = new FlightBoard(1);

        Player player1 = new Player(new UUID(0,1), "Margarozzo1", Color.RED, new ShipManager(1, Color.BLUE));
        Player player2 = new Player(new UUID(0,2), "Margarozzo2", Color.BLUE, new ShipManager(1, Color.BLUE));
        Player player3 = new Player(new UUID(0,3), "Margarozzo3", Color.GREEN, new ShipManager(1, Color.BLUE));
        Player player4 = new Player(new UUID(0,4), "Margarozzo4", Color.YELLOW, new ShipManager(1, Color.BLUE));

        board.addPlayerMarker(player1);
        board.addPlayerMarker(player2);
        board.addPlayerMarker(player3);
        board.addPlayerMarker(player4);

        board.printFlightBoardState();

        board.removePlayerMarker(player2);
        board.movePlayerForward(1, player3);
        board.movePlayerForward(1, player4);
        board.movePlayerBackwards(2, player1);

        board.addPlayerMarker(player2,board.getPlayerPosition().get(player2));
        assertEquals(-1, board.getCompletedLaps().get(player2));
        board.printFlightBoardState();
    }


}