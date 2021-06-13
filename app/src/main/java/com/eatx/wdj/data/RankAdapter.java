package com.eatx.wdj.data;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.RecyclerView;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.eatx.wdj.data.model.Post;
import com.eatx.wdj.R;
import com.eatx.wdj.data.model.Rankers;
import com.eatx.wdj.ui.login.MainActivity;
import com.eatx.wdj.ui.main.Board;
import com.eatx.wdj.ui.main.DetailedView;
import com.eatx.wdj.ui.main.delBoardRequest;
import com.eatx.wdj.ui.main.nameRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class RankAdapter extends RecyclerView.Adapter<RankAdapter.MyViewHolder> {

    private Context mContext;
    private List<Rankers> rankers = new ArrayList<>();
    private String password;
    private int bno , no;

    public RankAdapter (Context context,List<Rankers> rankers){
        this.mContext = context;
        this.rankers = rankers;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView id, name , sid, classR , totalRun, countLate;
        private LinearLayout mContainer;

        public MyViewHolder (View view){
            super(view);

            name = view.findViewById(R.id.nameR);
            sid = view.findViewById(R.id.sidR);
            classR = view.findViewById(R.id.classR);
            totalRun = view.findViewById(R.id.totalRun);
            countLate = view.findViewById(R.id.countLate);
            mContainer = view.findViewById(R.id.rank_container);
        }
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.row_rank_item,parent,false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final Rankers rank = rankers.get(position);

        holder.name.setText(rank.getName());
        holder.sid.setText(String.valueOf(rank.getSid()));
        holder.classR.setText(rank.getClassValue());
        holder.totalRun.setText(String.valueOf(rank.getTotalrun()));
        holder.countLate.setText(String.valueOf(rank.getCountlate()));
    }

    public void updateData(int position) {
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        rankers.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return rankers.size();
    }
}