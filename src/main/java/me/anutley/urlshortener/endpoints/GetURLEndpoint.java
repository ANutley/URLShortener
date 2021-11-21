package me.anutley.urlshortener.endpoints;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import me.anutley.urlshortener.database.URL;
import org.jetbrains.annotations.NotNull;

public class GetURLEndpoint implements Handler {
    @Override
    public void handle(@NotNull Context ctx) {
        String code = ctx.pathParam("code");

        URL url = new URL(code);

        if (url.getCode() == null || url.getUrl() == null) {
            ctx.result("No URL redirect for this code");
        } else {
            url.setHits(url.getHits() + 1).save();
            ctx.redirect(url.getUrl());
        }

    }
}
