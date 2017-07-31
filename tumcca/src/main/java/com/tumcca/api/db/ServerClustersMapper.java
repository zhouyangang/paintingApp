package com.tumcca.api.db;

import com.tumcca.api.model.ServerClusters;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Title.
 * <p/>
 * Description.
 *
 * @author Bill Lv {@literal <billcc.lv@hotmail.com>}
 * @version 1.0
 * @since 2015-06-13
 */
public class ServerClustersMapper implements ResultSetMapper<ServerClusters> {
    @Override
    public ServerClusters map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        return new ServerClusters(resultSet.getString("SERVER_ID"), resultSet.getInt("STATUS"));
    }
}
