package com.example.android.newsapp;

import android.net.Uri;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class QueryUtils {

    private static final String HTTPS = "https";
    private static final String BASE_URL = "content.guardianapis.com";
    private static final String SEARCH = "search";
    private static final String API_KEY = "api-key";
    private static final String SHOW_REFERENCES = "show-references";
    private static final String SHOW_TAGS = "show-tags";
    private static final String SECTION = "section";
    private static final String SEARCH_VALUE = "q";
    private static final String ORDER_BY = "order-by";
    private static final String DATE_FORMAT = "MMM d, yyy";
    private static final String JSON_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";
    private static final String HTTP_GET = "GET";
    private static final int HTTP_OK = 200;
    private static final int TIMEOUT = 15000;
    private static final String UTF_8 = "UTF-8";
    private static final String SEPARATOR = ", ";

    // Create the URL
    // https://content.guardianapis.com/search?order-by=newest&show-references=author&show-tags=contributor&section=sport&q=football&api-key=test
    public static URL createUrl() {
        Uri.Builder builder = new Uri.Builder();

        String stringUrl = builder.scheme(HTTPS)
            .encodedAuthority(BASE_URL)
            .appendPath(SEARCH)
            .appendQueryParameter(API_KEY, "test")
            .appendQueryParameter(SHOW_REFERENCES, "author")
            .appendQueryParameter(SHOW_TAGS, "contributor")
            .appendQueryParameter(SECTION, "sport")
            .appendQueryParameter(SEARCH_VALUE, "football")
            .appendQueryParameter(ORDER_BY, "newest")
            .toString();

        try {
            return new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e("Queryutils ", "Creating URL failed: ", e);
            return null;
        }
    }

    public static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";
        HttpURLConnection urlConnection = null;
        InputStream responseStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod(HTTP_GET);
            urlConnection.setReadTimeout(TIMEOUT);
            urlConnection.setConnectTimeout(TIMEOUT);
            urlConnection.connect();
            int responseCode = urlConnection.getResponseCode();

            if (responseCode == HTTP_OK){
                responseStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(responseStream);
            } else {
                Log.e("QueryUtils ", "Response code: " + responseCode);
            }
        } catch (IOException e) {
            Log.e("Queryutils ", "HTTP request faile:\n" + e.toString());
        } finally {
            if (responseStream != null) {
                responseStream.close();
            }
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return jsonResponse;
    }

    public static List<News> parseJson(String response) {
        ArrayList<News> listOfNews = new ArrayList<>();

        try {
            JSONObject jsonResponse = new JSONObject(response);
            JSONObject jsonResults = jsonResponse.getJSONObject("response");
            JSONArray results = jsonResults.getJSONArray("results");

            for (int i = 0; i < results.length(); i++) {
                JSONObject result = results.getJSONObject(i);
                String title = result.getString("webTitle");
                String url = result.getString("webUrl");
                String date = result.getString("webPublicationDate");
                String section = result.getString("sectionName");
                JSONArray tagsArray = result.getJSONArray("tags");

                String authorsString = "";
                if (tagsArray.length() == 0) {
                    authorsString = null;
                } else {
                    for (int j = 0; j < tagsArray.length(); j++) {
                        JSONObject firstObject = tagsArray.getJSONObject(j);
                        authorsString += (firstObject.getString("webTitle")) + SEPARATOR;
                    }
                    authorsString = authorsString.substring(
                            0, authorsString.length() - (1 + SEPARATOR.length()));
                }
                listOfNews.add(
                        new News(title, authorsString, beautifyJsonDate(date), section, url));
            }
        } catch (JSONException e) {
            Log.e("Queryutils ", "JSON parsing failed ", e);
        }
        return listOfNews;
    }

    // Format raw date
    private static String beautifyJsonDate(String rawDate) {
        SimpleDateFormat jsonRawDateFormatter = new SimpleDateFormat(JSON_DATE_FORMAT, Locale.US);
        SimpleDateFormat finalDateFormatter = new SimpleDateFormat(DATE_FORMAT, Locale.US);

        try {
            Date parsedJsonDate = jsonRawDateFormatter.parse(rawDate);
            return finalDateFormatter.format(parsedJsonDate);
        } catch (ParseException e) {
            Log.e("Queryutils ", "JSON date formatting failed ", e);
            return rawDate;
        }
    }

    private static String readFromStream(InputStream inputStream){
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader =
                    new InputStreamReader(inputStream, Charset.forName(UTF_8));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            try {
                String line = reader.readLine();
                while (line != null) {
                    output.append(line);
                    line = reader.readLine();
                }
            } catch (IOException e) {
                Log.e("Queryutils ", "Stream reading failed ", e);
            }
        }
        return output.toString();
    }

}
