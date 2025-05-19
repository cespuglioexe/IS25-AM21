package it.polimi.it.galaxytrucker.model.gameStates;

import it.polimi.it.galaxytrucker.model.design.statePattern.StateMachine;
import it.polimi.it.galaxytrucker.model.managers.FlightBoard;
import it.polimi.it.galaxytrucker.model.managers.GameManager;
import it.polimi.it.galaxytrucker.model.managers.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LegalityCheckState extends GameState {
    private List<Player> playersWithIllegalShips = new ArrayList<>();

    @Override
    public void enter(StateMachine fsm) {
        GameManager game = (GameManager) fsm;

        List<Player> players = game.getPlayers();
        playersWithIllegalShips = findPlayersWithIllegalShips(players);

        if (!playersWithIllegalShips.isEmpty()) {
            removeIllegalPlayerMarkers(game, playersWithIllegalShips);
        }

        update(fsm);
    }

    private void removeIllegalPlayerMarkers(GameManager game, List<Player> playersWithIllegalShips) {
        FlightBoard flightBoard = game.getFlightBoard();
        List<Player> playersInFlightOrder = flightBoard.getPlayerOrder();

        removeAllPlayerMarkers(flightBoard, playersInFlightOrder);

        List<Player> playersWithLegalShips = findPlayersWithLegalShips(playersInFlightOrder, playersWithIllegalShips);

        for (Player player : playersWithLegalShips) {
            flightBoard.addPlayerMarker(player);
        }
    }
    private void removeAllPlayerMarkers(FlightBoard flightBoard, List<Player> players) {
        for (Player player : players) {
            flightBoard.removePlayerMarker(player);
        }
    }
    private List<Player> findPlayersWithLegalShips(List<Player> players, List<Player> playersWithIllegalShips) {
        return players.stream()
            .filter(p -> !playersWithIllegalShips.contains(p))
            .toList();
    }

    public List<Player> findPlayersWithIllegalShips(List<Player> players) {
        return players.stream()
            .filter(player -> !player.getShipManager().isShipLegal())
            .collect(Collectors.toList());
    }

    @Override
    public void update(StateMachine fsm) {
        if (playersWithIllegalShips.isEmpty()) {
            System.out.println("All player ships are legal");
            fsm.changeState(new GameTurnStartState());
        } else {
            System.out.println("Players whit illegal ships: " + playersWithIllegalShips.stream().map(Player::getPlayerName).toList());
            fsm.changeState(new ShipFixingState(playersWithIllegalShips));
        }
    }

    @Override
    public void exit(StateMachine fsm) {

    }
}
