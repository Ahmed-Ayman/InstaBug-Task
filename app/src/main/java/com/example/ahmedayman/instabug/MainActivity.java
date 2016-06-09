package com.example.ahmedayman.instabug;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    //init
    ListView listView;
    CustomAdabter adapter ;
    List<String> repoNames = new ArrayList<>();
    List<String> descriptions = new ArrayList<>();
    List<String> userNames = new ArrayList<>();
    List<String> urls = new ArrayList<>();
    List<String> ownerUrls = new ArrayList<>();
    List<Boolean> fork = new ArrayList<>();
    TextView noConnection ; //Text view to display when there's no connection.

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        noConnection= (TextView) findViewById(R.id.no_connection);
        //execute the AsyncTask
        new FetchTheRepo().execute("https://api.github.com/users/square/repos");
    //display the data on the list view
        adapter = new CustomAdabter(MainActivity.this,repoNames,descriptions,userNames,fork);
        listView=(ListView) findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

     //On long click listener
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
     //  creating Dialoge
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            String [] dialogeItems = {"Go to the main repository","Go to the owner repository"};
            builder.setItems(dialogeItems, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if (which == 0) {
                                String link = urls.get(position);
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse(link));
                                startActivity(intent);
                            }
                            else{
                                String link = ownerUrls.get(position);
                                Intent intent = new Intent(Intent.ACTION_VIEW);
                                intent.setData(Uri.parse(link));
                                startActivity(intent);
                            }
                        }
                    });

            builder.create().show();
            return false;
        }
    });
    }

    /**
     * Asynk Task to fetch the Json for the API
     */
    public class FetchTheRepo extends AsyncTask<String , Void ,String > {

        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           progressDialog = ProgressDialog.show(MainActivity.this, "Loading",
                    "Loading the Repo's......", true);
        }
        @Override
        protected String doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }
            HttpURLConnection urlConnection = null;
            BufferedReader bufferedReader = null;
            String JsonResponse = null;

            try {
                String link = params[0];
                URL url = new URL(link);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();
                InputStream stream = urlConnection.getInputStream();
                StringBuffer json = new StringBuffer();

                if (stream == null) {
                    return null;
                }
                bufferedReader = new BufferedReader(new InputStreamReader(stream));

                String line ;
                while ((line = bufferedReader.readLine()) !=null)
                {
                    json.append(line+"\n");
                }
                if (json.length() == 0)
                    return null ;

                JsonResponse = json.toString() ;

                Log.e("json",JsonResponse);
            } catch (IOException e) {
                progressDialog.dismiss();
                }
            finally {
                if (urlConnection!= null)
                {
                    urlConnection.disconnect();
                }
                if(bufferedReader!= null)
                {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }//finally check if not null to close

            return  JsonResponse ;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s==null)
            {
                noConnection.setText(R.string.connection);
            }
            else {
           progressDialog.dismiss();
                try {
                    getMyData(s);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter.notifyDataSetChanged();
            }
        }

        /**
         *
          * @param s  Json Response
         * @throws JSONException
         * this method will fill the arrays with the needed data.
         */
        private void getMyData(String s ) throws JSONException {
            JSONArray arrayOfData  = new JSONArray(s);

        for (int i =  0 ; i < 10;i++)
        {
            // Parsing the Json
            JSONObject item = arrayOfData.getJSONObject(i);
            String name = item.getString("name");
            String userName = item.getString("full_name");
            JSONObject owner = item.getJSONObject("owner");
            String ownerUrl = owner.getString("html_url");
            String htmlUrl = item.getString("html_url");
            String description = item.getString("description");

            //filling the Array lists with the data
            repoNames.add(name);
            descriptions.add(description);
            userNames.add(userName);
            urls.add(htmlUrl);
            ownerUrls.add(ownerUrl);

            //checking the Fork status
            String forkStatus =item.getString("fork");

            if(forkStatus.equals("false")) {
                fork.add(false);
            }
            else
                fork.add(true);
        }
        }
    }

    }