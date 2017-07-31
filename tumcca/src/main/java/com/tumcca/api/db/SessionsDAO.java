package com.tumcca.api.db;

import com.google.common.base.Optional;
import com.tumcca.api.model.Sessions;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;
import org.skife.jdbi.v2.sqlobject.mixins.Transactional;

import java.util.Date;

/**
 * Title.
 * <p/>
 * Description.
 *
 * @author Bill Lv {@literal <billcc.lv@hotmail.com>}
 * @version 1.0
 * @since 2015-06-03
 */
public interface SessionsDAO extends AutoCloseable, Transactional<SessionsDAO> {
    @SqlUpdate("INSERT INTO TUMCCA_SESSIONS (UID, TOKEN, STATUS) VALUES (:UID, :TOKEN, :STATUS)")
    void insert(@Bind("UID") Optional<Long> uid, @Bind("TOKEN") Optional<String> token, @Bind("STATUS") Optional<Integer> status);

    @SqlQuery("SELECT UID, SIGN_IN_TIME, SIGN_OUT_TIME, STATUS FROM TUMCCA_SESSIONS WHERE TOKEN = :TOKEN")
    @Mapper(SessionsMapper.class)
    Sessions findByToken(@Bind("TOKEN") Optional<String> token);

    @SqlUpdate("UPDATE TUMCCA_SESSIONS SET SIGN_OUT_TIME = :SIGN_OUT_TIME, STATUS = :STATUS WHERE TOKEN = :TOKEN")
    void update(@Bind("TOKEN") Optional<String> token, @Bind("SIGN_OUT_TIME") Optional<Date> signOutTime, @Bind("STATUS") Optional<Integer> status);
}
