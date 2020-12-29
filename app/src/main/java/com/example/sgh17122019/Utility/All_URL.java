package com.example.sgh17122019.Utility;

import java.util.HashMap;

public class All_URL {
    public static String REGISTER_LINK = "https://sgh201920.000webhostapp.com/Login%20And%20Registration/Reg.php";

    public static String LOGIN_LINK = "https://sgh201920.000webhostapp.com/Login%20And%20Registration/Login.php";




    public HashMap<String, String> databaseEntry(String email_string, String password_string, String name_string, String phone_string,String hostelname_string,String hostel_add_string){
        HashMap<String, String> params = new HashMap<>();
        params.put("hostelHeadName", name_string);
        params.put("hostelHeadEmail", email_string);
        params.put("hostelHeadPassword", password_string);
        params.put("hostelHeadMobileNumber",phone_string);
        params.put("hostelName",hostelname_string);
        params.put("hostelAddress",hostel_add_string);
        return params;
    }
}


