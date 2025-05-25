package it.polimi.it.galaxytrucker.model.gamestates;

import java.util.List;
import java.util.Set;

import it.polimi.it.galaxytrucker.model.componenttiles.ComponentTile;
import it.polimi.it.galaxytrucker.model.design.statePattern.*;
import it.polimi.it.galaxytrucker.exceptions.InvalidActionException;
import it.polimi.it.galaxytrucker.exceptions.InvalidFunctionCallInState;
import it.polimi.it.galaxytrucker.model.managers.GameManager;
import it.polimi.it.galaxytrucker.model.managers.Player;

import java.util.UUID;

public abstract class GameState extends State {
    // CONNECTION STATE
    public void addPlayer(StateMachine fsm, Player player) throws InvalidActionException, InvalidFunctionCallInState {
        throw new InvalidFunctionCallInState("Can't add player in state: " + this.getClass().getSimpleName());
    }
    public void removePlayer(StateMachine fsm, UUID id) throws InvalidFunctionCallInState {
        throw new InvalidFunctionCallInState("Can't remove player in state: " + this.getClass().getSimpleName());
    }

    // BUILDING STATE
    public void drawComponentTile(StateMachine fsm, UUID playerID) throws InvalidActionException, InvalidFunctionCallInState {
        throw new InvalidFunctionCallInState("Can't draw a component in state: " + this.getClass().getSimpleName());
    }
    public void placeComponentTile(StateMachine fsm, UUID playerID, int row, int column) throws InvalidFunctionCallInState {
        throw new InvalidFunctionCallInState("Can't place a component in state: " + this.getClass().getSimpleName());
    }
    public void rotateComponentTile(StateMachine fsm, UUID playerID, int row, int column) throws InvalidFunctionCallInState {
        throw new InvalidFunctionCallInState("Can't rotate a component in state: " + this.getClass().getSimpleName());
    }
    public void finishBuilding(StateMachine fsm, UUID playerID) throws InvalidFunctionCallInState {
        throw new InvalidFunctionCallInState("The current state is: " + this.getClass().getSimpleName());
    }
    public void saveComponentTile(StateMachine fsm, UUID playerID) throws InvalidFunctionCallInState {
        throw new InvalidFunctionCallInState("Can't save a component in state: " + this.getClass().getSimpleName());
    }
    public void discardComponentTile(StateMachine fsm, UUID playerID) throws InvalidFunctionCallInState {
        throw new InvalidFunctionCallInState("Can't discard a component in state: " + this.getClass().getSimpleName());
    }
    public List<ComponentTile> getDiscardedComponentTiles() {
        throw new InvalidFunctionCallInState("Can't discard a component in state: " + this.getClass().getSimpleName());
    }
    public void selectSavedComponentTile (StateMachine fsm, UUID playerID, int index) throws InvalidFunctionCallInState {
        throw new InvalidFunctionCallInState("Can't select component in state: " + this.getClass().getSimpleName());
    }
    public void selectDiscardedComponentTile (StateMachine fsm, UUID playerID, int index) throws InvalidFunctionCallInState {
        throw new InvalidFunctionCallInState("Can't select component in state: " + this.getClass().getSimpleName());
    }
    
    // FIXING STATE
    public void deleteComponentTile(StateMachine fsm, UUID playerID, int row, int column) throws InvalidFunctionCallInState {
        throw new InvalidFunctionCallInState("Can't delete component in state: " + this.getClass().getSimpleName());
    }
    public void startBuildPhaseTimer(GameManager gameManager) {
        throw new InvalidFunctionCallInState("Can't start a building timer in state: " + this.getClass().getSimpleName());
    }

    //ATTACK AFTERMATH FIXING STATE
    public void removeBranch(StateMachine fsm, UUID player, Set<List<Integer>> branch) {
        throw new InvalidFunctionCallInState("Can't remove a branch in state: " + this.getClass().getSimpleName());
    }
}
