/*
 * Copyright 2009 Sikirulai Braheem <sbraheem at bramosystems dot com>
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

    import flash.events.EventDispatcher;
    import flash.media.*;

    import com.bramosystems.oss.player.external.Log;
    import com.bramosystems.oss.player.events.SettingChangedEvent;

    public class Setting extends EventDispatcher {
        private var volume:Number;
        private var shuffle:Boolean;
        private var loopCount:int;

        public function Setting() {
            volume = 0.5;
            shuffle = false;
            loopCount = 1;
        }

        public function setVolume(volume:Number):void {
            this.volume = volume;
            dispatchEvent(new SettingChangedEvent(SettingChangedEvent.VOLUME_CHANGED, String(volume)));
            Log.info("Volume set to " + (volume * 100).toFixed(0) + "%");
        }

        public function getVolume():Number {
            return volume;
        }

        public function setShuffleEnabled(shuffle:Boolean):void {
            this.shuffle = shuffle;
            dispatchEvent(new SettingChangedEvent(SettingChangedEvent.SHUFFLE_CHANGED, String(this.shuffle)));
            Log.info(shuffle ? "Shuffle turned on" : "Shuffle turned off");
        }

        public function isShuffleEnabled():Boolean {
            return shuffle;
        }

        public function setLoopCount(count:int):void {
            loopCount = count;
            dispatchEvent(new SettingChangedEvent(SettingChangedEvent.LOOP_COUNT_CHANGED, String(loopCount)));
            Log.info("Loop count changed : " + loopCount);
        }

        public function getLoopCount():int {
            return loopCount;
        }
    }
}
