package org.softwarelibre.tapleau;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import org.softwarelibre.tapleau.haptic.fragments.BrailleFragment;
import org.softwarelibre.tapleau.haptic.fragments.HapticFragment;
import org.softwarelibre.tapleau.haptic.fragments.LanguageFragment;

import co.tanvas.haptics.service.adapter.HapticServiceAdapterEventListener;

public class MainActivity extends AppCompatActivity
        implements BrailleFragment.OnFragmentInteractionListener, LanguageFragment.OnFragmentInteractionListener {

    public static String PACKAGE_NAME;
    public HapticFragment currentFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.content_main);

        PACKAGE_NAME = getApplicationContext().getPackageName();

        Spinner spin = (Spinner) findViewById(R.id.spinner);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.languages_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        final ImageButton button = (ImageButton) findViewById(R.id.imageButton);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                InputMethodManager inputManager = (InputMethodManager)
                        getSystemService(Context.INPUT_METHOD_SERVICE);

                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
                performLineAnimation1();
            }
        });

        final ImageButton button1 = (ImageButton) findViewById(R.id.imageButton3);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                performLineAnimation1reverse();
            }
        });



        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.fragment_container) != null) {
            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create a new fragment to be placed in the activity layout
            currentFragment = BrailleFragment.newInstance(true, true, true, true, true, false);

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, currentFragment).commit();
        }
    }

    public void performLineAnimation1() {

        View think_you = findViewById(R.id.editText);
        View search = findViewById(R.id.imageButton);
        TranslateAnimation slide = new TranslateAnimation(0.0f, 0.0f, 0.0f, -800.0f);

        slide.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation arg0) {
                Spinner spin = (Spinner) findViewById(R.id.spinner);
                spin.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                EditText edited = ((EditText) findViewById(R.id.editText));
                edited.setVisibility(View.INVISIBLE);
                EditText tv = ((EditText) findViewById(R.id.editText2));
                tv.setVisibility(View.VISIBLE);
                tv.setText(edited.getText());

                ImageButton ib = (ImageButton) findViewById(R.id.imageButton);
                ib.setVisibility(View.GONE);
                ImageButton ib1 = (ImageButton) findViewById(R.id.imageButton2);
                ib1.setVisibility(View.VISIBLE);
                ImageButton ib2 = (ImageButton) findViewById(R.id.imageButton3);
                ib2.setVisibility(View.VISIBLE);

                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 2);
                LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 5);
                LinearLayout.LayoutParams lp3 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
                RelativeLayout layout1 = ((RelativeLayout) findViewById(R.id.layout1));
                FrameLayout layout2 = ((FrameLayout) findViewById(R.id.fragment_container));
                LinearLayout layout3 = ((LinearLayout) findViewById(R.id.layout3));
                layout1.setLayoutParams(lp);
                layout2.setLayoutParams(lp2);
                layout3.setLayoutParams(lp3);
            }
        });

        slide.setDuration(700);
        think_you.startAnimation(slide);
        search.startAnimation(slide);
        //slide.setFillEnabled(true);




    }

    public void performLineAnimation1reverse() {

        View think_you = findViewById(R.id.editText2);
        View search = findViewById(R.id.imageButton2);
        TranslateAnimation slide = new TranslateAnimation(0.0f, 0.0f, 0.0f, 800.0f);

        slide.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation arg0) {
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1);
                LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(0, 0, 0);
                LinearLayout.LayoutParams lp3 = new LinearLayout.LayoutParams(0, 0, 0);
                RelativeLayout layout1 = ((RelativeLayout) findViewById(R.id.layout1));
                FrameLayout layout2 = ((FrameLayout) findViewById(R.id.fragment_container));
                LinearLayout layout3 = ((LinearLayout) findViewById(R.id.layout3));
                layout1.setLayoutParams(lp);
                layout2.setLayoutParams(lp2);
                layout3.setLayoutParams(lp3);
                ImageButton ib2 = (ImageButton) findViewById(R.id.imageButton3);
                ib2.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }

            @Override
            public void onAnimationEnd(Animation arg0) {
                EditText edited = ((EditText) findViewById(R.id.editText2));
                edited.setVisibility(View.INVISIBLE);
                EditText tv = ((EditText) findViewById(R.id.editText));
                tv.setVisibility(View.VISIBLE);
                tv.setText(edited.getText());
                Spinner spin = (Spinner) findViewById(R.id.spinner);
                spin.setVisibility(View.VISIBLE);
                ImageButton ib = (ImageButton) findViewById(R.id.imageButton2);
                ib.setVisibility(View.GONE);
                ImageButton ib1 = (ImageButton) findViewById(R.id.imageButton);
                ib1.setVisibility(View.VISIBLE);


            }
        });

        slide.setDuration(700);
        think_you.startAnimation(slide);
        search.startAnimation(slide);
        //slide.setFillEnabled(true);



    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            // Register the activity as an event handler
            HapticServiceAdapterEventListener listener =
                    HapticServiceAdapterEventListener.obtain(this);
            listener.addHandler(currentFragment);
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
