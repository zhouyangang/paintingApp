package com.tumcca.api.resources;

import io.dropwizard.auth.Auth;
import io.dropwizard.jersey.params.IntParam;
import io.dropwizard.jersey.params.LongParam;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.skife.jdbi.v2.DBI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import com.tumcca.api.db.AlbumDAO;
import com.tumcca.api.db.UsersDAO;
import com.tumcca.api.db.WorksDAO;
import com.tumcca.api.model.AlbumAddVO;
import com.tumcca.api.model.AlbumPO;
import com.tumcca.api.model.AlbumWorksMoveVO;
import com.tumcca.api.model.ErrorVO;
import com.tumcca.api.model.Pictures;
import com.tumcca.api.model.Principals;
import com.tumcca.api.model.WorksPO;
import com.tumcca.api.model.WorksSearchPictureVO;
import com.tumcca.api.model.WorksSearchVO;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

/**
 * Title.
 * <p/>
 * Description.
 *
 * @author Neil 
 * @version 1.0
 * @since 2015-06-27
 */
@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
@Api("/album")
public class AlbumResource {
	private static final Logger LOGGER = LoggerFactory.getLogger(AlbumResource.class);
	
    final DBI dbi;

    final Map<Integer, ErrorVO> errorsMap;

    final ObjectMapper mapper = new ObjectMapper();
    
    public AlbumResource(DBI dbi, Map<Integer, ErrorVO> errorsMap){
    	this.dbi = dbi;
    	this.errorsMap = errorsMap;
    }
    
    private void assertProfile(Principals principal, Integer errorCode) throws Exception {
        try (final UsersDAO usersDAO = dbi.open(UsersDAO.class)) {
            if (usersDAO.findArtist(Optional.of(principal.getUid())) == null) {
                throw new WebApplicationException(Response.status(422).entity(errorsMap.get(errorCode)).build());
            }
        }
    }
    
    private void assertAlbum(Principals principal, Long id) throws Exception {
        try (final AlbumDAO albumDAO = dbi.open(AlbumDAO.class)) {
            final AlbumPO album = albumDAO.findById(Optional.of(id), Optional.of(principal.getUid()));
            if (album == null) {
                throw new WebApplicationException(Response.status(404).entity(errorsMap.get(1008)).build());
            }
        }
    }
    
    @POST
    @Path("/album")
    @ApiOperation("Add album")
    public Response addAlbum(@Auth Principals principal,  @Valid @ApiParam AlbumAddVO albumAddVO) throws Exception{
    	assertProfile(principal, 1100);
    	try (final AlbumDAO albumDAO = dbi.open(AlbumDAO.class)) {
    		Long albumId = albumDAO.insertAlbum(Optional.of(principal.getUid()), Optional.of(albumAddVO.getTitle()), Optional.of(albumAddVO.getDescription()));
    		return Response.ok(ImmutableMap.of("id", albumId)).build();
    	}
    }
    
    @PUT
    @Path("/album/{albumId}")
    @ApiOperation("Update album")
    public Response updateAlbum(@Auth Principals principal, @PathParam("albumId") LongParam albumId, @Valid @ApiParam AlbumAddVO albumAddVO) throws Exception{
    	assertProfile(principal, 1101);
    	assertAlbum(principal, albumId.get());
    	
    	try (final AlbumDAO albumDAO = dbi.open(AlbumDAO.class)) {
    		albumDAO.updateAlbum(Optional.of(albumId.get()), Optional.of(albumAddVO.getTitle()), Optional.of(albumAddVO.getDescription()), Optional.of(principal.getUid()));
    	}
    	return Response.ok(ImmutableMap.of("id", albumId.get())).build();
    }
    
    @DELETE
    @Path("/album/{albumId}")
    @ApiOperation("Delete album")
    public Response deleteAlbum(@Auth Principals principal, @PathParam("albumId") LongParam albumId) throws Exception{
    	assertProfile(principal, 1102);
    	assertAlbum(principal, albumId.get());
    	
    	try (final AlbumDAO albumDAO = dbi.open(AlbumDAO.class)) {
    		albumDAO.deleteAlbum(Optional.of(albumId.get()), Optional.of(principal.getUid()));
    	}
    	return Response.ok(ImmutableMap.of("id", albumId.get())).build();
    }
    
    @GET
    @Path("/album/{albumId}")
    @ApiOperation("Get a album by Id")
    public Response getAlbum(@Auth Principals principal, @PathParam("albumId") LongParam albumId) throws Exception{
    	assertProfile(principal, 1103);
    	assertAlbum(principal, albumId.get());
    	
    	try (final AlbumDAO albumDAO = dbi.open(AlbumDAO.class)) {
    		AlbumPO album = albumDAO.findById(Optional.of(albumId.get()), Optional.of(principal.getUid()));
    		return Response.ok(album).build();
    	}
    }
    
    @GET
    @Path("/album")
    @ApiOperation("Get my albums")
    public Response loadMyAlbum(@Auth Principals principal) throws Exception{
    	assertProfile(principal, 1103);
    	
    	try (final AlbumDAO albumDAO = dbi.open(AlbumDAO.class)) {
    		List<AlbumPO> albums = albumDAO.findByAuthor(Optional.of(principal.getUid()));
    		return Response.ok(albums).build();
    	}
    }
    
    @POST
    @Path("/album/workses/move2/{albumId}")
    @ApiOperation("Move the selected workses to the specific album")
    public Response moveWorks(@Auth Principals principal, @PathParam("albumId") LongParam albumId, @Valid @ApiParam AlbumWorksMoveVO albumWorksMoveVO) throws Exception{
    	assertProfile(principal, 1104);
    	assertAlbum(principal, albumId.get());
    	try (final AlbumDAO albumDAO = dbi.open(AlbumDAO.class)) {
    		albumDAO.moveAlbumWorks(albumWorksMoveVO.getWorkses(), Optional.of(albumId.get()), Optional.of(principal.getUid()));
    	}
    	return Response.ok(ImmutableMap.of("id", albumId.get())).build();
    }
    
    @GET
    @Path("/album/{albumId}/workses/count")
    @ApiOperation("Get works count of a album")
    public Response getWorksCount(@Auth Principals principal, @PathParam("albumId") LongParam albumId) throws Exception{
    	assertProfile(principal, 1105);
    	
    	Integer worksCount = 0;
    	try (final AlbumDAO albumDAO = dbi.open(AlbumDAO.class);){
    		worksCount = albumDAO.countWorks(Optional.of(albumId.get()), Optional.of(principal.getUid()));
    	}
    	
    	return Response.ok(ImmutableMap.of("count", worksCount)).build();
    }
    
    @POST
    @Path("/album/{albumId}/workses/page/{page}/size/{size}/width/{width}")
    @ApiOperation("Get my workses in a album")
    public Response loadMyAlbumWorks(@Auth Principals principal, @PathParam("albumId") LongParam albumId
    		, @PathParam("page") IntParam page, @PathParam("size") IntParam size, @PathParam("width") IntParam width) throws Exception{
        if (page.get() < 1) {
            throw new WebApplicationException(Response.status(422).entity(errorsMap.get(1025)).build());
        }
        if (size.get() < 1 || size.get() > 100) {
            throw new WebApplicationException(Response.status(422).entity(errorsMap.get(1026)).build());
        }
        if (width.get() < 1) {
            throw new WebApplicationException(Response.status(422).build());
        }
    	assertProfile(principal, 1105);
    	
    	try (final AlbumDAO albumDAO = dbi.open(AlbumDAO.class); final WorksDAO worksDAO = dbi.open(WorksDAO.class)) {
    		List<WorksPO> worksPOs = albumDAO.findByAlbumId(Optional.of(albumId.get()), Optional.of(principal.getUid()), Optional.of((page.get() - 1) * size.get()), Optional.of(size.get()));
            
    		final List<WorksSearchVO> worksSearchVOs = new ArrayList<>(worksPOs.size());
            for (WorksPO w : worksPOs) {
                List<Pictures> pics = worksDAO.findPictures(Optional.of(w.getId()));
                WorksSearchPictureVO worksPic = new WorksSearchPictureVO();
                for(Pictures pic : pics){
                	Integer widthOrg = pic.getWidth();
                	Integer heightOrg = pic.getHeight();
                	if(widthOrg != null && widthOrg != 0){
	                	pic.setWidth(width.get());
	                	
	                	BigDecimal decimal1 = new BigDecimal(width.get()*heightOrg); 
	                	BigDecimal decimal2 = new BigDecimal(widthOrg); 
	                	BigDecimal decimal = decimal1.divideToIntegralValue(decimal2); 
	                	Integer height = Integer.parseInt(decimal.setScale(0, RoundingMode.HALF_DOWN).toString()); 
	                	
	                	pic.setHeight(height);
                	}
                	worksPic.setId(pic.getId());
                	worksPic.setWidth(pic.getWidth());
                	worksPic.setHeight(pic.getHeight());
                }
                worksSearchVOs.add(new WorksSearchVO(w.getId(), w.getCategory(), w.getTags(), w.getTitle()
                		, w.getDescription(), w.getAuthor(), worksPic , w.getCreateTime()));
            }
            ImmutableMap<String, Object> returnMap = ImmutableMap.of("total", worksPOs.size(), "results", worksSearchVOs);
            return Response.ok(returnMap).build();
    	}
    }
}
