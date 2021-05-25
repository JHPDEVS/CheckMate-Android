package com.eatx.wdj.ui.Register;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class WriteRequest extends StringRequest {
    final static private String URL = "https://eatx.shop/.well-known/Write.php";

    private Map<String, String> map;

    public WriteRequest(String type , String id, String password, String subject, String content, String writer, String wdate, Response.Listener<String> listener) {
        super(Method.POST, URL,listener,null);

        map = new HashMap<>();
        map.put("type",type);
        map.put("id",id);
        map.put("password",password);
        map.put("subject",subject);
        map.put("content",content);
        map.put("writer",writer);
        map.put("wdate",wdate);
    }
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }

}
