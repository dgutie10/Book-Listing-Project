package com.example.android.booklisting;

import android.graphics.drawable.Drawable;
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
import java.sql.Struct;
import java.util.ArrayList;
import java.util.List;
import java.util.SimpleTimeZone;


/**
 * Created by diegog on 11/3/2016.
 */

public final class QueryUtil {

    public static final String LOG_TAG = QueryUtil.class.getName();

    private QueryUtil(){}

    public static ArrayList<Book> extractContent(String jsonString){
        if (TextUtils.isEmpty(jsonString)) return null;

        ArrayList<Book> books = new ArrayList<>();

        try{
            JSONObject root = new JSONObject(jsonString);
            JSONArray bookArray = root.getJSONArray("items");

            for(int i = 0; i < bookArray.length(); i++){
                JSONObject currentBook = bookArray.getJSONObject(i);
                JSONObject volumeInfo =  currentBook.getJSONObject("volumeInfo");
                String title = volumeInfo.getString("title");
                String author = volumeInfo.getString("authors");
                String thumbNail = volumeInfo.getJSONObject("imageLinks").getString("thumbnail");
                Log.e("QueryUtils","img :"+ thumbNail);
                books.add(new Book(title,author,getImageFromUrl(thumbNail,title)));
            }

        }catch (JSONException e){
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }
        return books;
    }

    public static List<Book> fetchBooks (String requestUrl){

        Log.e(LOG_TAG,"onCreateLoader initiated");
        URL url = createUrl(requestUrl);
        String jsonResponse = null;
        try{
            jsonResponse = makeHttpRequest(url);
        }catch (IOException e){
            Log.e(LOG_TAG,"Problem making the HTTP request");
        }

        List<Book> books = extractContent(jsonResponse);

        return books;
    }

    private static URL createUrl(String requestUrl){
        URL url = null;
        try{
            url = new URL(requestUrl);
        }catch (MalformedURLException e){
            Log.e(LOG_TAG, "Problem building url ",e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException{
        String responseJson = "";
        if (url == null ) return null;

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;

        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            if (urlConnection.getResponseCode() == 200){
                inputStream = urlConnection.getInputStream();
                responseJson = readFromStream(inputStream);
            }else {
                Log.e(LOG_TAG,"Error response code: "+urlConnection.getResponseCode());
            }
        }catch (IOException e){
            Log.e(LOG_TAG,"Error retrieving data",e);
        } finally {
            if (inputStream != null) inputStream.close();
            if (urlConnection != null) urlConnection.disconnect();
        }

        return responseJson;
    }

    private static String readFromStream (InputStream inputStream) throws IOException{
        StringBuilder output = new StringBuilder();

        if (inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null){
                output.append(line);
                line = reader.readLine();
            }
        }

        return output.toString();
    }

    private static Drawable getImageFromUrl (String requestUrl, String title){
        URL url = createUrl(requestUrl);
        try {
            InputStream input = (InputStream) url.getContent();
            Drawable image = Drawable.createFromStream(input,title);
            return image;

        }catch (IOException e){
            Log.e(LOG_TAG,"getImageFromUrl: Error retrieving image.",e);
            return null;
        }

    }

    private static String arrayToString (JSONArray arr ){
        StringBuilder builder = new StringBuilder();
        try{
            for (int i = 0; i < arr.length(); i++){
                builder.append(arr.getJSONObject(i).getString(""));
            }
        }catch (JSONException e){
            return null;
        }
    }
}
