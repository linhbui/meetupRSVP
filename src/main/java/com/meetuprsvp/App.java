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

    public void getMeetupData () {
        AsyncHttpClient c = new AsyncHttpClient(new AsyncHttpClientConfig.Builder().setRequestTimeout(3600000).build());

        c.prepareGet("https://stream.meetup.com/2/rsvps").execute(new AsyncCompletionHandler<com.ning.http.client.Response>()
        {
            @Override
            public STATE onBodyPartReceived (HttpResponseBodyPart content){
                ByteBuffer contentByte = content.getBodyByteBuffer();
                CharBuffer contentCharBuffer = StandardCharsets.UTF_8.decode(contentByte);
                String contentString = contentCharBuffer.toString();
                JsonObject object = Json.createReader(new StringReader(contentString)).readObject();
                String lon, lat, city;
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

                System.out.println(city);

                JsonObject data = JsonMaker.dataToJson(lon, lat, city);
                pusher.trigger("client-data", "rsvp", data.toString());

                return STATE.CONTINUE;
            }

            @Override
            public void onThrowable(Throwable t) {
                System.out.println("Inside Throwable!!!");
                System.out.println(t.getMessage());
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
