package m.h.testapp.booklist;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import m.h.testapp.R;
import m.h.testapp.booklist.response.Book;
import m.h.testapp.booklist.response.MainPhoto;


public class DataListAdapter extends RecyclerView.Adapter<DataListAdapter.listViewHolder> {

    List<Book> datas;
    Context context;
    CustomItemClickListener listeners;


    public DataListAdapter(Context context, List<Book> data, CustomItemClickListener listener) {
        this.datas = data;
        this.context=context;
        this.listeners=listener;
    }


    @Override
    public listViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_data, parent, false);
        final listViewHolder listViewHolder = new listViewHolder(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listeners.onItemClick(v, listViewHolder.getAdapterPosition(),listViewHolder.postidD);
            }
        });


        return new listViewHolder(v);
    }


    @Override
    public void onBindViewHolder(listViewHolder holder, int position) {

        Book item=datas.get(position);
        holder.postidD=item.getId();
        holder.tvbookname.setText(item.getName());
        holder.tvnarrators.setText(item.getAuthors().toString());

        MainPhoto mainPhoto=item.getMainPhoto();
        final String imgURL = mainPhoto.getThumbnail();
        Picasso.with(context).load(imgURL).into(holder.imgdata);
        holder.tvsize_str.setText(item.getSizeStr());
        holder.tvprice.setText(item.getPrice().toString()+" "+item.getPriceCurrency().toString());

    }


    @Override
    public int getItemCount() {
        return datas.size();
    }


    public class listViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        int postidD;
        TextView tvnarrators,tvbookname,tvsize_str,tvprice;
        ImageView imgdata;

        public listViewHolder(View itemView) {
            super(itemView);

            tvnarrators=itemView.findViewById(R.id.tvnarrators);
            tvbookname=itemView.findViewById(R.id.tvbookname);
            imgdata=itemView.findViewById(R.id.imgdata);
            tvsize_str=itemView.findViewById(R.id.tvsize_str);
            tvprice=itemView.findViewById(R.id.tvprice);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            listeners.onItemClick(view, this.getAdapterPosition(),postidD);
        }
    }

    public interface CustomItemClickListener {
        void onItemClick(View v, int position, int postid);
    }


    public void addBook(List<Book> books){
        Log.e("books---size",""+books.size());
        for (Book book:books){
            datas.add(book);
        }
        notifyDataSetChanged();

    }
}
