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

import com.bramosystems.oss.player.playlist.client.ParseException;
import com.bramosystems.oss.player.playlist.client.spf.Attribution;
import com.bramosystems.oss.player.playlist.client.spf.SPFPlaylist;
import com.bramosystems.oss.player.playlist.client.spf.Track;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.xml.client.XMLParser;
import java.util.HashMap;

/**
 *
 * @author Sikirulai Braheem <sbraheem at bramosystems.com>
 */
public class XSPFHandler implements SAXHandler {

    private SPFPlaylist playlist;
    private Track track;
    private Attribution attribution;
    private XSPFNodeNames parentNode;

    @Override
    public void onNodeStart(String nodeName, HashMap<String, String> attr, String namespaceURI) throws ParseException {
        try {
            switch (XSPFNodeNames.valueOf(nodeName.toLowerCase())) {
                case playlist:
                    JsArray<Track> t = JsArray.createArray().cast();
                    playlist.setTracks(t);
                    parentNode = XSPFNodeNames.playlist;
                    break;
                case attribution:
                    attribution = Attribution.createObject().cast();
                    parentNode = XSPFNodeNames.attribution;
                    break;
                case track:
                    track = Track.createObject().cast();
                    parentNode = XSPFNodeNames.track;
                    break;
            }
        } catch (Exception e) {
            throw new ParseException("Parse Error : " + nodeName, e);
        }
    }

    @Override
    public void setNodeValue(String nodeName, String value) throws ParseException {
        try {
            switch (XSPFNodeNames.valueOf(nodeName.toLowerCase())) {
                case title:
                    switch (parentNode) {
                        case playlist:
                            playlist.setTitle(value);
                            break;
                        case track:
                            track.setTitle(value);
                    }
                    break;
                case creator:
                    switch (parentNode) {
                        case playlist:
                            playlist.setCreator(value);
                            break;
                        case track:
                            track.setCreator(value);
                    }
                    break;
                case annotation:
                    switch (parentNode) {
                        case playlist:
                            playlist.setAnnotation(value);
                            break;
                        case track:
                            track.setAnnotation(value);
                    }
                    break;
                case info:
                    switch (parentNode) {
                        case playlist:
                            playlist.setInfo(value);
                            break;
                        case track:
                            track.setInfo(value);
                    }
                    break;
                case location:
                    switch (parentNode) {
                        case playlist:
                            playlist.setLocation(value);
                            break;
                        case track:
                            track.getLocation().push(value);
                            break;
                        case attribution:
                            attribution.setLocation(value);
                    }
                    break;
                case identifier:
                    switch (parentNode) {
                        case playlist:
                            playlist.setIdentifier(value);
                            break;
                        case attribution:
                            attribution.setIdentifier(value);
                            break;
                        case track:
                            track.getIdentifier().push(value);
                    }
                    break;
                case image:
                    switch (parentNode) {
                        case playlist:
                            playlist.setImage(value);
                            break;
                        case track:
                            track.setImage(value);
                    }
                    break;
                case date:
                    switch (parentNode) {
                        case playlist:
                            DateTimeFormat dt = DateTimeFormat.getFormat(DateTimeFormat.PredefinedFormat.ISO_8601);
                            playlist.setDate(dt.parse(value));
                            break;
                    }
                    break;
                case license:
                    switch (parentNode) {
                        case playlist:
                            playlist.setLicense(value);
                            break;
                    }
                    break;
                case album:
                    switch (parentNode) {
                        case track:
                            track.setAlbum(value);
                            break;
                    }
                    break;
                case tracknum:
                    switch (parentNode) {
                        case track:
                            track.setTrackNumber(Double.parseDouble(value));
                            break;
                    }
                    break;
                case duration:
                    switch (parentNode) {
                        case track:
                            track.setDuration(Double.parseDouble(value));
                            break;
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
            switch (XSPFNodeNames.valueOf(nodeName.toLowerCase())) {
                case track:
                    playlist.getTracks().push(track);
                    parentNode = XSPFNodeNames.playlist;
                    break;
                case attribution:
                    playlist.setAttribution(attribution);
                    parentNode = XSPFNodeNames.playlist;
                    break;
            }
        } catch (Exception e) {
            throw new ParseException("Parse Error : " + nodeName, e);
        }
    }

    public SPFPlaylist getPlaylist(String xspf) throws ParseException {
        playlist = SPFPlaylist.createObject().cast();
        SAXParser sax = new SAXParser(this);
        sax.parseDocument(XMLParser.parse(xspf));
        return playlist;
    }

    private enum XSPFNodeNames {

        playlist, title, creator, annotation, info, location, identifier, image, date,
        license, attribution, track, album, tracknum, duration, link, meta, tracklist,
        rel, extension, version
    }
}
