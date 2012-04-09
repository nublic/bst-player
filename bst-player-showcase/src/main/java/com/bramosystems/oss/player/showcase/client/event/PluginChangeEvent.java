/*
 *  Copyright 2010 Sikiru.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package com.bramosystems.oss.player.showcase.client.event;

import com.bramosystems.oss.player.core.client.Plugin;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.GwtEvent.Type;

/**
 *
 * @author Sikiru
 */
public class PluginChangeEvent extends GwtEvent<PluginChangeHandler> {

    public static Type<PluginChangeHandler> TYPE = new Type<PluginChangeHandler>();
    private Plugin plugin;

    public PluginChangeEvent(Plugin plugin) {
        this.plugin = plugin;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    @Override
    public Type<PluginChangeHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(PluginChangeHandler handler) {
        handler.onPluginChanged(this);
    }
}
