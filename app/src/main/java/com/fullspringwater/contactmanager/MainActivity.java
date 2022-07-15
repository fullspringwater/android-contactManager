package com.fullspringwater.contactmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.fullspringwater.contactmanager.adapter.ContactAdapter;
import com.fullspringwater.contactmanager.data.DatabaseHandler;
import com.fullspringwater.contactmanager.model.Contact;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ContactAdapter adapter;
    ArrayList<Contact> contactList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnAdd = findViewById(R.id.btnAdd);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddContactActivity.class);
                startActivity(intent);
            }
        });




//
//        // 주소록 디비에서 id가 1인 주소록 데이터 로그 찍어보자.
//        Contact contact1 = db.getContact(1);
//        Log.i("MyContact", "디비에서 하나만 가져오기, id : " + contact1.id +
//                ", name : " + contact1.name + ", phone : " + contact1.phone);


    }

    @Override
    protected void onResume() {
        super.onResume();

        // DB에서 주소록 데이터를 모두 가져와서, 리사이클러뷰에 표시한다.
        DatabaseHandler db = new DatabaseHandler(MainActivity.this);
        contactList = db.getAllContact();
        adapter = new ContactAdapter(MainActivity.this, contactList);

        recyclerView.setAdapter(adapter);

        printDBData();
    }

    // DB에 있는 모든 주소록 데이터를, 디버깅용으로 로그에 출력하는 함수
    void printDBData(){
        // 데이터베이스에 데이터 넣고, 가져오는 것 테스트
        DatabaseHandler db = new DatabaseHandler(MainActivity.this);

        // 주소록 데이터를 디비에서 가져와서, 로그 찍어보자
        ArrayList<Contact> contactList = db.getAllContact();

        for(Contact data : contactList){
            Log.i("MyContact: ", "id : " + data.id +
                    ", name : " + data.name + ", phone : " + data.phone);
        }
    }
}