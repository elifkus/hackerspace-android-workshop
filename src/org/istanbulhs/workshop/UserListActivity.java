package org.istanbulhs.workshop;

import java.util.ArrayList;
import java.util.List;

import org.istanbulhs.util.HttpUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

public class UserListActivity extends ListActivity {
    static final String[] COUNTRIES = new String[] {
        "USA", "Turkey", "Dünya", "UK", "Ireland", "PRC", "The Netherlands",
        "Abhazia", "Neverland", "Home", "Wastelands", "Middle Earth",
        "Wild Wild West", "The Root of All Evil", "Hasanpaşa", "Mahmut"
        };
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //xml'den arayuzu yukluyoruz
        setContentView(R.layout.user_list);
        
        //bundle'i acip icinden username'i okuyoruz
        String username = getIntent().getExtras().getString("username");
        
        //username'i arayuzdeki textview'de gozukmesi icin set ediyoruz
        TextView textView = (TextView)findViewById(R.id.usernametext);
        textView.setText(username);
        
        //kullanici listesini almak icin kullanacagimiz liste
        //http://service.istanbulhs.org/user/list/
        String url = "http://service.istanbulhs.org/user/list/";
        
        String responseString = null;
        
        //request'i at
        try {
            responseString = HttpUtil.getResponseStringForHttpRequest(url);
        } catch (Exception e) {                
            Log.e("hackerspace", e.toString());
            Log.e("hackerspace", "mesaj", e);    
        }
        
        //donen string'i bir string listesine parse ediyoruz
        List<String> nameList = null;
        
        try {
            nameList = parseStringIntoList(responseString);
            
        } catch (JSONException e) {
            //eger parse edersek hata yersek en azindan elimizde bos liste olsun
            nameList = new ArrayList<String>();
            
            Log.e("hackerspace", "Could not parse json string");
            Log.e("hackerspace", responseString);
        }
        
        //Activity'miz ListActivity tipinden. Elimizdeki liste ile bir ListAdapter yaratiyoruz,
        //ve Activity'ye set ediyoruz
        // ListAdapter icin daha detayli bilgi icin http://developer.android.com/reference/android/widget/ListAdapter.html
        //ListAdapter, string listesi ile listview arayuzu arasinda kopru gorevi goruyor. Listeden ListView olusmasini sagliyor 
        
        ListAdapter listAdapter = new ArrayAdapter<String>(this, R.layout.list_item, nameList);
        setListAdapter(listAdapter);

    }
    
    private List<String> parseStringIntoList(String text) throws JSONException {
        //parse ettigimiz isimleri koymak icin bir liste yaratiyoruz
        List<String> nameList = new ArrayList<String>();
        
        //gelen string'in json array oldugunu biliyoruz
        //android bizim icin bu string'i array'a ceviriyor 
        //asagidaki class ile
        JSONArray jsonList = new JSONArray(text);
        //Bize donen string
        /*[{"id":"1","name":"birsey"},{"id":"2","name":"spehlivan"},
         * {"id":"3","name":"serdarp"},{"id":"4","name":"serdarpehlivan"},
         * {"id":"5","name":"serdar"},{"id":"6","name":"test"},{"id":"7","name":"ser"},
         * {"id":"8","name":"test2"},{"id":"9","name":"test3"},{"id":"10","name":"test4"},
         * {"id":"11","name":"zaaa"},{"id":"12","name":"elifkus"},{"id":"13","name":"safkan"},
         * {"id":"14","name":"ardicsssss"},{"id":"15","name":"hilalnazli"},
         * {"id":"16","name":"arc"}]*/
        
        //Bu JSONArray'i string listesine cevirmek istiyoruz
        for (int i=0; i<jsonList.length(); i++) {
            //Her objeden name'i alip listeye koyuyoruz
            JSONObject jsonObject = jsonList.getJSONObject(i);
            //optString metodu ile okuyoruz, eger name yoksa objenin icinde 
            //"???" stringini gonderecek
            String name = jsonObject.optString("name", "???");
            
            nameList.add(name);
        }
        
        return nameList;
    }
    

}
