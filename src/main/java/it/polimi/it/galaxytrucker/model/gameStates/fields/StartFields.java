package it.polimi.it.galaxytrucker.model.gameStates.fields;

import it.polimi.it.galaxytrucker.model.exceptions.InvalidActionException;
import it.polimi.it.galaxytrucker.model.managers.Model;;

public enum StartFields implements FieldRequirements {
    LEVEL {
        private final int MIN_LEVEL = 1;
        private final int MAX_LEVEL = 2;

        @Override
        public boolean isSet(Model model) throws InvalidActionException {
            Integer level = model.getLevel();

            if (isEmpty(level)) {
                return false;
            }

            isValid(level);
            return true;
        }

        @Override
        protected void isValid(Integer value) throws InvalidActionException {
            if (value < MIN_LEVEL || value > MAX_LEVEL) {
                throw new InvalidActionException("The selected level is not supported yet");
            }
        }
    },
    NUMBER_OF_PLAYERS {
        private final int MIN_PLAYERS = 1; // SHOULD BE SET TO 2! 1 IS FOR FASTER TESTING
        private final int MAX_PLAYERS = 4;

        @Override
        public boolean isSet(Model model) throws InvalidActionException {
            Integer numberOfPlayers = model.getNumberOfPlayers();

            if (isEmpty(numberOfPlayers)) {
                return false;
            }

            isValid(numberOfPlayers);
            return true;
        }

        @Override
        protected void isValid(Integer value) throws InvalidActionException {
            if (value < MIN_PLAYERS || value > MAX_PLAYERS) {
                throw new InvalidActionException("The selected number of players is not valid");
            }
        }
    };

    protected abstract void isValid(Integer value) throws InvalidActionException;

    private static boolean isEmpty(Integer value) {
        return value == null;
    }
}
