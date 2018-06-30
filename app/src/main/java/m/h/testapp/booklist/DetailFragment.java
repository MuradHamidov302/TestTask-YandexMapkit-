package m.h.testapp.booklist;


import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import m.h.testapp.R;
import m.h.testapp.booklist.response.BookDetailRespose;
import m.h.testapp.booklist.response.BookDetailSub;
import m.h.testapp.booklist.response.MainPhoto;
import m.h.testapp.retrofit.BASE_URL;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {
    private static int itemid;
    TextView tvduration_strD,tvnarratorsD,tvbooknameD,tvannotationD;
    Button btnopenfileD;
    ImageView imgdetailD;
    private ProgressDialog progressDialog;




    public static DetailFragment newInstance(int id) {
        itemid=id;
        return new DetailFragment();
    }

    public DetailFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View v= inflater.inflate(R.layout.fragment_detail, container, false);

        tvduration_strD=v.findViewById(R.id.tvduration_strD);
        tvnarratorsD=v.findViewById(R.id.tvnarratorsD);
        tvbooknameD=v.findViewById(R.id.tvbooknameD);
        tvannotationD=v.findViewById(R.id.tvannotationD);
        btnopenfileD=v.findViewById(R.id.btnopenfileD);
        imgdetailD=v.findViewById(R.id.imgdetailD);


        DetailRequestMethod(itemid);
        return v;
    }




    //detail request method=-------------------------------------------------------------------------------

    private void DetailRequestMethod(int id){

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading...");
        progressDialog.show();


        Call<BookDetailRespose> call = BASE_URL.retrofitServicesBooklist.bookdetail(id);
        Log.w("request d", call.request().toString());

        call.enqueue(new Callback<BookDetailRespose>()
        {
            @Override
            public void onResponse(Call<BookDetailRespose> call, Response<BookDetailRespose> response) {
                if (response.isSuccessful()) {
                    progressDialog.dismiss();
                  final BookDetailRespose book = response.body();
                    Toast.makeText(getContext(),"true",Toast.LENGTH_SHORT).show();
                    tvduration_strD.setText(book.getDurationStr());
                   List<BookDetailSub> authors= (List<BookDetailSub>) book.getAuthors();
                   String authorsT="";
                   for (int i=0;i<authors.size();i++){
                       authorsT+=authors.get(i).getName()+"\n";
                   }
                    tvnarratorsD.setText(authorsT);
                    tvbooknameD.setText(book.getName());
                    tvannotationD.setText(book.getAnnotation());

                    MainPhoto mainPhoto= book.getMainPhoto();

                  final String imgURL = (mainPhoto.getFullSize());
                    Picasso.with(getContext()).load(imgURL).memoryPolicy(MemoryPolicy.NO_CACHE).into(imgdetailD);

                    btnopenfileD.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (book.getFile()!=null){

                                Intent browserIntent = new Intent(
                                        Intent.ACTION_VIEW,
                                        Uri.parse(book.getFile()));
                                startActivity(browserIntent);
                            }else{
                                Toast.makeText(getContext(),"No File",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else  {
                    Toast.makeText(getContext(),"false",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<BookDetailRespose> call, Throwable t) {
                Toast.makeText(getContext(),"onfail",Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }

        });


    }
    //------------------------------------------------------------------------------------------------------



}
