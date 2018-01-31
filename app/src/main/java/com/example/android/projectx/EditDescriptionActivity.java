package com.example.android.projectx;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class EditDescriptionActivity extends AppCompatActivity {
    ArrayList<String> relationshipList, educationList, bloodList, workList;
    ArrayAdapter<String> arrayAdapter, educationArrayAdapter, bloodArrayAdapter,workAdapter;
    Spinner spinner, educationSpinner, bloodSpinner,workSpinner;
    TextView dob;
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
    Button submitButton;
    Date indates, outdate, Todaydate;
    EditText firstName, middleName, lastName,agee, birthPlace, designation,additionalContacts;
    RadioGroup gender;
    RadioButton male, female;
    String mmale,mfemale;
    ModelUser user;
    String model;
    int getEducationIndex=0,getRelationshipIndex=0,getWorkIndex=0,getBloodIndex=0;

   SharedPreferences preferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_description);

        preferences = getSharedPreferences("SP",MODE_PRIVATE);

        model = preferences.getString("models","");

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        agee = findViewById(R.id.age);
        firstName = findViewById(R.id.first_name);
        middleName=findViewById(R.id.middle_name);
        lastName = findViewById(R.id.last_name);
        birthPlace = findViewById(R.id.birthPlace);
        designation = findViewById(R.id.designation);
        additionalContacts = findViewById(R.id.additonalContacts);
        male = findViewById(R.id.male);
        female = findViewById(R.id.female);
        gender = findViewById(R.id.gender);

        educationSpinner = findViewById(R.id.spinner2);
        spinner = findViewById(R.id.spinner);
        bloodSpinner = findViewById(R.id.spinner3);
        workSpinner =findViewById(R.id.spinner4);

        dob = findViewById(R.id.dateView);
        relationshipList = new ArrayList<>();

        relationshipList.add("N/A");
        relationshipList.add("Single");
        relationshipList.add("Taken");
        relationshipList.add("Married");
        relationshipList.add("Casual");
        relationshipList.add("Complicated");

        educationList = new ArrayList<>();
        educationList.add("N/A");
        educationList.add("No Education");
        educationList.add("Primary School");
        educationList.add("Secondary School");
        educationList.add("High School Graduate");
        educationList.add("Bachelors Undergrad");
        educationList.add("Bachelor's Degree");
        educationList.add("Masters Undergrad");
        educationList.add("Master's Degree");
        educationList.add("Doctorate Undergrad");
        educationList.add("Doctoral Degree");

        bloodList = new ArrayList<>();
        bloodList.add("N/A");
        bloodList.add("A+");
        bloodList.add("A-");
        bloodList.add("AB+");
        bloodList.add("AB-");
        bloodList.add("B+");
        bloodList.add("B-");
        bloodList.add("O+");
        bloodList.add("O-");

        workList = new ArrayList<>();
        workList.add("N/A");
        workList.add("Employed");
        workList.add("Unemployed");

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,relationshipList);
        spinner.setAdapter(arrayAdapter);

        bloodArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, bloodList);
        bloodSpinner.setAdapter(bloodArrayAdapter);

        workAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item,workList);
        workSpinner.setAdapter(workAdapter);

        educationArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, educationList);
        educationSpinner.setAdapter(educationArrayAdapter);

        gender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.male:
                        mmale = male.getText().toString();
                        Log.d("fii", "onCheckedChanged: "+mmale);

                        // do operations specific to this selection
                        break;
                    case R.id.female:
                        mmale =female.getText().toString();
                        Log.d("fii", "onCheckedChanged: "+mmale);

                        // do operations specific to this selection
                        break;
                }
            }
        });


        if(model==null||model==""){

        }else {
            user = new Gson().fromJson(model, ModelUser.class);
            firstName.setText(user.getfName());
            middleName.setText(user.getmName());
            lastName.setText(user.getlName());
            agee.setText(user.getAge());
            birthPlace.setText(user.getBirthPlace());
            designation.setText(user.getDesignation());

            dob.setText(user.getDateOfBirth());
            additionalContacts.setText(user.getAdditionalContacts());

            dob.setTextColor(getResources().getColor(R.color.colorAccent));
            for(int i=0;i<bloodList.size();i++){
                Log.d("tos", "onCreate: "+bloodList.get(i));
                String bloodGroup = String.valueOf(user.getBloodGroup());
                if(bloodList.get(i).equalsIgnoreCase(bloodGroup)){
                    getBloodIndex = i;
                }
            }
            bloodSpinner.setSelection(getBloodIndex);

            for(int i=0;i<relationshipList.size();i++){
                String relationshipGroup = String.valueOf(user.getStatus());
                if(relationshipList.get(i).equalsIgnoreCase(relationshipGroup)){
                    getRelationshipIndex = i;
                }
            }
            spinner.setSelection(getRelationshipIndex);

            for(int i=0;i<educationList.size();i++){
                String educationGroup = String.valueOf(user.getEducation());
                if(educationList.get(i).equalsIgnoreCase(educationGroup)){
                    getEducationIndex = i;
                }
            }
            educationSpinner.setSelection(getEducationIndex);


            for(int i=0;i<workList.size();i++){
                String workGroup = String.valueOf(user.getWork());
                if(workList.get(i).equalsIgnoreCase(workGroup)){
                    getWorkIndex = i;
                }
            }
            workSpinner.setSelection(getWorkIndex
            );


            if(String.valueOf(user.getGender()).equalsIgnoreCase("male")){
                gender.check(male.getId());
            }else {
                gender.check(female.getId());
            }


        }



        Todaydate = new Date();
        calendar.add(Calendar.YEAR, -14);


        final DatePickerDialog.OnDateSetListener indate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, day);

                dob.setText(myFormat.format(calendar.getTime()));
                try {
                    indates = myFormat.parse(dob.getText().toString());

                    int age = Todaydate.getYear() - indates.getYear();

                    if (Todaydate.getYear() < indates.getYear()){
                        age--;
                    }


                    agee.setText(String.valueOf(age));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        };


        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               DatePickerDialog dialog =  new DatePickerDialog(EditDescriptionActivity.this, indate, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH));
               dialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 441796964000L);
               dialog.show();
            }
        });





        submitButton = findViewById(R.id.submitButton);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ModelUser modelUser = new ModelUser(firstName.getText().toString(),middleName.getText().toString(),lastName.getText().toString(),
                        dob.getText().toString(),agee.getText().toString(),birthPlace.getText().toString(),designation.getText().toString(),
                        additionalContacts.getText().toString(),mmale,spinner.getSelectedItem().toString(),
                        educationSpinner.getSelectedItem().toString(), bloodSpinner.getSelectedItem().toString(),workSpinner.getSelectedItem().toString());


                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("models",new Gson().toJson(modelUser));
                editor.commit();

                Intent i =new Intent(EditDescriptionActivity.this,HomeActivity.class);

                i.putExtra("fragmentid",3);
//                i.putExtra("model",new Gson().toJson(modelUser));
                startActivity(i);
            }
        });


    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, WelcomeActivity.class));
        super.onBackPressed();
    }
}
