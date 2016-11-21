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
        String author = null;
        if (TextUtils.isEmpty(jsonString)) return null;

        ArrayList<Book> books = new ArrayList<>();

        try{
            JSONObject root = new JSONObject(jsonString);
            JSONArray bookArray = root.getJSONArray("items");

            for(int i = 0; i < bookArray.length(); i++){
                JSONObject currentBook = bookArray.getJSONObject(i);
                JSONObject volumeInfo =  currentBook.getJSONObject("volumeInfo");
                String title = volumeInfo.getString("title");
                if (volumeInfo.has("authors")){author = arrayToString(volumeInfo.getString("authors"));}
                else author = "No author found";
                String thumbNail = volumeInfo.getJSONObject("imageLinks").getString("thumbnail");
                books.add(new Book(title,author,getImageFromUrl(thumbNail,title)));
            }

        }catch (JSONException e){
            Log.e("QueryUtils", "Problem parsing the book JSON results", e);
        }
        return books;
    }

    public static List<Book> fetchBooks (String requestUrl){

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
            return null;
        }

    }

    private static String arrayToString (String authors ){
        String[] arr = authors.substring(1,authors.length()-1).split(",");
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            builder.append(arr[i].substring(1,arr[i].length()-1));
            builder.append(", ");
        }
        String finalString= builder.toString().trim();
        return finalString.substring(0,finalString.length()-1);
    }
}
