package com.example.ahmedayman.instabug;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AbsListView;
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

public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {
    //init
    ListView listView;
    CustomAdabter adapter;
    List<RepoModel> repoModelList;
    TextView noConnection; //Text view to be displayed when there's no connection.
    DataBaseHandler db;
    TextView reached;

    private static final String url = "https://api.github.com/users/square/repos";
    private static final String accessToken = "&access_token=81be0f2d7d8741d6b15e46c394a81a538b4e053f";
    private static final String TAG_REPONAME = "name";
    private static final String TAG_USERNAME = "full_name";
    private static final String TAG_OWNER = "owner";
    private static final String TAG_OWNERURL = "html_url";
    private static final String TAG_URL = "html_url";
    private static final String TAG_DESCREPTION = "description";
    private static final String TAG_FORK = "fork";
    boolean flag_loading = false;
    private int itemsNum = 10, pages = 1;
    int count = 0, length = -1;
    int here = 0;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        noConnection = (TextView) findViewById(R.id.no_connection);
        reached = (TextView) findViewById(R.id.reached);
        listView = (ListView) findViewById(R.id.list_view);


        repoModelList = new ArrayList<>();
        db = new DataBaseHandler(this);

        //display the data on the list view
        adapter = new CustomAdabter(MainActivity.this, repoModelList);
        listView.setAdapter(adapter);
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            //On long click listener
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                //  creating Dialoge
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                String[] dialogeItems = {"the main repository", "the owner repository"};
                builder.setItems(dialogeItems, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            String link = repoModelList.get(position).getUrl();
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(link));
                            startActivity(intent);
                        } else {
                            String link = repoModelList.get(position).getOwnerUrl();
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

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {

                if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount != 0) {

                    if (length == 0)
                        reached.setVisibility(View.VISIBLE);
                    else
                        reached.setVisibility(View.GONE);

                    if (flag_loading == false && length != 0) {
                        flag_loading = true;
                        count++;
                        addItems(pages++, itemsNum);
                    }
                }
            }
        });
        addItems(1, pages);
    }

    // add Items to the List View
    private void addItems(int page, int itemsNum) {
        if (count != 0)
            swipeRefreshLayout.setRefreshing(true);

        new FetchTheRepo().execute(url.concat("?per_page=" + itemsNum + "&page=" + pages) + accessToken);
        flag_loading = false;
    }

    @Override
    public void onRefresh() {
        addItems(pages++, itemsNum);
    }

    /**
     * Async Task to fetch the Json and to display it on the List View by Notifying the Adabter
     */
    public class FetchTheRepo extends AsyncTask<String, Void, String> {

        ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            if (count == 0) {
                progressDialog = ProgressDialog.show(MainActivity.this, "Loading",
                        "Loading the Repo's......", true);
            }
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

                if (stream == null) {
                    return null;
                }

                StringBuffer json = new StringBuffer();
                bufferedReader = new BufferedReader(new InputStreamReader(stream));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    json.append(line + "\n");
                }
                if (json.length() == 0)
                    return null;
                JsonResponse = json.toString();
                //Parse
                try {
                    parse(JsonResponse);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }//finally check if not null to close
            return JsonResponse;
        }

        @Override
        protected void onPostExecute(String s) {
            swipeRefreshLayout.setRefreshing(false);
            if (progressDialog.isShowing())
                progressDialog.dismiss();
            // Question to Ask !! is there any other way to display this noConnection TextViw ?!
            if (s == null) {
                if (repoModelList.size() == 0)
                noConnection.setVisibility(View.VISIBLE);
                List<RepoModel> data = db.getAllRepos();
                for (int i = here; i < data.size(); i++) {
                    repoModelList.add(data.get(i));
                    here++;
                }
                if (repoModelList.size()!=0) {
                    adapter.notifyDataSetChanged();
                    noConnection.setVisibility(View.GONE);
                }
            } else
                adapter.notifyDataSetChanged();
        }

        /**
         * @param s Json Response
         * @throws JSONException this method will fill the arrays with the needed data.
         */
        private void parse(String s) throws JSONException {
            JSONArray arrayOfData = new JSONArray(s);
            length = arrayOfData.length();
            for (int i = 0; i < arrayOfData.length(); i++) {
                RepoModel model = new RepoModel();
                // Parsing the Json
                JSONObject item = arrayOfData.getJSONObject(i);
                JSONObject owner = item.getJSONObject(TAG_OWNER);
                model.setDescription(item.getString(TAG_DESCREPTION));
                model.setFork(item.getString(TAG_FORK));
                model.setUsername(item.getString(TAG_USERNAME));
                model.setRepoName(item.getString(TAG_REPONAME));
                model.setUrl(item.getString(TAG_URL));
                model.setOwnerUrl(owner.getString(TAG_OWNERURL));
                repoModelList.add(model);
                db.addRepo(model);
            }
        }
    }
}