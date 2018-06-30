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
import android.widget.Button;
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
    Button btnnext;
    String pagenext=null;
    DataListAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ProgressBar progressBar;

    public DataListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_data_list, container, false);
        progressBar=v.findViewById(R.id.progresbar);
        btnnext=v.findViewById(R.id.btnnext);
        NextBtn();
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
                    performPagination(pagenext);
                }else{
                    Toast.makeText(getContext(),"no page",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    //------------------------------------------------------------------



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
//
//
//package com.onbranch.onbranchandroidapp.activities;
//
//        import android.content.Intent;
//        import android.os.Bundle;
//        import android.support.v4.widget.SwipeRefreshLayout;
//        import android.support.v7.widget.LinearLayoutManager;
//        import android.support.v7.widget.RecyclerView;
//        import android.view.View;
//        import android.widget.TextView;
//        import android.widget.Toast;
//
//        import com.crashlytics.android.Crashlytics;
//        import com.google.gson.Gson;
//        import com.google.gson.JsonArray;
//        import com.google.gson.JsonObject;
//        import com.google.gson.JsonParser;
//        import com.google.gson.reflect.TypeToken;
//        import com.loopj.android.http.AsyncHttpClient;
//        import com.loopj.android.http.AsyncHttpResponseHandler;
//        import com.loopj.android.http.RequestParams;
//        import com.onbranch.onbranchandroidapp.R;
//        import com.onbranch.onbranchandroidapp.adapters.UserRVAdapter;
//        import com.onbranch.onbranchandroidapp.models.Paging;
//        import com.onbranch.onbranchandroidapp.models.Profile;
//        import com.onbranch.onbranchandroidapp.utils.Auth;
//        import com.onbranch.onbranchandroidapp.utils.BaseActivity;
//        import com.onbranch.onbranchandroidapp.utils.CommonMethods;
//        import com.onbranch.onbranchandroidapp.utils.DebugToast;
//
//        import java.util.ArrayList;
//
//        import cz.msebera.android.httpclient.Header;
//
//public class UsersActivity extends BaseActivity {
//
//    private RecyclerView mRvUsers;
//    private UserRVAdapter mAdapter;
//    private int mSearchId;
//    private String mSearchPrefix;
//    private String mSearchSuffix;
//    private AsyncHttpClient mClient;
//    private JsonParser mParser;
//    private Gson mGson;
//    private ArrayList<Profile> mUsers;
//    private SwipeRefreshLayout mSwipeRefreshLayout;
//    private TextView mToolbarTitle;
//    private Paging mPaging;
//    private String mQuery;
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        if (mClient != null)
//            mClient.cancelAllRequests(true);
//    }
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_users);
//
//        initVars();
//        getUsers();
//    }
//
//    private void initVars() {
//        //TODO: need update to integers
//        String type = getIntent().getStringExtra("TYPE");
//        String usersType = getIntent().getStringExtra("USERS_TYPE");
//
//        findViewById(R.id.users_back_ic).setOnClickListener(view -> finish());
//        mToolbarTitle = findViewById(R.id.users_toolbar_title);
//
//        //get data mUrlStr
//        mClient = new AsyncHttpClient(true, 80, 443);
//        mGson = new Gson();
//        mParser = new JsonParser();
//        mPaging = new Paging();
//
//        mRvUsers = findViewById(R.id.rvUsers);
//
//        mUsers = new ArrayList<>();
//        mAdapter = new UserRVAdapter(getContext(), mUsers, true);
//        mAdapter.setOnItemClickListener(new UserRVAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(int position) {
//                Intent intent = new Intent(getContext(), UserProfileActivity.class);
//                intent.putExtra("USER_ID", mUsers.get(position).getUserid());
//                startActivity(intent);
//            }
//
//            @Override
//            public void onItemLongClick(int position) {
//
//            }
//
//            @Override
//            public void onFollowClick(int position) {
//                onFollow(position);
//            }
//        });
//
//        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
//        RecyclerView.OnScrollListener RVScrollListener = new RecyclerView.OnScrollListener() {
//            @Override
//            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
//                super.onScrollStateChanged(recyclerView, newState);
//            }
//
//            @Override
//            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
//                super.onScrolled(recyclerView, dx, dy);
//                int lastvisibleitemposition = layoutManager.findLastVisibleItemPosition();
//
//                if (!mSwipeRefreshLayout.isRefreshing() && mPaging.getNext() != -1) {
//                    if (lastvisibleitemposition == mAdapter.getItemCount() - 2) {
//                        getUsers();
//                    }
//                }
//            }
//        };
//
//        mRvUsers.setLayoutManager(layoutManager);
//        mRvUsers.addOnScrollListener(RVScrollListener);
//        mRvUsers.setAdapter(mAdapter);
//
//        mSwipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
//        mSwipeRefreshLayout.setColorSchemeColors(CommonMethods.getColor(getContext(), R.color.customNormalBlue));
//        mSwipeRefreshLayout.setOnRefreshListener(() -> {
//            mSwipeRefreshLayout.setRefreshing(true);
//            mPaging = new Paging();
//            getUsers();
//        });
//
//        switch (type) {
//            case "POST":
//                mSearchId = getIntent().getIntExtra("POST_ID", 0);
//                mSearchPrefix = "posts";
//                break;
//            case "COMMENT":
//                mSearchId = getIntent().getIntExtra("BRANCH_ID", 0);
//                mSearchPrefix = "branch";
//                break;
//            case "RECENT":
//                mQuery = "new_users";
//                mToolbarTitle.setText(R.string.recent_visitors);
//                break;
//            case "MEMBERS":
//                mSearchId = getIntent().getIntExtra("BRANCH_ID", 0);
//                mSearchPrefix = "branch";
//                break;
//            case "STATISTICS":
//                mSearchId = getIntent().getIntExtra("BRANCH_ID", 0);
//                mSearchPrefix = "branch";
//                break;
//        }
//
//        //TODO: update with constants
//        if (usersType != null)
//            switch (usersType) {
//                case "Likes":
//                    mSearchSuffix = "/likers";
//                    mToolbarTitle.setText(R.string.likes);
//                    mQuery = mSearchPrefix + "/" + mSearchId + mSearchSuffix;
//                    break;
//                case "Commenters":
//                    mSearchSuffix = "/commenters";
//                    mToolbarTitle.setText(R.string.group_chat_members);
//                    mQuery = mSearchPrefix + "/" + mSearchId + mSearchSuffix;
//                    break;
//                case "Members":
//                    mSearchSuffix = "/members";
//                    mToolbarTitle.setText(R.string.members);
//                    mQuery = mSearchPrefix + "/" + mSearchId + mSearchSuffix;
//                    break;
//                case "Visitors":
//                    int secondBranchId = getIntent().getIntExtra("SECOND_BRANCH_ID", 0);
//                    mSearchSuffix = "/discounted-places/" + secondBranchId + "/visitors";
//                    mToolbarTitle.setText(R.string.visitors);
//                    mQuery = mSearchPrefix + "/" + mSearchId + mSearchSuffix;
//                    break;
//                default:
//                    break;
//            }
//    }
//
//    private void getUsers() {
//        mSwipeRefreshLayout.setRefreshing(true);
//
//        String urlStr = getString(R.string.api_server_v2) + mQuery + "?m3token=" + Auth.getM3token(getContext()) + "&page=" + mPaging.getNext();
//
//        CommonMethods.print(urlStr);
//        mClient.get(urlStr, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                try {
//                    JsonObject response = mParser.parse(new String(responseBody)).getAsJsonObject();
//                    CommonMethods.checkPing(getContext(), response);
//
//                    CommonMethods.print(new String(responseBody));
//
//                    if (response.get("status").getAsInt() == 1) {
//                        JsonArray data = response.get("data").getAsJsonArray();
//                        JsonObject paging = response.get("pagination").getAsJsonObject();
//
//                        if (mPaging.getNext() == 0)
//                            mUsers.clear();
//
//                        mPaging = mGson.fromJson(paging, Paging.class);
//
//                        ArrayList<Profile> tempUsers = mGson.fromJson(data, new TypeToken<ArrayList<Profile>>() {
//                        }.getType());
//
//                        mUsers.addAll(tempUsers);
//                        mAdapter.notifyDataSetChanged();
//
//                        if (mUsers.isEmpty())
//                            showEmpty(true);
//                        else
//                            showEmpty(false);
//
//                    } else {
//                        JsonObject errorObj = response.get("error").getAsJsonObject();
//                        CommonMethods.checkError(getAppCompatActivity(), response.get("status").getAsInt(), errorObj.get("msg").getAsString());
//                        showEmpty(true);
//                    }
//                } catch (Exception ignored) {
//                    ignored.printStackTrace();
//                    showEmpty(true);
//                }
//
//
//                mSwipeRefreshLayout.setRefreshing(false);
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers,
//                                  byte[] responseBody, Throwable error) {
//                error.printStackTrace();
//                Crashlytics.log(error.getMessage());
//                DebugToast.makeText(getContext(), R.string.error_occurred, Toast.LENGTH_LONG).show();
//                mSwipeRefreshLayout.setRefreshing(false);
//                showEmpty(true);
//                findViewById(R.id.empty_lay_retry).setVisibility(View.VISIBLE);
//                ((TextView) findViewById(R.id.empty_lay_title)).setText(getString(R.string.no_internet));
//                ((TextView) findViewById(R.id.empty_lay_subtitle)).setText(getString(R.string.check_connection));
//            }
//        });
//    }
//
//    private void showEmpty(boolean show) {
//        if (show) {
//            findViewById(R.id.empty_lay).setVisibility(View.VISIBLE);
//            mRvUsers.setVisibility(View.GONE);
//
//            findViewById(R.id.empty_lay_retry).setOnClickListener(view -> {
//                mSwipeRefreshLayout.setRefreshing(true);
//                getUsers();
//            });
//        } else {
//            findViewById(R.id.empty_lay).setVisibility(View.GONE);
//            mRvUsers.setVisibility(View.VISIBLE);
//        }
//    }
//
//    public void onFollow(final int position) {
//        Profile user = mUsers.get(position);
//        String followType;
//
//        if (user.getI_follow() != 0) {
//            followType = "unfollow";
//
//            String url = getContext().getString(R.string.api_server_v2) + "user/" + followType + "/" + String.valueOf(user.getUserid());
//
//            RequestParams params = new RequestParams();
//            params.put("m3token", Auth.getM3token(getContext()));
//
//            CommonMethods.print(url);
//
//            mClient.delete(url, params, new AsyncHttpResponseHandler() {
//                @Override
//                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                    try {
//                        JsonParser parser = new JsonParser();
//                        JsonObject response = parser.parse(new String(responseBody)).getAsJsonObject();
//
//                        if (response.get("status").getAsInt() == 1) {
//                            JsonObject data = response.get("data").getAsJsonObject();
//
//                            mUsers.get(position).setI_follow(data.get("i_follow").getAsInt());
//                            mAdapter.notifyDataSetChanged();
//                        } else {
//                            JsonObject errorObj = response.get("error").getAsJsonObject();
//                            CommonMethods.checkError(getAppCompatActivity(), response.get("status").getAsInt(), errorObj.get("msg").getAsString());
//                        }
//                    } catch (Exception ignored) {
//                        ignored.printStackTrace();
//                        Crashlytics.log(ignored.getMessage());
//                    }
//                }
//
//                @Override
//                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                    error.printStackTrace();
//                    Crashlytics.log(error.getMessage());
//                    DebugToast.makeText(getContext(), getString(R.string.error_occurred), Toast.LENGTH_LONG).show();
//                }
//            });
//        } else {
//            followType = "follow";
//
//            String url = getContext().getString(R.string.api_server_v2) + "user/" + followType + "/" + String.valueOf(user.getUserid());
//
//            RequestParams params = new RequestParams();
//            params.put("m3token", Auth.getM3token(getContext()));
//
//            CommonMethods.print(url);
//
//            mClient.post(url, params, new AsyncHttpResponseHandler() {
//                @Override
//                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                    try {
//                        JsonParser parser = new JsonParser();
//                        JsonObject response = parser.parse(new String(responseBody)).getAsJsonObject();
//
//                        if (response.get("status").getAsInt() == 1) {
//                            JsonObject data = response.get("data").getAsJsonObject();
//
//                            mUsers.get(position).setI_follow(data.get("i_follow").getAsInt());
//                            mAdapter.notifyDataSetChanged();
//
//                            if (mUsers.get(position).getI_follow() == 1) {
//                                Toast.makeText(getContext(), getString(R.string.thank_you), Toast.LENGTH_SHORT).show();
//                            } else {
//                                Toast.makeText(getContext(), getString(R.string.follow_request_sent_user), Toast.LENGTH_SHORT).show();
//                            }
//                        } else {
//                            JsonObject errorObj = response.get("error").getAsJsonObject();
//                            CommonMethods.checkError(getAppCompatActivity(), response.get("status").getAsInt(), errorObj.get("msg").getAsString());
//                        }
//                    } catch (Exception ignored) {
//                        ignored.printStackTrace();
//                        Crashlytics.log(ignored.getMessage());
//                        DebugToast.makeText(getContext(), getString(R.string.error_occurred), Toast.LENGTH_LONG).show();
//                    }
//
//                }
//
//                @Override
//                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                    error.printStackTrace();
//                    Crashlytics.log(error.getMessage());
//                    DebugToast.makeText(getContext(), getString(R.string.error_occurred), Toast.LENGTH_LONG).show();
//                }
//            });
//        }
//    }
//}
