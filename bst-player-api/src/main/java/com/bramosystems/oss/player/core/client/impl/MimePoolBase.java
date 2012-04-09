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
package com.bramosystems.oss.player.core.client.impl;

import com.bramosystems.oss.player.core.client.*;
import com.bramosystems.oss.player.core.client.impl.plugin.PlayerManager;
import com.bramosystems.oss.player.core.client.impl.plugin.PluginManager;
import java.util.HashMap;
import java.util.Set;

/**
 * Utility class to get the file types associated with browser plugins
 *
 * @author Sikirulai Braheem <sbraheem at bramosystems dot com>
 * @since 1.2
 * @deprecated Functionality transfered to PluginManager class.  Left for backward compatibility.
 */
public class MimePoolBase implements MimePool {

    @Override
    public HashMap<String, String> getMimeTypes() {
        return PluginManager.getRegisteredMimeTypes();
    }

    @Override
    public final Set<String> getRegisteredExtensions(Plugin plugin) throws PluginNotFoundException {
        return PlayerManager.getInstance().getPlayerInfo("core", plugin.name()).getRegisteredExtensions();
    }

    @Override
    public final Set<String> getRegisteredProtocols(Plugin plugin) throws PluginNotFoundException {
        return PlayerManager.getInstance().getPlayerInfo("core", plugin.name()).getRegisteredProtocols();
    }
}
