package me.anutley.urlshortener.endpoints.api;

import com.fasterxml.jackson.databind.JsonNode;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import me.anutley.urlshortener.database.Database;
import me.anutley.urlshortener.database.URL;
import me.anutley.urlshortener.util.Config;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public class PutURLEndpoint implements Handler {
    @Override
    public void handle(@NotNull Context ctx) throws Exception {

        if (ctx.header("secret") == null) return;

        if (!ctx.header("secret").equals(Config.getInstance().get("Secret"))) {
            ctx.status(401);
            ctx.result("Unauthorised");
            return;
        }

        JsonNode json = Database.getMapper().readTree(ctx.body());

        String url = json.get("url").asText();
        String code = json.get("code").asText();
        String perm = json.get("permanent").asText();
        boolean permanent = false;

        if (StringUtils.isEmpty(url)) {
            ctx.result("You need to specify a url you want to shorten");
            return;
        }

        if (!url.startsWith("https://") && !url.startsWith("http://")) {
            url = "https://" + url;
        }

        String finalCode = code;

        int urlCodeSize = Integer.parseInt(Config.getInstance().get("URLCodeSize"));
        if (urlCodeSize < 6) urlCodeSize = 6;

        if (code.toLowerCase(Locale.ROOT).equals("r")
                || code.toLowerCase(Locale.ROOT).equals("random")
                || Database.getAllURLs().stream().anyMatch(u -> u.getCode().equals(finalCode))
                || StringUtils.isEmpty(code)) {
            code = RandomStringUtils.randomAlphabetic(urlCodeSize);
        }

        if (perm.toLowerCase(Locale.ROOT).equals("t")
                || perm.toLowerCase(Locale.ROOT).equals("true"))
            permanent = true;


        new URL(code)
                .setPermanent(permanent)
                .setUrl(url)
                .setTimeCreated(System.currentTimeMillis())
                .save();

        ctx.result(Config.getInstance().get("Domain") + code);
    }
}
