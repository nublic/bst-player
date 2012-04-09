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
package com.bramosystems.oss.player.core.client;

import com.bramosystems.oss.player.core.client.impl.MimePoolBase;
import java.util.HashMap;
import java.util.Set;

/**
 * Utility interface to get the file types associated with browser plugins.  
 * 
 * <p>A concrete implementation of this interface can be retrieved with the
 * <code>instance</code> property
 *
 * @author Sikirulai Braheem <sbraheem at bramosystems dot com>
 * @since 1.2
 */
public interface MimePool {

    /**
     * The static instance of the MimePool implementation
     */
    public static MimePool instance = new MimePoolBase();

    /**
     * Returns the file extensions registered on the specified plugin
     *
     * @param plugin the desired plugin
     * @return the registered file extensions
     */
    public Set<String> getRegisteredExtensions(Plugin plugin) throws PluginNotFoundException;

    /**
     * Returns the streaming protocols registered on the specified plugin
     *
     * @param plugin the desired plugin
     * @return the registered streaming protocols
     */
    public Set<String> getRegisteredProtocols(Plugin plugin) throws PluginNotFoundException;

    /**
     * Returns the media mimeTypes registered on the application.  These are the mimetypes
     * listed in the <code>bstplayer.media.mimeTypes</code> property file.
     *
     * <p>The map is filled as <code>mimeType,file-extension</code> value pairs.
     * @return the mimeTypes
     */
    public HashMap<String, String> getMimeTypes();
}
