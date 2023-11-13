package postPackage;

import basePackage.BaseClass;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.RequestOptions;

import jsonFilesDataProvider.UserLombok;

import org.testng.Assert;
import org.testng.annotations.Test;
import utils.UtilitiesClass;

public class PostCall extends BaseClass {

    private APIRequestContext ctx;

    UtilitiesClass utils = new UtilitiesClass();

    @Test(priority = 1)
    public void postCallUsingLombok(){
        //initialize Context
        ctx = returnContext();

        //Create Request Options
        UserLombok user = utils.user1();

        RequestOptions reqOpt = RequestOptions.create()
                .setHeader("Content-Type", "application/json")
                .setHeader("Authorization", "Bearer 75457d9a1fe90c50f8b79049f07289e0cb2632bcbbc410934432a1e853832df0")
                .setData(user);

        //Initialize Post Request
        APIResponse postResponse = ctx.post("https://gorest.co.in/public/v2/users", reqOpt);

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
        UserLombok actualUserReasponse = null;
        try {
            actualUserReasponse = objmap.readValue(responseText, UserLombok.class);
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
