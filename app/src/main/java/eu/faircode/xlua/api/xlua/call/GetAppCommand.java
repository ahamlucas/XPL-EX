package eu.faircode.xlua.api.xlua.call;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import eu.faircode.xlua.api.XProxyContent;
import eu.faircode.xlua.api.app.AppPacket;
import eu.faircode.xlua.api.standard.CallCommandHandler;
import eu.faircode.xlua.api.standard.command.CallPacket;
import eu.faircode.xlua.api.xlua.provider.XLuaAppProvider;

public class GetAppCommand extends CallCommandHandler {
    @SuppressWarnings("unused")
    public GetAppCommand() {
        name = "getApp";
        requiresPermissionCheck = true;
    }

    @Override
    public Bundle handle(CallPacket commandData) throws Throwable {
        Log.i("XLua.GetAppCommand", "Got Call");
        AppPacket packet = commandData.readExtrasAs(AppPacket.class);
        if(packet == null)
            throw new Exception("App Packet was NULL... [getApp]");

        Log.i("XLua.GetAppCommand", "Got Call packet=" + packet.toString());

        return XLuaAppProvider.getApp(
                commandData.getContext(),
                commandData.getDatabase(),
                packet).toBundle();
    }

    public static Bundle invoke(Context context, AppPacket packet) {
        return XProxyContent.luaCall(
                context,
                "getApp",
                packet.toBundle());
    }
}
