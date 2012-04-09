/*
 * Copyright 2011 Sikirulai Braheem <sbraheem at bramosystems.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.bramosystems.oss.player.core.client.impl;

import com.bramosystems.oss.player.core.client.spi.PlayerElement;
import java.util.HashMap;
import java.util.Iterator;

/**
 * IE specific native implementation of the PluginManagerImpl class. It is not recommended to
 * interact with this class directly.
 *
 * @author Sikirulai Braheem <sbraheem at bramosystems.com>
 */
public class CorePlayerProviderIE extends CorePlayerProvider {

    @Override
    protected PlayerElement getVLCElement(String playerId, String mediaURL, boolean autoplay) {
        PlayerElement axo = new PlayerElement(PlayerElement.Type.ObjectElementIE, playerId, "clsid:9BE31822-FDAD-461B-AD51-BE1D1C159921");
        axo.getElement().setPropertyString("events", "True");
        axo.addParam("AutoPlay", "False");
        axo.addParam("AutoLoop", "False");
        axo.addParam("Src", "");
        return axo;
    }

    @Override
    protected PlayerElement getWMPElement(String playerId, String mediaURL, boolean autoplay,
            HashMap<String, String> params) {
        PlayerElement xo = new PlayerElement(PlayerElement.Type.ObjectElementIE, playerId, "clsid:6BF52A52-394A-11d3-B153-00C04F79FAA6");
        xo.addParam("autostart", Boolean.toString(autoplay));
        xo.addParam("URL", mediaURL);

        Iterator<String> keys = params.keySet().iterator();
        while (keys.hasNext()) {
            String name = keys.next();
            xo.addParam(name, params.get(name));
        }
        return xo;
    }

    @Override
    protected PlayerElement getQTElement(String playerId, String mediaURL, boolean autoplay,
            HashMap<String, String> params) {
        PlayerElement xo = new PlayerElement(PlayerElement.Type.ObjectElementIE, playerId, "clsid:02BF25D5-8C17-4B23-BC80-D3488ABDDC6B");
        xo.getElement().setAttribute("style", "BEHAVIOR:url(#" + QTStateManagerIE.behaviourObjId + ")");
        xo.addParam("AutoPlay", Boolean.toString(autoplay));

        Iterator<String> keys = params.keySet().iterator();
        while (keys.hasNext()) {
            String name = keys.next();
            xo.addParam(name, params.get(name));
        }
        return xo;
    }

    @Override
    protected PlayerElement getSWFElement(String playerId, String swfURL, HashMap<String, String> params) {
        PlayerElement xo = new PlayerElement(PlayerElement.Type.ObjectElementIE, playerId, "clsid:D27CDB6E-AE6D-11cf-96B8-444553540000");
        xo.addParam("src", swfURL);

        Iterator<String> keys = params.keySet().iterator();
        while (keys.hasNext()) {
            String name = keys.next();
            xo.addParam(name, params.get(name));
        }
        return xo;
    }

    @Override
    protected PlayerElement getDivXElement(String playerId, String mediaURL, boolean autoplay,
            HashMap<String, String> params) {
        PlayerElement xo = new PlayerElement(PlayerElement.Type.ObjectElementIE, playerId, "clsid:67DABFBF-D0AB-41fa-9C46-CC0F21721616");
        xo.addParam("autoPlay", Boolean.toString(autoplay));
//        xo.addParam("src", mediaURL);

        Iterator<String> keys = params.keySet().iterator();
        while (keys.hasNext()) {
            String name = keys.next();
            xo.addParam(name, params.get(name));
        }
        return xo;
    }

    @Override
    public boolean isWMPProgrammableEmbedModeSupported() {
        return true;
    }
}
