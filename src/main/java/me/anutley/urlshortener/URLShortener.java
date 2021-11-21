package me.anutley.urlshortener;

import io.javalin.Javalin;
import me.anutley.urlshortener.database.Database;
import me.anutley.urlshortener.endpoints.GetURLEndpoint;
import me.anutley.urlshortener.endpoints.api.GetURLInfoEndpoint;
import me.anutley.urlshortener.endpoints.api.PutURLEndpoint;
import me.anutley.urlshortener.util.Config;
import me.anutley.urlshortener.util.URLDeleteScheduler;

import java.io.IOException;

public class URLShortener {

    public static void main(String[] args) throws IOException {
        new Config();
        new Database();

        URLDeleteScheduler.run();
        setupJavalin();

    }

    public static void setupJavalin() {
        Javalin app = Javalin.create().start(Integer.parseInt(Config.getInstance().get("Port")));

        //User endpoints
        app.get("/", ctx -> ctx.redirect("https://github.com/ANutley/URLShortener"));
        app.get("/{code}/", new GetURLEndpoint());

        //API endpoints
        app.put("/api/", new PutURLEndpoint());
        app.get("/api/{code}", new GetURLInfoEndpoint());
    }

}
