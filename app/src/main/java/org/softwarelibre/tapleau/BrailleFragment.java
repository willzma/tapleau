package org.softwarelibre.tapleau;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.softwarelibre.tapleau.symbols.BrailleCircle;

import co.tanvas.haptics.service.adapter.iHapticServiceAdapterEventHandler;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BrailleFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BrailleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BrailleFragment extends Fragment implements iHapticServiceAdapterEventHandler {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BrailleCircle[][] circles = new BrailleCircle[3][2];

    private OnFragmentInteractionListener mListener;

    public BrailleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BrailleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BrailleFragment newInstance(String param1, String param2) {
        BrailleFragment fragment = new BrailleFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_braille, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void serviceAdapterWasCreated(Intent intent) {
        Log.d("tapleau", "Service adapter was successfully created.");
    }

    @Override
    public void serviceAdapterIsConnecting(Intent intent) {
        Log.d("tapleau", "Service adapter is connecting...");
    }

    @Override
    public void serviceAdapterIsConnected(Intent intent) {
        Log.d("tapleau", "Service adapter has successfully connected.");
        double row = 382.7;
        double col = 220.8;
        for (int i = 0; i < circles.length; i++) {
            for (int j = 0; j < circles[0].length; j++) {
                String s = "button" + i + j;
                circles[i][j] = new BrailleCircle(this.getActivity(), getView().findViewById(getResources().
                        getIdentifier(s, "id", MainActivity.PACKAGE_NAME)), col, row);
                col += 768;
            }

            row += 514.8;
            col = 220.8;
        }
    }

    @Override
    public void serviceAdapterIsDisconnecting(Intent intent) {
        Log.d("tapleau", "Service adapter is disconnecting...");
    }

    @Override
    public void serviceAdapterIsDisconnected(Intent intent) {
        Log.d("tapleau", "Service adapter has successfully disconnected.");
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
