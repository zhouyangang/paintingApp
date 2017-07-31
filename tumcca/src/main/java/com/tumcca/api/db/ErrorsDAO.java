package com.tumcca.api.db;

import com.tumcca.api.model.ErrorVO;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
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
 * @since 2015-06-05
 */
public interface ErrorsDAO extends AutoCloseable, Transactional<ErrorsDAO> {
    @SqlQuery("SELECT CODE, MESSAGE FROM TUMCCA_ERRORS")
    @Mapper(ErrorsMapper.class)
    List<ErrorVO> findAll();
}
