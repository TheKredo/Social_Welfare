package learner.sukesh.socialwelfare;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import learner.sukesh.socialwelfare.utils.Constants;

public class MainActivity extends AppCompatActivity {

    String[] title;
    String[] disc;
    String[] nameUser;
    int num;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView= (ListView) findViewById(R.id.issueList);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MainActivity.this,SubmitIssue.class);
                startActivity(i);
            }
        });

        Firebase.setAndroidContext(getApplicationContext());
        if(isNetworkAvailable())
        {
            starting();
        }
        else
        {
            Toast.makeText(MainActivity.this,"Internet not connected. Plz try again.",Toast.LENGTH_SHORT).show();
        }
    }

    public void starting()
    {
        Firebase mref=new Firebase(Constants.FIREBASE_URL);
        mref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                num = Integer.parseInt(dataSnapshot.child("issue").child("number").getValue().toString());
                if (num > 0) {
                    title = new String[num];
                    disc = new String[num];
                    nameUser = new String[num];
                    for (int i = 0; i < num; i++) {
                        title[i] = dataSnapshot.child("issue").child(String.valueOf(i)).child("Title").getValue().toString();
                        disc[i] = dataSnapshot.child("issue").child(String.valueOf(i)).child("Discription").getValue().toString();
                        nameUser[i] = dataSnapshot.child("issue").child(String.valueOf(i)).child("Name").getValue().toString();
                    }

                    IssueList type = new IssueList(MainActivity.this, title, nameUser);
                    listView.setAdapter(type);

                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent i = new Intent(MainActivity.this, IssueDetail.class);
                            i.putExtra("Position", String.valueOf(position));
                            startActivity(i);
                        }
                    });
                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            saveChanges();
            Intent i=new Intent(MainActivity.this,LoginActivity.class);
            startActivity(i);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void saveChanges()
    {
        String Filename = "Sukesh.txt";
        String nick="";
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
