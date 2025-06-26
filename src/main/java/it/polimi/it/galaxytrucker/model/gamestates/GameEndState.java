package it.polimi.it.galaxytrucker.model.gamestates;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import it.polimi.it.galaxytrucker.messages.servermessages.GameUpdate;
import it.polimi.it.galaxytrucker.messages.servermessages.GameUpdateType;
import it.polimi.it.galaxytrucker.model.design.statePattern.StateMachine;
import it.polimi.it.galaxytrucker.model.managers.GameManager;
import it.polimi.it.galaxytrucker.model.managers.Player;
import it.polimi.it.galaxytrucker.model.managers.ShipManager;

public class GameEndState extends GameState {

    @Override
    public void enter(StateMachine fsm) {
        GameManager gameManager = (GameManager) fsm;

        List<Player> playersInFlightOrder = gameManager.getFlightBoard().getPlayerOrder();
        List<Player> activePlayers = gameManager.getActivePlayers();
        List<Player> allPlayers = gameManager.getPlayers();

        distributeRankingRewards(playersInFlightOrder);
        rewardBestLookingShip(activePlayers);
        convertCargoToCredits(activePlayers);
        penalizeDestroyedComponents(allPlayers);

        List<Player> rankings = gameManager.getPlayerRank();
        printRanking(rankings);

        HashMap<UUID, Integer> map = (HashMap<UUID, Integer>) IntStream.range(0, rankings.size())
                .boxed()
                .collect(Collectors.toMap(
                        i -> rankings.get(i).getPlayerID(),
                        i -> i
                ));



        HashMap<UUID, String> map2 = (HashMap<UUID, String>)  rankings.stream()
                .collect(Collectors.toMap(
                        Player::getPlayerID,
                        Player::getPlayerName
                ));

        gameManager.updateListeners(new GameUpdate.GameUpdateBuilder(GameUpdateType.NEW_STATE)
                .setNewSate(GameEndState.class.getSimpleName())
                .setNicknames(map2)
                .setPlayerMarkerPositions(map)
                .build()
        );
    }

    private void distributeRankingRewards(List<Player> players) {
        int reward = players.size();

        System.out.println("RANKING:");
        for (Player player : players) {
            System.out.printf("%s received %d: actual %d%n", player.getPlayerName(), reward, player.getCredits() + reward);
            player.addCredits(reward--);
        }
    }
    private void rewardBestLookingShip(List<Player> players) {
        final int REWARD = 2;

        System.out.println("BEST LOOKING SHIP:");
        Map<Player, Integer> playerToExposedConnectors = players.stream()
            .collect(Collectors.toMap(
                Function.identity(),
                p -> p.getShipManager().countAllExposedConnectors()
            ));

        int minExposed = playerToExposedConnectors.values().stream()
            .min(Integer::compareTo)
            .orElse(0);

        playerToExposedConnectors.entrySet().stream()
            .filter(entry -> entry.getValue() == minExposed)
            .map(Map.Entry::getKey)
            .forEach(p -> {
                p.addCredits(REWARD);
                System.out.printf("%s received %d: actual %d%n", p.getPlayerName(), REWARD, p.getCredits());
            });
    }
    private void convertCargoToCredits(List<Player> players) {
        System.out.println("CARGO:");
        for (Player player : players) {
            int value = player.convertCargoToCredits();

            System.out.printf("%s received %d: actual %d%n", player.getPlayerName(), value, player.getCredits() + value);
            player.addCredits(value);
        }
    }
    private void penalizeDestroyedComponents(List<Player> players) {
        System.out.println("DESTROYED COMPONENTS:");
        for (Player player : players) {
            ShipManager ship = player.getShipManager();
            int penalty = ship.getDestroyedComponents().size();

            System.out.printf("%s penalized for %d: actual %d%n", player.getPlayerName(), penalty, player.getCredits() - penalty);
            player.addCredits(-penalty);
        }
    }
    private void printRanking(List<Player> players) {
        int i = 1;
        System.out.println();

        for (Player player : players) {
            System.out.printf("%d: %s [%d]%n", i++, player.getPlayerName(), player.getCredits());
        }
    }

    @Override
    public void update(StateMachine fsm) {

    }

    @Override
    public void exit(StateMachine fsm) {

    }
}
