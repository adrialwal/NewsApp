package com.example.adrialwalters.newsapp;

import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper methods related to requesting and receiving news data from Guardian API.
 */
public final class QueryUtils extends AppCompatActivity {

    /**
     * Tag for the log message
     */
    private static final String LOG_TAG = QueryUtils.class.getSimpleName();

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Query the Guardian API and return an {@link News} objects.
     */
    public static List<News> fetchNewsData(String requestUrl) {

        Log.i(LOG_TAG, "Test: fetchNewsData() called...");

        // Create URL object
        URL url = createUrl(requestUrl);
        String jsonResponse = null;

        // Perform HTTP request to the URL and receive a JSON response back
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request", e);
        }

        // Extract relevant fields from the JSON response and create an {@link News}.
        List<News> news = extractArticleFromJson(jsonResponse);

        return news;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL");
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(1500);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code:" + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            // InputStreamReader reads and decodes them into characters using a specified charset
            InputStreamReader inputStreamReader =
                    new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            // Since InputStreamReader only reads a single character at a time, wrapping it in a
            // BufferedReader will buffer the input before converting into character and returning
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return a list of {@link News} objects that has been built up from
     * parsing the given JSON response
     */
    public static List<News> extractArticleFromJson(String newsJSON) {
        String title = "";
        String date = "";
        String dateTime;
        String category = "";
        String url = "";

        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(newsJSON)) {
            return null;
        }

        // Create an empty ArrayList that we can start adding books to.
        ArrayList<News> newsList = new ArrayList<>();

        //Try to parse the JSON response string. If there's a problem with the way the JSON
        // if formatted, a JSONException exception object will be thrown.
        // Catch the exception so that app doesn't crash, and print the error message to the logs.
        try {

            // Create a JSONObject from the JSON response jsonResponse
            JSONObject baseJsonResponse = new JSONObject(newsJSON);

            // Create a JSONArray from the response
            JSONObject responseJsonObject = baseJsonResponse.getJSONObject("response");
            JSONArray resultsArray = responseJsonObject.getJSONArray("results");

            // For each book in the newsArray, create an {@link News} object
            for (int i = 0; i < resultsArray.length(); i++) {

                // Get the news at the current index and crate a JSON Object
                JSONObject thisNews = resultsArray.getJSONObject(i);

                // Extract the title of the JSON object
                if (thisNews.has("webTitle")) {
                    title = thisNews.getString("webTitle");
                }

                // Extract the date of the JSON object
                if (thisNews.has("webPublicationDate")) {
                    dateTime = thisNews.getString("webPublicationDate");
                    date = dateTime.substring(0, 10);
                }

                // Extract the URL from the JSON object
                if (thisNews.has("webUrl")) {
                    url = thisNews.getString("webUrl");
                }

                // Extract the section for the article from the JSON object
                if (thisNews.has("sectionName")) {
                    category = thisNews.getString("sectionName");
                }

                // Add the new {@link News} to the list of articles.
                News news = new News(title, category, date, url);
                newsList.add(news);
            }
        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from exception.
            Log.e(LOG_TAG, "Problem parsing the book JSON results", e);
        }
        // Return the list of news articles
        return newsList;
    }
}


