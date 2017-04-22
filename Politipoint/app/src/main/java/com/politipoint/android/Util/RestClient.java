package com.politipoint.android.Util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.Calendar;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RestClient {

    private static MemberService service;
    private static String key = "ZsyvmijWed7wQqIJ6bz8d3W1vDXXvqm1jPAQTJMi";

    public static MemberService getClient() {
        if (service == null) {
            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ssZ")
                    .create();

            // Add logging into retrofit 2.0
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);



            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Interceptor.Chain chain) throws IOException {

                            long timestamp = (int) (Calendar.getInstance().getTimeInMillis() / 1000);

                            Request request = chain.request();
                            HttpUrl url = request.url()
                                    .newBuilder()
                                    //.addQueryParameter("api_key", key)
                                    .build();

                            request = request
                                    .newBuilder()
                                    .url(url)
                                    .addHeader("X-API-Key",key)
                                    .build();

                            Response response = chain.proceed(request);

                            return response;
                        }
                    }).build();


            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.propublica.org/congress/v1/")
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(client).build();

            service = retrofit.create(MemberService.class);
        }
        return service;
    }
}
