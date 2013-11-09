package com.example.merchantv01;
import java.io.IOException;
import java.io.InputStream;
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
import org.xmlpull.v1.XmlPullParserException;
import android.os.AsyncTask;


public class HttpRequestTask extends AsyncTask<String, Void, List<Entry>> {

	private MainActivity activity;

	public HttpRequestTask(MainActivity activity) {
		this.activity = activity;
	}

	@SuppressWarnings("finally")
	@Override
	protected List<Entry> doInBackground(String... urls) {
		// params comes from the execute() call: params[0] is the url.
		List<Entry> result = null;
		try {
			result = loadXml(urls[0]);
		} catch (XmlPullParserException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	// onPostExecute displays the results of the AsyncTask.
	@Override
	protected void onPostExecute(List<Entry> result) {

		activity.progress.cancel();

	}

	@SuppressWarnings("unchecked")
	private List<Entry> loadXml(String urlString)
			throws XmlPullParserException, IOException {
		InputStream stream = null;
		List<Entry> entries = null;
		FeedParser parser = new FeedParser();
		try {
			stream = downloadUrl(urlString);
			entries = parser.parse(stream, activity);
			// Makes sure that the InputStream is closed after the app is
			// finished using it.
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		if (stream != null) {
			stream.close();
		}
		return entries;

	}

	

	private InputStream downloadUrl(String urlString) throws IOException,
			URISyntaxException {
		URL url = new URL(urlString);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		/*
		URI uri = new URI(urlString);
		HttpParams params = new BasicHttpParams();
		params.setParameter("key1", "value1");
		params.setParameter("key2", "value2");
		params.setParameter("key3", "value3");
		HttpRequestBase request = new HttpGet(uri);
		HttpClient httpClient = new DefaultHttpClient();
		httpClient.execute(request);
*/
		conn.setReadTimeout(10000 /* milliseconds */);
		conn.setConnectTimeout(15000 /* milliseconds */);
		conn.setRequestMethod("GET");
		conn.setDoInput(true);
		conn.setRequestProperty("Accept", "text/xml");
		// Starts the query
		conn.connect();
		return conn.getInputStream();
	}

}