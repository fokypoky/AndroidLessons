package ru.mirea.petukhov.a.mireaproject;

import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;

import androidx.core.os.CancellationSignal;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FingerprintFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FingerprintFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private TextView secretTextView;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FingerprintFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FingerprintFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FingerprintFragment newInstance(String param1, String param2) {
        FingerprintFragment fragment = new FingerprintFragment();
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
        View root = inflater.inflate(R.layout.fragment_fingerprint, container, false);

        secretTextView = root.findViewById(R.id.secretTextView);
        secretTextView.setVisibility(View.INVISIBLE);

        Button button = root.findViewById(R.id.authButton);
        button.setOnClickListener(this);

        return root;
    }

    @Override
    public void onClick(View v) {
        FingerprintManager fingerprintManager = (FingerprintManager) getActivity().getSystemService(getContext().FINGERPRINT_SERVICE);
        if(!fingerprintManager.isHardwareDetected() || !fingerprintManager.hasEnrolledFingerprints()){
            Toast.makeText(getActivity(), "No hardware or fingerprints detected", Toast.LENGTH_LONG).show();
            return;
        }

        android.os.CancellationSignal cancellationSignal = new android.os.CancellationSignal();
        fingerprintManager.authenticate(null, cancellationSignal, 0, new FingerprintManager.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errMsgId, CharSequence errString) {
               Toast.makeText(getActivity(),"Auth error: " + errString, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onAuthenticationFailed() {
                Toast.makeText(getActivity(), "Auth failed", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
                secretTextView.setVisibility(View.VISIBLE);
                secretTextView.setText("The most sensitive information");
            }
        }, null);
    }
}