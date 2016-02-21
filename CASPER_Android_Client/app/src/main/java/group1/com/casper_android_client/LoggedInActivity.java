package group1.com.casper_android_client;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LoggedInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logged_in);

        // get the intent sen
        Intent intent = getIntent();

        TextView user = (TextView) findViewById(R.id.showUsername);
        TextView password = (TextView) findViewById(R.id.showPassword);
        user.setText(Singleton.getInstance().getUserName());
        password.setText(Singleton.getInstance().getPassWord());

        // Declare Back button
        Button back = (Button) findViewById(R.id.backButton);

        // Declare Settings Button
        Button settings = (Button) findViewById(R.id.Settingsbutton);

        // Declare Video controller Button
        Button controls = (Button) findViewById(R.id.controlButton);



    }

    public void options(View v){
        Intent intent = new Intent(this,SettingsActivity.class);
        startActivity(intent);
    }

    public void backButton(View v){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }


    public void ctrl(View v){
        Intent intent = new Intent(this,VideoStreamActivity.class);
        startActivity(intent);
    }



}
