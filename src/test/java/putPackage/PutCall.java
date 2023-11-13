package putPackage;

import basePackage.BaseClass;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.Request;
import com.microsoft.playwright.options.RequestOptions;
import jsonFilesDataProvider.UserLombok;
import jsonFilesDataProvider.UserPOJO;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.UtilitiesClass;

import java.io.IOException;


public class PutCall extends BaseClass {


    //1. post - user id = 123
    //2. put -  user id = 123
    //3. get -  user id = 123
    UtilitiesClass utils = new UtilitiesClass();

    @Test(priority = 1)
    public void putCallExecution(){
        //First Create a User Data using Post Call
        UserLombok reqUser = UserLombok.builder()
                .name("Tangsu Marang")
                .email(utils.generateEmail("dailyAPI").toString())
                .gender("female")
                .status("active")
                .build();

        ctx = returnContext();
        RequestOptions reqOpt = RequestOptions.create()
                                .setHeader("Content-Type", "application/json")
                                .setHeader("Authorization", "Bearer 75457d9a1fe90c50f8b79049f07289e0cb2632bcbbc410934432a1e853832df0")
                                .setData(reqUser);
        APIResponse response = ctx.post("https://gorest.co.in/public/v2/users", reqOpt);

        System.out.println(response.status());
        System.out.println(response.statusText());
        System.out.println(response.text());
        System.out.println(response.url());



        //Deserialization: Convert Json/Text to POJO
        ObjectMapper objMap = new ObjectMapper();
        UserLombok resUserPost = null;
        try {
            resUserPost = objMap.readValue(response.text(), UserLombok.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        //Assertion For TestNG
        Assert.assertEquals(response.status(), 201);
        Assert.assertEquals(response.statusText(), "Created");
        Assert.assertEquals(resUserPost.getName(), reqUser.getName());
        Assert.assertEquals(resUserPost.getEmail(), reqUser.getEmail());
        Assert.assertEquals(resUserPost.getGender(), reqUser.getGender());
        Assert.assertEquals(resUserPost.getStatus(), reqUser.getStatus());
        Assert.assertNotNull(resUserPost.getId());


        //Initilize Put Call
            //First Get The ID
            String id = resUserPost.getId();
            //Change the User Data
            reqUser.setName("zilan zilani");
            reqUser.setStatus("inactive");

        //Put call to update User
        RequestOptions reqOpt1 = RequestOptions.create()
                .setHeader("Content-Type", "application/json")
                .setHeader("Authorization", "Bearer 75457d9a1fe90c50f8b79049f07289e0cb2632bcbbc410934432a1e853832df0")
                .setData(reqUser);
        APIResponse putResponse = ctx.put("https://gorest.co.in/public/v2/users/" + id, reqOpt1);


        System.out.println(putResponse.status()+"  :  "+putResponse.statusText());
        System.out.println("Updated User: "+putResponse.text());
        UserLombok actualPutResponse = null;
        try {
            actualPutResponse = objMap.readValue(putResponse.text(), UserLombok.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        //Assertion
        Assert.assertEquals(actualPutResponse.getName(), reqUser.getName());
        Assert.assertEquals(actualPutResponse.getStatus(), reqUser.getStatus());
        Assert.assertEquals(actualPutResponse.getId(), id);


        //3 Get the Updated user with GetCall
        RequestOptions reqOpt2 = RequestOptions.create()
                .setHeader("Authorization", "Bearer 75457d9a1fe90c50f8b79049f07289e0cb2632bcbbc410934432a1e853832df0");
        APIResponse getResponse = ctx.get("https://gorest.co.in/public/v2/users/" + id, reqOpt1);
        Assert.assertEquals(getResponse.status(), 200);
        Assert.assertEquals(getResponse.statusText(), "OK");

        System.out.println("Get Response of Updated User "+getResponse.text());
        UserLombok actualGetResponse = null;
        try {
            actualGetResponse = objMap.readValue(getResponse.text(), UserLombok.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        Assert.assertEquals(actualGetResponse.getName(), reqUser.getName());
        Assert.assertEquals(actualGetResponse.getEmail(), reqUser.getEmail());
        Assert.assertEquals(actualGetResponse.getStatus(), reqUser.getStatus());
        Assert.assertEquals(actualGetResponse.getGender(), reqUser.getGender());
        Assert.assertEquals(actualGetResponse.getId(), id);

    }

}
