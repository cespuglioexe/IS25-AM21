package it.polimi.it.galaxytrucker.messages.servermessages;

public enum GameUpdateType {
    NEW_STATE,
    DRAWN_TILE,
    PLACED_TILE,
    DISPLAY_CARD_PILE,
    SHIP_DATA,
    TILE_LIST,
    TIMER_END,
    TIMER_START,

    PLAYER_SHIP_UPDATED,
    SAVED_COMPONENTS_UPDATED,
    DISCARDED_COMPONENTS_UPDATED,

    // Used for server-sent updates
    SET_USERNAME_RESULT,
    CREATE_GAME_RESULT,
    JOIN_GAME_RESULT,
    LIST_ACTIVE_CONTROLLERS,

    INPUT,
    INVALID_INPUT,
    CARD_DETAILS,
    PLAYER_MARKER_MOVED,
}
