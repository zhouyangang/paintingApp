package com.tumcca.api.db;

import com.tumcca.api.model.Users;
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
public class UsersMapper implements ResultSetMapper<Users> {
    @Override
    public Users map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        return new Users(resultSet.getLong("ID"), resultSet.getString("EMAIL"), resultSet.getString("MOBILE"), resultSet.getString("PASSWORD_HASH"));
    }
}
