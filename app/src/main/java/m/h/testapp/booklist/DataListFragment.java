package m.h.testapp.booklist;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import m.h.testapp.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class DataListFragment extends Fragment {
    private ProgressDialog progressDialog;
    public static DataListFragment newInstance() {
        return new DataListFragment();
    }
    RecyclerView recyclerView;


    public DataListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View v= inflater.inflate(R.layout.fragment_data_list, container, false);


       recyclerView=(RecyclerView)v.findViewById(R.id.datalistrv);

        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

    return v;
    }

}