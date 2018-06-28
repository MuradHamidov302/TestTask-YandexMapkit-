package m.h.testapp.login;


import android.content.Intent;
import android.os.Bundle;
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

import org.json.JSONObject;

import java.util.Arrays;

import m.h.testapp.R;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        Log.d("AppLog------", "key:" + FacebookSdk.getApplicationSignature(this)+"=");
        setContentView(R.layout.activity_login);
//toolbar-----------------------------------------------------------
        mActionBarToolbar = (Toolbar) findViewById(R.id.mActionBarToolbar);
        mActionBarToolbar.setTitle("");
        setSupportActionBar(mActionBarToolbar);

        //facebook login-------------------------------------------------
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
                        "sucses----",
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onCancel() {
                Toast.makeText(getApplicationContext(),"cancle"
                        ,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(),"error"
                        ,Toast.LENGTH_SHORT).show();
            }
        });

        //click button facebook---------------------------
        imgbtnfacebook=findViewById(R.id.imgbtnfacebook);
        imgbtnfacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                    //startActivity(new Intent(getApplicationContext(),MapActivity.class));

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
    private void phoneLoginMethod(String phone) {
        Call<JSONObject> call = BASE_URL.retrofitServices.phonelogin(phone);
        Log.w("request login", call.request().toString());


        call.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {

                if (response.isSuccessful()) {
                    JSONObject userResponse = response.body();

                    Toast.makeText(getApplicationContext(),""+userResponse.toString(),Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),PhoneSucssesActivity.class));
                }else{

                    ResponseBody userResponse = response.errorBody();
                    Toast.makeText(getApplicationContext(),"error isssucsses"+userResponse,Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
               Toast.makeText(getApplicationContext(),"error fail",Toast.LENGTH_SHORT).show();
            }
        });
    }
    //-----------------------------------------------------------------------------



    //call request facebook login----------------------------------------------------
    private void facebookLoginMethod(String token) {
        Call<JSONObject> call = BASE_URL.retrofitServices.facebooklogin(token,"facebook");
        Log.w("request login", call.request().toString());


        call.enqueue(new Callback<JSONObject>() {
            @Override
            public void onResponse(Call<JSONObject> call, Response<JSONObject> response) {

                if (response.isSuccessful()) {
                    JSONObject userResponse = response.body();

                    Toast.makeText(getApplicationContext(),""+userResponse.toString(),Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(),PhoneSucssesActivity.class));
                }else{

                    ResponseBody userResponse = response.errorBody();
                    Toast.makeText(getApplicationContext(),"error isssucsses"+userResponse,Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<JSONObject> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"error fail",Toast.LENGTH_SHORT).show();
            }
        });
    }
    //-----------------------------------------------------------------------------
}
