package org.istanbulhs.workshop.asynctask;

import java.util.ArrayList;
import java.util.List;

import org.istanbulhs.util.HttpUtil;
import org.istanbulhs.workshop.UserListActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

//Arayuzde kullaniciyi hasta etmek istemedigimiz icin webten veri cekme gibi isleri
//ayri bir thread'de yapmak istiyoruz. Internet yavassa isler cok yavaslayabilir
//Bunu kolaylikla yapmak icin de Android bize AsyncTask diye bir class yapmis
//Burda <> icindeki 3 arguman sirasiyla suna denk geliyor: Params, Progress, Result
//Birincisi execute(Params...) ve doInBackgroung(Params...)
//Ikincisi onProgressUpdate(Progress...) 
//ucuncusu onPostExecute(Result)
//Detaylar ve daha duzgun anlatim: http://developer.android.com/reference/android/os/AsyncTask.html
public class GetListAsyncTask extends AsyncTask<Void, Void, String> {
    private UserListActivity activity;
    private ProgressDialog progressDialog;
    
    //UserListActivity'nin metodunu cagirmak istedimiz icin onu constructor'da aliyoruz
    public GetListAsyncTask(UserListActivity activity) {
        this.activity = activity;
    }
    
    //Ayri thread calismadan arayuzde yapmak istediklerimizi bu metodda yapiyoruz. 
    //Mesela Yukleniyor diye bir pencere aciyoruz
    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("Yükleniyor...");
        progressDialog.show();
        
        super.onPreExecute();
    }

    //Bu metoddaki kodlar ayri bir thread'de calisiyor
    //Boylelikle arayuz bloke olmuyor
    @Override
    protected String doInBackground(Void... arg0) {
        //Servisimiz hali hazirda hizli calistigi icin koydugumuz pencereyi
        //hakkaten gormek icin koydugumuz komik kod
        //2 saniye boyunca uyu
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            //Bu arada asagidaki kod android'de log atilmak icin kullanilan kod
            Log.e("hackerspace", e.toString());
            Log.e("hackerspace", "mesaj", e);  
        }
 
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
        
        return responseString;
    }

    //Bu method yeniden ana threadimizde yani arayuzun calistigi thread'de calisiyor
    @Override
    protected void onPostExecute(String responseString) {
       //donen string'i bir string listesine parse ediyoruz
        List<String> nameList = null;
        
        try {
            //JSON string'i parse ediyoruz
            nameList = parseStringIntoList(responseString);
            
        } catch (JSONException e) {
            //eger parse edersek ve hata yersek en azindan elimizde bos liste olsun
            nameList = new ArrayList<String>();
            
            Log.e("hackerspace", "Could not parse json string");
            Log.e("hackerspace", responseString);
        }
       
        //Listeyi useractivitylist'e veriyoruz
        this.activity.setList(nameList);
        
        //Yukleniyor penceresini kapatiyoruz. 
        this.progressDialog.dismiss();
        
        super.onPostExecute(responseString);
    }
    
    //Donen JSON string'i string listesine ceviren kod
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
            //optString metodu ile okuyoruz, eger name yoksa objenin icinde, 
            //"???" stringini dondurecek
            String name = jsonObject.optString("name", "???");
            
            nameList.add(name);
        }
        
        return nameList;
    }
    

}
