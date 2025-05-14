package it.polimi.it.galaxytrucker.networking.client.clientmodel;

import java.util.UUID;

public class PlayerData {
    private String nickname;
    private final UUID playerId = UUID.randomUUID();
    private UUID matchId;

    public PlayerData() {
    }

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
}
