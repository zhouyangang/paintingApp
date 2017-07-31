package com.tumcca.api.db;

import java.util.List;

import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;
import org.skife.jdbi.v2.sqlobject.mixins.Transactional;
import org.skife.jdbi.v2.sqlobject.stringtemplate.UseStringTemplate3StatementLocator;
import org.skife.jdbi.v2.unstable.BindIn;

import com.google.common.base.Optional;
import com.tumcca.api.model.AlbumPO;
import com.tumcca.api.model.WorksPO;

/**
 * Title.
 * <p/>
 * Description.
 *
 * @author Neil 
 * @version 1.0
 * @since 2015-06-27
 */
@UseStringTemplate3StatementLocator
public interface AlbumDAO extends AutoCloseable, Transactional<AlbumDAO>{
    @SqlUpdate("INSERT INTO TUMCCA_ALBUM (AUTHOR, TITLE, DESCRIPTION) VALUES (:AUTHOR, :TITLE, :DESCRIPTION)")
    @GetGeneratedKeys
    Long insertAlbum(@Bind("AUTHOR") Optional<Long> author, @Bind("TITLE") Optional<String> TITLE, @Bind("DESCRIPTION") Optional<String> description);
    
    @SqlUpdate("UPDATE TUMCCA_ALBUM SET TITLE=:TITLE, DESCRIPTION=:DESCRIPTION WHERE ID=:ID AND AUTHOR=:AUTHOR")
    void updateAlbum(@Bind("ID") Optional<Long> id, @Bind("TITLE") Optional<String> TITLE, @Bind("DESCRIPTION") Optional<String> description, @Bind("AUTHOR") Optional<Long> author);
    
    @SqlUpdate("DELETE FROM TUMCCA_ALBUM WHERE ID=:ID AND AUTHOR=:AUTHOR")
    void deleteAlbum(@Bind("ID") Optional<Long> id, @Bind("AUTHOR") Optional<Long> author);
    
    @SqlQuery("SELECT ID, AUTHOR, TITLE, DESCRIPTION, CREATE_TIME FROM TUMCCA_ALBUM WHERE AUTHOR=:AUTHOR ORDER BY ID")
    @Mapper(AlbumPOMapper.class)
    List<AlbumPO> findByAuthor(@Bind("AUTHOR") Optional<Long> author);
    
    @SqlQuery("SELECT ID, AUTHOR, TITLE, DESCRIPTION, CREATE_TIME FROM TUMCCA_ALBUM WHERE ID=:ID AND AUTHOR=:AUTHOR")
    @Mapper(AlbumPOMapper.class)
    AlbumPO findById(@Bind("ID") Optional<Long> id, @Bind("AUTHOR") Optional<Long> author);
    
    @SqlUpdate("UPDATE TUMCCA_WORKS SET ALBUMID=:ALBUMID WHERE ID in (<WORKIDS>) AND AUTHOR=:AUTHOR")
    void moveAlbumWorks(@BindIn("WORKIDS") List<Long> worksId, @Bind("ALBUMID") Optional<Long> albumId, @Bind("AUTHOR") Optional<Long> author);
    
    @SqlQuery("SELECT ID, CATEGORY, AUTHOR, STATUS, CREATE_TIME, ALBUMID, TITLE, DESCRIPTION, TAGS FROM TUMCCA_WORKS WHERE STATUS = 1 AND AUTHOR=:AUTHOR AND ALBUMID=:ALBUMID ORDER BY ID LIMIT :START, :SIZE")
    @Mapper(WorksPOMapper.class)
    List<WorksPO> findByAlbumId(@Bind("ALBUMID") Optional<Long> albumId, @Bind("AUTHOR") Optional<Long> author, @Bind("START") Optional<Integer> start, @Bind("SIZE") Optional<Integer> size);
    
    @SqlQuery("SELECT COUNT(*) FROM TUMCCA_WORKS WHERE STATUS = 1 AND AUTHOR=:AUTHOR AND ALBUMID=:ALBUMID")
    Integer countWorks(@Bind("ALBUMID") Optional<Long> albumId, @Bind("AUTHOR") Optional<Long> author);
}
