package com.eatx.wdj.ui.main;


import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class delBoardRequest extends StringRequest {

    final static private String URL = "https://ckmate.shop/.well-known/delBoard.php";

    private Map<String, String> map;

    public delBoardRequest(int bno , String password,Response.Listener<String> listener) {
        super(Method.POST, URL,listener,null);
        map = new HashMap<>();
        map.put("bno",bno+"");
        map.put("password",password);

    }
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}
