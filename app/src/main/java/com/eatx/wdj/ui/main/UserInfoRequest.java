package com.eatx.wdj.ui.main;


import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserInfoRequest extends StringRequest {

    final static private String URL = "https://eatx.shop/.well-known/getUserInfo.php";

    private Map<String, String> map;

    public UserInfoRequest(String id,Response.Listener<String> listener) {
        super(Method.POST, URL,listener,null);

        map = new HashMap<>();
        map.put("id",id);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
