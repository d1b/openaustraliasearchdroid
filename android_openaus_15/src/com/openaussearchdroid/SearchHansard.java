package com.openaussearchdroid;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

public class SearchHansard extends Activity
{

	private static EditText et;
	private static TextView tv;
	private static Spinner houseselect;
	private static final String oakey = "F8c6oBD4YQsvEAGJT8DUgL8p";
	private static Button hansbutton;
	private static LinearLayout hansinner;

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.searchhansard);
		houseselect = (Spinner) findViewById(R.id.HouseSelector);
		hansinner = (LinearLayout) findViewById(R.id.hansinnerlayout);
		final String[] items = {"representatives", "senate"};
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, items);
		houseselect.setAdapter(adapter);
		hansbutton = (Button) findViewById(R.id.SearchHansardButton);
		hansbutton.setOnClickListener(new View.OnClickListener()
		{

			public void onClick(View v)
			{
				et = (EditText) findViewById(R.id.SearchHansardText);
				String urlString = "http://www.openaustralia.org/api/getDebates" +
				"?key=" + oakey +
				"&type=" + houseselect.getSelectedItem().toString() +
				"&search=" + URLEncoder.encode(et.getText().toString()) +
				"&output=json";
				String result;
				try
				{
					result = Utilities.getDataFromUrl(urlString, "url");
				}
				catch (IOException e)
				{
					e.printStackTrace();
					return;
				}
				Log.i("GetResult",result);
				JSONObject jsonr;
				try
				{
					jsonr = new JSONObject(result);
				}
				catch(JSONException e)
				{
					Log.e("jsonerror", e.getMessage().toString());
					return;
				}

				hansinner.removeAllViewsInLayout();
				JSONArray json;

				try
				{
					json = jsonr.getJSONArray("rows");
				}
				catch (JSONException e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
					return;
				}

				JSONObject jsonD;
				for(int i = 0; i < json.length(); i++)
				{
					jsonD = null;

					try
					{
						jsonD = json.getJSONObject(i);
					}
					catch (JSONException e1)
					{
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					TextView tvr = new TextView(v.getContext());
					tvr.setId(500+i);
					try
					{
						tvr.setText(jsonD.getString("body"));
					}
					catch (JSONException e)
					{
						e.printStackTrace();
					}
					hansinner.addView(tvr);
				}
				/*
				catch (IOException e)
				{
					Log.e("DEBUGTAG", "Remtoe Image Exception", e);
				}
				*/
			}
		});
	}

	/** is this even used ? */
	public void searchHansardButtonClick(View target) throws IOException
	{
		et = (EditText) findViewById(R.id.SearchHansardText);
		String urlString = "http://www.openaustralia.org/api/getDebates" +
		"?key=" + oakey +
		"&type=" + houseselect.getSelectedItem().toString() +
		"&search=" + URLEncoder.encode(et.getText().toString()) +
		"&output=json";
		String result = Utilities.getDataFromUrl(urlString, "url");
		Log.i("GetResult",result);
		JSONObject jsonr;
		try
		{
			jsonr = new JSONObject(result);
		}
		catch(JSONException e)
		{
			Log.e("jsonerror", e.getMessage().toString());
			return;
		}
		/* TODO: use a string builder here instead of String for memdata*/
		String memdata = null;
		for(int j = 0; j < jsonr.length(); j++)
		{
			JSONArray json;
			try
			{
				json = jsonr.getJSONArray("rows");
			}
			catch (JSONException e1)
			{
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return;
			}
			for(int i = 0; i < json.length(); i++)
			{
				JSONObject jsonD = null;
				try
				{
					jsonD = new JSONObject(json.getJSONObject(j).toString());
				}
				catch (JSONException e1)
				{
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				JSONArray nameArray=jsonD.names();
				JSONArray valArray = null;
				try
				{
					valArray = jsonD.toJSONArray(nameArray);
				}
				catch (JSONException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();

				}
				String quote = "";
				for(int k = 0;k < valArray.length();k++)
				{
					try
					{
						if(nameArray.getString(k).equals("body"))
						{
							quote = valArray.getString(k).toString();
						}
					}
					catch (JSONException e)
					{
						e.printStackTrace();
					}
				}
				memdata += quote + "\n\n";
			}
			tv.setText(memdata);
		}
		/*
		catch (IOException e)
		{
			Log.e("DEBUGTAG", "Remtoe Image Exception", e);
		}
		*/
	}

}
