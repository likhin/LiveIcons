package com.nkl.liveicons;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

public class LiveIcons extends Activity {
	
	RadioGroup rg;
	Button bt;
    CheckBox cb1,cb2;
	
	final String KEY_SAVED_RADIO_BUTTON_INDEX = "SAVED_RADIO_BUTTON_INDEX";
    final String KEY_WHATSAPP_PULSE = "WHATSAPP_PULSE";
    final String KEY_HELLO_PULSE = "HELLO_PULSE";

	@SuppressLint("WorldReadableFiles")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Log.i("LiveIcons" , "---Manufacturer is " + Build.MANUFACTURER + "--- ");
		debug(" ---Manufacturer is " + Build.MANUFACTURER + "--- " );
		
		rg = (RadioGroup) findViewById(R.id.msgIconRadio);
		rg.setOnCheckedChangeListener(radioGroupOnCheckedChangeListener);
		
		bt = (Button)findViewById(R.id.apply_button);

        cb1 = (CheckBox) findViewById(R.id.checkBoxWhatsApp);
        cb2 = (CheckBox) findViewById(R.id.checkBoxHello);


        ImageView img = (ImageView)findViewById(R.id.imageView);
        img.setBackgroundResource(R.drawable.flip_sms);
        AnimationDrawable frameAnimation = (AnimationDrawable) img.getBackground();
        frameAnimation.start();

        ImageView img2 = (ImageView)findViewById(R.id.imageView2);
        img2.setBackgroundResource(R.drawable.breathing_sms);
        AnimationDrawable frameAnimation2 = (AnimationDrawable) img2.getBackground();
        frameAnimation2.start();

        ImageView img3 = (ImageView)findViewById(R.id.imageView3);
        img3.setBackgroundResource(R.drawable.whatsapp_pulse);
        AnimationDrawable frameAnimation3 = (AnimationDrawable) img3.getBackground();
        frameAnimation3.start();

        ImageView img4 = (ImageView)findViewById(R.id.imageView4);
        img4.setBackgroundResource(R.drawable.hello_pulse);
        AnimationDrawable frameAnimation4 = (AnimationDrawable) img4.getBackground();
        frameAnimation4.start();



        LoadPreferences();
		       
	}
	
	OnCheckedChangeListener radioGroupOnCheckedChangeListener =
			new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(RadioGroup group, int checkedId) {
            // Save the index of the checked radio button to MY_PREF preferences file
			RadioButton checkedRadioButton = (RadioButton) rg.findViewById(checkedId);
			int checkedIndex = rg.indexOfChild(checkedRadioButton);
			SavePreferences(KEY_SAVED_RADIO_BUTTON_INDEX, checkedIndex);
		}
	};

	
	@SuppressLint("WorldReadableFiles")
	@SuppressWarnings("deprecation")
	private void SavePreferences(String key, int value) {
        // Loads preferences file MY_PREF
		SharedPreferences pref = getSharedPreferences("MY_PREF", MODE_WORLD_READABLE);
		SharedPreferences.Editor editor = pref.edit();
        // Puts integer value "value" to tag "key" to MY_PREF
		editor.putInt(key, value);
        // Saves changes to file
		editor.commit();
	}
	
	@SuppressLint("WorldReadableFiles")
	@SuppressWarnings("deprecation")
	private void LoadPreferences() {
        // Loads preferences file MY_PREF
		SharedPreferences pref = getSharedPreferences("MY_PREF", MODE_WORLD_READABLE);

        // Get value at "SAVED_RADIO_BUTTON_INDEX" else use default value 0
		int savedRadioIndex = pref.getInt(KEY_SAVED_RADIO_BUTTON_INDEX, 0);
        // Link the radio button referenced by the index
		RadioButton savedCheckedRadioButton = (RadioButton) rg.getChildAt(savedRadioIndex);
        // Set the radio button as activated
		savedCheckedRadioButton.setChecked(true);

        // Get value at "WHATSAPP_PULSE" else use default value 1
        int checker = pref.getInt(KEY_WHATSAPP_PULSE, 1);
        CheckBox cb1 = (CheckBox) findViewById(R.id.checkBoxWhatsApp);
        // Set the value to the CheckBox
        if(checker == 1)
            cb1.setChecked(true);
        else
            cb1.setChecked(false);

        // Get value at "HELLO_PULSE" else use default value 1
        checker = pref.getInt(KEY_HELLO_PULSE, 1);
        CheckBox cb2 = (CheckBox) findViewById(R.id.checkBoxHello);
        // Set the value to the CheckBox
        if(checker == 1)
            cb2.setChecked(true);
        else
            cb2.setChecked(false);
	}
	
	public void apply (View view) {
        // Save all settings to preferences on clicking Apply button

        CheckBox cb1 = (CheckBox) findViewById(R.id.checkBoxWhatsApp);
        int checker = (cb1.isChecked()) ? 1 : 0;
        SavePreferences(KEY_WHATSAPP_PULSE, checker);

        CheckBox cb2 = (CheckBox) findViewById(R.id.checkBoxHello);
        checker = (cb2.isChecked()) ? 1 : 0;
        SavePreferences(KEY_HELLO_PULSE, checker);

        // Display a short toast demanding a reboot
        Context context = getApplicationContext();
        CharSequence text = "Done! Can i haz reboot? :3";
        int duration = Toast.LENGTH_SHORT;
		Toast toast = Toast.makeText(context, text, duration);
		toast.show();
	}
	
	public static void debug(String text) {
        // Save logs to a file LIlog.txt on sdcard
        File logFile = new File("sdcard/LIlog.txt");

        String currentDateTime = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());

        if (!logFile.exists())
        {
            try
            {
                logFile.createNewFile();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        try
        {
            //BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append(currentDateTime).append(": LI> ").append(text);
            buf.newLine();
            buf.close();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }

}
