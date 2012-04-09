/*
 *  Copyright 2010 Sikiru Braheem <sbraheem at bramosystems . com>.
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
package com.bramosystems.oss.player.showcase.client.res;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ExternalTextResource;
import com.google.gwt.resources.client.ImageResource;

/**
 *
 * @author Sikiru Braheem <sbraheem at bramosystems . com>
 */
public interface Bundle extends ClientBundle {

    Bundle bundle = GWT.create(Bundle.class);

    public ImageResource tick();

    public ImageResource error();

    public ImageResource cross();

    public ImageResource music();

    @Source("index.html")
    public ExternalTextResource home();

    @Source("notices.html")
    public ExternalTextResource notices();

    @Source("styles.css")
    public Styles css();

    public interface Styles extends CssResource {

        public String controlPaneList();

        public String controlPaneButtonPanel();

        public String pluginOption();

        public String disabledPlugin();

        public String navItem();

        public String navItemSelected();

        public String pct20();

        public String pct60();

        public String pct30();

        public String pct40();

        public String clear();

        public String spacedPanel();

        public String headerRow();

        public String evenRow();

        public String oddRow();

        public String yes();

        public String no();

        public String error();

        public String playlistItem();

        public String capsule();
    }
}
