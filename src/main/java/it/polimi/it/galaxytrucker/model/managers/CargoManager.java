package it.polimi.it.galaxytrucker.model.managers;

import it.polimi.it.galaxytrucker.model.componenttiles.BatteryComponent;
import it.polimi.it.galaxytrucker.model.utility.Cargo;
import it.polimi.it.galaxytrucker.model.utility.Color;

import java.util.List;

public class CargoManager {
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

    public static void manageCargoAddition(Cargo load, List<Integer> coords, Player player) {
        player.getShipManager().addCargo(coords.get(0),coords.get(1), load);
    }
}
