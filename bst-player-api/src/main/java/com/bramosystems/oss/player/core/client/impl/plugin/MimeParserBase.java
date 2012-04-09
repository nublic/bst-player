/*
 * Copyright 2009 Sikirulai Braheem
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.bramosystems.oss.player.core.client.impl.plugin;

import com.bramosystems.oss.player.core.client.impl.*;
import com.bramosystems.oss.player.core.client.*;
import com.google.gwt.core.client.GWT;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Utility class to get the file types associated with browser plugins
 *
 * @author Sikirulai Braheem <sbraheem at bramosystems dot com>
 * @since 1.2.1
 */
public abstract class MimeParserBase {

    public static final MimeParserBase instance = GWT.create(MimeParserBase.class);
    private HashMap<String, String> mimeTypes;
    private PlayerInfo infoCache;

    @SuppressWarnings("OverridableMethodCallInConstructor")
    protected MimeParserBase() {
        mimeTypes = new HashMap<String, String>();
        initMimeTypes(mimeTypes);
    }

    protected HashMap<String, String> getMimeTypes() {
        return mimeTypes;
    }

    protected void addPlayerProperties(PlayerInfo info) {
        infoCache = info;
        if(info.getPlayerName().equals(Plugin.Native.name())) {
            addNativeExtensions();
        } else {
            processPlayer(info.getProviderName(), info.getPlayerName());
        }
    }

    protected abstract void processPlayer(String provName, String playerName);

    /**
     * Called by the constructor method to populate all known audio/video
     * mime types.
     *
     * @param mimeTypes the mimeType map to be filled.  The map is filled as
     * (mimeType,file-extension) value pairs.
     */
    protected abstract void initMimeTypes(HashMap<String, String> mimeTypes);

    /**
     * Adds the specified extensions to the pool of file types supported by the
     * plugin
     *
     * @param plugin the plugin
     * @param extensions the file types supported by the plugin, multiple types should
     * be separated by commas e.g. wav,wma
     */
    protected final void addExtensions(int major, int minor, int rev, String extensions) {
        if (extensions.length() > 0) {
            PluginVersion pv = PluginVersion.get(major, minor, rev);
            if (infoCache.getRequiredPluginVersion().compareTo(pv) >= 0) {
                infoCache.getRegisteredExtensions().addAll(Arrays.asList(extensions.split(",")));
            }
        }
    }

    /**
     * Adds the specified protocols to the pool of streaming protocols supported by the
     * plugin
     *
     * @param plugin the plugin
     * @param protocols the streaming protocol supported by the plugin, multiple types should
     * be separated by commas e.g. rtp,rtsp
     */
    protected final void addProtocols(int major, int minor, int rev, String protocols) {
        if (protocols.length() > 0) {
            PluginVersion pv = PluginVersion.get(major, minor, rev);
            if (infoCache.getRequiredPluginVersion().compareTo(pv) >= 0) {
                infoCache.getRegisteredProtocols().addAll(Arrays.asList(protocols.split(",")));
            }
        }
    }

    /**
     * Adds all audio/video file types that have native support on the client.
     * This call has no effect on non-HTML5 compliant browsers.
     */
    protected final void addNativeExtensions() {
        if (PlayerUtil.isHTML5CompliantClient()) {
            NativePlayerUtil.TestUtil test = NativePlayerUtil.getTestUtil();
            Iterator<String> mimeKeys = mimeTypes.keySet().iterator();
            while (mimeKeys.hasNext()) {
                String mime = mimeKeys.next();
                try {
                    switch (test.canPlayType(mime)) {
                        case maybe:
                        case probably:
                            addExtensions(5, 0, 0, mimeTypes.get(mime));
                    }
                } catch (Exception e) { // mimeType cannot be played ...
                }
            }
        }
    }
}
