package com.beone.flagggaming.navbar;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.beone.flagggaming.R;

public class HomeFragment extends Fragment {

        @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        //AGREGO COMPORTAMIENTOS DEL FRGMENT

           // Toast.makeText(getContext(),"PROBANDO",Toast.LENGTH_SHORT).show();
        return root;
    }

}