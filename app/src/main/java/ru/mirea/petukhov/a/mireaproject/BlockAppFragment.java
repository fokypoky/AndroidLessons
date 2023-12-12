package ru.mirea.petukhov.a.mireaproject;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BlockAppFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BlockAppFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final Set<String> packageNames = new HashSet<>();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BlockAppFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BlockAppFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BlockAppFragment newInstance(String param1, String param2) {
        BlockAppFragment fragment = new BlockAppFragment();
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
        View root = inflater.inflate(R.layout.fragment_block_app, container, false);
        Button button = (Button)root.findViewById(R.id.blockButton);
        button.setOnClickListener(this);
        return root;
    }

    @Override
    public void onClick(View v) {
        final String packageName = "com.google.android.youtube";
        List<String> allPackages = getAppNames();
        if(allPackages.contains(packageName)){
            Toast.makeText(getActivity(), "Найдено приложение YouTube. НЕ ПОНИМАЮ. БЛОКИРУЮ!", Toast.LENGTH_LONG).show();
            getActivity().finish();
        }
    }

    private List<String> getAppNames(){
        List<String> appNames = new ArrayList<>();

        PackageManager packageManager = getActivity().getPackageManager();

        List<ApplicationInfo> packages = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);
        for(ApplicationInfo packageInfo : packages){
            appNames.add(packageInfo.packageName);
        }

        return appNames;
    }
}