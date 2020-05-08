package org.nathan.pahl.steamstatscore.broker;

import com.lukaspradel.steamapi.core.exception.SteamApiException;
import com.lukaspradel.steamapi.data.json.friendslist.Friendslist;
import com.lukaspradel.steamapi.data.json.friendslist.GetFriendList;
import com.lukaspradel.steamapi.webapi.client.SteamWebApiClient;
import com.lukaspradel.steamapi.webapi.request.GetFriendListRequest;
import com.lukaspradel.steamapi.webapi.request.builders.SteamWebApiRequestFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class SteamFriendsBroker {

    private SteamWebApiClient steamWebApiClient;

    @Autowired
    public SteamFriendsBroker(SteamWebApiClient steamWebApiClient) {
        this.steamWebApiClient = steamWebApiClient;
    }
    
    @Cacheable(
        value = "friendsList",
        key = "#steamId"
    )
    public Friendslist getFriendList(Long steamId) throws SteamApiException {
        GetFriendListRequest request = SteamWebApiRequestFactory.createGetFriendListRequest(steamId + "");
        return ((GetFriendList)steamWebApiClient.processRequest(request)).getFriendslist();
    }

}