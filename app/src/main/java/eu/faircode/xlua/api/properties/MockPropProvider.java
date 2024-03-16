package eu.faircode.xlua.api.properties;

import android.content.Context;
import android.util.Log;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import eu.faircode.xlua.XDatabase;
import eu.faircode.xlua.api.XResult;
import eu.faircode.xlua.api.settings.LuaSettingDefault;
import eu.faircode.xlua.api.settings.LuaSettingsDatabase;
import eu.faircode.xlua.api.standard.database.DatabaseHelp;
import eu.faircode.xlua.api.standard.database.SqlQuerySnake;
import eu.faircode.xlua.utilities.CollectionUtil;
import eu.faircode.xlua.utilities.MockUtils;

public class MockPropProvider {
    private static String TAG = "XLua.MockPropProvider";

    //KEY=(Property Name) VALUE=Setting it points too
    private static HashMap<String, MockPropMap> mappedProperties = new HashMap<>();
    private static final Object lock = new Object();

    /*public static String getPropertyValue(Context context, XDatabase db, String propertyName,  int user, String packageName) {
        MockPropMap map = mappedProperties.get(propertyName);
        if(map == null)
            return MockUtils.NOT_BLACKLISTED;

        String sName = map.getSettingName();
        String value = LuaSettingsDatabase.getSettingValue(context, db, sName, user, packageName);
        //if(value == null)
        //    value = XMockSettingsProvider.getDefaultSettingValue(context, db, sName);
        //fix

        if(value == null)
            return MockUtils.NOT_BLACKLISTED;

        return value;
    }*/

    public static XResult putMockPropMap(Context context, XDatabase db, MockPropPacket packet) {
        initCache(context, db);
        XResult res = MockPropDatabase.putSettingMapForProperty(db, packet);
        if(res.succeeded() && !packet.isDeleteMap()) { synchronized (lock) { mappedProperties.put(packet.getName(), packet.createMap()); } }
        else if(res.succeeded()) { synchronized (lock) { mappedProperties.remove(packet.getName()); } }
        Log.i(TAG, "mock prop map insert result=" + res.getMessage());
        return res;
    }

    public static Collection<MockPropSetting> getSettingsForPackage(Context context, XDatabase db, int user, String packageName, boolean getAll) { return getSettingsForPackage(context, db, MockPropPacket.createQueryRequest(user, packageName, getAll));  }
    public static Collection<MockPropSetting> getSettingsForPackage(Context context, XDatabase db, MockPropPacket packet) {
        Log.i(TAG, "Entering [getSettingsForPackage] packet=" + packet);
        int user = packet.getUser();
        String packageName = packet.getCategory();
        boolean getAll = packet.isGetAll();

        Log.i(TAG, "[getSettingsForPackage] db=" + db.getName() + " user=" + user + " pkg=" + packageName + " all=" + getAll);
        initCache(context, db);
        Collection<MockPropSetting> userSettings = MockPropDatabase.getPropertySettingsForUser(db, user, packageName);
        Log.i(TAG, "user settings=" + userSettings.size());
        if(!getAll)
            return userSettings;

        HashMap<String, MockPropSetting> users = new HashMap<>(userSettings.size());
        if(!CollectionUtil.isValid(userSettings)) {
            for(MockPropSetting s : userSettings)
                users.put(s.getName(), s);
        }

        Log.i(TAG, "user settings (2) =" + users.size() + " mapped properties=" + mappedProperties.size());

        synchronized (lock) {
            for(Map.Entry<String, MockPropMap> e : mappedProperties.entrySet()) {
                String k = e.getKey();
                MockPropMap m = e.getValue();
                if(!users.containsKey(k)) {
                    MockPropSetting mSetting = MockPropSetting.create(user, packageName,  k, m.getSettingName(), MockPropSetting.PROP_NULL);
                    users.put(k, mSetting);
                }
            }
        }

        Log.i(TAG, "total user settings=" + users.size());
        return users.values();
    }

    public static void initCache(Context context, XDatabase db) {
        if (!CollectionUtil.isValid(mappedProperties)) {
            synchronized (lock) {
                if (!CollectionUtil.isValid(mappedProperties)) {
                    //Still null even after lock que
                    HashMap<String, MockPropMap> maps = new HashMap<>();
                    Collection<MockPropMap> settings = MockPropDatabase.forceCheckMapsDatabase(context, db);
                    for (MockPropMap set : settings)
                        maps.put(set.getName(), set);

                    mappedProperties = maps;
                    Log.i(TAG, "mapped settings =" + maps.size());
                }
            }
        }
    }
}