package com.tumcca.api.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tumcca.api.message.Follow;
import com.tumcca.api.message.UnFollow;
import org.atmosphere.wasync.*;
import org.atmosphere.wasync.impl.AtmosphereClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * Title.
 * <p/>
 * Description.
 *
 * @author Bill Lv {@literal <billcc.lv@hotmail.com>}
 * @version 1.0
 * @since 2015-06-21
 */
public class WSUtil {
    static final Logger LOGGER = LoggerFactory.getLogger(WSUtil.class);

    static final ObjectMapper mapper = new ObjectMapper();

    public static RequestBuilder createFollowRequest(String token, String domain, String followUri, AtmosphereClient atmosphereClient) {
        return atmosphereClient.newRequestBuilder()
                .header("Authorization", "Bearer " + token)
                .method(Request.METHOD.GET)
                .uri(domain + followUri)
                .trackMessageLength(true)
                .encoder(new Encoder<Follow, String>() {
                    @Override
                    public String encode(Follow data) {
                        try {
                            return mapper.writeValueAsString(data);
                        } catch (IOException e) {
                            LOGGER.error("write value as string error", e);
                            throw new RuntimeException(e);
                        }
                    }
                })
                .decoder(new Decoder<String, Follow>() {
                    @Override
                    public Follow decode(Event type, String data) {

                        data = data.trim();

                        // Padding from Atmosphere, skip
                        if (data.length() == 0) {
                            return null;
                        }

                        if (type.equals(Event.MESSAGE)) {
                            try {
                                return mapper.readValue(data, Follow.class);
                            } catch (IOException e) {
                                return null;
                            }
                        } else {
                            return null;
                        }
                    }
                })
                .transport(Request.TRANSPORT.WEBSOCKET)
                .transport(Request.TRANSPORT.SSE)
                .transport(Request.TRANSPORT.LONG_POLLING);
    }

    public static RequestBuilder createUnFollowRequest(String token, String domain, String unfollowUri, AtmosphereClient atmosphereClient) {
        return atmosphereClient.newRequestBuilder()
                .header("Authorization", "Bearer " + token)
                .method(Request.METHOD.GET)
                .uri(domain + unfollowUri)
                .trackMessageLength(true)
                .encoder(new Encoder<UnFollow, String>() {
                    @Override
                    public String encode(UnFollow data) {
                        try {
                            return mapper.writeValueAsString(data);
                        } catch (IOException e) {
                            LOGGER.error("write value as string error", e);
                            throw new RuntimeException(e);
                        }
                    }
                })
                .decoder(new Decoder<String, UnFollow>() {
                    @Override
                    public UnFollow decode(Event type, String data) {

                        data = data.trim();

                        // Padding from Atmosphere, skip
                        if (data.length() == 0) {
                            return null;
                        }

                        if (type.equals(Event.MESSAGE)) {
                            try {
                                return mapper.readValue(data, UnFollow.class);
                            } catch (IOException e) {
                                return null;
                            }
                        } else {
                            return null;
                        }
                    }
                })
                .transport(Request.TRANSPORT.WEBSOCKET)
                .transport(Request.TRANSPORT.SSE)
                .transport(Request.TRANSPORT.LONG_POLLING);
    }
}
