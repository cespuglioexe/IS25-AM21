package it.polimi.it.galaxytrucker.aventurecard;

import it.polimi.it.galaxytrucker.componenttiles.BatteryComponent;
import it.polimi.it.galaxytrucker.componenttiles.CabinModule;
import it.polimi.it.galaxytrucker.componenttiles.SpecialCargoHold;
import it.polimi.it.galaxytrucker.managers.Player;
import it.polimi.it.galaxytrucker.utility.Cargo;
import it.polimi.it.galaxytrucker.utility.Color;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CargoManager {


    public void manageCargoDischarge(int numCargo, Player player) {
            while(numCargo != 0){
                try{
                    int n=0;
                        if(player.getShipManager().getCargoPositon().get(Color.RED).isEmpty() == false){
                            for (List<Integer> list : player.getShipManager().getCargoPositon().get(Color.RED)) {
                                if(n==0){
                                    player.getShipManager().removeCargo(
                                            list.get(0),
                                            list.get(1),
                                            Color.RED);
                                    numCargo--;
                                    n++;
                                }
                            }
                        } else{
                            if(player.getShipManager().getCargoPositon().get(Color.YELLOW).isEmpty() == false){
                                for (List<Integer> list : player.getShipManager().getCargoPositon().get(Color.YELLOW)) {
                                    if(n==0){
                                        player.getShipManager().removeCargo(
                                                list.get(0),
                                                list.get(1),
                                                Color.YELLOW);
                                        numCargo--;
                                        n++;
                                    }
                                }
                            } else{
                                if(player.getShipManager().getCargoPositon().get(Color.GREEN).isEmpty() == false){
                                    for (List<Integer> list : player.getShipManager().getCargoPositon().get(Color.GREEN)) {
                                        if(n==0){
                                            player.getShipManager().removeCargo(
                                                    list.get(0),
                                                    list.get(1),
                                                    Color.GREEN);
                                            numCargo--;
                                            n++;
                                        }
                                    }
                                } else{
                                    for (List<Integer> list : player.getShipManager().getCargoPositon().get(Color.BLUE)) {
                                        if(n==0){
                                            player.getShipManager().removeCargo(
                                                    list.get(0),
                                                    list.get(1),
                                                    Color.BLUE);
                                            numCargo--;
                                            n++;
                                        }
                                    }
                                }
                            }
                        }

                        for (List<Integer> position : player.getShipManager().getAllComponentsPositionOfType(BatteryComponent.class)) {
                            if(n==0){
                                player.getShipManager().removeBattery(
                                        position.get(0),
                                        position.get(1));
                                numCargo--;
                                n++;
                            }
                        }
                }catch(Exception e){
                    int del;
                    for(int i=0;i<numCargo;i++){
                        del=0;
                        for (List<Integer> position : player.getShipManager().getAllComponentsPositionOfType(BatteryComponent.class)) {
                                if(position.isEmpty() == false){
                                    BatteryComponent battery = (BatteryComponent) player.getShipManager().getComponent(position.get(0),position.get(1)).orElse(null);
                                    if(battery.getBatteryCapacity() != 0) {
                                        if (del == 0) {
                                            player.getShipManager().removeBattery(
                                                    position.get(0),
                                                    position.get(1));
                                            i++;
                                            del = 1;
                                        }
                                    } else{ i=numCargo;
                                        numCargo=0;}
                                } else{ i=numCargo;
                                    numCargo=0;}
                        }

                    }
                }
            }
    }



    public void manageCargoAddition(Cargo load, Player player) {
            List<Integer> position = new ArrayList<Integer>();
            position.add(7);
            position.add(6);
         //   position = waitingCoordinates();
            player.getShipManager().addCargo(position.get(0),position.get(1),load);
    }


}
