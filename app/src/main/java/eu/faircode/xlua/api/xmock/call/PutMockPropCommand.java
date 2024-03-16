package eu.faircode.xlua.api.xmock.call;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import eu.faircode.xlua.api.XProxyContent;
import eu.faircode.xlua.api.XResult;
import eu.faircode.xlua.api.properties.MockPropDatabase;
import eu.faircode.xlua.api.properties.MockPropPacket;
import eu.faircode.xlua.api.properties.MockPropProvider;
import eu.faircode.xlua.api.settings.LuaSettingPacket;
import eu.faircode.xlua.api.settings.LuaSettingsDatabase;
import eu.faircode.xlua.api.standard.CallCommandHandler;
import eu.faircode.xlua.api.standard.UserIdentityPacket;
import eu.faircode.xlua.api.standard.command.CallPacket;

public class PutMockPropCommand extends CallCommandHandler {
    //This will register with the LUA Database not the MOCK Database
    public PutMockPropCommand() {
        name = "putMockProp";
        requiresPermissionCheck = true;
    }

    @Override
    public Bundle handle(CallPacket commandData) throws Throwable {
        throwOnPermissionCheck(commandData.getContext());
        MockPropPacket packet = commandData.read(MockPropPacket.class);
        if(packet == null)
            return XResult.create().setMethodName("putMockProp").setFailed("Mock Prop packet is NULL").toBundle();

        packet.resolveUserID();
        packet.ensureCode(UserIdentityPacket.CODE_NULL_EMPTY);
        Log.i("XLua.PutMockPropCommand", "Prop packet=" + packet);

        switch (packet.getCode()) {
            case MockPropPacket.CODE_DELETE_PROP_MAP:
            case  MockPropPacket.CODE_INSERT_UPDATE_PROP_MAP:
                return MockPropProvider.putMockPropMap(commandData.getContext(), commandData.getDatabase(), packet).toBundle();
            case  MockPropPacket.CODE_DELETE_PROP_SETTING:
            case MockPropPacket.CODE_INSERT_UPDATE_PROP_SETTING:
                return MockPropDatabase.putPropertySetting(commandData.getDatabase(), packet).toBundle();
        }

        return XResult.create().setMethodName("putMockProp")
                .setExtra(packet.toString())
                .setFailed("Cannot find Correct Command Code Handler! code=" + packet.getCode()).toBundle();
    }

    public static Bundle invoke(Context context, MockPropPacket packet) {
        return XProxyContent.mockCall(
                context,
                "putMockProp",
                packet.toBundle());
    }
}