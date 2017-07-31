package com.tumcca.api.auth;

import com.google.common.base.Optional;
import com.tumcca.api.model.Principals;
import com.tumcca.api.service.AuthService;
import io.dropwizard.auth.AuthenticationException;
import io.dropwizard.auth.Authenticator;
import io.dropwizard.auth.basic.BasicCredentials;
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
public class TumccaBasicAuthenticator implements Authenticator<BasicCredentials, Principals> {
    static final Logger LOGGER = LoggerFactory.getLogger(TumccaBasicAuthenticator.class);

    final AuthService authService;

    public TumccaBasicAuthenticator(AuthService authService) {
        this.authService = authService;
    }

    @Override
    public Optional<Principals> authenticate(BasicCredentials basicCredentials) throws AuthenticationException {
        try {
            return authService.signIn(basicCredentials.getUsername(), basicCredentials.getPassword());
        } catch (Exception e) {
            throw new AuthenticationException("authenticate error");
        }
    }
}
