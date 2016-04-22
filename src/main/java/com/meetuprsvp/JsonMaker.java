package com.meetuprsvp;

import javax.json.*;

public class JsonMaker {
    public static JsonObject dataToJson(String lon, String lat, String city) {
        JsonObject data = Json.createObjectBuilder()
                .add("lon", lon)
                .add("lat", lat)
                .add("city", city)
                .build();
        return data;
    }
}
