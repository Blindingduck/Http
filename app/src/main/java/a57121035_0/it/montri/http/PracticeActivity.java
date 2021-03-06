package a57121035_0.it.montri.http;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PracticeActivity extends AppCompatActivity {
    TextView tvCount;
    Spinner spn_Condition,spn_Name,spn_Lastname,spn_Gender,spn_Day;
    EditText edt_Searchname,edt_Searchlastname;
    Button btn_Search;
    ListView lv_Show;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);
        spn_Condition = (Spinner) findViewById(R.id.spnCondition);
        spn_Name = (Spinner) findViewById(R.id.spnName);
        spn_Lastname = (Spinner) findViewById(R.id.spnLastname);
        spn_Gender = (Spinner) findViewById(R.id.spnGender);
        spn_Day = (Spinner) findViewById(R.id.spnDay);
        edt_Searchname = (EditText) findViewById(R.id.edtSearchname);
        edt_Searchlastname = (EditText) findViewById(R.id.edtSearchlastname);
        tvCount = (TextView) findViewById(R.id.tvCount);
        tvCount.setVisibility(View.INVISIBLE);
        btn_Search = (Button) findViewById(R.id.btnSearch);
        spn_Name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0){
                    edt_Searchname.setText("");
                    edt_Searchname.setVisibility(View.INVISIBLE);
                }
                else{
                    edt_Searchname.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        spn_Lastname.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0) {
                    edt_Searchlastname.setText("");
                    edt_Searchlastname.setVisibility(View.INVISIBLE);
                }
                else{
                    edt_Searchlastname.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btn_Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url="10.0.2.2/searchAll.php";
                switch (spn_Condition.getSelectedItemPosition()){
                    case 0 : url+="?condition=";
                        break;
                    case 1 : url+="?condition=AND";
                        break;
                    case 2 : url+="?condition=OR";
                        break;
                }
                switch (spn_Name.getSelectedItemPosition()){
                    case 0 : url +="&name";
                        break;
                    case 1 : url +="&name=LIKE\'"+edt_Searchname.getText().toString()+"\'";
                        break;
                    case 2 : url +="&name=NOT LIKE\'"+edt_Searchname.getText().toString()+"\'";
                        break;
                    case 3 : url +="&name=LIKE\'"+edt_Searchname.getText().toString()+"%\'";
                        break;
                    case 4 : url +="&name=LIKE\'%"+edt_Searchname.getText().toString()+"\'";
                        break;
                    case 5 : url +="&name=LIKE\'%"+edt_Searchname.getText().toString()+"%\'";
                        break;
                }
                switch (spn_Lastname.getSelectedItemPosition()){
                    case 0 : url +="&lastname";
                        break;
                    case 1 : url +="&lastname=LIKE\'"+edt_Searchlastname.getText().toString()+"\'";
                        break;
                    case 2 : url +="&lastname=NOT LIKE \'"+edt_Searchlastname.getText().toString()+"\'";
                        break;
                    case 3 : url +="&lastname=LIKE\'"+edt_Searchlastname.getText().toString()+"\'%";
                        break;
                    case 4 : url +="&lastname=LIKE\'%"+edt_Searchlastname.getText().toString()+"\'";
                        break;
                    case 5 : url +="&lastname=LIKE\'%"+edt_Searchlastname.getText().toString()+"%\'";
                        break;
                }
                switch (spn_Gender.getSelectedItemPosition()){
                    case 0 : url +="&gender";
                        break;
                    case 1 : url +="&gender=M";
                        break;
                    case 2 : url +="&gender=F";
                        break;
                }
                switch (spn_Day.getSelectedItemPosition()){
                    case 0 : url +="&day";
                        break;
                    case 1 : url +="&day=WED";
                        break;
                    case 2 : url +="&day=THU";
                        break;
                }
                List<HashMap<String,String>> myList = new ArrayList<HashMap<String, String>>();
                SimpleAdapter list_Adapter = null;
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);
                HttpResponse httpResponse = null;
                BufferedReader bufferedReader = null;
                String data = "";
                    try {
                        httpResponse = httpClient.execute(httpPost);
                        bufferedReader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
                        data = bufferedReader.readLine();
                        String[] data1 = data.split("[|]");
                        int i = 0;
                        while (i < data1.length) {
                            String[] data2 = data1[i].split("[$]");
                            HashMap<String, String> myHashmap = new HashMap<String, String>();
                            myHashmap.put("No", data2[0]);
                            myHashmap.put("ID", data2[1]);
                            myHashmap.put("Name", data2[2]);
                            myHashmap.put("Lastname", data2[3]);
                            myHashmap.put("url", data2[4]);
                            myList.add(myHashmap);
                            i++;
                        }
                        String[] from = new String[]{"No", "ID", "Name","Lastname","url"};
                        int[] to = new int[]{R.id.tvNo, R.id.tvID, R.id.tvName,R.id.tvLastname,R.id.tvUrl};
                        list_Adapter = new SimpleAdapter(PracticeActivity.this, myList, R.layout.search_layout, from, to);
                        lv_Show.setAdapter(list_Adapter);

                    } catch (IOException e) {
                        e.printStackTrace();
                        Toast.makeText(PracticeActivity.this, "Error", Toast.LENGTH_SHORT).show();
                    }

            }
        });
    }
}
