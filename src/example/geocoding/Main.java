package example.geocoding;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Main extends Activity 
{
	private LocationManager locationManager;
	private Location currentLocation;
	private TextView txtLatitude;
	private TextView txtLongitude;
	private Button btnReverseGeocode;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        // Get handles to the elements on our android activity page.
        this.txtLatitude = (TextView) findViewById(R.id.txtLatitude);
        this.txtLongitude = (TextView) findViewById(R.id.txtLongitude);
        this.btnReverseGeocode = (Button) findViewById(R.id.btnReverseGeocode);
        
        // Subscribe to our button's click event
        this.btnReverseGeocode.setOnClickListener(
            	new OnClickListener() {
            		public void onClick(View v)
            		{
            			handleReverseGeocodeClick();
            		}
            	}
        );
        
	// Get an instance of the android system LocationManager 
	// so we can access the phone's GPS receiver
	this.locationManager = 
		(LocationManager) getSystemService(Context.LOCATION_SERVICE);
	
	// Subscribe to the location manager's updates on the current location
	this.locationManager.requestLocationUpdates("gps", (long)30000, (float) 10.0, new LocationListener()
		{
			public void onLocationChanged(Location arg0) 
			{
				handleLocationChanged(arg0);
			}
		
			public void onProviderDisabled(String arg0) {
				// TODO Auto-generated method stub
				
			}
		
			public void onProviderEnabled(String arg0) {
				// TODO Auto-generated method stub
				
			}
		
			public void onStatusChanged(String arg0, int arg1, Bundle arg2) {
				// TODO Auto-generated method stub
			}
		});
    }
    
    private void handleLocationChanged(Location loc)
    {
    	// Save the latest location
    	this.currentLocation = loc;
    	// Update the latitude & longitude TextViews
    	this.txtLatitude.setText(Double.toString(loc.getLatitude()));
    	this.txtLongitude.setText(Double.toString(loc.getLongitude()));
    }
    
    private void handleReverseGeocodeClick()
    {
    	if (this.currentLocation != null)
    	{
    		// Kickoff an asynchronous task to fire the reverse geocoding
    		// request off to google
    		ReverseGeocodeLookupTask task = new ReverseGeocodeLookupTask();
    		task.applicationContext = this;
    		task.execute();
    	}
    	else
    	{
    		// If we don't know our location yet, we can't do reverse
    		// geocoding - display a please wait message
    		showToast("Please wait until we have a location fix from the gps");
    	}
    }
    
	public void showToast(CharSequence message)
    {
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(getApplicationContext(), message, duration);
		toast.show();
    }
	
	public class ReverseGeocodeLookupTask extends AsyncTask <Void, Void, String>
    {
    	private ProgressDialog dialog;
    	protected Context applicationContext;
    	
    	@Override
    	protected void onPreExecute()
    	{
    		this.dialog = ProgressDialog.show(applicationContext, "Please wait...contacting the tubes.", 
                    "Requesting reverse geocode lookup", true);
    	}
    	
		@Override
		protected String doInBackground(Void... params) 
		{
			String localityName = "";
			
			if (currentLocation != null)
			{
				localityName = Geocoder.reverseGeocode(currentLocation);
			}
			
			return localityName;
		}
		
		@Override
		protected void onPostExecute(String result)
		{
			this.dialog.cancel();
			Utilities.showToast("Your Locality is: " + result, applicationContext);
		}
    }
}