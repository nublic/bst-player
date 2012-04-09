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

import com.bramosystems.oss.player.core.event.client.DebugEvent;
import com.bramosystems.oss.player.core.event.client.DebugHandler;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.*;

/**
 *
 * @author Sikiru
 */
public class PlayerLogPane extends Composite implements DebugHandler {

    public PlayerLogPane() {
        initWidget(bb.createAndBindUi(this));
    }

    private void log(String message) {
        Label l = new Label("+ " + message);
        l.setWordWrap(true);
        list.add(l);
    }

    @Override
    public void onDebug(DebugEvent event) {
        log(event.getMessage());
    }

    @UiHandler("clearButton")
    public void onClearLog(ClickEvent evt){
        list.clear();
    }

    @UiField FlowPanel list;
    @UiField Button clearButton;

    @UiTemplate("xml/PlayerLogPane.ui.xml")
    interface Binder extends UiBinder<Widget, PlayerLogPane> {}
    Binder bb = GWT.create(Binder.class);}
