package com.tumcca.api.service;

import com.google.common.base.Optional;
import com.tumcca.api.db.SessionsDAO;
import com.tumcca.api.db.UsersDAO;
import com.tumcca.api.model.Principals;
import com.tumcca.api.model.Sessions;
import com.tumcca.api.model.Users;
import com.tumcca.api.util.PasswordHash;
import org.skife.jdbi.v2.DBI;
import org.skife.jdbi.v2.sqlobject.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.UUID;

/**
 * Title.
 * <p/>
 * Description.
 *
 * @author Bill Lv {@literal <billcc.lv@hotmail.com>}
 * @version 1.0
 * @since 2015-06-03
 */
public class AuthService {
    static final Logger LOGGER = LoggerFactory.getLogger(AuthService.class);

    final DBI dbi;
    final Long sessionTimeout;

    public AuthService(DBI dbi, Long sessionTimeout) {
        this.dbi = dbi;
        this.sessionTimeout = sessionTimeout;
    }

    @Transaction
    public void markSession(String token) throws Exception {
        try (final SessionsDAO sessionsDAO = dbi.open(SessionsDAO.class)) {
            sessionsDAO.update(Optional.of(token), Optional.of(new Date()), Optional.of(0));
        }
    }

    @Transaction
    public Boolean exists(String username) throws Exception {
        try (final UsersDAO usersDAO = dbi.open(UsersDAO.class)) {
            final Users user = usersDAO.findUser(Optional.of(username));
            return user != null;
        }
    }

    @Transaction
    public void signOut(Principals principal) throws Exception {
        try (final SessionsDAO sessionsDAO = dbi.open(SessionsDAO.class)) {
            final Sessions session = sessionsDAO.findByToken(Optional.of(principal.getToken()));
            if (validateSession(session)) {
                sessionsDAO.update(Optional.of(principal.getToken()), Optional.of(new Date()), Optional.of(0));
            } else {
                throw new Exception("Session timeout or you've already signed out");
            }
        }
    }

    @Transaction
    public Optional<Principals> signIn(String username, String password) throws Exception {
        try (final UsersDAO usersDAO = dbi.open(UsersDAO.class);
             final SessionsDAO sessionsDAO = dbi.open(SessionsDAO.class)) {
            final Users user = usersDAO.findUser(Optional.of(username));
            if (user == null) {
                return Optional.absent();
            }
            if (PasswordHash.check(password, user.getPasswordHash())) {
                final String token = generateToken();
                sessionsDAO.insert(Optional.of(user.getId()), Optional.of(token), Optional.of(1));
                return Optional.of(new Principals(user.getId(), token));
            }
            return Optional.absent();
        }
    }

    @Transaction
    public Sessions getSession(String token) throws Exception {
        try (final SessionsDAO sessionsDAO = dbi.open(SessionsDAO.class)) {
            return sessionsDAO.findByToken(Optional.of(token));
        }
    }

    public Boolean validateSession(Sessions session) {
        if (session == null || session.getStatus() == 0) {
            return false;
        }
        return session.getSignInTime().getTime() + sessionTimeout * 1000 > System.currentTimeMillis();
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }
}
