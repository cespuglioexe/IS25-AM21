package it.polimi.it.galaxytrucker.managers;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import it.polimi.it.galaxytrucker.aventurecard.AdventureDeck;
import it.polimi.it.galaxytrucker.gameStates.State;

public interface Model {
    //GETTER
    public State getCurrentState();
    public int getLevel();
    public int getNumberOfPlayers();
    public List<Player> getPlayers();
    public Player getPlayerByID(UUID id);
    public boolean allPlayersConnected();
    public ShipManager getPlayerShip(UUID id);
    public Set<Player> getPlayersWithIllegalShips();
    public FlightBoardState getFlightBoard();
    public AdventureDeck getAdventureDeck();

    //SETTER
    public void changeState(State nextState);
    public void setLevel(int level);
    public void setNumberOfPlayers(int numberOfPlayers);
    public UUID addNewPlayer(String name);
    public void removePlayer(UUID id);
}
