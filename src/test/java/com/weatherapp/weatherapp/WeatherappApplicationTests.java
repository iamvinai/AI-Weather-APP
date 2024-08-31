package com.weatherapp.weatherapp;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.json.JSONObject;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.ai.chat.client.ChatClient;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.springframework.boot.test.context.SpringBootTest;

import com.weatherapp.weatherapp.controller.WeatherService;

@SpringBootTest
class WeatherappApplicationTests {

	private WeatherService weatherService;
    private MockWebServer mockWebServer;
    private ChatClient chatClient;

    @BeforeEach
    public void setup() {
        mockWebServer = new MockWebServer();
        try {
			mockWebServer.start();
		} catch (IOException e) {
			e.printStackTrace();
		}

        // Create a mock ChatClient
        chatClient = mock(ChatClient.class);
        ChatClient.Builder builder = mock(ChatClient.Builder.class);
        when(builder.build()).thenReturn(chatClient);

        // Create the WeatherService instance
        weatherService = new WeatherService(builder);
        weatherService.setCityName("London");
        weatherService.setUnit("metric");

    }

    @AfterEach
    public void tearDown() throws Exception {
        mockWebServer.shutdown();
    }

    @Test
    public void testGenerate() {
        // Set up the mock response from the ChatClient
        when(chatClient.prompt().user(anyString()).call().content()).thenReturn("\"It is a nice day!\"");

        String result = weatherService.generate("Current weather in London");
        assertEquals("It is a nice day!", result);
    }

    @Test
    public void testGetWeatherSuccess() throws Exception {
        // Mock API response
        mockWebServer.enqueue(new MockResponse()
                .setBody("{\"weather\":{\"description\":\"clear sky\"},\"main\":{\"temp\":20,\"temp_min\":15,\"temp_max\":25,\"humidity\":60},\"wind\":{\"speed\":5.0}}")
                .addHeader("Content-Type", "application/json"));

        JSONObject weather = weatherService.getWeather();
        assertNotNull(weather);
        assertEquals("clear sky", weather.getJSONArray("weather").getJSONObject(0).getString("description"));
        assertEquals(20, weather.getJSONObject("main").getInt("temp"));
        assertEquals(5.0, weather.getJSONObject("wind").getDouble("speed"));
    }

    @Test
    public void testGetWeatherFailure() throws Exception {
        // Mock API response for failure
        mockWebServer.enqueue(new MockResponse().setResponseCode(500));

        JSONObject weather = weatherService.getWeather();
        assertNull(weather);
    }

    @Test
    public void testGetWeatherObject() throws Exception {
        // Mock API response
        mockWebServer.enqueue(new MockResponse()
                .setBody("{\"main\":{\"temp\":20,\"temp_min\":15,\"temp_max\":25,\"humidity\":60}}")
                .addHeader("Content-Type", "application/json"));

        JSONObject mainObject = weatherService.getMainJsonArray();
        assertNotNull(mainObject);
        assertEquals(20, mainObject.getInt("temp"));
    }

}
