package eu.faircode.xlua.api.xmock.query;

import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import eu.faircode.xlua.api.XProxyContent;
import eu.faircode.xlua.api.configs.MockConfigDatabase;
import eu.faircode.xlua.api.standard.QueryCommandHandler;
import eu.faircode.xlua.api.standard.command.QueryPacket;
import eu.faircode.xlua.utilities.CursorUtil;

public class GetMockConfigsCommand extends QueryCommandHandler {
    public static GetMockConfigsCommand create(boolean marshall) { return new GetMockConfigsCommand(marshall); };

    @SuppressWarnings("unused")
    public GetMockConfigsCommand() { this(false); }
    public GetMockConfigsCommand(boolean marshall) {
        this.marshall = marshall;
        this.name = marshall ? "getMockConfigs2" : "getMockConfigs";
        this.requiresPermissionCheck = false;
    }

    @Override
    public Cursor handle(QueryPacket commandData) throws Throwable {
        throwOnPermissionCheck(commandData.getContext());
        return CursorUtil.toMatrixCursor(
                MockConfigDatabase.getMockConfigs(commandData.getContext(), commandData.getDatabase()),
                marshall, 0);
    }

    public static Cursor invoke(Context context, boolean marshall) {
        return XProxyContent.mockQuery(
                context,
                marshall ? "getMockConfigs2" : "getMockConfigs");
    }
}