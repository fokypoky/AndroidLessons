package ru.mirea.petukhov.a.mireaproject;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.provider.Settings;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DeviceIdFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DeviceIdFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public DeviceIdFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DeviceIdFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DeviceIdFragment newInstance(String param1, String param2) {
        DeviceIdFragment fragment = new DeviceIdFragment();
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
        View root = inflater.inflate(R.layout.fragment_device_id, container, false);
        Button saveIdButton = root.findViewById(R.id.saveDeviceId);
        saveIdButton.setOnClickListener(this);
        return root;
    }

    @Override
    public void onClick(View v) {
        String androidId = Settings.Secure.getString(getActivity().getContentResolver(), Settings.Secure.ANDROID_ID);
        writeToDb(androidId);
        Toast.makeText(getActivity(), "Данные записаны успешно", Toast.LENGTH_LONG).show();
    }

    private void writeToDb(String data){
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://mireaproject-d68ff-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference dbref = database.getReference("android_id");

        dbref.setValue(data);
    }
}