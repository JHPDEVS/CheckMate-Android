package com.eatx.wdj.ui.Register;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {
    final static private String URL = "https://ckmate.shop/.well-known/Register.php";

    private Map<String, String> map;

    public RegisterRequest(String id, String password, int sid, String name, String classValue,int phoneNumber, String email, Response.Listener<String> listener) {
        super(Method.POST, URL,listener,null);

        map = new HashMap<>();
        map.put("id",id);
        map.put("password",password);
        map.put("sid",sid +"");
        map.put("name",name);
        map.put("class",classValue);
        map.put("phoneNumber","0"+phoneNumber);
        map.put("email",email);
    }
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }

}
