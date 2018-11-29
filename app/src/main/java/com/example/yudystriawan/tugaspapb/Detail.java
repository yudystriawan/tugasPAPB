package com.example.yudystriawan.tugaspapb;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;


import java.util.ArrayList;

public class Detail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail);
        Intent intent = getIntent();
        Bundle gt = intent.getExtras();

        //RecView
        ArrayList<User> listOs = new ArrayList<User>();
        String komentar = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Ut ac urna pellentesque, commodo magna sit amet, cursus mi. Sed nec tempus velit. Donec ex sem, vehicula a ex vitae, dapibus ultricies diam.";
        listOs.add(new User("Vito", komentar));
        listOs.add(new User("Juki", komentar));
        listOs.add(new User("Ade", komentar));
        listOs.add(new User("Dityo", komentar));
        listOs.add(new User("Rizka", komentar));
        listOs.add(new User("Hartono", komentar));
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        CustomAdapter mAdapter = new CustomAdapter(this, listOs);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


    }
}


