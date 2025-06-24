package it.polimi.it.galaxytrucker.model.managers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polimi.it.galaxytrucker.model.adventurecards.cards.AbandonedShip;
import it.polimi.it.galaxytrucker.model.adventurecards.cards.AbandonedStation;
import it.polimi.it.galaxytrucker.model.adventurecards.cards.BigMeteorSwarm;
import it.polimi.it.galaxytrucker.model.adventurecards.cards.CombatZone;
import it.polimi.it.galaxytrucker.model.adventurecards.cards.MeteorSwarm;
import it.polimi.it.galaxytrucker.model.adventurecards.cards.OpenSpace;
import it.polimi.it.galaxytrucker.model.adventurecards.cards.Pirates;
import it.polimi.it.galaxytrucker.model.adventurecards.cards.Planets;
import it.polimi.it.galaxytrucker.model.adventurecards.cards.Slavers;
import it.polimi.it.galaxytrucker.model.adventurecards.cards.Smugglers;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.AdventureCard;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.AdventureCardInputContext;
import it.polimi.it.galaxytrucker.model.adventurecards.interfaces.AdventureCardVisitor;
import it.polimi.it.galaxytrucker.view.CLI.ConsoleColors;

public class AdventureCardInputDispatcherImpl implements AdventureCardInputDispatcher, AdventureCardVisitor {
    @Override
    public void dispatch(AdventureCard card, AdventureCardInputContext context) {
        card.accept(this, context);
    }

    @Override
    public void visit(Planets card, AdventureCardInputContext context) {
        if (isParticipation(context)) {
            Player player = context.get("player", Player.class);
            boolean participates = context.get("participates", Boolean.class);

            if (participates) {
                int choice = context.get("choice", Integer.class);

                card.participate(player, choice);
            } else {
                card.decline(player);
            }
        } else if (isCargoReward(context)) {
            int loadIndex = context.get("loadIndex", Integer.class);
            boolean accepts = context.get("acceptsCargo", Boolean.class);

            if (accepts) {
                int row = context.get("row", Integer.class);
                int column = context.get("column", Integer.class);

                card.acceptCargo(loadIndex, row, column);
            } else {
                card.discardCargo(loadIndex);
            }
        }
    }

    @Override
    public void visit(AbandonedShip card, AdventureCardInputContext context) {
        if (isParticipation(context)) {
            Player player = context.get("player", Player.class);
            boolean participates = context.get("participates", Boolean.class);

            if (participates) {
                int choice = context.get("choice", Integer.class);

                card.participate(player, choice);
            } else {
                card.decline(player);
            }
        } else if (isCrewmatePenalty(context)) {
            int row = context.get("row", Integer.class);
            int column = context.get("column", Integer.class);

            card.applyCrewmatePenalty(row, column);
        }
    }

    @Override
    public void visit(AbandonedStation card, AdventureCardInputContext context) {
        if (isParticipation(context)) {
            Player player = context.get("player", Player.class);
            boolean participates = context.get("participates", Boolean.class);

            if (participates) {
                int choice = context.get("choice", Integer.class);

                card.participate(player, choice);
            } else {
                card.decline(player);
            }
        } else if (isCargoReward(context)) {
            int loadIndex = context.get("loadIndex", Integer.class);
            boolean accepts = context.get("acceptsCargo", Boolean.class);

            if (accepts) {
                int row = context.get("row", Integer.class);
                int column = context.get("column", Integer.class);

                card.acceptCargo(loadIndex, row, column);
            } else {
                card.discardCargo(loadIndex);
            }
        }
    }

    @Override
    public void visit(OpenSpace card, AdventureCardInputContext context) {
        if (isEngineSelection(context)) {
            Player player = context.get("player", Player.class);
            boolean activates = context.get("activatesDoubleEngines", Boolean.class);

            if (activates) {
                HashMap<List<Integer>, List<Integer>> engineAndBatteries = context.getUnsafe("engineAndBatteries");

                card.selectEngine(player, engineAndBatteries);
            } else {
                card.selectNoEngine(player);
            }
        }
    }

    @Override
    public void visit(Pirates card, AdventureCardInputContext context) {
        if (isCannonSelection(context)) {
            boolean activates = context.get("activatesDoubleCannons", Boolean.class);

            if (activates) {
                HashMap<List<Integer>, List<Integer>> cannonAndBatteries = context.getUnsafe("cannonAndBatteries");

                card.selectCannons(cannonAndBatteries);
            } else {
                card.selectNoCannons();
            }
        } else if (isCreditReward(context)) {
            boolean accepts = context.get("acceptsCredit", Boolean.class);

            if (accepts) {
                card.applyCreditReward();
            } else {
                card.discardCreditReward();
            }
        } else if (isShieldSelection(context)) {
            boolean activates = context.get("activatesShield", Boolean.class);

            if (activates) {
                HashMap<List<Integer>, List<Integer>> shieldAndBatteries = context.getUnsafe("shieldAndBatteries");

                card.activateShields(shieldAndBatteries);
            } else {
                card.activateNoShield();
            }
        }
    }

    @Override
    public void visit(Slavers card, AdventureCardInputContext context) {
        if (isCannonSelection(context)) {
            boolean activates = context.get("activatesDoubleCannons", Boolean.class);

            if (activates) {
                HashMap<List<Integer>, List<Integer>> cannonAndBatteries = context.getUnsafe("cannonAndBatteries");

                card.selectCannons(cannonAndBatteries);
            } else {
                card.selectNoCannons();
            }
        } else if (isCreditReward(context)) {
            boolean accepts = context.get("acceptsCredit", Boolean.class);

            if (accepts) {
                card.applyCreditReward();
            } else {
                card.discardCreditReward();
            }
        } else if (isCrewmatePenalty(context)) {
            int row = context.get("row", Integer.class);
            int column = context.get("column", Integer.class);

            card.applyCrewmatePenalty(row, column);
        }
    }

    @Override
    public void visit(Smugglers card, AdventureCardInputContext context) {
        if (isCannonSelection(context)) {
            boolean activates = context.get("activatesDoubleCannons", Boolean.class);

            if (activates) {
                HashMap<List<Integer>, List<Integer>> cannonAndBatteries = context.getUnsafe("cannonAndBatteries");

                card.selectCannons(cannonAndBatteries);
            } else {
                card.selectNoCannons();
            }
        } else if (isCargoReward(context)) {
            int loadIndex = context.get("loadIndex", Integer.class);
            boolean accepts = context.get("acceptsCargo", Boolean.class);

            if (accepts) {
                int row = context.get("row", Integer.class);
                int column = context.get("column", Integer.class);

                card.acceptCargo(loadIndex, row, column);
            } else {
                card.discardCargo(loadIndex);
            }
        }
    }

    @Override
    public void visit(MeteorSwarm card, AdventureCardInputContext context) {
        System.out.println(ConsoleColors.MODEL_DEBUG + "balza the king" + ConsoleColors.RESET);
        if (isCannonSelection(context)) {
            System.out.println(ConsoleColors.MODEL_DEBUG + "Marga the boss" + ConsoleColors.RESET);
            boolean activates = context.get("activatesDoubleCannons", Boolean.class);
            List<Integer> cannonCoord;

            if (activates) {
                HashMap<List<Integer>, List<Integer>> cannonAndBatteries = context.getUnsafe("cannonAndBatteries");
                
                Map.Entry<List<Integer>, List<Integer>> entry = cannonAndBatteries.entrySet().iterator().next();
                cannonCoord = entry.getKey();
                List<Integer> batteryCoord = entry.getValue();

                card.shootAtMeteorWith(cannonCoord, batteryCoord);
            } else {
                cannonCoord = context.getUnsafe("singleCannonCoord");

                card.shootAtMeteorWith(cannonCoord);
            }
        } else if (isShieldSelection(context)) {
            boolean activates = context.get("activatesShield", Boolean.class);

            if (activates) {
                HashMap<List<Integer>, List<Integer>> shieldAndBatteries = context.getUnsafe("shieldAndBatteries");

                card.activateShields(shieldAndBatteries);
            } else {
                card.activateNoShield();
            }
        }
    }

    @Override
    public void visit(BigMeteorSwarm card, AdventureCardInputContext context) {
        if (isCannonSelection(context)) {
            boolean activates = context.get("activatesDoubleCannons", Boolean.class);
            List<Integer> cannonCoord;

            if (activates) {
                HashMap<List<Integer>, List<Integer>> cannonAndBatteries = context.getUnsafe("cannonAndBatteries");
                
                Map.Entry<List<Integer>, List<Integer>> entry = cannonAndBatteries.entrySet().iterator().next();
                cannonCoord = entry.getKey();
                List<Integer> batteryCoord = entry.getValue();

                card.shootAtMeteorWith(cannonCoord, batteryCoord);
            } else {
                cannonCoord = context.getUnsafe("singleCannonCoord");

                card.shootAtMeteorWith(cannonCoord);
            }
        } else if (isShieldSelection(context)) {
            boolean activates = context.get("activatesShield", Boolean.class);

            if (activates) {
                HashMap<List<Integer>, List<Integer>> shieldAndBatteries = context.getUnsafe("shieldAndBatteries");

                card.activateShields(shieldAndBatteries);
            } else {
                card.activateNoShield();
            }
        }
    }

    @Override
    public void visit(CombatZone card, AdventureCardInputContext context) {
        if (isCannonSelection(context)) {
            Player player = context.get("player", Player.class);
            boolean activates = context.get("activatesDoubleCannons", Boolean.class);

            if (activates) {
                HashMap<List<Integer>, List<Integer>> cannonAndBatteries = context.getUnsafe("cannonAndBatteries");

                card.selectCannons(player, cannonAndBatteries);
            } else {
                card.selectNoCannons(player);
            }
        } else if (isEngineSelection(context)) {
            Player player = context.get("player", Player.class);
            boolean activates = context.get("activatesDoubleEngines", Boolean.class);

            if (activates) {
                HashMap<List<Integer>, List<Integer>> engineAndBatteries = context.getUnsafe("engineAndBatteries");

                card.selectEngines(player, engineAndBatteries);
            } else {
                card.selectNoEngines(player);
            }
        } else if (isCrewmatePenalty(context)) {
            int row = context.get("row", Integer.class);
            int column = context.get("column", Integer.class);

            card.applyCrewmatePenalty(row, column);
        }
    }

    private boolean isParticipation(AdventureCardInputContext context) {
        return context.has("player") && context.has("participates");
    }

    private boolean isCargoReward(AdventureCardInputContext context) {
        return context.has("loadIndex") && context.has("acceptsCargo");
    }

    private boolean isCreditReward(AdventureCardInputContext context) {
        return context.has("acceptsCredit");
    }

    private boolean isCrewmatePenalty(AdventureCardInputContext context) {
        return context.has("crewmateCoords");
    }

    private boolean isCannonSelection(AdventureCardInputContext context) {
        return context.has("player") && (context.has("activatesDoubleCannons") || context.has("activatesSingleCannon"));
    }

    private boolean isEngineSelection(AdventureCardInputContext context) {
        return context.has("player") && context.has("activatesDoubleEngines");
    }

    private boolean isShieldSelection(AdventureCardInputContext context) {
        return context.has("activatesShield");
    }
}
