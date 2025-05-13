package it.polimi.it.galaxytrucker.networking.client.clientmodel;

import java.util.UUID;

public class PlayerData {
    private String nickname;
    private UUID playerId;
    private UUID matchId;

    public PlayerData() {
    }

    public PlayerData(String nickname, UUID playerId, UUID matchId) {
        this.nickname = nickname;
        this.playerId = playerId;
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

    public void setPlayerId(UUID playerId) {
        this.playerId = playerId;
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
}
