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

import com.bramosystems.oss.player.showcase.client.event.PlaylistChangeEvent;
import com.bramosystems.oss.player.showcase.client.event.PlaylistChangeHandler;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.*;
import java.util.ArrayList;

/**
 *
 * @author Sikiru
 */
public class PlaylistPane extends Composite implements ValueChangeHandler<String> {

    public static String baseURL = "http://oss.bramosystems.com/bst-player/demo/media/";
//    public static String baseURL = GWT.getHostPageBaseURL() + "media/";
    public static PlaylistPane singleton = new PlaylistPane();
    private ArrayList<MRL> entries, uTube;
    private AppOptions option;

    private PlaylistPane() {
        initWidget(bb.createAndBindUi(this));
        entries = new ArrayList<MRL>();
        uTube = new ArrayList<MRL>();

        entries.add(new MRL(baseURL + "o-na-som.mp3"));
        entries.add(new MRL(baseURL + "traffic.flv"));
        entries.add(new MRL(baseURL + "applause.mp3"));
        entries.add(new MRL("http://bst-player.googlecode.com/svn/tags/showcase/media/islamic-jihad.wmv"));
        entries.add(new MRL(baseURL + "big-buck-bunny.mp4", baseURL + "big-buck-bunny.ogv"));
        uTube.add(new MRL("http://www.youtube.com/v/QbwZL-EK6CY"));
//        uTube.add(new MRL("http://www.youtube.com/v/IqnWs_j5MbM"));
    }

    @Override
    public void onValueChange(ValueChangeEvent<String> event) {
        option = AppOptions.home;
        try {
            option = AppOptions.valueOf(event.getValue());
        } catch (Exception e) {
        }
        refreshView();
    }

    public void addChangeHandler(PlaylistChangeHandler handler) {
        addHandler(handler, PlaylistChangeEvent.TYPE);
    }

    public final void addEntry(String... urls) {
        switch (option) {
            case ytube:
                uTube.add(new MRL(urls));
                break;
            default:
                entries.add(new MRL(urls));
        }
        if (isAttached()) {
            refreshView();
        }
    }

    public MRL removeEntry(int index) {
        MRL m = option.equals(AppOptions.ytube) ? uTube.remove(index) : entries.remove(index);
        if (isAttached()) {
            refreshView();
        }
        return m;
    }

    public ArrayList<MRL> getEntries() {
        return option.equals(AppOptions.ytube) ? uTube : entries;
    }

    private void refreshView() {
        list.clear();
        ArrayList<MRL> es = option.equals(AppOptions.ytube) ? uTube : entries;
        for (MRL entry : es) {
            StringBuilder sb = new StringBuilder();
            for (String e : entry) {
                sb.append(e).append(", ");
            }
            sb.deleteCharAt(sb.lastIndexOf(", "));
            list.addItem(sb.toString());
        }
    }

    @UiHandler("addButton")
    public void onAddClicked(ClickEvent event) {
        String res = Window.prompt("Type the URL of the media to add to the playlist. Separate multiple URLs of the same media with a comma", "");
        if (res != null && !res.isEmpty()) {
            String[] r = res.split(",");
            for (String x : r) {
                x.trim();
            }
            addEntry(r);
            fireEvent(new PlaylistChangeEvent(new MRL(r), list.getItemCount(), true));
        }
    }

    @UiHandler("delButton")
    public void onDelClicked(ClickEvent event) {
        if (Window.confirm("Do you really want to remove the selected media from the playlist?")) {
            int index = list.getSelectedIndex();
            MRL m = removeEntry(index);
            delButton.setEnabled(false);
            fireEvent(new PlaylistChangeEvent(m, index, false));
        }
    }

    @UiHandler("list")
    public void onListChange(ChangeEvent event) {
        delButton.setEnabled(true);
    }
    
    @UiField ListBox list;
    @UiField Button addButton, delButton;

    @UiTemplate("xml/PlaylistPane.ui.xml")
    interface Binder extends UiBinder<Widget, PlaylistPane> {}
    Binder bb = GWT.create(Binder.class);
}
