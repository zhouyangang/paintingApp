package com.tumcca.api.db;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import com.tumcca.api.model.AlbumPO;

/**
 * Title.
 * <p/>
 * Description.
 *
 * @author Neil 
 * @version 1.0
 * @since 2015-06-27
 */
public class AlbumPOMapper implements ResultSetMapper<AlbumPO> {

	@Override
	public AlbumPO map(int i, ResultSet resultSet, StatementContext statementContext)
			throws SQLException {
		return new AlbumPO(resultSet.getLong("ID"), resultSet.getLong("AUTHOR"), resultSet.getString("TITLE"), resultSet.getString("DESCRIPTION"), resultSet.getTimestamp("CREATE_TIME"));
	}

}
