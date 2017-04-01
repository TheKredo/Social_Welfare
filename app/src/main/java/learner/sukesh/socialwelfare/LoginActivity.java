package learner.sukesh.socialwelfare;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import learner.sukesh.socialwelfare.utils.Constants;

public class LoginActivity extends AppCompatActivity {

    EditText email,password;
    int num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email= (EditText) findViewById(R.id.email);
        password= (EditText) findViewById(R.id.repassword);
        if(!load().equalsIgnoreCase(""))
        {
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }
        Firebase.setAndroidContext(getApplicationContext());
    }

    public void loginUS(View view)
    {
        if(isNetworkAvailable())
        {
            login();
        }
        else
        {
            Toast.makeText(LoginActivity.this,"Internet not connected. Plz try again.",Toast.LENGTH_SHORT).show();
        }
    }

    public void signup_fromlogin(View view)
    {
        Intent i = new Intent(LoginActivity.this, SignupActivity.class);
        startActivity(i);
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager)
                getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    public void login()
    {
        final Firebase mref=new Firebase(Constants.FIREBASE_URL);
        mref.authWithPassword(email.getText().toString(), password.getText().toString(), new Firebase.AuthResultHandler() {
            @Override
            public void onAuthenticated(AuthData authData) {
                mref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        num=Integer.parseInt(dataSnapshot.child("users").child("number").getValue().toString());
                        for(int i=0;i<num;i++)
                        {
                            String e,n;
                            e=dataSnapshot.child("users").child(String.valueOf(i)).child("Email").getValue().toString();
                            if(e.equalsIgnoreCase(email.getText().toString()))
                            {
                                n=dataSnapshot.child("users").child(String.valueOf(i)).child("Name").getValue().toString();
                                savedata(n);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }

            @Override
            public void onAuthenticationError(FirebaseError firebaseError) {
                Toast.makeText(LoginActivity.this,"Sorry. Could Not Sign You In!",Toast.LENGTH_SHORT).show();
            }
        });
    }

    protected String load()
    {
        String Filename = "Sukesh.txt";
        String name="";
        try {
            FileInputStream fis = getApplication().openFileInput(Filename);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            String sLine = null;

            while (((sLine = br.readLine()) != null)) {
                name += sLine;
            }
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return name;
    }

    public void savedata(String nick)
    {
        String Filename = "Sukesh.txt";
        try {

            FileOutputStream fos = getApplicationContext().openFileOutput(Filename, Context.MODE_PRIVATE);
            fos.write(nick.getBytes());
        } catch(FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
