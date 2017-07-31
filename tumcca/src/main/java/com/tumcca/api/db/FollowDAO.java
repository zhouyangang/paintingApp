package com.tumcca.api.db;

import com.google.common.base.Optional;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.mixins.Transactional;

/**
 * Title.
 * <p/>
 * Description.
 *
 * @author Bill Lv {@literal <billcc.lv@hotmail.com>}
 * @version 1.0
 * @since 2015-06-22
 */
public interface FollowDAO extends AutoCloseable, Transactional<FollowDAO> {
	//Follow 
    @SqlUpdate("INSERT INTO TUMCCA_FOLLOW (FOLLOWER, TO_FOLLOW) VALUES (:FOLLOWER, :TO_FOLLOW)")
    void insert(@Bind("FOLLOWER") Optional<Long> follower, @Bind("TO_FOLLOW") Optional<Long> toFollow);

    @SqlQuery("SELECT COUNT(*) FROM TUMCCA_FOLLOW WHERE FOLLOWER = :FOLLOWER AND TO_FOLLOW = :TO_FOLLOW")
    Integer count(@Bind("FOLLOWER") Optional<Long> follower, @Bind("TO_FOLLOW") Optional<Long> toFollow);

    @SqlQuery("SELECT COUNT(*) FROM TUMCCA_FOLLOW WHERE TO_FOLLOW = :TO_FOLLOW")
    Integer countFollowers(@Bind("TO_FOLLOW") Optional<Long> toFollow);

    @SqlQuery("SELECT COUNT(*) FROM TUMCCA_FOLLOW WHERE FOLLOWER = :FOLLOWER")
    Integer countToFollow(@Bind("FOLLOWER") Optional<Long> follower);

    @SqlUpdate("DELETE FROM TUMCCA_FOLLOW WHERE FOLLOWER = :FOLLOWER AND TO_FOLLOW = :TO_FOLLOW")
    void delete(@Bind("FOLLOWER") Optional<Long> follower, @Bind("TO_FOLLOW") Optional<Long> toFollow);
    
    //Follow Category
    @SqlUpdate("INSERT INTO TUMCCA_FOLLOW_CATEGORY (FOLLOWER, CATEGORY) VALUES (:FOLLOWER, :CATEGORY)")
    void insertCategory(@Bind("FOLLOWER") Optional<Long> follower, @Bind("CATEGORY") Optional<Long> category);
    
    @SqlQuery("SELECT COUNT(*) FROM TUMCCA_FOLLOW_CATEGORY WHERE FOLLOWER = :FOLLOWER AND CATEGORY = :CATEGORY")
    Integer countCategory(@Bind("FOLLOWER") Optional<Long> follower, @Bind("CATEGORY") Optional<Long> category);
    
    @SqlUpdate("DELETE FROM TUMCCA_FOLLOW_CATEGORY WHERE FOLLOWER = :FOLLOWER AND CATEGORY = :CATEGORY")
    void deleteCategory(@Bind("FOLLOWER") Optional<Long> follower, @Bind("CATEGORY") Optional<Long> category);
}
