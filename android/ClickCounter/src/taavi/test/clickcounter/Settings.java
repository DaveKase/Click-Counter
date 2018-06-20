package taavi.test.clickcounter;

import taavi.test.clickcounter.Database.Conf;
import taavi.test.clickcounter.Database.DatabaseHelper;
import android.app.Activity;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Settings extends Activity {

	/**
	 * Called when the activity is first created.
	 * 
	 * @param savedInstanceState
	 * */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.settings);
	}
	
	/**
	 * Called when OK button is clicked. Saves IP address from EditText to local database
	 * @param clickedButton Button that was clicked
	 */
	public void okClick(View clickedButton) {
		EditText ipEdit = (EditText) findViewById(R.id.editTextIp);
		String ip = ipEdit.getText().toString();
		String url = "http://" + ip + "/ClickCounter/getCount.php";
		
		DatabaseHelper dbh = new DatabaseHelper(this);
		SQLiteDatabase db = dbh.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(Conf.COL_ID, 1);
		values.put(Conf.COL_IP, url);
		
		db.delete(Conf.TABLE_NAME, null, null);
		db.insert(Conf.TABLE_NAME, null, values);
		
		Toast.makeText(this, "URL " + url + " saved", Toast.LENGTH_SHORT).show();

		db.close();
		dbh.close();
	}
}