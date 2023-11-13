package deletePackage;

import basePackage.BaseClass;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.RequestOptions;
import jsonFilesDataProvider.UserLombok;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.UtilitiesClass;


public class DeleteCall extends BaseClass {


    //1. post - user id = 123
    //2. delete -  user id = 123
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

        System.out.println("Post Call status: "+response.status()+"   >>    Post Call Status Text: "+ response.statusText());
        System.out.println("Post call Response: "+response.text());


        //Deserialization: Convert Json/Text to POJO
        ObjectMapper objMap = new ObjectMapper();
        UserLombok postRes = null;
        try {
            postRes = objMap.readValue(response.text(), UserLombok.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        //Assertion For TestNG
        Assert.assertEquals(response.status(), 201);
        Assert.assertEquals(response.statusText(), "Created");
        Assert.assertEquals(postRes.getName(), reqUser.getName());
        Assert.assertEquals(postRes.getEmail(), reqUser.getEmail());
        Assert.assertEquals(postRes.getGender(), reqUser.getGender());
        Assert.assertEquals(postRes.getStatus(), reqUser.getStatus());
        Assert.assertNotNull(postRes.getId());


        //Initilize Delete Call
            //First Get The ID
            String id = postRes.getId();


        //Delete call to Delete User
        RequestOptions reqOpt1 = RequestOptions.create()
                .setHeader("Authorization", "Bearer 75457d9a1fe90c50f8b79049f07289e0cb2632bcbbc410934432a1e853832df0");

        APIResponse deleteResponse = ctx.delete("https://gorest.co.in/public/v2/users/" + id, reqOpt1);


        System.out.println("Delete call status: "+deleteResponse.status()+"  >> Delete call StatusText: "+ deleteResponse.statusText());

        Assert.assertEquals(deleteResponse.status(), 204);
        Assert.assertEquals(deleteResponse.statusText(), "No Content");

        System.out.println("Delete response body: "+deleteResponse.text());


        //3 Getcall to see if the user is deleted
        RequestOptions reqOpt2 = RequestOptions.create()
                .setHeader("Authorization", "Bearer 75457d9a1fe90c50f8b79049f07289e0cb2632bcbbc410934432a1e853832df0");
        APIResponse getResponse = ctx.get("https://gorest.co.in/public/v2/users/" + id, reqOpt1);

        System.out.println("Get call of a deleted user status: "+getResponse.status()+"   >>    Get call of a deleted user StatusText: "+ getResponse.statusText());


        Assert.assertEquals(getResponse.status(), 404);
        Assert.assertEquals(getResponse.statusText(), "Not Found");

        System.out.println("Get Response of Delted User "+getResponse.text());

        Assert.assertTrue(getResponse.text().contains("Resource not found"));

    }

}
