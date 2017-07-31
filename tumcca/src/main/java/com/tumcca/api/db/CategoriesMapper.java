package com.tumcca.api.db;

import com.tumcca.api.model.Categories;
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
public class CategoriesMapper implements ResultSetMapper<Categories> {
    @Override
    public Categories map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        return new Categories(resultSet.getLong("ID"), resultSet.getString("PATH"), resultSet.getString("NAME"));
    }
}
