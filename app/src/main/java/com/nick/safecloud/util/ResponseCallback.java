package com.nick.safecloud.util;

/**
 * Created by Sparrow on 2017/2/21.
 */

import android.util.Log;

import com.zhy.http.okhttp.callback.Callback;

import java.io.IOException;

import okhttp3.Response;

public abstract class ResponseCallback extends Callback<ResponseCallback.ResponseResult> {
    private static final String TAG = "ResponseCallback";
    @Override
    public ResponseResult parseNetworkResponse(Response response, int id) throws IOException {
        for (String s : response.headers("Set-Cookie")) {
            Log.i(TAG, s);
        }
        return new ResponseResult(response.body().string(), response.header("Set-Cookie"));
    }


    public static class ResponseResult {
        public String cookie;
        public String reponse;

        public ResponseResult(String reponse, String cookie) {
            this.reponse = reponse;
            this.cookie = cookie;
        }


        public String getCookie() {
            return cookie;
        }

        public void setCookie(String cookie) {
            this.cookie = cookie;
        }

        public String getReponse() {
            return reponse;
        }

        public void setReponse(String reponse) {
            this.reponse = reponse;
        }

        @Override
        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("response : ")
                    .append(reponse)
                    .append("\n\n")
                    .append("cookie : ")
                    .append(cookie);

            return stringBuilder.toString();
        }
    }


}
