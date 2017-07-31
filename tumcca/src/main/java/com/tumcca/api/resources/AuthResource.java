package com.tumcca.api.resources;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.tumcca.api.db.UsersDAO;
import com.tumcca.api.model.*;
import com.tumcca.api.service.AuthService;
import com.tumcca.api.util.PasswordHash;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import io.dropwizard.auth.Auth;
import io.dropwizard.jersey.params.LongParam;
import org.apache.commons.lang3.StringUtils;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.TransactionStatus;
import org.skife.jdbi.v2.sqlobject.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
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
 * @since 2015-06-03
 */
@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
@Api("/auth")
public class AuthResource {
    static final Logger LOGGER = LoggerFactory.getLogger(AuthResource.class);

    final DBI dbi;

    final AuthService authService;

    final Map<Integer, ErrorVO> errorsMap;

    public AuthResource(DBI dbi, AuthService authService, Map<Integer, ErrorVO> errorsMap) {
        this.dbi = dbi;
        this.authService = authService;
        this.errorsMap = errorsMap;
    }

    @POST
    @Path("/mobile/exists")
    @ApiOperation("Validate if the mobile has been registered")
    public Response mobileExists(@Valid @ApiParam MobileVO mobileVO) throws Exception {
        try (final UsersDAO usersDAO = dbi.open(UsersDAO.class)) {
            final boolean exists = usersDAO.countMobile(Optional.of(mobileVO.getMobile())) > 0;
            return Response.ok(exists).build();
        }
    }

    @POST
    @Path("/email/exists")
    @ApiOperation("Validate if the email has been registered")
    public Response emailExists(@Valid @ApiParam EmailVO emailVO) throws Exception {
        try (final UsersDAO usersDAO = dbi.open(UsersDAO.class)) {
            final boolean exists = usersDAO.countEmail(Optional.of(emailVO.getEmail())) > 0;
            return Response.ok(exists).build();
        }
    }

    @POST
    @Path("/sign-in")
    @ApiOperation("User sign in")
    public Response signIn(@Valid @ApiParam SignInVO signIn) throws Exception {
        LOGGER.info("User: {} tries to sign in", signIn.getUsername());
        final Optional<Principals> principal =
                authService.signIn(signIn.getUsername(), signIn.getPassword());
        if (principal.isPresent()) {
            return Response.ok(principal.get()).build();
        }
        return Response.status(Response.Status.NOT_ACCEPTABLE).entity(errorsMap.get(1001)).build();
    }

    @POST
    @Path("/sign-out")
    @ApiOperation("User sign out")
    public Response signOut(@Auth Principals principal) throws Exception {
        LOGGER.info("Principal {} is going to sign out....", principal);
        try {
            authService.signOut(principal);
        } catch (Exception e) {
            return Response.status(Response.Status.UNAUTHORIZED).entity(errorsMap.get(1002)).build();
        }
        return Response.ok(ImmutableMap.of("uid", principal.getUid())).build();
    }

    @POST
    @Path("/sign-up")
    @ApiOperation("User sign up")
    public Response signUp(@Valid @ApiParam SignUpVO signUpVO) throws Exception {
        LOGGER.info("Signing up....");
        final Long uid = addUser(signUpVO);
        return Response.ok(ImmutableMap.of("uid", uid)).build();
    }

    @POST
    @Path("/artists/profile")
    @ApiOperation("Add or update user profile")
    public Response refineProfile(@Auth Principals principal, @Valid @ApiParam ProfileVO profileVO) throws Exception {
        LOGGER.info("Refining profile....");
        try (final UsersDAO usersDAO = dbi.open(UsersDAO.class)) {
            final boolean userExists = usersDAO.countUid(Optional.of(principal.getUid())) > 0;
            if (!userExists) {
                return Response.status(404).entity(errorsMap.get(1003)).build();
            }
            final Artists artist = usersDAO.findArtist(Optional.of(principal.getUid()));
            if (artist == null) {
                usersDAO.insertArtist(Optional.of(principal.getUid()), Optional.of(profileVO.getPseudonym()), Optional.fromNullable(profileVO.getGender()).or(Optional.of(0)), Optional.fromNullable(profileVO.getIntroduction()), Optional.fromNullable(profileVO.getTitle()), Optional.fromNullable(profileVO.getHobbies()), Optional.fromNullable(profileVO.getForte()), Optional.fromNullable(profileVO.getAvatar()), Optional.fromNullable(profileVO.getCountry()), Optional.fromNullable(profileVO.getProvince()), Optional.fromNullable(profileVO.getCity()));
            } else {
                usersDAO.updateArtist(Optional.of(principal.getUid()), Optional.of(profileVO.getPseudonym()), Optional.fromNullable(profileVO.getGender()).or(Optional.of(0)), Optional.fromNullable(profileVO.getIntroduction()), Optional.fromNullable(profileVO.getTitle()), Optional.fromNullable(profileVO.getHobbies()), Optional.fromNullable(profileVO.getForte()), Optional.fromNullable(profileVO.getAvatar()), Optional.fromNullable(profileVO.getCountry()), Optional.fromNullable(profileVO.getProvince()), Optional.fromNullable(profileVO.getCity()));
            }
            return Response.ok(ImmutableMap.of("uid", principal.getUid())).build();
        }
    }

    @GET
    @Path("/artists/profile")
    @ApiOperation("Read user profile")
    public Response getProfile(@Auth Principals principal) throws Exception {
        try (final UsersDAO usersDAO = dbi.open(UsersDAO.class)) {
            final Artists artist = usersDAO.findArtist(Optional.of(principal.getUid()));
            if (artist == null) {
                return Response.status(404).entity(errorsMap.get(1004)).build();
            }
            final ProfileVO profileVO = new ProfileVO(artist.getPseudonym(), artist.getGender(), artist.getIntroduction(), artist.getTitle(), artist.getHobbies(), artist.getForte(), artist.getAvatar(), artist.getCountry(), artist.getProvince(), artist.getCity());
            return Response.ok(profileVO).build();
        }
    }

    @GET
    @Path("/artists/{uid}/profile")
    @ApiOperation("Read user profile by uid")
    public Response getProfileByUid(@PathParam("uid") LongParam uid) throws Exception {
        try (final UsersDAO usersDAO = dbi.open(UsersDAO.class)) {
            final Artists artist = usersDAO.findArtist(Optional.of(uid.get()));
            if (artist == null) {
                return Response.status(404).entity(errorsMap.get(1004)).build();
            }
            final ProfileVO profileVO = new ProfileVO(artist.getPseudonym(), artist.getGender(), artist.getIntroduction(), artist.getTitle(), artist.getHobbies(), artist.getForte(), artist.getAvatar(), artist.getCountry(), artist.getProvince(), artist.getCity());
            return Response.ok(profileVO).build();
        }
    }

    @PUT
    @Path("/artists/account")
    @ApiOperation("Update user account")
    public Response updateAccount(@Auth Principals principal, @Valid @ApiParam AccountVO accountVO) throws Exception {
        updateUser(principal.getUid(), accountVO);
        return Response.ok(ImmutableMap.of("uid", principal.getUid())).build();
    }

    @GET
    @Path("/artists/account")
    @ApiOperation("Read user account")
    public Response getAccount(@Auth Principals principal) throws Exception {
        try (final UsersDAO usersDAO = dbi.open(UsersDAO.class)) {
            final UserVO userVO = usersDAO.findUserVO(Optional.of(principal.getUid()));
            if (userVO == null) {
                return Response.status(404).entity(errorsMap.get(1003)).build();
            }
            return Response.ok(userVO).build();
        }
    }

    @GET
    @Path("/artists/{uid}/contact")
    @ApiOperation("Read user contact by uid")
    public Response getContactByUid(@Auth Principals principal, @PathParam("uid") LongParam uid) throws Exception {
        try (final UsersDAO usersDAO = dbi.open(UsersDAO.class)) {
            final ContactVO contactVO = usersDAO.findContactVO(Optional.of(uid.get()));
            if (contactVO == null) {
                return Response.status(404).entity(errorsMap.get(1003)).build();
            }
            return Response.ok(contactVO).build();
        }
    }

    @DELETE
    @Path("/artists/{artistId}")
    public Response deleteArtist(@PathParam("artistId") LongParam artistId) {
        LOGGER.info("Deleting artist {}", artistId);
        deleteUser(artistId.get());
        return Response.ok().build();
    }

    @Transaction
    private void deleteUser(final Long id) {
        try (final UsersDAO usersDAO = dbi.open(UsersDAO.class)) {
            usersDAO.deleteArtist(Optional.of(id));
            usersDAO.deleteRole(Optional.of(id));
            usersDAO.deleteUser(Optional.of(id));
        } catch (Exception e) {
            throw new WebApplicationException(500);
        }
    }

    @Transaction
    private void updateUser(final Long uid, final AccountVO accountVO) throws Exception {
        try (final UsersDAO usersDAO = dbi.open(UsersDAO.class)) {
            usersDAO.begin();
            final UserVO userVO = usersDAO.findUserVO(Optional.of(uid));
            if (userVO == null) {
                throw new WebApplicationException(Response.status(404).entity(errorsMap.get(1003)).build());
            }
            if (StringUtils.isNotBlank(accountVO.getEmail())) {
                if (!accountVO.getEmail().equals(userVO.getEmail())) {
                    if (usersDAO.countEmail(Optional.of(accountVO.getEmail())) == 0) {
                        usersDAO.updateEmail(Optional.of(uid), Optional.of(accountVO.getEmail()));
                    } else {
                        usersDAO.rollback();
                        throw new WebApplicationException(Response.status(422).entity(errorsMap.get(1005)).build());
                    }
                }
            }
            if (StringUtils.isNotBlank(accountVO.getMobile())) {
                if (!accountVO.getMobile().equals(userVO.getMobile())) {
                    if (usersDAO.countMobile(Optional.of(accountVO.getMobile())) == 0) {
                        usersDAO.updateMobile(Optional.of(uid), Optional.of(accountVO.getMobile()));
                    } else {
                        usersDAO.rollback();
                        throw new WebApplicationException(Response.status(422).entity(errorsMap.get(1006)).build());
                    }
                }
            }
            usersDAO.commit();
        }
    }

    @Transaction
    private Long addUser(final SignUpVO signUpVO) throws Exception {
        if (StringUtils.isBlank(signUpVO.getEmail())) {
            if (StringUtils.isBlank(signUpVO.getMobile())) {
                throw new WebApplicationException(Response.status(422).entity(errorsMap.get(1007)).build());
            } else {
                try (final UsersDAO usersDAO = dbi.open(UsersDAO.class)) {
                    if (usersDAO.countMobile(Optional.of(signUpVO.getMobile())) > 0) {
                        throw new WebApplicationException(Response.status(422).entity(errorsMap.get(1006)).build());
                    }
                }
            }
        } else {
            try (final UsersDAO usersDAO = dbi.open(UsersDAO.class)) {
                if (usersDAO.countEmail(Optional.of(signUpVO.getEmail())) > 0) {
                    throw new WebApplicationException(Response.status(422).entity(errorsMap.get(1005)).build());
                }
            }
        }

        try (final UsersDAO usersDAO = dbi.open(UsersDAO.class)) {
            return usersDAO.inTransaction((UsersDAO transactional, TransactionStatus status) -> {
                final Long uid = transactional.insertUser(Optional.fromNullable(signUpVO.getEmail()), Optional.fromNullable(signUpVO.getMobile()), Optional.of(PasswordHash.hash(signUpVO.getPassword())));
                transactional.insertRole(Optional.of(uid), Optional.of(Roles.ARTIST.toString()));
                return uid;
            });
        }
    }

}
