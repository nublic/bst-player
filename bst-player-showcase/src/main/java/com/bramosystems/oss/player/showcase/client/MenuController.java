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

import com.bramosystems.oss.player.showcase.client.res.Bundle;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.*;

/**
 *
 * @author Sikiru
 */
public class MenuController extends FlowPanel implements ValueChangeHandler<String> {

    public MenuController() {
        AppOptions[] apps = AppOptions.values();
        for (int i = 0; i < apps.length; i++) {
            add(new AppLink(apps[i]));
        }
    }

    @Override
    public void onValueChange(ValueChangeEvent<String> event) {
        AppOptions link = AppOptions.home;
        try {
            link = AppOptions.valueOf(event.getValue());
        } catch (Exception e) {
        }

        for(int i = 0; i < getWidgetCount(); i++) {
            AppLink al = (AppLink)getWidget(i);
            al.setSelected(al.getTargetHistoryToken().equals(link.name()));
        }
    }

    private class AppLink extends Hyperlink {

        public AppLink(AppOptions app) {
            super(app.getTitle(), app.name());
            setStylePrimaryName(Bundle.bundle.css().navItem());
        }

        void setSelected(boolean selected) {
            if (selected) {
                addStyleName(Bundle.bundle.css().navItemSelected());
            } else {
                removeStyleName(Bundle.bundle.css().navItemSelected());
            }
        }
    }
}
