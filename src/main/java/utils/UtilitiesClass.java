package utils;

import jsonFilesDataProvider.UserLombok;

public class UtilitiesClass {

    //Generate Random Email
    public StringBuffer generateEmail(String emailHost){
        return new StringBuffer(emailHost+System.currentTimeMillis()+"@gmail.com");
    }

    public UserLombok user1(){
        UserLombok user1 = UserLombok.builder()
                .name("ARHAM")
                .email(generateEmail("dailyAPI").toString())
                .gender("male")
                .status("active")
                .build();
        return user1;
    }
}
