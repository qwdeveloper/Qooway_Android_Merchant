package com.example.merchantv01;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;
import android.os.AsyncTask;
import android.widget.Toast;

public class HttpPostTask extends AsyncTask<String, Void, String> {

	private MainActivity activity;

	public HttpPostTask(MainActivity activity) {
		this.activity = activity;
	}

	@SuppressWarnings("finally")
	@Override
	protected String doInBackground(String... urls) {
		// params comes from the execute() call: params[0] is the url.
		String resultCode = null;
		try {
			resultCode = sendJson(urls);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return resultCode;
	}

	// onPostExecute displays the results of the AsyncTask.
	@Override
	protected void onPostExecute(String result) {

		if(activity.progress!=null)
			activity.progress.cancel();
	}


	private String sendJson(String[] urls) throws IOException,URISyntaxException {
		JSONObject jsonObjSend = null;

		try {
			jsonObjSend = new JSONObject(urls[1]);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String resultt = jsonObjSend.toString();
		HttpURLConnection httpcon = (HttpURLConnection) ((new URL(urls[0]).openConnection()));
		httpcon.setDoOutput(true);
		httpcon.setRequestProperty("Accept", "application/json");
		httpcon.setRequestProperty("Content-Type", "application/json");
		httpcon.setRequestMethod("POST");
		httpcon.connect();
		byte[] outputBytes = jsonObjSend.toString().getBytes("UTF-8");
		OutputStream os = httpcon.getOutputStream();
		os.write(outputBytes);
		os.close();

		return httpcon.getResponseMessage();
	}

	

}