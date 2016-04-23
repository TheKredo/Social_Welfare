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

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import learner.sukesh.socialwelfare.utils.Constants;

public class SubmitIssue extends AppCompatActivity {

    int num;
    EditText title,disc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_submit_issue);

        title= (EditText) findViewById(R.id.editText);
        disc= (EditText) findViewById(R.id.editText2);
        Firebase.setAndroidContext(getApplicationContext());

    }

    public void submit(View view)
    {
        if(isNetworkAvailable())
        {
            Firebase ref=new Firebase(Constants.FIREBASE_URL);
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    num = Integer.parseInt(dataSnapshot.child("issue").child("number").getValue().toString());
                    sub(num);
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {

                }
            });
        }
        else
        {
            Toast.makeText(SubmitIssue.this,"Internet not connected. Plz try again.",Toast.LENGTH_SHORT).show();
        }
    }

    public void sub(int number)
    {
        Firebase ref=new Firebase(Constants.FIREBASE_URL);
        ref.child("issue").child(String.valueOf(number)).child("Title").setValue(title.getText().toString());
        ref.child("issue").child(String.valueOf(number)).child("Discription").setValue(disc.getText().toString());
        ref.child("issue").child(String.valueOf(number)).child("Name").setValue(load());
        ref.child("issue").child("number").setValue(String.valueOf(number + 1));
        Intent intent=new Intent(SubmitIssue.this,MainActivity.class);
        startActivity(intent);
        finish();
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

    public boolean isNetworkAvailable() {
        ConnectivityManager cm = (ConnectivityManager)
                getApplication().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        // if no network is available networkInfo will be null
        // otherwise check if we are connected
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

}
