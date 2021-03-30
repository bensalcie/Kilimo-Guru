package org.is_great.bensalcie.agrihub;

import android.content.Context;
import android.content.SharedPreferences;

public class IntroManager {
    SharedPreferences prefs;
    SharedPreferences.Editor editor;
    Context context;

    public  IntroManager(Context context)
    {
        this.context=context;
        prefs=context.getSharedPreferences("first",0);
        editor=prefs.edit();
    }
    public void setFirst(boolean isFirst)
    {
        editor.putBoolean("check",isFirst);
        editor.commit();
    }
    public boolean Check()
    {
        return prefs.getBoolean("check",true);

    }

}
