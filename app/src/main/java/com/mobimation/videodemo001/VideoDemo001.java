package com.mobimation.videodemo001;

import com.mobimation.videodemo001.R;

import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View.OnKeyListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.VideoView;

// Simple video playback demo. This version plays an FLV (Flash file) off a remote server.
// By pressing the Menu button and Preferences you can experiment with specifying other URLs and try playing that.
// This version is enhanced to demo the video pausing / prompting feature

public class VideoDemo001 extends Activity implements OnKeyListener, AnimationListener {
	private String videoURL="";
	boolean load=true;
	boolean paused=false;
	int pos=0;
	private int answer;
	VideoDemo001 a;
	VideoView vv;
	Animation myFadeInAnimation;
	Animation myFadeOutAnimation;
	View q;
	View o;
	Button b1;
	Button bx;
	Button b2;
	static boolean menu=false;
	
	private RelativeLayout parent;
	private VideoView video;
	
	

    public void onAnimationEnd(Animation animation) {
     if (animation==myFadeInAnimation)
    	pauseVideo(); // Pause video after menu overlay has faded in
    } 

    public void onAnimationRepeat(Animation animation) { 

    } 

    public void onAnimationStart(Animation animation) { 
    	if (animation==myFadeOutAnimation)
            resumeVideo();  // Resume video as soon as fade out animation starts
    } 
	
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
        case R.id.preferences:
        	// Launch the preferences editor
            Intent myIntent = new Intent(this,PreferencesEditor.class);
            startActivity(myIntent);
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
	
    /** Called when a View hs become attached to its Window */
/*    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.e("display", "parent is " + parent.isHardwareAccelerated());
        Log.e("display", "video is " + video.isHardwareAccelerated());    
    } */
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.main);
        /*
 getWindow().setFlags(WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
        		, WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED); */
        a=this;
        vv= (VideoView) this.findViewById(R.id.video1);
        
        parent = (RelativeLayout) findViewById(R.id.FrameLayout1);
        // parent.setLayerType(View.LAYER_TYPE_HARDWARE, null);

        video = (VideoView) findViewById(R.id.video1);
      
        
        vv.requestFocus();
        o=(View) findViewById(R.id.overlay1);
        myFadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fadein);
        myFadeInAnimation.setAnimationListener(this);

        
        myFadeOutAnimation = AnimationUtils.loadAnimation(this, R.anim.fadeout);
        myFadeOutAnimation.setAnimationListener(this);
        b1=(Button) findViewById(R.id.b1);
        bx=(Button) findViewById(R.id.bx);
        b2=(Button) findViewById(R.id.b2);
        SharedPreferences settings = getSharedPreferences("VideoSettings", 0);
  	    videoURL=settings.getString("videoURL", null);
        if (videoURL==null)
    	  videoURL=PreferencesEditor.defaultURL;
        
        vv = (VideoView) findViewById(R.id.video1);
        
//  	vv.setVideoPath(videoURL);   
  	    vv.setVideoURI(Uri.parse(videoURL));
  	    
  	    vv.start();
  	     // q.requestFocus();
    	o.setOnKeyListener(this);
  	    o.setVisibility(View.INVISIBLE); 
  	  vv.setOnKeyListener(this);
    }
    
    @Override
    protected void onResume() {
    	SharedPreferences settings = getSharedPreferences("VideoSettings", 0);
  	    videoURL=settings.getString("videoURL", null);
        if (videoURL==null)
    	  videoURL=PreferencesEditor.defaultURL;
        
        vv = (VideoView) findViewById(R.id.video1);

        vv.setVideoURI(Uri.parse(videoURL));
        vv.start();
        load=false; 
        super.onResume();
    }
    
	public boolean onKey(final View v, int keyCode, KeyEvent event) {
		switch (keyCode) {
		// Here we take care of some key events. You can add more key codes for adding
		// more functionality.
		case 126:   // PLAY
		 if ((event.getAction()==KeyEvent.ACTION_DOWN)) {
              if (load) {
            	  // First time video prepares there is an initial delay
            	  // vv.setVideoPath(videoURL);
            	  vv.setVideoURI(Uri.parse(videoURL));
            	  vv.start();
            	  load=false;
              }
              else {
            	  vv.seekTo(0);  // Rapid seek to 0 for already playing app
            	  vv.start();
              }
		 }
		 break;
		case 41: // M = Question fades in/out, video pauses/resumes
			if (event.getAction()==KeyEvent.ACTION_DOWN) {
				if (!menu) {   // Show
		        	 menu=true;
	        	    o.post(new Runnable() {
	                  public void run() {
	                	o.setVisibility(View.VISIBLE);
 	                	o.startAnimation(myFadeInAnimation); 
	                	b1.requestFocus();
	                	bx.setPressed(false);
	                	b2.setPressed(false);
	                	b1.setOnKeyListener(a);
	                	bx.setOnKeyListener(a);
	                	b2.setOnKeyListener(a);
//		                pauseVideo();
	                  }
	                });  	
				}
				else {  // Fade out / Resume video
			        	o.post(new Runnable() {
			                public void run() {

			                	o.clearFocus();
			                	o.setVisibility(View.INVISIBLE);
			                	o.startAnimation(myFadeOutAnimation);
			                  }
			                });		
			        	menu=false;
				}
			}
		 break;
		 
		case 23:   // D-PAD SELECT KEY PRESSED
		  int res=0;

      	  o.post(new Runnable() {
            public void run() {
      		  if (v==b1) answer=0; else if (v==bx) answer=1; else if (v==b2) answer=2;
      		    comment("Your answer was "+ answer );
            	b1.invalidate();
            	bx.invalidate();
            	b2.invalidate();
            	o.setVisibility(View.INVISIBLE); 
            	resumeVideo();
              }
            });		
  
         vv.requestFocus();
      	 menu=false;
		 break;
		 
		case 127:  // PAUSE transport key
			if (event.getAction()==KeyEvent.ACTION_DOWN) {
				// Pause a playing song if any
				if (paused) {
				 // Resume
				 paused=false;
				 vv.seekTo(pos);  // Recall paused pos
				 vv.start();      // Resume
				}
				else {
				  vv.pause();     // Pause
				  pos=vv.getCurrentPosition();  // Remember current pos
				  pos=pos+200;  // Empirical lag compensation
			      paused=true;
				}
			}
		break;
		
		case 86:  // STOP tramsport key
			if (event.getAction()==KeyEvent.ACTION_DOWN)
				if (event.getMetaState()==8)  // FN key pressed
				// Stop the media player task
					vv.stopPlayback();
					paused=false;
		break;
		case 89:  // REWIND
			if (event.getAction()==KeyEvent.ACTION_DOWN) {
				// Rewind in song
				pos=vv.getCurrentPosition();
			    pos=pos-(1000+1000*(event.getRepeatCount()/10));  // Seek acceleration backwards
			    if (pos<0) pos=0;
			    vv.seekTo(pos);
		}
		break;
		case 90:  // FAST FORWARD
			if (event.getAction()==KeyEvent.ACTION_DOWN) {
				// Fast forward in song
				pos=vv.getCurrentPosition();
			    pos=pos+(1000+(1000*(event.getRepeatCount()/10))); // Seek acceleration forward
			    int playend=vv.getDuration();
			    if (pos>playend)
			    	pos=playend;
			    vv.seekTo(pos);
			}
	    break;

		default:
		}
		return false;
		}
	
	private void pauseVideo() {
          vv.pause();
		  pos=vv.getCurrentPosition();
		  pos=pos+200;  // Empirical lag compensation
	      paused=true;
	}
	private void resumeVideo() {
		if (paused) {
		 // Resume
		 paused=false;
		 vv.seekTo(pos);
		 vv.start();
		}
	}
	private void comment(String verdict) {
		Toast.makeText(this, verdict, Toast.LENGTH_SHORT).show();
	}
}