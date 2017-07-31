package com.tumcca.api.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import com.tumcca.api.model.WorksPO;

/**
 * Title.
 * <p/>
 * Description.
 *
 * @author Bill Lv {@literal <billcc.lv@hotmail.com>}
 * @version 1.0
 * @since 2015-06-16
 */
public class WorksPOMapper implements ResultSetMapper<WorksPO> {
    @Override
    public WorksPO map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        return new WorksPO(resultSet.getLong("ID"), resultSet.getLong("CATEGORY"), resultSet.getLong("AUTHOR")
        		, resultSet.getInt("STATUS"), resultSet.getTimestamp("CREATE_TIME"), resultSet.getLong("ALBUMID")
        		, resultSet.getString("TITLE") , resultSet.getString("DESCRIPTION"), resultSet.getString("TAGS"));
    }
}
