package it.polimi.it.galaxytrucker.commands;

/**
 * Represents different types of requests a client can make
 * to a server.
 * <p>
 *     A <i>request</i> is interpreted as the client asking
 *     the server for <i>information</i>, rather than performing
 *     an action that might lead to changes in the game state.
 * </p>
 *
 * @author giacomoamaducci
 * @version 1.0
 */
@Deprecated
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
    SELECT_SAVED_TILE,
    SELECT_DISCARDED_TILE,


}
