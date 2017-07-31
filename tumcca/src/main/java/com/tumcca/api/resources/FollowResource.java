package com.tumcca.api.resources;

import io.dropwizard.auth.Auth;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.tumcca.api.db.CategoriesDAO;
import com.tumcca.api.db.FollowDAO;
import com.tumcca.api.db.UsersDAO;
import com.tumcca.api.message.Follow;
import com.tumcca.api.message.FollowCategory;
import com.tumcca.api.message.UnFollow;
import com.tumcca.api.model.Categories;
import com.tumcca.api.model.ErrorVO;
import com.tumcca.api.model.Principals;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
@Api("/follow")
public class FollowResource extends BaseResource{

	private static final Logger LOGGER = LoggerFactory.getLogger(FollowResource.class);
	
	public FollowResource(DBI dbi, Map<Integer, ErrorVO> errorsMap) {
		super(dbi, errorsMap);
	}
    
    @POST
    @Path("/follow")
    @ApiOperation("Add follow")
    public Response addFollow(@Auth Principals principal, @Valid @ApiParam Follow follow) throws Exception{
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
            followDAO.insert(Optional.of(follow.getFollower()), Optional.of(follow.getToFollow()));
        }
    	
        return Response.ok(follow).build();
    }
    
    @DELETE
    @Path("/follow")
    @ApiOperation("Delete follow by follower and tofollow")
    public Response deleteFollow(@Auth Principals principal, @Valid @ApiParam UnFollow unfollow) throws Exception{
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
            followDAO.delete(Optional.of(unfollow.getFollower()), Optional.of(unfollow.getFollowing()));
        }
        
        return Response.ok(unfollow).build();
    }
    
    @GET
    @Path("/follow/count")
    @ApiOperation("Get my follow count 关注数量")
    public Response followCount(@Auth Principals principal) throws Exception{
    	try (final FollowDAO followDAO = dbi.open(FollowDAO.class)) {
    		Integer toFollows = followDAO.countToFollow(Optional.of(principal.getUid()));
    		return Response.ok(ImmutableMap.of("count", toFollows)).build();
    	}
    }
    
    @GET
    @Path("/follow/count/fan")
    @ApiOperation("Get my fan count 粉丝数量")
    public Response fanCount(@Auth Principals principal) throws Exception{
    	try (final FollowDAO followDAO = dbi.open(FollowDAO.class)) {
    		Integer fans = followDAO.countFollowers(Optional.of(principal.getUid()));
    		return Response.ok(ImmutableMap.of("count", fans)).build();
    	}
    }
    
    
    
    @POST
    @Path("/followcategory")
    @ApiOperation("Add follow category")
    public Response addFollowCategory(@Auth Principals principal, @Valid @ApiParam FollowCategory followCat) throws Exception{
        if (principal.getUid() != followCat.getFollower()) {
            throw new WebApplicationException(Response.status(403).entity(errorsMap.get(1030)).build());
        }

        try (final FollowDAO followDAO = dbi.open(FollowDAO.class)) {
            if (followDAO.countCategory(Optional.of(followCat.getFollower()), Optional.of(followCat.getCategory())) > 0) {
                LOGGER.info("You have already followed the category {}", followCat.getCategory());
                throw new WebApplicationException(Response.status(422).entity(errorsMap.get(1032)).build());
            }
            followDAO.insertCategory(Optional.of(followCat.getFollower()), Optional.of(followCat.getCategory()));
        }
    	
        return Response.ok(followCat).build();
    }
    
    @DELETE
    @Path("/followcategory")
    @ApiOperation("Delete follow category by follower and category")
    public Response deleteFollowCategory(@Auth Principals principal, @Valid @ApiParam FollowCategory followCat) throws Exception{
        if (principal.getUid() != followCat.getFollower()) {
            throw new WebApplicationException(Response.status(403).entity(errorsMap.get(1030)).build());
        }
        
        try (final FollowDAO followDAO = dbi.open(FollowDAO.class)) {
            followDAO.deleteCategory(Optional.of(followCat.getFollower()), Optional.of(followCat.getCategory()));
        }
        return Response.ok(followCat).build();
    }
    
    @GET
    @Path("/followcategory")
    @ApiOperation("Get my follow category")
    public Response followCategory(@Auth Principals principal) throws Exception{
    	try (final CategoriesDAO catDAO = dbi.open(CategoriesDAO.class)) {
    		List<Categories> categories = catDAO.findByUid(Optional.of(principal.getUid()));
    		return Response.ok(categories).build();
    	}
    }
    
}
