package org.nathan.pahl.steamstatscore;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;

import com.google.gson.JsonObject;

import de.SweetCode.SteamAPI.method.SteamResponse;
import okhttp3.Request;
import okhttp3.Response;

public class SteamIdRetriever extends SteamRetriever{

    private volatile CountDownLatch latch = new CountDownLatch(1);
    private Long steamId;

    public SteamIdRetriever() { }

    private SteamResponse callback = new SteamResponse() {

        @Override
        public void onResponse(Request request, Response response, Optional<JsonObject> body) {
            Optional.of(body)
                .get()
                .ifPresent((obj) -> {
                    if(obj.get("response") != null && obj.get("response").getAsJsonObject().get("steamid") != null) {
                        steamId = obj.get("response").getAsJsonObject().get("steamid").getAsLong();
                    }
                });
            latch.countDown();
        }

    };

    public SteamResponse getCallback() {
        return this.callback;
    }

    public Long getSteamId() throws InterruptedException {
        latch.await();
        return this.steamId;
    }

}