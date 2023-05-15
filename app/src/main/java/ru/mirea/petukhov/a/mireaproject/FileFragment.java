package ru.mirea.petukhov.a.mireaproject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

public class FileFragment extends Fragment implements View.OnClickListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public FileFragment() {
        // Required empty public constructor
    }
    public static FileFragment newInstance(String param1, String param2) {
        FileFragment fragment = new FileFragment();
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
        View view = inflater.inflate(R.layout.fragment_file, container, false);
        view.findViewById(R.id.saveButton).setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("File path to save");

        final EditText input = new EditText(getContext());

        input.setInputType(InputType.TYPE_CLASS_TEXT /*| InputType.TYPE_TEXT_VARIATION_PASSWORD*/);
        builder.setView(input);

        builder.setPositiveButton("Save file", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                SharedPreferences preferences = getActivity()
                        .getSharedPreferences("mp-projile-preferences", Context.MODE_PRIVATE);

                String data = preferences.getString("ProfileData", "not set");
                String filePath = input.getText().toString();

                FileOutputStream stream;
                try
                {
                    stream = getActivity().openFileOutput(filePath, Context.MODE_PRIVATE);
                    stream.write(data.getBytes());
                    stream.close();
                    Toast.makeText(getActivity(), "Файл успешно записан", Toast.LENGTH_LONG).show();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}