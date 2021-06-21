package com.eatx.wdj.ui.main;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.eatx.wdj.R;
import com.eatx.wdj.data.BoardAdapter;
import com.eatx.wdj.data.NoticeBoardAdapter;
import com.eatx.wdj.data.model.Post;
import com.eatx.wdj.data.model.noticeBoardModel;
import com.eatx.wdj.ui.login.MainActivity;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class noticeBoard extends Fragment {
    private List<noticeBoardModel> posts;
    private MainViewModel mViewModel;
    private RecyclerView.LayoutManager manager;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView recyclerView;
    private ImageView searchButton;
    public static noticeBoard newInstance() {
        return new noticeBoard();
    }
    private ProgressDialog write;
    View inflatedview = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        inflatedview = inflater.inflate(R.layout.fragment_notice_board, container, false);
        recyclerView = inflatedview.findViewById(R.id.boardrecyclerview);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        posts = new ArrayList<>();
        mAdapter = new NoticeBoardAdapter(getActivity(),posts);
        Update();
        searchButton = inflatedview.findViewById(R.id.searchButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),noticeBoardSearch.class);
                startActivity(intent);
            }
        });
        return inflatedview;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        // TODO: Use the ViewModel
    }

    protected void Update() {
        new Thread() {
            public void run() {
                try {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            write = new ProgressDialog(getActivity());
                            write.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                            write.setMessage("로딩중");
                            write.setCanceledOnTouchOutside(false);
                            write.show();
                        }
                    });
                    posts.clear();
                    Document doc = Jsoup.connect("https://computer.yju.ac.kr/board_XwRR82").get();
                    Elements contents = doc.select("tbody").select("tr.notice");
                    Elements contents2 = doc.select("tbody").select("tr");
                    System.out.println(contents2);
                    List<String> imageUrls = new ArrayList<>();

//                    for(Element content : contents) {
//                          imageUrls.add(content.select("td.title").select("a").attr("abs:href")); // 하이퍼링크
//                          imageUrls.add(content.select("td.title").select("a").select("strong").text()); // 제목
//                          imageUrls.add(content.select("td.author").select("a").text()); // 작성자
//                          imageUrls.add(content.select("td.time").text()); // 작성날짜
//                    }

                    for(Element content : contents2) {
                        imageUrls.add(content.select("td.no").text());
                        imageUrls.add(content.select("td.title").select("a").attr("abs:href"));
                        imageUrls.add(content.select("td.title").select("a").text()); // 제목
                        imageUrls.add(content.select("td.author").select("a").text()); // 작성자
                        imageUrls.add(content.select("td.time").text()); // 작성날짜

                        noticeBoardModel post = new noticeBoardModel();
                        post.setTitle(content.select("td.title").select("a").text());
                        post.setHref(content.select("td.title").select("a").attr("abs:href"));
                        post.setDate(content.select("td.time").text());
                        post.setAuthor(content.select("td.author").select("a").text());
                        post.setNum(content.select("td.no").text());
                        if(post.getTitle() != "") {
                            posts.add(post);
                        }
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter.notifyDataSetChanged();
                                recyclerView.setAdapter(mAdapter);
                                write.dismiss();
                            }
                        });
                    }
                    System.out.println(imageUrls);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

}