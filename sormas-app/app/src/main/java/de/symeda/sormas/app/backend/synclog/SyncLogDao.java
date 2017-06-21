package de.symeda.sormas.app.backend.synclog;

import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Stack;

/**
 * Some methods are copied from {@link com.j256.ormlite.dao.RuntimeExceptionDao}.
 *
 * Created by Mate Strysewske on 24.05.2017.
 */
public class SyncLogDao {

    private Dao<SyncLog, Long> dao;

    public SyncLogDao(Dao<SyncLog, Long> innerDao) throws SQLException {
        this.dao = innerDao;
    }

    private final Stack<String> parentEntityNameStack = new Stack<>();
    private String parentEntityNames = null;

    public void pushParentEntityName(String parentEntityName) {
        if (parentEntityName == null || parentEntityName.isEmpty())
            throw new IllegalArgumentException("parentEntityName is null or empty");

        parentEntityNameStack.push(parentEntityName);
        parentEntityNames = null;
    }

    public String popParentEntityName() {
        String result = parentEntityNameStack.pop();
        parentEntityNames = null;
        return result;
    }

    public SyncLog createWithParentStack(String entityName, String conflictText) {

        if (!parentEntityNameStack.empty()) {
            if (parentEntityNames == null) {
                StringBuilder nameBuilder = new StringBuilder();
                for (String parentEntityName : parentEntityNameStack) {
                    nameBuilder.append(parentEntityName).append(", ");
                }
                parentEntityNames = nameBuilder.toString();
            }
            entityName = parentEntityNames + entityName;
        }

        SyncLog syncLog = new SyncLog(entityName, conflictText);
        syncLog.setCreationDate(new Date());
        create(syncLog);

        return syncLog;
    }

    public List<SyncLog> queryForAll(String orderBy, boolean ascending) {
        try {
            QueryBuilder builder = queryBuilder();
            return builder.orderBy(orderBy, ascending).query();
        } catch (SQLException | IllegalArgumentException e) {
            Log.e(getClass().getName(), "Could not perform queryForAll");
            throw new RuntimeException();
        }
    }

    /**
     * @see Dao#create(Object)
     */
    public int create(SyncLog data) {
        try {
            return dao.create(data);
        } catch (SQLException e) {
            Log.e(getClass().getName(), "create threw exception on: " + data, e);
            throw new RuntimeException(e);
        }
    }

    /**
     * @see Dao#queryBuilder()
     */
    public QueryBuilder<SyncLog, Long> queryBuilder() {
        return dao.queryBuilder();
    }

}
