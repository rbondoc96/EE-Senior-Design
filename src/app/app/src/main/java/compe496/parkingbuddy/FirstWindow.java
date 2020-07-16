package compe496.parkingbuddy;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.EditText;
import android.view.Menu;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Dialog;
import android.preference.PreferenceManager;

public class FirstWindow extends AppCompatActivity {

    private static final String TAG = "MyActivity";

    public int[] Floor1Values = {0,0,0,0,0,0,0,0};
    public int[] Floor2Values = {0,0,0,0,0,0,0,0};

    /** Testing values */
    public int[] Floor3Values = {1,200,0,0,0,0,0,0};

    public int[] Floor4Values = {0,0,0,0,0,0,0,0};
    public int[] Floor5Values = {0,0,0,0,0,0,0,0};

    // Dialog popup box for option menu
    Dialog myDialog;
    Switch percentage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_window);

        myDialog = new Dialog(this);

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());


        /** Variables */
        ProgressBar sdsuParkingStructure1;
        ProgressBar sdsuParkingStructure2;
        ProgressBar sdsuParkingStructure3;
        ProgressBar sdsuParkingStructure4;
        ProgressBar sdsuParkingStructure5;
        ProgressBar sdsuParkingStructure6;

        /**Download JSON file from specified location -THIS IS MUTABLE*/
//        downloadJSON("http://cpe-76-93-136-117.san.res.rr.com/stock_service.php"); //<------ CHANGE THIS AS APPROPRIATE
        downloadJSON("http://nwzbwjlj.p18.rt3.io/stock_service.php");


        /** The following allows the spinner dropdown box to show specific schools */
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.schoolSelection_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);
        //Create a listener to check for changes in selection
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int spinnerPosition, long id) {
                //Create a generic spinnerText string
                switch(spinnerPosition) {
                    case 0:
                        //Code for SDSU selection
                        break; //Do nothing since this is what we want
                    case 1:
                        //Code for University of San Diego Selection
                        Toast.makeText(getApplicationContext(), "University of San Diego Not Yet Implemented", Toast.LENGTH_SHORT).show();
                        break;
                    case 2:
                        //Code for Grossmont College selection
                        Toast.makeText(getApplicationContext(), "Grossmont College Not Yet Implemented", Toast.LENGTH_SHORT).show();
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        /* Below is the control determinate to choose which progress bars to display depending on school selection*/

        /* Progress Bar Testing Area*/
        sdsuParkingStructure1 = findViewById(R.id.sdsu_progressBar1);
        sdsuParkingStructure2 = findViewById(R.id.sdsu_progressBar2);
        sdsuParkingStructure3 = findViewById(R.id.sdsu_progressBar3);
        sdsuParkingStructure4 = findViewById(R.id.sdsu_progressBar4);
        sdsuParkingStructure5 = findViewById(R.id.sdsu_progressBar5);
        sdsuParkingStructure6 = findViewById(R.id.sdsu_progressBar6);

        sdsuParkingStructure1.setMax(100);
        sdsuParkingStructure2.setMax(100);
        sdsuParkingStructure3.setMax(100);
        sdsuParkingStructure4.setMax(100);
        sdsuParkingStructure5.setMax(100);
        sdsuParkingStructure6.setMax(100);

//        sdsuParkingStructure1.setProgress(getCurrentRelativePercentage(Floor1Values, Floor2Values, Floor3Values, Floor4Values, Floor5Values)); //to be programmatically calculated in future use cases

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

//                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();


                try{
                    JSONArray a = new JSONArray(s);

                    for(int i = 0; i <  a.length();i++){
                        JSONObject o = a.getJSONObject(i);
                        if(i == 0){
                            Floor1Values[0] = o.optInt("Section_1");
                            Floor1Values[1] = o.optInt("Section_2");
                            Floor1Values[2] = o.optInt("Section_3");
                            Floor1Values[3] = o.optInt("Section_4");
                            Floor1Values[4] = o.optInt("Section_5");
                            Floor1Values[5] = o.optInt("Section_6");
                            Floor1Values[6] = o.optInt("Section_7");
                            Floor1Values[7] = o.optInt("Section_8");


                            Log.wtf("FirstWindow", "Floor 1 Values: " + "[ " + Floor1Values[0] +
                                    " " + Floor1Values[1] + " " + Floor1Values[2] + " " + Floor1Values[3] + " " +
                                    " " + Floor1Values[4] + " " + Floor1Values[5] + " " + Floor1Values[6] + " " +Floor1Values[7] + "]");
                        }
                        else if (i == 1){
                            Floor2Values[0] = o.optInt("Section_1");
                            Floor2Values[1] = o.optInt("Section_2");
                            Floor2Values[2] = o.optInt("Section_3");
                            Floor2Values[3] = o.optInt("Section_4");
                            Floor2Values[4] = o.optInt("Section_5");
                            Floor2Values[5] = o.optInt("Section_6");
                            Floor2Values[6] = o.optInt("Section_7");
                            Floor2Values[7] = o.optInt("Section_8");

                            Log.wtf("FirstWindow", "Floor 2 Values: " + "[ " + Floor2Values[0] +
                                    " " + Floor2Values[1] + " " + Floor2Values[2] + " " + Floor2Values[3] + " " +
                                    " " + Floor2Values[4] + " " + Floor2Values[5] + " " + Floor2Values[6] + " " +Floor2Values[7] + "]");
                        }
                        else if (i == 2){
                            Floor3Values[0] = o.optInt("Section_1");
                            Floor3Values[1] = o.optInt("Section_2");
                            Floor3Values[2] = o.optInt("Section_3");
                            Floor3Values[3] = o.optInt("Section_4");
                            Floor3Values[4] = o.optInt("Section_5");
                            Floor3Values[5] = o.optInt("Section_6");
                            Floor3Values[6] = o.optInt("Section_7");
                            Floor3Values[7] = o.optInt("Section_8");

                            Log.wtf("FirstWindow", "Floor 3 Values: " + "[ " + Floor3Values[0] +
                                    " " + Floor3Values[1] + " " + Floor3Values[2] + " " + Floor3Values[3] + " " +
                                    " " + Floor3Values[4] + " " + Floor3Values[5] + " " + Floor3Values[6] + " " +Floor3Values[7] + "]");
                        }
                        else if (i == 3){
                            Floor4Values[0] = o.optInt("Section_1");
                            Floor4Values[1] = o.optInt("Section_2");
                            Floor4Values[2] = o.optInt("Section_3");
                            Floor4Values[3] = o.optInt("Section_4");
                            Floor4Values[4] = o.optInt("Section_5");
                            Floor4Values[5] = o.optInt("Section_6");
                            Floor4Values[6] = o.optInt("Section_7");
                            Floor4Values[7] = o.optInt("Section_8");

                            Log.wtf("FirstWindow", "Floor 4 Values: " + "[ " + Floor4Values[0] +
                                    " " + Floor4Values[1] + " " + Floor4Values[2] + " " + Floor4Values[3] + " " +
                                    " " + Floor4Values[4] + " " + Floor4Values[5] + " " + Floor4Values[6] + " " +Floor4Values[7] + "]");
                        }
                        else{
                            Floor5Values[0] = o.optInt("Section_1");
                            Floor5Values[1] = o.optInt("Section_2");
                            Floor5Values[2] = o.optInt("Section_3");
                            Floor5Values[3] = o.optInt("Section_4");
                            Floor5Values[4] = o.optInt("Section_5");
                            Floor5Values[5] = o.optInt("Section_6");
                            Floor5Values[6] = o.optInt("Section_7");
                            Floor5Values[7] = o.optInt("Section_8");

                            Log.wtf("FirstWindow", "Floor 5 Values: " + "[ " + Floor5Values[0] +
                                    " " + Floor5Values[1] + " " + Floor5Values[2] + " " + Floor5Values[3] + " " +
                                    " " + Floor5Values[4] + " " + Floor5Values[5] + " " + Floor5Values[6] + " " +Floor5Values[7] + "]");
                        }
                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }


                ProgressBar sdsuParkingStructure1;
                ProgressBar sdsuParkingStructure2;
                ProgressBar sdsuParkingStructure3;
                ProgressBar sdsuParkingStructure4;
                ProgressBar sdsuParkingStructure5;

                /* Progress Bar Testing Area*/
                sdsuParkingStructure1 = findViewById(R.id.sdsu_progressBar1);
                sdsuParkingStructure2 = findViewById(R.id.sdsu_progressBar2);
                sdsuParkingStructure3 = findViewById(R.id.sdsu_progressBar3);
                sdsuParkingStructure4 = findViewById(R.id.sdsu_progressBar4);
                sdsuParkingStructure5 = findViewById(R.id.sdsu_progressBar5);


                setProgressBar(getCurrentRelativePercentage(Floor1Values, Floor2Values, Floor3Values, Floor4Values, Floor5Values), sdsuParkingStructure1);
                refresh(2500);

                Log.e("FirstWindow", "Number Returned from Function getCurrentRelativePercentage: " + getCurrentRelativePercentage(Floor1Values, Floor2Values, Floor3Values, Floor4Values, Floor5Values));

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

    public int getCurrentRelativePercentage(int[] level1Arr, int[] level2Arr, int[] level3Arr, int[] level4Arr, int[] level5Arr){
        int sum = 0;
        for(int i : level1Arr){
            sum = sum + i;
        }
        for(int j : level2Arr){
            sum = sum + j;
        }
        for(int k : level3Arr){
            sum = sum + k;
        }
        for(int l : level4Arr){
            sum = sum + l;
        }
        for(int o : level5Arr){
            sum = sum + o;
        }

        /** Testing value */
        return (sum/40); //Dividing by 40 since we have 8*5 combined sections.
    }

    /** Called when the user taps the first progress bar */
    public void sdsu_structure_bar1Press(View view) {

        /** User Instantiated JSON array*/
//        int[] level3ValuesA = {1, 1, 1, 0, 0, 0, 0, 0};
        int[] level1ValuesA = Floor1Values;
        int[] level2ValuesA = Floor2Values;
        int[] level3ValuesA = Floor3Values;
        int[] level4ValuesA = Floor4Values;
        int[] level5ValuesA = Floor5Values;

        //Gets the correct school name and allows it to be sent to the next activity
        Spinner mySpinner = findViewById(R.id.spinner);
        String mySpinnerText = mySpinner.getSelectedItem().toString();

        //Need something to send which parking structure we are currently working with
        TextView currentStructure = (TextView) findViewById(R.id.sdsu_progressBar1_text);
        String currentStructureText = currentStructure.getText().toString();



        //Redundant checkpoint (remove if desired)
        Toast.makeText(getApplicationContext(), "Progress Bar 1 has been pressed", Toast.LENGTH_SHORT).show();


        //Ensures user is using the corrent institution instead of ones not yet implemented
        if(mySpinnerText.equals("San Diego State University")) {
            Intent goToStructureLevels = new Intent(this, StructureOneLevels.class);
            goToStructureLevels.putExtra("schoolSelection", mySpinnerText);
            goToStructureLevels.putExtra("currentStructure", currentStructureText);
            goToStructureLevels.putExtra("level1ValuesA", level1ValuesA);
            goToStructureLevels.putExtra("level2ValuesA", level2ValuesA);
            goToStructureLevels.putExtra("level3ValuesA", level3ValuesA);
            goToStructureLevels.putExtra("level4ValuesA", level4ValuesA);
            goToStructureLevels.putExtra("level5ValuesA", level5ValuesA);

            startActivity(goToStructureLevels);
        }
        else {
            Toast.makeText(getApplicationContext(), "Please Select San Diego State University As Your Institution", Toast.LENGTH_SHORT).show();
        }
    }

    /** Called when the user taps the second progress bar -TO BE IMPLEMENTED-*/
    public void sdsu_structure_bar2Press(View view) {
        Toast.makeText(getApplicationContext(), "Parking Structure 3 is not available", Toast.LENGTH_SHORT).show();
    }

    /** Called when the user taps the third progress bar -TO BE IMPLEMENTED-*/
    public void sdsu_structure_bar3Press(View view) {
        Toast.makeText(getApplicationContext(), "Parking Structure 4 is not available", Toast.LENGTH_SHORT).show();
    }

    /** Called when the user taps the fourth progress bar -TO BE IMPLEMENTED-*/
    public void sdsu_structure_bar4Press(View view) {
        Toast.makeText(getApplicationContext(), "Parking Structure 6 is not available", Toast.LENGTH_SHORT).show();
    }

    /** Called when the user taps the fifth progress bar -TO BE IMPLEMENTED-*/
    public void sdsu_structure_bar5Press(View view) {
        Toast.makeText(getApplicationContext(), "Parking Structure 7 is not available", Toast.LENGTH_SHORT).show();
    }

    /** Called when the user taps the sixth progress bar -TO BE IMPLEMENTED-*/
    public void sdsu_structure_bar6Press(View view) {
        Toast.makeText(getApplicationContext(), "Parking Structure 12 is not available", Toast.LENGTH_SHORT).show();
    }

    /** Called when the user taps the Options button */
    public void optionsPress(View view) {
        //Popup Dialog option box
        myDialog.setContentView(R.layout.popupmenu);
        myDialog.show();


        percentage = (Switch) myDialog.findViewById(R.id.percent);
        percentage.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                TextView PS1P = findViewById(R.id.PS1_percent);
                TextView PS3P = findViewById(R.id.PS3_percent);
                TextView PS4P = findViewById(R.id.PS4_percent);
                TextView PS6P = findViewById(R.id.PS6_percent);
                TextView PS7P = findViewById(R.id.PS7_percent);

                ProgressBar sdsuParkingStructure1 = findViewById(R.id.sdsu_progressBar1);

                if (isChecked) {
                    percentage.setChecked(true);
                    PS1P.setText(String.valueOf(getCurrentRelativePercentage(Floor1Values, Floor2Values, Floor3Values, Floor4Values, Floor5Values))+"%");
                    PS3P.setText("100%");
                    PS4P.setText("100%");
                    PS6P.setText("100%");
                    PS7P.setText("100%");
                } else {

                    PS1P.setText("");
                    PS3P.setText("");
                    PS4P.setText("");
                    PS6P.setText("");
                    PS7P.setText("");
                    setProgressBar(getCurrentRelativePercentage(Floor1Values, Floor2Values, Floor3Values, Floor4Values, Floor5Values), sdsuParkingStructure1);
                }
            }
        });

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

    /** TESTING VALUES */
    public void setProgressBar(int relativePercentage, ProgressBar PS){

        if (relativePercentage >= 100){
            //Toast.makeText(getApplicationContext(), "Level is Full", Toast.LENGTH_SHORT).show();
            PS.setProgress(100);
        }
         if (relativePercentage >= 75){
            //Set progressbar color to Red Gradient
            PS.setProgressDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.redprogress, null));
            PS.setProgress(relativePercentage);
//            PS.setProgress(100);
        }
        else if (relativePercentage >= 50){
            //Set progressbar color to Orange Gradient
            PS.setProgressDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.orangeprogress, null));
            PS.setProgress(relativePercentage);
//            PS.setProgress(66);
        }
        else if (relativePercentage >= 25){
            //Set progressbar color to Yellow Gradient
            PS.setProgressDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.yellowprogress, null));
            PS.setProgress(relativePercentage);
//            PS.setProgress(33);
        }
        //Otherwise level3Count is less than 30
        else {
            //Set progressbar to original green color
            PS.setProgressDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.colorprogress, null));
            PS.setProgress(relativePercentage);
//            PS.setProgress(0);
        }
    }

}
