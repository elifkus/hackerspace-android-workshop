package org.istanbulhs.workshop;

import java.util.List;

import org.istanbulhs.workshop.asynctask.GetListAsyncTask;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

public class UserListActivity extends ListActivity implements OnClickListener {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //xml'den arayuzu yukluyoruz
        setContentView(R.layout.user_list);
        
        //Servisi cagirip kullanici listesi kodunu okuma isini 
        //ayri bir thread'de yapiyoruz
        GetListAsyncTask task = new GetListAsyncTask(this);
        task.execute();
        
        //Harita butonuna onclicklistener ekliyoruz
        //Tiklayinca haritayi gosteren ekran acilacak
        Button mapButton = (Button)findViewById(R.id.mapbutton);
        mapButton.setOnClickListener(this);
        
        //bundle'i acip icinden birinci ekrandan gonderilen username'i okuyoruz
        String username = getIntent().getExtras().getString("username");
        
        //username'i arayuzdeki textview'de gozukmesi icin set ediyoruz
        TextView textView = (TextView)findViewById(R.id.usernametext);
        textView.setText(username);
    }
    
    //Bu metodu GetListAsyncTask'ten cagiriyoruz. Ayri thread'de listeyi aliyoruz 
    //O class'in icinden bu ListActivity'e set ediyoruz. 
    public void setList(List<String> nameList) {
        //Activity'miz ListActivity tipinden. Elimizdeki liste ile bir ListAdapter yaratiyoruz,
        //ve Activity'ye set ediyoruz
        // ListAdapter icin daha detayli bilgi icin http://developer.android.com/reference/android/widget/ListAdapter.html
        //ListAdapter, string listesi ile listview arayuzu arasinda kopru gorevi goruyor. Listeden ListView olusmasini sagliyor 
        
        ListAdapter listAdapter = new ArrayAdapter<String>(this, R.layout.list_item, nameList);
        setListAdapter(listAdapter);        
    }

    //Harita butonuna basinca CustomMapActivity ekranimiz aciliyor
    @Override
    public void onClick(View v) {
        Intent myIntent = new Intent(this, CustomMapActivity.class);
        startActivity(myIntent);
    }
}
