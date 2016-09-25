package org.softwarelibre.tapleau.haptic.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import org.softwarelibre.tapleau.MainActivity;
import org.softwarelibre.tapleau.R;
import org.softwarelibre.tapleau.haptic.symbols.BrailleDot;

import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BrailleFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BrailleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BrailleFragment extends HapticFragment {
    private boolean[] enabledDots = new boolean[6];

    public BrailleDot[][] circles = new BrailleDot[3][2];

    private OnFragmentInteractionListener mListener;

    public BrailleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment BrailleFragment.
     */
    public static BrailleFragment newInstance(boolean b1, boolean b2, boolean b3, boolean b4, boolean b5, boolean b6) {
        BrailleFragment fragment = new BrailleFragment();
        fragment.enabledDots[0] = b1;
        fragment.enabledDots[1] = b2;
        fragment.enabledDots[2] = b3;
        fragment.enabledDots[3] = b4;
        fragment.enabledDots[4] = b5;
        fragment.enabledDots[5] = b6;
        Bundle args = new Bundle();
        args.putBoolean("b1", b1);
        args.putBoolean("b2", b2);
        args.putBoolean("b3", b3);
        args.putBoolean("b4", b4);
        args.putBoolean("b5", b5);
        args.putBoolean("b6", b6);
        fragment.setArguments(args);
        return fragment;
    }

    public static BrailleFragment newInstance(boolean[] bools) {
        return newInstance(bools[0], bools[1], bools[2], bools[3], bools[4], bools[5]);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            System.out.println("Does this shit even run");
            enabledDots[0] = getArguments().getBoolean("b1");
            enabledDots[1] = getArguments().getBoolean("b2");
            enabledDots[2] = getArguments().getBoolean("b3");
            enabledDots[3] = getArguments().getBoolean("b4");
            enabledDots[4] = getArguments().getBoolean("b5");
            enabledDots[5] = getArguments().getBoolean("b6");
        }
        System.out.println(Arrays.toString(enabledDots));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_braille, container, false);
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
        System.out.println("Service adapter was successfully created");
    }

    @Override
    public void serviceAdapterIsConnecting(Intent intent) {
        System.out.println("Service adapter is connecting");
    }

    @Override
    public void serviceAdapterIsConnected(Intent intent) {
        System.out.println("Service adapter has successfully connected.");
        double row = 382.7;
        double col = 220.8;
        for (int i = 0; i < circles.length; i++) {
            for (int j = 0; j < circles[0].length; j++) {
                if (enabledDots[(i * 2) + j]) {
                    String identifier = "button" + i + j;
                    ImageView currentDotView = (ImageView) getView().findViewById(getResources().
                            getIdentifier(identifier, "id", MainActivity.PACKAGE_NAME));
                    circles[i][j] = new BrailleDot(this.getActivity(), currentDotView, col, row);
                    currentDotView.setImageResource(R.drawable.graydot);

                    currentDotView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            ((ImageView) v).setImageResource(R.drawable.bluedot);
                            return true;
                        }
                    });

                    currentDotView.setOnDragListener(new View.OnDragListener() {

                        @Override
                        public boolean onDrag(View v, DragEvent event) {
                            ((ImageView) v).setImageResource(R.drawable.bluedot);
                            return true;
                        }
                    });
                }
                col += 768;
            }

            row += 514.8;
            col = 220.8;
        }
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
