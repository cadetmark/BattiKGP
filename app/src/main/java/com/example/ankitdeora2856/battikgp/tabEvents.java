package com.example.ankitdeora2856.battikgp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ankitdeora2856 on 17-03-2016.
 */
public class tabEvents extends Fragment {

    String myJSON;
    String JSON_STRING;
    private static final String TAG_ID = "eventId";
    private static final String TAG_RESULTS="result";
    private static final String TAG_NAME = "eventName";
    JSONArray events = null;
    ArrayList<HashMap<String, String>> eventList;
    ListView list;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.tab_events, container, false);
        list = (ListView) v.findViewById(R.id.eventListView);
        eventList = new ArrayList<HashMap<String,String>>();
        //System.out.println("before data");
        getData();

        return v;
    }

    protected void showList(){
        try {
            JSONObject jsonObj = new JSONObject(myJSON);
            events = jsonObj.getJSONArray(TAG_RESULTS);
            //System.out.println("Inside try");
            for(int i=0;i<events.length();i++){
                JSONObject c = events.getJSONObject(i);
                String id = c.getString(TAG_ID);
                String name = c.getString(TAG_NAME);
                HashMap<String,String> events = new HashMap<String,String>();

                events.put(TAG_ID,id);
                events.put(TAG_NAME,name);
                eventList.add(events);
            }
            //System.out.println("Inside try1");
            ListAdapter myAdapter = new SimpleAdapter(
                    getContext(), eventList, R.layout.event_list,
                    new String[]{TAG_NAME},
                    new int[]{R.id.id_event_name}
            );

            list.setAdapter(myAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void getData(){
        class GetDataJSON extends AsyncTask<String, Void, String> {

            @Override
            protected String doInBackground(String... params) {
                try {
                    //System.out.println("Inside url1");
                    URL url = new URL("http://battikgp.net23.net/Event.php");
                    //System.out.println("Inside url2");
                    HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();

                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setUseCaches(true);

                    //System.out.println("Inside url3");
                    InputStream inputStream = httpURLConnection.getInputStream();
                    //System.out.println("Inside url4");
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                    //System.out.println("Inside url4b");
                    StringBuilder stringBuilder = new StringBuilder();
                    while ((JSON_STRING = bufferedReader.readLine())!=null)
                    {
                        //System.out.println("Inside url5");
                        stringBuilder.append(JSON_STRING+"\n");
                    }
                   // System.out.println("Inside url6");
                    bufferedReader.close();
                    inputStream.close();
                    httpURLConnection.disconnect();
                    return stringBuilder.toString().trim();

                } catch (MalformedURLException e) {
                    //System.out.println("Inside url7");
                    e.printStackTrace();
                } catch (IOException e) {
                    //System.out.println("Inside url8");
                    e.printStackTrace();
                }
                //System.out.println("Inside url9");
                return null;
            }

            @Override
            protected void onPostExecute(String result){
                //TextView tv = (TextView) getView().findViewById(R.id.id_eventTextView);
                //tv.setText(result.toString());
                //System.out.println("after post execute");

                myJSON=result;
                showList();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute();
    }

}
