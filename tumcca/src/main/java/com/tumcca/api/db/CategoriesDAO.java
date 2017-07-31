package com.tumcca.api.db;

import com.google.common.base.Optional;
import com.tumcca.api.model.Categories;
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
public interface CategoriesDAO extends AutoCloseable, Transactional<CategoriesDAO> {
    @SqlUpdate("INSERT INTO TUMCCA_WORKS_CATEGORIES (PATH, NAME) VALUES (:PATH, :NAME)")
    @GetGeneratedKeys
    Long insert(@Bind("PATH") Optional<String> path, @Bind("NAME") Optional<String> name);

    @SqlUpdate("DELETE FROM TUMCCA_WORKS_CATEGORIES WHERE ID = :ID")
    void delete(@Bind("ID") Optional<Long> id);

    @SqlUpdate("UPDATE TUMCCA_WORKS_CATEGORIES SET PATH = :PATH, NAME = :NAME WHERE ID = :ID")
    void update(@Bind("ID") Optional<Long> id, @Bind("PATH") Optional<String> path, @Bind("NAME") Optional<String> name);

    @SqlQuery("SELECT ID, PATH, NAME FROM TUMCCA_WORKS_CATEGORIES WHERE PATH = :PATH")
    @Mapper(CategoriesMapper.class)
    List<Categories> findByPath(@Bind("PATH") Optional<String> path);

    @SqlQuery("SELECT ID, PATH, NAME FROM TUMCCA_WORKS_CATEGORIES WHERE ID = :ID")
    @Mapper(CategoriesMapper.class)
    Categories findById(@Bind("ID") Optional<Long> id);

    @SqlQuery("SELECT COUNT(*) FROM TUMCCA_WORKS_CATEGORIES WHERE PATH = :PATH AND NAME = :NAME")
    Integer count(@Bind("PATH") Optional<String> path, @Bind("NAME") Optional<String> name);

    @SqlQuery("SELECT COUNT(*) FROM TUMCCA_WORKS_CATEGORIES WHERE PATH LIKE :PATH")
    Integer countByPathLike(@Bind("PATH") Optional<String> path);
    
    @SqlQuery("SELECT ID, PATH, NAME FROM TUMCCA_WORKS_CATEGORIES WHERE ID IN (SELECT CATEGORY FROM TUMCCA_FOLLOW_CATEGORY WHERE FOLLOWER=:FOLLOWER)")
    @Mapper(CategoriesMapper.class)
    List<Categories> findByUid(@Bind("FOLLOWER") Optional<Long> follower);
}
