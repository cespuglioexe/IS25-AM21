package it.polimi.it.galaxytrucker.model.adventurecards.interfaces.attack;

import java.util.List;
import java.util.stream.IntStream;

import it.polimi.it.galaxytrucker.model.managers.ShipManager;
import it.polimi.it.galaxytrucker.model.utility.Dice;

public class AimRight implements AimingStrategy {
    @Override
    public List<Integer> aim(ShipManager ship) {
        int aimedRow;
        int aimedColumn;

        aimedRow = calculateAimedRow();
        aimedColumn = calculateAimedColumn(aimedRow, ship);

        return List.of(aimedRow, aimedColumn);
    }
    private int calculateAimedRow() {
        return Dice.roll() + Dice.roll();
    }
    private int calculateAimedColumn(int row, ShipManager ship) {
        return IntStream.range(ShipManager.getStartColumn(), ShipManager.getStartColumn() + ShipManager.getColumns())
            .filter(column -> isValidTargetCoord(row, column, ship))
            .findFirst()
            .orElse(ShipManager.getStartColumn() + ShipManager.getColumns() - 1);
    }
    private boolean isValidTargetCoord(int row, int column, ShipManager ship) {
        try {
            return ship.getComponent(row, column).isPresent() && !ship.isOutside(row, column);
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }
}
