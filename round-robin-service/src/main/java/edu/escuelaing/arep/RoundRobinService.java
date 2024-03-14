package edu.escuelaing.arep;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class RoundRobinService{
    private static List<String> servers = new ArrayList<>();
    private static final String USER_AGENT = "Mozilla/5.0";
    private static final String CONTENT_TYPE = "application/json";
    private static int currentIndex = 0;


    public RoundRobinService(){
        servers.add("http://log-service1:35000/new-string");
        servers.add("http://log-service2:35000/new-string");
        servers.add("http://log-service3:35000/new-string");
    }

    private String getNextServer(){
        String server = servers.get(currentIndex);
        currentIndex = (currentIndex + 1) % servers.size(); // Incrementar el índice y evitar desbordamiento
        return server;
    }

    public String connectWithLogService(String json) throws IOException{
        URL obj = new URL(getNextServer());
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("User-Agent", USER_AGENT);
        con.setRequestProperty("Content-Type", CONTENT_TYPE);
        con.setDoOutput(true);

        try (OutputStream os = con.getOutputStream()) {
            byte[] input = json.getBytes("utf-8");
            os.write(input, 0, input.length);
            os.close();
        }
        
        //The following invocation perform the connection implicitly before getting the code
        int responseCode = con.getResponseCode();
        System.out.println("POST Response Code :: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // return result
            return response.toString();
        } else {
            System.out.println("POST request not worked");
            return null;
        }
    }
}