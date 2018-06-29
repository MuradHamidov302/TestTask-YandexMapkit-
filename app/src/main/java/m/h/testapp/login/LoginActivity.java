package m.h.testapp.login;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;

import m.h.testapp.R;
import m.h.testapp.map.MapActivity;
import m.h.testapp.retrofit.BASE_URL;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.provider.ContactsContract.Intents.Insert.EMAIL;

public class LoginActivity extends AppCompatActivity {

    EditText etphone;
    ImageButton imgbtnphone, imgbtnfacebook;
    Toolbar mActionBarToolbar;
    LoginButton facebooklogin;
    CallbackManager callbackManager;
    public ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
       // Log.d("AppLog------", "key:" + FacebookSdk.getApplicationSignature(this)+"=");
        setContentView(R.layout.activity_login);
//toolbar-----------------------------------------------------------
        mActionBarToolbar = (Toolbar) findViewById(R.id.mActionBarToolbar);
        mActionBarToolbar.setTitle("");
        setSupportActionBar(mActionBarToolbar);

        //click button facebook---------------------------
        imgbtnfacebook=findViewById(R.id.imgbtnfacebook);
        imgbtnfacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                facebookloginbtn();
                facebooklogin.performClick();
            }
        });
//----------------------------------------------------------------------------------
        etphone=findViewById(R.id.etphone);
        imgbtnphone=findViewById(R.id.imgbtnphone);
        imgbtnphone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!isEmpty(etphone)) {
                    String Phone=etphone.getText().toString();
                    phoneLoginMethod(Phone);

                } else {
                    etphone.requestFocus();
                    etphone.setError("empty");
                }

            }
        });

    }


    //onactivityresult method----------------------------------------
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode,resultCode,data);
        super.onActivityResult(requestCode, resultCode, data);
    }


    //check input empty------------------------------
private boolean isEmpty(EditText edittext) {
    if (edittext.getText().toString().trim().length() > 0)
        return false;

    return true;
}
//----------------------------------------------

    //call request phone login----------------------------------------------------
    private void phoneLoginMethod(final String phone) {

        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        Call<ResponseBody> call = BASE_URL.retrofitServicesLogin.phonelogin(phone);
        Log.w("request login", call.request().toString());


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {
                    ResponseBody userResponse = response.body();
                    Toast.makeText(getApplicationContext(),"true",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(getApplicationContext(),PhoneSucssesActivity.class);
                    i.putExtra("phone", phone);;
                    startActivity(i);
                }else{
                    Toast.makeText(getApplicationContext(),"false",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
               Toast.makeText(getApplicationContext(),"error fail",Toast.LENGTH_SHORT).show();
            }
        });
    }
    //-----------------------------------------------------------------------------



    //call request facebook login send token----------------------------------------------------
    private void facebookLoginMethod(final String token) {

        progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        Call<ResponseBody> call = BASE_URL.retrofitServicesLogin.facebooklogin(token,"facebook");
        Log.w("request login", call.request().toString());


        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                progressDialog.dismiss();
                if (response.isSuccessful()) {

                    ResponseBody userResponse = response.body();
                    Toast.makeText(getApplicationContext(),"true",Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setMessage("Good Login.\ntoken : "+token+"\nClick Ok go map Activity")
                            .setCancelable(false)
                            .setPositiveButton("OK ",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    startActivity(new Intent(getApplicationContext(),MapActivity.class));
                                }
                            });
                    AlertDialog alert=builder.create();
                    alert.show();

                }else{
                    Toast.makeText(getApplicationContext(),"false",Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(getApplicationContext(),"error fail",Toast.LENGTH_SHORT).show();
            }
        });
    }
    //-----------------------------------------------------------------------------


    //facebook btn click take token----------------------------------------------
    public  void facebookloginbtn(){
        facebooklogin=(LoginButton)findViewById(R.id.facebooklogin);
        facebooklogin.setReadPermissions(Arrays.asList(EMAIL));
        callbackManager=CallbackManager.Factory.create();
        facebooklogin.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                facebookLoginMethod(loginResult.getAccessToken().getToken().toString());

                Log.e("token----",loginResult.getAccessToken().getToken().toString());
                Toast.makeText(
                        LoginActivity.this,
                        "onsucses",
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(),"oncancle"
                        ,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(),"onerror"
                        ,Toast.LENGTH_SHORT).show();
            }
        });
    }

    //-------------------------------------------


}
