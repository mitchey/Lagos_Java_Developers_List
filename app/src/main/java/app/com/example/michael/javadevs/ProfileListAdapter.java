package app.com.example.michael.javadevs;

/**
 * Created by Michael on 13/09/2017.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;


public class ProfileListAdapter extends ArrayAdapter<DeveloperProfile> {


    public ProfileListAdapter (Context content, List<DeveloperProfile> developerProfiles){
        super(content, 0, developerProfiles);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_tree, parent, false);
        }

        // DeveloperProfile object for the position in the list
        DeveloperProfile developerProfile = getItem(position);

        // Link to list_tree.xml layout using ID
        TextView userName = (TextView) listItemView.findViewById(R.id.userName);

        userName.setText(developerProfile.getMyUserName());

        TextView locationView = (TextView) listItemView.findViewById(R.id.locationText);

        locationView.setText(developerProfile.getMyLocation());

        return listItemView;
    }

}