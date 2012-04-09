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
package com.bramosystems.oss.player.playlist.client.impl;

import com.bramosystems.oss.player.core.client.PlayTime;
import com.bramosystems.oss.player.playlist.client.ParseException;
import com.bramosystems.oss.player.playlist.client.asx.ASXEntry;
import com.bramosystems.oss.player.playlist.client.asx.ASXPlaylist;
import com.bramosystems.oss.player.playlist.client.asx.Ref;
import com.bramosystems.oss.player.playlist.client.asx.Repeat;
import com.google.gwt.xml.client.XMLParser;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Stack;

/**
 *
 * @author Sikirulai Braheem <sbraheem at bramosystems.com>
 */
public class ASXHandler implements SAXHandler {

    private Stack<ASXNodeNames> parentNodes = new Stack<ASXNodeNames>();
    private ASXPlaylist playlist;
    private ASXEntry entry;
    private Ref ref;
    private Repeat repeat;
    private Iterator<String> it;

    private ASXNodeNames getNodeName(String name) {
        return ASXNodeNames.valueOf("_" + name);
    }

    @Override
    public void onNodeStart(String nodeName, HashMap<String, String> attr, String namespaceURI) throws ParseException {
        String href = null;
        try {
            switch (getNodeName(nodeName.toLowerCase())) {
                case _asx:
                    parentNodes.push(ASXNodeNames._asx);
                    it = attr.keySet().iterator();
                    while (it.hasNext()) {
                        String ky = it.next();
                        if (ky.equalsIgnoreCase("version")) {
                            playlist.setVersion(Double.parseDouble(attr.get(ky)));
                        } else if (ky.equalsIgnoreCase("previewmode")) {
                            playlist.setPreviewMode(attr.get(ky).equalsIgnoreCase("yes"));
//                        } else if (ky.equalsIgnoreCase("bannerbar")) {
//                            playlist.setBannerBar(attr.get(ky).equalsIgnoreCase("auto")
//                                    ? ASXPlaylist.BannerBar.Auto : ASXPlaylist.BannerBar.Fixed);
                        }
                    }
                    break;
                case _entry:
                    parentNodes.push(ASXNodeNames._entry);
                    entry = new ASXEntry();
                    it = attr.keySet().iterator();
                    while (it.hasNext()) {
                        String ky = it.next();
                        if (ky.equalsIgnoreCase("clientSkip")) {
                            entry.setClientSkip(attr.get(ky).equalsIgnoreCase("yes"));
                        } else if (ky.equalsIgnoreCase("skipIfRef")) {
                            entry.setClientSkip(attr.get(ky).equalsIgnoreCase("yes"));
                        }
                    }
                    break;
                case _entryref:
                    it = attr.keySet().iterator();
                    while (it.hasNext()) {
                        String ky = it.next();
                        if (ky.equalsIgnoreCase("href")) {
                            href = attr.get(ky);
                            break;
                        }
                    }
                    switch (parentNodes.peek()) {
                        case _asx:
                            playlist.getEntryRefs().add(href);
                            break;
                        case _repeat:
                            repeat.getEntryRefs().add(href);
                    }
                    break;
                    /*
                case _base:
                    it = attr.keySet().iterator();
                    while (it.hasNext()) {
                        String ky = it.next();
                        if (ky.equalsIgnoreCase("href")) {
                            href = attr.get(ky);
                            break;
                        }
                    }
                    switch (parentNodes.peek()) {
                        case _asx:
                            if (playlist.getBaseHref() == null) {
                                playlist.setBaseHref(href);
                            }
                            break;
                        case _entry:
                            if (entry.getBaseHref() == null) {
                                entry.setBaseHref(href);
                            }
                    }
                    break;
                    */
                case _duration:
                    it = attr.keySet().iterator();
                    while (it.hasNext()) {
                        String ky = it.next();
                        if (ky.equalsIgnoreCase("value")) {
                            href = attr.get(ky);
                            break;
                        }
                    }
                    switch (parentNodes.peek()) {
                        case _entry:
                            if (entry.getDuration() == null) {
                                entry.setDuration(new PlayTime(href));
                            }
                            break;
                        case _ref:
                            if (ref.getDuration() == null) {
                                ref.setDuration(new PlayTime(href));
                            }
                    }
                    break;
                case _moreinfo:
                    it = attr.keySet().iterator();
                    while (it.hasNext()) {
                        String ky = it.next();
                        if (ky.equalsIgnoreCase("href")) {
                            href = attr.get(ky);
                            break;
                        }
                    }
                    switch (parentNodes.peek()) {
                        case _asx:
                            if (playlist.getMoreInfoHref() == null) {
                                playlist.setMoreInfoHref(href);
                            }
                            break;
                        case _entry:
                            if (entry.getMoreInfoHref() == null) {
                                entry.setMoreInfoHref(href);
                            }
                    }
                    break;
                case _param:
                    it = attr.keySet().iterator();
                    while (it.hasNext()) {
                        String ky = it.next();
                        switch (parentNodes.peek()) {
                            case _asx:
                                playlist.getParams().put(ky, attr.get(ky));
                                break;
                            case _entry:
                                entry.getParams().put(ky, attr.get(ky));
                        }
                    }
                    break;
                case _ref:
                    parentNodes.push(ASXNodeNames._ref);
                    ref = new Ref();
                    it = attr.keySet().iterator();
                    while (it.hasNext()) {
                        String ky = it.next();
                        if (ky.equalsIgnoreCase("href")) {
                            ref.setHref(attr.get(ky));
                        }
                    }
                    break;
                case _repeat:
                    repeat = new Repeat();
                    it = attr.keySet().iterator();
                    while (it.hasNext()) {
                        String ky = it.next();
                        if (ky.equalsIgnoreCase("count")) {
                            repeat.setCount(Integer.parseInt(attr.get(ky)));
                        }
                    }
                    parentNodes.push(ASXNodeNames._repeat);
                    break;
                case _starttime:
                    it = attr.keySet().iterator();
                    while (it.hasNext()) {
                        String ky = it.next();
                        if (ky.equalsIgnoreCase("value")) {
                            href = attr.get(ky);
                            break;
                        }
                    }
                    switch (parentNodes.peek()) {
                        case _entry:
                            if (entry.getStartTime() == null) {
                                entry.setStartTime(new PlayTime(href));
                            }
                            break;
                        case _ref:
                            if (ref.getStartTime() == null) {
                                ref.setStartTime(new PlayTime(href));
                            }
                    }
                    break;
            }
        } catch (Exception e) {
            throw new ParseException("Parse Error : " + nodeName, e);
        }
    }

    @Override
    public void setNodeValue(String nodeName, String value) throws ParseException {
        try {
            switch (getNodeName(nodeName.toLowerCase())) {
                case _abstract:
                    switch (parentNodes.peek()) {
                        case _asx:
                            if (playlist.getAbstract() == null) {
                                playlist.setAbstract(value);
                            }
                            break;
                        case _entry:
                            if (entry.getAbstract() == null) {
                                entry.setAbstract(value);
                            }
                    }
                    break;
                case _author:
                    switch (parentNodes.peek()) {
                        case _asx:
                            if (playlist.getAuthor() == null) {
                                playlist.setAuthor(value);
                            }
                            break;
                        case _entry:
                            if (entry.getAuthor() == null) {
                                entry.setAuthor(value);
                            }
                    }
                    break;
                case _copyright:
                    switch (parentNodes.peek()) {
                        case _asx:
                            if (playlist.getCopyright() == null) {
                                playlist.setCopyright(value);
                            }
                            break;
                        case _entry:
                            if (entry.getCopyright() == null) {
                                entry.setCopyright(value);
                            }
                    }
                    break;
                case _title:
                    switch (parentNodes.peek()) {
                        case _asx:
                            if (playlist.getTitle() == null) {
                                playlist.setTitle(value);
                            }
                            break;
                        case _entry:
                            if (entry.getTitle() == null) {
                                entry.setTitle(value);
                            }
                    }
                    break;
            }
        } catch (Exception e) {
            throw new ParseException("Parse Error : " + nodeName, e);
        }
    }

    @Override
    public void onNodeEnd(String nodeName) throws ParseException {
        try {
            switch (getNodeName(nodeName.toLowerCase())) {
                case _entry:
                    parentNodes.pop();
                    switch (parentNodes.peek()) {
                        case _repeat:
                            repeat.getEntries().add(entry);
                            break;
                        case _asx:
                            playlist.getEntries().add(entry);
                   }
                    break;
                case _ref:
                    entry.getRefs().add(ref);
                    parentNodes.pop();
                    break;
                case _repeat:
                    if (playlist.getRepeat() == null) {
                        playlist.setRepeat(repeat);
                    }
                case _asx:
                    parentNodes.pop();
            }
        } catch (Exception e) {
            throw new ParseException("Parse Error : " + nodeName, e);
        }
    }

    public ASXPlaylist getPlaylist(String asx) throws ParseException {
        playlist = new ASXPlaylist();
        SAXParser sax = new SAXParser(this);
        sax.parseDocument(XMLParser.parse(asx));
        return playlist;
    }

    private enum ASXNodeNames {
        _abstract, _asx, _author, _base, _copyright, _duration, _endmarker, _entry,
        _entryref, _event, _moreinfo, _param, _ref, _repeat, _startmarker, _starttime,
        _title
    }
}
