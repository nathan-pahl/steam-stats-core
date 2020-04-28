package org.nathan.pahl.steamstatscore.broker;

import com.lukaspradel.steamapi.webapi.client.SteamWebApiClient;

import org.nathan.pahl.steamstatscore.SteamIdRetriever;
import org.nathan.pahl.steamstatscore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.SweetCode.SteamAPI.SteamAPI;
import de.SweetCode.SteamAPI.SteamHTTPMethod;
import de.SweetCode.SteamAPI.SteamHost;
import de.SweetCode.SteamAPI.SteamVersion;
import de.SweetCode.SteamAPI.SteamVisibility;
import de.SweetCode.SteamAPI.interfaces.ISteamUser;
import de.SweetCode.SteamAPI.method.input.Input;
import de.SweetCode.SteamAPI.method.methods.ResolveVanityURL;

@Service
public class SteamUserBroker {

    private SteamAPI steamAPI;
    private SteamWebApiClient steamWebApiClient;

    @Autowired
    public SteamUserBroker(SteamAPI steamAPI, SteamWebApiClient steamWebApiClient, UserService userService) {
        this.steamAPI = steamAPI;
        this.steamWebApiClient = steamWebApiClient;
    }

    public Long getSteamIdFromUsername(String username) throws InterruptedException {
        ISteamUser steamUser = new ISteamUser(steamAPI);
        SteamHost host = SteamHost.PUBLIC;
        SteamVersion version = SteamVersion.V_1;
        SteamVisibility visibility = SteamVisibility.ALL;
        Input input = new Input.Builder().add("vanityurl", username).build();

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
    
}