package ru.mirea.petukhov.a.mireaproject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
public class ProfileFragment extends Fragment implements View.OnClickListener{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private EditText infoEditTextView;
    private Button saveChangesButton;
    public ProfileFragment() {
        // Required empty public constructor
    }
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        infoEditTextView = view.findViewById(R.id.infoEditText);
        view.findViewById(R.id.saveButtonClick).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        // ЗАПИСЫВАЮ В SHARED PREFERENCES, ЧТОБЫ В ДРУГОМ ФРАГМЕНТЕ ИХ ПРОЧИТАТЬ И ЗАПИСАТЬ В ФАЙЛ
        String data = infoEditTextView.getText().toString();
        SharedPreferences preferences = getActivity()
                .getSharedPreferences("mp-projile-preferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("ProfileData", data);
        editor.apply();
        Toast.makeText(getActivity(), "Текст успешно сохранен", Toast.LENGTH_LONG).show();
    }
}