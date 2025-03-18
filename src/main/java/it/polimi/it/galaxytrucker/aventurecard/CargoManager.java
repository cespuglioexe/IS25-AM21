package it.polimi.it.galaxytrucker.aventurecard;

import it.polimi.it.galaxytrucker.componenttiles.CabinModule;
import it.polimi.it.galaxytrucker.componenttiles.SpecialCargoHold;
import it.polimi.it.galaxytrucker.managers.Player;
import it.polimi.it.galaxytrucker.utility.Cargo;
import it.polimi.it.galaxytrucker.utility.Color;

import java.util.List;
import java.util.Set;

public class CargoManager {


    public void manageCargoDischarge(int numCargo, Player player) {

                // Prima prendo le coordinate
                // poi getComponent(con le coordinate)
                // CargoHold

                // Controllo con un try-catch tutte le special CargoHold e  poi CargoHold fino a che non
                // esco dal ciclo

                // Se il numCargo == 0, viceversa lancio un for in cui elimino batterie
                // getLista e poi chiami removeBattery con le coordinate della lista (BatteryComponent

            while(numCargo != 0){
                try{




                }catch(Exception e){
                    System.out.println(e);
                }
            }


    }



    public void manageCargoAddition(Cargo load, Player player, int row, int column) {
            player.getShipManager().addCargo(row,column,load);
    }


}
