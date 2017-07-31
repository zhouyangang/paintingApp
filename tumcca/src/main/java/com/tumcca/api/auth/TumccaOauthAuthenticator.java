package com.tumcca.api.auth;

import com.google.common.base.Optional;
import com.tumcca.api.model.Principals;
import com.tumcca.api.model.Sessions;
import com.tumcca.api.service.AuthService;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Title.
 * <p/>
 * Description.
 *
 * @author Bill Lv {@literal <billcc.lv@hotmail.com>}
 * @version 1.0
 * @since 2015-06-03
 */
public class TumccaOauthAuthenticator implements Authenticator<String, Principals> {
    static final Logger LOGGER = LoggerFactory.getLogger(TumccaOauthAuthenticator.class);

    final AuthService authService;

    public TumccaOauthAuthenticator(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public Optional<Principals> authenticate(String credentials) throws AuthenticationException {
        final String token = credentials;
        Sessions session;
        try {
            session = authService.getSession(token);
        } catch (Exception e) {
            throw new AuthenticationException("get session error");
        }
        final Boolean valid = authService.validateSession(session);
        if (valid) {
            return Optional.of(new Principals(session.getUid(), token));
        } else {
            try {
                authService.markSession(token);
            } catch (Exception e) {
                throw new AuthenticationException("mark session error");
            }
            return Optional.absent();
        }
    }
}
