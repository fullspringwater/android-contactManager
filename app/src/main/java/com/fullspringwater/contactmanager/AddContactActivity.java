package com.fullspringwater.contactmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fullspringwater.contactmanager.data.DatabaseHandler;
import com.fullspringwater.contactmanager.model.Contact;

import java.util.ArrayList;

public class AddContactActivity extends AppCompatActivity {
    EditText editName;
    EditText editPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        editName = findViewById(R.id.editName);
        editPhone = findViewById(R.id.editPhone);
        Button btnSave = findViewById(R.id.btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // 1. 유저가 입력한 이름과 폰번 가져오기
                String name = editName.getText().toString().trim();
                String phone = editPhone.getText().toString().trim();

                if(name.isEmpty() || phone.isEmpty()){
                    Toast.makeText(AddContactActivity.this, "데이터 입력을 올바르게 해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                // 2. 컨택 객체로 만들기
                Contact contact = new Contact(name, phone);

                // 3. 디비에 저장하기
                // 3-1. 디비 핸들러 가져오기
                // 데이터베이스에 데이터 넣고, 가져오는 것 테스트
                DatabaseHandler db = new DatabaseHandler(AddContactActivity.this);

                // 3-2 주소록 데이터를 디비에 저장하는 함수 호출
                db.addContact(contact);

                db.close();
                // 3-3 토스트 보여주기
                Toast.makeText(AddContactActivity.this, "잘 저장되었습니다.", Toast.LENGTH_SHORT).show();

                // 4. 액티비티 종료하기
                finish();

            }
        });
    }
}