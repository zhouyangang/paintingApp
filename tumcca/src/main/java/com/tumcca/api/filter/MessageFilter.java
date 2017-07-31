package com.tumcca.api.filter;

import org.atmosphere.cpr.BroadcastFilter;

/**
 * Title.
 * <p/>
 * Description.
 *
 * @author Bill Lv {@literal <billcc.lv@hotmail.com>}
 * @version 1.0
 * @since 2015-06-22
 */
public class MessageFilter implements BroadcastFilter {
    @Override
    public BroadcastAction filter(String broadcasterId, Object originalMessage, Object message) {
        return new BroadcastAction(message);
    }
}
