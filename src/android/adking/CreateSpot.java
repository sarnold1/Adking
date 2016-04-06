package android.adking;

import java.util.HashMap;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class CreateSpot extends AppCompatActivity implements View.OnClickListener
{

	EditText titleSpot, descriptionSpot, contentSpot;
	
	Button saveSpotButton,abortButton; // Button to save a Spot, Button to Cancel
	
	// URL to PHP-Script, which writes the Title, Description and Content of a Spot into the Database
	private static final String CREATE_SPOT_URL = "http://mysqlandroid.bplaced.net/createSpot.php";
	
	// called, when Activity is opened
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		// set Layout of View
		setContentView(R.layout.create_spot);
		
		// Text Box to enter the Title
		titleSpot = (EditText) findViewById(R.id.titleSpot);
		// Text Box to enter the Description
		descriptionSpot = (EditText) findViewById(R.id.descriptionSpot);
		// Text Box to enter the Main Content (actual Offer)
		contentSpot = (EditText) findViewById(R.id.insertSpotText);
		
		saveSpotButton = (Button)findViewById(R.id.buttonSaveSpot);
		// add ClickListener 
		saveSpotButton.setOnClickListener(this);
		
		abortButton = (Button)findViewById(R.id.buttonAbort);
		// add ClickListener
		abortButton.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.create_spot, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) 
		{
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	// Method, called by ClickListener 
	@Override
	public void onClick(View v) 
	{
		// if Cancel-Button is clicked
       	if(v == abortButton)
       	{
       		// change Activity to StartScreen
       		startActivity(new Intent(CreateSpot.this, StartScreen.class));
       	}
       		
       	// if Save-Button is clicked
       	if(v == saveSpotButton)
       	{
       		// get Text from Title, Description and Content
       		saveSpot(titleSpot.getText().toString().trim(), descriptionSpot.getText().toString().trim(),
       	 			contentSpot.getText().toString().trim());
       	}
	 }	

	private void saveSpot(String titleSpot, String descriptionSpot, String contentSpot)
	{
		/*TextView textview = (TextView) findViewById(R.id.textView1);
		textview.setVisibility(View.VISIBLE);*/
		
		 // internal Class to save a Spot as async. Process
		 class CreateSpotClass extends AsyncTask<String,Void,String>
	        {
			 	// create Progress Dialog
	        	ProgressDialog loading;
	        	// new Instance of Class ConnectToDB
	        	ConnectToDB ctb = new ConnectToDB();
	        	// set up the login task, shows a progress dialog in the user interface
	            @Override
	            protected void onPreExecute() 
	            {
	                super.onPreExecute();
	                // open a Progress dialog
	                loading = ProgressDialog.show(CreateSpot.this, "Bitte warten", null,true,true);
	            }
	 
	            // invoked on the user interface after doInBackground is finished
	            @Override
	            protected void onPostExecute(String s) 
	            {
	                super.onPostExecute(s);
	                // lets ProgressDialog disappear
	                loading.dismiss();
	                /* compares String s with String "success"
	                 * the two String are considered equal, if the length is the same
	                 */
	                Intent intent = new Intent(CreateSpot.this, AccountOptions.class);
	                startActivity(intent);
	                /*if(s.equalsIgnoreCase("success"))
	                {
	                	// 
	                    Intent intent = new Intent(CreateSpot.this, AccountOptions.class);
	                    intent.putExtra(USER_NAME, username); // adds username to Intent intent
	                    startActivity(intent); // opens the Activity AccountOptions as new Window
	                }
	                else
	                {
	                	// shows feedback in a small popup
	                    Toast.makeText(CreateSpot.this, s, Toast.LENGTH_LONG).show();
	                }*/
	            }
	 
	            /* Background computation for login function
	         	   is executed after onPreExecute
	         	   to perform background computation, that takes a long time
	         	   takes transfer parameters of the asynchronous task 
	            */
	            @Override
	            protected String doInBackground(String... params)
	            {
	            	// create new Hash Map
	            	HashMap<String, String> data = new HashMap<String,String>();
	            	// add Title to Hash Map as first Parameter
	                data.put("title",params[0]);
	                // add Description to Hash Map as second Parameter
	                data.put("description",params[1]);
	                // add Content to Hash Map as third Parameter
	                data.put("content",params[2]);
	 
	                // upload Hash Map to URL (Create Spot) via POST
	                String result = ctb.sendPostRequest(CREATE_SPOT_URL,data);
	                // get Result of HTTP Connection
	                return result;
	            }
	        }
		 // new Instance of Class CreateSpotClass
         CreateSpotClass csc = new CreateSpotClass();
         // run all Methods of CreateSpotClass
         csc.execute(titleSpot, descriptionSpot, contentSpot);
	}
}
