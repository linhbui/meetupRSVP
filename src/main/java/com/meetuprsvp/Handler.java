package com.meetuprsvp;

import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.HttpResponseBodyPart;
import com.ning.http.client.Response;
import com.pusher.rest.Pusher;

import javax.json.JsonObject;

/**
 * Created by linhbui on 4/22/16.
 */
class Handler extends AsyncCompletionHandler<Response> {
    private Pusher pusher;

    public Handler(Pusher pusher) {
        this.pusher = pusher;
    }

    @Override
    public STATE onBodyPartReceived (HttpResponseBodyPart content){
        JsonParser parser = new JsonParser(content);
        JsonObject data = JsonMaker.dataToJson(parser.getLon(), parser.getLat(), parser.getCity());
        System.out.println(parser.getCity());

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
}
