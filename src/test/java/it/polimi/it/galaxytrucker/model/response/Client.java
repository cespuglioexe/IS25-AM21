package it.polimi.it.galaxytrucker.model.response;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import it.polimi.it.galaxytrucker.listeners.Listener;
import it.polimi.it.galaxytrucker.messages.servermessages.GameUpdate;
import it.polimi.it.galaxytrucker.messages.servermessages.GameUpdate.GameUpdateBuilder;
import it.polimi.it.galaxytrucker.model.componenttiles.CabinModule;
import it.polimi.it.galaxytrucker.model.componenttiles.CentralCabin;
import it.polimi.it.galaxytrucker.model.managers.GameManager;
import it.polimi.it.galaxytrucker.model.managers.Player;
import it.polimi.it.galaxytrucker.model.managers.ShipManager;
import it.polimi.it.galaxytrucker.model.utility.Cargo;
import it.polimi.it.galaxytrucker.model.utility.Color;
import it.polimi.it.galaxytrucker.model.utility.Coordinates;
import it.polimi.it.galaxytrucker.view.CLI.ConsoleColors;

public class Client implements Listener {
    private Player client;
    private GameManager model;

    public Client(GameManager model) {
        this.model = model;
    }

    public void setClient(Player player) {
        client = player;
    }

    public Player getPlayer() {
        return client;
    }

    @Override
    public void notify(GameUpdate update) {
        GameUpdate localCopy = GameUpdateBuilder.clone(update);
        switch (localCopy.getInstructionType()) {
            case INPUT:
                if (localCopy.getInterestedPlayerId().equals(client.getPlayerID())) {
                    buildCardInput(update);
                }
            default:
                break;
        }
    }

    private void buildCardInput(GameUpdate update) {
        System.out.println(ConsoleColors.GREEN + client.getPlayerName() + " received the message!" + ConsoleColors.RESET);
        switch (update.getNewSate()) {
            case "ParticipationState":
                model.manageParticipation(client.getPlayerID(), false, 0);
                break;
            case "CargoRewardState":
                List<Cargo> cargoReward = getCargoList();

                HashMap<Integer, Coordinates> acceptedCargo = new HashMap<>();
                for (int i = 0; i < cargoReward.size(); i++) {
                    acceptedCargo.put(i++, new Coordinates(0, 0));
                }

                model.manageAcceptedCargo(client.getPlayerID(), acceptedCargo);
                break;
            case "CreditRewardState":
                model.manageCreditChoice(client.getPlayerID(), true);
                break;
            case "CrewmatePenaltyState":
                List<Coordinates> removedCrewmates = selectNRandomCrewmates();
                model.manageRemovedCrewmate(client.getPlayerID(), removedCrewmates);
                break;
            case "CalculateFirePowerState":
            case "CannonSelectionState":
            case "BigMeteorState":
                List<List<Coordinates>> emptyCannons = new ArrayList<>();
                model.activateCannon(client.getPlayerID(), emptyCannons);
                break;
            case "CalculateEnginePowerState":
            case "EngineSelectionState":
                List<List<Coordinates>> emptyEngines = new ArrayList<>();
                model.activateEngine(client.getPlayerID(), emptyEngines);
                break;
            case "ActivateShieldState":
            case "SmallMeteorState":
            case "AttackState":
                List<List<Coordinates>> emptyShield = new ArrayList<>();
                model.activateShield(client.getPlayerID(), emptyShield);
                break;
            default:
                System.out.println("\u001B[31mNOT FOUND STATE NEITHER CARD!!!\u001B[0m");
                throw new RuntimeException("NOT FOUND STATE NEITHER CARD!!!");
        }
    }

    private List<Cargo> getCargoList() {
        List<Cargo> rewardList = new ArrayList<>();

        @SuppressWarnings("unchecked")
        List<String> serializedCargoReward = (List<String>) model.getAdventureDeck().getLastDrawnCard().getEventData().get("cargoReward");

        for (String cargoValue : serializedCargoReward) {
            Cargo cargo = new Cargo(Color.valueOf(cargoValue));

            rewardList.add(cargo);
        }

        return rewardList;
    }

    private List<Coordinates> selectNRandomCrewmates() {
        List<Coordinates> available = new ArrayList<>();
        int crewmatePenalty = (int) model.getAdventureDeck().getLastDrawnCard().getEventData().get("crewmatePenalty");
        ShipManager ship = client.getShipManager();

        // Cabins
        Set<List<Integer>> cabins = ship.getAllComponentsPositionOfType(CabinModule.class);
        for (List<Integer> cabinCoord : cabins) {
            CabinModule cabin = (CabinModule) ship.getComponent(cabinCoord.get(0), cabinCoord.get(1)).get();
            int crewmates = cabin.getCrewmates().size();
            for (int i = 0; i < crewmates; i++) {
                available.add(new Coordinates(cabinCoord.get(1), cabinCoord.get(0)));
            }
        }
        // Central cabins
        Set<List<Integer>> centralCabins = ship.getAllComponentsPositionOfType(CentralCabin.class);
        for (List<Integer> centralCabinCoord : centralCabins) {
            CentralCabin cabin = (CentralCabin) ship.getComponent(centralCabinCoord.get(0), centralCabinCoord.get(1)).get();
            int crewmates = cabin.getCrewmates().size();
            for (int i = 0; i < crewmates; i++) {
                available.add(new Coordinates(centralCabinCoord.get(1), centralCabinCoord.get(0)));
            }
        }

        // Mischia e prendi i primi N
        Collections.shuffle(available);
        if (crewmatePenalty < available.size()) {
            return available.subList(0, crewmatePenalty);
        } else {
            return available;
        }
    }
}
