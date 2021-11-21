package me.anutley.urlshortener.endpoints.api;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import me.anutley.urlshortener.database.Database;
import me.anutley.urlshortener.database.URL;
import org.jetbrains.annotations.NotNull;

public class GetURLInfoEndpoint implements Handler {
    @Override
    public void handle(@NotNull Context ctx) {
        String code = ctx.pathParam("code");

        URL url = new URL(code);

        if (url.getCode() == null) {
            ctx.result("No redirect can be found with this code");
        } else {
            ctx.json(Database.getMapper().createObjectNode()
                    .put("url", url.getUrl())
                    .put("code", url.getCode())
                    .put("permanent", url.isPermanent())
                    .put("hits", url.getHits())
                    .put("time_created", url.getTimeCreated()).toPrettyString()
            );
        }
    }
}
