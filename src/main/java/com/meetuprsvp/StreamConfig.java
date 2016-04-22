package com.meetuprsvp;

import com.ning.http.client.AsyncHttpClientConfig;

/**
 * Created by linhbui on 4/22/16.
 */
class StreamConfig extends AsyncHttpClientConfig.Builder {
    public StreamConfig() {
        setReadTimeout(3600000);
        setRequestTimeout(3600000);
    }
}
