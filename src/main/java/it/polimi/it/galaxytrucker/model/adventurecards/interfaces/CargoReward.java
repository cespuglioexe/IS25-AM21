package it.polimi.it.galaxytrucker.model.adventurecards.interfaces;

import java.util.List;

import it.polimi.it.galaxytrucker.model.utility.Cargo;;

public interface CargoReward {
    public List<Cargo> getCargoReward();
    public void acceptCargo(int cargo, int row, int column);
    public void discardCargo(int cargo);
}
