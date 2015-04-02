package com.example.dash.httpjason;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;


public class MainActivity extends ActionBarActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener{


    TextView tvIsConnected;
    RadioGroup rg1;
    RadioButton rbF,rbC;
    EditText etNum1;
    TextView tvResult;
    Button btnConvert;
    private double bit1;
    private double bit2;
    String tipoc;
    String tipof;
    double c5;
    double c;
    String o;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvIsConnected = (TextView) findViewById(R.id.tvIsConnected);

        rg1=(RadioGroup)findViewById(R.id.rg1);
        rbC=(RadioButton)findViewById(R.id.rbC);
        rbF=(RadioButton)findViewById(R.id.rbF);
        etNum1=(EditText)findViewById(R.id.etNum1);
        tvResult=(TextView)findViewById(R.id.tvResult);
        btnConvert=(Button)findViewById(R.id.btnConvert);
        rg1=(RadioGroup)findViewById(R.id.rg1);

        rg1.setOnCheckedChangeListener(this);

        btnConvert.setOnClickListener(this);

        // check if you are connected or not
        if(isConnected()){
            tvIsConnected.setBackgroundColor(0xFF00CC00);
            tvIsConnected.setText("You are conncted");
        }
        else{
            tvIsConnected.setText("You are NOT conncted");
        }


        // call AsynTask to perform network operation on separate thread
        //new HttpAsyncTask().execute("http://hmkcode.appspot.com/rest/controller/get.json");
        new HttpAsyncTask().execute("https://raw.githubusercontent.com/kiroarama/appMovil/master/README.json");
        //new HttpAsyncTask().execute("https://console.developers.google.com/m/cloudstorage/b/resulset12345.appspot.com/o/proces.json");


    }



    public static String GET(String url){
		InputStream inputStream = null;
		String result = "";
		try {

			// create HttpClient
			HttpClient httpclient = new DefaultHttpClient();

			// make GET request to the given URL
			HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

			// receive response as inputStream
			inputStream = httpResponse.getEntity().getContent();

			// convert inputstream to string
			if(inputStream != null)
				result = convertInputStreamToString(inputStream);
			else
				result = "Did not work!";

		} catch (Exception e) {
			Log.d("InputStream", e.getLocalizedMessage());
		}

		return result;
	}

    private static String convertInputStreamToString(InputStream inputStream) throws IOException{
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    public boolean isConnected(){
    	ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
    	    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
    	    if (networkInfo != null && networkInfo.isConnected())
    	    	return true;
    	    else
    	    	return false;
    }
    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            return GET(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
        	Toast.makeText(getBaseContext(), "Datos Recividos!", Toast.LENGTH_LONG).show();

        	try {



				JSONObject json = new JSONObject(result);

               JSONArray nom = json.getJSONArray("valores");
                JSONArray nom2 = json.getJSONArray("tipo");





                bit1=nom.getJSONObject(0).getDouble("num1");
                bit2=nom.getJSONObject(1).getDouble("num2");
                tipoc=nom2.getJSONObject(0).getString("cen");
                tipof=nom2.getJSONObject(1).getString("far");





			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
       }
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onCheckedChanged(RadioGroup group, int checkedId) {



        if (rbC.isChecked()) {

            o="c";



        }
        if (rbF.isChecked()) {

            o="f";



        }


    }

    public void onClick (View v) {

        if (etNum1.getText().toString().equals("")) {
            Toast t0 = Toast.makeText(this, "No hay nada que convertir", Toast.LENGTH_LONG);
            t0.show();
        } else {


            c = Double.parseDouble(etNum1.getText().toString());

            switch (o) {
                case "c":
                    //tvResult.setText(c4);
                    c5=(c-bit2)/bit1;
                    //c5 = c4 + 1;

                    tvResult.setText("" + c5+" "+tipoc);
                    break;
                case "f":
                    c5 = (c  * bit1)+bit2;


                    tvResult.setText(""+c5+" "+tipof);
                    break;

            }



            rbC.setChecked(false);
            rbF.setChecked(false);

        }
    }
}
