package com.example.android.projectx.WelcomeRegister;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.projectx.Retrofit.ApiClient;
import com.example.android.projectx.Retrofit.ApiService;
import com.example.android.projectx.R;
import com.lamudi.phonefield.Countries;
import com.lamudi.phonefield.PhoneInputLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScreenRegister extends AppCompatActivity {
    ArrayList<ModelClass> list;
    String TAG = "thistag", mCheck;
    Button button;
//    EditText countryCode;
    Spinner sp,flagSpinner;
    ArrayList<String> namelist = new ArrayList<>();
    PhoneInputLayout phoneInputLayout;
    Countries countries;
    String finalPhoneNumber;

    AlertDialog.Builder builder1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);
//        countryCode = (EditText) findViewById(R.id.country_code);
        sp = findViewById(R.id.spinner);

        button = findViewById(R.id.button2);
        phoneInputLayout = findViewById(R.id.country);

        getNotice();
        mCheck = getUserCountry(this).toUpperCase();
        Log.d("fis", "onCreate: "+mCheck);

        phoneInputLayout.setDefaultCountry(mCheck);
        phoneInputLayout.setHint(R.string.hint_at_phonenumber_edittext);

        flagSpinner = phoneInputLayout.getSpinner();
        flagSpinner.setClickable(false);
        flagSpinner.setEnabled(false);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finalPhoneNumber = phoneInputLayout.getPhoneNumber().toString();

                if( phoneInputLayout.isValid()){
//                    Toast.makeText(StartActivity.this, "Valid Phone Number"+finalPhoneNumber, Toast.LENGTH_SHORT).show();

                    builder1 = new AlertDialog.Builder(ScreenRegister.this);
                    builder1.setMessage("Is this your Phone Number?\n\n" +
                            finalPhoneNumber +
                            "\n\nYou'll receive an SMS shortly. Thank You!!");

                    builder1.setCancelable(false);

                    builder1.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent i = new Intent(getApplicationContext(), VerificationActivity.class);
                                    i.putExtra("phoneNumber", finalPhoneNumber);
                                    startActivity(i);
                                    dialog.cancel();

                                }
                            });

                    builder1.setNegativeButton(
                            "Edit",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }
                else {
//                    Toast.makeText(StartActivity.this, "Invalid number" + finalPhoneNumber, Toast.LENGTH_SHORT).show();

                    builder1 = new AlertDialog.Builder(ScreenRegister.this);
                    builder1.setMessage("This Number \n \n" +
                            finalPhoneNumber +
                            " is not valid.\n\n"+"Please make sure the number is correct.");
                    builder1.setCancelable(false);

                    builder1.setPositiveButton(
                            "Edit",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();

                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();
                }
            }
        }
        );
    }
    public void getNotice() {

        ApiService api = ApiClient.getApiService();
        Call<List<ModelClass>> call = api.getJson(); //api.getJson provides data to call object list
        call.enqueue(new Callback<List<ModelClass>>() { //enqueque requests data
            @Override
            public void onResponse(Call<List<ModelClass>> call, Response<List<ModelClass>> response) { // either positive or negative response

                int getIndex=0;
                list=new ArrayList<>(); //define a list object                              //data is in response

                for(int i = 0; i<response.body().size(); i++){
                    list.add(response.body().get(i));
                    namelist.add(response.body().get(i).getName());  // adding data into the list
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(ScreenRegister.this,
                        android.R.layout.simple_list_item_1,namelist);
                sp.setAdapter(adapter);


//                String countryName = getUserCountry(ScreenRegister.this);

                for(int i = 0; i< namelist.size(); i++){

                    String name = response.body().get(i).getCode();
                    if(mCheck.equalsIgnoreCase(name)){

                        getIndex = i;
                    }
                }

                sp.setSelection(getIndex);

                clickSpinner(sp);
            }
            @Override
            public void onFailure(Call<List<ModelClass>> call, Throwable t) {
                Log.d("chekcth", String.valueOf(t));
                Toast.makeText(ScreenRegister.this, "No internte conteh", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void clickSpinner(Spinner spinner){
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                countryCode.setText(list.get(i).getDialCode());
                phoneInputLayout.setDefaultCountry(list.get(i).getCode());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }
    public static String getUserCountry(Context context) {
        try {
            final TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            final String simCountry = tm.getSimCountryIso();
            if (simCountry != null && simCountry.length() == 2) {
                return simCountry.toLowerCase(Locale.US);
            }
            else if (tm.getPhoneType() != TelephonyManager.PHONE_TYPE_CDMA) { // device is not 3G (would be unreliable)
                String networkCountry = tm.getNetworkCountryIso();
                if (networkCountry != null && networkCountry.length() == 2) { // network country code is available
                    return networkCountry.toLowerCase(Locale.US);
                }
            }
        }
        catch (Exception e) { }
        return null;
    }

    public void clickFlag(final Spinner spinner){

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                for(int k=0; i<countries.COUNTRIES.size();k++){

//                    spinner.

                }

//                sp.setSelection(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

}