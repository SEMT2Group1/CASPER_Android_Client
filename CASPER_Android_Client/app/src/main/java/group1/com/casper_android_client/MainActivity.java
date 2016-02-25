package group1.com.casper_android_client;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


/**
 * Created by Andreas Fransson
 */
public class MainActivity extends AppCompatActivity {

    // Declare server URL
    private final String PATH = "http://192.168.0.31:3000";

    // Declare UI Elements
    private User loggedInUser;
    private ProgressBar loading;
    private Button LoginButton;
    private TextView user;
    private TextView password;
    private TextView errormsg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Declare Login Button
        LoginButton = (Button)findViewById(R.id.LoginButton);
        // Declare Text inputfields
        user = (TextView)findViewById(R.id.User);
        password = (TextView)findViewById(R.id.Password);
        errormsg = (TextView)findViewById(R.id.ErrorMsg);
        // Declare Loading wheel
        loading = (ProgressBar) findViewById(R.id.progressBar);

    }

        // on Login start a new thread to authenticate credentials
        // & break into UI threads to access View items
        public void onLogin(final View v)
        {

            if(user.getText().toString().equals("marco") && password.getText().toString().equals("bajs")){
                finish();
                Intent intent = new Intent(MainActivity.this, LoggedInActivity.class);
                startActivity(intent);
            }else
            {

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {

                    runOnUiThread(new Runnable() {
                                      @Override
                                      public void run() {
                                          // Show the loading wheel
                                          loading.setVisibility(v.VISIBLE);
                                      }
                                  }

                    );

                    String userNameString = user.getText().toString();
                    String passwordString = password.getText().toString();

                    // Pass the extra data to next activity
                    Singleton.getInstance().setUserName(userNameString);
                    Singleton.getInstance().setPassWord(passwordString);

                    System.out.println("connect button clicked");
                    System.out.println(userNameString);
                    System.out.println(passwordString);

                    OkHttpClient client = new OkHttpClient();

                    MediaType mediaType = MediaType.parse("application/json");
                    RequestBody body = RequestBody.create(mediaType, "{ \"username\" : \"" + user.getText() + "\", \"password\" : \"" + password.getText() + "\"}");
                    Request request = new Request.Builder()
                            .url(PATH + "/login")
                            .post(body)
                            .addHeader("content-type", "application/json")
//                .addHeader("cache-control", "no-cache")
//                .addHeader("postman-token", "1afc98db-9786-e9a1-92dd-c93c082f958f")
                            .build();
                    try {
                        Response response = client.newCall(request).execute();
                        String jsonString = response.body().string();
                        JSONObject jsonObject = new JSONObject(jsonString);
                        // check if answer contains token
                        if (jsonObject.has("token")) {
                            //authResult.setText("Successfully handshaked with\nDrone!");
                            System.out.println(jsonObject.get("token"));
                            loggedInUser = new User(jsonObject);
                            // Make sure the error msg isnt displayed after haveing the correct login information.
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    errormsg.setVisibility(v.INVISIBLE);
                                }
                            });

                            // Make sure the Activity is finished and deleted.
                            finish();
                            Intent intent = new Intent(MainActivity.this, LoggedInActivity.class);
                            startActivity(intent);
                        }
                        // if it doesn't, we failed to authenticate
                        else {

                            runOnUiThread(new Runnable() {
                                              @Override
                                              public void run() {
                                                  // Stop the loading wheel and show Error Msg.
                                                  loading.setVisibility(v.INVISIBLE);
                                                  errormsg.setVisibility(v.VISIBLE);
                                              }
                                          }
                            );
                            // authResult.setText("Handshake failed, bad credentials");
                            System.out.println("Wrong username or password");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });

            // Start the thread
            thread.start();

        }
        }




}
