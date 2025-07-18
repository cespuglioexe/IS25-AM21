package it.polimi.it.galaxytrucker.model.managers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JsonNode;

import com.fasterxml.jackson.databind.ObjectMapper;
import it.polimi.it.galaxytrucker.listeners.Listener;
import it.polimi.it.galaxytrucker.listeners.Observable;
import it.polimi.it.galaxytrucker.model.adventurecards.AdventureDeck;
import it.polimi.it.galaxytrucker.model.adventurecards.cardevents.InputNeeded;
import it.polimi.it.galaxytrucker.model.adventurecards.cardevents.UpdateStatus;
import it.polimi.it.galaxytrucker.model.adventurecards.cards.*;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.AdventureCard;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.AdventureCardInputContext;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.attack.Projectile;
import it.polimi.it.galaxytrucker.model.componenttiles.ComponentTile;
import it.polimi.it.galaxytrucker.model.componenttiles.DoubleCannon;
import it.polimi.it.galaxytrucker.model.crewmates.Alien;
import it.polimi.it.galaxytrucker.model.crewmates.Crewmate;
import it.polimi.it.galaxytrucker.model.crewmates.Human;
import it.polimi.it.galaxytrucker.model.design.statePattern.State;
import it.polimi.it.galaxytrucker.model.design.statePattern.StateMachine;
import it.polimi.it.galaxytrucker.model.gamestates.GameState;
import it.polimi.it.galaxytrucker.model.gamestates.StartState;
import it.polimi.it.galaxytrucker.exceptions.IllegalComponentPositionException;
import it.polimi.it.galaxytrucker.exceptions.InvalidActionException;
import it.polimi.it.galaxytrucker.exceptions.NotFoundException;
import it.polimi.it.galaxytrucker.model.json.Json;
import it.polimi.it.galaxytrucker.messages.servermessages.GameUpdate;
import it.polimi.it.galaxytrucker.messages.servermessages.GameUpdateType;
import it.polimi.it.galaxytrucker.messages.servermessages.GameUpdate.GameUpdateBuilder;
import it.polimi.it.galaxytrucker.model.utility.Cargo;
import it.polimi.it.galaxytrucker.model.utility.Color;
import it.polimi.it.galaxytrucker.model.utility.Coordinates;
import it.polimi.it.galaxytrucker.model.utility.Direction;
import it.polimi.it.galaxytrucker.model.utility.ProjectileType;
import it.polimi.it.galaxytrucker.view.CLI.ConsoleColors;
// import it.polimi.it.galaxytrucker.networking.rmi.server.RMIServer;
;

public class GameManager extends StateMachine implements Model, Observable {
    private final Integer level;
    private final Integer numberOfPlayers;
    private final List<Player> players;
    private Set<ComponentTile> components;
    private final FlightBoard flightBoard;
    private final AdventureDeck adventureDeck;

    private final List<Listener> listeners;

    public GameManager(int level, int numberOfPlayers) {
        this.level = level;
        this.numberOfPlayers = numberOfPlayers;
        this.flightBoard = new FlightBoard(level);
        this.adventureDeck = new AdventureDeck();
        this.players = new ArrayList<>();

        this.listeners = new ArrayList<>();

        start(new StartState());
    }

    @Override
    public Integer getLevel() {
        return this.level;
    }

    @Override
    public Integer getNumberOfPlayers() {
        return this.numberOfPlayers;
    }

    @Override
    public List<Player> getPlayers() {
        return this.players;
    }

    @Override
    public Player getPlayerByID(UUID id) {
        return this.players.stream()
            .filter(player -> player.getPlayerID().equals(id))
            .findFirst()
            .orElse(null);
    }

    @Override
    public List<Player> getPlayerRank() {
        return players.stream()
            .sorted(Comparator.comparingInt(Player::getCredits).reversed())
            .toList();
    }

    @Override
    public boolean allPlayersConnected() {
        return this.players.size() == this.numberOfPlayers;
    }

    @Override
    public ShipManager getPlayerShip(UUID id) {
        return Optional.ofNullable(this.getPlayerByID(id).getShipManager())
            .orElse(null);
    }

    @Override
    public Set<Player> getPlayersWithIllegalShips() {
        return this.players.stream()
            .filter(player -> !player.getShipManager().isShipLegal())
            .collect(Collectors.toSet());
    }

    @Override
    public Set<ComponentTile> getComponentTiles() {
        return this.components;
    }

    @Override
    public FlightBoard getFlightBoard() {
        return this.flightBoard;
    }

    @Override
    public AdventureDeck getAdventureDeck() {
        return this.adventureDeck;
    }

    @Override
    public void getSavedComponentTiles(UUID playerId) {
//        sendGameUpdateToSinglePlayer(playerId,
//                new GameUpdate.GameUpdateBuilder(GameUpdateType.TILE_LIST)
//                        .setTileList(getPlayerByID(playerId).getShipManager().getSavedComponentTiles())
//                        .build()
//        );
    }

    @Override
    public List<ComponentTile> getDiscardedComponentTiles(UUID playerId) {
        GameState gameState = (GameState) this.getCurrentState();

        return gameState.getDiscardedComponentTiles();
    }


    @Override
    public void getPlayerShipBoard(UUID playerId) {
//        sendGameUpdateToSinglePlayer(
//                playerId,
//                new GameUpdate.GameUpdateBuilder(GameUpdateType.SHIP_DATA)
//                        .setShipBoard(getPlayerShip(playerId).getShipBoard())
//                        .build()
//        );
    }


    /**
     * Adds a new player to the game.
     *
     * @throws InvalidActionException if the game is already full
     */
    @Override
    public void addPlayer(Player player) throws InvalidActionException {
        GameState gameState = (GameState) this.getCurrentState();
        gameState.addPlayer(this, player);
    }

    @Override
    public void removePlayer(UUID id, int col, int row) throws NotFoundException {
        GameState gameState = (GameState) this.getCurrentState();
        gameState.removePlayer(this, id);
    }

    public void initializeComponentTiles() {
        try (InputStream input = getClass().getResourceAsStream("/it/polimi/it/galaxytrucker/json/componenttiles.json")) {
            if (input == null) {
                throw new FileNotFoundException("componenttiles.json not found in resources");
            }

            String json = new String(input.readAllBytes(), StandardCharsets.UTF_8);
            JsonNode node = Json.parse(json);
            components = Json.fromJsonSet(node, ComponentTile.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initializeAdventureDeck() {
        try {
            List<AdventureCard> cards_lvl1 = loadCards(getClass().getResourceAsStream("/it/polimi/it/galaxytrucker/json/cards_lvl1.json"));
            List<AdventureCard> cards_lvl2 = loadCards(getClass().getResourceAsStream("/it/polimi/it/galaxytrucker/json/cards_lvl2.json"));
            List<AdventureCard> cards_testFlight = loadCards(getClass().getResourceAsStream("/it/polimi/it/galaxytrucker/json/cards_testFlight.json"));

            for(int i=0;i<4;i++){
                if(getLevel()==1){
                    adventureDeck.addStack(i,List.of(
                            cards_testFlight.get(new Random().nextInt(cards_testFlight.size())),
                            cards_testFlight.get(new Random().nextInt(cards_testFlight.size()))
                    ));
                } else if (getLevel() == 2) {
                    adventureDeck.addStack(i,List.of(
                            cards_lvl1.get(new Random().nextInt(cards_lvl1.size())),
                            cards_lvl2.get(new Random().nextInt(cards_lvl2.size())),
                            cards_lvl2.get(new Random().nextInt(cards_lvl2.size()))
                            ));
                }
            }
            adventureDeck.initializeDeck();
            adventureDeck.shuffle();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private List<AdventureCard> loadCards(InputStream input) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, Object>> rawCards = mapper.readValue(input, List.class);

        return rawCards.stream()
                .map(this::createCardFromMap)
                .toList();
    }

    private AdventureCard createCardFromMap(Map<String, Object> cardData) throws IllegalArgumentException {
        String cardType = (String) cardData.get("cardType");
        return switch (cardType) {
            case "Planets" -> createPlanetsCard(cardData);
            case "CombatZone" -> createCombatZoneCard(cardData);
            case "MeteorSwarm" -> createMeteorSwarmCard(cardData);
            case "Pirates" -> createPiratesCard(cardData);
            case "Abandoned Ship" -> createAbandonedShipCard(cardData);
            case "Abandoned Station" -> createAbandonedStationCard(cardData);
            case "Epidemic" -> createEpidemicCard(cardData);
            case "OpenSpace" -> createOpenSpaceCard(cardData);
            case "Smugglers" -> createSmugglersCard(cardData);
            case "Slavers" -> createSlaversCard(cardData);
            case "StarDust" -> createStarDustCard(cardData);
            default -> null;
        };
    }

    private Planets createPlanetsCard(Map<String, Object> data) {
        int numberPlanets = (Integer) data.get("numberOfPlanets");
        List<List<Cargo>> cargoRewardbyPlanets = convertToListCargoList((List<List<String>>) data.get("cargoRewardsByPlanet"));
        int flightDayPenalty = (Integer) data.get("flightDayPenalty");
        String graphic = (String) data.get("graphic");

        return new Planets(numberPlanets,
                cargoRewardbyPlanets,flightDayPenalty,
                new FlightBoardFlightRules(flightBoard),graphic);
    }

    private MeteorSwarm createMeteorSwarmCard(Map<String, Object> data) {

        List<Projectile> projectiles = convertToProjectileList((List<List<String>>) data.get("projectiles"));
        String graphic = (String) data.get("graphic");

        return new MeteorSwarm(projectiles,
                new FlightBoardFlightRules(flightBoard),graphic);

    }

    private CombatZone createCombatZoneCard(Map<String, Object> data) {

        int crewmatePenalty = (Integer) data.get("crewmatePenalty");
        int flightDayPenalty = (Integer) data.get("flightDayPenalty");

        String graphic = (String) data.get("graphic");


        return new CombatZone(crewmatePenalty,flightDayPenalty,
                new FlightBoardFlightRules(flightBoard),graphic);
    }

    private Pirates createPiratesCard(Map<String, Object> data) {

        int firePowerRequired = (Integer) data.get("firePowerRequired");
        int creditReward = (Integer) data.get("creditReward");
        List<Projectile> projectiles = convertToProjectileList((List<List<String>>) data.get("projectiles"));
        int flightDayPenalty = (Integer) data.get("flightDayPenalty");
        String graphic = (String) data.get("graphic");

        return new Pirates(firePowerRequired,creditReward,
                flightDayPenalty, projectiles,
                new FlightBoardFlightRules(flightBoard),graphic);

    }

    private AbandonedShip createAbandonedShipCard(Map<String, Object> data) {
        int crewmatePenalty = (Integer) data.get("crewmatePenalty");
        int creditReward = (Integer) data.get("creditReward");
        int flightDayPenalty = (Integer) data.get("flightDayPenalty");
        String graphic = (String) data.get("graphic");

        return new AbandonedShip(creditReward,crewmatePenalty,flightDayPenalty,
                new FlightBoardFlightRules(flightBoard),graphic);
    }

    private AbandonedStation createAbandonedStationCard(Map<String, Object> data) {

        List<Cargo> cargoReward = convertToCargoList((List<List<String>>) data.get("cargoReward"));
        int crewmateRequired = (Integer) data.get("crewmateRequired");
        int flightDayPenalty = (Integer) data.get("flightDayPenalty");
        String graphic = (String) data.get("graphic");

        return  new AbandonedStation(cargoReward,crewmateRequired,flightDayPenalty,
                new FlightBoardFlightRules(flightBoard),graphic);

    }

    private Epidemic createEpidemicCard(Map<String, Object> data) {
        String graphic = (String) data.get("graphic");
        return new Epidemic(new FlightBoardFlightRules(flightBoard),graphic);
    }

    private OpenSpace createOpenSpaceCard(Map<String, Object> data) {
        String graphic = (String) data.get("graphic");
        return new OpenSpace(new FlightBoardFlightRules(flightBoard),graphic);
    }

    private Smugglers createSmugglersCard(Map<String, Object> data) {

        int firePowerRequired = (Integer) data.get("firePowerRequired");
        int cargoPenalty = (Integer) data.get("cargoPenalty");
        List<Cargo> cargoReward = convertToCargoList((List<List<String>>) data.get("cargoReward"));
        int flightDayPenalty = (Integer) data.get("flightDayPenalty");
        String graphic = (String) data.get("graphic");

        return new Smugglers(firePowerRequired,cargoReward,cargoPenalty,flightDayPenalty,
                new FlightBoardFlightRules(flightBoard),graphic);

    }

    private Slavers createSlaversCard(Map<String, Object> data) {

        int firePowerRequired = (Integer) data.get("firePowerRequired");
        int crewmatePenalty = (Integer) data.get("crewmatePenalty");
        int creditReward = (Integer) data.get("creditReward");
        int flightDayPenalty = (Integer) data.get("flightDayPenalty");
        String graphic = (String) data.get("graphic");

        return new Slavers(creditReward,crewmatePenalty,flightDayPenalty,firePowerRequired,
                new FlightBoardFlightRules(flightBoard),graphic);
    }

    private StarDust createStarDustCard(Map<String, Object> data) {
        int flightDayPenalty = (Integer) data.get("flightDayPenalty");
        String graphic = (String) data.get("graphic");
        return new StarDust(flightDayPenalty,new FlightBoardFlightRules(flightBoard),graphic);
    }

    private List<Projectile> convertToProjectileList(List<List<String>> raw) {
        return raw.stream()
                .map(innerList -> {

                    String typeStr = innerList.get(0).replace("ProjectileType.", "");
                    String directionStr = innerList.get(1).replace("Direction.", "");

                    ProjectileType type = ProjectileType.valueOf(typeStr);
                    Direction direction = Direction.valueOf(directionStr);

                    return new Projectile(type, direction);
                })
                .toList();
    }

    private List<List<Cargo>> convertToListCargoList(List<List<String>> raw) {
        return raw.stream()
                .map(innerList -> innerList.stream()
                        .map(colorStr -> new Cargo(Color.valueOf(colorStr)))
                        .toList())
                .toList();
    }

    private List<Cargo> convertToCargoList(List<List<String>> raw) {
        return raw.stream()
                .flatMap(innerList -> innerList.stream()
                        .map(colorStr -> new Cargo(Color.valueOf(colorStr))))
                .collect(Collectors.toList());
    }

    public void drawComponentTile(UUID playerID) throws InvalidActionException {
        GameState gameState = (GameState) this.getCurrentState();
        gameState.drawComponentTile(this, playerID);
    }

    @Override
    public void placeComponentTile(UUID playerID, int row, int column, int rotation) throws IndexOutOfBoundsException, IllegalComponentPositionException {
        GameState gameState = (GameState) this.getCurrentState();
        gameState.placeComponentTile(this, playerID, row, column);

        for(int i = 0; i < rotation; i++) {
            gameState.rotateComponentTile(this, playerID, row, column);
        }

        updateListeners(new GameUpdate.GameUpdateBuilder(GameUpdateType.PLAYER_SHIP_UPDATED)
                .setShipBoard(getPlayerShip(playerID).getShipBoard())
                .setInterestedPlayerId(playerID)
                .build()
        );
    }

    public void rotateComponentTile(UUID playerID, int row, int column) throws IndexOutOfBoundsException, IllegalComponentPositionException {
        GameState gameState = (GameState) this.getCurrentState();
        gameState.rotateComponentTile(this, playerID, row, column);
    }

    public void finishBuilding(UUID playerID) {
        GameState gameState = (GameState) this.getCurrentState();
        gameState.finishBuilding(this, playerID);

        // TODO: notify
    }


    @Override
    public void saveComponentTile(UUID playerID) {
        GameState gameState = (GameState) this.getCurrentState();
        gameState.saveComponentTile(this, playerID);
        
        Player player = getPlayerByID(playerID);
        player.updateListeners(
                new GameUpdate.GameUpdateBuilder(GameUpdateType.SAVED_COMPONENTS_UPDATED)
                        .setTileList(player.getShipManager().getSavedComponentTiles())
                        .build()
        );
    }

    @Override
    public void discardComponentTile(UUID playerID) {
        GameState gameState = (GameState) this.getCurrentState();
        gameState.discardComponentTile(this, playerID);

        updateListeners(
                new GameUpdate.GameUpdateBuilder(GameUpdateType.DISCARDED_COMPONENTS_UPDATED)
                        .setTileList(((GameState) getCurrentState()).getDiscardedComponentTiles())
                        .build()
        );
    }

    @Override
    public void addCrewmateToCabin(UUID playerID, int row, int column, Crewmate crewmate) {
        if (crewmate instanceof Human)
            getPlayerShip(playerID).addCrewmate(row, column, (Human) crewmate);
        else
            getPlayerShip(playerID).addCrewmate(row, column, (Alien) crewmate);

        updateListeners(new GameUpdate.GameUpdateBuilder(GameUpdateType.PLAYER_SHIP_UPDATED)
                .setShipBoard(getPlayerShip(playerID).getShipBoard())
                .setInterestedPlayerId(playerID)
                .build()
        );
    }

    @Override
    public void selectSavedComponentTile(UUID playerID, int index) {
        GameState gameState = (GameState) this.getCurrentState();
        gameState.selectSavedComponentTile(this, playerID, index);

        Player player = getPlayerByID(playerID);
        player.updateListeners(
                new GameUpdate.GameUpdateBuilder(GameUpdateType.SAVED_COMPONENTS_UPDATED)
                        .setTileList(player.getShipManager().getSavedComponentTiles())
                        .build()
        );
    }

    @Override
    public void selectDiscardedComponentTile(UUID playerID, int index) {
        GameState gameState = (GameState) this.getCurrentState();
        gameState.selectDiscardedComponentTile(this, playerID, index);

        updateListeners(
                new GameUpdate.GameUpdateBuilder(GameUpdateType.DISCARDED_COMPONENTS_UPDATED)
                        .setTileList(((GameState) getCurrentState()).getDiscardedComponentTiles())
                        .build()
        );
    }

    @Override
    public void deleteComponentTile(UUID playerID, int row, int column) {
        GameState gameState = (GameState) this.getCurrentState();
        gameState.deleteComponentTile(this, playerID, row, column);

        updateListeners(new GameUpdate.GameUpdateBuilder(GameUpdateType.PLAYER_SHIP_UPDATED)
                .setShipBoard(getPlayerShip(playerID).getShipBoard())
                .setInterestedPlayerId(playerID)
                .build()
        );
    }

    @Override
    public void deleteBranch(UUID playerID, Set<List<Integer>> branch) {
        GameState gameState = (GameState) this.getCurrentState();
        gameState.removeBranch(this, playerID, branch);

        // TODO: notify
    }

    @Override
    public void startBuildPhaseTimer() {
        GameState gameState = (GameState) this.getCurrentState();
        gameState.startBuildPhaseTimer(this);
    }

    @Override
    public void activateCannon(UUID playerID, List<List<Coordinates>> cannonAndBatteries) {
        AdventureCardInputContext response = new AdventureCardInputContext();
        AdventureCardInputDispatcher inputHandler = new AdventureCardInputDispatcherImpl();
        ShipManager ship = getPlayerShip(playerID);

        response.put(AdventureCardInputFields.PLAYER, getPlayerByID(playerID));
        if (cannonAndBatteries.isEmpty()) {
            response.put(AdventureCardInputFields.ACTIVATES_DOUBLE_CANNONS, false);
            response.put(AdventureCardInputFields.ACTIVATES_SINGLE_CANNON, false);

        } else if (isDoubleCannon(ship, cannonAndBatteries.getFirst().getFirst())) {
            response.put(AdventureCardInputFields.ACTIVATES_DOUBLE_CANNONS, true);
            HashMap<List<Integer>, List<Integer>> doubleCannonAndBatteries = buildDoubleCannonResponse(cannonAndBatteries);

            response.put(AdventureCardInputFields.DOUBLE_CANNONS_AND_BATTERIES, doubleCannonAndBatteries);

        } else {
            response.put(AdventureCardInputFields.ACTIVATES_SINGLE_CANNON, true);

            List<Integer> singleCannon = new ArrayList<>();
            Coordinates coord = cannonAndBatteries.getFirst().getFirst();

            singleCannon.add(coord.getRow());
            singleCannon.add(coord.getColumn());

            response.put(AdventureCardInputFields.SINGLE_CANNON, singleCannon);
        }
        System.out.println(ConsoleColors.MODEL_DEBUG + response.toString() + ConsoleColors.RESET);

        inputHandler.dispatch(adventureDeck.getLastDrawnCard(), response);
    }
    private boolean isDoubleCannon(ShipManager ship, Coordinates coord) {
        ComponentTile tile = ship.getComponent(coord.getRow(), coord.getColumn()).get();

        return tile instanceof DoubleCannon;
    }
    private HashMap<List<Integer>, List<Integer>> buildDoubleCannonResponse(List<List<Coordinates>> cannonAndBatteries) {
        HashMap<List<Integer>, List<Integer>> doubleCannonAndBatteries = new HashMap<>();

        for (List<Coordinates> cannonAndBattery : cannonAndBatteries) {
            List<Integer> cannonCoord = new ArrayList<>();
            List<Integer> batteryCoord = new ArrayList<>();

            cannonCoord.add(cannonAndBattery.getFirst().getRow());
            cannonCoord.add(cannonAndBattery.getFirst().getColumn());

            batteryCoord.add(cannonAndBattery.getLast().getRow());
            batteryCoord.add(cannonAndBattery.getLast().getColumn());

            doubleCannonAndBatteries.put(cannonCoord, batteryCoord);
        }

        return doubleCannonAndBatteries;
    }

    @Override
    public void activateEngine(UUID playerID, List<List<Coordinates>> engineAndBatteries) {
        AdventureCardInputContext response = new AdventureCardInputContext();
        AdventureCardInputDispatcher inputHandler = new AdventureCardInputDispatcherImpl();

        response.put(AdventureCardInputFields.PLAYER, getPlayerByID(playerID));
        if (engineAndBatteries.isEmpty()) {
            response.put(AdventureCardInputFields.ACTIVATES_DOUBLE_ENGINES, false);
        } else {
            response.put(AdventureCardInputFields.ACTIVATES_DOUBLE_ENGINES, true);
            response.put(AdventureCardInputFields.DOUBLE_ENGINES_AND_BATTERIES, buildDoubleEnigneResponse(engineAndBatteries));
        }

        inputHandler.dispatch(adventureDeck.getLastDrawnCard(), response);
    }
    private HashMap<List<Integer>, List<Integer>> buildDoubleEnigneResponse(List<List<Coordinates>> engineAndBatteries) {
        HashMap<List<Integer>, List<Integer>> doubleEngineAndBatteries = new HashMap<>();

        for (List<Coordinates> cannonAndBattery : engineAndBatteries) {
            List<Integer> engineCoord = new ArrayList<>();
            List<Integer> batteryCoord = new ArrayList<>();

            engineCoord.add(cannonAndBattery.getFirst().getRow());
            engineCoord.add(cannonAndBattery.getFirst().getColumn());

            batteryCoord.add(cannonAndBattery.getLast().getRow());
            batteryCoord.add(cannonAndBattery.getLast().getColumn());

            doubleEngineAndBatteries.put(engineCoord, batteryCoord);
        }

        return doubleEngineAndBatteries;
    }

    @Override
    public void activateShield(UUID playerID, List<List<Coordinates>> shieldAndBatteries) {
        AdventureCardInputContext response = new AdventureCardInputContext();
        AdventureCardInputDispatcher inputHandler = new AdventureCardInputDispatcherImpl();

        if (shieldAndBatteries.isEmpty()) {
            response.put(AdventureCardInputFields.ACTIVATES_SHIELD, false);
        } else {
            response.put(AdventureCardInputFields.ACTIVATES_SHIELD, true);
            response.put(AdventureCardInputFields.SHIELD_AND_BATTERIES, buildShieldResponse(shieldAndBatteries));
        }

        inputHandler.dispatch(adventureDeck.getLastDrawnCard(), response);
    }
    private HashMap<List<Integer>, List<Integer>> buildShieldResponse(List<List<Coordinates>> shieldsAndBatteries) {
        HashMap<List<Integer>, List<Integer>> shieldsAndBatteriesMap = new HashMap<>();
        for (List<Coordinates> shieldAndBattery : shieldsAndBatteries) {
            List<Integer> shieldCoord = new ArrayList<>();
            List<Integer> batteryCoord = new ArrayList<>();

            shieldCoord.add(shieldAndBattery.getFirst().getRow());
            shieldCoord.add(shieldAndBattery.getFirst().getColumn());

            batteryCoord.add(shieldAndBattery.getLast().getRow());
            batteryCoord.add(shieldAndBattery.getLast().getColumn());

            shieldsAndBatteriesMap.put(shieldCoord, batteryCoord);
        }
        return shieldsAndBatteriesMap;
    }

    @Override
    public void manageAcceptedCargo(UUID playerId, HashMap<Integer, Coordinates> acceptedCargo) {
        AdventureCardInputContext response = new AdventureCardInputContext();
        AdventureCardInputDispatcher inputHandler = new AdventureCardInputDispatcherImpl();

        for (Integer cargo : acceptedCargo.keySet()) {
            System.out.format("%d -> (%d, %d)\n", cargo, acceptedCargo.get(cargo).getRow(), acceptedCargo.get(cargo).getColumn());
        }

        for (int i : acceptedCargo.keySet()) {
            response.put(AdventureCardInputFields.LOAD_INDEX, 0);
            Coordinates coord = acceptedCargo.get(i);

            if (coord.getRow() == 0 && coord.getColumn() == 0) {
                response.put(AdventureCardInputFields.ACCEPTS_CARGO, false);
            }
            else {
                response.put(AdventureCardInputFields.ACCEPTS_CARGO, true);
                response.put(AdventureCardInputFields.ROW, coord.getRow());
                response.put(AdventureCardInputFields.COLUMN, coord.getColumn());
            }
            inputHandler.dispatch(adventureDeck.getLastDrawnCard(), response);
        }
    }

    @Override
    public void manageCreditChoice(UUID playerId, boolean creditChoice) {
        AdventureCardInputContext response = new AdventureCardInputContext();
        AdventureCardInputDispatcher inputHandler = new AdventureCardInputDispatcherImpl();

        response.put(AdventureCardInputFields.ACCEPTS_CREDIT, creditChoice);
        inputHandler.dispatch(adventureDeck.getLastDrawnCard(), response);
    }

    @Override
    public void manageRemovedCrewmate(UUID  playerId, List<Coordinates> removedCrewmate){
        AdventureCardInputContext response = new AdventureCardInputContext();
        AdventureCardInputDispatcher inputHandler = new AdventureCardInputDispatcherImpl();
        System.out.println(ConsoleColors.MAGENTA + "Pre isSlavers check: " + adventureDeck.getLastDrawnCard().getClass().getSimpleName() + ConsoleColors.RESET);
        final boolean isSlavers = adventureDeck.getLastDrawnCard() instanceof Slavers;
        System.out.println(ConsoleColors.MAGENTA + "Boolean 1: " + isSlavers + ConsoleColors.RESET);

        List<List<Integer>> crewmatesList = new ArrayList<>();
        for (Coordinates coord : removedCrewmate) {
            response.put(AdventureCardInputFields.CREWMATE_PENALTY, true);
            response.put(AdventureCardInputFields.ROW, coord.getRow());
            response.put(AdventureCardInputFields.COLUMN, coord.getColumn());
            System.out.println(ConsoleColors.MAGENTA + "Boolean 2: " + isSlavers + ConsoleColors.RESET);

            if (!isSlavers) {
                System.out.println(ConsoleColors.MAGENTA + "Actual card:" + adventureDeck.getLastDrawnCard().getClass().getSimpleName() + ConsoleColors.RESET);
                inputHandler.dispatch(adventureDeck.getLastDrawnCard(), response);
            } else {
                List<Integer> coords = new ArrayList<>();
                coords.add(coord.getRow());
                coords.add(coord.getColumn());
                crewmatesList.add(coords);
            }
        }
        if (isSlavers) {
            response.put(AdventureCardInputFields.CREWMATES, crewmatesList);
            inputHandler.dispatch(adventureDeck.getLastDrawnCard(), response);
        }
    }


    @Override
    public void manageParticipation(UUID  playerId, boolean participation, int choice){
        AdventureCardInputContext response = new AdventureCardInputContext();
        AdventureCardInputDispatcher inputHandler = new AdventureCardInputDispatcherImpl();

        response.put(AdventureCardInputFields.PLAYER, getPlayerByID(playerId));
        response.put(AdventureCardInputFields.PARTICIPATES, participation);
        response.put(AdventureCardInputFields.CHOICE, choice);
        inputHandler.dispatch(adventureDeck.getLastDrawnCard(), response);
    }


    @Override
    public void addListener(Listener listener) {
        synchronized (listeners) {
            listeners.add(listener);
        }
    }

    @Override
    public void removeListener(Listener listener) {
        synchronized (listeners) {
            listeners.remove(listener);
        }
    }

    public void updateListeners(GameUpdate command) {
        synchronized (listeners) {
            for (Listener listener : listeners) {
                listener.notify(command);
            }
        }
    }

    public List<Player> getActivePlayers() {
        return players.stream()
            .filter(player -> !player.isDefeated())
            .toList();
    }

    public void defeat(Player player) {
        player.defeat();
        flightBoard.removePlayerMarker(player);
    }

    public void updateListenersCardNeedsInput(InputNeeded event) {
        State currentState = ((StateMachine) event.getSource()).getCurrentState();
        String card = event.getSource().getClass().getSimpleName();
        Player interestedPlayer = event.getInterestedPlayer();

        GameUpdate input = new GameUpdate.GameUpdateBuilder(GameUpdateType.INPUT)
            .setInterestedPlayerId(interestedPlayer.getPlayerID())
            .setOperationMessage(card)
            .setNewSate(currentState.getClass().getSimpleName())
            .build();

        updateListeners(input);
    }

    public void updateListenersCardDetails(UpdateStatus event) {
        State currentState = ((StateMachine) event.getSource()).getCurrentState();
        String card = event.getSource().getClass().getSimpleName();
        Map<String, Object> cardDetails = event.getSource().getEventData();

        GameUpdate update = new GameUpdate.GameUpdateBuilder(GameUpdateType.CARD_DETAILS)
            .setNewSate(currentState.getClass().getSimpleName())
            .setOperationMessage(card)
            .setCardDetail(cardDetails)
            .build();

        updateListeners(update);
    }
}
