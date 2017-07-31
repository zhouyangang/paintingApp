package com.tumcca.api.db;

import com.tumcca.api.model.PicturesCache;
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
 * @since 2015-06-12
 */
public class PicturesCacheMapper implements ResultSetMapper<PicturesCache> {
    @Override
    public PicturesCache map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        return new PicturesCache(resultSet.getString("URI"), resultSet.getString("STORAGE_NAME"), resultSet.getTimestamp("CREATE_TIME"));
    }
}
