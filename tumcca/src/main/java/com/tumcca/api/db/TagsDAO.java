package com.tumcca.api.db;

import com.google.common.base.Optional;
import com.tumcca.api.model.Tags;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
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
public interface TagsDAO extends AutoCloseable, Transactional<TagsDAO> {
    @SqlUpdate("INSERT INTO TUMCCA_WORKS_TAGS (NAME) VALUES (:NAME)")
    @GetGeneratedKeys
    Long insert(@Bind("NAME") Optional<String> name);

    @SqlQuery("SELECT ID, NAME FROM TUMCCA_WORKS_TAGS")
    @Mapper(TagsMapper.class)
    List<Tags> findAll();

    @SqlUpdate("DELETE FROM TUMCCA_WORKS_TAGS WHERE ID = :ID")
    void delete(@Bind("ID") Optional<Long> id);

    @SqlQuery("SELECT COUNT(*) FROM TUMCCA_WORKS_TAGS WHERE NAME = :NAME")
    Integer count(@Bind("NAME") Optional<String> name);
}
