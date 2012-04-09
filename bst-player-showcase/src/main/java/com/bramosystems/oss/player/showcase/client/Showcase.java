/*
 * Copyright 2009 Sikirulai Braheem
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a footer of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.bramosystems.oss.player.showcase.client;

import com.bramosystems.oss.player.showcase.client.res.Bundle;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.*;

/**
 *
 * @author Sikirulai Braheem <sbraheem at gmail.com>
 */
public class Showcase implements EntryPoint {
    private Widget root;

    public Showcase() {
        root = uiBinder.createAndBindUi(this);
        controlPane.addChangeHandlers(stage);
        stage.addDebugHandler(controlPane.getPlayerLogPane());
    }

    @Override
    public void onModuleLoad() {
        Bundle.bundle.css().ensureInjected();

        RootPanel.get("loading").setVisible(false);
        RootPanel.get("app-pane").add(root);

        History.addValueChangeHandler(stage);
        History.addValueChangeHandler(menu);
        History.fireCurrentHistoryState();
    }

    @UiField StageController stage;
    @UiField ControlPane controlPane;
    @UiField MenuController menu;

    private static ShowcaseUiBinder uiBinder = GWT.create(ShowcaseUiBinder.class);

    @UiTemplate("xml/Showcase.ui.xml")
    interface ShowcaseUiBinder extends UiBinder<Widget, Showcase> {}
}
