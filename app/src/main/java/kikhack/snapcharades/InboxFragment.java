package kikhack.snapcharades;

import android.content.Intent;
<<<<<<< HEAD
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;
=======
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
>>>>>>> 58fc8ad28b2e65c3b73995196ca84dff70d69719

/**
 * Created by bryancho on 2015-01-31.
 */
public class InboxFragment extends ListFragment {
<<<<<<< HEAD

    protected List<ParseObject> mMessages;
    protected View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_inbox, container, false);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>(ParseConstants.CLASS_MESSAGES);
        query.whereEqualTo(ParseConstants.KEY_RECIPIENT_IDS, ParseUser.getCurrentUser().getObjectId());
        query.addDescendingOrder(ParseConstants.KEY_CREATED_AT);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> messages, ParseException e) {
                if (e == null) {
                    mMessages = messages;
                    String[] usernames = new String[mMessages.size()];
                    int i = 0;
                    for (ParseObject message : mMessages) {
                        usernames[i] = message.getString(ParseConstants.KEY_SENDER_NAME);
                        i++;
                    }
                    MessageAdapter adapter = new MessageAdapter(getListView().getContext(), mMessages);
                    TextView showEmpty = (TextView) rootView.findViewById(R.id.showEmpty);
                    if (adapter.getCount() == 0) {
                        showEmpty.setVisibility(View.VISIBLE);
                    } else {
                        showEmpty.setVisibility(View.INVISIBLE);
                    }
                    setListAdapter(adapter);
                }

            }
        });
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        ParseObject message = mMessages.get(position);
        String messageType = message.getString(ParseConstants.KEY_FILETYPE);
        String prompt = message.getString(ParseConstants.KEY_PROMPT);

        ParseFile file = message.getParseFile(ParseConstants.KEY_FILE);
        Uri fileUri = Uri.parse(file.getUrl());
        Intent sendIntent = new Intent(getActivity(), NewImageActivity.class);
        sendIntent.putExtra("prompt", prompt);
        sendIntent.setData(fileUri);
        startActivity(sendIntent);
        //Delete Message
        List<String> ids = message.getList(ParseConstants.KEY_RECIPIENT_IDS);
        if (ids.size() == 1) {

            //last person - delete
            message.deleteInBackground();
        } else {
            //remove person
        }
    }
}
=======
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_inbox, container, false);
        return rootView;
    }
}
>>>>>>> 58fc8ad28b2e65c3b73995196ca84dff70d69719
