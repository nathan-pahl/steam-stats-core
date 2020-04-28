package org.nathan.pahl.steamstatscore.controller;

import java.util.ArrayList;
import java.util.List;

import com.lukaspradel.steamapi.core.exception.SteamApiException;
import com.lukaspradel.steamapi.data.json.friendslist.GetFriendList;
import com.lukaspradel.steamapi.data.json.ownedgames.GetOwnedGames;
import com.lukaspradel.steamapi.data.json.ownedgames.Response;
import com.lukaspradel.steamapi.webapi.client.SteamWebApiClient;
import com.lukaspradel.steamapi.webapi.request.GetFriendListRequest;
import com.lukaspradel.steamapi.webapi.request.GetOwnedGamesRequest;
import com.lukaspradel.steamapi.webapi.request.builders.SteamWebApiRequestFactory;

import org.nathan.pahl.steamstatscore.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import de.SweetCode.SteamAPI.SteamAPI;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class SteamStatsCoreController {

    private transient Logger logger = LoggerFactory.getLogger(this.getClass());

    private SteamAPI steamAPI;
    private SteamWebApiClient steamWebApiClient;
    private UserService userService;

    @Autowired
    public SteamStatsCoreController(SteamAPI steamAPI, SteamWebApiClient steamWebApiClient, UserService userService) {
        this.steamAPI = steamAPI;
        this.steamWebApiClient = steamWebApiClient;
        this.userService = userService;
    }

    @GetMapping("/getGames")
    public Response getGames(String input) throws SteamApiException, InterruptedException {
        Long steamId = userService.parseInputForLong(input);
        if (steamId == null) {
            String username = userService.parseInputForUsername(input);
        }
        return getOwnedGames(steamId);
    }

    private Response getOwnedGames(Long steamId) throws SteamApiException {
        boolean includeAppInfo = true;
        boolean includePlayedFreeGames = true;
        List<Integer> appIdsFilter = new ArrayList<>();
        GetOwnedGamesRequest request = SteamWebApiRequestFactory.createGetOwnedGamesRequest(steamId + "",
                includeAppInfo, includePlayedFreeGames, appIdsFilter);
        return ((GetOwnedGames) steamWebApiClient.processRequest(request)).getResponse();
    }

    private GetFriendList getFriendList(Long steamId) throws SteamApiException {
        GetFriendListRequest request = SteamWebApiRequestFactory.createGetFriendListRequest(steamId + "");
        return steamWebApiClient.processRequest(request);
    }

}