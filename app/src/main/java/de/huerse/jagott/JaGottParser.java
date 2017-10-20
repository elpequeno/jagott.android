package de.huerse.jagott;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

import de.huerse.jagott.Adapters.JgtArchivRVAdapter;
import de.huerse.jagott.Adapters.JgtHeuteRVAdapter;

public class JaGottParser {

    //Variables for Database request for todays text
    private String jsonResult = "empty";

    private ArrayList<NameValuePair> mNameValuePairs = new ArrayList<>();

    static HashMap<String, String> mTitleToIdMapper = new HashMap<>();

    private void ParseAndDisplayJaGottHeute(String jsonResult) {

        try {
            JSONObject jsonResponse = new JSONObject(jsonResult);
            JSONArray jsonMainNode = jsonResponse.optJSONArray("ja_gott_heute");

            for (int i = 0; i < jsonMainNode.length(); i++) {
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                String date = jsonChildNode.optString("title");
                String text = jsonChildNode.optString("introtext");
                String split[] = text.split("\r\n");

                String message = "";
                for (int j = 1; j < split.length; j++) {
                    if (!split[j].equals("\n"))
                        message = message + split[j];
                }

                Global.GlobalJaGottCurrentText = date + "\n" + Html.fromHtml(split[0]).toString().trim() + "\n" + Html.fromHtml(message).toString().trim() + "\n";
                Global.GlobalJaGottCurrentDate = date;
                Global.GlobalJaGottCurrentVerse = Html.fromHtml(split[0]).toString().trim();
                Global.GlobalJaGottCurrentMessage = Html.fromHtml(message).toString().trim();

                ArrayList<String> jgtHeuteResult = new ArrayList<>();
                jgtHeuteResult.add(0,Global.GlobalJaGottCurrentDate );
                jgtHeuteResult.add(1,Global.GlobalJaGottCurrentVerse );
                jgtHeuteResult.add(2,Global.GlobalJaGottCurrentMessage );

                RecyclerView rv = (RecyclerView)Global.GlobalMainActivity.findViewById(R.id.container);

                JgtHeuteRVAdapter adapter = new JgtHeuteRVAdapter(jgtHeuteResult);
                rv.setAdapter(adapter);
            }
        } catch (Exception e) {
            //Toast.makeText(getApplicationContext(), "Error" + e.toString(),
            //      Toast.LENGTH_SHORT).show();
            try {
                String date = "Ups, da ist wohl was schief gelaufen.";
                String text = "Es tut uns sehr leid, aber für heute kann kein Text geladen werden.";
                String message = "Bitte überprüfe deine Internetverbindung. Möglicherweise gibt es auch keinen aktuellen Text. Kontrolliere das am Besten mal auf ja-gott.de. Wenn es weiterhin nicht funktioniert, schicke eine Mail an andre@ja-gott.de";

                //Global.GlobalJaGottHeuteText = date + "\n" + text + "\n" + message + "\n";
                Global.GlobalJaGottCurrentText = date + "\n" + text + "\n" + message + "\n";
                Global.GlobalJaGottCurrentDate = date;
                Global.GlobalJaGottCurrentVerse = text;
                Global.GlobalJaGottCurrentMessage = message;

                ArrayList<String> jgtHeuteResult = new ArrayList<>();
                jgtHeuteResult.add(0,Global.GlobalJaGottCurrentDate );
                jgtHeuteResult.add(1,Global.GlobalJaGottCurrentVerse );
                jgtHeuteResult.add(2,Global.GlobalJaGottCurrentMessage );

                RecyclerView rv = (RecyclerView)Global.GlobalMainActivity.findViewById(R.id.container);

                JgtHeuteRVAdapter adapter = new JgtHeuteRVAdapter(jgtHeuteResult);
                rv.setAdapter(adapter);
            } catch (Exception ex) {
                //View has been changed while loading text. Error Message cannot be displayed.
                //Do nothing!
            }
        }
    }

    private void ParseAndDisplayJaGottArchivText(String jsonResult) {

        try {
            JSONObject jsonResponse = new JSONObject(jsonResult);
            JSONArray jsonMainNode = jsonResponse.optJSONArray("ja_gott_archiv_text");

            for (int i = 0; i < jsonMainNode.length(); i++) {
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                String date = jsonChildNode.optString("title");
                String text = jsonChildNode.optString("introtext");
                //String outPut = name + "-" + number;
                //employeeList.add(createEmployee("employees", outPut));
                String split[] = text.split("\r\n");

                String message = "";
                for (int j = 1; j < split.length; j++) {
                    if (!split[j].equals("\n"))
                        message = message + split[j];
                }

                Global.GlobalJaGottCurrentDate = date;
                Global.GlobalJaGottCurrentVerse = Html.fromHtml(split[0]).toString().trim();
                Global.GlobalJaGottCurrentMessage = Html.fromHtml(message).toString().trim();
                Global.GlobalJaGottCurrentText = date + "\n" + Html.fromHtml(split[0]).toString().trim() + "\n" + Html.fromHtml(message).toString().trim() + "\n";

                ArrayList<String> jgtHeuteResult = new ArrayList<>();
                jgtHeuteResult.add(0,Global.GlobalJaGottCurrentDate );
                jgtHeuteResult.add(1, Global.GlobalJaGottCurrentVerse);
                jgtHeuteResult.add(2,Global.GlobalJaGottCurrentMessage);

                RecyclerView rv = (RecyclerView)Global.GlobalMainActivity.findViewById(R.id.container);

                //JgtHeuteRVAdapter adapter = new JgtHeuteRVAdapter(jgtHeuteResult);
                //rv.setAdapter(adapter);
                //define a new Intent for the second Activity
                Intent intent = new Intent(Global.GlobalMainActivity, JgtArchivTextActivity.class);
                Bundle b = new Bundle();
                b.putStringArrayList("jgtHeuteResult", jgtHeuteResult);
                intent.putExtras(b);

                //start the second Activity
                Global.GlobalMainActivity.startActivity(intent);
            }
        } catch (Exception e) {
            //Toast.makeText(getApplicationContext(), "Error" + e.toString(),
            //      Toast.LENGTH_SHORT).show();
            //try {
                //String date = "Ups, da ist wohl was schief gelaufen.";
                //String text = "Es tut uns sehr leid, aber dein Text kann nicht aus dem Archiv geladen werden.";
                //String message = "Bitte überprüfe deine Internetverbindung. Wenn es weiterhin nicht funktioniert, schicke eine Mail an andre@ja-gott.de";

                //TextView dateView = (TextView) Global.GlobalMainActivity.findViewById(R.id.dateView);
                //dateView.setText(date);
                //TextView verse = (TextView) Global.GlobalMainActivity.findViewById(R.id.verseView);
                //verse.setText(text);
                //verse.setTextColor(Global.GlobalMainActivity.getResources().getColor(R.color.white));
                //TextView messageView = (TextView) Global.GlobalMainActivity.findViewById(R.id.messageView);
                //messageView.setText(message);
            //} catch (Exception ex) {
                //View has been changed while loading text. Error Message cannot be displayed.
                //Do nothing!
            //}
        }
    }

    private void ParseAndDisplayJaGottArchiv(String jsonResult) {

        ArrayList<String> archivList = new ArrayList<>();
        mTitleToIdMapper = new HashMap<>();

        try {
            JSONObject jsonResponse = new JSONObject(jsonResult);
            JSONArray jsonMainNode = jsonResponse.optJSONArray("ja_gott_archiv");

            for(int i = 0; i<jsonMainNode.length();i++)
            {
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                String name = jsonChildNode.optString("title");
                String id = jsonChildNode.optString("id");
                mTitleToIdMapper.put(name, id);
                archivList.add(name);
            }

            RecyclerView rv = (RecyclerView)Global.GlobalMainActivity.findViewById(R.id.container);

            JgtArchivRVAdapter adapter = new JgtArchivRVAdapter(archivList);
            rv.setAdapter(adapter);

        } catch (Exception e) {
            // Do Nothing;
        }
    }

    public void clickOnArchiveText(String selected) {
        mNameValuePairs.add(new BasicNameValuePair("id", mTitleToIdMapper.get(selected)));

        String url_archiv_text = "http://www.ja-gott.de/archiv-text-jagott.php";
        ArchivTextJsonReadTask task = new ArchivTextJsonReadTask();
        // passes values for the urls string array
        task.execute(url_archiv_text);
    }

    // Async Task to access the web
    private class HeuteJsonReadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(params[0]);
            try {
                HttpResponse response = httpclient.execute(httppost);
                jsonResult = inputStreamToString(
                        response.getEntity().getContent()).toString();
            }
            catch (Exception e) {
                //e.printStackTrace();
            }
            return "bla";
        }

        @Override
        protected void onPostExecute(String result) {
            new JaGottParser().ParseAndDisplayJaGottHeute(jsonResult);
        }
    }// end async task

    // Async Task to access the web
    private class ArchivJsonReadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(params[0]);
            try {
                HttpResponse response = httpclient.execute(httppost);
                jsonResult = inputStreamToString( response.getEntity().getContent()).toString();

            }
            catch (Exception e) {
                //e.printStackTrace();
            }
            return "bla";
        }

        @Override
        protected void onPostExecute(String result) {
            new JaGottParser().ParseAndDisplayJaGottArchiv(jsonResult);
        }
    }// end async task

    // Async Task to access the web
    private class ArchivTextJsonReadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(params[0]);

            try {
                httppost.setEntity(new UrlEncodedFormEntity(mNameValuePairs));
                HttpResponse response = httpclient.execute(httppost);
                jsonResult = inputStreamToString( response.getEntity().getContent()).toString();

            }
            catch (Exception e) {
                //e.printStackTrace();
            }
            return "bla";
        }

        @Override
        protected void onPostExecute(String result) {
            new JaGottParser().ParseAndDisplayJaGottArchivText(jsonResult);
        }
    }// end async task

    private StringBuilder inputStreamToString(InputStream is) {
        String rLine;
        StringBuilder answer = new StringBuilder();
        BufferedReader rd = new BufferedReader(new InputStreamReader(is));

        try {
            while ((rLine = rd.readLine()) != null) {
                answer.append(rLine);
            }
        }

        catch (IOException e) {
            // e.printStackTrace();
            Toast.makeText(Global.GlobalMainActivity.getApplicationContext(),
                    "Error..." + e.toString(), Toast.LENGTH_LONG).show();
        }
        return answer;
    }

    void refreshJaGottHeute()
    {
        String url_heute = "http://www.ja-gott.de/ja_gott_heute.php";
        HeuteJsonReadTask task = new HeuteJsonReadTask();
        // passes values for the urls string array
        task.execute(url_heute);
    }

    void refreshJaGottArchiv()
    {
        String url_archiv = "http://www.ja-gott.de/archiv-jagott.php";
        ArchivJsonReadTask task = new ArchivJsonReadTask();
        // passes values for the urls string array
        task.execute(url_archiv);
    }
}
