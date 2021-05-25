package com.eatx.wdj.ui.main;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.ViewModel;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;
import com.eatx.wdj.R;
import com.eatx.wdj.data.ItemAdapter;
import com.eatx.wdj.data.model.ItemModel;
import com.eatx.wdj.ui.Register.UserBean;
import com.eatx.wdj.ui.login.LoginActivity;
import com.eatx.wdj.ui.login.MainActivity;
import com.google.android.material.snackbar.Snackbar;

import java.sql.Array;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CheckActivity extends AppCompatActivity implements ItemAdapter.onItemListener {

    private ItemAdapter adapter;
    private List<ItemModel> itemList;
    private String[] sids;
    private String[] names;
    private RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_acitivty);
        recyclerView = findViewById(R.id.recyclerView);
        buildRecyclerView();
    }

    /****************************************************
     리사이클러뷰, 어댑터 셋팅
     ***************************************************/
    private void buildRecyclerView() {

        // below line we are creating a new array list
        List<ItemModel> itemList = new ArrayList<ItemModel>();
        // below line is to add data to our array list.
//        itemList1.add(new ItemModel(R.drawable.ic_launcher_background, "1","1"));
//        itemList1.add(new ItemModel(R.drawable.ic_launcher_background, "c++","1dd"));
//        itemList1.add(new ItemModel(R.drawable.ic_launcher_background, "3","3"));
//        itemList1.add(new ItemModel(R.drawable.ic_launcher_background, "4","4"));

        // initializing our adapter class.

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);
        doStuff();
        adapter = new ItemAdapter(itemList);
        recyclerView.setAdapter(adapter);

        // setting adapter to
        // our recycler view.

    }
    private void setUpRecyclerView() {

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);

        //adapter
        itemList = new ArrayList<>(); //샘플테이터

        recyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL); //밑줄
        recyclerView.addItemDecoration(dividerItemDecoration);

        //데이터셋변경시
    //    adapter.dataSetChanged(itemList);


    }

    private void fillData() {

    }

    private void filter(String text) {
        // creating a new array list to filter our data.
        ArrayList<ItemModel> filteredlist = new ArrayList<>();

        // running a for loop to compare elements.
        for (ItemModel item : itemList) {
            // checking if the entered string matched with any item of our recycler view.
            if (item.getText1().toLowerCase().contains(text.toLowerCase())) {
                // if the item is matched we are
                // adding it to our filtered list.
                filteredlist.add(item);
            }
        }
        if (filteredlist.isEmpty()) {
            // if no item is added in filtered list we are
            // displaying a toast message as no data found.
            Toast.makeText(this, "No Data Found..", Toast.LENGTH_SHORT).show();
        } else {
            // at last we are passing that filtered
            // list to our adapter class.
            adapter.filterList(filteredlist);
        }
    }
    /****************************************************
     onCreateOptionsMenu SearchView  기능구현
     ***************************************************/
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    /****************************************************
     리사이클러뷰 클릭이벤트 인터페이스 구현
     ***************************************************/
    @Override
    public void onItemClicked(int position) {
        Toast.makeText(this, "" +position, Toast.LENGTH_SHORT).show();
    }

    private void doStuff() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                PreparedStatement st;
                ResultSet rs;
                Connection connection = null;
                String query = "SELECT * FROM `students`";
                try {
                    Class.forName("org.mariadb.jdbc.Driver");
                    connection = DriverManager.getConnection("jdbc:mariadb://eatx.shop:3307/student_db","wdj","wdj123");
                    st = connection.prepareStatement(query);
                    rs = st.executeQuery();
                    while(rs.next()) {
                        System.out.println("업데이트중");
//                    UserBean bean = new UserBean();
//                    bean.setId(rs.getString("sid"));
//                    bean.setName(rs.getString("name"));
                        System.out.println("학번 : " + rs.getString("sid") + "이름 : " + rs.getString("name"));

                        itemList.add(new ItemModel(R.drawable.ic_launcher, rs.getString("sid"), rs.getString("name")));
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                    System.out.println("로그인 실패");
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                try {
                    connection.close();  // 연결해제
                    System.out.println("연결해제");
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                    System.out.println("연결해제 실패");
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        adapter.notifyDataSetChanged();
                        //어댑터의 리스너 호출
                       // adapter.setOnClickListener(this);
                    }
                });
            }
        });
    }
}

