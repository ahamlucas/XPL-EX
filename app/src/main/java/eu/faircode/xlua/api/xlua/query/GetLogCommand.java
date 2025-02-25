package eu.faircode.xlua.api.xlua.query;

import android.database.Cursor;
import android.os.Binder;
import android.os.Process;
import android.util.Log;

import java.util.Arrays;

import eu.faircode.xlua.XDatabase;
import eu.faircode.xlua.XUtil;
import eu.faircode.xlua.api.standard.QueryCommandHandler;
import eu.faircode.xlua.api.standard.command.QueryPacket;
import eu.faircode.xlua.api.hook.assignment.LuaAssignment;
import eu.faircode.xlua.api.standard.database.SqlQuerySnake;

public class GetLogCommand extends QueryCommandHandler {
    private static final String TAG = "XLua.GetLogCommand";

    @SuppressWarnings("unused")
    public GetLogCommand() {
        this.name = "getLog";
        this.requiresPermissionCheck = true;
        this.requiresSingleThread = true;
    }

    @Override
    public Cursor handle(QueryPacket commandData) throws Throwable {
        XDatabase db = commandData.getDatabase();

        int caller = Binder.getCallingUid();
        int userid = XUtil.getUserId(caller);
        int start = XUtil.getUserUid(userid, 0);
        int end = XUtil.getUserUid(userid, Process.LAST_APPLICATION_UID);

        Log.i(TAG, "retrieving all logs for caller id=" + caller + " userid=" + userid + " sql query start=" + start + " end=" + end);

        db.readLock();
        SqlQuerySnake snake = SqlQuerySnake
                .create(db, LuaAssignment.Table.name)
                .onlyReturnColumns("package", "uid", "hook", "used", "old", "new")
                .whereColumn("restricted", "1", "*")
                .whereColumn("uid", start, ">=")
                .whereColumn("uid", end, "<=")
                .orderBy("used DESC");

        Cursor c = snake.query();
        snake.clean(null);
        db.readUnlock();
        return c;
    }
}
