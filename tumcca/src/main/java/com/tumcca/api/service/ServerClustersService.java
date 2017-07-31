package com.tumcca.api.service;

import com.google.common.base.Optional;
import com.tumcca.api.db.ServerClustersDAO;
import com.tumcca.api.model.ServerClusters;
import io.dropwizard.lifecycle.Managed;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
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
public class ServerClustersService implements Managed {
    static final Logger LOGGER = LoggerFactory.getLogger(ServerClustersService.class);

    final DBI dbi;
    final String serverId;

    public ServerClustersService(DBI dbi, String serverId) {
        this.dbi = dbi;
        this.serverId = serverId;
    }

    @Override
    public void start() throws Exception {
        LOGGER.info("Server {} is starting....", serverId);
        try (final ServerClustersDAO serverClustersDAO = dbi.open(ServerClustersDAO.class)) {
            final ServerClusters serverClusters = serverClustersDAO.find(Optional.of(serverId));
            if (serverClusters == null) {
                serverClustersDAO.insert(Optional.of(serverId), Optional.of(1));
            } else if (serverClusters.getStatus() == 0) {
                serverClustersDAO.update(Optional.of(serverId), Optional.of(1), Optional.of(new Date()));
            } else {
                final String errorMessage = "Server id conflicts";
                LOGGER.error(errorMessage);
                throw new WebApplicationException(errorMessage);
            }
        }
    }

    @Override
    public void stop() throws Exception {
        LOGGER.info("Server {} is stopping....", serverId);
        try (final ServerClustersDAO serverClustersDAO = dbi.open(ServerClustersDAO.class)) {
            final ServerClusters serverClusters = serverClustersDAO.find(Optional.of(serverId));
            if (serverClusters == null) {
                final String errorMessage = "server not registered";
                LOGGER.error(errorMessage);
                throw new WebApplicationException(errorMessage);
            } else if (serverClusters.getStatus() == 0) {
                final String errorMessage = "server status invalid";
                LOGGER.error(errorMessage);
                throw new WebApplicationException(errorMessage);
            } else {
                LOGGER.info("Server {} signed out successfully", serverId);
                serverClustersDAO.update(Optional.of(serverId), Optional.of(0), Optional.of(new Date()));
            }
        }
    }
}
