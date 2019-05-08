package org.nathan.pahl.steamstatscore;

import java.util.Optional;
import java.util.concurrent.CountDownLatch;

import com.google.gson.JsonObject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.SweetCode.SteamAPI.method.SteamResponse;
import okhttp3.Request;
import okhttp3.Response;

public class SteamRetriever {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private volatile CountDownLatch latch = new CountDownLatch(1);
    protected JsonObject jsonObject;

    public SteamRetriever() { }

    protected SteamResponse callback = new SteamResponse() {

        @Override
        public void onResponse(Request request, Response response, Optional<JsonObject> body) {
            Optional.of(body).get().ifPresent((responseObject) -> {
                jsonObject = responseObject.get("response").getAsJsonObject();
            });
            latch.countDown();
        }

    };

    public SteamResponse getCallback() {
        return this.callback;
    }

    public JsonObject retrieve() {
        try {
            latch.await();
            return this.jsonObject;
        } catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

}