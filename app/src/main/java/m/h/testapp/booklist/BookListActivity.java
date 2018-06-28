package m.h.testapp.booklist;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import m.h.testapp.R;

public class BookListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_list);

        FragmentTransaction fragmentTransaction0=getSupportFragmentManager()
                .beginTransaction();
        fragmentTransaction0.replace(R.id.mainframe , DataListFragment.newInstance() ,"datalist");
        fragmentTransaction0.commit();
    }
}
