package com.openaussearchdroid;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.net.HttpURLConnection;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.openaussearchdroid.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


public class OpenAusSearchDroid extends Activity {
    /** Called when the activity is first created. */
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
    
    public void launchSearchReps(View target){
    	Intent myIntent = new Intent(target.getContext(), SearchReps.class);
        startActivityForResult(myIntent, 0);
    }
    
    public void launchSearchSenate(View target){
    	Intent myIntent = new Intent(target.getContext(), SearchSenate.class);
        startActivityForResult(myIntent, 0);
    }
    
    public void launchSearchHansard(View target){
    	Intent myIntent = new Intent(target.getContext(), SearchHansard.class);
        startActivityForResult(myIntent, 0);
    }

}