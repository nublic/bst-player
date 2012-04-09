/*
 * Copyright 2010 Sikirulai Braheem
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
package com.bramosystems.oss.player.uibinder.client;

import com.bramosystems.oss.player.core.client.PlayerUtil;
import com.bramosystems.oss.player.core.client.PluginNotFoundException;
import com.bramosystems.oss.player.core.client.PluginVersion;
import com.bramosystems.oss.player.core.client.PluginVersionException;
import com.bramosystems.oss.player.core.client.ui.SWFWidget;
import com.bramosystems.oss.player.util.client.RegExp;
import com.bramosystems.oss.player.util.client.RegExp.RegexException;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 * Wrapper class for {@link SWFWidget} providing UiBinder support.
 *
 * <h3>Usage Example</h3>
 *
 * <p>
 * <code><pre>
 * &lt;ui:UiBinder xmlns:ui='urn:ui:com.google.gwt.uibinder'
 *      xmlns:g='urn:import:com.google.gwt.user.client.ui'
 *      xmlns:player='urn:import:com.bramosystems.oss.player.uibinder.client'&gt;
 *         ...
 *         &lt;player:Flash height='20px' width='100%' pluginVersion='9.0.0'
 *                 mediaURL='http://some-nice-flash.swf' /&gt;
 *         ...
 * &lt;/ui:UiBinder&gt;
 * </pre></code>
 *
 * @author Sikiru Braheem <sbraheem at bramosystems . com>
 * @since 1.1
 */
public class Flash extends Composite {

    private SWFWidget swf;

    /**
     * The constructor
     *
     * @param mediaURL the URL of the media to playback
     * @param height the height of the player (in CSS units)
     * @param width the width of the player (in CSS units)
     * @param pluginVersion the minimum version of the required Flash plugin
     */
    @UiConstructor
    public Flash(String mediaURL, String height, String width, String pluginVersion) {
        Widget w = null;
        try {
            PluginVersion version = new PluginVersion();
            try {
                RegExp.RegexResult res = RegExp.getRegExp("(\\d+).(\\d+).(\\d+)", "").exec(pluginVersion);
                version.setMajor(Integer.parseInt(res.getMatch(1)));
                version.setMinor(Integer.parseInt(res.getMatch(2)));
                version.setRevision(Integer.parseInt(res.getMatch(3)));
            } catch (RegexException ex) {
            }

            swf = new SWFWidget(Player.resolveMediaURL(mediaURL), width, height, version);
            w = swf;
        } catch (PluginNotFoundException ex) {
            w = PlayerUtil.getMissingPluginNotice(ex.getPlugin());
        } catch (PluginVersionException ex) {
            w = PlayerUtil.getMissingPluginNotice(ex.getPlugin(), ex.getRequiredVersion());
        }
        initWidget(w);
    }

    /**
     * Returns the underlying {@code SWFWidget} object wrapped by this widget.
     *
     * @return the underlying {@code SWFWidget}
     */
    public SWFWidget getSWFWidget() {
        return swf;
    }

    /**
     * Sets the comma-separated list of name/value pairs as HTML param tags
     * on the widget
     *
     * @param params comma-separated list of parameters
     */
    public void setParams(String params) {
        if (swf != null) {
            String[] _params = params.split(",");
            for (String _param : _params) {
                String[] nameValue = _param.split("=");
                swf.addProperty(nameValue[0], nameValue[1]);
            }
        }
    }

    /**
     * Sets the {@code flashVars} property of the underlying Flash application
     *
     * @param flashVars the flashVars property
     */
    public void setFlashVars(String flashVars) {
        if (swf != null) {
            swf.addProperty("flashVars", flashVars);
        }
    }
}
