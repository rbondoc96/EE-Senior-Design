package compe496.parkingbuddy;

import android.content.Intent;
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

public class StructureOneLevels extends AppCompatActivity {

    /** Number of spots on level 3 = 247*/
    int level3Count = 247;

    private static final String TAG = "Second Window";

    public int[] level1ValuesB;
    public int[] level2ValuesB;
    public int[] level3ValuesB;
    public int[] level4ValuesB;
    public int[] level5ValuesB;

    @Override
    protected void onCreate(Bundle savedInstanceState) { //Runs as window is opened
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_structure_one_levels);

        //Grab the passed data for current structure and school selection from mainActivity
        TextView currentSchoolSelection = findViewById(R.id.sdsu_currentSchoolTextView);
        TextView currentStructureSelection = findViewById(R.id.sdsu_currentStructure);

        currentSchoolSelection.setText(getIntent().getStringExtra("schoolSelection"));
        currentStructureSelection.setText(getIntent().getStringExtra("currentStructure"));

        ProgressBar sdsuP1Level1;
        ProgressBar sdsuP1Level2;
        ProgressBar sdsuP1Level3;
        ProgressBar sdsuP1Level4;
        ProgressBar sdsuP1Level5;

        sdsuP1Level1 = findViewById(R.id.sdsu_level1_ProgressBar);
        sdsuP1Level2 = findViewById(R.id.sdsu_level2_ProgressBar);
        sdsuP1Level3 = findViewById(R.id.sdsu_level3_ProgressBar);
        sdsuP1Level4 = findViewById(R.id.sdsu_level4_ProgressBar);
        sdsuP1Level5 = findViewById(R.id.sdsu_level5_ProgressBar);

        sdsuP1Level1.setMax(100); //Guesstimating the maximum amount of parking spots
        sdsuP1Level2.setMax(100);
        sdsuP1Level3.setMax(100);
        sdsuP1Level4.setMax(100);
        sdsuP1Level5.setMax(100);

        processExtraData();
    }

    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);//must store the new intent unless getIntent() will return the old one
        processExtraData();
    }

    private void processExtraData(){
        Intent intent = getIntent();
        //use the data received here

        ProgressBar sdsuP1Level1;
        ProgressBar sdsuP1Level2;
        ProgressBar sdsuP1Level3;
        ProgressBar sdsuP1Level4;
        ProgressBar sdsuP1Level5;

        sdsuP1Level1 = findViewById(R.id.sdsu_level1_ProgressBar);
        sdsuP1Level2 = findViewById(R.id.sdsu_level2_ProgressBar);
        sdsuP1Level3 = findViewById(R.id.sdsu_level3_ProgressBar);
        sdsuP1Level4 = findViewById(R.id.sdsu_level4_ProgressBar);
        sdsuP1Level5 = findViewById(R.id.sdsu_level5_ProgressBar);

        level1ValuesB = intent.getIntArrayExtra("level1ValuesA");
        level2ValuesB = intent.getIntArrayExtra("level2ValuesA");
        level3ValuesB = intent.getIntArrayExtra("level3ValuesA");
        level4ValuesB = intent.getIntArrayExtra("level4ValuesA");
        level5ValuesB = intent.getIntArrayExtra("level5ValuesA");


//        downloadJSON("http://cpe-76-93-136-117.san.res.rr.com/stock_service.php"); //<------ CHANGE THIS AS APPROPRIATE
        downloadJSON("http://nwzbwjlj.p18.rt3.io/stock_service.php");

        setProgressBar(getCurrentRelativePercentage(level3ValuesB), sdsuP1Level3);


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
                        if(i == 0){
                            level1ValuesB[0] = o.optInt("Section_1");
                            level1ValuesB[1] = o.optInt("Section_2");
                            level1ValuesB[2] = o.optInt("Section_3");
                            level1ValuesB[3] = o.optInt("Section_4");
                            level1ValuesB[4] = o.optInt("Section_5");
                            level1ValuesB[5] = o.optInt("Section_6");
                            level1ValuesB[6] = o.optInt("Section_7");
                            level1ValuesB[7] = o.optInt("Section_8");


                            //Log.wtf("myWTFtag", "Floor 1 Values: " + "[ " + level1ValuesB[0] +
                            //        " " + level1ValuesB[1] + " " + level1ValuesB[2] + " " + level1ValuesB[3] + " " +
                             //       " " + level1ValuesB[4] + " " + level1ValuesB[5] + " " + level1ValuesB[6] + " " +level1ValuesB[7] + "]");
                        }
                        else if (i == 1){
                            level2ValuesB[0] = o.optInt("Section_1");
                            level2ValuesB[1] = o.optInt("Section_2");
                            level2ValuesB[2] = o.optInt("Section_3");
                            level2ValuesB[3] = o.optInt("Section_4");
                            level2ValuesB[4] = o.optInt("Section_5");
                            level2ValuesB[5] = o.optInt("Section_6");
                            level2ValuesB[6] = o.optInt("Section_7");
                            level2ValuesB[7] = o.optInt("Section_8");

                           // Log.wtf("myWTFtag", "Floor 2 Values: " + "[ " + level2ValuesB[0] +
                           //         " " + level2ValuesB[1] + " " + level2ValuesB[2] + " " + level2ValuesB[3] + " " +
                            //        " " + level2ValuesB[4] + " " + level2ValuesB[5] + " " + level2ValuesB[6] + " " +level2ValuesB[7] + "]");
                        }
                        else if (i == 2){
                            level3ValuesB[0] = o.optInt("Section_1");
                            level3ValuesB[1] = o.optInt("Section_2");
                            level3ValuesB[2] = o.optInt("Section_3");
                            level3ValuesB[3] = o.optInt("Section_4");
                            level3ValuesB[4] = o.optInt("Section_5");
                            level3ValuesB[5] = o.optInt("Section_6");
                            level3ValuesB[6] = o.optInt("Section_7");
                            level3ValuesB[7] = o.optInt("Section_8");

                            Log.wtf("Second Window", "Floor 3 Values: " + "[ " + level3ValuesB[0] +
                                    " " + level3ValuesB[1] + " " + level3ValuesB[2] + " " + level3ValuesB[3] + " " +
                                    " " + level3ValuesB[4] + " " + level3ValuesB[5] + " " + level3ValuesB[6] + " " +level3ValuesB[7] + "]");
                        }
                        else if (i == 3){
                            level4ValuesB[0] = o.optInt("Section_1");
                            level4ValuesB[1] = o.optInt("Section_2");
                            level4ValuesB[2] = o.optInt("Section_3");
                            level4ValuesB[3] = o.optInt("Section_4");
                            level4ValuesB[4] = o.optInt("Section_5");
                            level4ValuesB[5] = o.optInt("Section_6");
                            level4ValuesB[6] = o.optInt("Section_7");
                            level4ValuesB[7] = o.optInt("Section_8");

                            //Log.wtf("myWTFtag", "Floor 4 Values: " + "[ " + level4ValuesB[0] +
                            //        " " + level4ValuesB[1] + " " + level4ValuesB[2] + " " + level4ValuesB[3] + " " +
                             //       " " + level4ValuesB[4] + " " + level4ValuesB[5] + " " + level4ValuesB[6] + " " +level4ValuesB[7] + "]");
                        }
                        else{
                            level5ValuesB[0] = o.optInt("Section_1");
                            level5ValuesB[1] = o.optInt("Section_2");
                            level5ValuesB[2] = o.optInt("Section_3");
                            level5ValuesB[3] = o.optInt("Section_4");
                            level5ValuesB[4] = o.optInt("Section_5");
                            level5ValuesB[5] = o.optInt("Section_6");
                            level5ValuesB[6] = o.optInt("Section_7");
                            level5ValuesB[7] = o.optInt("Section_8");

                            //Log.wtf("myWTFtag", "Floor 5 Values: " + "[ " + level5ValuesB[0] +
                            //        " " + level5ValuesB[1] + " " + level5ValuesB[2] + " " + level5ValuesB[3] + " " +
                            //        " " + level5ValuesB[4] + " " + level5ValuesB[5] + " " + level5ValuesB[6] + " " +level5ValuesB[7] + "]");
                        }
                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }


                ProgressBar sdsuP1Level1;
                ProgressBar sdsuP1Level2;
                ProgressBar sdsuP1Level3;
                ProgressBar sdsuP1Level4;
                ProgressBar sdsuP1Level5;

                sdsuP1Level1 = findViewById(R.id.sdsu_level1_ProgressBar);
                sdsuP1Level2 = findViewById(R.id.sdsu_level2_ProgressBar);
                sdsuP1Level3 = findViewById(R.id.sdsu_level3_ProgressBar);
                sdsuP1Level4 = findViewById(R.id.sdsu_level4_ProgressBar);
                sdsuP1Level5 = findViewById(R.id.sdsu_level5_ProgressBar);


                setProgressBar(getCurrentRelativePercentage(level3ValuesB), sdsuP1Level3);


                refresh(5000);

                Log.e("Second Window", "Level 3 Relative Percentage: " + getCurrentRelativePercentage(level3ValuesB));

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

    public int getCurrentRelativePercentage(int[] levelArr){
        int sum = 0;
        for(int i : levelArr){
            sum = sum + i;
        }

        /** Taking out sections 7 & 8 in division **/
        return (sum/4); //Dividing by 8 since we have 8 sections.
    }

    /** Called when the user taps the first progress bar -TO BE IMPLEMENTED-*/
    public void sdsu_structure1_Level1_Press(View view) {
        Toast.makeText(getApplicationContext(), "Level 1 is not available", Toast.LENGTH_SHORT).show();
    }

    /** Called when the user taps the second progress bar -TO BE IMPLEMENTED-*/
    public void sdsu_structure1_Level2_Press(View view) {
        Toast.makeText(getApplicationContext(), "Level 2 is not available", Toast.LENGTH_SHORT).show();
    }

    /** Called when the user taps the third progress bar */
    public void sdsu_structure1_Level3_Press(View view) {

        Toast.makeText(getApplicationContext(), "Level 3 was pressed", Toast.LENGTH_SHORT).show();

        TextView currentSchool = findViewById(R.id.sdsu_currentSchoolTextView);
        TextView currentStructure = findViewById(R.id.sdsu_currentStructure);

        String school = currentSchool.getText().toString();
        String structure = currentStructure.getText().toString();

        //Grab new data to pass to next activity
        TextView currentLevel = findViewById(R.id.sdsu_level3_ProgressBar_text);
        String currentLevelText = currentLevel.getText().toString();

        //Grab JSON array and send to level 3
        //int[] level3ValuesBB = level3ValuesB;

        //Go to final activity layer (The floor layout)
        Intent goToFloor3Layout = new Intent(this, StructureOneLevel3Layout.class);
        goToFloor3Layout.putExtra("schoolSelectionFinal", school);
        goToFloor3Layout.putExtra("currentStructureFinal", structure);
        goToFloor3Layout.putExtra("currentLevel", currentLevelText);
        goToFloor3Layout.putExtra("level3ValuesB", level3ValuesB);

        startActivity(goToFloor3Layout);


    }

    /** Called when the user taps the fourth progress bar -TO BE IMPLEMENTED-*/
    public void sdsu_structure1_Level4_Press(View view) {
        Toast.makeText(getApplicationContext(), "Level 4 is not available", Toast.LENGTH_SHORT).show();
    }

    /** Called when the user taps the fifth progress bar -TO BE IMPLEMENTED-*/
    public void sdsu_structure1_Level5_Press(View view) {
        Toast.makeText(getApplicationContext(), "Level 5 is not available", Toast.LENGTH_SHORT).show();
    }

    /** Called when Increment Button is pressed */
    /** Button used for testing purposes; function not needed anymore */
    public void incrementPress(View view){

        ProgressBar level3 = findViewById(R.id.sdsu_level3_ProgressBar);

        if (level3Count >= 100){
            Toast.makeText(getApplicationContext(), "Level is Full", Toast.LENGTH_SHORT).show();
            level3.setProgress(100);
        }
        else if (level3Count >= 75){
            //Set progressbar color to Red Gradient
            level3.setProgressDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.redprogress, null));

            level3Count += 5;
            level3.setProgress(level3Count);
        }
        else if (level3Count >= 50){
            //Set progressbar color to Orange Gradient
            level3.setProgressDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.orangeprogress, null));

            level3Count += 5;
            level3.setProgress(level3Count);
        }
        else if (level3Count >= 25){
            //Set progressbar color to Yellow Gradient
            level3.setProgressDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.yellowprogress, null));

            level3Count += 5;
            level3.setProgress(level3Count);
        }
        //Otherwise level3Count is less than 30
        else {
            //Set progressbar to original green color
            level3.setProgressDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.colorprogress, null));

            level3Count += 5;
            level3.setProgress(level3Count);
        }
    }

    /** Called when Decrement Button is pressed */
    /** Used for testing; not needed */
    public void decrementPress(View view){
        ProgressBar level3 = findViewById(R.id.sdsu_level3_ProgressBar);

        if (level3Count <= 0){
            Toast.makeText(getApplicationContext(), "Level is Empty", Toast.LENGTH_SHORT).show();
            level3.setProgress(0);
        }
        else if (level3Count <= 30){
            //Set progressbar color to Green Gradient
            level3.setProgressDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.colorprogress, null));

            level3Count -= 5;
            level3.setProgress(level3Count);
        }
        else if (level3Count <= 55){
            //Set progressbar color to Yellow Gradient
            level3.setProgressDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.yellowprogress, null));

            level3Count -= 5;
            level3.setProgress(level3Count);
        }
        else if (level3Count <= 80){
            //Set progressbar color to Orange Gradient
            level3.setProgressDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.orangeprogress, null));

            level3Count -= 5;
            level3.setProgress(level3Count);
        }
        //Otherwise level3Count is greater than 84
        else {
            //Keep progressbar at red
            level3.setProgressDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.redprogress, null));

            level3Count -= 5;
            level3.setProgress(level3Count);
        }
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

    public void setProgressBar(int relativePercentage, ProgressBar PS){

        if (relativePercentage >= 100){
            Toast.makeText(getApplicationContext(), "Level is Full", Toast.LENGTH_SHORT).show();
            PS.setProgress(100);
        }
        else if (relativePercentage >= 75){
            //Set progressbar color to Red Gradient
            PS.setProgressDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.redprogress, null));
            PS.setProgress(relativePercentage);
        }
        else if (relativePercentage >= 50){
            //Set progressbar color to Orange Gradient
            PS.setProgressDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.orangeprogress, null));
            PS.setProgress(relativePercentage);
        }
        else if (relativePercentage >= 25){
            //Set progressbar color to Yellow Gradient
            PS.setProgressDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.yellowprogress, null));
            PS.setProgress(relativePercentage);
        }
        //Otherwise level3Count is less than 25
        else {
            //Set progressbar to original green color
            PS.setProgressDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.colorprogress, null));
            PS.setProgress(0);
        }
    }
}
