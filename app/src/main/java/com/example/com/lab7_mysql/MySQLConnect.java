package com.example.com.lab7_mysql;

import android.app.Activity;
import android.os.Build;
import android.os.StrictMode;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("ALL")
public class MySQLConnect {
    private final Activity main;
    private List<String> list;
    private String URL = "http://localhost/" , GET_URL = "Lab7/get_post.php" , SENT_URL ="Lab7/sent_post.php";

    public  MySQLConnect(MainActivity mainActivity){ main = null;}
    public MySQLConnect(Activity mainA) {

        main = mainA;
        list = new ArrayList<String>();
    }
    public List<String> getData() {
        String url = URL + GET_URL;
        //noinspection deprecation
        StringRequest stringRequest = new StringRequest(url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                showJSON(response);
                Toast.makeText(main, list.get(0), Toast.LENGTH_LONG).show();
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(main,error.getMessage(),Toast.LENGTH_LONG).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(main.getApplicationContext());
        requestQueue.add(stringRequest);
        return list;
    }
    private void showJSON(String response) {
        String comment = "";
        try{
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray("result");
            for (int i = 0; i <result.length();i++){
                JSONObject collectData = result.getJSONObject(i);
                comment = collectData.getString("comment");
                list.add(comment);
            }
        }catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    public void sentData(String value) {
        StrictMode.enableDefaults();
        if (Build.VERSION.SDK_INT > 9){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        }
        try {
            ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            nameValuePairs.add(new BasicNameValuePair("isAdd","true"));
            nameValuePairs.add(new  BasicNameValuePair("comment",value));
            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(URL + SENT_URL);
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UFT-8"));
            httpClient.execute(httpPost);
            Toast.makeText(main,"Completed",Toast.LENGTH_LONG).show();
        }catch (UnsupportedEncodingException e){e.printStackTrace();}
        catch (ClientProtocolException e){e.printStackTrace();}catch (IOException e){e.printStackTrace();}
    }
}
