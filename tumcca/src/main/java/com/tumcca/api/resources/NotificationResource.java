package com.tumcca.api.resources;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.tumcca.api.db.FollowDAO;
import com.tumcca.api.db.UserNotificationDAO;
import com.tumcca.api.db.UsersDAO;
import com.tumcca.api.message.Follow;
import com.tumcca.api.message.UnFollow;
import com.tumcca.api.model.ErrorVO;
import com.tumcca.api.model.Principals;
import com.tumcca.api.model.UserNotificationStatus;
import com.tumcca.api.model.UserNotifications;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import io.dropwizard.auth.Auth;
import io.dropwizard.jersey.params.LongParam;
import org.atmosphere.wasync.RequestBuilder;
import org.atmosphere.wasync.Socket;
import org.atmosphere.wasync.impl.AtmosphereClient;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Title.
 * <p/>
 * Description.
 *
 * @author Bill Lv {@literal <billcc.lv@hotmail.com>}
 * @version 1.0
 * @since 2015-06-22
 */
@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
@Api("/notification")
public class NotificationResource {
    static final Logger LOGGER = LoggerFactory.getLogger(NotificationResource.class);

    final DBI dbi;
    final Map<Integer, ErrorVO> errorsMap;
    final AtmosphereClient atmosphereClient;
    final RequestBuilder followRequest;
    final RequestBuilder unfollowRequest;

    public NotificationResource(DBI dbi, Map<Integer, ErrorVO> errorsMap, AtmosphereClient atmosphereClient, RequestBuilder followRequest, RequestBuilder unfollowRequest) {
        this.dbi = dbi;
        this.errorsMap = errorsMap;
        this.atmosphereClient = atmosphereClient;
        this.followRequest = followRequest;
        this.unfollowRequest = unfollowRequest;
    }

    @GET
    @Path("/notifications")
    @ApiOperation("Get unread notifications")
    public Response getUnread(@Auth Principals principal) throws Exception {
        try (final UserNotificationDAO userNotificationDAO = dbi.open(UserNotificationDAO.class)) {
            final List<UserNotifications> notifications = userNotificationDAO.findByUid(Optional.of(principal.getUid()));
            return Response.ok(notifications).build();
        }
    }

    @POST
    @Path("/notifications/{notificationId}/read")
    @ApiOperation("Read notification")
    public Response markRead(@Auth Principals principal, @PathParam("notificationId") LongParam notificationId) throws Exception {
        try (final UserNotificationDAO userNotificationDAO = dbi.open(UserNotificationDAO.class)) {
            final UserNotificationStatus userNotificationStatus = userNotificationDAO.findById(Optional.of(notificationId.get()));
            if (userNotificationStatus == null) {
                throw new WebApplicationException(Response.status(404).entity(errorsMap.get(1008)).build());
            }
            if (userNotificationStatus.getStatus() == 1) {
                throw new WebApplicationException(Response.status(422).entity(errorsMap.get(1029)).build());
            }
            final Long uid = userNotificationStatus.getUid();
            if (uid != principal.getUid()) {
                throw new WebApplicationException(Response.status(403).entity(errorsMap.get(1028)).build());
            }
            userNotificationDAO.update(Optional.of(notificationId.get()), Optional.of(1), Optional.of(new Date()));
        }
        return Response.ok(ImmutableMap.of("id", notificationId.get())).build();
    }

    @POST
    @Path("/follow/notify")
    @ApiOperation("Notify follow proactively")
    public Response notifyFollower(@Auth Principals principal, @Valid @ApiParam Follow follow) throws Exception {
        if (principal.getUid() != follow.getFollower()) {
            throw new WebApplicationException(Response.status(403).entity(errorsMap.get(1030)).build());
        }
        if (follow.getFollower() == follow.getToFollow()) {
            throw new WebApplicationException(Response.status(422).entity(errorsMap.get(1031)).build());
        }
        try (final UsersDAO usersDAO = dbi.open(UsersDAO.class)) {
            if (usersDAO.countUid(Optional.of(follow.getToFollow())) == 0) {
                throw new WebApplicationException(Response.status(404).entity(errorsMap.get(1033)).build());
            }
        }

        try (final FollowDAO followDAO = dbi.open(FollowDAO.class)) {
            if (followDAO.count(Optional.of(follow.getFollower()), Optional.of(follow.getToFollow())) > 0) {
                LOGGER.info("You have already followed the guy {}", follow.getToFollow());
                throw new WebApplicationException(Response.status(422).entity(errorsMap.get(1032)).build());
            }
        }

        Socket followSocket = null;
        try {
            followSocket = atmosphereClient.create();
            followSocket.open(followRequest.build());
            followSocket.fire(follow);
        } finally {
            if (followSocket != null) {
                followSocket.close();
            }
        }
        return Response.ok(ImmutableMap.of("toFollow", follow.getToFollow())).build();
    }

    @POST
    @Path("/unfollow/notify")
    @ApiOperation("Unfollow the following")
    public Response notifyFollower(@Auth Principals principal, @Valid @ApiParam UnFollow unfollow) throws Exception {
        if (principal.getUid() != unfollow.getFollower() && principal.getUid() != unfollow.getFollowing()) {
            throw new WebApplicationException(Response.status(403).entity(errorsMap.get(1034)).build());
        }
        try (final UsersDAO usersDAO = dbi.open(UsersDAO.class)) {
            if (usersDAO.countUid(Optional.of(unfollow.getFollower())) == 0) {
                LOGGER.info("The follower does NOT exist");
                throw new WebApplicationException(Response.status(404).entity(errorsMap.get(1035)).build());
            }
            if (usersDAO.countUid(Optional.of(unfollow.getFollowing())) == 0) {
                LOGGER.info("The following you would like to unfollow does NOT exist");
                throw new WebApplicationException(Response.status(404).entity(errorsMap.get(1036)).build());
            }
        }
        try (final FollowDAO followDAO = dbi.open(FollowDAO.class)) {
            if (followDAO.count(Optional.of(unfollow.getFollower()), Optional.of(unfollow.getFollowing())) == 0) {
                LOGGER.info("You have NOT followed the guy {}", unfollow.getFollowing());
                throw new WebApplicationException(Response.status(422).entity(errorsMap.get(1037)).build());
            }
        }
        Socket unfollowSocket = null;
        try {
            unfollowSocket = atmosphereClient.create();
            unfollowSocket.open(unfollowRequest.build());
            unfollowSocket.fire(unfollow);
        } finally {
            if (unfollowSocket != null) {
                unfollowSocket.close();
            }
        }
        return Response.ok(ImmutableMap.of("follower", unfollow.getFollower())).build();
    }
}
