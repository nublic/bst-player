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
package com.bramosystems.oss.player.playlist.client;

import com.bramosystems.oss.player.playlist.client.impl.ASXHandler;
import com.bramosystems.oss.player.playlist.client.impl.XSPFHandler;
import com.bramosystems.oss.player.playlist.client.asx.ASXPlaylist;
import com.bramosystems.oss.player.playlist.client.impl.JSPFPlaylist;
import com.bramosystems.oss.player.playlist.client.spf.SPFPlaylist;
import com.google.gwt.core.client.JsonUtils;

/**
 * Factory class handles parsing of various playlist formats
 * 
 * @since 1.3
 * @author Sikirulai Braheem <sbraheem at bramosystems.com>
 */
public class PlaylistFactory {

    /**
     * Parses playlist data in JSPF format to SPFPlaylist object
     * 
     * @param jspf playlist data in JSPF format
     * 
     * @return SPFPlaylist object
     * @throws ParseException if an error occurs during parsing
     */
    public static SPFPlaylist parseJspfPlaylist(String jspf) throws ParseException {
        try {
            return ((JSPFPlaylist) JsonUtils.safeEval(jspf)).getPlaylist();
        } catch (Exception e) {
            throw new ParseException("Parse Error", e);
        }
    }

    /**
     * Parses XML playlist in XSPF format to SPFPlaylist object
     * 
     * @param xspf playlist data in XSPF format
     * @return SPFPlaylist object
     * @throws ParseException if a error occurs during parsing
     */
    public static SPFPlaylist parseXspfPlaylist(String xspf) throws ParseException {
        return new XSPFHandler().getPlaylist(xspf);
    }

    /**
     * Parses XML playlist in ASX format to ASXPlaylist object
     * 
     * @param asx playlist data in ASX format
     * 
     * @return ASXPlaylist object
     * @throws ParseException if an error occurs during parsing 
     */
    public static ASXPlaylist parseAsxPlaylist(String asx) throws ParseException {
        return new ASXHandler().getPlaylist(asx);
    }
}
