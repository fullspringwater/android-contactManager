package com.fullspringwater.contactmanager.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.fullspringwater.contactmanager.EditActivity;
import com.fullspringwater.contactmanager.MainActivity;
import com.fullspringwater.contactmanager.R;
import com.fullspringwater.contactmanager.data.DatabaseHandler;
import com.fullspringwater.contactmanager.model.Contact;

import java.util.ArrayList;
import java.util.List;

// row 화면에 맵핑할 어댑터 클래스 만드는 순서

// 1. RycyclerView.Adapter 를 상속받는다.
// unimplemented method 모두 선택해서 넣는다.

// 5. RecyclerView.Adapter의 데이터 타입을 알려줘야 한다.
// 우리가 만든 ViewHolder 의 타입으로 설정 => 그러면 아래 빨간색이 뜬다.
public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ViewHolder> {


    // 4. 어댑터 클래스의 멤버 변수와 생성자를 만들어준다.
    Context context;
    List<Contact> contactList;

    int deleteIndex;

    public ContactAdapter(Context context, List<Contact> contactList) {
        this.context = context;
        this.contactList = contactList;
    }

    // 6. 아래 함수들 구현

    // onCreate와 비슷한 기능
    @NonNull
    @Override
    public ContactAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_row, parent, false);

        return new ContactAdapter.ViewHolder(view);
    }

    // 메모리에 있는 데이터(리스트) 를 화면에 표시하는 함수
    @Override
    public void onBindViewHolder(@NonNull ContactAdapter.ViewHolder holder, int position) {
        Contact contact = contactList.get(position);
        holder.txtName.setText(contact.name);
        holder.txtPhone.setText(contact.phone);


    }


    @Override
    public int getItemCount() {
        return contactList.size();
    }

    // 2. ViewHolder 클래스를 만든다.
    // 이 클래스는 contact_row.xml 화면에 있는 뷰를 연결시키는 클래스이다.
    // 빨간색 눌러서 생성자 만든다.
    // 화면과 연결할 자바 변수를 만든다.
    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView txtName;
        TextView txtPhone;
        ImageView imgDelete;
        CardView cardView;
        // 3. 생성자 안에다 연결시키는 코드를 작성한다.
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtName = itemView.findViewById(R.id.txtName);
            txtPhone = itemView.findViewById(R.id.txtPhone);
            imgDelete = itemView.findViewById(R.id.imgDelete);
            cardView = itemView.findViewById(R.id.cardView);


            // 카드뷰 클릭시 수정기능
            cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 카드뷰를 클릭하면 처리할 코드 작성

                    // 1. 유저가 몇번째 행을 클릭했는지, 인덱스로 알려준다.
                    int index = getAdapterPosition();

                    // 2. 이 인덱스에 저장되어있는 데이터를 가져온다.
                    Contact contact = contactList.get(index);

                    // 3. 아이디, 이름, 전화번호를, 수정하는 화면으로 데이터를 넘겨준다.
                    Intent intent = new Intent(context, EditActivity.class);
                    intent.putExtra("contact", contact);
//                    intent.putExtra("id", contact.id);
//                    intent.putExtra("name", contact.name);
//                    intent.putExtra("phone", contact.phone);
                    context.startActivity(intent);
                }
            });

            // 삭제기능
            imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // X 이미지 누르면, 해당 주소록 삭제하도록 개발!

                    // 1. 어떤 행을 눌렀는지 정보를 얻어온다. 인덱스
                    deleteIndex = getAdapterPosition();




                    // 3. 알러트 다이얼로그를 띄운다.
                    AlertDialog.Builder alert = new AlertDialog.Builder(context);
                    alert.setTitle("주소록 삭제");
                    alert.setMessage("정말 삭제하시겠습니까 ?");

                    alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        // 긍정 버튼을 눌렀을 때의 함수
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            DatabaseHandler db = new DatabaseHandler(context);
                            Contact contact = contactList.get(deleteIndex);
                            db.deleteContact(contact);
                            db.close();

                            contactList.remove(deleteIndex);
                            notifyDataSetChanged();
                            // adapter에서 activity 리프레시
//                            Intent intent = ((Activity)context).getIntent();
//                            ((Activity)context).finish(); //현재 액티비티 종료 실시
//                            ((Activity)context).overridePendingTransition(0, 0); //효과 없애기
//                            ((Activity)context).startActivity(intent); //현재 액티비티 재실행 실시
//                            ((Activity)context).overridePendingTransition(0, 0); //효과 없애기

                        }
                    });
                    // 부정버튼을 눌렀을때
                    // 리스너 메소드는 필요 없으니 null로 설정
                    alert.setNegativeButton("No", null);
                    alert.show();


                }
            });
        }
    }
}
