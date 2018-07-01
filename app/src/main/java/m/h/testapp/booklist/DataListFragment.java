package m.h.testapp.booklist;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import m.h.testapp.R;
import m.h.testapp.booklist.response.Book;
import m.h.testapp.booklist.response.SuccesProfile;
import m.h.testapp.retrofit.BASE_URL;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class DataListFragment extends Fragment {

    public static DataListFragment newInstance() {
        return new DataListFragment();
    }
    RecyclerView recyclerView;
    String MAIN_URL="https://api.audiokitab.com/core/api/books/?page=1";
    String pagenext=null;
    DataListAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ProgressBar progressBar;
    boolean isLoading=false;
    int lasttotal=0;

    public DataListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_data_list, container, false);
        progressBar=v.findViewById(R.id.progresbar);
        recyclerView=(RecyclerView)v.findViewById(R.id.datalistrv);

        layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        postsRequestMethod(MAIN_URL);
        return v;
    }

    //data request method=-------------------------------------------------------------------------------

    private void postsRequestMethod(String page){

      progressBar.setVisibility(View.VISIBLE);
        Call<SuccesProfile> call = BASE_URL.retrofitServicesBooklist.booklist(page);
        Log.w("request l", call.request().toString());
        call.enqueue(new Callback<SuccesProfile>()
        {
            @Override
            public void onResponse(Call<SuccesProfile> call, Response<SuccesProfile> response) {
                Log.w("post id data","-----------------mpp-----------------------");
              progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {

                   SuccesProfile successProfile = response.body();

                    if (successProfile.getNext()!=null){
                        pagenext=successProfile.getNext().toString();
                    }
                    List<Book> books=successProfile.getBooks();
                    adapter=new DataListAdapter(getContext(), books
                            , new DataListAdapter.CustomItemClickListener() {
                        @Override
                        public void onItemClick(View v, int position,int postid) {
                            FragmentTransaction fragmentTransaction0=getFragmentManager()
                                    .beginTransaction();
                            fragmentTransaction0.replace(R.id.mainframe , DetailFragment.newInstance(postid) ,"datalist");
                            fragmentTransaction0.commit();
                        }
                    });

                    recyclerView.setAdapter(adapter);

                }else{
                    Toast.makeText(getContext(),"false",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<SuccesProfile> call, Throwable t) {

                Toast.makeText(getContext(),"onFail",Toast.LENGTH_SHORT).show();
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int lastvisibleitemposition =((LinearLayoutManager)recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                int totalitem=adapter.getItemCount();

                if (dy>0) {
                    if (isLoading && totalitem>lasttotal) {
                         lasttotal=totalitem;
                        isLoading = false;
                    }

                    if (!isLoading&&lastvisibleitemposition>totalitem-2 ) {
                       isLoading = true;
                       performPagination(pagenext);
                    }
                }
            }
        });


    }
    //------------------------------------------------------------------------------------------------------


    public  void performPagination(String page){
        progressBar.setVisibility(View.VISIBLE);
        Call<SuccesProfile> call = BASE_URL.retrofitServicesBooklist.booklist(page);
        Log.w("request l", call.request().toString());
        call.enqueue(new Callback<SuccesProfile>()
        {
            @Override
            public void onResponse(Call<SuccesProfile> call, Response<SuccesProfile> response) {
                Log.w("post id data","------------ppp----------------------------");
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful()) {

                    SuccesProfile successProfile = response.body();

                    if (successProfile.getNext()!=null){
                        pagenext=successProfile.getNext().toString();

                        List<Book> books=successProfile.getBooks();
                        adapter.addBook(books);
                        books.clear();
                    }else{
                        pagenext=null;
                    }

                }else{
                    Toast.makeText(getContext(),"false",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SuccesProfile> call, Throwable t) {

            }

        });
    }
}

