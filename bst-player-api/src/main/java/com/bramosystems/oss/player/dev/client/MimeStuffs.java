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
package com.bramosystems.oss.player.dev.client;

import com.bramosystems.oss.player.core.client.Plugin;
import com.bramosystems.oss.player.core.client.PluginNotFoundException;
import com.bramosystems.oss.player.core.client.MimePool;
import com.bramosystems.oss.player.core.client.PlayerInfo;
import com.bramosystems.oss.player.core.client.impl.plugin.PlayerManager;
import com.bramosystems.oss.player.util.client.BrowserPlugin;
import com.bramosystems.oss.player.util.client.MimeType;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.user.client.ui.FlexTable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

/**
 *
 * @author Sikiru
 */
public class MimeStuffs extends FlexTable {

    public MimeStuffs() {
        setWidth("90%");
        setCellSpacing(5);
        setCellPadding(5);

        doMimePool2();
//        doMimeTypes();
//        doPlugins();
    }

    private void doPlugins() {
        setHTML(0, 0, "Name");
        setHTML(0, 1, "Plugin");
        setHTML(0, 2, "Description");

        EnumMap<Plugin, PluginBean> _mimes = new EnumMap<Plugin, PluginBean>(Plugin.class);

        JsArray<BrowserPlugin> plugins = BrowserPlugin.getPlugins();
        for (int plugs = 0; plugs < plugins.length(); plugs++) {
            BrowserPlugin bp = plugins.get(plugs);
            Plugin plug = toPlugin(bp);
            if (plug == Plugin.Auto) {
                continue;
            }

            for (int i = 0; i < bp.getMimeTypeCount(); i++) {
                MimeType mt = bp.getMimeType(i);
                if (isApplicableMimeType(mt)) {
                    PluginBean mb = _mimes.get(plug);
                    if (mb == null) {
                        mb = new PluginBean(bp.getName(), mt.getType(), plug);
                    }
                    mb.addSuffixes(mt.getSuffixes());
                    _mimes.put(plug, mb);
                }
            }
        }

        Iterator<Plugin> types = _mimes.keySet().iterator();
        int row = 1;
        while (types.hasNext()) {
            PluginBean pb = _mimes.get(types.next());
            setHTML(row, 0, pb.getName());
            setHTML(row, 1, pb.getPlugin().name());
//            setHTML(row, 2, pb.getDescription());
            setHTML(row++, 2, pb.getSuffixes());
        }
    }

    private boolean isApplicableMimeType(MimeType mt) {
        String type = mt.getType().toLowerCase();
        if (type.startsWith("audio") || type.startsWith("video")) {
            return true;
        }
        return false;
    }

    private void doMimeTypes() {
        setHTML(0, 0, "Type");
        setHTML(0, 1, "Description");
        setHTML(0, 2, "Enabled");
        setHTML(0, 3, "Suffixes");

        HashMap<String, MimeBean> _mimes = new HashMap<String, MimeBean>();
        JsArray<MimeType> mimes = MimeType.getMimeTypes();
        for (int i = 0; i < mimes.length(); i++) {
            try {
                MimeType mt = mimes.get(i);
                if (isApplicableMimeType(mt)) {
                    MimeBean mb = _mimes.get(mt.getType());
                    if (mb == null) {
                        mb = new MimeBean(mt.getType(), mt.getDescription());
                    }
                    PluginBean pb = new PluginBean(mt.getEnabledPlugin() != null
                            ? mt.getEnabledPlugin().getName() : "", mt.getType());
                    pb.addSuffixes(mt.getSuffixes());

                    mb.getPlugins().add(pb);
                    _mimes.put(mt.getType(), mb);
                }
            } catch (PluginNotFoundException ex) {
            }
        }

        int row = 1;
        Iterator<String> types = _mimes.keySet().iterator();
        while (types.hasNext()) {
            String _type = types.next();
            MimeBean _mb = _mimes.get(_type);
            setHTML(row, 0, _type);
            setHTML(row, 1, _mb.getDesc());
            for (int k = 0; k < _mb.getPlugins().size(); k++, row++) {
                setHTML(row, 2, _mb.getPlugins().get(k).getName());
                setHTML(row, 3, _mb.getPlugins().get(k).getSuffixes());
            }
            row++;
        }
    }

    private void doMimePool() throws PluginNotFoundException {
        MimePool pool = MimePool.instance;
        int row = 0;

        setHTML(row, 0, "Plugin");
        setHTML(row++, 1, "Suffixes");
        for (Plugin plug : Plugin.values()) {
            Set<String> suf = pool.getRegisteredExtensions(plug);
            setHTML(row, 0, plug.name());
            setHTML(row++, 1, suf != null ? suf.toString() : "-");
        }

        setHTML(row, 0, "Plugin");
        setHTML(row++, 1, "Protocols");
        for (Plugin plug : Plugin.values()) {
            Set<String> prot = pool.getRegisteredProtocols(plug);
            setHTML(row, 0, plug.name());
            setHTML(row++, 1, prot != null ? prot.toString() : "-");
        }
    }

    private void doMimePool2() {
        int row = 0;

        setHTML(row, 0, "Provider");
        setHTML(row, 1, "Player");
        setHTML(row, 2, "Plugin Version");
        setHTML(row, 3, "Suffixes");
        setHTML(row++, 4, "Protocols");

        for (String prov : PlayerManager.getInstance().getProviders()) {
            for (String plyr : PlayerManager.getInstance().getPlayerNames(prov)) {
                PlayerInfo suf = PlayerManager.getInstance().getPlayerInfo(prov, plyr);
                setHTML(row, 0, prov);
                setHTML(row, 1, suf.getPlayerName());
                setHTML(row, 2, suf.getDetectedPluginVersion().toString());
                setHTML(row, 3, suf.getRegisteredExtensions().toString());
                setHTML(row++, 4, suf.getRegisteredProtocols().toString());
            }
        }
    }

    public Plugin toPlugin(BrowserPlugin bp) {
        Plugin plugin = Plugin.Auto;
        String name = bp.getName().toLowerCase();
        if (name.contains("vlc")) {
            plugin = Plugin.VLCPlayer;
        } else if (name.contains("quicktime")) {
            plugin = Plugin.QuickTimePlayer;
        } else if (name.contains("divx")) {
            plugin = Plugin.DivXPlayer;
        } else if (name.contains("windows media player")) {
            plugin = Plugin.WinMediaPlayer;
        } else if (name.contains("shockwave flash")) {
            plugin = Plugin.FlashPlayer;
        }
        return plugin;
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

        private String name, mimeType;
        private TreeSet<String> suffixes;
        private Plugin plugin;

        public PluginBean(String name, String mimeType) {
            this(name, mimeType, Plugin.Auto);
        }

        public PluginBean(String name, String mimeType, Plugin plugin) {
            this.name = name;
            this.mimeType = mimeType;
            this.suffixes = new TreeSet<String>();
            this.plugin = plugin;
        }

        public Plugin getPlugin() {
            return plugin;
        }

        public String getSuffixes() {
            return suffixes.toString();
        }

        public void addSuffixes(String suffixes) {
            this.suffixes.addAll(Arrays.asList(suffixes.split(",")));
        }

        public String getMimeType() {
            return mimeType;
        }

        public String getName() {
            return name;
        }
    }
}
