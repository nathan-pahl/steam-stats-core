package org.nathan.pahl.steamstatscore.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.lukaspradel.steamapi.core.exception.SteamApiException;
import com.lukaspradel.steamapi.data.json.friendslist.Friend;
import com.lukaspradel.steamapi.data.json.friendslist.Friendslist;
import com.lukaspradel.steamapi.data.json.ownedgames.Response;
import com.lukaspradel.steamapi.data.json.playersummaries.Player;

import org.nathan.pahl.steamstatscore.broker.SteamFriendsBroker;
import org.nathan.pahl.steamstatscore.broker.SteamGamesBroker;
import org.nathan.pahl.steamstatscore.broker.SteamUserBroker;
import org.nathan.pahl.steamstatscore.domain.FriendSummary;
import org.nathan.pahl.steamstatscore.domain.FriendsListResponse;
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

    private transient final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserService userService;
    private final SteamUserBroker steamUserBroker;
    private final SteamGamesBroker steamGamesBroker;
    private final SteamFriendsBroker steamFriendsBroker;

    @Autowired
    public SteamStatsCoreController(final UserService userService, final SteamUserBroker steamUserBroker,
            final SteamGamesBroker steamGamesBroker, final SteamFriendsBroker steamFriendsBroker) {
        this.userService = userService;
        this.steamUserBroker = steamUserBroker;
        this.steamGamesBroker = steamGamesBroker;
        this.steamFriendsBroker = steamFriendsBroker;
    }

    @GetMapping("/getPlayerSummary")
    public FriendsListResponse getPlayerSummary(final String input) throws SteamApiException, InterruptedException {
        Long steamId = userService.parseInputForLong(input);
        if (steamId == null) {
            final String username = userService.parseInputForUsername(input);
            steamId = steamUserBroker.getSteamIdFromUsername(username);
        }
        List<String> steamIds = Arrays.asList(new String[]{steamId + ""});
        com.lukaspradel.steamapi.data.json.playersummaries.Response response = this.steamUserBroker
                .getPlayerSummaries(steamIds);
        Map<String, Player> players = response.getPlayers().stream().collect(Collectors.toMap(Player::getSteamid, Function.identity()));
        List<FriendSummary> friendSummaries = new ArrayList<>();
        steamIds.stream().forEach(id -> {
            friendSummaries.add(new FriendSummary(players.get(id), null));
        });     
        return new FriendsListResponse(friendSummaries);
    }

    @GetMapping("/getGames")
    public Response getGames(final String input) throws SteamApiException, InterruptedException {
        Long steamId = userService.parseInputForLong(input);
        if (steamId == null) {
            final String username = userService.parseInputForUsername(input);
            steamId = steamUserBroker.getSteamIdFromUsername(username);
        }
        return steamGamesBroker.getOwnedGames(steamId);
    }

    @GetMapping("/getFriends")
    public FriendsListResponse getFriends(final String input) throws SteamApiException, InterruptedException {
        Long steamId = userService.parseInputForLong(input);
        if (steamId == null) {
            final String username = userService.parseInputForUsername(input);
            steamId = steamUserBroker.getSteamIdFromUsername(username);
        }
        Friendslist friendsList = steamFriendsBroker.getFriendList(steamId);
        Map<String, Friend> friends = friendsList.getFriends().stream()
                .collect(Collectors.toMap(Friend::getSteamid, Function.identity()));
        List<String> steamIds = friends.keySet().stream().collect(Collectors.toList());
        com.lukaspradel.steamapi.data.json.playersummaries.Response response = this.steamUserBroker
                .getPlayerSummaries(steamIds);
        Map<String, Player> players = response.getPlayers().stream().collect(Collectors.toMap(Player::getSteamid, Function.identity()));
        List<FriendSummary> friendSummaries = new ArrayList<>();
        steamIds.stream().forEach(id -> {
            friendSummaries.add(new FriendSummary(players.get(id), friends.get(id)));
        });     
        return new FriendsListResponse(friendSummaries);
    }

}