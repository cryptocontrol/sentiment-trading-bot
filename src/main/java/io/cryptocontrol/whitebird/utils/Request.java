package io.cryptocontrol.whitebird.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.cryptocontrol.whitebird.Context;
import io.cryptocontrol.whitebird.Parameters;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;

/**
 * @author enamakel@cryptocontrol.io
 */
public class Request {
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");


    private static OkHttpClient getClient(Parameters parameters) {
        OkHttpClient client = new OkHttpClient();
        return client;
    }


    public static String makeGetRequest(String url) throws IOException {
        Context context = Context.getInstance();
        Parameters parameters = context.getParameters();

        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .build();

        Response response = getClient(parameters).newCall(request).execute();
        return response.body().string();
    }


    public static String makePostRequest(String url, JsonObject jsonObject) throws IOException {
        Context context = Context.getInstance();
        Parameters parameters = context.getParameters();

        if (jsonObject != null) jsonObject = new JsonObject();
        RequestBody body = RequestBody.create(JSON, jsonObject.getAsString());

        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .post(body)
                .build();

        Response response = getClient(parameters).newCall(request).execute();
        return response.body().string();
    }


    public static JsonObject makeGetJSONRequest(String url) throws IOException {
        String body = makeGetRequest(url);

        JsonParser parser = new JsonParser();
        return parser.parse(body).getAsJsonObject();
    }


    public static JsonObject makePostJSONRequest(String url, JsonObject jsonObject) throws IOException {
        String body = makePostRequest(url, jsonObject);

        JsonParser parser = new JsonParser();
        return parser.parse(body).getAsJsonObject();
    }
}
