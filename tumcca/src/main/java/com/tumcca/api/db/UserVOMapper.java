package com.tumcca.api.db;

import com.tumcca.api.model.UserVO;
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
public class UserVOMapper implements ResultSetMapper<UserVO> {
    @Override
    public UserVO map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        return new UserVO(resultSet.getString("EMAIL"), resultSet.getString("MOBILE"), resultSet.getString("AUTHORITY"));
    }
}
