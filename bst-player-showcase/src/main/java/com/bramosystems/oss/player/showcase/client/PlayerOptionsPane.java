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

import com.bramosystems.oss.player.core.client.RepeatMode;
import com.bramosystems.oss.player.showcase.client.event.PlayerOptionsChangeEvent;
import com.bramosystems.oss.player.showcase.client.event.PlayerOptionsChangeHandler;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;

/**
 *
 * @author Sikiru
 */
public class PlayerOptionsPane extends Composite {

    private static PlayerOptionsUiBinder uiBinder = GWT.create(PlayerOptionsUiBinder.class);
    public static PlayerOptionsPane singleton = new PlayerOptionsPane();

    @UiTemplate("xml/PlayerOptions.ui.xml")
    interface PlayerOptionsUiBinder extends UiBinder<Widget, PlayerOptionsPane> {}
    private PlayerOptions options;

    private PlayerOptionsPane() {
        initWidget(uiBinder.createAndBindUi(this));
        options = new PlayerOptions();
        controls.setValue(true, false);
        repeatOff.setValue(true, false);
    }

    public PlayerOptions getOptions() {
        return options;
    }

    public HandlerRegistration addChangeHandler(PlayerOptionsChangeHandler handler) {
        return addHandler(handler, PlayerOptionsChangeEvent.TYPE);
    }

    @UiHandler("controls")
    void onControlsChange(ValueChangeEvent<Boolean> event) {
        options.setShowControls(event.getValue());
        fireEvent(new PlayerOptionsChangeEvent(options));
    }

    @UiHandler("logger")
    void onLoggerChange(ValueChangeEvent<Boolean> event) {
        options.setShowLogger(event.getValue());
        fireEvent(new PlayerOptionsChangeEvent(options));
    }

    @UiHandler("resizeToVideo")
    void onResizeChange(ValueChangeEvent<Boolean> event) {
        options.setResizeToVideo(event.getValue());
        fireEvent(new PlayerOptionsChangeEvent(options));
    }

    @UiHandler("relButton")
    void onReloadPlayer(ClickEvent event) {
        fireEvent(new PlayerOptionsChangeEvent(options, true));
    }

    @UiHandler("shuffle")
    void onShuffle(ValueChangeEvent<Boolean> event) {
        options.setShuffleOn(event.getValue());
        fireEvent(new PlayerOptionsChangeEvent(options));
    }

    @UiHandler("repeatOff")
     void onRepeatOff(ValueChangeEvent<Boolean> event) {
        options.setRepeatMode(RepeatMode.REPEAT_OFF);
        fireEvent(new PlayerOptionsChangeEvent(options));
    }

    @UiHandler("repeatOne")
     void onRepeatOne(ValueChangeEvent<Boolean> event) {
        options.setRepeatMode(RepeatMode.REPEAT_ONE);
        fireEvent(new PlayerOptionsChangeEvent(options));
    }

    @UiHandler("repeatAll")
     void onRepeatAll(ValueChangeEvent<Boolean> event) {
        options.setRepeatMode(RepeatMode.REPEAT_ALL);
        fireEvent(new PlayerOptionsChangeEvent(options));
    }
    
    @UiField CheckBox controls, resizeToVideo, logger, shuffle;
    @UiField Button relButton;
    @UiField RadioButton repeatOff, repeatOne, repeatAll;
}
