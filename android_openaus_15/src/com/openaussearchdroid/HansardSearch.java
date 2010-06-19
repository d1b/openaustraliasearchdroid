package com.openaussearchdroid;

import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;


public class HansardSearch
{
	private final String url;
	private JSONObject resultJson;
	private String resultRaw;


	public HansardSearch(String url)
	{
		this.url = url;
	}

	public void fetchSearchResult() throws IOException
	{
		this.resultRaw = Utilities.getDataFromUrl(this.url, "url");
	}
	public void setJsonResultFromRawResult() throws JSONException
	{
		this.resultJson = new JSONObject(this.resultRaw);
	}
	public void fetchSearchResultAndSetJsonResult() throws IOException, JSONException
	{
		fetchSearchResult();
		setJsonResultFromRawResult();
	}
	public String getResultRaw()
	{
		return this.resultRaw;
	}
	public JSONObject getResultJson()
	{
		return this.resultJson;
	}

}
