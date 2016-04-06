/* Quelle:
 * https://www.simplifiedcoding.net/android-login-and-registration-with-php-mysql
 * Zugriff a, 04.01.16 um 17.06 Uhr
 */
package android.adking;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

// Class to open a Connection to the Database
public class ConnectToDB 
{ 
	// Method to upload a Hash Map as Parameter to a URL via "POST" to register an User
    public String sendPostRequest(String requestURL, HashMap<String, String> postDataParams) 
    {
        URL url;
        // set Response from Webserver as empty 
        String response = "";
        try 
        {
        	// create new URL for a Request
            url = new URL(requestURL);
 
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // max. waiting Time for Input-Stream, before the Connection will be closed in Milliseconds
            conn.setReadTimeout(15000);
            // max. waiting Time while Connection is built up in Milliseconds 
            conn.setConnectTimeout(15000);
            // sends the preferred Transfer Method to the Webserver
            conn.setRequestMethod("POST");
            // HTTP Connection allows Input
            conn.setDoInput(true);
            // HTTP Connection allows Output
            conn.setDoOutput(true);
 
            // fetch Output-Stream from HTTP Connection
            OutputStream os = conn.getOutputStream();
            // wraps OutputStreamWriter 
            BufferedWriter writer = new BufferedWriter(
            		//write Output-Stream, converted into "UTF-8" in BufferedWriter
                    new OutputStreamWriter(os, "UTF-8"));
            // write Post-Data as String into BufferedWriter
            writer.write(getPostDataString(postDataParams));
            // flush the BufferedWriter
            writer.flush();
            // close the BufferedWriter
            writer.close();
            // close OutputStreamWriter
            os.close();
            // fetch Response Code from HTTP Connection
            int responseCode=conn.getResponseCode();
            
            // if Response Code is "OK" or Status Code 200
            if (responseCode == HttpsURLConnection.HTTP_OK) 
            {
            	// fetch Input Stream from HTTP Connection, wraps InputStreamReader into BufferedReader
                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
                // return the next Line of BufferedReader
                response = br.readLine();
            }
            else 
            {
            	// register an User failed
                response="Error Registering";
            }
        } 
        catch (Exception e) 
        {
        	// write printable Presentation of Exception to Error Channel of System
            e.printStackTrace();
        }
        // return Response
        return response;
    }
  
    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException 
    {
    	// create new custom, async StringBuffer
        StringBuilder result = new StringBuilder();
        boolean first = true;
        
        for(Map.Entry<String, String> entry : params.entrySet())
        {
            if (first)
                first = false;
            else
            	// add the Character "&" to StringBuilder, if there is at least two Key-Value-Pairs 
                result.append("&");
 
            // fetch Key-Parameter from Map, encoded in "UTF-8" and add it to StringBuilder
            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            // add the Character "=" to StringBuilder
            result.append("=");
            // fetch Value-Parameter from Map, encoded in "UTF-8" and add it to StringBuilder 
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }
        // return Content of StringBuilder as String
        return result.toString();
    }
}
