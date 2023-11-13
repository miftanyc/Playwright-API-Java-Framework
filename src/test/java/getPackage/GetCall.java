package getPackage;

import basePackage.BaseClass;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.HttpHeader;
import com.microsoft.playwright.options.RequestOptions;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class GetCall extends BaseClass {
    private APIRequestContext ctx;

    @Test(priority=1)
    public void getUsers() {

        //Initialize Context
        ctx = returnContext();

        //Initialize Get Request
        APIResponse apiResponse = ctx.get("https://gorest.co.in/public/v2/users");

        // Collect API Data
        int statusCode = apiResponse.status();							//Status Code: 200
        String statusText = apiResponse.statusText();					//Status text: ok
        String plainTextOfResponseBody = apiResponse.text();			//Response Body in Plain Text Format not JSON
        String currentUrl = apiResponse.url();
        Map<String, String> headers = apiResponse.headers();


        byte[] responseBodyInByteFormat = apiResponse.body();			//Will Store Response in Byte Format. So need Jakson DataBind to convert this Byte Format to Jason Format
        ObjectMapper objMapper = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = objMapper.readTree(responseBodyInByteFormat);	// Convert Byte format to JSON Node
        } catch (IOException e) {
            e.printStackTrace();
        }
        String jsonResponseBody = jsonNode.toPrettyString();			//Convert JSON Node to JSON Format


        //Print API Statement
        System.out.println("Status Code: "+statusCode);
        System.out.println("Status Text: "+statusText);
        System.out.println("Current URL: "+currentUrl);


        System.out.println("----------------Status Response body in Plain Text-----------------------\n");
        System.out.println(plainTextOfResponseBody);

        System.out.println("\n----------------Status Response body in JSON Format-----------------------\n");
        System.out.println(jsonResponseBody);

        System.out.println("\n----------------Headers Key And Value-----------------------\n");
        for(String key : headers.keySet()) {
            String value = headers.get(key);
            System.out.println(key+" = "+value);
        }

        //Assert Function
        Assert.assertTrue(apiResponse.ok()); //This Return Boolean so Make it True
        Assert.assertEquals(statusCode, 200);
        Assert.assertEquals(statusText, "OK");
        Assert.assertEquals(currentUrl ,"https://gorest.co.in/public/v2/users");


        //Assert Some Header
        Assert.assertEquals(headers.get("connection"), "close");
        Assert.assertEquals(headers.get("content-type"), "application/json; charset=utf-8");

    }

    @Test(priority = 2)
    public void getAUserWithQuaryParam() {

        //Initialize Context
        ctx = returnContext();

        //Create RequestOption()
        RequestOptions reqOpt = RequestOptions.create();
                reqOpt.setQueryParam("id", 5405558);
                reqOpt.setQueryParam("gender", "female");

        // Initialize get Request
        APIResponse apiResponse = ctx.get("https://gorest.co.in/public/v2/users", reqOpt);

        int statusCode = apiResponse.status();
        String statusText = apiResponse.statusText();

        ObjectMapper objMap = new ObjectMapper();
        JsonNode jsonNode = null;
        try {
            jsonNode = objMap.readTree(apiResponse.body());
        } catch (IOException e) {
            e.printStackTrace();
        }
        String jsonResponseBody = jsonNode.toPrettyString();

        Map<String, String> headers = apiResponse.headers();

        System.out.println("Status code: "+statusCode);
        System.out.println("Status Text: "+ statusText);

        System.out.println("\n--------------------- Print Response Body--------------------");
        System.out.println(jsonResponseBody);

        System.out.println("\n------------------------ Print Headers----------------------");
        for(String key : headers.keySet()) {
            String value = headers.get(key);
            System.out.println(key+" = "+value);
        }


    }

    @Test (priority = 3)
    public void headersMapVsHeadersArray() {

        //Initialize Context
        ctx = returnContext();

        //Create Request Options
        RequestOptions reqOpt = RequestOptions.create();
        reqOpt.setQueryParam("id", 5405558);
        reqOpt.setQueryParam("gender", "female");

        //Initialize get Request
        APIResponse apiResponse = ctx.get("https://gorest.co.in/public/v2/users", reqOpt);


        //Headers in HashMap
        Map<String, String> headersInHashMap = apiResponse.headers();

        System.out.println("\n------------------------ Print Headers ***HASHMAP*** Using For Each Loop----------------------");
        for(String key : headersInHashMap.keySet()) {
            String value = headersInHashMap.get(key);
            System.out.println(key+" = "+value);


        }

        //Headers in ArrayList'
        List<HttpHeader> headersInArrayList = apiResponse.headersArray();
        System.out.println("\n------------------------ Print Headers ***ArrayList*** Using For Each Loop----------------------");
        for(HttpHeader array: headersInArrayList) {
            System.out.println(array.name+" : "+array.value);
        }
    }

}

