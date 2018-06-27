package m.h.testapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

import java.io.InputStream;
import java.net.URL;
import java.util.List;



public class DataListAdapter extends RecyclerView.Adapter<DataListAdapter.listViewHolder> {

    List<JSONObject> datas;
    Context context;
    CustomItemClickListener listeners;


    public DataListAdapter(Context context, List<JSONObject> data, CustomItemClickListener listener) {
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


    }

    //convert url image class method-------------------------------------------------------
    private class DownLoadImageTask extends AsyncTask<String,Void,Bitmap> {
        ImageView imageView;

        public DownLoadImageTask(ImageView imageView){
            this.imageView = imageView;
        }

        /*
            doInBackground(Params... params)
                Override this method to perform a computation on a background thread.
         */
        protected Bitmap doInBackground(String...urls){
            String urlOfImage = urls[0];
            Bitmap logo = null;
            try{
                InputStream is = new URL(urlOfImage).openStream();
                /*
                    decodeStream(InputStream is)
                        Decode an input stream into a bitmap.
                 */
                logo = BitmapFactory.decodeStream(is);
            }catch(Exception e){ // Catch the download exception
                e.printStackTrace();
            }
            return logo;
        }

        /*
            onPostExecute(Result result)
                Runs on the UI thread after doInBackground(Params...).
         */
        protected void onPostExecute(Bitmap result){
            imageView.setImageBitmap(result);
        }
    }

    //------------------------------------------------------------------------



    @Override
    public int getItemCount() {
        return datas.size();
    }


    public class listViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        int postidD;
        TextView location,title,tvtype;
        ImageView imgdata;

        public listViewHolder(View itemView) {
            super(itemView);

            location=itemView.findViewById(R.id.tvlocation);
            title=itemView.findViewById(R.id.tvtitle);
            imgdata=itemView.findViewById(R.id.imgdata);
            tvtype=itemView.findViewById(R.id.tvtype);
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

}
