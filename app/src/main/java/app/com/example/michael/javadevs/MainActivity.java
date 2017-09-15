package app.com.example.michael.javadevs;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;

import java.util.ArrayList;
import java.util.List;

import static android.os.Build.ID;


public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<List<DeveloperProfile>> {

    public static final String LOG_TAG = MainActivity.class.getName();
    private static final String API_REQUEST_URL =
            "https://api.github.com/search/users?q=location:lagos+type:user+language:java";

    //loader ID is constant.

    private static final int LOADER_ID = 1;

    // ListView Adapter
    private ProfileListAdapter mAdapter;

    //Display textview when list is empty
    private TextView mEmptyStateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // UNIVERSAL IMAGE LOADER SETUP; alternatively could have used Picasso
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc(true).cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .displayer(new FadeInBitmapDisplayer(300)).build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .discCacheSize(100 * 1024 * 1024).build();

        ImageLoader.getInstance().init(config);
        // END - UNIVERSAL IMAGE LOADER SETUP

        // Create reference object for ListView in the layout
        ListView developerProfileList = (ListView) findViewById(R.id.list);

        mEmptyStateTextView = (TextView) findViewById(R.id.empty_view);
        developerProfileList.setEmptyView(mEmptyStateTextView);

        //New adapter for empty list input
        mAdapter = new ProfileListAdapter(this, new ArrayList<DeveloperProfile>());

        // Populate list in the UI
        developerProfileList.setAdapter(mAdapter);

        // Set an item click listener on the ListView, which sends an intent to another activity
        developerProfileList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //Obtain individual profile
                DeveloperProfile currentProfilePick = mAdapter.getItem(position);

                String userName = currentProfilePick.getMyUserName();
                String profileName = currentProfilePick.getName();
                //Call launchProfile method
                launchProfilePage(userName, profileName);

            }
        });

        // ConnectivityManager object to verify network connectivity state
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);


        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();


        // If network connection exists, fetch data
        if (networkInfo != null && networkInfo.isConnected()) {

            //LoaderManager object to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            loaderManager.initLoader(LOADER_ID, null, this);
        } else {
            //Hide processing indicator and then display error
            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.INVISIBLE);

            // Update empty state with no connection error message
            mEmptyStateTextView.setText("No Internet Connection");
        }

    }

    public void launchProfilePage(String userName, String profileName){
        Intent profilePageIntent = new Intent(this, ProfilePage.class);
        profilePageIntent.putExtra("USERNAME", userName);
        profilePageIntent.putExtra("FULLNAME", profileName);
        startActivity(profilePageIntent);
    }

    @Override
    public Loader<List<DeveloperProfile>> onCreateLoader(int i, Bundle bundle) {

        Uri baseUri = Uri.parse(API_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();
        return new ProfileLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<DeveloperProfile>> loader, List<DeveloperProfile> profileID) {
        // Hide processing indicator after data is loaded.
        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.INVISIBLE);

        //Text for empty state
        mEmptyStateTextView.setText("No Profile Found");

        // Clear the adapter
        mAdapter.clear();

        //For ListView to update.
        if (profileID != null && !profileID.isEmpty()) {
            mAdapter.addAll(profileID);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<DeveloperProfile>> loader) {
        //Clear existing data.
        mAdapter.clear();
    }

}
