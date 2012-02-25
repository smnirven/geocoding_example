package example.geocoding;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class Utilities 
{	
	public static void showToast(CharSequence message, Context appContext)
    {
		int duration = Toast.LENGTH_SHORT;

		Toast toast = Toast.makeText(appContext, message, duration);
		toast.show();
    }
}
