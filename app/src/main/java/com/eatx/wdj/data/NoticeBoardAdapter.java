package com.eatx.wdj.data;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import com.eatx.wdj.data.model.noticeBoardModel;
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

public class NoticeBoardAdapter extends RecyclerView.Adapter<NoticeBoardAdapter.MyViewHolder> {

    private Context mContext;
    private List<noticeBoardModel> posts = new ArrayList<>();

    public NoticeBoardAdapter (Context context,List<noticeBoardModel> posts){
        this.mContext = context;
        this.posts = posts;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView mTitle, mHref , mCom , mWriter, mDate;
        private LinearLayout mContainer;

        public MyViewHolder (View view){
            super(view);

            mTitle = view.findViewById(R.id.titleValue);
            mHref = view.findViewById(R.id.subject);
            mCom = view.findViewById(R.id.com);
            mWriter = view.findViewById(R.id.writer);
            mDate = view.findViewById(R.id.date);
            mContainer = view.findViewById(R.id.board_container);
        }
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.notice_board_item,parent,false);
        return new MyViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final noticeBoardModel post = posts.get(position);

        holder.mCom.setText(post.getNum());
        holder.mTitle.setText(post.getTitle());
        holder.mWriter.setText(post.getAuthor());
        holder.mDate.setText(post.getDate());
        holder.mContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(post.getHref()));
                v.getContext().startActivity(intent);
            }
        });
    }

    public void updateData(int position) {
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        posts.remove(position);
        notifyItemRemoved(position);
        notifyDataSetChanged();
    }
    @Override
    public int getItemCount() {
        return posts.size();
    }
}