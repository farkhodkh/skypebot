package hello;

import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class HttpRequester {

    private final String USER_AGENT = "Mozilla/5.0";


    // HTTP GET request
    public void sendGet() throws Exception {

        String url = "http://www.google.com/search?q=google";

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();

        // optional default is GET
        con.setRequestMethod("GET");

        //add request header
        con.setRequestProperty("User-Agent", USER_AGENT);

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'GET' request to URL : " + url);
        System.out.println("Response Code : " + responseCode);

        BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        System.out.println(response.toString());

    }

    // HTTP POST request
    public String sendPost(String url, String urlParameters, Map<String, String> headers) throws Exception {
        URL obj = new URL(url);
        HttpsURLConnection con;
        //if(url.contains("https://")){
        con = (HttpsURLConnection) obj.openConnection();
//        }else{
//            con = (HttpURLConnection) obj.openConnection();
//        }

        //add reuqest header
        con.setRequestMethod("POST");

        for(Map.Entry<String, String> entry: headers.entrySet()) {
            con.setRequestProperty(entry.getKey(), entry.getValue());
        }

        // Send post request
        con.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(con.getOutputStream());
        //wr.write(urlParameters.getBytes("UTF-8"));
        wr.writeBytes(urlParameters);
        wr.flush();
        wr.close();

        int responseCode = con.getResponseCode();
        System.out.println("\nSending 'POST' request to URL : " + url);
        System.out.println("Post parameters : " + urlParameters);
        System.out.println("Response Code : " + responseCode + " Response message: " + con.getResponseMessage());

        if (!(responseCode == 200)) {
            JSONObject jsonCon = new JSONObject(con);
            Object error = jsonCon.get("responseMessage");
        }
        InputStream is = con.getInputStream();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(is));
        String inputLine;
        StringBuffer response = new StringBuffer();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        //print result
        System.out.println(response.toString());

        return response.toString();
    }

}