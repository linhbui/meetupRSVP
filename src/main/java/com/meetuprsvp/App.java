package com.meetuprsvp;

import static spark.Spark.get;
import static spark.SparkBase.staticFileLocation;

import com.ning.http.client.*;
import com.pusher.client.Pusher;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.SubscriptionEventListener;
import com.pusher.client.connection.ConnectionEventListener;
import com.pusher.client.connection.ConnectionState;
import com.pusher.client.connection.ConnectionStateChange;

import java.nio.*;
import java.nio.charset.*;
import java.io.StringReader;
import javax.json.*;

public class App {
    public static JsonObject dataToJson(String lon, String lat) {
        JsonObject data = Json.createObjectBuilder()
                .add("lon", lon)
                .add("lat", lat)
                .build();
        return data;
    }

    public static void getMeetupData () {
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
                return STATE.CONTINUE;
            }

            @Override
            public com.ning.http.client.Response onCompleted(com.ning.http.client.Response response) throws Exception {
                return response;
            }
        });
    }

    public static void main( String[] args) {
        staticFileLocation("public");
        // Create a new Pusher instance
        Pusher pusher = new Pusher("d666fb92d6623055b4a1");

        pusher.connect(new ConnectionEventListener() {
            @Override
            public void onConnectionStateChange(ConnectionStateChange change) {
                System.out.println("State changed to " + change.getCurrentState() +
                        " from " + change.getPreviousState());
            }

            @Override
            public void onError(String message, String code, Exception e) {
                System.out.println("There was a problem connecting!");
            }
        }, ConnectionState.ALL);

        // Subscribe to a channel
        Channel channel = pusher.subscribe("test_channel");

        // Bind to listen for events called "my_event" sent to "my-channel"
        channel.bind("my_event", new SubscriptionEventListener() {
            @Override
            public void onEvent(String channel, String event, String data) {
                System.out.println("Received event with data: " + data);
            }
        });

        get("/hello", (req, res) -> "hey there!");
    }
}
