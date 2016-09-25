package org.softwarelibre.tapleau.haptic.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import org.softwarelibre.tapleau.R;
import org.softwarelibre.tapleau.haptic.symbols.SymbolUtilities;

import co.tanvas.haptics.service.adapter.HapticServiceAdapter;
import co.tanvas.haptics.service.app.HapticApplication;
import co.tanvas.haptics.service.model.HapticMaterial;
import co.tanvas.haptics.service.model.HapticSprite;
import co.tanvas.haptics.service.model.HapticTexture;
import co.tanvas.haptics.service.model.HapticView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LanguageFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LanguageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LanguageFragment extends HapticFragment {
    private static final String ARG_PARAM1 = "syllable";

    private HapticView mHapticView;
    private HapticTexture mHapticTexture;
    private HapticMaterial mHapticMaterial;
    private HapticSprite mHapticSprite;

    private String syllable;

    public Bitmap foreground;
    public Bitmap background;
    public Bitmap hapticMap;
    public ImageView iv;

    private OnFragmentInteractionListener mListener;

    public LanguageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param syllable
     * @return A new instance of fragment LanguageFragment.
     */
    public static LanguageFragment newInstance(String syllable) {
        LanguageFragment fragment = new LanguageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, syllable);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Typeface myTypeFace = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Trebuchet.ttf");
        foreground = SymbolUtilities.drawTextToBitmap(getActivity(), syllable, Color.rgb(160, 160, 160), -1);
        background = SymbolUtilities.drawTextToBitmap(getActivity(), syllable, Color.rgb(21, 126, 251), -1);
        hapticMap = SymbolUtilities.getHapticMap(getActivity(), syllable);
        iv =  (ImageView) getView().findViewById(R.id.tableau);
        //iv.setImageBitmap(foreground);
        //iv.setImageBitmap(getHapticMap());

        if (getArguments() != null) {
            syllable = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_language, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void serviceAdapterWasCreated(Intent intent) {
        System.out.println("Service adapter was successfully created");
    }

    @Override
    public void serviceAdapterIsConnecting(Intent intent) {
        System.out.println("Service adapter is connecting");
    }

    @Override
    public void serviceAdapterIsConnected(Intent intent) {
        System.out.println("Service adapter has successfully connected.");

        HapticServiceAdapter serviceAdapter = HapticApplication.getHapticServiceAdapter();
        try {
            mHapticView = HapticView.create(serviceAdapter);
            mHapticView.activate();

            Display display = ((WindowManager) getActivity().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
            int rotation = display.getRotation();
            HapticView.Orientation orientation = HapticView.getOrientationFromAndroidDisplayRotation(rotation);
            mHapticView.setOrientation(orientation);

            Bitmap hapticBitmap = BitmapFactory.decodeResource(getActivity().getResources(), R.drawable.white_circle);
            byte[] textureData = HapticTexture.createTextureDataFromBitmap(hapticBitmap);

            mHapticTexture = HapticTexture.create(serviceAdapter);
            int textureDataWidth = hapticBitmap.getRowBytes() / 4; // 4 channels, i.e., ARGB
            int textureDataHeight = hapticBitmap.getHeight();
            mHapticTexture.setSize(textureDataWidth, textureDataHeight);
            mHapticTexture.setData(textureData);

            // Create a haptic material with the created haptic texture
            mHapticMaterial = HapticMaterial.create(serviceAdapter);
            mHapticMaterial.setTexture(0, mHapticTexture);
            // Create a haptic sprite with the haptic material
            mHapticSprite = HapticSprite.create(serviceAdapter);
            mHapticSprite.setMaterial(mHapticMaterial);
            // Set the size and position of the haptic sprite to correspond to the view we created
            mHapticSprite.setSize(324, 317);
            mHapticSprite.setPosition(0, 512);
            // Add the haptic sprite to the haptic view
            mHapticView.addSprite(mHapticSprite);
        } catch (Exception e) {
            e.printStackTrace();
        }

        iv.setImageBitmap(foreground);
    }

    @Override
    public void serviceAdapterIsDisconnecting(Intent intent) {
        System.out.println("Service adapter is disconnecting");
    }

    @Override
    public void serviceAdapterIsDisconnected(Intent intent) {
        System.out.println("Service adapter has successfully disconnected");
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
