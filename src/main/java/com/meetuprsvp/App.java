package com.meetuprsvp;

import static spark.Spark.get;
import static spark.SparkBase.staticFileLocation;

import com.ning.http.client.*;
import com.pusher.rest.Pusher;

import java.nio.*;
import java.nio.charset.*;
import java.io.StringReader;
import javax.json.*;

public class App {
    private Pusher pusher;

    public JsonObject dataToJson(String lon, String lat) {
        JsonObject data = Json.createObjectBuilder()
                .add("lon", lon)
                .add("lat", lat)
                .build();
        return data;
    }

    public void getMeetupData () {
        AsyncHttpClient c = new AsyncHttpClient();

        c.prepareGet("https://stream.meetup.com/2/rsvps").execute(new AsyncCompletionHandler<com.ning.http.client.Response>()
        {
            @Override
            public STATE onBodyPartReceived (HttpResponseBodyPart content){
                ByteBuffer contentByte = content.getBodyByteBuffer();
                CharBuffer contentCharBuffer = StandardCharsets.UTF_8.decode(contentByte);
                String contentString = contentCharBuffer.toString();
                JsonObject object = Json.createReader(new StringReader(contentString)).readObject();
                String lon, lat;
                try {
                    lon = object.getJsonObject("venue").get("lon").toString();
                } catch (java.lang.NullPointerException e) {
                    lon = "0";
                }

                try {
                    lat = object.getJsonObject("venue").get("lat").toString();
                } catch (java.lang.NullPointerException e) {
                    lat = "0";
                }

                JsonObject data = dataToJson(lon, lat);
                pusher.trigger("client-data", "rsvp", data.toString());

                return STATE.CONTINUE;
            }

            @Override
            public com.ning.http.client.Response onCompleted(com.ning.http.client.Response response) throws Exception {
                return response;
            }
        });
    }

    public void setPusher () {
        pusher = new Pusher(Secret.APP_ID, Secret.KEY , Secret.SECRET);
        pusher.setEncrypted(true);
    }

    public static void main( String[] args) {
        staticFileLocation("public");
        App app = new App();
        app.setPusher();
        app.getMeetupData();
        get("/hello", (req, res) -> "Hey!!!");
    }
}
