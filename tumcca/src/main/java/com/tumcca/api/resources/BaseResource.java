package com.tumcca.api.resources;

import java.util.Map;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import org.skife.jdbi.v2.DBI;

import com.google.common.base.Optional;
import com.tumcca.api.db.UsersDAO;
import com.tumcca.api.model.ErrorVO;
import com.tumcca.api.model.Principals;

public abstract class BaseResource {

    protected DBI dbi;

    protected Map<Integer, ErrorVO> errorsMap;
    
    public BaseResource(DBI dbi, Map<Integer, ErrorVO> errorsMap){
    	this.dbi = dbi;
    	this.errorsMap = errorsMap;
    }
	
    /**
     * Check the current user whether or not contain the profile.
     * @param principal
     * @param errorCode
     * @throws Exception
     */
    protected void assertProfile(Principals principal, Integer errorCode) throws Exception {
        try (final UsersDAO usersDAO = dbi.open(UsersDAO.class)) {
            if (usersDAO.findArtist(Optional.of(principal.getUid())) == null) {
                throw new WebApplicationException(Response.status(422).entity(errorsMap.get(errorCode)).build());
            }
        }
    }

}
