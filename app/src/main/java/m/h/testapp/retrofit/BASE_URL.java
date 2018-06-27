package m.h.testapp.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class BASE_URL {

 public static String API = "http://kuzya.saffman.co.uk";


   public static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL.API)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

   public static RetrofitServices retrofitServices = retrofit.create(RetrofitServices.class);

}
