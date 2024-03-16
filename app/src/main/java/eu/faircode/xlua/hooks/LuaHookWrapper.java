package eu.faircode.xlua.hooks;

import android.content.Context;

import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaClosure;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Prototype;
import org.luaj.vm2.Varargs;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;

import java.lang.reflect.Field;
import java.util.Map;

import de.robv.android.xposed.XC_MethodHook;
import eu.faircode.xlua.api.hook.XLuaHook;
import eu.faircode.xlua.XParam;

public class LuaHookWrapper {
    private static final String TAG = "XLua.LuaFieldHook";

    public final Globals globals;
    public final LuaClosure closure;
    public final LuaValue func;
    public final LuaValue[] args;

    public final boolean isMemberOrMethod;

    public LuaHookWrapper(
            Context context,
            XLuaHook hook,
            Map<String, String> settings,
            Map<String, Integer> propSettings,
            Prototype compiledScript,
            String function,
            XC_MethodHook.MethodHookParam param,
            Globals globals,
            String key) {

        isMemberOrMethod = true;
        this.globals = globals;
        closure = new LuaClosure(compiledScript, globals);
        closure.call();

        // Check if function exists
        func = globals.get(function);
        //if(!isValid()) return;
        //if (func.isnil())
        //    return;

        // Build arguments
        args = new LuaValue[]{
                CoerceJavaToLua.coerce(hook),
                //Create XPARAM here
                CoerceJavaToLua.coerce(new XParam(context, param, settings, propSettings, key))
        };
    }

    public LuaHookWrapper(Context context, XLuaHook hook, Map<String, String> settings, Map<String, Integer> propSettings, Prototype compiledScript, Field field, String key) {
        isMemberOrMethod = false;
        // Initialize Lua runtime
        globals = XHookUtil.getGlobals(context, hook, settings, propSettings, key);
        closure = new LuaClosure(compiledScript, globals);
        closure.call();

        func = globals.get("after");
        //if(!isValid()) return;

        args = new LuaValue[]{
                CoerceJavaToLua.coerce(hook),
                CoerceJavaToLua.coerce(new XParam(context, field, settings, propSettings, key))
        };
    }

    public static LuaHookWrapper createMember(Context context, XLuaHook hook, Map<String, String> settings, Map<String, Integer> propSettings, Prototype compiledScript, String function, XC_MethodHook.MethodHookParam param, Globals globals, String key) {
        return new LuaHookWrapper(context, hook, settings, propSettings, compiledScript, function, param, globals, key);
    }

    public static LuaHookWrapper createField(Context context, XLuaHook hook, Map<String, String> settings, Map<String, Integer> propSettings, Prototype compiledScript, Field field, String key) {
        return new LuaHookWrapper(context, hook, settings, propSettings, compiledScript, field, key);
    }

    public Varargs invoke() {
        if(!isValid()) return null;
        return func.invoke(args);
    }

    public boolean isValid() {
        return func != null && !func.isnil();
    }
}
