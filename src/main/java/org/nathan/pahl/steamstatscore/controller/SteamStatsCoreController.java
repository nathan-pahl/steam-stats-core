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

import org.nathan.pahl.steamstatscore.SteamIdRetriever;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import de.SweetCode.SteamAPI.SteamAPI;
import de.SweetCode.SteamAPI.SteamHTTPMethod;
import de.SweetCode.SteamAPI.SteamHost;
import de.SweetCode.SteamAPI.SteamVersion;
import de.SweetCode.SteamAPI.SteamVisibility;
import de.SweetCode.SteamAPI.interfaces.ISteamUser;
import de.SweetCode.SteamAPI.method.input.Input;
import de.SweetCode.SteamAPI.method.methods.ResolveVanityURL;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class SteamStatsCoreController {

    private transient Logger logger = LoggerFactory.getLogger(this.getClass());

    SteamAPI steamAPI;
    private SteamWebApiClient steamWebApiClient;

    @Autowired
    public SteamStatsCoreController(SteamAPI steamAPI, SteamWebApiClient steamWebApiClient) {
        this.steamAPI = steamAPI;
        this.steamWebApiClient = steamWebApiClient;
    }

    @GetMapping("/getGames")
    public Response getGames(String input) throws SteamApiException, InterruptedException {
        Long steamId = null;

        try {
            steamId = Long.parseLong(input);
        } catch (NumberFormatException e) {
            this.logger.info("User entered string");
        }

        if (steamId == null) {
            steamId = getSteamId(input);
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

    private Long getSteamId(String vanity) throws InterruptedException {
        ISteamUser steamUser = new ISteamUser(steamAPI);
        SteamHost host = SteamHost.PUBLIC;
        SteamVersion version = SteamVersion.V_1;
        SteamVisibility visibility = SteamVisibility.ALL;
        Input input = new Input.Builder().add("vanityurl", vanity).build();

        SteamIdRetriever retreiver = new SteamIdRetriever();

        boolean async = true;
        steamUser.get(ResolveVanityURL.class).execute(SteamHTTPMethod.GET, host, version, visibility, input,
                retreiver.getCallback(), async);

        Long steamId = retreiver.getSteamId();
        if (steamId == null) {
            throw new RuntimeException("Steam user does not exist");
        }
        return steamId;
    }

    private GetFriendList getFriendList(Long steamId) throws SteamApiException {
        GetFriendListRequest request = SteamWebApiRequestFactory.createGetFriendListRequest(steamId + "");
        return steamWebApiClient.processRequest(request);
    }

}