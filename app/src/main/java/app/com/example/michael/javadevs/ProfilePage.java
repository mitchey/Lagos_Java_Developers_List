package app.com.example.michael.javadevs;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;

public class ProfilePage extends AppCompatActivity {

    private static final String gitHubProfileUrl = "https://api.github.com/users/";
    private static final String MainGitHubProfileUrl = "https://github.com/users/";

    private String userName ="";
    private String profileName ="";
    private String myGitProfile = "";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);
        setTitle("Profile Page");

        userName = getIntent().getStringExtra("USERNAME");
        profileName = getIntent().getStringExtra("FULLNAME");


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);


        getAvatarImage();

    }

    // Go back to the Home Page

    public void HomeButton (View view) {

        Intent goHome = new Intent(this, MainActivity.class);
        startActivity(goHome);
    }

    //Share Profile

    public void ShareButton (View view) {

        Intent share = new Intent(android.content.Intent.ACTION_SEND);
        share.setType("text/plain");
        share.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);

        share.putExtra(Intent.EXTRA_SUBJECT, "GitHub Profile");
        share.putExtra(Intent.EXTRA_TEXT, myGitProfile);

        startActivity(Intent.createChooser(share, "Check out this awesome developer!"));
    }

    // Get Profile Avatar. Use Universal Image Loader Library
    public void getAvatarImage(){
        myGitProfile = MainGitHubProfileUrl+userName;

        URL profileURL = QueryUtils.createUrl(gitHubProfileUrl+userName);
        String jsonResponse = "";
        String avatarLink = "";


        TextView gitUrl = (TextView) findViewById(R.id.profile_url);
        gitUrl.setText(myGitProfile);

        try {
            jsonResponse = QueryUtils.makeHttpResquest(profileURL);
        }
        catch (IOException e){
            Log.e("ProfilePage.class", "Something went wrong");
            Toast.makeText(ProfilePage.this, "Error, Can't connect to Network", Toast.LENGTH_SHORT).show();
        }

        try {

            // Build up a list of user objects with the corresponding data.
            JSONObject baseJsonResponse = new JSONObject(jsonResponse);
            avatarLink = baseJsonResponse.getString("avatar_url");

            // Update the profile name
            profileName = baseJsonResponse.getString("name");


        } catch (JSONException e) {

            Log.e("ProfilePage.java", "Problem parsing the  JSON results", e);
        }

        if (avatarLink != null) {

            // Update the Profile Name
            TextView myName = (TextView) findViewById(R.id.profile_name);
            myName.setText(profileName);

            // Calling the imageLoader class for viewing the image

            ImageLoader imageLoader = ImageLoader.getInstance();
            ImageView profileImage = (ImageView) findViewById(R.id.avatar_image_view);

            imageLoader.displayImage(avatarLink,profileImage);
        }

    }

}