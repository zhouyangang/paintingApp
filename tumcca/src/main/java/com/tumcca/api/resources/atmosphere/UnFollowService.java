package com.tumcca.api.resources.atmosphere;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tumcca.api.message.UnFollow;
import org.atmosphere.cache.UUIDBroadcasterCache;
import org.atmosphere.client.TrackMessageSizeInterceptor;
import org.atmosphere.config.service.AtmosphereHandlerService;
import org.atmosphere.cpr.AtmosphereResponse;
import org.atmosphere.handler.OnMessage;
import org.atmosphere.interceptor.AtmosphereResourceLifecycleInterceptor;
import org.atmosphere.interceptor.BroadcastOnPostAtmosphereInterceptor;
import org.atmosphere.interceptor.HeartbeatInterceptor;

import javax.ws.rs.Path;
import java.io.IOException;

/**
 * Title.
 * <p/>
 * Description.
 *
 * @author Bill Lv {@literal <billcc.lv@hotmail.com>}
 * @version 1.0
 * @since 2015-06-23
 */
@Path("/")
@AtmosphereHandlerService(path = "/ws/unfollow",
        broadcasterCache = UUIDBroadcasterCache.class,
        interceptors = {AtmosphereResourceLifecycleInterceptor.class,
                BroadcastOnPostAtmosphereInterceptor.class,
                TrackMessageSizeInterceptor.class,
                HeartbeatInterceptor.class
        })
public class UnFollowService extends OnMessage<String> {
    final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void onMessage(AtmosphereResponse response, String message) throws IOException {
        final UnFollow unFollow = mapper.readValue(message, UnFollow.class);
        response.write(mapper.writeValueAsBytes(unFollow));
    }
}
