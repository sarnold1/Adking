/*
 * Quelle:
 * www.mkyong.com/android/android-custom-dialog-example/
 * Zugriff am 11.12. um 14.04 Uhr
 * stackoverflow.com/questions/6290531/check-if-edittext-is-empty
 * Zugriff am 11.12. um 15.15 Uhr
 * http://mrbool.com/how-to-create-an-android-user-login-system-using-mysql/31512
 * Zugriff am 14.12.15 um 17.17 Uhr
 * www.mybringback.com/android-sdk/13130/android-mysql-register-and-login-php-scripts/
 * Zugriff am 14.12.15 um 17.27 Uhr
 * http://www.mybringback.com/android-sdk/13193/android-mysql-php-json-part-5-developing-the-android-application/
 * Zugriff am 14.12.15 um 17.46 Uhr
 * https://www.youtube.com/watch?v=7jmK_Pd-4BU
 * Zugriff am 21.12.15 um 16.32 Uhr
 * http://www.tutorialspoint.com/android/android_php_mysql.htm
 * Zugriff am 02.01.16 um 15.00 Uhr
 * https://www.simplifiedcoding.net/android-mysql-tutorial-android-login-using-php-mysql
 * Zugriff am 07.01.16 um 13.10 Uhr
 * www.mysamplecode.com/2011/10/android-generate-md5-hash.html
 * Zugriff am 08.01.16 um 15.01 Uhr
 */

package android.adking;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

// Class for Login-Function
public class LoginScreen extends Activity implements OnClickListener
{
    public static final String USER_NAME = "USER_NAME";
    public static final String PASSWORD = "PASSWORD";
 
    // URL to PHP-Script for Login
    private static final String LOGIN_URL = "http://mysqlandroid.bplaced.net/loginWerbeinteressent.php";
 
    private EditText editTextUserName;
    private EditText editTextPassword;
 
    private Button buttonLogin;
    private Button buttonAbortLogin;
 
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        // set Layout of LoginScreen
        setContentView(R.layout.loginscreen);
 
        // assignment to loginUsername (Inputfield) of loginscreen.xml
        editTextUserName = (EditText) findViewById(R.id.loginUsername);
        
        // assignment to loginPassword (Inputfield) of loginscreen.xml
        editTextPassword = (EditText) findViewById(R.id.loginPassword);
        
        // assignment to buttonLogin of loginscreen.xml
        buttonLogin = (Button) findViewById(R.id.buttonLogin);
        // register Eventlistener for buttonLogin, sets buttonLogin as clickable
        buttonLogin.setOnClickListener(this);
        
        // assignment to loginAbortButton of loginscreen.xml
        buttonAbortLogin = (Button) findViewById(R.id.buttonAbortLogin);
        // register Eventlistener for buttonAbortLogin, sets buttonAbortLogin as clickable
        buttonAbortLogin.setOnClickListener(this);
    }
    
    // Eventlistener for loginbutton and logoutbutton
    @Override
    public void onClick(View v) 
    {
    	// if buttonLogin is clicked
        if(v == buttonLogin)
        {
        	login(); // calls method login
        }
        // if buttonAbortLogin is clicked
        if(v == buttonAbortLogin)
        {
        	// change from Activity LoginScreen to StartScreen
        	startActivity(new Intent(LoginScreen.this, StartScreen.class));
        }
    }
 
    private void login()
    {
    	// get Text from loginUsername and loginPassword and convert it to String
        String username = editTextUserName.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        
        // check if both fields (username and password) are empty
        if(TextUtils.isEmpty(username) && TextUtils.isEmpty(password))
        {
        	// show Message if both fields are empty
        	Toast usernameAndpwMissing = Toast.makeText(LoginScreen.this, "Bitte beide Felder ausfüllen", Toast.LENGTH_LONG);
        	usernameAndpwMissing.show();
        	return;
        }
        
        // check if only username is missing
        else if(TextUtils.isEmpty(username))
    	{
    		editTextUserName.setError("Benutzername fehlt");
    		return;
    	}
        // check if only password is missing
        else if(TextUtils.isEmpty(password))
    	{
    		editTextPassword.setError("Passwort fehlt");
    		return;
    	}
        
        else
        {
        	// calls Method md5 to encrypt Text of password
        	String md5Password = md5(password);
        	
        	// calls Method userLogin with username and md5Password
        	userLogin(username,md5Password);
        }
    }
 
    // Method for Login, takes username and a MD5-Hash of the password as transfer parameter
    private void userLogin(final String username, final String password)
    {
        class UserLoginClass extends AsyncTask<String,Void,String>
        {
        	// create new Progress Dialog
            ProgressDialog loading;
            @Override
            // Method runs before the Background Process
            protected void onPreExecute() 
            {
                super.onPreExecute();
                // show Progress Dialog to the User, before the actual Login Process will be started
                loading = ProgressDialog.show(LoginScreen.this,"Bitte warten",null,true,true);
            }
 
            // Method, called after Background Process is finished
            @Override
            protected void onPostExecute(String s) 
            {
                super.onPostExecute(s);
                // remove Progress Dialog
                loading.dismiss();
                // create new Activity
                Intent loginIntent = new Intent(LoginScreen.this,AccountOptions.class);
                // add username as additional Content to the Activity
                loginIntent.putExtra(USER_NAME,username);
                // open Activity
                startActivity(loginIntent);
            }
            
            // actual Login Process running in Background
            @Override
            protected String doInBackground(String... params) 
            {
            	// create Hash Map with two Parameters
                HashMap<String,String> data = new HashMap<String,String>();
                // add Username to Hash Map
                data.put("username",params[0]);
                // add Password to Hash Map
                data.put("password",params[1]);
 
                // new Instance of Class ConnectToDB
                ConnectToDB ctb = new ConnectToDB();
 
                // upload Hash Map to URL (Login) via POST
                String result = ctb.sendPostRequest(LOGIN_URL,data);
                // get Result of HTTP Connection
                return result;
            }
        }
        // new Instance of internal Class UserLoginClass
        UserLoginClass ulc = new UserLoginClass();
        // run all Methods of UserLoginClass
        ulc.execute(username,password);
    }
    
    // method to encrypt Text of password (Login)
  	private static String md5(String s)
  	{	
  		try
  		{
  			// create MD5 Hash
  			MessageDigest digest = MessageDigest.getInstance("MD5");
  			// convert Text from passwords into Bytes
  			digest.update(s.getBytes());
  			byte messageDigest[] = digest.digest();
  		
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