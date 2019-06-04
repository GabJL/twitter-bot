package es.uma.informatica.rysd.twitter.modelo;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.security.GeneralSecurityException;
import java.util.Calendar;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;

import com.google.gson.Gson;

import es.uma.informatica.rysd.twitter.entidades.Media;
import es.uma.informatica.rysd.twitter.entidades.User;

import java.util.Base64;


public class Twitter {
	
	
	static final private String consumerKey = Claves.consumerKey;
	static final private String consumerSecret = Claves.consumerSecret;
	static final private String oauthToken = Claves.oauthToken;
	static final private String oauthSecret = Claves.consumerSecret;
	
	private Gson gson = new Gson();

	private String encode(String value) 
	{
        String encoded = null;
        try {
            encoded = URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException ignore) {
        }
        StringBuilder buf = new StringBuilder(encoded.length());
        char focus;
        for (int i = 0; i < encoded.length(); i++) {
            focus = encoded.charAt(i);
            if (focus == '*') {
                buf.append("%2A");
            } else if (focus == '+') {
                buf.append("%20");
            } else if (focus == '%' && (i + 1) < encoded.length()
                    && encoded.charAt(i + 1) == '7' && encoded.charAt(i + 2) == 'E') {
                buf.append('~');
                i += 2;
            } else {
                buf.append(focus);
            }
        }
        return buf.toString();
    }
	
	private String computeSignature(String baseString, String keyString) throws GeneralSecurityException, UnsupportedEncodingException 
	{
	    SecretKey secretKey = null;

	    byte[] keyBytes = keyString.getBytes();
	    secretKey = new SecretKeySpec(keyBytes, "HmacSHA1");

	    Mac mac = Mac.getInstance("HmacSHA1");
	    mac.init(secretKey);

	    byte[] text = baseString.getBytes();

	    String s = Base64.getEncoder().encodeToString(mac.doFinal(text));

	    return s;
	}

	private String buildAuthorization(String get_or_post, String url, SortedMap<String, String> params)
	{
		// Authorization parameters
		
		// Signature method
		String oauth_signature_method = "HMAC-SHA1";	
		
		// Nonce
		String uuid_string = UUID.randomUUID().toString();
		uuid_string = uuid_string.replaceAll("-", "");
		String oauth_nonce = uuid_string; // any relatively random alphanumeric string will work here
		
		// get the timestamp
		Calendar tempcal = Calendar.getInstance();
		long ts = tempcal.getTimeInMillis();// get current time in milliseconds
		String oauth_timestamp = (new Long(ts/1000)).toString(); // then divide by 1000 to get seconds
		
		// the parameter string must be in alphabetical order
		String pre = "", post = "";
		
		for(Map.Entry<String,String> entry : params.entrySet()) {
			  String key = entry.getKey();
			  String value = entry.getValue();
			  
			  if(key.compareTo("oauth") < 0)
			  {
				  if(!pre.isEmpty())	pre +="&";
				  pre += key + "=" + encode(value);
			  }
			  else 
			  {
				  post += "&"+key + "=" + encode(value);
			  }
		}
		
		if(!pre.isEmpty()) pre +="&";
		

		String parameter_string = pre + "oauth_consumer_key=" + consumerKey + "&oauth_nonce=" + oauth_nonce + "&oauth_signature_method=" + oauth_signature_method + 
			"&oauth_timestamp=" + oauth_timestamp + "&oauth_token=" + encode(oauthToken) + "&oauth_version=1.0"
			+ post;	
		String signature_base_string = get_or_post + "&"+ encode(url) + "&" + encode(parameter_string);
		//		System.out.println("signature_base_string=" + signature_base_string);
		
		// this time the base string is signed using twitter_consumer_secret + "&" + encode(oauth_token_secret) instead of just twitter_consumer_secret + "&"
		String oauth_signature = "";
		try {
			oauth_signature = computeSignature(signature_base_string, encode(consumerSecret) + "&" + encode(oauthSecret));  // note the & at the end. Normally the user access_token would go here, but we don't know it yet for request_token
		} catch (GeneralSecurityException e) {
			e.printStackTrace();
		}
		catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		String authorization_header_string = "OAuth oauth_consumer_key=\"" + consumerKey + "\", oauth_nonce=\"" + oauth_nonce + "\", oauth_signature=\"" + encode(oauth_signature) + "\", oauth_signature_method=\"HMAC-SHA1\", oauth_timestamp=\"" + oauth_timestamp + "\", oauth_token=\"" + encode(oauthToken) + "\""+ ", oauth_version=\"1.0\"";
		//		System.out.println("authorization_header_string=" + authorization_header_string);

		return authorization_header_string;
	}
	
	public String getUserID(String user) throws Exception
	{
		String method = "GET";
		String twitter_endpoint = "https://api.twitter.com/1.1/users/show.json";
		
		SortedMap<String, String> parameters = new TreeMap<String, String>(); 
		parameters.put("screen_name",user);
		
		String urlStr = twitter_endpoint+"?screen_name="+encode(user);
		URL request = new URL(urlStr);
		
		HttpsURLConnection connection =  (HttpsURLConnection) request.openConnection();
		connection.setRequestProperty("Authorization", buildAuthorization(method, twitter_endpoint, parameters ));
        connection.setRequestMethod("GET");
        		
        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
            User u = gson.fromJson(new InputStreamReader(connection.getInputStream()),User.class);
            return u.id_str;
        }
        else
        {
            throw new RuntimeException ("Response code "+connection.getResponseCode()+ " "+
                    connection.getResponseMessage());
        }
	}
	
	public void sendDM(String user, String msg)
	{
		String method = "POST";
		String twitter_endpoint = "https://api.twitter.com/1.1/direct_messages/events/new.json";

		
		SortedMap<String, String> parameters = new TreeMap<String, String>(); 		
		String urlStr = twitter_endpoint;
		URL request = null;
		HttpsURLConnection connection;
		try {
			request = new URL(urlStr);
			connection = (HttpsURLConnection) request.openConnection();
			connection.setRequestProperty("Authorization", buildAuthorization(method, twitter_endpoint, parameters ));
	        connection.setRequestMethod("POST");
	        connection.setRequestProperty("Content-type", "application/json");

	        connection.setDoOutput(true);
	        String json = "{\"event\": {\"type\": \"message_create\", \"message_create\": {\"target\": {\"recipient_id\": \""
	        		+ user +"\"}, \"message_data\": {\"text\": \""+ msg + "\"}}}}";
	        OutputStream out = connection.getOutputStream();
	        out.write(json.getBytes(StandardCharsets.UTF_8.name()));
	        out.close();
	        			
	        if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
	            throw new RuntimeException ("Response code "+connection.getResponseCode()+ " "+
	                    connection.getResponseMessage());
	        }
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String UploadFile(String filename)
	{
		String method = "POST";
		String twitter_endpoint = "https://upload.twitter.com/1.1/media/upload.json";
		SortedMap<String, String> parameters = new TreeMap<String, String>(); 		
		
		String urlStr = twitter_endpoint;
		URL request = null;
		HttpsURLConnection connection;
		try {
			request = new URL(urlStr);
			//System.out.println(request.toString());
			connection = (HttpsURLConnection) request.openConnection();
			connection.setRequestProperty("Authorization", buildAuthorization(method, twitter_endpoint, parameters ));
	        connection.setRequestMethod("POST");
	        String boundary =  "*****";
	        connection.setRequestProperty("Content-type", "multipart/form-data; boundary=" + boundary);
	        connection.setDoOutput(true);
	        OutputStream out = connection.getOutputStream();
	        
	        // Uploading file
	        
	        // Read file:
	        InputStream inputStream;
	        byte[] allBytes = null;
			try {
				inputStream = new FileInputStream(filename);
		        long fileSize = new File(filename).length();
		        allBytes = new byte[(int) fileSize];
		        inputStream.read(allBytes);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			// Creating multipart content
	        String crlf = "\r\n";
	        String twoHyphens = "--";
	        out.write((twoHyphens + boundary + crlf).getBytes());
	        out.write(("Content-Disposition: form-data; name=\"media\";filename=\"" +  filename + "\"" + crlf).getBytes());
	        out.write(crlf.getBytes());
	        out.write(allBytes);
	        out.write(crlf.getBytes());
	        out.write((twoHyphens + boundary +twoHyphens + crlf).getBytes());    
	        out.close();
	        			
	        if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
	            throw new RuntimeException ("Response code "+connection.getResponseCode()+ " "+
	                    connection.getResponseMessage());
	        } else {
	        	Media m = gson.fromJson(new InputStreamReader(connection.getInputStream()) , Media.class);
	        	return m.media_id_string;
	        }
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public void sendTweet(String msg, String media_id)
	{
		String method = "POST";
		String twitter_endpoint = "https://api.twitter.com/1.1/statuses/update.json";

		
		SortedMap<String, String> parameters = new TreeMap<String, String>(); 		
		parameters.put("status",msg);
		String urlStr = twitter_endpoint+"?status="+encode(msg);
		if(media_id != null){
			parameters.put("media_ids", media_id);
			urlStr += "&media_ids="+media_id;
		}
		URL request = null;
		HttpsURLConnection connection;
		try {
			request = new URL(urlStr);
			connection = (HttpsURLConnection) request.openConnection();
			connection.setRequestProperty("Authorization", buildAuthorization(method, twitter_endpoint, parameters ));
	        connection.setRequestMethod("POST");

	        connection.setDoOutput(false);
	        			
	        if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
	            throw new RuntimeException ("Response code "+connection.getResponseCode()+ " "+
	                    connection.getResponseMessage());
	        }
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
