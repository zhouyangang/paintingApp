package com.tumcca.api.db;

import com.google.common.base.Optional;
import com.tumcca.api.model.Artists;
import com.tumcca.api.model.ContactVO;
import com.tumcca.api.model.UserVO;
import com.tumcca.api.model.Users;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.GetGeneratedKeys;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
import org.skife.jdbi.v2.sqlobject.customizers.Mapper;
import org.skife.jdbi.v2.sqlobject.mixins.Transactional;

/**
 * Title.
 * <p/>
 * Description.
 *
 * @author Bill Lv {@literal <billcc.lv@hotmail.com>}
 * @version 1.0
 * @since 2015-06-03
 */
public interface UsersDAO extends AutoCloseable, Transactional<UsersDAO> {
    @SqlUpdate("INSERT INTO TUMCCA_USERS (EMAIL, MOBILE, PASSWORD_HASH) VALUES (:EMAIL, :MOBILE, :PASSWORD_HASH)")
    @GetGeneratedKeys
    Long insertUser(@Bind("EMAIL") Optional<String> email, @Bind("MOBILE") Optional<String> mobile, @Bind("PASSWORD_HASH") Optional<String> passwordHash);

    @SqlUpdate("INSERT INTO TUMCCA_ROLES (UID, AUTHORITY) VALUES (:UID, :AUTHORITY)")
    void insertRole(@Bind("UID") Optional<Long> uid, @Bind("AUTHORITY") Optional<String> authority);

    @SqlUpdate("INSERT INTO TUMCCA_ARTISTS (UID, PSEUDONYM, GENDER, INTRODUCTION, TITLE, HOBBIES, FORTE, AVATAR, COUNTRY, PROVINCE, CITY) VALUES (:UID, :PSEUDONYM, :GENDER, :INTRODUCTION, :TITLE, :HOBBIES, :FORTE, :AVATAR, :COUNTRY, :PROVINCE, :CITY)")
    void insertArtist(@Bind("UID") Optional<Long> uid, @Bind("PSEUDONYM") Optional<String> pseudonym, @Bind("GENDER") Optional<Integer> gender, @Bind("INTRODUCTION") Optional<String> introduction, @Bind("TITLE") Optional<String> title, @Bind("HOBBIES") Optional<String> hobbies, @Bind("FORTE") Optional<String> forte, @Bind("AVATAR") Optional<Long> avatar, @Bind("COUNTRY") Optional<String> country, @Bind("PROVINCE") Optional<String> province, @Bind("CITY") Optional<String> city);

    @SqlUpdate("DELETE FROM TUMCCA_USERS WHERE ID = :ID")
    void deleteUser(@Bind("ID") Optional<Long> id);

    @SqlUpdate("DELETE FROM TUMCCA_ROLES WHERE UID = :UID")
    void deleteRole(@Bind("UID") Optional<Long> uid);

    @SqlUpdate("DELETE FROM TUMCCA_ARTISTS WHERE UID = :UID")
    void deleteArtist(@Bind("UID") Optional<Long> uid);

    @SqlUpdate("UPDATE TUMCCA_USERS SET EMAIL = :EMAIL WHERE ID = :ID")
    void updateEmail(@Bind("ID") Optional<Long> id, @Bind("EMAIL") Optional<String> email);

    @SqlUpdate("UPDATE TUMCCA_USERS SET MOBILE = :MOBILE WHERE ID = :ID")
    void updateMobile(@Bind("ID") Optional<Long> id, @Bind("MOBILE") Optional<String> mobile);

    @SqlUpdate("UPDATE TUMCCA_ARTISTS SET PSEUDONYM = :PSEUDONYM, GENDER = :GENDER, INTRODUCTION = :INTRODUCTION, TITLE = :TITLE, HOBBIES = :HOBBIES, FORTE = :FORTE, AVATAR = :AVATAR, COUNTRY = :COUNTRY, PROVINCE = :PROVINCE, CITY = :CITY WHERE UID = :UID")
    void updateArtist(@Bind("UID") Optional<Long> uid, @Bind("PSEUDONYM") Optional<String> pseudonym, @Bind("GENDER") Optional<Integer> gender, @Bind("INTRODUCTION") Optional<String> introduction, @Bind("TITLE") Optional<String> title, @Bind("HOBBIES") Optional<String> hobbies, @Bind("FORTE") Optional<String> forte, @Bind("AVATAR") Optional<Long> avatar, @Bind("COUNTRY") Optional<String> country, @Bind("PROVINCE") Optional<String> province, @Bind("CITY") Optional<String> city);

    @SqlQuery("SELECT ID, EMAIL, MOBILE, PASSWORD_HASH FROM TUMCCA_USERS WHERE EMAIL = :USERNAME OR MOBILE = :USERNAME")
    @Mapper(UsersMapper.class)
    Users findUser(@Bind("USERNAME") Optional<String> username);

    @SqlQuery("SELECT TU.EMAIL, TU.MOBILE, TR.AUTHORITY FROM TUMCCA_USERS TU JOIN TUMCCA_ROLES TR ON (TU.ID = TR.UID) WHERE TU.ID = :ID")
    @Mapper(UserVOMapper.class)
    UserVO findUserVO(@Bind("ID") Optional<Long> id);

    @SqlQuery("SELECT EMAIL, MOBILE FROM TUMCCA_USERS WHERE ID = :ID")
    @Mapper(ContactVOMapper.class)
    ContactVO findContactVO(@Bind("ID") Optional<Long> id);

    @SqlQuery("SELECT TR.AUTHORITY FROM TUMCCA_USERS TU JOIN TUMCCA_ROLES TR ON (TU.ID = TR.UID) WHERE TU.ID = :ID")
    String getUserAuthority(@Bind("ID") Optional<Long> id);

    @SqlQuery("SELECT UID, PSEUDONYM, GENDER, INTRODUCTION, TITLE, HOBBIES, FORTE, AVATAR, COUNTRY, PROVINCE, CITY FROM TUMCCA_ARTISTS WHERE UID = :UID")
    @Mapper(ArtistsMapper.class)
    Artists findArtist(@Bind("UID") Optional<Long> uid);

    @SqlQuery("SELECT COUNT(*) FROM TUMCCA_USERS WHERE EMAIL = :EMAIL")
    Integer countEmail(@Bind("EMAIL") Optional<String> email);

    @SqlQuery("SELECT COUNT(*) FROM TUMCCA_USERS WHERE MOBILE = :MOBILE")
    Integer countMobile(@Bind("MOBILE") Optional<String> mobile);

    @SqlQuery("SELECT COUNT(*) FROM TUMCCA_USERS WHERE ID = :ID")
    Integer countUid(@Bind("ID") Optional<Long> id);
}
