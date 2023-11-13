package alternateResourceExtra;

import jsonFilesDataProvider.UserPOJO;
import org.testng.Assert;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIRequest;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.RequestOptions;


public class PostCallUsingPOJOClass {

    static Playwright pw;
    static APIRequest request;
    static APIRequestContext ctx;

    static String email;

    public static void main(String[] args) {

        initializeAPI();
        PostResponseUsingPOJO();
        closeAPI();
    }

    public static void initializeAPI() {
        pw = Playwright.create();
        request = pw.request();
        ctx = request.newContext();
    }

    public static void closeAPI() {
        ctx.dispose();
        pw.close();
    }

    public static String generateemail() {

        email = "lalchand_kalaChand"+System.currentTimeMillis()+"@jilmil.com";
        return email;
    }

    public static void PostResponseUsingPOJO() {
        UserPOJO user = new UserPOJO("MILON", generateemail(), "male", "active");

        APIResponse postResponse = ctx.post("https://gorest.co.in/public/v2/users",
                RequestOptions.create()
                        .setHeader("Content-Type", "application/json")
                        .setHeader("Authorization", "Bearer 75457d9a1fe90c50f8b79049f07289e0cb2632bcbbc410934432a1e853832df0")
                        .setData(user));

        int status = postResponse.status();
        String statusText = postResponse.statusText();

        System.out.println("Status Code: "+status);
        System.out.println("status Text: "+ statusText);
        Assert.assertEquals(status, 201);
        Assert.assertEquals(statusText, "Created");

        String responseText = postResponse.text();
        System.out.println(responseText);

        //Convert Response text/json to POJO -- Deserialization
        ObjectMapper objmap = new ObjectMapper();


        UserPOJO actualUserReasponse = null;
        try {
            actualUserReasponse = objmap.readValue(responseText, UserPOJO.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        System.out.println("Actual User Response");
        System.out.println(actualUserReasponse);

        Assert.assertEquals(actualUserReasponse.getName(), user.getName());
        Assert.assertEquals(actualUserReasponse.getEmail(), user.getEmail());
        Assert.assertEquals(actualUserReasponse.getGender(), user.getGender());
        Assert.assertEquals(actualUserReasponse.getStatus(), user.getStatus());

        Assert.assertNotNull(actualUserReasponse.getId());

    }

}
