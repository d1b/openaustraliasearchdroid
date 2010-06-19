package com.openaussearchdroid;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PerformHansardSearch extends AsyncTask <HansardSearch, Integer, JSONArray>
{
	private View v;
	private LinearLayout hansInnerLayout;

	@Override
	protected JSONArray doInBackground(HansardSearch... hansSearchArray)
	{
		// TODO Auto-generated method stub

		HansardSearch hansSearch = hansSearchArray[0];
		this.v = hansSearch.getView();
		this.hansInnerLayout = hansSearch.getHansInnerLayout();
		try
		{
			hansSearch.fetchSearchResultAndSetJsonResult();
		}
		catch (IOException e)
		{
			Utilities.recordStackTrace(e);
			e.printStackTrace();
		}
		catch (JSONException e)
		{
			Utilities.recordStackTrace(e);
			e.printStackTrace();
		}
		if (hansSearch.getResultJson() == null)
		{
			Log.e("json is null", "in doInBackground in PerformHansardSearch");
			return null;
		}

		JSONArray jsonArray;
		try
		{
			jsonArray = hansSearch.getResultJson().getJSONArray("rows");
		}
		catch (JSONException e)
		{
			System.out.println(hansSearch.getResultRaw());
			Utilities.recordStackTrace(e);
			return null;
		}
		return jsonArray;
	}
	@Override
	protected void onPostExecute(JSONArray json)
	{
		if (json == null)
		{
			Log.e("json is null", "on post exec in PerformHansardSearch");
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
			hansInnerLayout.addView(tvr);
		}
	}
}
