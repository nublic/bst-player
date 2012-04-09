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
package com.bramosystems.oss.player.showcase.client;

import com.bramosystems.oss.player.core.client.PlayerUtil;
import com.bramosystems.oss.player.core.client.Plugin;
import com.bramosystems.oss.player.core.client.PluginNotFoundException;
import com.bramosystems.oss.player.core.client.PluginVersion;
import com.bramosystems.oss.player.showcase.client.event.PluginChangeEvent;
import com.bramosystems.oss.player.showcase.client.event.PluginChangeHandler;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.*;

/**
 *
 * @author Sikiru
 */
public class PluginPane extends Composite implements ValueChangeHandler<String> {

    public PluginPane() {
        initWidget(pp.createAndBindUi(this));
    }

    public HandlerRegistration addChangeHandler(PluginChangeHandler handler) {
        return addHandler(handler, PluginChangeEvent.TYPE);
    }

    @Override
    public void onValueChange(ValueChangeEvent<String> event) {
        AppOptions option = AppOptions.home;
        try {
            option = AppOptions.valueOf(event.getValue());
        } catch (Exception e) {
        }

        auto.setValue(true);
        switch (option) {
            case capsule:
            case core:
            case flat:
                auto.setEnabled(true);
                divx.setEnabled(hasPlugin(Plugin.DivXPlayer));
                flash.setEnabled(hasPlugin(Plugin.FlashPlayer));
                html5.setEnabled(hasPlugin(Plugin.Native));
                qt.setEnabled(hasPlugin(Plugin.QuickTimePlayer));
                vlc.setEnabled(hasPlugin(Plugin.VLCPlayer));
                wmp.setEnabled(hasPlugin(Plugin.WinMediaPlayer));
                uTube.setEnabled(false);
                uChrome.setEnabled(false);
                break;
            case matrix:
                auto.setEnabled(true);
                divx.setEnabled(hasMatrixSupport(Plugin.DivXPlayer));
                flash.setEnabled(hasMatrixSupport(Plugin.FlashPlayer));
                html5.setEnabled(hasMatrixSupport(Plugin.Native));
                qt.setEnabled(hasMatrixSupport(Plugin.QuickTimePlayer));
                vlc.setEnabled(hasMatrixSupport(Plugin.VLCPlayer));
                wmp.setEnabled(hasMatrixSupport(Plugin.WinMediaPlayer));
                uTube.setEnabled(false);
                uChrome.setEnabled(false);
                break;
            case ytube:
                auto.setEnabled(false);
                divx.setEnabled(false);
                flash.setEnabled(false);
                html5.setEnabled(false);
                qt.setEnabled(false);
                vlc.setEnabled(false);
                wmp.setEnabled(false);
                uTube.setEnabled(hasPlugin(Plugin.FlashPlayer));
                uChrome.setEnabled(hasPlugin(Plugin.FlashPlayer));
                uTube.setValue(hasPlugin(Plugin.FlashPlayer) && true);
                break;
            case home:
            case plugins:
            case mimes:
            default:
                auto.setEnabled(false);
                divx.setEnabled(false);
                flash.setEnabled(false);
                html5.setEnabled(false);
                qt.setEnabled(false);
                vlc.setEnabled(false);
                wmp.setEnabled(false);
                uTube.setEnabled(false);
                uChrome.setEnabled(false);
        }
    }

    private boolean hasMatrixSupport(Plugin plugin) {
        boolean has = false;
        switch (plugin) {
            case FlashPlayer:
            case QuickTimePlayer:
                has = hasPlugin(plugin);
        }
        return has;
    }

    private boolean hasPlugin(Plugin plugin) {
        if (plugin.equals(Plugin.Native)) {
            return PlayerUtil.isHTML5CompliantClient();
        }

        try {
            PluginVersion req = plugin.getVersion();
            PluginVersion v = null;
            switch (plugin) {
                case DivXPlayer:
                    v = PlayerUtil.getDivXPlayerPluginVersion();
                    break;
                case FlashPlayer:
                    v = PlayerUtil.getFlashPlayerVersion();
                    break;
                case QuickTimePlayer:
                    v = PlayerUtil.getQuickTimePluginVersion();
                    break;
                case VLCPlayer:
                    v = PlayerUtil.getVLCPlayerPluginVersion();
                    break;
                case WinMediaPlayer:
                    v = PlayerUtil.getWindowsMediaPlayerPluginVersion();
                    break;
            }
            return v.compareTo(req) >= 0;
        } catch (PluginNotFoundException ex) {
            return false;
        }
    }

    @UiHandler("html5")
    void onhtml5(ValueChangeEvent<Boolean> event) {
        if (event.getValue()) {
            fireEvent(new PluginChangeEvent(Plugin.Native));
        }
    }

    @UiHandler("wmp")
    void onwmp(ValueChangeEvent<Boolean> event) {
        if (event.getValue()) {
            fireEvent(new PluginChangeEvent(Plugin.WinMediaPlayer));
        }
    }

    @UiHandler("vlc")
    void onvlc(ValueChangeEvent<Boolean> event) {
        if (event.getValue()) {
            fireEvent(new PluginChangeEvent(Plugin.VLCPlayer));
        }
    }

    @UiHandler("divx")
    void ondivx(ValueChangeEvent<Boolean> event) {
        if (event.getValue()) {
            fireEvent(new PluginChangeEvent(Plugin.DivXPlayer));
        }
    }

    @UiHandler("flash")
    void onflash(ValueChangeEvent<Boolean> event) {
        if (event.getValue()) {
            fireEvent(new PluginChangeEvent(Plugin.FlashPlayer));
        }
    }

    @UiHandler("qt")
    void onqt(ValueChangeEvent<Boolean> event) {
        if (event.getValue()) {
            fireEvent(new PluginChangeEvent(Plugin.QuickTimePlayer));
        }
    }

    @UiHandler("auto")
    void onauto(ValueChangeEvent<Boolean> event) {
        if (event.getValue()) {
            fireEvent(new PluginChangeEvent(Plugin.Auto));
        }
    }
    @UiHandler("uTube")
    void onutube(ValueChangeEvent<Boolean> event) {
        if (event.getValue()) {
            fireEvent(new PluginChangeEvent(Plugin.Auto));
        }
    }
    @UiHandler("uChrome")
    void onuchrome(ValueChangeEvent<Boolean> event) {
        if (event.getValue()) {
            fireEvent(new PluginChangeEvent(Plugin.Native));
        }
    }
    PPaneBinder pp = GWT.create(PPaneBinder.class);
    @UiField
    RadioButton html5, wmp, vlc, divx, flash, qt, auto, uTube, uChrome;

    @UiTemplate("xml/PluginPane.ui.xml")
    interface PPaneBinder extends UiBinder<Widget, PluginPane> {
    }
}
