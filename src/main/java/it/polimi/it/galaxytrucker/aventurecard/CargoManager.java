package it.polimi.it.galaxytrucker.aventurecard;

import it.polimi.it.galaxytrucker.componenttiles.BatteryComponent;
import it.polimi.it.galaxytrucker.managers.Player;
import it.polimi.it.galaxytrucker.managers.ShipManager;
import it.polimi.it.galaxytrucker.utility.Cargo;
import it.polimi.it.galaxytrucker.utility.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CargoManager {


    public void manageCargoDischarge(int reqCargo, Player player) {
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
                while (((BatteryComponent) shipManager.getComponent(pos.get(0), pos.get(1)).get()).getBatteryCapacity() != 0) {
                    shipManager.removeBattery(pos.get(0), pos.get(1));
                    consumedCargo++;
                    // If the required amount of cargo has been removed, exits the loop
                    if (consumedCargo == reqCargo) return;
                }
            }
        }

        // If there aren't enough batteries to make up the difference, there is nothing more to do and the function returns
    }



    public void manageCargoAddition(Cargo load, Player player) {
            List<Integer> position = new ArrayList<Integer>();
            position.add(7);
            position.add(6);
         //   position = waitingCoordinates();
            player.getShipManager().addCargo(position.get(0),position.get(1),load);
    }
}
