package compe496.parkingbuddy;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class StructureOneLevel3Layout extends AppCompatActivity {

    private static final String TAG = "Final Window";


    public int[] level3ValuesC;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_structure_one_level3_layout);

        //Grab the passed data for current structure and school selection from mainActivity
        TextView currentSchoolSelection = findViewById(R.id.sdsu_currentSchoolTextView);
        TextView currentStructureSelection = findViewById(R.id.sdsu_currentStructure);
        TextView currentFloorSelection = findViewById(R.id.sdsu_currentFloor);

        TextView section1 = findViewById(R.id.section_1);
        TextView section2 = findViewById(R.id.section_2);
        TextView section3 = findViewById(R.id.section_3);
        TextView section4 = findViewById(R.id.section_4);
        TextView section5 = findViewById(R.id.section_5);
        TextView section6 = findViewById(R.id.section_6);
        TextView section7 = findViewById(R.id.section_7);
        TextView section8 = findViewById(R.id.section_8);

        level3ValuesC = getIntent().getIntArrayExtra("level3ValuesB");

//        String sec2 =String.format("%c",(level3ValuesC[1]/22)*100);
//        String sec4 =String.format("%c",((level3ValuesC[2] + level3ValuesC[3])/103)*100);
//        String sec5 = String.format("%c",(level3ValuesC[4]/22)*100);
//        String sec6 = String.format("%c", level3ValuesC[0] + level3ValuesC[5]);

//        section1.setText(String.valueOf(level3ValuesC[0]));
        section2.setText(String.valueOf((level3ValuesC[1])));
//        section3.setText(String.valueOf(level3ValuesC[2]));
        section4.setText(String.valueOf(((level3ValuesC[2]))));
        section5.setText(String.valueOf(((level3ValuesC[4]))));
        section6.setText(String.valueOf(((level3ValuesC[5]))));
//        section7.setText(String.valueOf(level3ValuesC[6]));
//        section8.setText(String.valueOf(level3ValuesC[7]));


        currentSchoolSelection.setText(getIntent().getStringExtra("schoolSelectionFinal"));
        currentStructureSelection.setText(getIntent().getStringExtra("currentStructureFinal"));
        currentFloorSelection.setText(getIntent().getStringExtra("currentLevel"));

        // Refreshes section values
//        downloadJSON("http://cpe-76-93-136-117.san.res.rr.com/stock_service.php"); //<------ CHANGE THIS AS APPROPRIATE
        downloadJSON("http://nwzbwjlj.p18.rt3.io/stock_service.php");

        //secVal();
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);//must store the new intent unless getIntent() will return the old one
    }

    public void secVal() {

        TextView section1 = findViewById(R.id.section_1);
        TextView section2 = findViewById(R.id.section_2);
        TextView section3 = findViewById(R.id.section_3);
        TextView section4 = findViewById(R.id.section_4);
        TextView section5 = findViewById(R.id.section_5);
        TextView section6 = findViewById(R.id.section_6);
        TextView section7 = findViewById(R.id.section_7);
        TextView section8 = findViewById(R.id.section_8);


//        TextView id[] = {section1, section2, section3, section4, section5, section6, section7, section8};
        TextView id[] = {section2, section4, section5, section6};

        for (int a = 0; a < 4; a++) {
            // Grabs previous section values and update text with new values
            String oldVal = id[a].getText().toString();
            String WOPercent[] = oldVal.split("%");
            int val = Integer.valueOf(WOPercent[0]);
            if (val > 100){
                val = 100;}
            id[a].setText(Integer.toString(val) );

            // Updates text background color based off density values
            if (val >= 3) { //Red
                if(a == 1)
                {
                    section3.setBackgroundColor(Color.argb(0x75, 0xcc, 0x29, 0x00));
                }
                else if (a == 3)
                {
                    section1.setBackgroundColor(Color.argb(0x75, 0xcc, 0x29, 0x00));
                }
                id[a].setBackgroundColor(Color.argb(0x75, 0xcc, 0x29, 0x00));
            } else if (val >= 2) { //Orange
                if(a == 1)
                {
                    section3.setBackgroundColor(Color.argb(0x75, 0xff, 0xA5, 0x00));
                }
                else if (a==3)
                {
                    section1.setBackgroundColor(Color.argb(0x75, 0xff, 0xA5, 0x00));
                }
                id[a].setBackgroundColor(Color.argb(0x75, 0xff, 0xA5, 0x00));
            } else if (val >= 1) { //Yellow
                if (a == 1) {
                    section3.setBackgroundColor(Color.argb(0x75, 0xff, 0xff, 0x00));
                }
                else if (a == 3) {
                    section1.setBackgroundColor(Color.argb(0x75, 0xff, 0xff, 0x00));
                }
                id[a].setBackgroundColor(Color.argb(0x75, 0xff, 0xff, 0x00));
//            } else if (val >= 25) { //Yellowish Green
//                id[a].setBackgroundColor(Color.argb(0x75, 0xad, 0xff, 0x2f));
            } else if (val < 1) { //Green
                if (a == 1) {
                    section3.setBackgroundColor(Color.argb(0x75, 0x00, 0xff, 0x00));
                }
                else if (a == 3) {
                    section1.setBackgroundColor(Color.argb(0x75, 0x00, 0xff, 0x00));
                }
                id[a].setBackgroundColor(Color.argb(0x75,0x00,0xff,0x00));
            }
        }
        // Calls Refresh handler every 1.5 sec
        //refresh(1500);
    }


    /** downloadJSON function to interpret data sent from Server */
    private void downloadJSON(final String urlWebService) {

        class DownloadJSON extends AsyncTask<Void, Void, String> {

            //this method will be called before execution
            //you can display a progress bar or something
            //so that user can understand that he should wait
            //as network operation may take some time
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            //this method will be called after execution
            //so here we are displaying a toast with the json string
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);

                //Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();


                try{
                    JSONArray a = new JSONArray(s);

                    for(int i = 0; i <  a.length();i++){
                        JSONObject o = a.getJSONObject(i);
                        if (i == 2){
                            level3ValuesC[0] = o.optInt("Section_1");
                            level3ValuesC[1] = o.optInt("Section_2");
                            level3ValuesC[2] = o.optInt("Section_3");
                            level3ValuesC[3] = o.optInt("Section_4");
                            level3ValuesC[4] = o.optInt("Section_5");
                            level3ValuesC[5] = o.optInt("Section_6");
                            level3ValuesC[6] = o.optInt("Section_7");
                            level3ValuesC[7] = o.optInt("Section_8");

                           Log.wtf(TAG, "Floor 3 Values: " + "[ " + level3ValuesC[0] +
                                    " " + level3ValuesC[1] + " " + level3ValuesC[2] + " " + level3ValuesC[3] + " " +
                                    " " + level3ValuesC[4] + " " + level3ValuesC[5] + " " + level3ValuesC[6] + " " +level3ValuesC[7] + "]");
                        }
                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }


                TextView section1 = findViewById(R.id.section_1);
                TextView section2 = findViewById(R.id.section_2);
                TextView section3 = findViewById(R.id.section_3);
                TextView section4 = findViewById(R.id.section_4);
                TextView section5 = findViewById(R.id.section_5);
                TextView section6 = findViewById(R.id.section_6);
                TextView section7 = findViewById(R.id.section_7);
                TextView section8 = findViewById(R.id.section_8);

                level3ValuesC = getIntent().getIntArrayExtra("level3ValuesB");


//        section1.setText(String.valueOf(level3ValuesC[0]));
                section2.setText(String.valueOf(((level3ValuesC[1]))));
//        section3.setText(String.valueOf(level3ValuesC[2]));
                section4.setText(String.valueOf(((level3ValuesC[2]))));
                section5.setText(String.valueOf(((level3ValuesC[4]))));
                section6.setText(String.valueOf(((level3ValuesC[5]))));
//                section7.setText(String.valueOf(level3ValuesC[6]));
//                section8.setText(String.valueOf(level3ValuesC[7]));




                refresh(5000);
                secVal();

            }

            //in this method we are fetching the json string
            @Override
            protected String doInBackground(Void... voids) {
                try {
                    //creating a URL
                    URL url = new URL(urlWebService);

                    //Opening the URL using HttpURLConnection
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();

                    //StringBuilder object to read the string from the service
                    StringBuilder sb = new StringBuilder();

                    //We will use a buffered reader to read the string from service
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    //A simple string to read values from each line
                    String json;

                    //reading until we don't find null
                    while ((json = bufferedReader.readLine()) != null) {

                        //appending it to string builder
                        sb.append(json + "\n");
                    }

                    //finally returning the read string
                    return sb.toString().trim();

                } catch (Exception e) {
                    Log.e(TAG, "Explanation of what was being attempted when the exception was thrown during JSON grab", e);
                    //This will pop up on the screen upon APP instantiation -if the JSON wasn't read
                    return "JSON RETURN ERROR";
                }
            }
        }
        DownloadJSON getJSON = new DownloadJSON();
        getJSON.execute();
    }

    // Refresh Handler (in milliseconds)
    public void refresh(int ms) {
        // Handler- allows app to send and process threads after a certain time period
        final Handler handler = new Handler();
        // Runnable- content (secVal) intended to be executed by a thread (handler)
        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
//                downloadJSON("http://cpe-76-93-136-117.san.res.rr.com/stock_service.php");
                downloadJSON("http://nwzbwjlj.p18.rt3.io/stock_service.php");

        }
        };
        // What adds the runnable file into the queue and run after a specific time period
        handler.postDelayed(runnable, ms);

    }
}
