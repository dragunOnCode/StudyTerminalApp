package com.example.studyterminalapp.utils;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.example.studyterminalapp.MyApp;
import com.example.studyterminalapp.activity.LoginActivity;
import com.example.studyterminalapp.activity.SplashActivity;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttp;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class RequestManager {
    private OkHttpClient mOkHttpClient;
    private static String BASE_URL = Constants.WEB_SITE;
    private static RequestManager instance;
    private Gson gson;

    public RequestManager() {

        PersistentCookieJar cookieJar =
                new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(MyApp.getApplication().getBaseContext()));
        mOkHttpClient = new OkHttpClient.Builder()
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .cookieJar(cookieJar)
                .build();
    }

    public static RequestManager getInstance() {
        if (instance == null) {
            synchronized (RequestManager.class) {
                if (instance == null) {
                    instance = new RequestManager();
                }
            }
        }
        return instance;
    }

    /***
     * post请求
     */
    public void PostRequest(HashMap<String, Object> paramsMap, String url, final ResultCallback callback) throws UnsupportedEncodingException {
        gson = new Gson();
        String params = gson.toJson(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), params);
        String requestUrl = BASE_URL + url;

        final Request request = new Request.Builder()
                .url(requestUrl)
                .post(body)
                .build();

        Call callClient = mOkHttpClient.newCall(request);

        callClient.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError("网络请求失败，请稍后重试");
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (response.isSuccessful()) {
                    int code = response.code();
                    if (code == 200) {//请求成功
                        String result = response.body().string();
                        callback.onResponse(String.valueOf(code), result);
                    } else
                        callback.onError(response.message());
                } else {
                    callback.onError("网络请求失败，请稍后重试");
                }

            }

        });
    }

    /**
     * Get请求
     */
    public void GetRequest(HashMap<String, Object> paramsMap, String url, final ResultCallback callback) throws UnsupportedEncodingException {
        gson = new Gson();
        StringBuffer sb = new StringBuffer();
        sb.append("?");

        Iterator<String> iterator = paramsMap.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String value = URLEncoder.encode(paramsMap.get(key).toString(), "utf-8");
            sb.append(key + "=" + value);
            if (iterator.hasNext()) {
                sb.append("&");
            }
        }
        String requestUrl;
        if (sb.length() == 1 && sb.toString().equals("?")) {
            requestUrl = BASE_URL + url;
        } else {
            requestUrl = BASE_URL + url + sb;
        }

        Log.i("Get Request", requestUrl);

        final Request request = new Request.Builder()
                .url(requestUrl)
                .get()
                .build();

        Call callClient = mOkHttpClient.newCall(request);

        callClient.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError("网络请求失败，请稍后重试");
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (response.isSuccessful()) {
                    int code = response.code();
                    if (code == 200) {//请求成功
                        String result = response.body().string();
                        callback.onResponse(String.valueOf(code), result);
                    } else
                        callback.onError(response.message());
                } else {
                    callback.onError("网络请求失败，请稍后重试");
                }

            }

        });
    }

    public void syncGetRequest(HashMap<String, Object> paramsMap, String url) throws UnsupportedEncodingException {
        gson = new Gson();
        StringBuffer sb = new StringBuffer();
        sb.append("?");

        Iterator<String> iterator = paramsMap.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String value = URLEncoder.encode(paramsMap.get(key).toString(), "utf-8");
            sb.append(key + "=" + value);
            if (iterator.hasNext()) {
                sb.append("&");
            }
        }
        String requestUrl = BASE_URL + url + sb;
        Log.i("Get Request", requestUrl);

        final Request request = new Request.Builder()
                .url(requestUrl)
                .get()
                .build();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Response response = null;
                try {
                    response = mOkHttpClient.newCall(request).execute();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                String result = null;
                try {
                    result = response.body().string();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Log.i("Sync Get", "result : " + result);
            }
        }).start();
    }

    /***
     * put请求
     */
    public void PutRequest(HashMap<String, Object> paramsMap, String url, final ResultCallback callback) throws UnsupportedEncodingException {
        gson = new Gson();
        String params = gson.toJson(paramsMap);
        RequestBody body = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), params);
        String requestUrl = BASE_URL + url;

        final Request request = new Request.Builder()
                .url(requestUrl)
                .put(body)
                .build();

        Call callClient = mOkHttpClient.newCall(request);

        callClient.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError("网络请求失败，请稍后重试");
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (response.isSuccessful()) {
                    int code = response.code();
                    if (code == 200) {//请求成功
                        String result = response.body().string();
                        callback.onResponse(String.valueOf(code), result);
                    } else
                        callback.onError(response.message());
                } else {
                    callback.onError("网络请求失败，请稍后重试");
                }

            }

        });
    }

    /***
     * delete请求
     */
    public void DeleteRequest(HashMap<String, Object> paramsMap, String url, final ResultCallback callback) throws UnsupportedEncodingException {
        gson = new Gson();
        StringBuffer sb = new StringBuffer();
        sb.append("?");

        Iterator<String> iterator = paramsMap.keySet().iterator();
        while (iterator.hasNext()) {
            String key = iterator.next();
            String value = URLEncoder.encode(paramsMap.get(key).toString(), "utf-8");
            sb.append(key + "=" + value);
            if (iterator.hasNext()) {
                sb.append("&");
            }
        }
        String requestUrl = BASE_URL + url + sb;
        Log.i("Delete Request", requestUrl);

        final Request request = new Request.Builder()
                .url(requestUrl)
                .delete()
                .build();

        Call callClient = mOkHttpClient.newCall(request);

        callClient.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onError("网络请求失败，请稍后重试");
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (response.isSuccessful()) {
                    int code = response.code();
                    if (code == 200) {//请求成功
                        String result = response.body().string();
                        callback.onResponse(String.valueOf(code), result);
                    } else
                        callback.onError(response.message());
                } else {
                    callback.onError("网络请求失败，请稍后重试");
                }

            }

        });
    }

    public interface ResultCallback {
        void onResponse(String code, String response);

        void onError(String msg);
    }
}


