package it.polimi.it.galaxytrucker.networking.client.clientmodel;

import it.polimi.it.galaxytrucker.model.utility.Color;

import java.util.UUID;

/**
 * This class represents a collection of unique identifiers associated with
 * a particular client connected to a server.
 *
 * @author giacomoamaducci
 * @version 1.0
 */
public class PlayerData {
    /**
     * The client's nickname.
     * Each client connected to the same server has a unique nickname.
     */
    private String nickname;

    /**
     * The client's unique identifier.
     */
    private final UUID playerId = UUID.randomUUID();
    /**
     * The unique identifier of the game the client is playing.
     */
    private UUID matchId;
    /**
     * Unique color given to this player
     */
    private Color color;

    public PlayerData() {}

    public PlayerData(String nickname, UUID matchId) {
        this.nickname = nickname;
        this.matchId = matchId;
    }

    public PlayerData(String nickname) {
        this.nickname = nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public void setMatchId(UUID matchId) {
        this.matchId = matchId;
    }

    public UUID getMatchId() {
        return matchId;
    }

    public void setColor(Color playerColor) {
        this.color = playerColor;
    }

    public Color getColor() {
        return color;
    }
}
