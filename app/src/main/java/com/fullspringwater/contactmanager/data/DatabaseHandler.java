package com.fullspringwater.contactmanager.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.fullspringwater.contactmanager.model.Contact;
import com.fullspringwater.contactmanager.util.Util;

import java.util.ArrayList;

public class DatabaseHandler extends SQLiteOpenHelper {
    public DatabaseHandler(@Nullable Context context) {
        super(context, Util.DATABASE_NAME, null, Util.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        // 테이블 생성
        String CREATE_CONTACT_TABLE = "create table " + Util.TABLE_NAME + "(" +
                Util.KEY_ID + " integer primary key, " + Util.KEY_NAME + " text, " +
                Util.KEY_PHONE + " text)";

        Log.i("MyContact", "테이블 생성문 : " + CREATE_CONTACT_TABLE);

        // 쿼리 실행
        sqLiteDatabase.execSQL(CREATE_CONTACT_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        // 기존의 contact 테이블을 삭제하고,
        String DROP_TABLE = "drop table " + Util.TABLE_NAME;
        sqLiteDatabase.execSQL(DROP_TABLE, new String[]{Util.DATABASE_NAME});

        // 새롭게 테이블을 다시 만든다
        onCreate(sqLiteDatabase);
    }

    // 우리가 앱 동작 시키는데 필요한 SQL 문이 적용된 함수들을 만든다.
    // CRUD 관련 함수들을 만든다.
    public void addContact(Contact contact){
        // 데이터베이스를 가져온다.
        SQLiteDatabase db = this.getWritableDatabase();

        // 테이블의 컬럼이름과 해당 데이터를 매칭해서 넣어준다.
        ContentValues values = new ContentValues();
        values.put(Util.KEY_NAME, contact.name);
        values.put(Util.KEY_PHONE, contact.phone);
        db.insert(Util.TABLE_NAME, null, values);
        db.close();
    }

    // 주소록 데이터 가져오기
    // 1개의 주소록 데이터만 가져오기
    // select * from contact where id=1;
    public Contact getContact(int id){
        // 데이터베이스를 가져온다.
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. 쿼리문 만든다.
        // 방법 1
        Cursor cursor = db.rawQuery("select * from " + Util.TABLE_NAME + " where id = " + id, null);
        // 방법 2
//        Cursor cursor2 = db.rawQuery("select * from contacts where id = ?", new String[]{""+id});

        if(cursor != null){
            cursor.moveToFirst();
        }

        // 데이터를 가져오려면, 컬럼의 인덱스를 넣어주면 된다
        // id를 가져오는 방법
        // cursor.getInt(0);
        // name 을 가져오는 방법
        // cursor.getString(1);
        // phone 을 가져오는 방법
        // cursor.getString(2);

        // DB 에 저장된 데이터를 메모리에다 만들어줘야, cpu가 처리할 수 있다.
        Contact contact = new Contact(
                cursor.getInt(0), cursor.getString(1), cursor.getString(2));

        db.close();
        return contact;
    }

    // 주소록 데이터 전체 가져오기
    // select * from contact;
    public ArrayList<Contact> getAllContact(){
        // 데이터베이스를 가져온다.
        SQLiteDatabase db = this.getReadableDatabase();

        // 2. 쿼리문 만든다.
        Cursor cursor = db.rawQuery("select * from " + Util.TABLE_NAME + ";", null);

        ArrayList<Contact> contactList = new ArrayList<>();

//        if(cursor.moveToFirst()) {
//            for(int i=0; i< cursor.getCount(); i++) {
//                Contact contact = new Contact(
//                        cursor.getInt(0), cursor.getString(1), cursor.getString(2));
//                contactList.add(contact);
//                cursor.moveToNext();
//            }
//        }

        if(cursor.moveToFirst()){
            do{
                Contact contact = new Contact(
                    cursor.getInt(0), cursor.getString(1), cursor.getString(2));
                contactList.add(contact);
            }while(cursor.moveToNext());
        }

        db.close();
        return contactList;

    }

    // 데이터 수정하는 함수
    public void updateContact(Contact contact){
        // 데이터베이스를 가져온다.
        SQLiteDatabase db = this.getWritableDatabase();

        // 방법 1
//        ContentValues values = new ContentValues();
//        values.put(Util.KEY_NAME, contact.name);
//        values.put(Util.KEY_PHONE, contact.phone);
//
//        db.update(Util.TABLE_NAME, values, Util.KEY_ID + "=?",
//                new String[]{contact.id+""});

        // 방법 2
        // 쿼리문 만든다.
        db.execSQL("update " + Util.TABLE_NAME +
                        " set " + Util.KEY_NAME + "= ?, " +  Util.KEY_PHONE + " = ? " +
                        "where " + Util.KEY_ID + "= ?;",
                new String[]{contact.name, contact.phone, contact.id+""});

        // 방법 3
//        db.execSQL("update " + Util.TABLE_NAME +
//                        " set " + Util.KEY_NAME + "= " + contact.name +
//                        ", " +  Util.KEY_PHONE + " = " + contact.phone +
//                        "where " + Util.KEY_ID + "= " + contact.id);
        db.close();
    }

    public void deleteContact(Contact contact){
        // delete from contact where id= 1;

        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + Util.TABLE_NAME +
                " where " + Util.KEY_ID + " = ?;", new String[]{contact.id+""});
        db.close();
    }

}
