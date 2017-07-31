package com.tumcca.api.db;

import com.google.common.base.Optional;
import com.tumcca.api.model.Pictures;
import com.tumcca.api.model.PicturesCache;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;
import org.skife.jdbi.v2.sqlobject.mixins.Transactional;

import java.util.Date;
import java.util.List;

/**
 * Title.
 * <p/>
 * Description.
 *
 * @author Bill Lv {@literal <billcc.lv@hotmail.com>}
 * @version 1.0
 * @since 2015-06-03
 */
public interface PicturesDAO extends AutoCloseable, Transactional<PicturesDAO> {
    @SqlUpdate("INSERT INTO TUMCCA_PICTURES (NAME, BUCKET_NAME, OSS_KEY, SOURCE, WIDTH, HEIGHT) VALUES (:NAME, :BUCKET_NAME, :OSS_KEY, :SOURCE, :WIDTH, :HEIGHT)")
    @GetGeneratedKeys
    Long insert(@Bind("NAME") Optional<String> name, @Bind("BUCKET_NAME") Optional<String> bucketName, @Bind("OSS_KEY") Optional<String> ossKey, @Bind("SOURCE") Optional<String> source, @Bind("WIDTH") Optional<Integer> width, @Bind("HEIGHT") Optional<Integer> height);

    @SqlUpdate("UPDATE TUMCCA_PICTURES SET STATUS = :STATUS WHERE ID = :ID")
    void update(@Bind("ID") Optional<Long> id, @Bind("STATUS") Optional<Integer> status);

    @SqlUpdate("DELETE FROM TUMCCA_PICTURES WHERE ID = :ID")
    void delete(@Bind("ID") Optional<Long> id);

    @SqlQuery("SELECT ID, NAME, BUCKET_NAME, OSS_KEY, CREATE_TIME, STATUS, WIDTH, HEIGHT FROM TUMCCA_PICTURES WHERE ID = :ID AND SOURCE = :SOURCE")
    @Mapper(PicturesMapper.class)
    Pictures findPicture(@Bind("ID") Optional<Long> id, @Bind("SOURCE") Optional<String> source);

    @SqlUpdate("INSERT INTO TUMCCA_PICTURES_CACHE (URI, STORAGE_NAME) VALUES (:URI, :STORAGE_NAME)")
    void insertLocal(@Bind("URI") Optional<String> uri, @Bind("STORAGE_NAME") Optional<String> storageName);

    @SqlQuery("SELECT URI, STORAGE_NAME, CREATE_TIME FROM TUMCCA_PICTURES_CACHE WHERE CREATE_TIME < :CREATE_TIME")
    @Mapper(PicturesCacheMapper.class)
    List<PicturesCache> findLocalCaches(@Bind("CREATE_TIME") Optional<Date> createTime);

    @SqlQuery("SELECT URI, STORAGE_NAME, CREATE_TIME FROM TUMCCA_PICTURES_CACHE WHERE URI = :URI")
    @Mapper(PicturesCacheMapper.class)
    PicturesCache findLocalByUri(@Bind("URI") Optional<String> uri);

    @SqlUpdate("DELETE FROM TUMCCA_PICTURES_CACHE WHERE URI = :URI")
    void deleteLocal(@Bind("URI") Optional<String> uri);

    @SqlUpdate("DELETE FROM TUMCCA_PICTURES_CACHE WHERE URI LIKE :URI")
    void deleteLocalLike(@Bind("URI") Optional<String> uri);
}
