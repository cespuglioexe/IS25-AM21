package it.polimi.it.galaxytrucker.model.adventurecards.interfaces.attack;

import java.util.List;
import java.util.stream.IntStream;

import it.polimi.it.galaxytrucker.model.managers.ShipManager;
import it.polimi.it.galaxytrucker.model.utility.Dice;

public class AimDown implements AimingStrategy {
    @Override
    public List<Integer> aim(ShipManager ship) {
        int aimedRow;
        int aimedColumn;

        aimedColumn = calculateAimedColumn();
        aimedRow = calculateAimedRow(aimedColumn, ship);

        return List.of(aimedRow, aimedColumn);
    }
    private int calculateAimedColumn() {
        return Dice.roll() + Dice.roll();
    }
    private int calculateAimedRow(int column, ShipManager ship) {
        return IntStream.range(ShipManager.getStartRow(), ShipManager.getStartRow() + ShipManager.getRows())
            .filter(row -> isValidTargetCoord(row, column, ship))
            .findFirst()
            .orElse(ShipManager.getStartRow() + ShipManager.getRows() - 1);
    }
    private boolean isValidTargetCoord(int row, int column, ShipManager ship) {
        try {
            return ship.getComponent(row, column).isPresent() && !ship.isOutside(row, column);
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }
}
