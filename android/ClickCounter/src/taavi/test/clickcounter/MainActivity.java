package taavi.test.clickcounter;

import java.io.IOException;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import taavi.test.clickcounter.Database.Conf;
import taavi.test.clickcounter.Database.DatabaseHelper;

public class MainActivity extends Activity {
	private static final String TAG = "MainActivity";
	private String mUrl;

	/**
	 * Called when the activity is first created.
	 * 
	 * @param savedInstanceState
	 * */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mUrl = getUrl();
	}
	
	/**
	 * Called when any of the four buttons was clicked
	 * 
	 * @param clickedButton Button that was clicked
	 */
	public void buttonsClicked(final View clickedButton) {
		new Thread("POST") {
			public void run() {
				try {
					JSONObject jObject = createJson(clickedButton.getId());
					sendJson(jObject);
				} catch(JSONException je) {
					Log.e(TAG, "JSON error: " + je.getMessage());
				} catch (ClientProtocolException cpe) {
					Log.e(TAG, "Client protocol error: " + cpe.getMessage());
					Toast.makeText(MainActivity.this, "Problem with connecting to server", Toast.LENGTH_SHORT).show();
				} catch (IOException ioe) {
					Log.e(TAG, "Input error: " + ioe.getMessage());
				}
			}
		}.start();
	}
	
	/**
	 * Creates JSON object and adds button ID to that object
	 * 
	 * @param buttonId Tells which button ID to add to JSON object
	 * @throws JSONException
	 */
	private JSONObject createJson(int buttonId) throws JSONException {
		JSONObject jObject = new JSONObject();
		
		switch(buttonId) {
		case R.id.buttonBlue:
			jObject.put("btn_id", "blue");
			break;
		case R.id.buttonGreen:
			jObject.put("btn_id", "green");
			break;
		case R.id.buttonRed:
			jObject.put("btn_id", "red");
			break;
		case R.id.buttonYellow:
			jObject.put("btn_id", "yellow");
			break;
		}
		
		return jObject;
	}
	
	/**
	 * Sends JSON object containing button id to server
	 * 
	 * @param jObject JSON object to send
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	private void sendJson(JSONObject jObject) throws ClientProtocolException, IOException {
		String content = jObject.toString();
		HttpClient httpclient = new DefaultHttpClient();
		HttpPost httpPost = new HttpPost(mUrl);
		
		StringEntity se = new StringEntity(content);
		httpPost.setEntity(se);
		httpPost.setHeader("Accept", "application/json");
        httpPost.setHeader("Content-type", "application/json");
        
        HttpResponse httpResponse = httpclient.execute(httpPost);
        HttpEntity entity = httpResponse.getEntity();
		EntityUtils.toString(entity, "UTF-8");
	}
	
	/**
	 * Gets URL address from local database
	 */
	private String getUrl() {
		String table = Conf.TABLE_NAME;
		String[] projection = {Conf.COL_IP};
		
		DatabaseHelper dbh = new DatabaseHelper(MainActivity.this);
		SQLiteDatabase db = dbh.getReadableDatabase();
		
		Cursor cursor = db.query(table, projection, null, null, null, null, null);
		cursor.moveToPosition(0);
		
		db.close();
		dbh.close();
		return cursor.getString(0);
	}

	/**
	 * Creates and inflates menu
	 * 
	 * @param menu
	 * @return true
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * If menu item is selected
	 * 
	 * @param item - item that was selected
	 * @return true
	 */
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_settings:
			Intent intent = new Intent(this, Settings.class);
			startActivity(intent);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}