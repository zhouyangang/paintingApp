package com.tumcca.api.resources;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.tumcca.api.db.CategoriesDAO;
import com.tumcca.api.db.TagsDAO;
import com.tumcca.api.db.UsersDAO;
import com.tumcca.api.model.*;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import io.dropwizard.auth.Auth;
import io.dropwizard.jersey.params.LongParam;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Title.
 * <p/>
 * Description.
 *
 * @author Bill Lv {@literal <billcc.lv@hotmail.com>}
 * @version 1.0
 * @since 2015-06-05
 */
@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
@Api("/misc")
public class MiscResource {
    static final Logger LOGGER = LoggerFactory.getLogger(MiscResource.class);

    final List<ErrorVO> errors;
    final Map<Integer, ErrorVO> errorsMap;
    final DBI dbi;

    public MiscResource(List<ErrorVO> errors, Map<Integer, ErrorVO> errorsMap, DBI dbi) {
        this.errors = errors;
        this.errorsMap = errorsMap;
        this.dbi = dbi;
    }

    @GET
    @Path("/misc/errors")
    @ApiOperation("Get error definitions")
    public Response getErrors() throws Exception {
        return Response.ok(errors).build();
    }

    @POST
    @Path("/tags/exists")
    @ApiOperation("Validate if the tag name has been registered")
    public Response tagExists(@Auth Principals principal, @Valid @ApiParam TagName tagName) throws Exception {
        assertAdmin(principal);

        try (final TagsDAO tagsDAO = dbi.open(TagsDAO.class)) {
            final boolean exists = tagsDAO.count(Optional.of(tagName.getName())) > 0;
            return Response.ok(exists).build();
        }
    }

    @POST
    @Path("/tags")
    @ApiOperation("Add tag")
    public Response addTag(@Auth Principals principal, @Valid @ApiParam TagName tagName) throws Exception {
        assertAdmin(principal);

        try (final TagsDAO tagsDAO = dbi.open(TagsDAO.class)) {
            final boolean exists = tagsDAO.count(Optional.of(tagName.getName())) > 0;
            if (exists) {
                throw new WebApplicationException(Response.status(422).entity(errorsMap.get(1014)).build());
            }
            final Long id = tagsDAO.insert(Optional.of(tagName.getName()));
            return Response.ok(ImmutableMap.of("id", id)).build();
        }
    }

    @DELETE
    @Path("/tags/{tagId}")
    @ApiOperation("Delete tag")
    public Response deleteTag(@Auth Principals principal, @PathParam("tagId") LongParam tagId) throws Exception {
        assertAdmin(principal);

        try (final TagsDAO tagsDAO = dbi.open(TagsDAO.class)) {
            tagsDAO.delete(Optional.of(tagId.get()));
            return Response.ok(ImmutableMap.of("id", tagId.get())).build();
        }
    }

    @GET
    @Path("/tags")
    @ApiOperation("Get all tags")
    public Response findAll() throws Exception {
        try (final TagsDAO tagsDAO = dbi.open(TagsDAO.class)) {
            final List<Tags> tags = tagsDAO.findAll();
            return Response.ok(tags).build();
        }
    }

    @POST
    @Path("/categories/exists")
    @ApiOperation("Validate if there is the same category name under the same parent category")
    public Response categoryExists(@Auth Principals principal, @Valid @ApiParam CategoriesInput categoryInput) throws Exception {
        assertAdmin(principal);

        final Boolean exists = categoryExists(categoryInput);
        return Response.ok(exists).build();
    }

    @POST
    @Path("/categories")
    @ApiOperation("Add category")
    public Response addCategory(@Auth Principals principal, @Valid @ApiParam CategoriesInput categoryInput) throws Exception {
        assertAdmin(principal);

        try (final CategoriesDAO categoriesDAO = dbi.open(CategoriesDAO.class)) {
            String parentPath = "/";
            if (categoryInput.getParentId() > 0) {
                final Categories parent = categoriesDAO.findById(Optional.of(categoryInput.getParentId()));
                parentPath = parent.getPath() + parent.getId() + "/";
            }
            if (categoriesDAO.count(Optional.of(parentPath), Optional.of(categoryInput.getName())) > 0) {
                throw new WebApplicationException(Response.status(Response.Status.CONFLICT).entity(errorsMap.get(1015)).build());
            }
            final Long id = categoriesDAO.insert(Optional.of(parentPath), Optional.of(categoryInput.getName()));
            return Response.ok(ImmutableMap.of("id", id)).build();
        }
    }

    @PUT
    @Path("/categories")
    @ApiOperation("Update category")
    public Response updateCategory(@Auth Principals principal, @Valid @ApiParam CategoriesUpdateInput categoryInput) throws Exception {
        assertAdmin(principal);

        try (final CategoriesDAO categoriesDAO = dbi.open(CategoriesDAO.class)) {
            final Categories category = categoriesDAO.findById(Optional.of(categoryInput.getId()));
            if (categoryInput.getName().equals(category.getName())) {
                return Response.ok(ImmutableMap.of("id", categoryInput.getId())).build();
            }
            if (categoriesDAO.count(Optional.of(category.getPath()), Optional.of(categoryInput.getName())) > 0) {
                throw new WebApplicationException(Response.status(Response.Status.CONFLICT).entity(errorsMap.get(1015)).build());
            }
            categoriesDAO.update(Optional.of(categoryInput.getId()), Optional.of(category.getPath()), Optional.of(categoryInput.getName()));
            return Response.ok(ImmutableMap.of("id", categoryInput.getId())).build();
        }
    }

    @DELETE
    @Path("/categories/{categoryId}")
    @ApiOperation("Delete category")
    public Response deleteCategory(@Auth Principals principal, @PathParam("categoryId") LongParam categoryId) throws Exception {
        assertAdmin(principal);

        try (final CategoriesDAO categoriesDAO = dbi.open(CategoriesDAO.class)) {
            final Categories category = categoriesDAO.findById(Optional.of(categoryId.get()));
            if (category == null) {
                throw new WebApplicationException(Response.status(404).entity(errorsMap.get(1008)).build());
            }
            final boolean existsChildren = categoriesDAO.countByPathLike(Optional.of(new StringBuilder("%/").append(categoryId.get()).append("/%").toString())) > 0;
            if (existsChildren) {
                throw new WebApplicationException(Response.status(422).entity(errorsMap.get(1016)).build());
            }
            categoriesDAO.delete(Optional.of(categoryId.get()));
            return Response.ok(ImmutableMap.of("id", categoryId.get())).build();
        }
    }

    @GET
    @Path("/categories/{parentId}")
    @ApiOperation("Load categories by parent category id")
    public Response loadCategories(@PathParam("parentId") LongParam parentId) throws Exception {
        final List<Categories> categories = loadCategories(parentId.get());
        return Response.ok(categories).build();
    }

    @GET
    @Path("/categories/{parentId}/recursively")
    @ApiOperation("Load categories by parent category id recursively")
    public Response loadCategoriesRecursively(@PathParam("parentId") LongParam parentId) throws Exception {
        final List<CategoriesVO> categoriesVOs = loadCategoriesVO(parentId.get());
        return Response.ok(categoriesVOs).build();
    }

    @GET
    @Path("/categories/all")
    @ApiOperation("Load all categories")
    public Response loadCategoriesAll() throws Exception {
        return Response.ok(loadCategoriesVO(0L)).build();
    }

    private List<CategoriesVO> loadCategoriesVO(Long id) throws Exception {
        final List<Categories> result = loadCategories(id);
        if (result == null || result.isEmpty()) {
            return new ArrayList<>(0);
        }
        List<CategoriesVO> vos = new ArrayList<>(result.size());
        for (Categories category : result) {
            final List<CategoriesVO> children = loadCategoriesVO(category.getId());
            vos.add(new CategoriesVO(category.getId(), category.getName(), children));
        }
        return vos;
    }

    private List<Categories> loadCategories(Long parentId) throws Exception {
        try (final CategoriesDAO categoriesDAO = dbi.open(CategoriesDAO.class)) {
            String parentPath = "/";
            if (parentId > 0) {
                final Categories parent = categoriesDAO.findById(Optional.of(parentId));
                parentPath = parent.getPath() + parent.getId() + "/";
            }
            return categoriesDAO.findByPath(Optional.of(parentPath));
        }
    }

    private void assertAdmin(Principals principal) throws Exception {
        try (final UsersDAO usersDAO = dbi.open(UsersDAO.class)) {
            final String userAuthority = usersDAO.getUserAuthority(Optional.of(principal.getUid()));
            if (!Roles.ADMIN.toString().equals(userAuthority)) {
                throw new WebApplicationException(Response.status(Response.Status.FORBIDDEN).entity(errorsMap.get(1013)).build());
            }
        }
    }

    private Boolean categoryExists(CategoriesInput categoryInput) throws Exception {
        try (final CategoriesDAO categoriesDAO = dbi.open(CategoriesDAO.class)) {
            String parentPath = "/";
            if (categoryInput.getParentId() > 0) {
                final Categories parent = categoriesDAO.findById(Optional.of(categoryInput.getParentId()));
                parentPath = parent.getPath() + parent.getId() + "/";
            }
            return categoriesDAO.count(Optional.of(parentPath), Optional.of(categoryInput.getName())) > 0;
        }
    }
}
