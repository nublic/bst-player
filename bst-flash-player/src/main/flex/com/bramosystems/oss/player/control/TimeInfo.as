/*
 * Copyright 2010 Sikirulai Braheem
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

package com.bramosystems.oss.player.control {

    import flash.errors.*;
    import mx.controls.Label;

    public class TimeInfo extends Label {

        public function TimeInfo() {
            setStyle('fontWeight', 'bold');
            setStyle('color', '0xffffff');

            updateTime(0,0);
        }

        public function updateTime(currentTime:Number, duration:Number):void {
            text = formatTime(currentTime) + "/" + formatTime(duration);
        }

        /**
         * Formats the specified time (in milliseconds) into time string in the
         * format <code>hh:mm:ss</code>.
         *
         * <p>The hours part of the formatted time is omitted if {@code milliseconds} time
         * is less than 60 minutes.
         *
         * @param milliSeconds media time to be formatted
         * @return the formatted time as String
         */
        public static function formatTime(ms:Number):String {
            var secth:uint = 0, secs:uint = 0, min:uint = 0, hrs:uint = 0;

            try {
                secth = ms % 1000;    // millisecs.
                ms /= 1000;

                secs = ms % 60;   // secs.
                ms /= 60;

                min = ms % 60;   // min.
                ms /= 60;

                hrs = ms % 60;   // hrs.
                ms /= 60;
            } catch (e:Error) {
                // catch exceptions like division by zero...
            }
            return (hrs > 0 ? hrs + ":" : "") + (min < 10 ? "0" + min : min) + ":"
                    + (secs < 10 ? "0" + secs : secs);
        }
    }
}