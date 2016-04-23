package learner.sukesh.socialwelfare;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import learner.sukesh.socialwelfare.utils.Constants;

public class SignupActivity extends AppCompatActivity {

    EditText name,email,password,repassword;
    int num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Firebase.setAndroidContext(getApplicationContext());
    }

    public void signup(View view)
    {
        name= (EditText) findViewById(R.id.name);
        email= (EditText) findViewById(R.id.email);
        password= (EditText) findViewById(R.id.password);
        repassword= (EditText) findViewById(R.id.repassword);
        Firebase mref=new Firebase(Constants.FIREBASE_URL);
        if(password.getText().toString().equalsIgnoreCase(repassword.getText().toString()))
        {
            mref.createUser(email.getText().toString(), password.getText().toString(), new Firebase.ResultHandler() {
                @Override
                public void onSuccess() {
                    savedata(name.getText().toString());
                    saveFirebase(email.getText().toString(),name.getText().toString());
                    Intent i=new Intent(SignupActivity.this,MainActivity.class);
                    startActivity(i);
                    finish();
                }

                @Override
                public void onError(FirebaseError firebaseError) {
                    Toast.makeText(SignupActivity.this,"error signing up",Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    public void saveFirebase(String mail,String nameUser)
    {
        Firebase mref=new Firebase(Constants.FIREBASE_URL);
        mref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                num = Integer.parseInt(dataSnapshot.child("users").child("number").getValue().toString());
                sign(num);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public void sign(int number)
    {
        Firebase mref=new Firebase(Constants.FIREBASE_URL);
        mref.child("users").child(String.valueOf(number)).child("Email").setValue(email.getText().toString());
        mref.child("users").child(String.valueOf(number)).child("Name").setValue(name.getText().toString());
        mref.child("users").child("number").setValue(String.valueOf(number + 1));
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
