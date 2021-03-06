package com.tumcca.api.db;

import com.tumcca.api.model.Sessions;
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
 * @since 2015-06-03
 */
public class SessionsMapper implements ResultSetMapper<Sessions> {
    @Override
    public Sessions map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        return new Sessions(resultSet.getLong("UID"), resultSet.getTimestamp("SIGN_IN_TIME"), resultSet.getInt("STATUS"));
    }
}
