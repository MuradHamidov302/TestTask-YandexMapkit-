package m.h.testapp.preference;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;


public class SharedPreferenceA {

    public SharedPreferences sp;
    public SharedPreferences.Editor editor;
    public static final String KEY_ID="00";
    public static final String USER_ID=null;
    public static final String LOGIN_STATUS="az.codeacademy.testsp_LOGIN_STATUS";
    public final String SHARE_NAME="az.codeacademy.testsp_SHARE_NAME";
    public final int MODE_PRIVERT=0;

    Context _context;


    public SharedPreferenceA(Context context) {
        this._context = context;
        sp=_context.getSharedPreferences(SHARE_NAME,MODE_PRIVERT);
        editor=sp.edit();
    }

    public void  storeData(String id, String userid){
        editor.putBoolean(LOGIN_STATUS,true);
        editor.putString(KEY_ID,id);
        editor.putString(USER_ID,userid);
        editor.commit();

    }

    public HashMap DetailStore(){
        HashMap<String, String> map=new HashMap<>();
        map.put(KEY_ID,sp.getString(KEY_ID,null));
        map.put(USER_ID,sp.getString(USER_ID,null));
        return map;

    }

    public boolean checkLogin(){
        if (!this.Login()){
//            Intent intent=new Intent(_context,LoginActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            _context.startActivity(intent);
            return true;
        }else{return false;}

    }

    public boolean Login(){
        return sp.getBoolean(LOGIN_STATUS,false);
    }

    public void logOut(){
        editor.clear();
        editor.commit();
    }
}
