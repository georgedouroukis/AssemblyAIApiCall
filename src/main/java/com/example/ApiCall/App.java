package com.example.ApiCall;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import com.google.gson.Gson;

public class App 
{
    public static void main( String[] args ) throws InterruptedException, URISyntaxException, IOException 
    {

    	String url = FileUploader.uploadFile(Constants.apiKey, "C:\\Users\\George\\Desktop\\t.mp4" );
    	
    	Transcript transcript = new Transcript();
    	transcript.setAudio_url(url);
    	System.out.println("url is: " + transcript.getAudio_url());
    	Gson gson = new Gson();
    	String jsonRequest = gson.toJson(transcript);
    	
    	System.out.println(jsonRequest);
    	
    	HttpClient httpClient = HttpClient.newHttpClient();
    	
    	
    	
    	HttpRequest postRequest = HttpRequest.newBuilder()
    			.uri(new URI("https://api.assemblyai.com/v2/transcript"))
    			.header("Authorization", Constants.apiKey)
    			.POST(BodyPublishers.ofString(jsonRequest))
    			.build();
    	
    	HttpResponse<String> postResponse = httpClient.send(postRequest, BodyHandlers.ofString());
    	
//    	System.out.println(postResponse.body());
    	
    	transcript = gson.fromJson(postResponse.body(), Transcript.class);
    	
//    	System.out.println(transcript.getId());
    	
    	
    	HttpRequest getRequest = HttpRequest.newBuilder()
    			.uri(new URI("https://api.assemblyai.com/v2/transcript/" + transcript.getId()))
    			.header("Authorization", Constants.apiKey)
    			.build();
    	
    	
    	while(true) {
    		
    		HttpResponse<String> getResponse = httpClient.send(getRequest, BodyHandlers.ofString());
    		
        	transcript = gson.fromJson(getResponse.body(), Transcript.class);
        	
       	
        	System.out.println(transcript.getStatus());
        	
        	if (transcript.getStatus().equals("completed") || transcript.getStatus().equals("error")) {
        		break;
        	}
        	Thread.sleep(1000);	

    	}

    	System.out.println("Transcription completed!");
    	System.out.println("Text: " + transcript.getText());
    	
    	
    }
}