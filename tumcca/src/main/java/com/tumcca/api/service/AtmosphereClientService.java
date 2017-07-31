package com.tumcca.api.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Optional;
import com.tumcca.api.db.FollowDAO;
import com.tumcca.api.db.UserNotificationDAO;
import com.tumcca.api.db.UsersDAO;
import com.tumcca.api.message.Follow;
import com.tumcca.api.message.UnFollow;
import com.tumcca.api.model.MessageAction;
import io.dropwizard.lifecycle.Managed;
import org.atmosphere.wasync.Event;
import org.atmosphere.wasync.Function;
import org.atmosphere.wasync.RequestBuilder;
import org.atmosphere.wasync.Socket;
import org.atmosphere.wasync.impl.AtmosphereClient;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import java.io.IOException;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Title.
 * <p/>
 * Description.
 *
 * @author Bill Lv {@literal <billcc.lv@hotmail.com>}
 * @version 1.0
 * @since 2015-06-22
 */
public class AtmosphereClientService implements Managed {
    static final Logger LOGGER = LoggerFactory.getLogger(AtmosphereClientService.class);

    final ObjectMapper mapper = new ObjectMapper();

    final AtmosphereClient atmosphereClient;
    final RequestBuilder followRequest;
    final RequestBuilder unfollowRequest;
    final DBI dbi;
    final Long clientServiceDelay;

    Socket followSocket;
    Socket unfollowSocket;
    ScheduledThreadPoolExecutor executor;

    public AtmosphereClientService(AtmosphereClient atmosphereClient, RequestBuilder followRequest, RequestBuilder unfollowRequest, DBI dbi, Long clientServiceDelay) {
        this.atmosphereClient = atmosphereClient;
        this.followRequest = followRequest;
        this.unfollowRequest = unfollowRequest;
        this.dbi = dbi;
        this.clientServiceDelay = clientServiceDelay;
    }

    @Override
    public void start() throws Exception {
        LOGGER.info("start atmosphere client....");
        Runnable task = () -> {
            LOGGER.info("start to create follow socket....");
            followSocket = atmosphereClient.create();
            try {
                followSocket.on("message", new Function<Follow>() {
                    @Override
                    public void on(Follow follow) {
                        LOGGER.info("Got message: {}", follow);
                        try (final UsersDAO usersDAO = dbi.open(UsersDAO.class)) {
                            if (usersDAO.countUid(Optional.of(follow.getFollower())) == 0) {
                                LOGGER.info("The follower does NOT exist");
                                return;
                            }
                            if (usersDAO.countUid(Optional.of(follow.getToFollow())) == 0) {
                                LOGGER.info("The to-follow you would like to follow does NOT exist");
                                return;
                            }
                        } catch (Exception e) {
                            throw new WebApplicationException(e);
                        }
                        try (final FollowDAO followDAO = dbi.open(FollowDAO.class)) {
                            if (followDAO.count(Optional.of(follow.getFollower()), Optional.of(follow.getToFollow())) > 0) {
                                LOGGER.info("You have already followed the guy {}", follow.getToFollow());
                                return;
                            }
                            followDAO.insert(Optional.of(follow.getFollower()), Optional.of(follow.getToFollow()));
                        } catch (Exception e) {
                            throw new WebApplicationException(e);
                        }
                        try (final UserNotificationDAO userNotificationDAO = dbi.open(UserNotificationDAO.class)) {
                            userNotificationDAO.insert(Optional.of(follow.getToFollow()), Optional.of(MessageAction.FOLLOW.toString()), Optional.of(mapper.writeValueAsString(follow)), Optional.of(0));
                        } catch (Exception e) {
                            throw new WebApplicationException(e);
                        }
                    }
                }).on(Event.CLOSE.name(), t ->
                                LOGGER.info("Monitor Connection closed")
                ).open(followRequest.build());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            LOGGER.info("start to create unfollow socket....");
            unfollowSocket = atmosphereClient.create();
            try {
                unfollowSocket.on("message", new Function<UnFollow>() {
                    @Override
                    public void on(UnFollow unfollow) {
                        LOGGER.info("Got message: {}", unfollow);
                        try (final UsersDAO usersDAO = dbi.open(UsersDAO.class)) {
                            if (usersDAO.countUid(Optional.of(unfollow.getFollower())) == 0) {
                                LOGGER.info("The follower does NOT exist");
                                return;
                            }
                            if (usersDAO.countUid(Optional.of(unfollow.getFollowing())) == 0) {
                                LOGGER.info("The following you would like to unfollow does NOT exist");
                                return;
                            }
                        } catch (Exception e) {
                            throw new WebApplicationException(e);
                        }
                        try (final FollowDAO followDAO = dbi.open(FollowDAO.class)) {
                            if (followDAO.count(Optional.of(unfollow.getFollower()), Optional.of(unfollow.getFollowing())) == 0) {
                                LOGGER.info("You have NOT followed the guy {}", unfollow.getFollowing());
                                return;
                            }
                            followDAO.delete(Optional.of(unfollow.getFollower()), Optional.of(unfollow.getFollowing()));
                        } catch (Exception e) {
                            throw new WebApplicationException(e);
                        }
                    }
                }).on(Event.CLOSE.name(), t ->
                                LOGGER.info("Monitor Connection closed")
                ).open(unfollowRequest.build());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
        executor = new ScheduledThreadPoolExecutor(1);
        executor.schedule(task, clientServiceDelay, TimeUnit.SECONDS);
    }

    @Override
    public void stop() throws Exception {
        LOGGER.info("close atmosphere client....");
        followSocket.close();
        unfollowSocket.close();
        executor.shutdown();
    }
}
