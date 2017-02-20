package de.huerse.jagott;


import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
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

public class JaGottParser {

    //Variables for Database request for todays text
    private String jsonResult = "empty";
    private String url_heute = "http://www.ja-gott.de/ja_gott_heute.php";
    private String url_archiv = "http://www.ja-gott.de/ja-gott-archiv.php";
    private String url_archiv_text = "http://www.ja-gott.de/ja_gott_archiv_text.php";

    ListView mArchivListView;
    ArrayList<NameValuePair> mNameValuePairs = new ArrayList<NameValuePair>();

    public void ParseAndDisplayJaGottHeute(String jsonResult) {

        try {
            JSONObject jsonResponse = new JSONObject(jsonResult);
            JSONArray jsonMainNode = jsonResponse.optJSONArray("ja_gott_heute");

            for (int i = 0; i < jsonMainNode.length(); i++) {
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                String date = jsonChildNode.optString("title");
                String text = jsonChildNode.optString("introtext");
                //String outPut = name + "-" + number;
                //employeeList.add(createEmployee("employees", outPut));
                String split[] = text.split("\r\n");

                String message = "";
                for (int j = 1; j < split.length; j++) {
                    if (split[j] != "\n")
                        message = message + split[j];
                }

                //Global.GlobalJaGottHeuteText = date + "\n" + Html.fromHtml(split[0]).toString().trim() + "\n" + Html.fromHtml(message).toString().trim() + "\n";
                Global.GlobalJaGottCurrentText = date + "\n" + Html.fromHtml(split[0]).toString().trim() + "\n" + Html.fromHtml(message).toString().trim() + "\n";
                Global.GlobalJaGottCurrentDate = date;
                Global.GlobalJaGottCurrentVerse = Html.fromHtml(split[0]).toString().trim();
                Global.GlobalJaGottCurrentMessage = Html.fromHtml(message).toString().trim();

                ArrayList<String> jgtHeuteResult = new ArrayList<String>();
                jgtHeuteResult.add(0,Global.GlobalJaGottCurrentDate );
                jgtHeuteResult.add(1,Global.GlobalJaGottCurrentVerse );
                jgtHeuteResult.add(2,Global.GlobalJaGottCurrentMessage + "\n\n\n\n\n\n\n\n\n\n\n\n\n" );

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

//                TextView dateView = (TextView) Global.GlobalMainActivity.findViewById(R.id.dateView);
//                dateView.setText(date);
//                TextView verse = (TextView) Global.GlobalMainActivity.findViewById(R.id.verseView);
//                verse.setText(text);
//                TextView messageView = (TextView) Global.GlobalMainActivity.findViewById(R.id.messageView);
//                messageView.setText(message);

                ArrayList<String> jgtHeuteResult = new ArrayList<String>();
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

    public void ParseAndDisplayJaGottArchivText(String jsonResult) {

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

                TextView dateView = (TextView) Global.GlobalMainActivity.findViewById(R.id.dateView);
                dateView.setText(date);
                TextView verse = (TextView) Global.GlobalMainActivity.findViewById(R.id.verseView);
                verse.setText(Html.fromHtml(split[0]).toString().trim());

                String message = "";
                for (int j = 1; j < split.length; j++) {
                    if (split[j] != "\n")
                        message = message + split[j];
                }

                TextView messageView = (TextView) Global.GlobalMainActivity.findViewById(R.id.messageView);
                messageView.setText(Html.fromHtml(message).toString().trim() + "\n\n\n\n\n\n\n");

                //Global.GlobalMainActivity.setButtonListenerforArchiveTextView();

                Global.GlobalJaGottCurrentDate = date;
                Global.GlobalJaGottCurrentVerse = Html.fromHtml(split[0]).toString().trim();
                Global.GlobalJaGottCurrentMessage = Html.fromHtml(message).toString().trim();
                Global.GlobalJaGottCurrentText = date + "\n" + Html.fromHtml(split[0]).toString().trim() + "\n" + Html.fromHtml(message).toString().trim() + "\n";
            }
        } catch (Exception e) {
            //Toast.makeText(getApplicationContext(), "Error" + e.toString(),
            //      Toast.LENGTH_SHORT).show();
            try {
                String date = "Ups, da ist wohl was schief gelaufen.";
                String text = "Es tut uns sehr leid, aber dein Text kann nicht aus dem Archiv geladen werden.";
                String message = "Bitte überprüfe deine Internetverbindung. Wenn es weiterhin nicht funktioniert, schicke eine Mail an andre@ja-gott.de";

                //TextView dateView = (TextView) Global.GlobalMainActivity.findViewById(R.id.dateView);
                //dateView.setText(date);
                //TextView verse = (TextView) Global.GlobalMainActivity.findViewById(R.id.verseView);
                //verse.setText(text);
                //verse.setTextColor(Global.GlobalMainActivity.getResources().getColor(R.color.white));
                //TextView messageView = (TextView) Global.GlobalMainActivity.findViewById(R.id.messageView);
                //messageView.setText(message);
            } catch (Exception ex) {
                //View has been changed while loading text. Error Message cannot be displayed.
                //Do nothing!
            }
        }
    }

    public void ParseAndDisplayJaGottArchiv(String jsonResult) {

        ArrayList<String> archivList = new ArrayList<String>();

        try {
            JSONObject jsonResponse = new JSONObject(jsonResult);
            JSONArray jsonMainNode = jsonResponse.optJSONArray("ja_gott_archiv");

            for(int i = 0; i<jsonMainNode.length();i++)
            {
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                String name = jsonChildNode.optString("title");
                archivList.add(name);
            }

//            mArchivListView = (ListView) Global.GlobalMainActivity.findViewById(R.id.archiv_list_view);
//            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
//                    Global.GlobalMainActivity,
//                    android.R.layout.simple_list_item_1, archivList);
//            mArchivListView.setAdapter(arrayAdapter);
//            mArchivListView.setOnItemClickListener(mArchivListHandler);

            RecyclerView rv = (RecyclerView)Global.GlobalMainActivity.findViewById(R.id.container);

            JgtArchivRVAdapter adapter = new JgtArchivRVAdapter(archivList);
            rv.setAdapter(adapter);

        } catch (Exception e) {

        }
    }

    public void clickOnArchiveText(String selected) {
        mNameValuePairs.add(new BasicNameValuePair("title", selected));

        ArchivTextJsonReadTask task = new ArchivTextJsonReadTask();
        // passes values for the urls string array
        task.execute(new String[]{url_archiv_text});
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

            catch (ClientProtocolException e) {
                //e.printStackTrace();
            } catch (IOException e) {
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
        String rLine = "";
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

    public void refreshJaGottHeute()
    {
        HeuteJsonReadTask task = new HeuteJsonReadTask();
        // passes values for the urls string array
        task.execute(new String[]{url_heute});
    }

    public void refreshJaGottArchiv()
    {
        ArchivJsonReadTask task = new ArchivJsonReadTask();
        // passes values for the urls string array
        task.execute(new String[]{url_archiv});
    }
}
