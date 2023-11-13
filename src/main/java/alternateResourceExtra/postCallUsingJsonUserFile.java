package alternateResourceExtra;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.testng.Assert;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;


public class postCallUsingJsonUserFile {

    static Playwright pw;
    static APIRequest request;
    static APIRequestContext ctx;

    public static void main(String[] args) {
        initializePlaywright();
        postCallUsingJsonFile();
        closeConnection();
    }

    public static void initializePlaywright() {
        pw = Playwright.create();
        request = pw.request();
        ctx = request.newContext();
    }

    public static void closeConnection() {
        ctx.dispose();
        pw.close();
    }

    public static void postCallUsingJsonFile() {

        File file = new File("./src/main/java/jsonFilesDataProvider/user.json");
        byte[] fileByte = null;
        try {
            fileByte = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        APIResponse postResponse = ctx.post("https://gorest.co.in/public/v2/users",
                RequestOptions.create()
                        .setHeader("Content-Type", "application/json")
                        .setHeader("Authorization", "Bearer 75457d9a1fe90c50f8b79049f07289e0cb2632bcbbc410934432a1e853832df0")
                        .setData(fileByte));

        int status = postResponse.status();
        String statusText = postResponse.statusText();
        String postResponseText = postResponse.text();
        postResponse.body();

        System.out.println(status);
        System.out.println(statusText);
        System.out.println(postResponseText);

        ObjectMapper objmap = new ObjectMapper();

        JsonNode postResponseJsonNode = null;
        try {
            postResponseJsonNode = objmap.readTree(postResponse.body());
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(postResponseJsonNode.toPrettyString());


        String userID = postResponseJsonNode.get("id").asText();



        System.out.println("======Get Call Response=====");
        //Fetch the user with userID
        APIResponse getResponse = ctx.get("https://gorest.co.in/public/v2/users/"+userID,
                RequestOptions.create()
                        .setHeader("Authorization", "Bearer 75457d9a1fe90c50f8b79049f07289e0cb2632bcbbc410934432a1e853832df0"));

        int getStatus = getResponse.status();
        String getStatusText = getResponse.statusText();
        String getResponseBody = getResponse.text();

        System.out.println("Get reponse: "+getStatus);
        System.out.println("Get reponse: "+getStatusText);
        System.out.println(getResponseBody);

        JsonNode getResponseJsonNode = null;
        try {
            getResponseJsonNode = objmap.readTree(getResponse.body());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        System.out.println("Get Call userID: "+getResponseJsonNode.get("id").asText());

        Assert.assertTrue(getResponseBody.contains(userID));
        Assert.assertTrue(getResponseBody.contains("Milon Khondokar"));
        Assert.assertTrue(getResponseBody.contains("male"));


    }

}

