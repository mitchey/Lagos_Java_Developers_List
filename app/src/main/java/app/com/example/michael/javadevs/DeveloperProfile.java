package app.com.example.michael.javadevs;

/**
 * Created by Michael on 13/09/2017.
 */

public class DeveloperProfile {

    private String myName;
    private String myLocation;
    private String myUserName;

    public DeveloperProfile(String myName,  String myUserName, String myLocation){

        this.myName = myName;
        this.myLocation = myLocation;
        this.myUserName = myUserName;
    }

    public String getName()
    {
        return myName;
    }
    public String getMyUserName() { return myUserName; }
    public String getMyLocation()
    {
        return myLocation;
    }
}
