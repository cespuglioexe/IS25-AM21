package it.polimi.it.galaxytrucker.networking.messages;

public enum RequestType {
    // Default value
    EMPTY,
    
    NEW_TILE,
    CARD_PILE,
    PLACE_COMPONENT,
    DISCARD_COMPONENT,
    SAVE_COMPONENT,

    ACTIVATE_COMPONENT
}
