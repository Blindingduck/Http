package a57121035_0.it.montri.http;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.Buffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    EditText edt_ID;
    Button btn_Show,btn_Insert,btn_Practice;
    ListView lv_Show;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        edt_ID = (EditText) findViewById(R.id.edtID);
        btn_Show = (Button) findViewById(R.id.btnShow);
        btn_Insert = (Button) findViewById(R.id.btnInsert);
        btn_Practice = (Button) findViewById(R.id.btnPractice);
        lv_Show = (ListView) findViewById(R.id.lvShow);

        btn_Practice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,PracticeActivity.class);
                startActivity(intent);
            }
        });

        btn_Insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,InsertActivity.class);
                startActivity(intent);
            }
        });

        btn_Show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                List<HashMap<String,String>> myList = new ArrayList<HashMap<String, String>>();
                SimpleAdapter list_Adapter = null;
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://10.0.2.2/android/android3.php?ID="+edt_ID.getText().toString());
                HttpResponse httpResponse = null;
                BufferedReader bufferedReader = null;
                String data = "";
                if(!edt_ID.getText().equals("")) {
                    try {
                        httpResponse = httpClient.execute(httpPost);
                        bufferedReader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
                        data = bufferedReader.readLine();
                        String[] data1 = data.split("[|]");
                        int i = 0;
                        while (i < data1.length) {
                            String[] data2 = data1[i].split("[$]");
                            HashMap<String, String> myHashmap = new HashMap<String, String>();
                            myHashmap.put("ID", data2[0]);
                            myHashmap.put("Name", data2[1]);
                            myHashmap.put("Url", data2[3]);
                            myList.add(myHashmap);
                            i++;
                        }
                        String[] from = new String[]{"ID", "Name", "Url"};
                        int[] to = new int[]{R.id.tvID, R.id.tvName, R.id.tvUrl};
                        list_Adapter = new SimpleAdapter(MainActivity.this, myList, R.layout.listviewshow_layout, from, to);
                        lv_Show.setAdapter(list_Adapter);

                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    myAlertDialog("Please Input ID","Error","OK");
                }
            }
        });
    }
    public void myAlertDialog(String Message,String Title,String btnText){
                AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setMessage(Message)
                .setCancelable(false)
                .setTitle(Title)
                .setPositiveButton(btnText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }).show();
    }
}
