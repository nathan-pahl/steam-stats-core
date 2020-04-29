package org.nathan.pahl.steamstatscore.domain;

import com.lukaspradel.steamapi.data.json.friendslist.Friend;
import com.lukaspradel.steamapi.data.json.playersummaries.Player;

public class FriendSummary extends Friend {

    private Player player;

    public FriendSummary(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return this.player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }


}