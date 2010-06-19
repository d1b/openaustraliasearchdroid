package com.openaussearchdroid;

import java.io.IOException;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

public class SearchHansardActivity extends Activity
{

	private EditText _et;
	private String previousSelection;
	private TextView _tv;
	private Spinner houseselect;
	private static final String oakey = "F8c6oBD4YQsvEAGJT8DUgL8p";
	private Button hansButton;

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.searchhansard);
		houseselect = (Spinner) findViewById(R.id.HouseSelector);
		final String[] items = {"representatives", "senate"};
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, items);
		houseselect.setAdapter(adapter);
		hansButton = (Button) findViewById(R.id.SearchHansardButton);
		hansButton.setOnClickListener(new View.OnClickListener()
		{

			public void onClick(View v)
			{
				EditText choice = (EditText) findViewById(R.id.SearchHansardText);

				if (choice.equals(_et) && houseselect.getSelectedItem().toString().equals(previousSelection))
				{
					Log.i("duplicate_selection", "duplicate search in hansard search");
					return;
				}
				LinearLayout hansInnerLayout = (LinearLayout) findViewById(R.id.hansinnerlayout);
				hansInnerLayout.removeAllViewsInLayout();

				new PerformHansardSearch().execute(new HansardSearch(getHansardUrl(), v, hansInnerLayout));
				previousSelection = houseselect.getSelectedItem().toString();
			}
		});
	}


	public String getHansardUrl()
	{
		_et = (EditText) findViewById(R.id.SearchHansardText);
		String urlString = "http://www.openaustralia.org/api/getDebates" +
		"?key=" + oakey +
		"&type=" + houseselect.getSelectedItem().toString() +
		"&search=" + URLEncoder.encode(_et.getText().toString()) +
		"&output=json";
		return urlString;
	}

	/** is this even used ? */
	public void searchHansardButtonClick(View target) throws IOException
	{
		String result = Utilities.getDataFromUrl(getHansardUrl(), "url");
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
			_tv.setText(memdata);
		}
	}
}

