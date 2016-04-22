package com.meetuprsvp;

import static spark.Spark.get;
import static spark.SparkBase.staticFileLocation;

import com.ning.http.client.AsyncHttpClient;
import com.pusher.rest.Pusher;

public class App {
    private Pusher pusher;

    public void getMeetupData () {
        AsyncHttpClient c = new AsyncHttpClient(new StreamConfig().build());
        c.prepareGet("https://stream.meetup.com/2/rsvps").execute(new Handler(pusher));
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
