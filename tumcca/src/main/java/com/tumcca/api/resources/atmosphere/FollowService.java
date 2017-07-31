package com.tumcca.api.resources.atmosphere;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tumcca.api.message.Follow;
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
 * @since 2015-06-22
 */
@Path("/")
@AtmosphereHandlerService(path = "/ws/follow",
        broadcasterCache = UUIDBroadcasterCache.class,
        interceptors = {AtmosphereResourceLifecycleInterceptor.class,
                BroadcastOnPostAtmosphereInterceptor.class,
                TrackMessageSizeInterceptor.class,
                HeartbeatInterceptor.class
        })
public class FollowService extends OnMessage<String> {
    final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void onMessage(AtmosphereResponse response, String message) throws IOException {
        final Follow follow = mapper.readValue(message, Follow.class);
        response.write(mapper.writeValueAsString(follow));
    }
}
