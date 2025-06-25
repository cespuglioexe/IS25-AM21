package it.polimi.it.galaxytrucker.model.managers;

import it.polimi.it.galaxytrucker.model.componenttiles.BatteryComponent;
import it.polimi.it.galaxytrucker.model.componenttiles.CargoHold;
import it.polimi.it.galaxytrucker.model.componenttiles.SpecialCargoHold;
import it.polimi.it.galaxytrucker.model.utility.Cargo;
import it.polimi.it.galaxytrucker.model.utility.Color;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CargoManager {
    // TODO: notify clients of ship modification

    public static void manageCargoDischarge(int reqCargo, Player player) {
        ShipManager shipManager = player.getShipManager();
        Color[] colors = Color.values();
        int consumedCargo = 0;

        // Checks for cargo of each color (from most to least precious)
        for (int i = colors.length - 1; i >= 0; i--) {
            if (!shipManager.getCargoPositon().get(colors[i]).isEmpty()) {
                // Removes most valuable cargo first, the passes to second most valuable, etc.
                for (List<Integer> pos : shipManager.getCargoPositon().get(colors[i])) {
                    shipManager.removeCargo(pos.get(0), pos.get(1), colors[i]);
                    consumedCargo++;
                    // If the required amount of cargo has been removed, exits the loop
                    if (consumedCargo == reqCargo) return;
                }
            }
        }

        // If there wasn't enough cargo to remove, batteries are removed to make up the difference
        if (consumedCargo < reqCargo) {
            for (List<Integer> pos : shipManager.getAllComponentsPositionOfType(BatteryComponent.class)) {
                // Make sure there is energy in the BatteryComponent
                while (((BatteryComponent) shipManager.getComponent(pos.get(0), pos.get(1)).get()).getBatteryCharge() != 0) {
                    shipManager.removeBattery(pos.get(0), pos.get(1));
                    consumedCargo++;
                    // If the required amount of cargo has been removed, exits the loop
                    if (consumedCargo == reqCargo) return;
                }
            }
        }

        // If there aren't enough batteries to make up the difference, there is nothing more to do and the function returns
    }

    private static Map<Color,Integer> mapCargoColorToInteger(){
        Map<Color,Integer> cargoMapInteger = new HashMap<>();
        cargoMapInteger.put(Color.RED,4);
        cargoMapInteger.put(Color.BLUE,1);
        cargoMapInteger.put(Color.GREEN,2);
        cargoMapInteger.put(Color.YELLOW,3);
        return cargoMapInteger;
    }

    private static Cargo findCargoLessValue(CargoHold cargoHold) {
        Cargo cargoLessValue;
        Map<Color,Integer> cargoMapInteger = mapCargoColorToInteger();
        cargoLessValue = cargoHold.getContainedCargo().getFirst();
        for(Cargo cargo : cargoHold.getContainedCargo()){
            if(cargoMapInteger.get(cargo.getColor()) < cargoMapInteger.get(cargoLessValue.getColor())){
                cargoLessValue = cargo;
            }
        }
        return cargoLessValue;
    }

    public static void manageCargoAddition(Cargo load, List<Integer> coords, Player player) {
        ShipManager shipManager = player.getShipManager();
        if(((shipManager.getComponent(coords.get(0), coords.get(1)).get() instanceof CargoHold) ||
            (shipManager.getComponent(coords.get(0), coords.get(1)).get() instanceof SpecialCargoHold))
            && shipManager.getComponent(coords.get(0), coords.get(1)).isPresent()) {

            CargoHold cargoHold = (CargoHold)shipManager.getComponent(coords.get(0), coords.get(1)).get();
            if(cargoHold.getContainedCargo().size() < cargoHold.getContainerNumber())
                player.getShipManager().addCargo(coords.get(0),coords.get(1), load);
            else {
                Cargo cargoLessValue = findCargoLessValue(cargoHold);
                shipManager.removeCargo(coords.get(0), coords.get(1), cargoLessValue.getColor());
                player.getShipManager().addCargo(coords.get(0),coords.get(1), load);
            }
        }
    }
}
