package org.nathan.pahl.steamstatscore.broker;

import java.util.ArrayList;
import java.util.List;

import com.lukaspradel.steamapi.core.exception.SteamApiException;
import com.lukaspradel.steamapi.data.json.ownedgames.GetOwnedGames;
import com.lukaspradel.steamapi.data.json.ownedgames.Response;
import com.lukaspradel.steamapi.webapi.client.SteamWebApiClient;
import com.lukaspradel.steamapi.webapi.request.GetOwnedGamesRequest;
import com.lukaspradel.steamapi.webapi.request.builders.SteamWebApiRequestFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SteamGamesBroker {

    private SteamWebApiClient steamWebApiClient;

    @Autowired
    public SteamGamesBroker(SteamWebApiClient steamWebApiClient) {
        this.steamWebApiClient = steamWebApiClient;
    }

    public Response getOwnedGames(Long steamId) throws SteamApiException {
        boolean includeAppInfo = true;
        boolean includePlayedFreeGames = true;
        List<Integer> appIdsFilter = new ArrayList<>();
        GetOwnedGamesRequest request = SteamWebApiRequestFactory.createGetOwnedGamesRequest(steamId + "",
                includeAppInfo, includePlayedFreeGames, appIdsFilter);
        return ((GetOwnedGames) steamWebApiClient.processRequest(request)).getResponse();
    }

}