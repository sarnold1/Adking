package android.adking;

import android.support.v7.app.ActionBarActivity;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

@SuppressWarnings("deprecation")
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
// Class which contains all options the User (Werbeinteressent) can choose
public class AccountOptions extends ActionBarActivity implements OnClickListener 
{
	private TextView showUserLoggedIn;
	private Button createSpotButton;
	private Button logoutButton;
	EditText editName, editSurname, editUsername;
	
	// called, when Activity is opened
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		// set Layout of AccountOption
		setContentView(R.layout.account_options);
		
		// display username of the logged in User transfered from LoginScreen.java
		showUserLoggedIn = (TextView) findViewById(R.id.textViewLoggedInUser);
		// return started Intent
        Intent intent = getIntent();
        // fetch Username from LoginScreen.java
        String username = intent.getStringExtra(LoginScreen.USER_NAME);
        // display Username in TextView
        showUserLoggedIn.setText(username);
		
        // Text Box to enter Username
		editUsername = (EditText) findViewById(R.id.loginUsername);
		// Text Box to enter Name
		editName = (EditText) findViewById(R.id.insertName);
		// Text Box to enter Surname
		editSurname = (EditText) findViewById(R.id.insertSurname);
		
		logoutButton = (Button)findViewById(R.id.buttonLogout); // assignment to Button "buttonLogout" (accountOptions.xml)
		// register Eventlistener for logoutButton		
		logoutButton.setOnClickListener(this);
		createSpotButton = (Button)findViewById(R.id.buttonCreateSpot); // assignment to Button "buttonCreateSpot" (accountOptions.xml)
		// register Eventlistener for Button "createSpotButton"		
		createSpotButton.setOnClickListener(this);
	}		
	
	// Method, called by Clicklistener
	@Override
	public void onClick(View v)
	{
		switch(v.getId())
		{
			// if Logout-Button is clicked
			case R.id.buttonLogout:
				//change Activity to LoginScreen
				startActivity(new Intent(AccountOptions.this, LoginScreen.class));
				// leave switch clause
				break;
				
			// if Create-Spot-Button is clicked
			case R.id.buttonCreateSpot:
				//change Activity to CreateSpot
				startActivity(new Intent(AccountOptions.this, CreateSpot.class));
				// leave switch clause
				break;
		}				
	}

	// initialize Activity's standard Options Menu
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.account_options, menu);
		return true;
	}

	// called every time, when an Item of the Options menu is selected
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

	protected void onStart()
	{
		super.onStart();
	}
	
}