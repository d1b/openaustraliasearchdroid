package com.openaussearchdroid;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SearchReps extends Activity
{

	private static final String oakey = "F8c6oBD4YQsvEAGJT8DUgL8p";
	private EditText _etext;
	private TextView _tv;
	private ImageView _iv;
	private Button _repsbutton;
	private LinearLayout _tab;
	@SuppressWarnings("unused")
	private TextView _tvhans;
	private String previousSearch = "";

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.searchrep_reps);
		_iv = (ImageView) findViewById(R.id.MemberPic);
		_tab = (LinearLayout) findViewById(R.id.innerlayout);
		_tvhans = (TextView) findViewById(R.id.hansardmentions_label);
		_repsbutton = (Button) findViewById(R.id.searchRepButton);
		_repsbutton.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				_etext = (EditText) findViewById(R.id.EditText01);
				if (previousSearch.equals(_etext.getText().toString()))
				{
					Log.i("duplicate_search", " in search reps");
					return;
				}

				_tab.removeAllViewsInLayout();

				_tv = (TextView) findViewById(R.id.TextView01);
				String urlString = "http://www.openaustralia.org/api/getRepresentative" +
				"?key=" + oakey +
				"&division=" + URLEncoder.encode(_etext.getText().toString()) +
				"&output=json";
				String result;
				try
				{
					result = Utilities.getDataFromUrl(urlString, "OpenAusURL");
				}
				catch (IOException e)
				{
					Utilities.recordStackTrace(e);
					return;
				}
				JSONObject json;
				try
				{
					json = new JSONObject(result);
				}
				catch(JSONException e)
				{
					Log.i("Praeda", e.getMessage());
					e.printStackTrace();
					return;
				}

				JSONArray nameArray = json.names();
				JSONArray valArray;

				try
				{
					valArray = json.toJSONArray(nameArray);
				}
				catch (JSONException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
					return;
				}
				String memdata = null;
				String full_name = null;
				String date_entered = null;
				String party = null;
				String personid = null;
				String stringAtIndex = null;
				String valueValueAtIndex = null;
				for(int i = 0;i< valArray.length() ;i++)
				{
					try
					{
						stringAtIndex = nameArray.getString(i);
						valueValueAtIndex = valArray.getString(i);
					}
					catch (JSONException e)
					{
						Utilities.recordStackTrace(e);
						e.printStackTrace();
						return;
					}


					if(stringAtIndex.equals("full_name"))
					{
						full_name = "Name: " + valueValueAtIndex + "\n";
					}
					else if(stringAtIndex.equals("party"))
					{
						party = "Party: " + valueValueAtIndex + "\n";
					}
					else if(stringAtIndex.equals("entered_house"))
					{
						date_entered = "Date Elected: " + valueValueAtIndex + "\n";
					}
					else if(stringAtIndex.equals("person_id"))
					{
						personid = valueValueAtIndex;
					}
					else if(stringAtIndex.equals("image"))
					{
						try
						{
							fetchRepImage(valueValueAtIndex);
						}
						catch (IOException e)
						{
							Utilities.recordStackTrace(e);
						}
					}
				}

				memdata = full_name + date_entered + party;

				_tv.setText(memdata);
				/* Grab Hansard Mentions */
				urlString = "http://www.openaustralia.org/api/getDebates" +
				"?key="+ oakey +
				"&type=representatives" +
				"&order=d" +
				"&person=" + personid;
				Log.i("OpenAusURL", urlString);
				new PerformHansardSearch().execute(new HansardSearch(urlString, v, _tab));

				// A Simple JSONObject Value Pushing
				try
				{
					json.put("sample key", "sample value");
				}
				catch (JSONException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Log.i("Praeda","<jsonobject>\n"+json.toString()+"\n</jsonobject>");
				// Closing the input stream will trigger connection release
				previousSearch = _etext.getText().toString();

			}
		});
	}

	public void fetchRepImage(String valueValueAtIndex) throws IOException
	{
		URL aURL;
		Log.i("searchrepimage", "http://www.openaustralia.org" + valueValueAtIndex);
		aURL = new URL("http://www.openaustralia.org" + valueValueAtIndex);

		URLConnection con;
		con = aURL.openConnection();

		InputStream is;
		BufferedInputStream bis = null;

		con.connect();
		is = con.getInputStream();
		/* Buffered is always good for a performance plus. */
		bis = new BufferedInputStream(is);

		/* Decode url-data to a bitmap. */
		Bitmap bm = BitmapFactory.decodeStream(bis);
		try
		{
			bis.close();
		}
		catch (IOException e)
		{
			Utilities.recordStackTrace(e);
		}

		try
		{
			is.close();
		}
		catch (IOException e)
		{
			Utilities.recordStackTrace(e);
		}
		/* Apply the Bitmap to the ImageView that will be returned. */
		_iv.setImageBitmap(bm);
	}

	public void searchRepClickHandler(View target)
	{

	}

}
