package kikhack.snapcharades;

import android.app.AlertDialog;
import android.content.Intent;
<<<<<<< HEAD
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
=======
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
>>>>>>> 58fc8ad28b2e65c3b73995196ca84dff70d69719
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
<<<<<<< HEAD
=======
import com.parse.SignUpCallback;
>>>>>>> 58fc8ad28b2e65c3b73995196ca84dff70d69719


public class LoginActivity extends ActionBarActivity {


    protected EditText mUsername;
    protected EditText mPassword;
    protected Button mLogInButton;

    protected TextView mSignUpTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

<<<<<<< HEAD
        mSignUpTextView = (TextView) findViewById(R.id.signUpText);
        mSignUpTextView.setOnClickListener(new View.OnClickListener() {
=======
        mSignUpTextView = (TextView)findViewById(R.id.signUpText);
        mSignUpTextView.setOnClickListener(new View.OnClickListener(){
>>>>>>> 58fc8ad28b2e65c3b73995196ca84dff70d69719
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
<<<<<<< HEAD
        mUsername = (EditText) findViewById(R.id.usernameField);
        mPassword = (EditText) findViewById(R.id.passwordField);
        mLogInButton = (Button) findViewById(R.id.loginButton);
        mLogInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
=======
        mUsername = (EditText)findViewById(R.id.usernameField);
        mPassword = (EditText)findViewById(R.id.passwordField);
        mLogInButton = (Button)findViewById(R.id.loginButton);
        mLogInButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
>>>>>>> 58fc8ad28b2e65c3b73995196ca84dff70d69719
                String username = mUsername.getText().toString();
                String password = mPassword.getText().toString();

                username = username.trim();
                password = password.trim();

<<<<<<< HEAD
                if (username.isEmpty() || password.isEmpty()) {
=======
                if(username.isEmpty() || password.isEmpty()){
>>>>>>> 58fc8ad28b2e65c3b73995196ca84dff70d69719
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setMessage(R.string.login_error_message)
                            .setTitle(R.string.login_error_title)
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
<<<<<<< HEAD
                } else {
=======
                }else{
>>>>>>> 58fc8ad28b2e65c3b73995196ca84dff70d69719
                    // Log in
                    ParseUser.logInInBackground(username, password, new LogInCallback() {
                        @Override
                        public void done(ParseUser user, ParseException e) {
<<<<<<< HEAD
                            if (e == null) {
=======
                            if(e == null){
>>>>>>> 58fc8ad28b2e65c3b73995196ca84dff70d69719
                                // Success!
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
<<<<<<< HEAD
                            } else {
=======
                            }else{
>>>>>>> 58fc8ad28b2e65c3b73995196ca84dff70d69719
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                builder.setMessage(e.getMessage())
                                        .setTitle(R.string.login_error_title)
                                        .setPositiveButton(android.R.string.ok, null);
                                AlertDialog dialog = builder.create();
                                dialog.show();
                            }
                        }
                    });
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
