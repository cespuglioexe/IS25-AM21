package it.polimi.it.galaxytrucker.commands.servercommands;

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
    DISCARDED_COMPONENTS_UPDATED
}
