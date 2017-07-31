package com.tumcca.api.db;

import com.tumcca.api.model.Artists;
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
public class ArtistsMapper implements ResultSetMapper<Artists> {
    @Override
    public Artists map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {
        return new Artists(resultSet.getLong("UID"), resultSet.getString("PSEUDONYM"), resultSet.getInt("GENDER"), resultSet.getString("INTRODUCTION"), resultSet.getString("TITLE"), resultSet.getString("HOBBIES"), resultSet.getString("FORTE"), resultSet.getLong("AVATAR"), resultSet.getString("COUNTRY"), resultSet.getString("PROVINCE"), resultSet.getString("CITY"));
    }
}
