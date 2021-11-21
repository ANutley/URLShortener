package me.anutley.urlshortener.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class Config {

    public static Config instance;
    private static final Logger LOGGER = LoggerFactory.getLogger(Config.class);
    private final ObjectMapper mapper = new ObjectMapper();
    JsonNode config = mapper.readTree(new File("./config.json"));


    public Config() throws IOException {
        instance = this;
    }

    static {
        File file = new File("./config.json");
        if (!file.exists()) {
            LOGGER.error("A file named config.json must be placed in the working directory with the appropriately values! See https://github.com/ANutley/URLShortener/tree/master/src/main/resources/config.json for an example ");
        }
    }


    public static Config getInstance() {
        return instance;
    }

    public String get(String key) {
        return config.get(key).toString().replace("\"", "");
    }


}