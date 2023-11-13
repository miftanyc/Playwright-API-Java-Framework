package alternateResourceExtra;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;
import utils.UtilitiesClass;

public class postCallAfterCreatingUserUsingHashMap {

    public static void main(String[] args){

        Playwright pw = Playwright.create();
        APIRequest apirequest = pw.request();
        APIRequestContext apictx = apirequest.newContext();

        String email = "chingaPinga"+System.currentTimeMillis()+"@tikkakhan.com";

        UtilitiesClass utils = new UtilitiesClass();
        //Json Data as Payload for Post Request
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("name", "Delon");
        data.put("gender", "female");
        data.put("email", email);
        data.put("status", "active");


        APIResponse postResponse = apictx.post("https://gorest.co.in/public/v2/users",
                RequestOptions.create()
                        .setHeader("Content-Type", "application/json")
                        .setHeader("Authorization", "Bearer 75457d9a1fe90c50f8b79049f07289e0cb2632bcbbc410934432a1e853832df0")
                        .setData(data)
        );

        int statusCode = postResponse.status();
        String statusText = postResponse.statusText();
        String responsePlain = postResponse.text();

        ObjectMapper objmap = new ObjectMapper();
        JsonNode postResponseJNode=null;
        try {
            postResponseJNode = objmap.readTree(postResponse.body());
        } catch (IOException e) {
            e.printStackTrace();
        }
        String jsonResponse = postResponseJNode.toPrettyString();

        System.out.println("Status Code: "+statusCode);
        System.out.println("Status text: "+statusText);
        System.out.println("----------Response Body in Plain Text------: ");
        System.out.println(responsePlain);

        System.out.println("------Response Body in Json Format------");
        System.out.println(jsonResponse);

        Assert.assertEquals(statusCode, 201);
        Assert.assertEquals(statusText, "Created");

        //Get The user ID from Response
        String userID = postResponseJNode.get("id").asText();
        System.out.println("UserID: "+userID);


        //Now Create get Response with id to make sure user is created or not

        APIResponse getResponse = apictx.get("https://gorest.co.in/public/v2/users/"+userID,
                RequestOptions.create()
                        .setHeader("Authorization", "Bearer 75457d9a1fe90c50f8b79049f07289e0cb2632bcbbc410934432a1e853832df0")
        );

        int getStatusCode = getResponse.status();
        String getStatusText = getResponse.statusText();
        String getResponseText = getResponse.text();
        System.out.println(getStatusCode);
        System.out.println(getStatusText);
        System.out.println(getResponseText);

        Assert.assertEquals(getStatusCode, 200);
        Assert.assertEquals(getStatusText, "OK");
        Assert.assertTrue(getResponseText.contains(userID));
        Assert.assertTrue(getResponseText.contains("Delon"));
        Assert.assertTrue(getResponseText.contains("female"));
        Assert.assertTrue(getResponseText.contains(email));

        apictx.dispose();
        pw.close();
    }
}
