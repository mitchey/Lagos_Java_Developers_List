package app.com.example.michael.javadevs;

/**
 * Created by Michael on 13/09/2017.
 */

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;


public class ProfileLoader extends AsyncTaskLoader{

    // Query URL
    private String mUri;

    public ProfileLoader(Context context, String url){
        super(context);
        this.mUri = url;

    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     *  Acts on Background thread.
     */

    @Override
    public List<DeveloperProfile> loadInBackground() {
        if (this.mUri == null) {
            return null;
        }

        List<DeveloperProfile> profile = QueryUtils.fetchProfileData(mUri);
        return profile;
    }
}