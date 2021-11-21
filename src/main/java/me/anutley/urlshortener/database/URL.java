package me.anutley.urlshortener.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class URL {

    private String url;
    private String code;
    private boolean permanent;
    private long hits;
    private long timeCreated;

    public URL(String code) {

        try (final Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT * FROM urls WHERE code = ?")) {

            preparedStatement.setString(1, code);

            ResultSet result = preparedStatement.executeQuery();

            if (result.next())
                this
                        .setCode(result.getString("code"))
                        .setUrl(result.getString("url"))
                        .setPermanent(result.getBoolean("permanent"))
                        .setHits(result.getLong("hits"))
                        .setTimeCreated(result.getLong("time_created"));
            else
                this
                        .setCode(code);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public URL save() {

        try (final Connection connection = Database.getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("SELECT * FROM urls WHERE code = ?")) {

            preparedStatement.setString(1, this.getCode());

            ResultSet result = preparedStatement.executeQuery();

            if (!result.next()) {

                PreparedStatement newURL = connection
                        .prepareStatement("INSERT INTO urls (url, code, permanent, hits, time_created) VALUES (?, ?, ?, ?, ?)");

                newURL.setString(1, this.getUrl());
                newURL.setString(2, this.getCode());
                newURL.setBoolean(3, this.isPermanent());
                newURL.setLong(4, this.getHits());
                newURL.setLong(5, this.getTimeCreated());
                newURL.executeUpdate();
            } else {
                PreparedStatement editURL = connection
                        .prepareStatement("UPDATE urls SET url = ?, permanent = ?, hits = ?, time_created = ? where code = ?");

                editURL.setString(1, this.getUrl());
                editURL.setBoolean(2, this.isPermanent());
                editURL.setLong(3, this.getHits());
                editURL.setLong(4, this.getTimeCreated());
                editURL.setString(5, this.getCode());

                editURL.executeUpdate();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return this;
    }

    public String getUrl() {
        return url;
    }

    public String getCode() {
        return code;
    }

    public boolean isPermanent() {
        return permanent;
    }

    public long getHits() {
        return hits;
    }

    public long getTimeCreated() {
        return timeCreated;
    }

    public URL setUrl(String url) {
        this.url = url;
        return this;
    }

    public URL setCode(String code) {
        this.code = code;
        return this;
    }

    public URL setPermanent(boolean permanent) {
        this.permanent = permanent;
        return this;
    }

    public URL setHits(long hits) {
        this.hits = hits;
        return this;
    }

    public URL setTimeCreated(long timeCreated) {
        this.timeCreated = timeCreated;
        return this;
    }


    public static void removeURLById(String code) {
        try (final Connection connection = Database
                .getConnection();
             PreparedStatement preparedStatement = connection
                     .prepareStatement("DELETE from urls where code = ?")) {

            preparedStatement.setString(1, code);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
