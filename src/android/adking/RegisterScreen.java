/* Quelle:
   https://www.simplifiedcoding.net/android-user-registration-tutorial/
   Zugriff am 04.01.16 um 10.35 Uhr
   https://www.simplifiedcoding.net/android-login-and-registration-with-php-mysql
   Zugriff am 04.01.16 um 17.06 Uhr
   www.mysamplecode.com/2011/10/android-generate-md5-hash.html
   Zugriff am 08.01.16 um 15.01 Uhr
   http://stackoverflow.com/questions/6290531/check-if-edittext-is-empty
   Zugriff am 25.01.16 um 13.56 Uhr 
 */
package android.adking;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

// Class to create a new account (for now "Werbeinteressent")
public class RegisterScreen extends AppCompatActivity implements View.OnClickListener
{
	// Button to Register a User and Cancel 
	Button buttonRegister, buttonAbortRegister;
	// Text Boxes for Name, Surname, Username, Password, and repeat Password
	EditText editName, editSurname, editUsername, editPassword, editRepeatpassword;
	
	// URL to PHP-Script, which creates a new Account in the Database
	private static final String REGISTER_URL = "http://mysqlandroid.bplaced.net/registerWerbeinteressent.php";
	
	// called when Activity RegisterScreen is opened
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		// set Layout of RegisterScreen
		setContentView(R.layout.registerscreen);
		
		editName = (EditText) findViewById(R.id.insertName);
		editSurname = (EditText) findViewById(R.id.insertSurname);
		editUsername = (EditText) findViewById(R.id.insertUsername);
		editPassword = (EditText) findViewById(R.id.insertPassword);
		editRepeatpassword = (EditText) findViewById(R.id.insertPasswordRepeat);
		
		buttonRegister = (Button)findViewById(R.id.btnRegister);
		// add ClickListener
		buttonRegister.setOnClickListener(this);
		
		buttonAbortRegister = (Button)findViewById(R.id.btnAbortRegister);
		// add ClickListener
		buttonAbortRegister.setOnClickListener(this);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register_screen, menu);
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
		// if registerbutton is clicked
		if(v == buttonRegister)
		{
			// call Method registerWerbeinteressent
			registerWerbeinteressent();
		}
		// if Abortbutton is clicked
		if(v == buttonAbortRegister)
		{
			// change Activity to StartScreen
			startActivity(new Intent(RegisterScreen.this, StartScreen.class));
		}
	}
	
	// Method to create a new account
	private void registerWerbeinteressent()
	{
		// get Text from form field "insertSurname" (registerscreen.xml)
		String surname = editSurname.getText().toString().trim();
		// get Text from form field "insertName" (registerscreen.xml)
		String name = editName.getText().toString().trim();
		// get Text from form field "insertUsername" (registerscreen.xml)
		String username = editUsername.getText().toString().trim();
		// get Text from form field "insertPassword" (registerscreen.xml)
		String password = editPassword.getText().toString().trim();
		// get Text from form field "insertPasswordRepeat" (registerscreen.xml)
		String repeatPassword = editRepeatpassword.getText().toString().trim();

		// check if all fields are empty
		if(TextUtils.isEmpty(surname) && TextUtils.isEmpty(name) && TextUtils.isEmpty(username) && TextUtils.isEmpty(password))
		{
			// show Message if all fields are empty
			Toast allFieldsAreEmpty = Toast.makeText(RegisterScreen.this, "Bitte alle Felder ausfüllen", Toast.LENGTH_LONG);
			allFieldsAreEmpty.show();
			return;
		}
		
		// if surname is empty
		else if(TextUtils.isEmpty(surname))
		{
			// show Message if Surname is missing
			editSurname.setError("Nachname fehlt");
			return;
		}
		// if name is empty
		else if(TextUtils.isEmpty(name))
		{
			// show Message if Name is missing
			editName.setError("Vorname fehlt");
			return;
		}
		// if username is empty
		else if(TextUtils.isEmpty(username))
		{
			// show Message if Username is missing
			editUsername.setError("Benutzername fehlt");
			return;
		}
		// if password is empty
		else if(TextUtils.isEmpty(password))
		{
			// show Message if Password is missing
			editPassword.setError("kein Passwort festgelegt");
			return;
		}
		
		else
		{
			// call Method md5 to encrypt Text of password and repeatPassword
			String md5Password = md5(password);
			String md5repeatPassword = md5(repeatPassword);
			
			// if md5Password and md5repeatPassword are not equal
			// display an error message
			if(!md5Password.equals(md5repeatPassword))
			{
				Toast passwordNotIdentical = Toast.makeText(RegisterScreen.this, "Passwörter stimmen nicht überein", Toast.LENGTH_SHORT);
				passwordNotIdentical.show();
			}
			else
				// call Method register to write account data, inserted into form fields, in the table Werbeinteressent
				register(surname, name, username, md5Password);
		}
	}
 
	// Method to write User Input into Database, when a new Account is created by the User 
    private void register(String surname, String name, String username, String password) 
    {
        class RegisterUser extends AsyncTask<String, Void, String>
        {
        	// create new Progress Dialog
            ProgressDialog loading;
            // new Instance of Class ConnectToDB
            ConnectToDB ctb = new ConnectToDB();
 
            // Method, called before Background Process is running
            @Override
            protected void onPreExecute() 
            {
                super.onPreExecute();
                // show Progress Dialog
                loading = ProgressDialog.show(RegisterScreen.this, "Bitte warten",null, true, true);
            }
 
            // Method, called after Background Process is finished
            @Override
            protected void onPostExecute(String s)
            {
                super.onPostExecute(s);
                // remove Progress Dialog
                loading.dismiss();
                //Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
                // change Activity to LoginScreen
                startActivity(new Intent(RegisterScreen.this, LoginScreen.class));
            }

            // Background Process to write Account Data into Database
            @Override
            protected String doInBackground(String... params) 
            {
            	// create new Hash Map
                HashMap<String, String> data = new HashMap<String,String>();
                // add Surname as first Parameter
                data.put("surname",params[0]);
                // add Name as second Parameter
                data.put("name",params[1]);
                // add Username as third Parameter
                data.put("username",params[2]);
                // add Password as fourth Parameter
                data.put("password",params[3]);
 
                // upload Hash Map to URL (Register) via POST 
                String result = ctb.sendPostRequest(REGISTER_URL,data);
                // get Result of HTTP Connection
                return result;
            }
        }
 
        // new Instance of internal Class RegisterUser 
        RegisterUser ru = new RegisterUser();
        // run all Method of RegisterUser
        ru.execute(surname,name,username,password);
    }
    
    // Method to encrypt Text of password (Registration)
 	private final String md5(final String s)
 	{	
 		try
 		{
 			// create MD5 Hash
 			MessageDigest md = MessageDigest.getInstance("MD5");
 			// convert Text from passwords into Bytes
 			md.update(s.getBytes());
 			byte messageDigest[] = md.digest();
 		
 			// create Hex String
 			StringBuffer hexString = new StringBuffer();
 			for(int i = 0; i < messageDigest.length; i++)
 			{
 				hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
 			}
 			return hexString.toString();
 		}
 		catch(NoSuchAlgorithmException e)
 		{
 			e.printStackTrace();
 		}
 		return "";
 	}
}
