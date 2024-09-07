package com.weatherapp.weatherapp.controller;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.ai.chat.client.ChatClient;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Service
public class WeatherService {
    
    private final OkHttpClient client = new OkHttpClient();
    private final ChatClient chatClient;

    @Value("${weather.api.key}")
    private String apiKey;
    
    private String cityName;
    private String unit;

    public WeatherService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public String generate(String message) {
        message += " Generate a funny comment on this weather in two sentences only.";
        if ("imperial".equals(getUnit())) {
            message += " Take temperature in Fahrenheit.";
        } else if ("metric".equals(getUnit())) {
            message += " Take temperature in Celsius.";
        }
        String response = chatClient.prompt().user(message).call().content();
        return response.substring(1, response.length()-1);
    }

    public JSONObject getWeather() {
        String url = String.format("https://api.openweathermap.org/data/2.5/weather?q=%s&units=%s&appid=%s",
            getCityName(), getUnit(), apiKey);
        
        Request request = new Request.Builder().url(url).build();
        try (Response response = client.newCall(request).execute()) {
            if (response.body() != null) {
                return new JSONObject(response.body().string());
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject getWeatherJsonArray() {
        return getWeatherObject("weather");
    }

    public JSONObject getMainJsonArray() {
        return getWeatherObject("main");
    }

    public JSONObject getWindJsonArray() {
        return getWeatherObject("wind");
    }

    public JSONObject getSysJsonArray() {
        return getWeatherObject("sys");
    }

    private JSONObject getWeatherObject(String key) {
        try {
            JSONObject weather = getWeather();
            return weather != null ? weather.getJSONObject(key) : null;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}