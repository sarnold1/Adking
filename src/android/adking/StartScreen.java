/*
 * Quelle: https://www.androidpit.de/de/android/wiki/view/Android_Anf%C3%A4nger_Workshop
 * Zugriff am 11.12. um 11.26 Uhr 
 * http://developer.android.com/reference/android/widget/Button.html
 * Zugriff am 
 */

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

@SuppressWarnings("deprecation")
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class StartScreen extends ActionBarActivity implements OnClickListener 
{	
	// Buttons for Login and Register
	private Button loginButton;
	private Button registerButton;

	// called, when Activity is opened
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start_screen);
		// "btnLogin" from start_screen.xml
		loginButton = (Button)findViewById(R.id.btnLogin);
		// register Listener for loginButton
		loginButton.setOnClickListener(this);
		//"btnRegister" from start_screen.xml
		registerButton = (Button)findViewById(R.id.btnRegister);
		// register Listener for registerButton 
		registerButton.setOnClickListener(this);
	}
	
	// initialize Activity's standard Options Menu
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.start_screen, menu);
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
	
	// Method, called by ClickListener
	@Override
	public void onClick(View v) 
	{
		switch (v.getId()) 
	    {
			// if btnLogin on Startscreen is clicked
	       	case R.id.btnLogin:
	       		// opens Window "LoginScreen"
	       		startActivity(new Intent(StartScreen.this, LoginScreen.class));
	       		// leave switch clause
	       		break;
	       	
	       	// if btnRegister on Startscreen is clicked
	       	case R.id.btnRegister:
	       		// opens Window "RegisterScreen"
	       		startActivity(new Intent(StartScreen.this, RegisterScreen.class));
	       		// leave switch clause
	       		break;
	     }	
	}
}
