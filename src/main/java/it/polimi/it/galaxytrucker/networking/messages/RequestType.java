package it.polimi.it.galaxytrucker.networking.messages;

public enum RequestType {
    // Default value
    EMPTY,
    NEW_TILE,
    SAVED_TILES,
    DISCARDED_TILES,
    CARD_PILE,
    PLACE_COMPONENT,
    DISCARD_COMPONENT,
    SAVE_COMPONENT,
    SHIP_BOARD,

    ACTIVATE_COMPONENT
}
