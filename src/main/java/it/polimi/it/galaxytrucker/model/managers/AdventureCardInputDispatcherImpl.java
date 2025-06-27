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
        System.out.println(ConsoleColors.MAGENTA + "Dispatched card:" + card.getClass().getSimpleName() + ConsoleColors.RESET);
        card.accept(this, context);
    }

    @Override
    public void visit(Planets card, AdventureCardInputContext context) {
        if (isParticipation(context)) {
            Player player = context.get(AdventureCardInputFields.PLAYER, Player.class);
            boolean participates = context.get(AdventureCardInputFields.PARTICIPATES, Boolean.class);

            if (participates) {
                int choice = context.get(AdventureCardInputFields.CHOICE, Integer.class);

                card.participate(player, choice);
            } else {
                card.decline(player);
            }
        } else if (isCargoReward(context)) {
            //int loadIndex = context.get(AdventureCardInputFields.LOAD_INDEX, Integer.class);
            boolean accepts = context.get(AdventureCardInputFields.ACCEPTS_CARGO, Boolean.class);

            if (accepts) {
                int row = context.get(AdventureCardInputFields.ROW, Integer.class);
                int column = context.get(AdventureCardInputFields.COLUMN, Integer.class);
                System.out.println("Accepts cargo (" + row + ", " + column + ")");

                card.acceptCargo(0, row, column);
            } else {
                System.out.println("Declines cargo");
                card.discardCargo(0);
            }
        }
    }

    @Override
    public void visit(AbandonedShip card, AdventureCardInputContext context) {
        if (isParticipation(context)) {
            Player player = context.get(AdventureCardInputFields.PLAYER, Player.class);
            boolean participates = context.get(AdventureCardInputFields.PARTICIPATES, Boolean.class);

            if (participates) {
                card.participate(player, 0);
            } else {
                card.decline(player);
            }
        } else if (isCrewmatePenalty(context)) {
            int row = context.get(AdventureCardInputFields.ROW, Integer.class);
            int column = context.get(AdventureCardInputFields.COLUMN, Integer.class);

            card.applyCrewmatePenalty(row, column);
        }
    }

    @Override
    public void visit(AbandonedStation card, AdventureCardInputContext context) {
        System.out.println("Participation: " + isParticipation(context));
        if (isParticipation(context)) {
            Player player = context.get(AdventureCardInputFields.PLAYER, Player.class);
            boolean participates = context.get(AdventureCardInputFields.PARTICIPATES, Boolean.class);

            if (participates) {
                card.participate(player, 0);
            } else {
                card.decline(player);
            }
        } else if (isCargoReward(context)) {
            //int loadIndex = context.get(AdventureCardInputFields.LOAD_INDEX, Integer.class);
            boolean accepts = context.get(AdventureCardInputFields.ACCEPTS_CARGO, Boolean.class);

            if (accepts) {
                int row = context.get(AdventureCardInputFields.ROW, Integer.class);
                int column = context.get(AdventureCardInputFields.COLUMN, Integer.class);

                card.acceptCargo(0, row, column);
            } else {
                card.discardCargo(0);
            }
        }
    }

    @Override
    public void visit(OpenSpace card, AdventureCardInputContext context) {
        System.out.println("DISPATCH CONTEXT: " + context);
        if (isEngineSelection(context)) {
            Player player = context.get(AdventureCardInputFields.PLAYER, Player.class);
            boolean activates = context.get(AdventureCardInputFields.ACTIVATES_DOUBLE_ENGINES, Boolean.class);

            if (activates) {
                HashMap<List<Integer>, List<Integer>> engineAndBatteries = context.getUnsafe(AdventureCardInputFields.DOUBLE_ENGINES_AND_BATTERIES);

                card.selectEngine(player, engineAndBatteries);
            } else {
                card.selectNoEngine(player);
            }
        }
    }

    @Override
    public void visit(Pirates card, AdventureCardInputContext context) {
        if (isCannonSelection(context)) {
            boolean activates = context.get(AdventureCardInputFields.ACTIVATES_DOUBLE_CANNONS, Boolean.class);

            if (activates) {
                HashMap<List<Integer>, List<Integer>> cannonAndBatteries = context.getUnsafe(AdventureCardInputFields.DOUBLE_CANNONS_AND_BATTERIES);

                card.selectCannons(cannonAndBatteries);
            } else {
                card.selectNoCannons();
            }
        } else if (isCreditReward(context)) {
            boolean accepts = context.get(AdventureCardInputFields.ACCEPTS_CREDIT, Boolean.class);

            if (accepts) {
                card.applyCreditReward();
            } else {
                card.discardCreditReward();
            }
        } else if (isShieldSelection(context)) {
            boolean activates = context.get(AdventureCardInputFields.ACTIVATES_SHIELD, Boolean.class);

            if (activates) {
                HashMap<List<Integer>, List<Integer>> shieldAndBatteries = context.getUnsafe(AdventureCardInputFields.SHIELD_AND_BATTERIES);

                card.activateShields(shieldAndBatteries);
            } else {
                card.activateNoShield();
            }
        }
    }

    @Override
    public void visit(Slavers card, AdventureCardInputContext context) {
        if (isCannonSelection(context)) {
            boolean activates = context.get(AdventureCardInputFields.ACTIVATES_DOUBLE_CANNONS, Boolean.class);

            if (activates) {
                HashMap<List<Integer>, List<Integer>> cannonAndBatteries = context.getUnsafe(AdventureCardInputFields.DOUBLE_CANNONS_AND_BATTERIES);

                card.selectCannons(cannonAndBatteries);
            } else {
                card.selectNoCannons();
            }
        } else if (isCreditReward(context)) {
            boolean accepts = context.get(AdventureCardInputFields.ACCEPTS_CREDIT, Boolean.class);

            if (accepts) {
                card.applyCreditReward();
            } else {
                card.discardCreditReward();
            }
        } else if (isCrewmatePenalty(context)) {
            List<List<Integer>> crewmates = context.getUnsafe(AdventureCardInputFields.CREWMATES);

            card.sellSlaves(crewmates);
        }
    }

    @Override
    public void visit(Smugglers card, AdventureCardInputContext context) {
        if (isCannonSelection(context)) {
            boolean activates = context.get(AdventureCardInputFields.ACTIVATES_DOUBLE_CANNONS, Boolean.class);

            if (activates) {
                HashMap<List<Integer>, List<Integer>> cannonAndBatteries = context.getUnsafe(AdventureCardInputFields.DOUBLE_CANNONS_AND_BATTERIES);

                card.selectCannons(cannonAndBatteries);
            } else {
                card.selectNoCannons();
            }
        } else if (isCargoReward(context)) {
            boolean accepts = context.get(AdventureCardInputFields.ACCEPTS_CARGO, Boolean.class);

            if (accepts) {
                int row = context.get(AdventureCardInputFields.ROW, Integer.class);
                int column = context.get(AdventureCardInputFields.COLUMN, Integer.class);

                card.acceptCargo(0, row, column);
            } else {
                card.discardCargo(0);
            }
        }
    }

    @Override
    public void visit(MeteorSwarm card, AdventureCardInputContext context) {
        if (isCannonSelection(context)) {
            boolean activatesDoubleCannon = context.get(AdventureCardInputFields.ACTIVATES_DOUBLE_CANNONS, Boolean.class);
            boolean activatesSingleCannon = context.get(AdventureCardInputFields.ACTIVATES_SINGLE_CANNON, Boolean.class);
            List<Integer> cannonCoord;

            if (activatesDoubleCannon) {
                HashMap<List<Integer>, List<Integer>> cannonAndBatteries = context.getUnsafe(AdventureCardInputFields.DOUBLE_CANNONS_AND_BATTERIES);
                
                Map.Entry<List<Integer>, List<Integer>> entry = cannonAndBatteries.entrySet().iterator().next();
                cannonCoord = entry.getKey();
                List<Integer> batteryCoord = entry.getValue();

                card.shootAtMeteorWith(cannonCoord, batteryCoord);
            } else if (activatesSingleCannon) {
                cannonCoord = context.getUnsafe(AdventureCardInputFields.ACTIVATES_SINGLE_CANNON);

                card.shootAtMeteorWith(cannonCoord);
            } else {
                card.notShootAtMeteor();
            }
        } else if (isShieldSelection(context)) {
            boolean activates = context.get(AdventureCardInputFields.ACTIVATES_SHIELD, Boolean.class);

            if (activates) {
                HashMap<List<Integer>, List<Integer>> shieldAndBatteries = context.getUnsafe(AdventureCardInputFields.SHIELD_AND_BATTERIES);

                card.activateShields(shieldAndBatteries);
            } else {
                card.activateNoShield();
            }
        }
    }

    @Override
    public void visit(BigMeteorSwarm card, AdventureCardInputContext context) {
        if (isCannonSelection(context)) {
            boolean activatesDoubleCannon = context.get(AdventureCardInputFields.ACTIVATES_DOUBLE_CANNONS, Boolean.class);
            boolean activatesSingleCannon = context.get(AdventureCardInputFields.ACTIVATES_SINGLE_CANNON, Boolean.class);
            List<Integer> cannonCoord;

            if (activatesDoubleCannon) {
                HashMap<List<Integer>, List<Integer>> cannonAndBatteries = context.getUnsafe(AdventureCardInputFields.DOUBLE_CANNONS_AND_BATTERIES);
                
                Map.Entry<List<Integer>, List<Integer>> entry = cannonAndBatteries.entrySet().iterator().next();
                cannonCoord = entry.getKey();
                List<Integer> batteryCoord = entry.getValue();

                card.shootAtMeteorWith(cannonCoord, batteryCoord);
            } else if (activatesSingleCannon) {
                cannonCoord = context.getUnsafe(AdventureCardInputFields.ACTIVATES_SINGLE_CANNON);

                card.shootAtMeteorWith(cannonCoord);
            } else {
                card.notShootAtMeteor();
            }
        } else if (isShieldSelection(context)) {
            boolean activates = context.get(AdventureCardInputFields.ACTIVATES_SHIELD, Boolean.class);

            if (activates) {
                HashMap<List<Integer>, List<Integer>> shieldAndBatteries = context.getUnsafe(AdventureCardInputFields.SHIELD_AND_BATTERIES);

                card.activateShields(shieldAndBatteries);
            } else {
                card.activateNoShield();
            }
        }
    }

    @Override
    public void visit(CombatZone card, AdventureCardInputContext context) {
        if (isCannonSelection(context)) {
            Player player = context.get(AdventureCardInputFields.PLAYER, Player.class);
            boolean activates = context.get(AdventureCardInputFields.ACTIVATES_DOUBLE_CANNONS, Boolean.class);

            if (activates) {
                HashMap<List<Integer>, List<Integer>> cannonAndBatteries = context.getUnsafe(AdventureCardInputFields.DOUBLE_CANNONS_AND_BATTERIES);

                card.selectCannons(player, cannonAndBatteries);
            } else {
                card.selectNoCannons(player);
            }
        } else if (isEngineSelection(context)) {
            Player player = context.get(AdventureCardInputFields.PLAYER, Player.class);
            boolean activates = context.get(AdventureCardInputFields.ACTIVATES_DOUBLE_ENGINES, Boolean.class);

            if (activates) {
                HashMap<List<Integer>, List<Integer>> engineAndBatteries = context.getUnsafe(AdventureCardInputFields.DOUBLE_ENGINES_AND_BATTERIES);

                card.selectEngines(player, engineAndBatteries);
            } else {
                card.selectNoEngines(player);
            }
        } else if (isCrewmatePenalty(context)) {
            int row = context.get(AdventureCardInputFields.ROW, Integer.class);
            int column = context.get(AdventureCardInputFields.COLUMN, Integer.class);

            card.applyCrewmatePenalty(row, column);
        } else if (isShieldSelection(context)) {
            boolean activates = context.get(AdventureCardInputFields.ACTIVATES_SHIELD, Boolean.class);

            if (activates) {
                HashMap<List<Integer>, List<Integer>> shieldAndBatteries = context.getUnsafe(AdventureCardInputFields.SHIELD_AND_BATTERIES);

                card.activateShields(shieldAndBatteries);
            } else {
                card.activateNoShield();
            }
        }
    }

    private boolean isParticipation(AdventureCardInputContext context) {
        return context.has(AdventureCardInputFields.PLAYER) && context.has(AdventureCardInputFields.PARTICIPATES);
    }

    private boolean isCargoReward(AdventureCardInputContext context) {
        return context.has(AdventureCardInputFields.ACCEPTS_CARGO);
    }

    private boolean isCreditReward(AdventureCardInputContext context) {
        return context.has(AdventureCardInputFields.ACCEPTS_CREDIT);
    }

    private boolean isCrewmatePenalty(AdventureCardInputContext context) {
        return context.has(AdventureCardInputFields.CREWMATE_PENALTY);
    }

    private boolean isCannonSelection(AdventureCardInputContext context) {
        return context.has(AdventureCardInputFields.PLAYER) && (
            context.has(AdventureCardInputFields.ACTIVATES_DOUBLE_CANNONS) || 
            context.has(AdventureCardInputFields.ACTIVATES_SINGLE_CANNON)
        );
    }

    private boolean isEngineSelection(AdventureCardInputContext context) {
        return context.has(AdventureCardInputFields.PLAYER) && context.has(AdventureCardInputFields.ACTIVATES_DOUBLE_ENGINES);
    }

    private boolean isShieldSelection(AdventureCardInputContext context) {
        return context.has(AdventureCardInputFields.ACTIVATES_SHIELD);
    }
}
