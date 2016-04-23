package learner.sukesh.socialwelfare;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import learner.sukesh.socialwelfare.utils.Constants;

public class IssueDetail extends AppCompatActivity {

    int position;
    String title;
    String disc;
    TextView d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_issue_detail);

        Firebase.setAndroidContext(getApplicationContext());

        Bundle b=getIntent().getExtras();
        if(b!=null)
        {
            position=Integer.parseInt(b.getString("Position"));
        }

        d= (TextView) findViewById(R.id.disc);

        Firebase ref=new Firebase(Constants.FIREBASE_URL);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                title=dataSnapshot.child("issue").child(String.valueOf(position)).child("Title").getValue().toString();
                disc=dataSnapshot.child("issue").child(String.valueOf(position)).child("Discription").getValue().toString();
                setTitle(title);
                d.setText(disc);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }
}
