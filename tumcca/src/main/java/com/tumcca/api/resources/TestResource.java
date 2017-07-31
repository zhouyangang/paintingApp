package com.tumcca.api.resources;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.model.GetObjectRequest;
import com.aliyun.oss.model.ObjectMetadata;
import com.google.common.collect.ImmutableMap;
import com.tumcca.api.model.ErrorVO;
import com.tumcca.api.model.OSSConfig;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import io.dropwizard.jersey.params.LongParam;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

/**
 * Title.
 * <p/>
 * Description.
 *
 * @author Bill Lv {@literal <billcc.lv@hotmail.com>}
 * @version 1.0
 * @since 2015-05-29
 */
@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
@Api("/test")
public class TestResource {
    static final Logger LOGGER = LoggerFactory.getLogger(TestResource.class);

    final DBI dbi;

    final String uploadPath;

    final OSSConfig ossConfig;

    final Map<Integer, ErrorVO> errorsMap;

    public TestResource(DBI dbi, String uploadPath, OSSConfig ossConfig, Map<Integer, ErrorVO> errorsMap) {
        this.dbi = dbi;
        this.uploadPath = uploadPath;
        this.ossConfig = ossConfig;
        this.errorsMap = errorsMap;
    }

    @GET
    @Path("/photo-info/{photoId}")
    @ApiOperation("Test endpoint with path param")
    public Response getPhotoInfo(@PathParam("photoId") LongParam photoId) {
        LOGGER.info("Get photo info by {}", photoId);
        if (photoId.get() != 1) {
            throw new WebApplicationException(Response.status(404).entity(errorsMap.get(1008)).build());
        }
        return Response.ok(ImmutableMap.of("id", 1, "title", "Hello Tumcca")).build();
    }

    @GET
    @Path("/photos/{photoId}")
    @Produces("image/jpeg")
    @ApiOperation("Test endpoint to download")
    public Response getPhoto(@PathParam("photoId") LongParam photoId) {
        if (photoId.get() != 1) {
            throw new WebApplicationException(404);
        }
        final String bucketName = "tumcca";
        final String key = "test1.jpg";
        final OSSClient ossClient = new OSSClient(ossConfig.getEndpoint(), ossConfig.getAccessKeyId(), ossConfig.getSecretAccessKey());
        final String uqName = UUID.randomUUID().toString();
        final File localFile = Paths.get(uploadPath, uqName).toFile();
        downloadFile(ossClient, bucketName, key, localFile);
        if (!localFile.exists()) {
            throw new WebApplicationException(Response.status(404).entity(errorsMap.get(1008)).build());
        }

        return Response.ok(localFile)
                .header("Content-Disposition",
                        "attachment; filename=" + key).build();
    }

    private static ObjectMetadata downloadFile(OSSClient client, String bucketName, String key, File file)
            throws OSSException, ClientException {
        return client.getObject(new GetObjectRequest(bucketName, key),
                file);
    }
}
