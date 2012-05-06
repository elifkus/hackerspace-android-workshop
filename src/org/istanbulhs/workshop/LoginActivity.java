package org.istanbulhs.workshop;

import org.istanbulhs.util.HttpUtil;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity implements OnClickListener {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        //R.layout.main id'li view'u yukle
        setContentView(R.layout.main);
        
         //Yuklu olan contentview'dan yani main xml'den git id'si registerButton olan view'u getir
         View registerView = this.findViewById(R.id.registerButton);
    
         //registerView'u Button'a cevir (onun aslinda bir button oldugunu biliyorum)
         Button registerButton = (Button)registerView; 
         
         //butona click eventi ekle
         registerButton.setOnClickListener(this);
         
         //Content view'dan butonu getir, event ekle. (Usttekinin aynisi sadece daha kompakt)
         Button loginButton = (Button)this.findViewById(R.id.loginButton);
         loginButton.setOnClickListener(this);
         
    }

    //clicklendigi zaman bu metod cagiriliyor
    @Override
    public void onClick(View v) {
        //bu LoginActivity hem loginButon'a hem de registerButon'a clicklistener olarak 
        //ekledigimiz icin her ikisinin onclicklerinde bu metod cagiriliyor
        //Bu yuzden onclick'i gonderen kimse ona gore farkli kod calistiriyoruz
        if (v.getId() == R.id.registerButton) 
        {
            //http://service.istanbulhs.org/user/register/elifkus/elifkus@gmail.com/aaaabbbb
            //View v --> clicklenen view
            //Her Button aslinda bir view ayni zamanda. 
            //Button class'i View'u extend ediyor
            //Biz id'den dolayi bu gelen view'un bir buton oldugunu biliyoruz
            
            EditText usernameBox = (EditText)findViewById(R.id.kullaniciEditText);
            //String'e ceviriyoruz ve trim ediyoruz. 
            //Trim etmek demek etrafindaki whitespace karakterlerini silmek demek
            //Bunlar url'de kalirsa hata aliriz
            String username = usernameBox.getText().toString().trim();
            
            //yukardakinin aynisi email icin
            EditText emailEditText = (EditText)findViewById(R.id.emailEditText);
            String email = emailEditText.getText().toString().trim();
            Log.i("hackerspace","email: " + email);
            
            //yukardakinin aynisi sifre icin
            EditText passwordBox = (EditText)findViewById(R.id.sifreEditText);
            String password = passwordBox.getText().toString().trim();
            
            //http://service.istanbulhs.org/user/register/elifkus/elifkus@gmail.com/aaaabbbb
            String url = "http://service.istanbulhs.org/"+"user/register/" + username + "/" 
                                                            + email + "/"+ password;

            String responseString = null;
            
            //request'i at
            try {
                responseString = HttpUtil.getResponseStringForHttpRequest(url);
            } catch (Exception e) {                
                Log.e("hackerspace", e.toString());
                Log.e("hackerspace", "mesaj", e);    
            }
            //Donen string'i butona set et
            Button button = (Button)v;
            button.setText(responseString);
            
        } else if (v.getId() == R.id.loginButton) {
            
            Button button = (Button)v;
            
            //kullanici adindaki edittext'te kullanici adinin ne girildigne bakiyoruz
            EditText usernameBox = (EditText)findViewById(R.id.kullaniciEditText);
            //String'e ceviriyoruz ve trim ediyoruz. 
            //Trim etmek demek etrafindaki whitespace karakterlerini silmek demek
            //Bunlar url'de kalirsa hata aliriz
            String username = usernameBox.getText().toString().trim();
            
            //yukardakinin aynisi sifre icin
            EditText passwordBox = (EditText)findViewById(R.id.sifreEditText);
            String password = passwordBox.getText().toString().trim();
            
            // http://service.istanbulhs.org/user/login/<kullanici>/<sifre>
            // Orn: http://service.istanbulhs.org/user/login/elifkus/aaaabbbb
            //Request'i atacagimiz url
            String url = "http://service.istanbulhs.org/"+"user/login/" + username + "/" + password;
            
            String responseString = null;
            
            //request'i at
            try {
                responseString = HttpUtil.getResponseStringForHttpRequest(url);
            } catch (Exception e) {                
                Log.e("hackerspace", e.toString());
                Log.e("hackerspace", "mesaj", e);    
            }
            //Donen string'i butona set et
            button.setText(responseString);
            
            //Bir sonraki aktiviteye username'i gondermek icin bir bundle olusturuyoruz
            Bundle dataBundle = new Bundle();
            dataBundle.putString("username", username);
            
            //Yeni aktivitenin baslatmak icin yeni bir intent yaratiyoruz.
            //Bundle'imizi bu intent'e ekliyoruz
            Intent newActivityIntent = new Intent(this, UserListActivity.class);
            newActivityIntent.putExtras(dataBundle);
            this.startActivity(newActivityIntent);
            
        }
        
        
    }
}