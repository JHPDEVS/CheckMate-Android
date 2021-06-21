package com.eatx.wdj.ui.main;

import androidx.annotation.Nullable;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class AbsenceRequest extends StringRequest {
    final static private String URL = "https://ckmate.shop/.well-known/absence.php";

    private Map<String, String> map;

    public AbsenceRequest(String id, String name, int sid, String classValue, String deviceid,String date, String timestamp,String descValue, Response.Listener<String> listener) {
        super(Method.POST, URL,listener,null);

        map = new HashMap<>();
        map.put("id",id);
        map.put("name",name);
        map.put("sid",sid +"");
        map.put("class",classValue);
        map.put("deviceid",deviceid);
        map.put("date",date);
        map.put("timestamp",timestamp);
        map.put("descValue",descValue);
    }
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }

}
