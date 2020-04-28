package org.nathan.pahl.steamstatscore.controller;

import com.lukaspradel.steamapi.core.exception.SteamApiException;
import com.lukaspradel.steamapi.data.json.ownedgames.Response;

import org.nathan.pahl.steamstatscore.broker.SteamGamesBroker;
import org.nathan.pahl.steamstatscore.broker.SteamUserBroker;
import org.nathan.pahl.steamstatscore.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class SteamStatsCoreController {

    private transient Logger logger = LoggerFactory.getLogger(this.getClass());

    private UserService userService;
    private SteamUserBroker steamUserBroker;
    private SteamGamesBroker steamGamesBroker;

    @Autowired
    public SteamStatsCoreController(UserService userService, SteamUserBroker steamUserBroker, SteamGamesBroker steamGamesBroker) {
        this.userService = userService;
        this.steamUserBroker = steamUserBroker;
        this.steamGamesBroker = steamGamesBroker;
    }

    @GetMapping("/getGames")
    public Response getGames(String input) throws SteamApiException, InterruptedException {
        Long steamId = userService.parseInputForLong(input);
        if (steamId == null) {
            String username = userService.parseInputForUsername(input);
            steamId = steamUserBroker.getSteamIdFromUsername(username);
        }
        return steamGamesBroker.getOwnedGames(steamId);
    }

}