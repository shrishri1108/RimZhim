package com.rimzhim.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.rimzhim.Adapters.spinCityAdapter;
import com.rimzhim.Adapters.spinStateAdapter;
import com.rimzhim.MainMenu.MainActivity;
import com.rimzhim.ModelClasses.LoginResponse;
import com.rimzhim.ModelClasses.StateModel.CitiesItem;
import com.rimzhim.ModelClasses.StateModel.ListItem;
import com.rimzhim.ModelClasses.StateModel.StateModel;
import com.rimzhim.Networking.ApiClient;
import com.rimzhim.R;
import com.rimzhim.SharedPres.SharedPrefManager;
import com.rimzhim.SharedPres.loginResponsePref;
import com.rimzhim.SimpleClasses.Functions;
import com.rimzhim.databinding.ActivitySignupMainBinding;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Signup_Main extends AppCompatActivity {
    Button signup_buttonBTN;
    TextView sign_up_loginTV;
    ActivitySignupMainBinding binding;
    String email, mobile;
    TextInputEditText selectDate;
    private int mYear, mMonth, mDay;
    String selectedState;
    ArrayList<ListItem> stateList =new ArrayList<>();
    ArrayList<CitiesItem> cityList =new ArrayList<>();
    String stateId="no", cityId="no";
    ProgressDialog progressDialog;
    Dialog mdialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_signup_main);
        Functions.whiteStatusBar(this);
        Intent intent = getIntent();
        email =intent.getStringExtra("email");
        mobile = intent.getStringExtra("mobile");

        signup_buttonBTN= (Button) findViewById(R.id.signup_buttonBTN);
        sign_up_loginTV= (TextView) findViewById(R.id.sign_up_loginTV);
        mdialog = new Dialog(this);
        signup_buttonBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                processToSignUp();
            }


        });
        sign_up_loginTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openLogin_Activity();
            }


        });
        binding.dateOfBirthET.setFocusable(false);
        binding.stateSpinner.setFocusable(false);
        binding.citySpinner.setFocusable(false);


        binding.dateOfBirthET.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view == binding.dateOfBirthET) {
                    final Calendar calendar = Calendar.getInstance ();
                    mYear = calendar.get ( Calendar.YEAR );
                    mMonth = calendar.get ( Calendar.MONTH );
                    mDay = calendar.get ( Calendar.DAY_OF_MONTH );
                    //show dialog
                    DatePickerDialog datePickerDialog = new DatePickerDialog ( Signup_Main.this, new DatePickerDialog.OnDateSetListener () {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            binding.dateOfBirthET.setText ( dayOfMonth + "/" + (month + 1) + "/" + year );
                        }
                    }, mYear, mMonth, mDay );
                    datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
                    datePickerDialog.show ();
                }
            }
        });

        binding.changebackIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        loadStateList();
    }

    private void processToSignUp() {
        final String userName =binding.usernameET.getText().toString().trim();
        final  String firstName = binding.FirstNameET.getText().toString().trim();
        final  String lastName = binding.lastNameET.getText().toString().trim();
        final  String dob = binding.dateOfBirthET.getText().toString().trim();
        final String password = binding.passwordET.getText().toString().trim();
        final String rePassword = binding.rePasswordET.getText().toString().trim();
        final String state = binding.stateSpinner.getText().toString().trim();
        final String city = binding.citySpinner.getText().toString().trim();

        if(TextUtils.isEmpty(userName)){
            binding.usernameET.setError("Add Your User Name");
            binding.usernameET.requestFocus();
            return;
        }

        if(userName.length() > 20){
            binding.usernameET.setError("User Name must be 4 to 20 characters");
            binding.usernameET.requestFocus();
            return;
        }

        if(userName.length() < 4){
            binding.usernameET.setError("User Name must be 4 to 20 characters");
            binding.usernameET.requestFocus();
            return;
        }

        if(userName.contains(" ")){
            binding.usernameET.setError("Remove space from User Name");
            binding.usernameET.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(firstName)){
            binding.FirstNameET.setError("Add Your First Name");
            binding.FirstNameET.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(firstName)){
            binding.FirstNameET.setError("Add Your First Name");
            binding.FirstNameET.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(lastName)){
            binding.lastNameET.setError("Add Your Last Name");
            binding.lastNameET.requestFocus();
            return;
        }



        if(TextUtils.isEmpty(password)){
            binding.passwordET.setError("Add the Password");
            binding.passwordET.requestFocus();
            return;

        }

        if(password.length() <= 6 || password.length() >10){
            binding.passwordET.setError("Password length should be in between 7 to 10");
            binding.passwordET.requestFocus();
            return;

        }

        if(TextUtils.isEmpty(rePassword)){
            binding.rePasswordET.setError("Enter the Password");
            binding.rePasswordET.requestFocus();
            return;
        }

        if(rePassword.length() <= 6 || rePassword.length() >10){
            binding.rePasswordET.setError("Password length should be in between 7 to 10");
            binding.rePasswordET.requestFocus();
            return;

        }

        if(!password.equals(rePassword)){
            binding.rePasswordET.setError("Confirm Password Not Match");
            binding.rePasswordET.requestFocus();
            return;
        }

        if(TextUtils.isEmpty(dob)){
            //binding.dateOfBirthET.setError("Enter Your Date of Birth");
            //binding.dateOfBirthET.requestFocus();
            Toast.makeText(getApplicationContext(),"Enter Your Date of Birth",Toast.LENGTH_SHORT).show();
            return;
        }

        if(stateId.equals("no")){
            binding.stateSpinner.setError("Select the State");
            binding.stateSpinner.requestFocus();
            return;
        }

        if(cityId.equals("no")){

            binding.citySpinner.setError("Select the City");
            binding.citySpinner.requestFocus();
            return;
        }
        if(!binding.checkboxMeat.isChecked()){
            Toast.makeText(getApplicationContext(),"Please Accept the Term and Privacy Policy", Toast.LENGTH_SHORT).show();
            return;
        }
        HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("name",firstName+" "+lastName);
        if(mobile.equals("NO_MOBILE")){
            hashMap.put("email",email);


        }else{
            hashMap.put("phone",mobile);

        }
        hashMap.put("user_name",userName);
        hashMap.put("password",password);
        hashMap.put("dob",dob);
        hashMap.put("state_id",stateId);
        hashMap.put("city_id",cityId);

        progressDialog = new ProgressDialog(Signup_Main.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        progressDialog.setCancelable(false);
        Call<LoginResponse> call = ApiClient.getUserService().userSignUp(hashMap);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if(response.isSuccessful()){
                    if(response.body().getResult().equals("true")){
                        progressDialog.dismiss();
                        loginResponsePref.getInstance(getApplicationContext()).setToken(String.valueOf(response.body().getToken()),response.body().getMessage());

                        Toast.makeText(getApplicationContext(),response.body().getMessage().toString(),Toast.LENGTH_SHORT).show();
                        LoginResponse.User user = new LoginResponse.User(
                                Integer.valueOf(response.body().getUser().getId()),
                                Integer.valueOf(response.body().getUser().getRoleId()),
                                response.body().getUser().getName(),
                                response.body().getUser().getEmail(),
                                response.body().getUser().getPhone(),
                                response.body().getUser().getStateId(),
                                response.body().getUser().getCityId(),
                                response.body().getUser().getUser_name(),
                                response.body().getUser().getReferralCode(),
                                response.body().getUser().getDob(),
                                response.body().getUser().getLongitude(),
                                response.body().getUser().getGender(),
                                response.body().getUser().getWallet(),
                                response.body().getUser().getReferUserId(),
                                response.body().getUser().getLatitude(),
                                Integer.valueOf(response.body().getUser().getStatus()),
                                response.body().getUser().getBio(),
                                response.body().getUser().getWinning(),
                                response.body().getUser().getBackground_image(),
                                response.body().getUser().getImage(),
                                Integer.valueOf(response.body().getUser().getIsDelete()),
                                response.body().getUser().getAddress(),
                                response.body().getUser().getCreatedAt(),
                                response.body().getUser().getUpdatedAt(),
                                response.body().getUser().getTimeOfBirth(),
                                response.body().getUser().getSocialId(),
                                response.body().getUser().getSocialId()
                        );
                        SharedPrefManager.getInstance(getApplicationContext()).userLogin(user);
                        openAccountReadyDialog();
                    }else{
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }else {
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(),response.body().getMessage().toString(),Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(Signup_Main.this,"Throwable "+t.getLocalizedMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void openAccountReadyDialog() {
        mdialog.setContentView(R.layout.popup);
        mdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mdialog.setCancelable(false);
        Button popupBTN = mdialog.findViewById(R.id.popupBTN);
        popupBTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mdialog.dismiss();
                openDashboard_Screen();
            }
        });
        mdialog.show();

    }

    private void openDashboard_Screen() {
        Intent intent = new Intent(Signup_Main.this, MainActivity.class);
        startActivity(intent);
        finish();

    }

    private void loadStateList() {
        Call<StateModel> call = ApiClient.getUserService().LoadStateList();
        call.enqueue(new Callback<StateModel>() {
            @Override
            public void onResponse(Call<StateModel> call, Response<StateModel> response) {
                if(response.isSuccessful()){
                    if(response.body().isResult() == true){
                        stateList = response.body().getList();
                        spinStateAdapter spinStateAdapter = new spinStateAdapter(getApplicationContext(),1,Signup_Main.this,android.R.layout.simple_spinner_item,stateList,true);
                        binding.stateSpinner.setAdapter(spinStateAdapter);
                        binding.stateSpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                binding.stateSpinner.setText(stateList.get(i).getName());
                                stateId = String.valueOf(stateList.get(i).getId());

                                cityList.clear();
                                binding.citySpinner.setText("");
                                //cityList = stateList.get(i).getCities();
                                cityList.addAll(stateList.get(i).getCities());
                                // System.out.println("======"+new Gson().toJson(cityList));

                                spinCityAdapter cityAdapter = new spinCityAdapter(getApplicationContext(),1,Signup_Main.this,android.R.layout.simple_spinner_item,cityList,true);
                                binding.citySpinner.setAdapter(cityAdapter);

                                binding.citySpinner.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                    @Override
                                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                        binding.citySpinner.setText(cityList.get(i).getName());
                                        cityId = String.valueOf(cityList.get(i).getId());
                                    }
                                });

                            }
                        });

                    }
                }

            }

            @Override
            public void onFailure(Call<StateModel> call, Throwable t) {

            }
        });


    }
    private ArrayList<CitiesItem> loadCities(JSONArray cities) {
        ArrayList<CitiesItem> temp = new ArrayList<>();
        CitiesItem citiesItem = new CitiesItem();
        temp.add(citiesItem);
        return temp;
    }

    private void openLogin_Activity() {
        Intent intent = new Intent(this, Login_Activity.class);
        startActivity(intent);
    }
}