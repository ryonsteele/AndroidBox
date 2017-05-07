package android.ryons.com.bwtiffimaging;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import java.util.List;

public class Util {

    private static String TAG;

    public static String getTag(Context context) {

        try {
            context = context.getApplicationContext();
        } catch(Exception ex) {
            // do nothing.
        }

        if (Util.isEmptyString(TAG) && null!=context) {
            TAG = context.getResources().getString(R.string.tag);
            return TAG;
        }

        return getTag(); // failsafe in case there is no context at all.
    }

    /* This method is purely a failsafe in case any places that called getTag() were not replaced
     * with the call passing a context in. Not using the context results in a resource not found
     * exception.
     */
    public static String getTag() {
        return "BWTiffImaging";
    }


    public static boolean isEmptyString(String testString){
        if(testString == null || testString.equals("") || testString.equals(" ")) return true;
        else return false;
    }

    public static boolean isIntentAvailable(Context context, String action) {
        final PackageManager packageManager = context.getPackageManager();
        final Intent intent = new Intent(action);
        List<ResolveInfo> list =
                packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return list.size() > 0;
    }


}
