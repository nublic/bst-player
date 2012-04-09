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
package com.bramosystems.oss.player.showcase.client;

import com.bramosystems.oss.player.core.client.Plugin;
import com.bramosystems.oss.player.core.client.PluginNotFoundException;
import com.bramosystems.oss.player.core.client.MimePool;
import com.bramosystems.oss.player.core.client.PluginVersion;
import com.bramosystems.oss.player.core.client.impl.plugin.PluginManager;
import com.bramosystems.oss.player.showcase.client.res.Bundle;
import com.bramosystems.oss.player.util.client.BrowserPlugin;
import com.bramosystems.oss.player.util.client.MimeType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * @author Sikiru Braheem <sbraheem at bramosystems . com>
 */
public class BrowserInfo extends FlowPanel {

    public BrowserInfo() {
    }

    public void update(AppOptions option) {
        clear();
        switch (option) {
            case mimes:
                doMimePool();
                break;
            case plugins:
                doPlugins();
        }
    }

    private void doPlugins() {
        addPluginRow("Name", "Plugin filename", "Description", EntryType.header);
        JsArray<BrowserPlugin> plugins = BrowserPlugin.getPlugins();
        for (int row = 1; row < plugins.length(); row++) {
            BrowserPlugin bp = plugins.get(row);
            addPluginRow(bp.getName(), bp.getFileName(), bp.getDescription(),
                    row % 2 != 0 ? EntryType.even : EntryType.odd);
        }
    }

    private void doMimeTypes() {
        addMimesRow("Type", "Description", "Enabled", "Suffixes", EntryType.header);
        HashMap<String, MimeBean> _mimes = new HashMap<String, MimeBean>();
        JsArray<MimeType> mimes = MimeType.getMimeTypes();
        for (int i = 0; i < mimes.length(); i++) {
            try {
                MimeType mt = mimes.get(i);
                MimeBean mb = _mimes.get(mt.getType());
                if (mb == null) {
                    mb = new MimeBean(mt.getType(), mt.getDescription());
                }
                mb.getPlugins().add(new PluginBean(
                        mt.getEnabledPlugin() != null ? mt.getEnabledPlugin().getName() : "",
                        mt.getSuffixes()));
                _mimes.put(mt.getType(), mb);
            } catch (PluginNotFoundException ex) {
            }
        }

        int row = 1;
        Iterator<String> types = _mimes.keySet().iterator();
        while (types.hasNext()) {
            String _type = types.next();
            MimeBean _mb = _mimes.get(_type);

            addMimesRow(_type, _mb.getDesc(), "", "", row % 2 != 0 ? EntryType.even : EntryType.odd);
            for (int k = 0; k < _mb.getPlugins().size(); k++, row++) {
//                setHTML(row, 2, _mb.getPlugins().get(k).getName());
//                setHTML(row, 3, _mb.getPlugins().get(k).getSuffixes());
            }
            row++;
        }
    }

    private void doMimePool() {
        addPoolRow("Player Widget", "Plugin", "Plugin Version", false, "Supported Filename Suffixes", EntryType.header);
        MimePool pool = MimePool.instance;
        ArrayList<Plugin> plugs = new ArrayList<Plugin>(Arrays.asList(Plugin.values()));
        plugs.remove(Plugin.Auto);
        plugs.remove(Plugin.MatrixSupport);
        plugs.remove(Plugin.PlaylistSupport);
        int row = 0;
        for (Plugin plug : plugs) { //TODO: REFINE THIS TO WORK WITH PLUGININFO class
            Boolean isSupported = null;
            String player = plug.name(), plugName = plug.name(), ver = "-";
            PluginVersion pv = null;
            Set<String> suf = null;
            try {
                switch (plug) {
                    case DivXPlayer:
                        plugName = "DivX Web Player";
                        break;
                    case QuickTimePlayer:
                        plugName = "QuickTime Player";
                        break;
                    case VLCPlayer:
                        plugName = "VLC Multimedia Player";
                        break;
                    case FlashPlayer:
                        player = "FlashMediaPlayer";
                        plugName = "Adobe Flash Player";
                        break;
                    case WinMediaPlayer:
                        plugName = "Windows Media Player";
                        break;
                    case Native:
                        plugName = "HTML5 <code>&lt;video&gt;</code>";
                        player = "NativePlayer";
                        break;
                }
                
                pv = PluginManager.getPluginInfo(plug).getVersion();
                ver = pv.toString();
                isSupported = pv.compareTo(plug.getVersion()) >= 0;
                suf = pool.getRegisteredExtensions(plug);
            } catch (PluginNotFoundException ex) {
            }

            addPoolRow(player, plugName, ver, isSupported,
                    (isSupported != null) && isSupported && (suf != null) ? suf.toString() : "-", row++ % 2 != 0 ? EntryType.even : EntryType.odd);
        }
        add(lb.createAndBindUi(null));
    }

    private void addPluginRow(String name, String filename, String desc, EntryType type) {
        FlowPanel fp = new FPanel(type);
        fp.add(new XLabel(name, Bundle.bundle.css().pct30()));
        fp.add(new XLabel(filename, Bundle.bundle.css().pct30()));
        fp.add(new XLabel(desc, Bundle.bundle.css().pct40()));
        fp.add(new XLabel(Bundle.bundle.css().clear()));
        add(fp);
    }

    private void addMimesRow(String mime, String desc, String enabled, String suffs, EntryType type) {
        FlowPanel fp = new FPanel(type);
        fp.add(new XLabel(mime, Bundle.bundle.css().pct20()));
        fp.add(new XLabel(desc, Bundle.bundle.css().pct20()));
        fp.add(new XLabel(enabled, Bundle.bundle.css().pct20()));
        fp.add(new XLabel(suffs, Bundle.bundle.css().pct20()));
        fp.add(new XLabel(Bundle.bundle.css().clear()));
        add(fp);
    }

    private void addPoolRow(String player, String plugin, String version, Boolean isSupted, String suffs, EntryType type) {
        FlowPanel fp = new FPanel(type);
        if (type.equals(EntryType.header)) {
            fp.add(new XLabel(player, Bundle.bundle.css().pct20()));
        } else {
            XLabel lbl = new XLabel(player, "");
            if (isSupted == null) {
                lbl.setStyleName(Bundle.bundle.css().no());
            } else {
                lbl.setStyleName(isSupted ? Bundle.bundle.css().yes() : Bundle.bundle.css().error());
            }
            fp.add(lbl);
        }
        fp.add(new XLabel(plugin, Bundle.bundle.css().pct20()));
        fp.add(new XLabel(version, Bundle.bundle.css().pct20()));
        fp.add(new XLabel(suffs, Bundle.bundle.css().pct40()));
        fp.add(new XLabel(Bundle.bundle.css().clear()));
        add(fp);
    }

    private class XLabel extends HTML {

        public XLabel(String style) {
            setWordWrap(true);
            setStyleName(style);
        }

        public XLabel(String text, String style) {
            super(text, true);
            setStyleName(style);
        }
    }

    private class FPanel extends FlowPanel {

        public FPanel(EntryType entry) {
            setStyleName(Bundle.bundle.css().spacedPanel());
            switch (entry) {
                case header:
                    addStyleName(Bundle.bundle.css().headerRow());
                    break;
                case even:
                    addStyleName(Bundle.bundle.css().evenRow());
                    break;
                case odd:
                    addStyleName(Bundle.bundle.css().oddRow());
                    break;
            }
        }
    }

    private enum EntryType {

        header, even, odd
    }

    private class MimeBean implements Comparable<MimeBean> {

        private String type, desc;
        private ArrayList<PluginBean> plugins;

        public MimeBean(String type, String desc) {
            this.type = type;
            this.desc = desc;
            plugins = new ArrayList<PluginBean>();
        }

        public String getDesc() {
            return desc;
        }

        public String getType() {
            return type;
        }

        public ArrayList<PluginBean> getPlugins() {
            return plugins;
        }

        @Override
        public int compareTo(MimeBean o) {
            return type.compareTo(o.type);
        }
    }

    private class PluginBean {

        private String name, suffixes;

        public PluginBean(String name, String suffixes) {
            this.name = name;
            this.suffixes = suffixes;
        }

        public String getSuffixes() {
            return suffixes;
        }

        public String getName() {
            return name;
        }
    }

    @UiTemplate("xml/BrowserInfoLegend.ui.xml")
    interface LegendBinder extends UiBinder<Widget, Void> {
    };
    LegendBinder lb = GWT.create(LegendBinder.class);
}
