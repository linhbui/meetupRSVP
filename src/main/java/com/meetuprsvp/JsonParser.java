package com.meetuprsvp;

import com.ning.http.client.HttpResponseBodyPart;

import javax.json.*;
import java.io.StringReader;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.StandardCharsets;

/**
 * Created by linhbui on 4/22/16.
 */
class JsonParser {
    private String lon;
    private String lat;
    private String city;

    public JsonParser(HttpResponseBodyPart content) {
        parse(content);

    }

    private void parse(HttpResponseBodyPart content) {
        ByteBuffer contentByte = content.getBodyByteBuffer();
        CharBuffer contentCharBuffer = StandardCharsets.UTF_8.decode(contentByte);
        String contentString = contentCharBuffer.toString();
        JsonObject object = Json.createReader(new StringReader(contentString)).readObject();
        try {
            lon = object.getJsonObject("venue").get("lon").toString();
        } catch (java.lang.NullPointerException e) {
            lon = "74.0059"; //NYC
        }

        try {
            lat = object.getJsonObject("venue").get("lat").toString();
        } catch (java.lang.NullPointerException e) {
            lat = "40.7128"; //NYC
        }

        try {
            city = object.getJsonObject("group").get("group_city").toString();
        } catch (java.lang.NullPointerException e) {
            city = "New York City";
        }

        // Once in a while Meetup will return (0,0), which screws up the map's red dot
        if (lat == "0" && lon == "0") {
            lon = "74.0059"; //NYC
            lat = "40.7128"; //NYC
        }
    }

    String getLon () {
        return lon;
    }

    String getLat () {
        return lat;
    }

    String getCity () {
        return city;
    }
}
