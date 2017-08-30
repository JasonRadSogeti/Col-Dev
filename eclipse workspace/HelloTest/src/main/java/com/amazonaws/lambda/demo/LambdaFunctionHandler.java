package com.amazonaws.lambda.demo;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;


import javax.net.ssl.HttpsURLConnection;

import org.json.simple.JSONObject;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class LambdaFunctionHandler implements RequestHandler<Object, String> {

    @Override
    public String handleRequest(Object input, Context context) {
        context.getLogger().log("Input: " + input);
        String returnString = "";
        
        try {
        	String httpsURL = "https://fcm.googleapis.com/fcm/send";
        	

            //String query = "email="+ URLEncoder.encode("abc@xyz.com","UTF-8"); 
            //query += "&";
            //query += "password="+URLEncoder.encode("abcd","UTF-8") ;
        	
        	//String payload = "{\"notification\": {\"title\": \"Server Notification\",\"body\": \"Good progress!\"},\"to\": \"cMJMMs-Z8bI:APA91bHsQnsPLZNh2Zd2BdJbO_ZwbLvohrxZ8lKKtoR7Ts30bEsgS3WLPcmDUcNMaEH-KDRZ_1jFIHmo_Y44vPKtO4oD2uRSK-UGTpdekA-1f8199t5oJjuJwG7ghOEuXrIlhoXjHqUZ\"}";
        	JSONObject jspayload = new JSONObject();
        	
        	JSONObject jsnotification = new JSONObject();
        	JSONObject dataObject = new JSONObject();
        	
        	//Data to pass to the phone if the app is in the foreground
        	//**********************************************************************************
        	dataObject.put("title", "Sales Page Notification");
        	dataObject.put("text", "Your website was just viewed!");
        	dataObject.put("ChannelName", "Sales Webpage");
        	//**********************************************************************************
        	
        	//jsnotification.put("title", "Website Notification");
        	//jsnotification.put("body", "Your website was just viewed!");
        	//jspayload.put("notification", jsnotification);
        	//jspayload.put("sound", "bloop11");
        	jspayload.put("data", dataObject);
        	//jspayload.put("to", "d3hsAGHbKy8:APA91bHE8-uGXPNXClfTUpIQyASd2wT48AF6gdjnEEpX5KYoq5G1UOBSOU_oXEVrA1J_9L00C7pBVZEWGiDH5Aa-kXtUmzE-kGcBzmTG2ZGNBgWYQXOhTA3Avi2hQGvI");
        	jspayload.put("to", "cH8ipNUpZPg:APA91bEE12Oa3iRHm0XlRz0AeUCYLtze3R1l_b38BUejIzUfVCPnm_G5_Uhi3E1LLnoqhHTIFivkvjCmbS-OvtBBAk-DuB2HsTLqJU3fuyGnb6iNlJS2uzN4GuL74");
        	//jspayload.put("to","/topics/SogetiNotifier");
        	
        	
        	System.out.println("|" + jspayload.toString() +"|");
        	returnString+=jspayload.toString();
        	//System.out.println("|" + String.valueOf(jspayload.toString().length()) + "|");
        	
        	//returnString+=payload;
            URL myurl = new URL(httpsURL);
            HttpsURLConnection con = (HttpsURLConnection)myurl.openConnection();
            con.setRequestMethod("POST");

            con.setRequestProperty("Content-length", String.valueOf(jspayload.toString().length())); 
            con.setRequestProperty("Content-Type","application/json"); 
            con.setRequestProperty("Authorization", "key=AAAAr9MbJRo:APA91bGOqYFPSk2QLWoo9SFGwVtHwMIbQjgWfYWSYQfPrKLC_s7iw8RaeaRYWh7a7OaDzvwZG6kYrCPqT08vhnL8kOXFQUI3pydjQNXDLZVG6VLFHSgSmCGBvPxo");
            con.setRequestProperty("Accept", "*/*");
            con.setRequestProperty("Accept-Encoding", "gzip, deflate");
            //con.setRequestProperty("User-Agent", "runscope/0.1");
            con.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 5.0;Windows98;DigExt)"); 
            con.setDoOutput(true); 
            con.setDoInput(true); 

            DataOutputStream output = new DataOutputStream(con.getOutputStream());  


            output.writeBytes(jspayload.toString());

            output.close();

            DataInputStream inputStream2 = new DataInputStream( con.getInputStream() ); 
            ByteArrayInputStream inputStream = new ByteArrayInputStream("test".getBytes());


            for( int c = inputStream.read(); c != -1; c = inputStream.read() ) 
            System.out.print( (char)c ); 
            inputStream.close(); 

            System.out.println("Resp Code:"+con .getResponseCode()); 
            System.out.println("Resp Message:"+ con .getResponseMessage()); 
            
            
        }
        catch(IOException ioe) {
        	System.out.println("error");
        }
        
        
        // TODO: implement your handler
        return (returnString + "\nHelloFromLambda");
    }

}
