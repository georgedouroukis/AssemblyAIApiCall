package com.example.ApiCall;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class FileUploader {
    public static String uploadFile(String apiToken, String path) throws IOException, InterruptedException, URISyntaxException {
        
    	 File file = new File(path);
         Path filePath = file.toPath();

         // Read the file content as bytes
         byte[] fileBytes = Files.readAllBytes(filePath);
    	
    	
    	HttpClient httpClient = HttpClient.newHttpClient();

    	HttpRequest request = HttpRequest.newBuilder()
    			.uri(new URI("https://api.assemblyai.com/v2/upload"))
    			.header("Authorization", Constants.apiKey)
    			.POST(BodyPublishers.ofByteArray(fileBytes))
    			.build();
       
    	HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
       

        JsonObject jsonObject = JsonParser.parseString(response.body()).getAsJsonObject();
        String url = jsonObject.get("upload_url").getAsString(); 
        System.out.println(url);
        return url;
        
        
    }
}