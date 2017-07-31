package com.tumcca.api.db;

import com.tumcca.api.model.Pictures;
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
public class PicturesMapper implements ResultSetMapper<Pictures> {
    @Override
    public Pictures map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        return new Pictures(resultSet.getLong("ID"), resultSet.getString("NAME"), resultSet.getString("BUCKET_NAME")
        		, resultSet.getString("OSS_KEY"), resultSet.getTimestamp("CREATE_TIME"), resultSet.getInt("STATUS"), resultSet.getInt("WIDTH"), resultSet.getInt("HEIGHT"));
    }
}
