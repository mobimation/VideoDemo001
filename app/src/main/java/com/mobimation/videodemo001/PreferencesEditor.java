package com.mobimation.videodemo001;

import com.mobimation.videodemo001.R;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

// Simple example of Preferences settings panel
// where settings are stored in local storage using the
// Shared Preferences store.
public class PreferencesEditor extends Activity {
    private String PREFS_NAME = "VideoSettings";
    public static String defaultURL="http://www.lilldata.se/suzuki/GT750M-1.flv"; // Royalty free motorcycle demo video example
    private String videoURL="";
    
    Button submit;
    Button reset;
    EditText urlEdit;
    
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    setContentView(R.layout.preflayout);
	    submit = (Button)findViewById(R.id.button1);
	    reset  = (Button)findViewById(R.id.button2);
	    urlEdit= (EditText)findViewById(R.id.editURL);
	    // b.setOnClickListener(this);
		
	    submit.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	          videoURL=urlEdit.getText().toString();
	          SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
	          SharedPreferences.Editor editor = settings.edit();
	          
	          // Put settings of each field      
	          videoURL = urlEdit.getText().toString();
	          editor.putString("videoURL", videoURL);
	          
	          // Commit the edits
	          editor.commit();
	          editor.apply();
	          finish();
	        }
	      });
	    
	    reset.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	      	  videoURL=defaultURL;
	          urlEdit.setText(videoURL);  
	    }
	  });

	}
	
    @Override
    protected void onStop(){
    // When the editor closes the settings are saved
    // to a shared preferences store.
       super.onStop();
      // Open persistent store for editing
      SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
      SharedPreferences.Editor editor = settings.edit();
      
      // Put settings of each field      
      videoURL = urlEdit.getText().toString();
      editor.putString("videoURL", videoURL);
      
      // Commit the edits
      editor.commit();
    }
    
    @Override
    protected void onStart() {  // Reload settings when editor starts
      super.onStart();
	  SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
	  if (settings.contains("videoURL")) // If prefs set
	    videoURL=settings.getString("videoURL", null);
      if (videoURL==null) 
    	  videoURL=defaultURL;
       urlEdit.setText(videoURL);  
    }
}
