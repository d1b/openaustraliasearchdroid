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
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Button;

public class SearchReps extends Activity
{

	private static final String oakey = "F8c6oBD4YQsvEAGJT8DUgL8p";
	private EditText etext;
	private TextView tv;
	private ImageView iv;
	private Button repsbutton;
	private LinearLayout tab;
	@SuppressWarnings("unused")
	private TextView tvhans;

	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.searchrep_reps);
		iv = (ImageView) findViewById(R.id.MemberPic);
		tab = (LinearLayout) findViewById(R.id.innerlayout);
		tvhans = (TextView) findViewById(R.id.hansardmentions_label);
		repsbutton = (Button) findViewById(R.id.searchRepButton);
		repsbutton.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View v)
			{
				// TODO Auto-generated method stub

				etext = (EditText) findViewById(R.id.EditText01);
				Context context = v.getContext();
				tv = (TextView) findViewById(R.id.TextView01);
				String urlstring = "http://www.openaustralia.org/api/getRepresentative" +
				"?key=" + oakey +
				"&division=" + URLEncoder.encode(etext.getText().toString()) +
				"&output=json";
				Log.i("OpenAusURL", urlstring);
				URL url;
				try 
				{
					url = new URL(urlstring);
				}
				catch (MalformedURLException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
					return;
				}
				InputStream instream;
				try
				{
					instream = url.openStream();
				}
				catch (IOException e)
				{
					Log.i("Praeda", e.getMessage());
					e.printStackTrace();
					return;
				}
				try
				{
					String result = Utilities.convertStreamToString(instream);
					Log.i("GetResult",result);
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
					for(int i = 0;i< valArray.length() ;i++)
					{
						try
						{
							Log.i("Praeda","<jsonname"+i+">\n"+nameArray.getString(i)+"\n</jsonname"+i+">\n"
									+"<jsonvalue"+i+">\n"+valArray.getString(i)+"\n</jsonvalue"+i+">");
							if(nameArray.getString(i).equals("full_name"))
							{
								full_name = "Name: " + valArray.getString(i) + "\n";
							}
							else if(nameArray.getString(i).equals("party"))
							{
								party = "Party: " + valArray.getString(i) + "\n";
							}
							else if(nameArray.getString(i).equals("entered_house"))
							{
								date_entered = "Date Elected: " + valArray.getString(i) + "\n";
							}
							else if(nameArray.getString(i).equals("person_id"))
							{
								personid = valArray.getString(i);
							}
							else if(nameArray.getString(i).equals("image"))
							{
								URL aURL;
								try
								{
									Log.i("searchrepimage", "http://www.openaustralia.org" + valArray.getString(i));
									aURL = new URL("http://www.openaustralia.org" + valArray.getString(i));
								}
								catch (MalformedURLException e1)
								{
									// TODO Auto-generated catch block
									e1.printStackTrace();
									return;
								}
								try
								{
									URLConnection con = aURL.openConnection();
									con.connect();
									InputStream is = con.getInputStream();
									/* Buffered is always good for a performance plus. */
									BufferedInputStream bis = new BufferedInputStream(is);
									/* Decode url-data to a bitmap. */
									Bitmap bm = BitmapFactory.decodeStream(bis);
									bis.close();
									is.close();
									/* Apply the Bitmap to the ImageView that will be
										returned. */
									iv.setImageBitmap(bm);
								}
								catch (IOException e)
								{
									Log.e("DEBUGTAG", "Remtoe Image Exception", e);
								}

							}
						}
						catch (JSONException e)
						{
							e.printStackTrace();
						}
					}

					memdata = full_name + date_entered + party;

					tv.setText(memdata);
					/* Grab Hansard Mentions */
					String hansurlstring = "http://www.openaustralia.org/api/getDebates" +
					"?key="+ oakey +
					"&type=representatives" +
					"&order=d" +
					"&person=" + personid;
					Log.i("OpenAusURL", hansurlstring);
					URL hansurl = new URL(hansurlstring);
					InputStream hansinstream;

					try
					{
						hansinstream = hansurl.openStream();
					}
					catch (IOException e)
					{
						e.printStackTrace();
						return;
					}
					try
					{
						String hansresult = Utilities.convertStreamToString(hansinstream);
						Log.i("GetResult",hansresult);
						JSONObject hansjson;
						try
						{
							hansjson = new JSONObject(hansresult);
						}
						catch(JSONException e)
						{
							e.printStackTrace();
							TextView exceptJSON = new TextView(context);
							exceptJSON.setId(1231);
							exceptJSON.setText(e.getMessage());
							tab.addView(exceptJSON);
							return;	
						}

						JSONArray hansresArray;
						try
						{
							hansresArray = hansjson.getJSONArray("rows");
						}
						catch (JSONException e)
						{
							// TODO Auto-generated catch block
							Log.i("Praeda", e.getMessage());
							e.printStackTrace();
							return;
						}
						//clear all old textviews
						tab.removeAllViewsInLayout();
						for(int i = 0; i < hansresArray.length();i++)
						{
							try
							{
								Log.d("hansresults", hansresArray.getString(i));
								JSONObject jray = hansresArray.getJSONObject(i);
								Log.d("hansresults", jray.getString("body"));
								TextView tvr = new TextView(context);
								tvr.setId(200+i);
								/*tvr.setLayoutParams(new LayoutParams(
			  		  						LayoutParams.WRAP_CONTENT,
			  		  						LayoutParams.WRAP_CONTENT));*/
								tvr.setText(jray.getString("body"));
								tab.addView(tvr);
							}
							catch(JSONException e)
							{
								Log.e("balls", e.toString());
							}
						}
					}
					catch (IOException e)
					{
						Log.e("searchRepFail", e.getMessage().toString());
					}
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
					instream.close();
				}
				catch(IOException e)
				{
					Log.e("searchRepFail", e.getMessage().toString());
				}
			}


		});
	}

	public void searchRepClickHandler(View target)
	{

	}

}
