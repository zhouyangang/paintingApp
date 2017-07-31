package com.tumcca.api.db;

import com.google.common.base.Optional;
import com.tumcca.api.model.UserNotificationStatus;
import com.tumcca.api.model.UserNotifications;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;
import org.skife.jdbi.v2.sqlobject.mixins.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Title.
 * <p/>
 * Description.
 *
 * @author Bill Lv {@literal <billcc.lv@hotmail.com>}
 * @version 1.0
 * @since 2015-06-22
 */
public interface UserNotificationDAO extends AutoCloseable, Transactional<UserNotificationDAO> {
    @SqlUpdate("INSERT INTO TUMCCA_USER_NOTIFICATIONS (UID, ACTION, MESSAGE, STATUS) VALUES (:UID, :ACTION, :MESSAGE, :STATUS)")
    @GetGeneratedKeys
    Long insert(@Bind("UID") Optional<Long> uid, @Bind("ACTION") Optional<String> action, @Bind("MESSAGE") Optional<String> message, @Bind("STATUS") Optional<Integer> status);

    @SqlQuery("SELECT ID, ACTION, MESSAGE, CREATE_TIME FROM TUMCCA_USER_NOTIFICATIONS WHERE UID = :UID AND STATUS = 0 ORDER BY CREATE_TIME DESC")
    @Mapper(UserNotificationMapper.class)
    List<UserNotifications> findByUid(@Bind("UID") Optional<Long> uid);

    @SqlQuery("SELECT UID, STATUS FROM TUMCCA_USER_NOTIFICATIONS WHERE ID = :ID")
    @Mapper(UserNotificationStatusMapper.class)
    UserNotificationStatus findById(@Bind("ID") Optional<Long> id);

    @SqlUpdate("UPDATE TUMCCA_USER_NOTIFICATIONS SET STATUS = :STATUS, READ_TIME = :READ_TIME WHERE ID = :ID")
    void update(@Bind("ID") Optional<Long> id, @Bind("STATUS") Optional<Integer> status, @Bind("READ_TIME") Optional<Date> readTime);
}
