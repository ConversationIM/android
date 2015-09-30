package com.example.sam.conversationalim;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Sam on 9/29/2015.
 */
public class AboutFrag extends Fragment {


    public AboutFrag(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        return inflater.inflate(R.layout.about_frag, container, false);
    }


}
