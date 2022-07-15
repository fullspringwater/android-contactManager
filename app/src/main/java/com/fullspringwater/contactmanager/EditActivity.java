package com.fullspringwater.contactmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fullspringwater.contactmanager.data.DatabaseHandler;
import com.fullspringwater.contactmanager.model.Contact;

public class EditActivity extends AppCompatActivity {

    // 넘어온 데이터에 대한 멤버변수
//    int id;
//    String name;
//    String phone;

    Contact contact;

    // 화면 UI에 대한 멤버변수
    EditText editName;
    EditText editPhone;
    Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        // 다른 액티비티로부터 넘겨받은 데이터가 있으면, 이 데이터를 먼저 처리하자.
        contact = (Contact) getIntent().getSerializableExtra("contact");
//        id = getIntent().getIntExtra("id", 0);
//        name = getIntent().getStringExtra("name");
//        phone = getIntent().getStringExtra("phone");

        // 화면과 변수 연결
        editName = findViewById(R.id.editName);
        editPhone = findViewById(R.id.editPhone);
        btnSave = findViewById(R.id.btnSave);

        // 넘겨받은 데이터를 화면에 셋팅
        editName.setText(contact.name);
        editPhone.setText(contact.phone);

        // 버튼 클릭시 처리할 코드 작성 : DB에 저장하고,
        // 액티비티 종료해서, 메인으로 돌아감
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 1. 유저가 입력한 이름과 폰번 가져오기
                String name = editName.getText().toString().trim();
                String phone = editPhone.getText().toString().trim();

                if(name.isEmpty() || phone.isEmpty()){
                    Toast.makeText(getApplicationContext(), "데이터 입력을 올바르게 해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                // 2. 컨택 객체로 만들기
//                Contact contact = new Contact(id, name, phone);
                contact.name = name;
                contact.phone = phone;
                // 3. 디비에 저장하기
                // 3-1. 디비 핸들러 가져오기
                DatabaseHandler db = new DatabaseHandler(EditActivity.this);

                // 3-2 주소록 데이터를 업데이트 하는 함수 호출
                // 업데이트에 필요한 파라미터는 Contact 클래스의 객체다
                db.updateContact(contact);
                db.close();
                // 3-3 토스트 보여주기
                Toast.makeText(getApplicationContext(), "수정되었습니다.", Toast.LENGTH_SHORT).show();

                // 4. 액티비티 종료하기
                finish();
            }
        });
    }
}