package hello;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class BotApi {
    protected String mToken;
    protected HttpRequester mRequester;


    private final String APP_ID = "571b780f-3d94-493c-a22f-2442fa72e9bc";
    private final String APP_SECRET = "6TdwJQ4AATLQ1DgSJ9bgAAa";

    public BotApi(String token) {
        mRequester = new HttpRequester();

        // TODO вставить проверку времени по токену
        if (token == null) {
            getAuthToken();
        }
    }

    private void getAuthToken() {
        String url = "https://login.microsoftonline.com/botframework.com/oauth2/v2.0/token";

        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Host", "login.microsoftonline.com");
        headers.put("Content-Type", "application/x-www-form-urlencoded");

        String request = "grant_type=client_credentials&client_id=" + APP_ID + "&client_secret=" + APP_SECRET + "&scope=https%3A%2F%2Fapi.botframework.com%2F.default";


        try {
            System.out.println("\nGetting new token");
            String response = mRequester.sendPost(url, request, headers);
            JSONObject token_response = new JSONObject(response);

            System.out.println("\nToken refreshed");
//            System.out.println("\nNew token is:");
//            System.out.println(token_response.getString("access_token"));
            mToken = token_response.getString("access_token");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(String BaseURL, JSONObject conversation, JSONObject recepient, String text, JSONObject from, String conversionId) {
        //https://smba.trafficmanager.net/apis/v3/conversations/abcd1234/activities/bf3cc9a2f5de...
        //String url = "https://smba.trafficmanager.net/apis/v3/conversations/" + conversation.get("id") + "/activities";
        String url = BaseURL + "v3/conversations/" + conversation.get("id") + "/activities/" + conversionId;
        System.out.println("Conversation id is:" + conversionId);
        // Комплектуем заголовки
        Map<String, String> headers = new HashMap<String, String>();
        headers.put("Authorization", "Bearer " + mToken);
        //headers.put("Content-Type", "application/json; charset=UTF-8");
        //headers.put("Content-Type", "application/json");
        headers.put("Content-Type", "application/json; charset=utf-8");

        // Создаем тело запроса
        Map<String, Object> request = new HashMap<String, Object>();
        request.put("type", "message");
        request.put("conversation", conversation);

        request.put("recipient", recepient);
        request.put("locale", "ru");
        request.put("from", from);
        request.put("replyToId", conversionId);
        // Задаем сообщение
        request.put("text", text);

        // Отправляем
        try {
            JSONObject jsonRequest = new JSONObject(request);
            String response = mRequester.sendPost(url, jsonRequest.toString(), headers);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
