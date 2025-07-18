package it.polimi.it.galaxytrucker.messages.clientmessages;

public enum UserInputType {
    HANDSHAKE,

    SET_PLAYER_USERNAME,
    CREATE_NEW_GAME,
    JOIN_ACTIVE_GAME,
    SEE_ACTIVE_GAMES,

    SELECT_RANDOM_COMPONENT,
    SELECT_SAVED_COMPONENT,
    SELECT_DISCARDED_COMPONENT,

    PLACE_COMPONENT,
    SAVE_SELECTED_COMPONENT,
    DISCARD_SELECTED_COMPONENT,
    PLACE_CREWMATE,

    SEE_CARDS_PILE,

    RESTART_BUILDING_TIMER,
    CONFIRM_BUILDING_END,
    CONFIRM_CREWMATES_END,

    REMOVE_COMPONENT,

    ACTIVATE_CANNON,
    ACTIVATE_ENGINE,
    ACTIVATE_SHIELD,
    CARGO_REWARD,
    CREDIT_REWARD,
    CREWMATE_PENALTY,
    PARTICIPATION,
}
