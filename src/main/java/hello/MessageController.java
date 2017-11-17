package hello;

import java.util.concurrent.atomic.AtomicLong;

import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

@RestController
public class MessageController {
    protected String token;
    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();
    private BotApi mBot = new BotApi(token);

    @RequestMapping(method = RequestMethod.GET, value = "/message")
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
        System.out.println("Received Message: " + name);
        return new Greeting(counter.incrementAndGet(),
                String.format(template, name));
    }

    @RequestMapping(method = RequestMethod.GET, value = "*")
    public Greeting greetingAll(@RequestParam(value="name", defaultValue="World") String name) {
        System.out.println("Received Message: " + name);
        return new Greeting(counter.incrementAndGet(),
                String.format(template, name));
    }
    //Received Message: {"text":"Hello aaaa","type":"message","timestamp":"2017-10-11T07:47:22.217Z","localTimestamp":"2017-10-11T10:47:22.217+03:00","id":"1507708042223","channelId":"skype","serviceUrl":"https://smba.trafficmanager.net/apis/","from":{"id":"29:1ScJwVK-EwJ-pJiSC2tv5S7P5MAQkmPrb0T5AqfSdk2A","name":"Александр Чуприна"},"conversation":{"id":"29:1ScJwVK-EwJ-pJiSC2tv5S7P5MAQkmPrb0T5AqfSdk2A"},"recipient":{"id":"28:b1cb1394-69e9-443f-9908-5777725493b0","name":"Hello Hexel!"},"entities":[{"locale":"ru-RU","country":"UA","platform":"Windows","type":"clientInfo"}],"channelData":{"text":"Hello aaaa"}}

    //Received Message: {"action":"add","type":"contactRelationUpdate","timestamp":"2017-10-11T08:00:20.042Z","id":"f:2c82a5ad","channelId":"skype","serviceUrl":"https://smba.trafficmanager.net/apis/","from":{"id":"29:1MugxL8AXqTgZjaazIeVE6SgLsxl1ynnYpxE_lbK-3mE","name":"tatjanaosa"},"conversation":{"id":"29:1MugxL8AXqTgZjaazIeVE6SgLsxl1ynnYpxE_lbK-3mE"},"recipient":{"id":"28:b1cb1394-69e9-443f-9908-5777725493b0","name":"Hello Hexel!"},"entities":[{"locale":"en","platform":"Windows","type":"clientInfo"}]}

    @RequestMapping(method = RequestMethod.POST, value="/message")
    public ResponseEntity<?> messageReceived(@RequestBody String request) {
        //mBot = new BotApi(token);
        System.out.println("Received Message: " + request.toString());
        JSONObject json = new JSONObject(request);

        try {
            if (json.getString("text").contains("hello")) {
                mBot.sendMessage((String) json.get("serviceUrl"), json.getJSONObject("conversation"), json.getJSONObject("from"), "Hello, World!", json.getJSONObject("recipient"), (String) json.get("id"));
            } else {
                mBot.sendMessage((String) json.get("serviceUrl"), json.getJSONObject("conversation"), json.getJSONObject("from"), "Hello, " + json.getString("text"), json.getJSONObject("recipient"), (String) json.get("id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
            mBot.sendMessage((String) json.get("serviceUrl"), json.getJSONObject("conversation"), json.getJSONObject("from"), "Дай мне по башке у меня сбой какойто", json.getJSONObject("recipient"), (String) json.get("id"));
        }

        HttpHeaders headers = new HttpHeaders();
        return new ResponseEntity<String>(headers, HttpStatus.CREATED);
    }
}