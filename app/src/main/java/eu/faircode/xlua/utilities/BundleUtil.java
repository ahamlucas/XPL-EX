package eu.faircode.xlua.utilities;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import eu.faircode.xlua.api.standard.interfaces.ISerial;

public class BundleUtil {

    public static Bundle combineBundles(Bundle a, Bundle b) {
        //Bundle main = new Bundle();
        //for( a.keySet())
        return null;
    }

    public static Boolean readBoolean(Bundle b, String keyName) { return readBoolean(b, keyName, null); }
    public static Boolean readBoolean(Bundle b, String keyName,  Boolean defaultValue) {
        if(b == null || !b.containsKey(keyName)) return defaultValue;
        return b.getBoolean(keyName);
    }

    public static <T extends ISerial> Bundle createFromISerial(T obj, boolean returnFalseIfNull) {
        if(obj == null)
            return returnFalseIfNull ? createResultStatus(false) : new Bundle();

        return obj.toBundle();
    }

    public static Bundle createFromStringArray(String keyName, List<String> lst) { return createFromStringArray(keyName, lst.toArray(new String[0])); }
    public static Bundle createFromStringArray(String keyName, String[] arr) {
        Bundle b = new Bundle();
        b.putStringArray(keyName, arr);
        return b;
    }

    public static Bundle createSingleLong(String keyName, long value) {
        Bundle b = new Bundle();
        b.putLong(keyName, value);
        return b;
    }

    public static Bundle createSingleInt(String keyName, int value) {
        Bundle b = new Bundle();
        b.putInt(keyName, value);
        return b;
    }

    public static Bundle createSingleString(String keyName, String value) { return createSingleString(keyName, value, true); }
    public static Bundle createSingleString(String keyName, String value, boolean ignoreNullValue) {
        Bundle b = new Bundle();
        if(!ignoreNullValue && value == null) b.putString(keyName, "null");
        else b.putString(keyName, value);
        return b;
    }

    public static String[] readStringArray(Bundle bundle, String keyName) {
        if(bundle == null || !bundle.containsKey(keyName))
            return new String[] { };

        return bundle.getStringArray(keyName);
    }


    public static List<String> readStringList(Bundle bundle, String keyName) { return readStringList(bundle, keyName, false); }
    public static List<String> readStringList(Bundle bundle, String keyName, boolean fromArray) {
        List<String> elements = new ArrayList<>();
        if(bundle == null || !bundle.containsKey(keyName))
            return elements;

        if(fromArray) {
            String[] arr = bundle.getStringArray(keyName);
            if(arr != null) Collections.addAll(elements, arr);
        }else {
            List<String> copy = bundle.getStringArrayList(keyName);
            if(copy != null) return copy;
        }

        return elements;
    }

    public static boolean readResultStatus(Bundle bundle) {
        if(bundle == null) return false;
        //FIXC THIS

        if(!bundle.containsKey("result"))
            return false;

        String res = bundle.getString("result");
        if(res == null || res.isEmpty())
            return false;

        if(res.equals("-1"))
            return false;

        if(res.equals("0"))
            return true;

        if(res.equalsIgnoreCase("false"))
            return false;

        if(res.equalsIgnoreCase("true"))
            return true;

        return false;
    }

    public static String readResultStatusMessage(Bundle bundle) {
        if(bundle == null) return "Action Executed but returned NULL (assume it went well :P )";
        StringBuilder sb = new StringBuilder();

        String res = null;
        if(bundle.containsKey("result")) {
            res = String.valueOf(bundle.get("result"));
        }

        if(res == null || res.isEmpty()) {
            sb.append("Action Executed but returned NULL (assume it went well :P )");
        }else {
            if(res.equals("-1"))
                sb.append("Action Failed to execute! (-1)");
            else if(res.equals("0"))
                sb.append("Action Executed Successfully! (0)");
            else if(res.equalsIgnoreCase("false"))
                sb.append("Action Failed to execute!");
            else if(res.equalsIgnoreCase("true"))
                sb.append("Action Executed Successfully!");
        }

        if(bundle.containsKey("message")) {
            String bMessage = bundle.getString("message");
            if(bMessage != null && !bMessage.isEmpty()) {
                sb.append(" Message: ");
                sb.append(bMessage);
            }
        }

        return sb.toString();
    }

    public static String readString(Bundle bundle, String keyName) { return readString(bundle, keyName, null); }
    public static String readString(Bundle bundle, String keyName, String defaultValue) {
        if(bundle == null || !bundle.containsKey(keyName)) return defaultValue;
        return bundle.getString(keyName, defaultValue);
    }

    //get rid of this -5 constraint
    public static int readInteger(Bundle bundle, String keyName) { return readInteger(bundle, keyName, -5); }
    public static int readInteger(Bundle bundle, String keyName, Integer defaultValue) {
        if(bundle == null || !bundle.containsKey(keyName)) return defaultValue;
        if(defaultValue == null) return bundle.getInt(keyName);
        return bundle.getInt(keyName, defaultValue);
    }

    public static Bundle createResultStatus(boolean result) {
        Bundle b = new Bundle();
        b.putInt("result", result ? 0 : -1);
        return b;
    }
}
