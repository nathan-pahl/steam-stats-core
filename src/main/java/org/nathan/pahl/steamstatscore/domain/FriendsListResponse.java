package org.nathan.pahl.steamstatscore.domain;

import java.util.List;

public class FriendsListResponse {

    int count;
    List<FriendSummary> friends;

    public FriendsListResponse(List<FriendSummary> friends) {
        this.friends = friends;
        this.count = friends.size();
    }

    public int getCount() {
        return this.count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<FriendSummary> getFriends() {
        return this.friends;
    }

    public void setFriends(List<FriendSummary> friends) {
        this.friends = friends;
    }
    

}