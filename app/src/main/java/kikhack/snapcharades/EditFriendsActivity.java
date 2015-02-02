package kikhack.snapcharades;

import android.app.AlertDialog;
import android.app.ListActivity;
<<<<<<< HEAD
=======
import android.support.v7.app.ActionBarActivity;
>>>>>>> 58fc8ad28b2e65c3b73995196ca84dff70d69719
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
<<<<<<< HEAD
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
=======
import android.widget.ListView;
>>>>>>> 58fc8ad28b2e65c3b73995196ca84dff70d69719

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;


public class EditFriendsActivity extends ListActivity {

    public static final String TAG = EditFriendsActivity.class.getSimpleName();

    protected List<ParseUser> mUsers;
    protected ParseRelation<ParseUser> mRelation;
    protected ParseUser mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_friends);

        getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
<<<<<<< HEAD
        Button doneBtn = (Button) findViewById(R.id.addFriendDone);
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
=======
>>>>>>> 58fc8ad28b2e65c3b73995196ca84dff70d69719

    }

    @Override
    protected void onResume() {
        super.onResume();

        mCurrentUser = ParseUser.getCurrentUser();
        mRelation = mCurrentUser.getRelation("relation");

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.orderByAscending("username");
        query.setLimit(1000);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> users, ParseException e) {
                if (e == null) {
                    // Success
                    mUsers = users;
                    String[] usernames = new String[mUsers.size()];
                    int i = 0;
                    for (ParseUser user : mUsers) {
                        usernames[i] = user.getUsername();
                        i++;
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                            EditFriendsActivity.this, android.R.layout.simple_list_item_checked,
                            usernames);
                    setListAdapter(adapter);
                    putCheckmarks();
                } else {
                    // error
                    Log.e(TAG, e.getMessage());
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditFriendsActivity.this);
                    builder.setMessage(e.getMessage())
                            .setTitle(R.string.error_title)
                            .setPositiveButton(android.R.string.ok, null);
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_friends, menu);
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

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        if (getListView().isItemChecked(position)) {
<<<<<<< HEAD
=======

        } else {
>>>>>>> 58fc8ad28b2e65c3b73995196ca84dff70d69719
            mRelation.add(mUsers.get(position));
            mCurrentUser.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        // Success
<<<<<<< HEAD
                        Toast.makeText(getApplicationContext(), "Friend Successfully Added!", Toast.LENGTH_SHORT).show();
=======
>>>>>>> 58fc8ad28b2e65c3b73995196ca84dff70d69719
                    } else {
                        Log.e(TAG, e.getMessage());
                    }
                }
            });
<<<<<<< HEAD
        } else {

=======
>>>>>>> 58fc8ad28b2e65c3b73995196ca84dff70d69719
        }
    }

    private void putCheckmarks() {
        mRelation.getQuery().findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> friends, ParseException e) {
                if (e == null) {
                    // list returned - look for match
                    for (int i = 0; i < mUsers.size(); i++) {
                        ParseUser user = mUsers.get(i);

                        for (ParseUser friend : friends) {
                            if (friend.getObjectId().equals(user.getObjectId())) {
                                getListView().setItemChecked(i, true);
                            }
                        }
                    }
                } else {
                    Log.e(TAG, e.getMessage());
                }
            }
        });

    }
}

