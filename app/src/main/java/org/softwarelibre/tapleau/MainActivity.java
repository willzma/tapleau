package org.softwarelibre.tapleau;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.memetix.mst.language.Language;
import com.memetix.mst.translate.Translate;
import com.microsoft.bing.speech.SpeechClientStatus;
import com.microsoft.cognitiveservices.speechrecognition.DataRecognitionClient;
import com.microsoft.cognitiveservices.speechrecognition.ISpeechRecognitionServerEvents;
import com.microsoft.cognitiveservices.speechrecognition.MicrophoneRecognitionClient;
import com.microsoft.cognitiveservices.speechrecognition.RecognitionResult;
import com.microsoft.cognitiveservices.speechrecognition.RecognitionStatus;
import com.microsoft.cognitiveservices.speechrecognition.SpeechRecognitionMode;
import com.microsoft.cognitiveservices.speechrecognition.SpeechRecognitionServiceFactory;

import org.softwarelibre.tapleau.haptic.fragments.BrailleFragment;
import org.softwarelibre.tapleau.haptic.fragments.HapticFragment;
import org.softwarelibre.tapleau.haptic.fragments.LanguageFragment;
import org.softwarelibre.tapleau.haptic.symbols.SymbolUtilities;

import java.util.concurrent.TimeUnit;

import co.tanvas.haptics.service.adapter.HapticServiceAdapterEventListener;

public class MainActivity extends AppCompatActivity implements ISpeechRecognitionServerEvents, BrailleFragment.OnFragmentInteractionListener, LanguageFragment.OnFragmentInteractionListener {

    public static String PACKAGE_NAME;
    public HapticFragment currentFragment;
    int m_waitSeconds = 0;
    DataRecognitionClient dataClient = null;
    MicrophoneRecognitionClient micClient = null;
    MainActivity.FinalResponseStatus isReceivedResponse = MainActivity.FinalResponseStatus.NotReceived;
    EditText searchText;
    String translatedText;
    String currentWord;
    String currentLanguage;
    int wordPointer = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.content_main);

        PACKAGE_NAME = getApplicationContext().getPackageName();

        Spinner spin = (Spinner) findViewById(R.id.spinner);
        final Spinner spinner = (Spinner) findViewById(R.id.spinner);
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

                if(spinner.getSelectedItem().equals("Korean")) {
                    new MyAsyncTask() {
                        protected void onPostExecute(Boolean result) {
                            searchText.setHint(translatedText);
                            searchText.setText("");
                            currentWord = translatedText;
                            currentLanguage = "Korean";
                        }
                    }.execute(searchText.getText().toString());
                } else if (spinner.getSelectedItem().equals("Braille")) {
                    String s = searchText.getText().toString();
                    searchText.setHint(searchText.getText());
                    searchText.setText("");
                    Typeface myTypeFace = Typeface.createFromAsset(getAssets(), "fonts/braille.ttf");
                    currentWord = s;
                    currentLanguage = "Braille";
                }
            }
        });

        final Button nextButton = (Button) findViewById(R.id.nextButton);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wordPointer < currentWord.length()) {
                    switch(currentLanguage) {
                        case "Braille": {
                            currentFragment = BrailleFragment.newInstance(SymbolUtilities.textToBraille(currentWord.charAt(wordPointer)));
                            getSupportFragmentManager().beginTransaction()
                                    .add(R.id.fragment_container, currentFragment).commit();
                            wordPointer++;
                        }break;case "Korean": {
                            currentFragment = LanguageFragment.newInstance(String.valueOf(currentWord.charAt(wordPointer)));
                            getSupportFragmentManager().beginTransaction()
                                    .add(R.id.fragment_container, currentFragment).commit();
                            wordPointer++;
                        }break;
                    }
                } else {
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
            }
        });

        final ImageButton button1 = (ImageButton) findViewById(R.id.imageButton3);
        button1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                performLineAnimation1reverse();
            }
        });



        Translate.setClientId(getString(R.string.translate_client_id));
        Translate.setClientSecret(getString(R.string.translate_client_secret));

        searchText = (EditText) findViewById(R.id.editText);

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

    public enum FinalResponseStatus {NotReceived, OK, Timeout}

    /**
     * Gets the primary subscription key
     */
    public String getPrimaryKey() {
        return this.getString(R.string.primaryKey);
    }

    /**
     * Gets the LUIS application identifier.
     *
     * @return The LUIS application identifier.
     */
    private String getLuisAppId() {
        return this.getString(R.string.luisAppID);
    }

    /**
     * Gets the LUIS subscription identifier.
     *
     * @return The LUIS subscription identifier.
     */
    private String getLuisSubscriptionID() {
        return this.getString(R.string.luisSubscriptionID);
    }

    /**
     * Gets a value indicating whether or not to use the microphone.
     *
     * @return true if [use microphone]; otherwise, false.
     */
    /*
    private Boolean getUseMicrophone() {
        int id = this._radioGroup.getCheckedRadioButtonId();
        return id == R.id.micIntentRadioButton ||
                id == R.id.micDictationRadioButton ||
                id == (R.id.micRadioButton - 1);
    }
    */
    private Boolean getUseMicrophone() {
        return true;
    }


    /**
     * Gets a value indicating whether LUIS results are desired.
     *
     * @return true if LUIS results are to be returned otherwise, false.
     */
    /*
    private Boolean getWantIntent() {
        int id = this._radioGroup.getCheckedRadioButtonId();
        return id == R.id.dataShortIntentRadioButton ||
                id == R.id.micIntentRadioButton;
    }
    */

    /**
     * Gets the current speech recognition mode.
     *
     * @return The speech recognition mode.
     */
    private SpeechRecognitionMode getMode() {
        return SpeechRecognitionMode.ShortPhrase;
    }
    /*
    private SpeechRecognitionMode getMode() {
        int id = this._radioGroup.getCheckedRadioButtonId();
        if (id == R.id.micDictationRadioButton ||
                id == R.id.dataLongRadioButton) {
            return SpeechRecognitionMode.LongDictation;
        }
        return SpeechRecognitionMode.ShortPhrase;
    }
    */

    /**
     * Gets the default locale.
     *
     * @return The default locale.
     */
    private String getDefaultLocale() {
        return "en-us";
    }

    /**
     * Gets the short wave file path.
     *
     * @return The short wave file.
     */
    private String getShortWaveFile() {
        return "whatstheweatherlike.wav";
    }

    /**
     * Gets the long wave file path.
     *
     * @return The long wave file.
     */
    private String getLongWaveFile() {
        return "batman.wav";
    }

    /**
     * Handles the Click event of the _startButton control.
     */
    private void StartButton_Click(View arg0) {
        searchText.setText("");

        this.m_waitSeconds = this.getMode() == SpeechRecognitionMode.ShortPhrase ? 20 : 200;

        this.LogRecognitionStart();
        if (this.getUseMicrophone()) {
            if (this.micClient == null) {
                this.micClient = SpeechRecognitionServiceFactory.createMicrophoneClient(
                        this,
                        this.getMode(),
                        this.getDefaultLocale(),
                        this,
                        this.getPrimaryKey());
            }
        }
        this.micClient.startMicAndRecognition();
    }


    /**
     * Logs the recognition start.
     */
    private void LogRecognitionStart() {
        String recoSource;
        if (this.getUseMicrophone()) {
            recoSource = "microphone";
        } else if (this.getMode() == SpeechRecognitionMode.ShortPhrase) {
            recoSource = "short wav file";
        } else {
            recoSource = "long wav file";
        }

    }

    private void SendAudioHelper(String filename) {
        RecognitionTask doDataReco = new RecognitionTask(this.dataClient, this.getMode(), filename);
        try {
            doDataReco.execute().get(m_waitSeconds, TimeUnit.SECONDS);
        } catch (Exception e) {
            doDataReco.cancel(true);
            isReceivedResponse = FinalResponseStatus.Timeout;
        }
    }

    public void onFinalResponseReceived(final RecognitionResult response) {
        boolean isFinalDicationMessage = this.getMode() == SpeechRecognitionMode.LongDictation &&
                (response.RecognitionStatus == RecognitionStatus.EndOfDictation ||
                        response.RecognitionStatus == RecognitionStatus.DictationEndSilenceTimeout);
        if (null != this.micClient && this.getUseMicrophone() && ((this.getMode() == SpeechRecognitionMode.ShortPhrase) || isFinalDicationMessage)) {
            // we got the final result, so it we can end the mic reco.  No need to do this
            // for dataReco, since we already called endAudio() on it as soon as we were done
            // sending all the data.
            this.micClient.endMicAndRecognition();
        }

        if (isFinalDicationMessage) {
            this.isReceivedResponse = FinalResponseStatus.OK;
        }



        }

    /**
     * Called when a final response is received and its intent is parsed
     */
    public void onIntentReceived(final String payload) {
    }

    public void onPartialResponseReceived(final String response) {
//        this.WriteLine("--- Partial result received by onPartialResponseReceived() ---");
//        this.WriteLine(response);
//        this.WriteLine();
        searchText.setText(response);
    }

    public void onError(final int errorCode, final String response) {
    }

    /**
     * Called when the microphone status has changed.
     *
     * @param recording The current recording state
     */
    public void onAudioEvent(boolean recording) {
        if (!recording) {
            this.micClient.endMicAndRecognition();
        }
    }




    /*
     * Speech recognition with data (for example from a file or audio source).
     * The data is broken up into buffers and each buffer is sent to the Speech Recognition Service.
     * No modification is done to the buffers, so the user can apply their
     * own VAD (Voice Activation Detection) or Silence Detection
     *
     * @param dataClient
     * @param recoMode
     * @param filename
     */
    private class RecognitionTask extends AsyncTask<Void, Void, Void> {
        DataRecognitionClient dataClient;
        SpeechRecognitionMode recoMode;
        String filename;

        RecognitionTask(DataRecognitionClient dataClient, SpeechRecognitionMode recoMode, String filename) {
            this.dataClient = dataClient;
            this.recoMode = recoMode;
            this.filename = filename;
        }

        @Override
        protected Void doInBackground(Void... params) {
            return null;
        }



    }
    class MyAsyncTask extends AsyncTask<String, Integer, Boolean> {
        @Override
        protected Boolean doInBackground(String... strings) {
            try {
                translatedText = Translate.execute(strings[0], Language.ENGLISH, Language.KOREAN);
                Log.d("!!!", translatedText);
            } catch (Exception e) {
                Log.e("!!!", "!!!", e);
            }
            return true;
        }
    }
}

