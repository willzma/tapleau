package org.softwarelibre.tapleau;

import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

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
        setContentView(R.layout.content_main);

        PACKAGE_NAME = getApplicationContext().getPackageName();

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
