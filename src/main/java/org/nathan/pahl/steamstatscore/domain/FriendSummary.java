package org.nathan.pahl.steamstatscore.domain;

import com.lukaspradel.steamapi.data.json.friendslist.Friend;
import com.lukaspradel.steamapi.data.json.playersummaries.Player;

public class FriendSummary {

    private Player player;
    private Friend friend;

    public FriendSummary(Player player, Friend friend) {
        this.player = player;
        this.friend = friend;
    }

    public Player getPlayer() {
        return this.player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Friend getFriend() {
        return this.friend;
    }

    public void setFriend(Friend friend) {
        this.friend = friend;
    }

}