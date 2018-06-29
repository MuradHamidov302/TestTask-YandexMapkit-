package m.h.testapp.booklist;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import m.h.testapp.R;
import m.h.testapp.booklist.response.SuccesProfile;
import m.h.testapp.retrofit.BASE_URL;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class DataListFragment extends Fragment {
    private ProgressDialog progressDialog;
    public static DataListFragment newInstance() {
        return new DataListFragment();
    }
    RecyclerView recyclerView;
    String MAIN_URL="https://api.audiokitab.com/core/api/books/?page=1";
    Button btnprev,btnnext;
    String pageprev=null;
    String pagenext=null;


    public DataListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_data_list, container, false);
        btnprev=v.findViewById(R.id.btnprev);
        btnnext=v.findViewById(R.id.btnnext);
        NextBtn();
        PrevBtn();
        postsRequestMethod(MAIN_URL);

        recyclerView=(RecyclerView)v.findViewById(R.id.datalistrv);

        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

    return v;
    }



    //data request method=-------------------------------------------------------------------------------

    private void postsRequestMethod(String page){

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Loading...");
        progressDialog.show();

        Call<SuccesProfile> call = BASE_URL.retrofitServicesBooklist.booklist(page);
        Log.w("request l", call.request().toString());
        call.enqueue(new Callback<SuccesProfile>()
        {
            @Override
            public void onResponse(Call<SuccesProfile> call, Response<SuccesProfile> response) {
                Log.w("post id data","----------------------------------------");
                progressDialog.dismiss();
                if (response.isSuccessful()) {

                   SuccesProfile successProfile = response.body();

                    if (successProfile.getNext()!=null){
                        pagenext=successProfile.getNext().toString();
                    }
                   if (successProfile.getPrevious()!=null){
                       pageprev=successProfile.getPrevious().toString();
                   }

                    recyclerView.setAdapter(new DataListAdapter(getContext(), successProfile.getBooks()
                            , new DataListAdapter.CustomItemClickListener() {
                        @Override
                        public void onItemClick(View v, int position,int postid) {
                                              }
                    }));

                }else{
                    Toast.makeText(getContext(),"false",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<SuccesProfile> call, Throwable t) {
                progressDialog.dismiss();
            }

        });


    }
    //------------------------------------------------------------------------------------------------------



    //click btnprev btnnext------------------------------------------------------
    void NextBtn(){
        btnnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pagenext!=null){
                    postsRequestMethod(pagenext);
                }else{
                    Toast.makeText(getContext(),"no page",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    void PrevBtn(){
        btnprev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (pageprev!=null){
                    postsRequestMethod(pageprev);
                }else{
                    Toast.makeText(getContext(),"no page",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
   //------------------------------------------------------------------


}
