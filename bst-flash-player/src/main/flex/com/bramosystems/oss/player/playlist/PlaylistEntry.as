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

package com.bramosystems.oss.player.playlist {

    public class PlaylistEntry {
        private var trackLength:uint;
        private var trackName:String;
        private var fileName:String;

        public function PlaylistEntry(length:uint, name:String, file:String) {
            trackLength = length;
            trackName = name;
            fileName = file;
        }

        public function getTrackLength():uint {
            return trackLength;
        }

        public function getTrackName():String {
            return trackName;
        }

        public function getFileName():String {
            return fileName;
        }
    }
}
