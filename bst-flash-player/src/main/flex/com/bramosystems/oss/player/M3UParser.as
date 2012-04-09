/*
 * Copyright 2009 Sikirulai Braheem
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.bramosystems.oss.player {

    import com.bramosystems.oss.player.playlist.*;

    public class M3UParser {
        private var mp3BaseUrl:String;

        private var descriptor:RegExp = /^#EXTM3U$/m;
        private var recordMarker:String = "#EXTINF:";
        private var lengthRegex:RegExp = /\d+,/;
        private var nameRegex:RegExp = /,.*$/m;
        private var slashRegex:RegExp = /[\\]/gm;
        private var fileRegex:RegExp = /^\d+,.*$/m;
        private var trimRegex:RegExp = /(?:\r|\n)*/g;
        private var absUrlRegex:RegExp = /^(?:http|ftp|https):\/\//m;
        private var relUrlRegex:RegExp = /^\//m;

        public function M3UParser(m3uFileBaseURL:String) {
            mp3BaseUrl = m3uFileBaseURL;
        }

        public function parse(m3uData:String):Array {
            var mp3s:Array = new Array();

            // check for format descriptor ...
            if(m3uData.search(descriptor) != 0) {
                // invalid file, throw error ...
                throw new SyntaxError("Invalid playlist file!");
            } else {
                // parse file ...
                var entries:Array = m3uData.split(recordMarker);
                for(var i:int = 1; i < entries.length; i++) {
                    mp3s.push(parseEntry(entries[i]));
                }
            }
            return mp3s;
        }

        private function parseEntry(m3uEntry:String):PlaylistEntry {
            m3uEntry = m3uEntry.replace(slashRegex, "/");
            return new PlaylistEntry(parseInt(m3uEntry.match(lengthRegex).toString().replace(",", "")),
                m3uEntry.match(nameRegex).toString().replace(",", ""),
                resolveFileURL(mp3BaseUrl, m3uEntry.replace(fileRegex, "")));
        }

        public function resolveFileURL(baseUrl:String, url:String):String {
            url = url.replace(trimRegex, "");
            if((url.search(absUrlRegex) < 0) && (url.search(relUrlRegex) != 0)) {
                return encodeURI(baseUrl + "/" + url);
            } else {
                return encodeURI(url);
            }
        }
    }
}
