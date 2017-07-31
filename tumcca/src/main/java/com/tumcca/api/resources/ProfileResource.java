package com.tumcca.api.resources;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.tumcca.api.db.FollowDAO;
import com.tumcca.api.db.UsersDAO;
import com.tumcca.api.model.ErrorVO;
import com.tumcca.api.model.Principals;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import io.dropwizard.auth.Auth;
import io.dropwizard.jersey.params.LongParam;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
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
@Api("/profile")
public class ProfileResource {
    static final Logger LOGGER = LoggerFactory.getLogger(ProfileResource.class);

    final DBI dbi;
    final Map<Integer, ErrorVO> errorsMap;

    public ProfileResource(DBI dbi, Map<Integer, ErrorVO> errorsMap) {
        this.dbi = dbi;
        this.errorsMap = errorsMap;
    }

    @GET
    @Path("/profile/followers/count")
    @ApiOperation("Get followers number")
    public Response getFollowerNumber(@Auth Principals principal) throws Exception {
        try (final FollowDAO followDAO = dbi.open(FollowDAO.class)) {
            final Integer count = followDAO.countFollowers(Optional.of(principal.getUid()));
            return Response.ok(ImmutableMap.of("followerCount", count)).build();
        }
    }

    @GET
    @Path("/profile/following/count")
    @ApiOperation("Get following number")
    public Response getToFollowNumber(@Auth Principals principal) throws Exception {
        try (final FollowDAO followDAO = dbi.open(FollowDAO.class)) {
            final Integer count = followDAO.countToFollow(Optional.of(principal.getUid()));
            return Response.ok(ImmutableMap.of("followingCount", count)).build();
        }
    }

    @GET
    @Path("/profile/{uid}/followers/count")
    @ApiOperation("Get followers number by uid")
    public Response getFollowerNumberByUid(@PathParam("uid") LongParam uid) throws Exception {
        try (final UsersDAO usersDAO = dbi.open(UsersDAO.class)) {
            if (usersDAO.countUid(Optional.of(uid.get())) == 0) {
                throw new WebApplicationException(Response.status(404).entity(errorsMap.get(1008)).build());
            }
        }
        try (final FollowDAO followDAO = dbi.open(FollowDAO.class)) {
            final Integer count = followDAO.countFollowers(Optional.of(uid.get()));
            return Response.ok(ImmutableMap.of("followerCount", count)).build();
        }
    }

    @GET
    @Path("/profile/{uid}/following/count")
    @ApiOperation("Get following number by uid")
    public Response getToFollowNumberByUid(@PathParam("uid") LongParam uid) throws Exception {
        try (final UsersDAO usersDAO = dbi.open(UsersDAO.class)) {
            if (usersDAO.countUid(Optional.of(uid.get())) == 0) {
                throw new WebApplicationException(Response.status(404).entity(errorsMap.get(1008)).build());
            }
        }
        try (final FollowDAO followDAO = dbi.open(FollowDAO.class)) {
            final Integer count = followDAO.countToFollow(Optional.of(uid.get()));
            return Response.ok(ImmutableMap.of("followingCount", count)).build();
        }
    }
}
