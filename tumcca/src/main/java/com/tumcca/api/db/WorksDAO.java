package com.tumcca.api.db;

import com.google.common.base.Optional;
import com.tumcca.api.model.Pictures;
import com.tumcca.api.model.WorksPO;

import org.skife.jdbi.v2.sqlobject.*;
import org.skife.jdbi.v2.sqlobject.customizers.BatchChunkSize;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;
import org.skife.jdbi.v2.sqlobject.mixins.Transactional;

import java.util.List;

/**
 * Title.
 * <p/>
 * Description.
 *
 * @author Bill Lv {@literal <billcc.lv@hotmail.com>}
 * @version 1.0
 * @since 2015-06-16
 */
public interface WorksDAO extends AutoCloseable, Transactional<WorksDAO> {
    @SqlUpdate("INSERT INTO TUMCCA_WORKS (CATEGORY, AUTHOR, STATUS, ALBUMID, TITLE, DESCRIPTION, TAGS) "
    		+ " VALUES (:CATEGORY, :AUTHOR, :STATUS, :ALBUMID, :TITLE, :DESCRIPTION, :TAGS)")
    @GetGeneratedKeys
    Long insertWorks(@Bind("CATEGORY") Optional<Long> category, @Bind("AUTHOR") Optional<Long> author
    		, @Bind("STATUS") Optional<Integer> status, @Bind("ALBUMID") Optional<Long> albumId
    		, @Bind("TITLE") Optional<String> title
    		, @Bind("DESCRIPTION") Optional<String> description
    		, @Bind("TAGS") Optional<String> tags);

    @SqlBatch("INSERT INTO TUMCCA_WORKS_PICTURES (WORKS, PICTURE) VALUES (:WORKS, :PICTURE)")
    @BatchChunkSize(100)
    void insertWorksPictures(@Bind("WORKS") Optional<Long> works, @Bind("PICTURE") List<Long> pictures);

    @SqlUpdate("UPDATE TUMCCA_WORKS SET CATEGORY = :CATEGORY, TITLE=:TITLE, DESCRIPTION=:DESCRIPTION, TAGS=:TAGS WHERE ID = :ID")
    void updateWorks(@Bind("ID") Optional<Long> id, @Bind("CATEGORY") Optional<Long> category
    		, @Bind("TITLE") Optional<String> title
    		, @Bind("DESCRIPTION") Optional<String> description
    		, @Bind("TAGS") Optional<String> tags);

    @SqlBatch("UPDATE TUMCCA_WORKS_PICTURES SET PICTURE = :PICTURE WHERE WORKS = :WORKS")
    @BatchChunkSize(100)
    void updateWorksPictures(@Bind("WORKS") Optional<Long> works, @Bind("PICTURE") List<Long> pictures);

    @SqlUpdate("UPDATE TUMCCA_WORKS SET STATUS = :STATUS WHERE ID = :ID")
    void updateWorksStatus(@Bind("ID") Optional<Long> id, @Bind("STATUS") Optional<Integer> status);

    @SqlUpdate("DELETE FROM TUMCCA_WORKS WHERE ID = :ID")
    void deleteWorks(@Bind("ID") Optional<Long> id);

    @SqlUpdate("DELETE FROM TUMCCA_WORKS_PICTURES WHERE WORKS = :WORKS")
    void deleteWorksPictures(@Bind("WORKS") Optional<Long> works);

    @SqlQuery("SELECT STATUS FROM TUMCCA_WORKS WHERE ID = :ID")
    Integer getWorksStatus(@Bind("ID") Optional<Long> id);

    @SqlQuery("SELECT PICTURE FROM TUMCCA_WORKS_PICTURES WHERE WORKS = :WORKS")
    List<Long> findPictureIds(@Bind("WORKS") Optional<Long> works);
    
    @SqlQuery("SELECT t.ID, t.NAME, t.BUCKET_NAME, t.OSS_KEY, t.CREATE_TIME, t.STATUS, t.WIDTH, t.HEIGHT FROM TUMCCA_PICTURES t,TUMCCA_WORKS_PICTURES t1 WHERE t1.WORKS = :WORKS and t.ID=t1.PICTURE")
    @Mapper(PicturesMapper.class)
    List<Pictures> findPictures(@Bind("WORKS") Optional<Long> works);

    @SqlQuery("SELECT ID, CATEGORY, AUTHOR, STATUS, CREATE_TIME, ALBUMID, TITLE, DESCRIPTION, TAGS FROM TUMCCA_WORKS WHERE ID = :ID AND STATUS = 1")
    @Mapper(WorksPOMapper.class)
    WorksPO findById(@Bind("ID") Optional<Long> id);

    @SqlQuery("SELECT ID, CATEGORY, AUTHOR, STATUS, CREATE_TIME, ALBUMID, TITLE, DESCRIPTION, TAGS FROM TUMCCA_WORKS WHERE STATUS = 1 ORDER BY CREATE_TIME DESC LIMIT :START, :SIZE")
    @Mapper(WorksPOMapper.class)
    List<WorksPO> findByPage(@Bind("START") Optional<Integer> start, @Bind("SIZE") Optional<Integer> size);

    @SqlQuery("SELECT ID, CATEGORY, AUTHOR, STATUS, CREATE_TIME, ALBUMID, TITLE, DESCRIPTION, TAGS FROM TUMCCA_WORKS WHERE STATUS = 1 AND AUTHOR = :AUTHOR ORDER BY CREATE_TIME DESC LIMIT :START, :SIZE")
    @Mapper(WorksPOMapper.class)
    List<WorksPO> findByAuthor(@Bind("AUTHOR") Optional<Long> author, @Bind("START") Optional<Integer> start, @Bind("SIZE") Optional<Integer> size);

    @SqlQuery("SELECT COUNT(*) FROM TUMCCA_WORKS WHERE ID = :ID")
    Integer countWorks(@Bind("ID") Optional<Long> id);
}
