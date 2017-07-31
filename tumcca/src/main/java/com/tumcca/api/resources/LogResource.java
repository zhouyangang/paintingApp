package com.tumcca.api.resources;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.tumcca.api.db.AndroidLogsDAO;
import com.tumcca.api.model.AndroidLogInput;
import com.tumcca.api.model.AndroidLogsVO;
import com.tumcca.api.model.ErrorVO;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import io.dropwizard.jersey.params.IntParam;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

/**
 * Title.
 * <p/>
 * Description.
 *
 * @author Bill Lv {@literal <billcc.lv@hotmail.com>}
 * @version 1.0
 * @since 2015-06-06
 */
@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
@Api("/log")
public class LogResource {
    static final Logger LOGGER = LoggerFactory.getLogger(LogResource.class);

    final DBI dbi;

    final Map<Integer, ErrorVO> errorsMap;

    public LogResource(DBI dbi, Map<Integer, ErrorVO> errorsMap) {
        this.dbi = dbi;
        this.errorsMap = errorsMap;
    }

    @POST
    @Path("/logs/android")
    @ApiOperation("Add android log")
    public Response addLog(@Valid @ApiParam AndroidLogInput androidLogInput) throws Exception {
        try (final AndroidLogsDAO androidLogsDAO = dbi.open(AndroidLogsDAO.class)) {
            final Long id = androidLogsDAO.insert(Optional.of(androidLogInput.getDescription()));
            return Response.ok(ImmutableMap.of("id", id)).build();
        }
    }

    @GET
    @Path("/logs/android/size/{size}")
    @ApiOperation("Find android logs limit by size")
    public Response findLogs(@PathParam("size") IntParam size) throws Exception {
        if (size.get() > 1000) {
            return Response.status(Response.Status.NOT_ACCEPTABLE).entity(errorsMap.get(1011)).build();
        }
        try (final AndroidLogsDAO androidLogsDAO = dbi.open(AndroidLogsDAO.class)) {
            final List<AndroidLogsVO> logs = androidLogsDAO.findAll(Optional.of(size.get()));
            return Response.ok(logs).build();
        }
    }
}
