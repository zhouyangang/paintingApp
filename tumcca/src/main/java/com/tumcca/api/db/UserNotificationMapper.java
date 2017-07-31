package com.tumcca.api.db;

import com.tumcca.api.model.UserNotifications;
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
 * @since 2015-06-22
 */
public class UserNotificationMapper implements ResultSetMapper<UserNotifications> {
    @Override
    public UserNotifications map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        return new UserNotifications(resultSet.getLong("ID"), resultSet.getString("ACTION"), resultSet.getString("MESSAGE"), resultSet.getTimestamp("CREATE_TIME"));
    }
}
