package com.tumcca.api.resources;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSException;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.tumcca.api.db.PicturesDAO;
import com.tumcca.api.model.*;
import com.tumcca.api.util.ImageUtil;
import com.tumcca.api.util.OSSUtil;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

import io.dropwizard.auth.Auth;
import io.dropwizard.jersey.params.IntParam;
import io.dropwizard.jersey.params.LongParam;

import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.imageio.ImageIO;
import javax.servlet.annotation.MultipartConfig;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

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
@Api("/material")
@MultipartConfig(
		 maxFileSize=10485760       //10Mb max
		,fileSizeThreshold=524288   //512 Kb before buffering to disk
		,maxRequestSize=10240       //10Kb of meta data
)
public class MaterialResource {
    static final Logger LOGGER = LoggerFactory.getLogger(MaterialResource.class);

    final DBI dbi;

    final String uploadPath;

    final OSSClient ossClient;

    final String bucketName;

    final String trashBucket;

    final Map<Integer, ErrorVO> errorsMap;

    final Long cacheTimeout;

    final String serverId;

    public MaterialResource(DBI dbi, String uploadPath, OSSClient ossClient, String bucketName, String trashBucket, Map<Integer, ErrorVO> errorsMap, Long cacheTimeout, String serverId) {
        this.dbi = dbi;
        this.uploadPath = uploadPath;
        this.ossClient = ossClient;
        this.bucketName = bucketName;
        this.trashBucket = trashBucket;
        this.errorsMap = errorsMap;
        this.cacheTimeout = cacheTimeout;
        this.serverId = serverId;
    }

    @POST
    @Path("/avatars/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @ApiOperation("Upload user avatar")
    public Response uploadAvatar(@FormDataParam("avatar") InputStream uploadedInputStream,
                                 @FormDataParam("avatar") FormDataContentDisposition fileDetail) throws Exception {
        return Response.ok(ImmutableMap.of("id", uploadPicture(uploadedInputStream, fileDetail, PictureSource.AVATAR.toString()))).build();
    }

    @GET
    @Path("/avatars/download/{id}")
    @Produces("image/jpeg")
    @ApiOperation("Download user avatar")
    public Response getAvatar(@PathParam("id") LongParam picId) throws Exception {
        return getPicture(serverId + "/api/avatars/download/" + picId.toString() + "/", picId, PictureSource.AVATAR.toString());
    }

    @POST
    @Path("/pictures/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @ApiOperation("Upload works picture")
    public Response uploadWorks(@Auth Principals principal, @FormDataParam("works") InputStream uploadedInputStream,
                                @FormDataParam("works") FormDataContentDisposition fileDetail) throws Exception {
        return Response.ok(ImmutableMap.of("id", uploadPicture(uploadedInputStream, fileDetail, PictureSource.WORKS.toString()))).build();
    }

    @GET
    @Path("/pictures/download/{id}")
    @Produces("image/jpeg")
    @ApiOperation("Download works picture")
    public Response getWorks(@PathParam("id") LongParam picId) throws Exception {
        return getPicture(serverId + "/api/pictures/download/" + picId.toString() + "/", picId, PictureSource.WORKS.toString());
    }

    @GET
    @Path("/pictures/download/{id}/thumb/{targetWidth}/{targetHeight}")
    @Produces("image/jpeg")
    @ApiOperation("Download works picture thumbnails")
    public Response getWorksThumbnail(@PathParam("id") LongParam picId,
                                      @PathParam("targetWidth") IntParam targetWidth,
                                      @PathParam("targetHeight") IntParam targetHeight) throws Exception {
        return getThumbnail(PictureSource.WORKS, picId, targetWidth, targetHeight);
    }

    @GET
    @Path("/avatars/download/{id}/thumb/{targetWidth}/{targetHeight}")
    @Produces("image/jpeg")
    @ApiOperation("Download avatar thumbnails")
    public Response getAvatarThumbnail(@PathParam("id") LongParam picId,
                                       @PathParam("targetWidth") IntParam targetWidth,
                                       @PathParam("targetHeight") IntParam targetHeight) throws Exception {
        return getThumbnail(PictureSource.AVATAR, picId, targetWidth, targetHeight);
    }

    private Response getThumbnail(PictureSource pictureSource, LongParam picId, IntParam targetWidth, IntParam targetHeight) throws Exception {
        Pictures picture;
        try (PicturesDAO picturesDAO = dbi.open(PicturesDAO.class)) {
            picture = picturesDAO.findPicture(Optional.of(picId.get()), Optional.of(pictureSource.toString()));
        }
        final StringBuilder uriBuilder = new StringBuilder();
        uriBuilder.append(serverId);
        switch (pictureSource) {
            case AVATAR:
                uriBuilder.append("/api/avatars/download/");
                break;
            case WORKS:
                uriBuilder.append("/api/pictures/download/");
                break;
        }
        uriBuilder.append(picId.toString()).append("/thumb/").append(targetWidth.toString()).append("/").append(targetHeight.toString()).append("/");
        final String uri = uriBuilder.toString();
        final Optional<File> fileCached = hitCache(uri, picture);
        final String newFileName = picture.getName().substring(0, picture.getName().lastIndexOf('.')) + "-" + targetWidth.get() + "x" + targetHeight.get() + ".jpg";
        if (fileCached.isPresent()) {
            return Response.ok(fileCached.get())
                    .header("Content-Disposition",
                            "attachment; filename=" + newFileName + "; size=" + fileCached.get().length()).build();
        }

        final String uqName = UUID.randomUUID().toString();
        final File localFile = Paths.get(uploadPath, uqName).toFile();
        try {
            OSSUtil.downloadFile(ossClient, bucketName, picture.getOssKey(), localFile);
        } catch (OSSException | ClientException e) {
            LOGGER.error("the picture may be removed from OSS", e);
            try (PicturesDAO picturesDAO = dbi.open(PicturesDAO.class)) {
            	picturesDAO.begin();
            	try{
            		picturesDAO.delete(Optional.of(picId.get()));
            		picturesDAO.commit();
            	} catch(Exception exp){
            		picturesDAO.rollback();
            		throw new WebApplicationException(exp);
            	}
            }
            throw new WebApplicationException(404);
        }

        final BufferedImage bufferedImage = ImageIO.read(localFile);

        final BufferedImage thumbnail = ImageUtil.resizeImage(bufferedImage, targetWidth.get(), targetHeight.get());

        final String thumbnailUniqueName = UUID.randomUUID().toString();
        final java.nio.file.Path thumbnailPath = Paths.get(uploadPath, thumbnailUniqueName);
        final File thumbnailFile = thumbnailPath.toFile();
        if (!thumbnailFile.exists()) {
            thumbnailFile.createNewFile();
        }
        ImageIO.write(thumbnail, "jpeg", thumbnailFile);
        try (PicturesDAO picturesDAO = dbi.open(PicturesDAO.class)) {
        	picturesDAO.begin();
        	try{
        		picturesDAO.insertLocal(Optional.of(uri), Optional.of(thumbnailUniqueName));
        		picturesDAO.commit();
        	} catch(Exception e){
        		picturesDAO.rollback();
        		throw new WebApplicationException(e);
        	}
        }

        localFile.delete();
        return Response.ok(thumbnailFile)
                .header("Content-Disposition",
                        "attachment; filename=" + newFileName + "; size=" + thumbnailFile.length()).build();
    }

    @DELETE
    @Path("/pictures/trash/{id}")
    @ApiOperation("Trash works picture")
    public Response trashWorks(@Auth Principals principal, @PathParam("id") LongParam picId) throws Exception {
        Pictures picture;
        try (PicturesDAO picturesDAO = dbi.open(PicturesDAO.class)) {
            picture = picturesDAO.findPicture(Optional.of(picId.get()), Optional.of(PictureSource.WORKS.toString()));
        }
        if (picture == null) {
            throw new WebApplicationException(404);
        }
        if (picture.getStatus() == 1) {
            throw new WebApplicationException(404);
        }
        ossClient.copyObject(picture.getBucketName(), picture.getOssKey(), trashBucket, picture.getOssKey());
        ossClient.deleteObject(picture.getBucketName(), picture.getOssKey());
        try (PicturesDAO picturesDAO = dbi.open(PicturesDAO.class)) {
            picturesDAO.deleteLocalLike(Optional.of(serverId + "/api/pictures/download/" + picId.toString() + "/%"));
            picturesDAO.update(Optional.of(picId.get()), Optional.of(1));
        }
        return Response.ok(ImmutableMap.of("id", picId.get())).build();
    }

    @POST
    @Path("/pictures/restore/{id}")
    @ApiOperation("Restore works picture")
    public Response restoreWorks(@Auth Principals principal, @PathParam("id") LongParam picId) throws Exception {
        Pictures picture;
        try (PicturesDAO picturesDAO = dbi.open(PicturesDAO.class)) {
            picture = picturesDAO.findPicture(Optional.of(picId.get()), Optional.of(PictureSource.WORKS.toString()));
        }
        if (picture == null) {
            throw new WebApplicationException(404);
        }
        if (picture.getStatus() == 0) {
            return Response.status(422).entity(errorsMap.get(1010)).build();
        }
        ossClient.copyObject(trashBucket, picture.getOssKey(), picture.getBucketName(), picture.getOssKey());
        ossClient.deleteObject(trashBucket, picture.getOssKey());
        try (PicturesDAO picturesDAO = dbi.open(PicturesDAO.class)) {
            picturesDAO.update(Optional.of(picId.get()), Optional.of(0));
        }
        return Response.ok(ImmutableMap.of("id", picId.get())).build();
    }

    @DELETE
    @Path("/pictures/remove/{id}")
    @ApiOperation("Remove works picture from trash")
    public Response deleteWorks(@Auth Principals principal, @PathParam("id") LongParam picId) throws Exception {
        Pictures picture;
        try (PicturesDAO picturesDAO = dbi.open(PicturesDAO.class)) {
            picture = picturesDAO.findPicture(Optional.of(picId.get()), Optional.of(PictureSource.WORKS.toString()));
        }
        if (picture == null) {
            throw new WebApplicationException(404);
        }
        if (picture.getStatus() == 0) {
            return Response.status(422).entity(errorsMap.get(1009)).build();
        }
        ossClient.deleteObject(trashBucket, picture.getOssKey());
        try (PicturesDAO picturesDAO = dbi.open(PicturesDAO.class)) {
            picturesDAO.deleteLocalLike(Optional.of(serverId + "/api/pictures/download/" + picId.toString() + "/%"));
            picturesDAO.delete(Optional.of(picId.get()));
        }
        return Response.ok(ImmutableMap.of("id", picId.get())).build();
    }

    private Long uploadPicture(final InputStream uploadedInputStream, final FormDataContentDisposition fileDetail, final String source) throws Exception {
        final String uniqueName = UUID.randomUUID().toString();
        final java.nio.file.Path filePath = Paths.get(uploadPath, uniqueName);
        Files.copy(uploadedInputStream, filePath);
        final String newUniqueName = UUID.randomUUID().toString();
        final java.nio.file.Path newFilePath = Paths.get(uploadPath, newUniqueName);
        ImageIO.write(ImageIO.read(filePath.toFile()), "jpeg", newFilePath.toFile());
        filePath.toFile().delete();
        OSSUtil.uploadFile(ossClient, bucketName, newUniqueName, newFilePath.toFile(), "image/jpeg");
    	Integer[] dimensions = ImageUtil.getDimensions(newFilePath.toFile());
        newFilePath.toFile().delete();
        final String fileName = fileDetail.getFileName();
        final String newFileName = fileName.substring(0, fileName.lastIndexOf('.')) + ".jpg";
        try (final PicturesDAO picturesDAO = dbi.open(PicturesDAO.class)) {
            return picturesDAO.insert(Optional.of(newFileName), Optional.of(bucketName), Optional.of(newUniqueName), Optional.of(source), Optional.of(dimensions[0]), Optional.of(dimensions[1]));
        }
    }

    private Optional<File> hitCache(final String uri, final Pictures picture) throws Exception {
        if (picture == null) {
            throw new WebApplicationException(404);
        }
        if (picture.getStatus() == 1) {
            throw new WebApplicationException(404);
        }

        final PicturesCache picturesCache;
        try (PicturesDAO picturesDAO = dbi.open(PicturesDAO.class)) {
            picturesCache = picturesDAO.findLocalByUri(Optional.of(uri));
        }
        if (picturesCache != null) {
            final Date deadline = new Date(System.currentTimeMillis() - cacheTimeout * 1000);
            final File fileCached = Paths.get(uploadPath, picturesCache.getStorageName()).toFile();
            if (!fileCached.exists()) {
                try (PicturesDAO picturesDAO = dbi.open(PicturesDAO.class)) {
                	picturesDAO.begin();
                	try{
                		picturesDAO.deleteLocal(Optional.of(uri));
                    	picturesDAO.commit();
                	} catch(Exception e){
                		picturesDAO.rollback();
                		throw new WebApplicationException(e);
                	}
                }
                return Optional.absent();
            }
            if (deadline.before(picturesCache.getCreateTime())) {
                return Optional.of(fileCached);
            } else {
                fileCached.delete();
                try (PicturesDAO picturesDAO = dbi.open(PicturesDAO.class)) {
                	picturesDAO.begin();
                	try{
                		picturesDAO.deleteLocal(Optional.of(uri));
                    	picturesDAO.commit();
                	} catch(Exception e){
                		picturesDAO.rollback();
                		throw new WebApplicationException(e);
                	}
                }
            }
        }
        return Optional.absent();
    }

    private Response getPicture(String uri, LongParam picId, String source) throws Exception {
        Pictures picture;
        try (PicturesDAO picturesDAO = dbi.open(PicturesDAO.class)) {
            picture = picturesDAO.findPicture(Optional.of(picId.get()), Optional.of(source));
        }
        final Optional<File> fileCached = hitCache(uri, picture);
        if (fileCached.isPresent()) {
            return Response.ok(fileCached.get())
                    .header("Content-Disposition",
                            "attachment; filename=" + picture.getName() + "; size=" + fileCached.get().length()).build();
        }

        final String uqName = UUID.randomUUID().toString();
        final File localFile = Paths.get(uploadPath, uqName).toFile();
        try {
            OSSUtil.downloadFile(ossClient, bucketName, picture.getOssKey(), localFile);
        } catch (OSSException | ClientException e) {
            LOGGER.error("the picture may be removed from OSS", e);
            try (PicturesDAO picturesDAO = dbi.open(PicturesDAO.class)) {
            	picturesDAO.begin();
            	try{
            		picturesDAO.delete(Optional.of(picId.get()));
            		picturesDAO.commit();
            	} catch(Exception exp){
            		picturesDAO.rollback();
            		throw new WebApplicationException(exp);
            	}
            }
            throw new WebApplicationException(404);
        }
        try (PicturesDAO picturesDAO = dbi.open(PicturesDAO.class)) {
        	picturesDAO.begin();
        	try{
        		picturesDAO.insertLocal(Optional.of(uri), Optional.of(uqName));
        		picturesDAO.commit();
        	} catch(Exception e){
        		picturesDAO.rollback();
        		throw new WebApplicationException(e);
        	}
        }
        return Response.ok(localFile)
                .header("Content-Disposition",
                        "attachment; filename=" + picture.getName() + "; size=" + localFile.length()).build();
    }
}
