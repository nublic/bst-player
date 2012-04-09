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

    import com.bramosystems.oss.player.*;
    import com.bramosystems.oss.player.events.*;
    import com.bramosystems.oss.player.external.Log;

    public class PlaylistManager {
        private var playlist:Playlist;
        private var setting:Setting;
        private var listIndex:int;
        private var shuffledIndexes:Array;

        private var shuffleOn:Boolean;
        private var loopCount:int;

        public function PlaylistManager(playlist:Playlist, setting:Setting) {
            this.playlist = playlist;
            resetIndex();

            shuffleOn = setting.isShuffleEnabled();
            loopCount = setting.getLoopCount();
            setting.addEventListener(SettingChangedEvent.SHUFFLE_CHANGED, updateShuffle);
            setting.addEventListener(SettingChangedEvent.LOOP_COUNT_CHANGED, updateLoopCount);
            playlist.addEventListener(PlaylistEvent.CLEARED, updatePlaylist);
            playlist.addEventListener(PlaylistEvent.ADDED, updatePlaylist);
            playlist.addEventListener(PlaylistEvent.REMOVED, updatePlaylist);

            if(shuffleOn) {
                shuffleList();
            }
        }

        public function resetIndex():void {
            listIndex = -1;
        }

        public function getListIndex():int {
            var _index:int = shuffleOn ? shuffledIndexes[listIndex] : listIndex;
            return _index;
        }

        private function updateShuffle(event:SettingChangedEvent):void {
            shuffleOn = Boolean(event.newValue);
            if(shuffleOn) {
                shuffleList();
            }
        }

        private function updatePlaylist(event:PlaylistEvent):void {
            if(shuffleOn) {
                shuffleList();
            }
        }

        private function updateLoopCount(event:SettingChangedEvent):void {
            loopCount = parseInt(event.newValue);
        }

        private function shuffleList():void {
           shuffledIndexes = playlist.getShuffledIndexes();
           Log.info("Indexes : " + shuffledIndexes);
        }

        /**************************** PLAYLIST INDEX MANAGEMENT *************************/
        public function getNextURLEntry():String {
             updateListIndex(true);
             if(listIndex < 0)
                 return null;

             var _index:int = shuffleOn ? shuffledIndexes[listIndex] : listIndex;
             return playlist.getEntry(_index).getFileName();
        }

        public function getPrevURLEntry():String {
            updateListIndex(false);
            if(listIndex < 0)
                 return null;

             var _index:int = shuffleOn ? shuffledIndexes[listIndex] : listIndex;
             return playlist.getEntry(_index).getFileName();
       }

        public function getURLEntry(index:int):String {
            if((index < 0) || (index >= playlist.size()))
                return null;

            listIndex = index;
            return playlist.getEntry(listIndex).getFileName();
        }

        public function updateListIndex(forward:Boolean):void {
            if(forward) {   // get next
                if (listIndex < (playlist.size() - 1)) {
                    listIndex++;
                } else {    // end of list
                    if(loopCount > 1) {
                        loopCount--;    // play again ...
                        listIndex = 0;
                    } else if(loopCount < 0) {
                        listIndex = 0;  // play forever ...
                    } else {
                        listIndex = -1;     // stop ...
                    }
                    if(shuffleOn) {
                        shuffleList();
                    }
                }
            } else {    // get previous
                // TODO: tentative principle, check if actual behaviour is the required!
                if(listIndex > 0) {
                    listIndex--;
                } else {
                    if(loopCount > 1) {
                        loopCount--;    // play again ...
                        listIndex = playlist.size() - 1;
                    } else if(loopCount < 0) {
                        listIndex = playlist.size() - 1;  // play forever ...
                    } else {
                        listIndex = -1;     // list exhausted, stop ...
                    }
                    if(shuffleOn) {
                        shuffleList();
                    }
                }
            }
        }
    }
}