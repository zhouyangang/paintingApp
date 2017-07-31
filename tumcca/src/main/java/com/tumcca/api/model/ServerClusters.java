package com.tumcca.api.model;

/**
 * Title.
 * <p/>
 * Description.
 *
 * @author Bill Lv {@literal <billcc.lv@hotmail.com>}
 * @version 1.0
 * @since 2015-06-13
 */
public class ServerClusters {
    String serverId;
    Integer status;

    public ServerClusters() {
    }

    public ServerClusters(String serverId, Integer status) {
        this.serverId = serverId;
        this.status = status;
    }

    public String getServerId() {
        return serverId;
    }

    public Integer getStatus() {
        return status;
    }
}
