package com.tumcca.api.db;

import com.google.common.base.Optional;
import com.tumcca.api.model.ServerClusters;
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
 * @since 2015-06-13
 */
public interface ServerClustersDAO extends AutoCloseable, Transactional<ServerClustersDAO> {
    @SqlQuery("SELECT COUNT(*) FROM TUMCCA_SERVER_CLUSTERS WHERE SERVER_ID = :SERVER_ID")
    Integer count(@Bind("SERVER_ID") Optional<String> serverId);

    @SqlQuery("SELECT SERVER_ID, STATUS FROM TUMCCA_SERVER_CLUSTERS WHERE SERVER_ID = :SERVER_ID")
    @Mapper(ServerClustersMapper.class)
    ServerClusters find(@Bind("SERVER_ID") Optional<String> serverId);

    @SqlUpdate("INSERT INTO TUMCCA_SERVER_CLUSTERS (SERVER_ID, STATUS) VALUES (:SERVER_ID, :STATUS)")
    void insert(@Bind("SERVER_ID") Optional<String> serverId, @Bind("STATUS") Optional<Integer> status);

    @SqlUpdate("UPDATE TUMCCA_SERVER_CLUSTERS SET STATUS = :STATUS, SHUTDOWN_TIME = :SHUTDOWN_TIME WHERE SERVER_ID = :SERVER_ID")
    void update(@Bind("SERVER_ID") Optional<String> serverId, @Bind("STATUS") Optional<Integer> status, @Bind("SHUTDOWN_TIME") Optional<Date> shutdownTime);
}
