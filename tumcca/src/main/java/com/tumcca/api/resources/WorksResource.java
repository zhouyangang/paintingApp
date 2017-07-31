package com.tumcca.api.resources;

import io.dropwizard.auth.Auth;
import io.dropwizard.jersey.params.IntParam;
import io.dropwizard.jersey.params.LongParam;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Timestamp;
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
import com.tumcca.api.db.CategoriesDAO;
import com.tumcca.api.db.PicturesDAO;
import com.tumcca.api.db.UsersDAO;
import com.tumcca.api.db.WorksDAO;
import com.tumcca.api.model.Artists;
import com.tumcca.api.model.ErrorVO;
import com.tumcca.api.model.PictureSource;
import com.tumcca.api.model.Pictures;
import com.tumcca.api.model.Principals;
import com.tumcca.api.model.WorksAddVO;
import com.tumcca.api.model.WorksES;
import com.tumcca.api.model.WorksHit;
import com.tumcca.api.model.WorksHitResult;
import com.tumcca.api.model.WorksKeywords;
import com.tumcca.api.model.WorksPO;
import com.tumcca.api.model.WorksSearchPictureVO;
import com.tumcca.api.model.WorksSearchVO;
import com.tumcca.api.service.ElasticSearchService;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;

/**
 * Title.
 * <p/>
 * Description.
 *
 * @author Bill Lv {@literal <billcc.lv@hotmail.com>}
 * @version 1.0
 * @since 2015-06-16
 */
@Path("/api")
@Produces(MediaType.APPLICATION_JSON)
@Api("/works")
public class WorksResource {
    static final Logger LOGGER = LoggerFactory.getLogger(WorksResource.class);

    final DBI dbi;

    final Map<Integer, ErrorVO> errorsMap;

    final String serverId;

    final ObjectMapper mapper = new ObjectMapper();
    
    final Long homePageUid;
    
    final ElasticSearchService ecs;

    public WorksResource(DBI dbi, Map<Integer, ErrorVO> errorsMap, String serverId, ElasticSearchService ecs, Long homePageUid) {
        this.dbi = dbi;
        this.errorsMap = errorsMap;
        this.serverId = serverId;
        this.ecs = ecs;
        this.homePageUid = homePageUid;
    }

    @POST
    @Path("/works")
    @ApiOperation("Add works")
    public Response addWorks(@Auth Principals principal, @Valid @ApiParam WorksAddVO worksAddVO) throws Exception {
    	Artists artists = assertProfile(principal, 1020);
        assertCategory(worksAddVO.getCategory());

        final List<Long> pictures = worksAddVO.getPictures();
        assertPictures(pictures);

        try (final WorksDAO worksDAO = dbi.open(WorksDAO.class)) {
            worksDAO.begin();
            Long worksId;
            try {
                worksId = worksDAO.insertWorks(Optional.of(worksAddVO.getCategory())
                		, Optional.of(principal.getUid())
                		, Optional.of(1)
                		, Optional.of(worksAddVO.getAlbumId())
                		, Optional.of(worksAddVO.getTitle())
                		, Optional.of(worksAddVO.getDescription())
                		, Optional.of(worksAddVO.getTags()));
                worksDAO.insertWorksPictures(Optional.of(worksId), pictures);
            } catch (Exception e) {
                worksDAO.rollback();
                throw new WebApplicationException(e);
            }

            final WorksES worksES = new WorksES(
            		worksAddVO.getTags(), worksAddVO.getTitle(), worksAddVO.getDescription()
            		, new java.util.Date(System.currentTimeMillis()), principal.getUid(), artists.getTitle()
            		, Long.valueOf(0), Long.valueOf(0), worksAddVO.getCategory(), 1, worksAddVO.getAlbumId());
            if (ecs.indexWorks(worksES, worksId)) {
                worksDAO.commit();
            } else {
                worksDAO.rollback();
            }
            return Response.ok(ImmutableMap.of("id", worksId)).build();
        }
    }

    @PUT
    @Path("/works/{worksId}")
    @ApiOperation("Update works")
    public Response updateWorks(@Auth Principals principal, @PathParam("worksId") LongParam worksId,
                                @Valid @ApiParam WorksAddVO worksAddVO) throws Exception {
        assertProfile(principal, 1021);
        assertCategory(worksAddVO.getCategory());
        assertWorks(worksId.get(), 0);

        final List<Long> pictures = worksAddVO.getPictures();
        assertPictures(pictures);

        try (final WorksDAO worksDAO = dbi.open(WorksDAO.class)) {
            worksDAO.begin();
            try {
                worksDAO.updateWorks(Optional.of(worksId.get()), Optional.of(worksAddVO.getCategory())
                		, Optional.of(worksAddVO.getTitle())
                		, Optional.of(worksAddVO.getDescription())
                		, Optional.of(worksAddVO.getTags()));
                worksDAO.updateWorksPictures(Optional.of(worksId.get()), pictures);
            } catch (Exception e) {
                worksDAO.rollback();
                throw new WebApplicationException(e);
            }

            final WorksES worksES = new WorksES(
            		worksAddVO.getTags(), worksAddVO.getTitle(), worksAddVO.getDescription()
            		, new java.util.Date(System.currentTimeMillis()), null, null
            		, null, null, worksAddVO.getCategory(), null, null);
            if (ecs.mergeWorks(worksES, worksId.get())) {
                worksDAO.commit();
            } else {
                worksDAO.rollback();
            }
            return Response.ok(ImmutableMap.of("id", worksId.get())).build();
        }
    }

    @DELETE
    @Path("/works/trash/{worksId}")
    @ApiOperation("Trash works")
    public Response trashWorks(@Auth Principals principal, @PathParam("worksId") LongParam worksId) throws Exception {
        assertProfile(principal, 1022);
        assertWorks(worksId.get(), 0);
        updateWorksStatus(worksId.get(), 0);
        return Response.ok(ImmutableMap.of("id", worksId.get())).build();
    }

    @POST
    @Path("/works/restore/{worksId}")
    @ApiOperation("Restore works")
    public Response restoreWorks(@Auth Principals principal, @PathParam("worksId") LongParam worksId) throws Exception {
        assertProfile(principal, 1023);
        assertWorks(worksId.get(), 0);
        updateWorksStatus(worksId.get(), 1);
        return Response.ok(ImmutableMap.of("id", worksId.get())).build();
    }

    @DELETE
    @Path("/works/{worksId}")
    @ApiOperation("Delete works")
    public Response deleteWorks(@Auth Principals principal, @PathParam("worksId") LongParam worksId) throws Exception {
        assertProfile(principal, 1024);
        assertWorks(worksId.get(), null);
        final List<Long> pictureIds;
        try (final WorksDAO worksDAO = dbi.open(WorksDAO.class)) {
            final Integer status = worksDAO.getWorksStatus(Optional.of(worksId.get()));
            if (status == 1) {
                throw new WebApplicationException(Response.status(422).entity(errorsMap.get(1019)).build());
            }
            pictureIds = worksDAO.findPictureIds(Optional.of(worksId.get()));
            worksDAO.begin();
            try {
                worksDAO.deleteWorksPictures(Optional.of(worksId.get()));
                worksDAO.deleteWorks(Optional.of(worksId.get()));
            } catch (Exception e) {
                worksDAO.rollback();
                throw new WebApplicationException(e);
            }
            ecs.deleteWorks(worksId.get());
            worksDAO.commit();
        }
        try (PicturesDAO picturesDAO = dbi.open(PicturesDAO.class)) {
            picturesDAO.begin();
            try {
                for (Long picId : pictureIds) {
                    picturesDAO.deleteLocalLike(Optional.of(serverId + "/api/pictures/download/" + picId.toString() + "/%"));
                    picturesDAO.update(Optional.of(picId), Optional.of(1));
                }
            } catch (Exception e) {
                picturesDAO.rollback();
                throw new WebApplicationException(e);
            }
            picturesDAO.commit();
        }
        return Response.ok(ImmutableMap.of("id", worksId.get())).build();
    }
    
    @GET
    @Path("/works/{worksId}")
    @ApiOperation("Get works by Id")
    public Response getWorks(@PathParam("worksId") LongParam worksId) throws Exception {
        WorksPO worksPO;
        try (final WorksDAO worksDAO = dbi.open(WorksDAO.class)) {
            worksPO = worksDAO.findById(Optional.of(worksId.get()));
            
            List<Pictures> pics = worksDAO.findPictures(Optional.of(worksId.get()));
            WorksSearchPictureVO worksPic = new WorksSearchPictureVO();
            for(Pictures pic : pics){
            	worksPic.setId(pic.getId());
            	worksPic.setWidth(pic.getWidth());
            	worksPic.setHeight(pic.getHeight());
            }
            
            worksPO.setPicture(worksPic);
        }
        return Response.ok(worksPO).build();
    }
    
    @GET
    @Path("/works/homepage/fixed/width/{width}")
    @ApiOperation("Search works in the fixed part of Home Page ")
    public Response homePageFixed(@PathParam("width") IntParam width) throws Exception {
        if (width.get() < 1) {
            throw new WebApplicationException(Response.status(422).build());
        }
    	
    	try(final WorksDAO worksDAO = dbi.open(WorksDAO.class)){
    		WorksHitResult whr = ecs.searchByAuthor(homePageUid);
    		if(whr.getTotal() > 0){
	    		final List<WorksSearchVO> worksSearchVOs = new ArrayList<>(whr.getWorksHits().size());
	    		popWorksSearchVOS(worksSearchVOs, whr, worksDAO, width.get());
	            ImmutableMap<String, Object> returnMap = ImmutableMap.of("total", whr.getTotal(), "results", worksSearchVOs);
	            return Response.ok(returnMap).build();
    		}
            ImmutableMap<String, Object> returnMap = ImmutableMap.of("total", whr.getTotal());
            return Response.ok(returnMap).build();
    	}
    }
    
    @GET
    @Path("/works/homepage/page/{page}/size/{size}/width/{width}")
    @ApiOperation("Search works in Home Page")
    public Response homePage(@PathParam("page") IntParam page, @PathParam("size") IntParam size, @PathParam("width") IntParam width) throws Exception {
    	assertPageParams(page, size);
        if (width.get() < 1) {
            throw new WebApplicationException(Response.status(422).build());
        }
    	
    	try(final WorksDAO worksDAO = dbi.open(WorksDAO.class)){
    		WorksHitResult whr = ecs.searchAll((page.get() - 1) * size.get(), size.get(), homePageUid);
    		if(whr.getTotal() > 0){
	    		final List<WorksSearchVO> worksSearchVOs = new ArrayList<>(whr.getWorksHits().size());
	    		popWorksSearchVOS(worksSearchVOs, whr, worksDAO, width.get());
	            ImmutableMap<String, Object> returnMap = ImmutableMap.of("total", whr.getTotal(), "results", worksSearchVOs);
	            return Response.ok(returnMap).build();
    		}
            ImmutableMap<String, Object> returnMap = ImmutableMap.of("total", whr.getTotal());
            return Response.ok(returnMap).build();
    	}
    	
    }

    @POST
    @Path("/works/search")
    @ApiOperation("Search works by keywords")
    public Response searchByKeywords(@Valid @ApiParam WorksKeywords worksKeywords) throws Exception {
        if (worksKeywords.getPage() < 1) {
            throw new WebApplicationException(Response.status(422).entity(errorsMap.get(1025)).build());
        }
        if (worksKeywords.getSize() < 1 || worksKeywords.getSize() > 100) {
            throw new WebApplicationException(Response.status(422).entity(errorsMap.get(1026)).build());
        }
        if (worksKeywords.getWidth() < 1) {
            throw new WebApplicationException(Response.status(422).build());
        }
        
    	try(final WorksDAO worksDAO = dbi.open(WorksDAO.class)){
    		WorksHitResult whr = ecs.searchByKeyword(worksKeywords.getStart(), worksKeywords.getSize(), worksKeywords.getKeywords(), homePageUid);
    		if(whr.getTotal() > 0){
	    		final List<WorksSearchVO> worksSearchVOs = new ArrayList<>(whr.getWorksHits().size());
	    		popWorksSearchVOS(worksSearchVOs, whr, worksDAO, worksKeywords.getWidth());
	            ImmutableMap<String, Object> returnMap = ImmutableMap.of("total", whr.getTotal(), "results", worksSearchVOs);
	            return Response.ok(returnMap).build();
    		}
            ImmutableMap<String, Object> returnMap = ImmutableMap.of("total", whr.getTotal());
            return Response.ok(returnMap).build();
    	}
    }
    
    private void popWorksSearchVOS(List<WorksSearchVO> worksSearchVOs, WorksHitResult whr, WorksDAO worksDAO, Integer width){
		for(WorksHit wh : whr.getWorksHits()){
            List<Pictures> pics = worksDAO.findPictures(Optional.of(Long.valueOf(wh.getId())));
            WorksSearchPictureVO worksPic = new WorksSearchPictureVO();
            for(Pictures pic : pics){
            	Integer widthOrg = pic.getWidth();
            	Integer heightOrg = pic.getHeight();
            	if(widthOrg != null && widthOrg != 0){
                	pic.setWidth(width);
                	
                	BigDecimal decimal1 = new BigDecimal(width*heightOrg); 
                	BigDecimal decimal2 = new BigDecimal(widthOrg); 
                	BigDecimal decimal = decimal1.divideToIntegralValue(decimal2); 
                	Integer height = Integer.parseInt(decimal.setScale(0, RoundingMode.HALF_DOWN).toString()); 
                	pic.setHeight(height);
            	}
            	worksPic.setId(pic.getId());
            	worksPic.setWidth(pic.getWidth());
            	worksPic.setHeight(pic.getHeight());
            }
            worksSearchVOs.add(new WorksSearchVO(
            		  Long.valueOf(wh.getId())
            		, wh.getWorks().getCategory()
            		, wh.getWorks().getTags()
            		, wh.getWorks().getTitle()
            		, wh.getWorks().getDescription()
            		, wh.getWorks().getAuthorId()
            		, worksPic 
            		, new Timestamp(wh.getWorks().getUpdateDate().getTime())));
		}
    }

    private void assertPageParams(IntParam page, IntParam size) {
        if (page.get() < 1) {
            throw new WebApplicationException(Response.status(422).entity(errorsMap.get(1025)).build());
        }
        if (size.get() < 1 || size.get() > 100) {
            throw new WebApplicationException(Response.status(422).entity(errorsMap.get(1026)).build());
        }
    }

    private void assertPictures(List<Long> pictures) throws Exception {
        if (pictures.isEmpty()) {
            throw new WebApplicationException(Response.status(404).entity(errorsMap.get(1017)).build());
        }
        try (final PicturesDAO picturesDAO = dbi.open(PicturesDAO.class)) {
            for (Long picId : pictures) {
                if (picturesDAO.findPicture(Optional.of(picId), Optional.of(PictureSource.WORKS.toString())) == null) {
                    throw new WebApplicationException(Response.status(404).entity(errorsMap.get(1018)).build());
                }
            }
        }
    }

    private void assertCategory(Long categoryId) throws Exception {
        try (final CategoriesDAO categoriesDAO = dbi.open(CategoriesDAO.class)) {
            if (categoriesDAO.findById(Optional.of(categoryId)) == null) {
                throw new WebApplicationException(Response.status(404).entity(errorsMap.get(1027)).build());
            }
        }
    }

    private void updateWorksStatus(Long worksId, Integer status) throws Exception {
        try (final WorksDAO worksDAO = dbi.open(WorksDAO.class)) {
        	worksDAO.begin();
        	try{
        		worksDAO.updateWorksStatus(Optional.of(worksId), Optional.of(status));
        	} catch (Exception e) {
                worksDAO.rollback();
                throw new WebApplicationException(e);
            }
        	if(ecs.updateWorksStatus(status, worksId)){
        		worksDAO.commit();
        	} else {
        		worksDAO.rollback();
        	}
        }
    }

    private Artists assertProfile(Principals principal, Integer errorCode) throws Exception {
        try (final UsersDAO usersDAO = dbi.open(UsersDAO.class)) {
        	Artists artists = usersDAO.findArtist(Optional.of(principal.getUid())); 
            if (artists == null) {
                throw new WebApplicationException(Response.status(422).entity(errorsMap.get(errorCode)).build());
            }
            return artists;
        }
    }

    private void assertWorks(Long worksId, Integer status) throws Exception {
        try (final WorksDAO worksDAO = dbi.open(WorksDAO.class)) {
            final Integer worksStatus = worksDAO.getWorksStatus(Optional.of(worksId));
            if (worksStatus == null || worksStatus == status) {
                throw new WebApplicationException(Response.status(404).entity(errorsMap.get(1008)).build());
            }
        }
    }
    
    public static void main(String[] args){
//    	BigDecimal decimal1 = new BigDecimal(123*546); 
//    	BigDecimal decimal2 = new BigDecimal(98); 
//    	BigDecimal decimal = decimal1.divideToIntegralValue(decimal2); 
//    	Integer height = Integer.parseInt(decimal.setScale(0, RoundingMode.HALF_DOWN).toString()); 
//    	
//    	System.out.println(height);

//    	WorksES worksES = new WorksES(
//    			"书法#山水画#", "王羲之", "王羲之书法"
//        		, new java.util.Date(System.currentTimeMillis()), Long.valueOf(1), "title"
//        		, Long.valueOf(0), Long.valueOf(0), Long.valueOf(1), 1, Long.valueOf(0));
//    	
//    	final ObjectMapper mapper = new ObjectMapper();
//    	
//    	try {
//			String worksESJson = mapper.writeValueAsString(worksES);
//			System.out.println(worksESJson);
//		} catch (JsonProcessingException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
    }
}
