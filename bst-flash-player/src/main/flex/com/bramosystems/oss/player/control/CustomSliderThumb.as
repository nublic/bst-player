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

package com.bramosystems.oss.player.control {

    import flash.display.*;
    import flash.events.Event;
    import mx.core.UIComponent;
    import mx.containers.*;
    import mx.skins.ProgrammaticSkin;
    import mx.events.ResizeEvent;

    public class CustomSliderThumb extends ProgrammaticSkin {

        public function CustomSliderThumb(){
//            x = 0;
//            setStyle("bottom", "0");
//            height = 20;
//            percentWidth = 100;
//            addEventListener(Event.ADDED, onAdded);
//                xPosition = 0;
        }

        override protected function updateDisplayList(w:Number, h:Number): void {
        }

        private function onAdded(event:Event):void {
        }

        private function drawPause(): void {
            graphics.beginFill(0xffffaa);
            graphics.drawRoundRect(2, 2, 4, 16, 1);
            graphics.drawRoundRect(10, 2, 4, 16, 1);
            graphics.endFill();
        }
    }
}