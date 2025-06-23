package it.polimi.it.galaxytrucker.view.GUI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import it.polimi.it.galaxytrucker.model.utility.Color;
import it.polimi.it.galaxytrucker.view.GUI.controllers.GUIParticipationChoiceController;
import it.polimi.it.galaxytrucker.view.GUI.controllers.GUIPlanetsSelectionController;

public class CardUpdateHandler {
    public static void handle(String card, String cardState, Map<String, Object> cardDetails) {
        switch(card) {
            case "AbandonedShip":
                break;
            case "AbandonedStation":
                break;
            case "BigMeteorSwarm":
            case "MeteorSwarm":
                break;
            case "CombatZone":
                break;
            case "Epidemic":
                break;
            case "OpenSpace":
                break;
            case "Pirates":
                break;
            case "Planets":
                handlePlanets(cardState, cardDetails);
                break;
            case "Slavers":
                break;
            case "Smugglers":
                break;
            case "StarDust":
                break;
        }
    }

    private static void handlePlanets(String cardState, Map<String, Object> cardDetails) {
        switch (cardState) {
            case "ParticipationState":
                GUIPlanetsSelectionController.getInstance().updateOccupiedPlanets();
                break;
        }
    }
}
