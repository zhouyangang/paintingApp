package com.tumcca.api.db;

import com.tumcca.api.model.Tags;
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
 * @since 2015-06-16
 */
public class TagsMapper implements ResultSetMapper<Tags> {
    @Override
    public Tags map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        return new Tags(resultSet.getLong("ID"), resultSet.getString("NAME"));
    }
}
