package com.eatx.wdj.ui.main;


import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BoardSearchRequest extends StringRequest {

    final static private String URL = "https://ckmate.shop/.well-known/BoardSearch.php";

    private Map<String, String> map;

    public BoardSearchRequest(String query ,Response.Listener<String> listener) {
        super(Method.POST, URL,listener,null);

        map = new HashMap<>();
        map.put("subject",query);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
