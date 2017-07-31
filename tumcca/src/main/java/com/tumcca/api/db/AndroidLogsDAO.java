package com.tumcca.api.db;

import com.google.common.base.Optional;
import com.tumcca.api.model.AndroidLogsVO;
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
 * @since 2015-06-06
 */
public interface AndroidLogsDAO extends AutoCloseable, Transactional<AndroidLogsDAO> {
    @SqlQuery("SELECT DESCRIPTION, TS FROM TUMCCA_ANDROID_LOGS ORDER BY ID DESC LIMIT :SIZE")
    @Mapper(AndroidLogsMapper.class)
    List<AndroidLogsVO> findAll(@Bind("SIZE") Optional<Integer> size);

    @SqlUpdate("INSERT INTO TUMCCA_ANDROID_LOGS (DESCRIPTION) VALUES (:DESCRIPTION)")
    @GetGeneratedKeys
    Long insert(@Bind("DESCRIPTION") Optional<String> description);
}
