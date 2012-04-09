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

    import com.bramosystems.oss.player.*;
    import com.bramosystems.oss.player.events.SeekChangedEvent;

    import flash.events.*;

    import mx.containers.*;
    import mx.controls.*;
    import mx.managers.ToolTipManager;

    public class Seekbar extends Canvas {
        private const HEIGHT:uint = 8;
        private var loading:Canvas, playing:Canvas;
        private var tip:ToolTip;
        private var _duration:Number;

        public function Seekbar() {
            x = 0;
            y = 0;
            height = HEIGHT;
            percentWidth = 100;
            setStyle("backgroundColor", Controller.INACTIVE_COLOR);

            loading = new Canvas();
            loading.alpha = 0.25;

            playing = new Canvas();

            initProgressBar(loading, false);
            initProgressBar(playing, true);

            addChild(loading);
            addChild(playing);
       }

        /********************** Events Callback Hooks *************************/
        public function updateLoadingProgress(progress:Number):void {
            loading.percentWidth = progress * 100;
        }

        public function updatePlayingProgress(progress:Number, duration:Number):void {
            playing.percentWidth = progress / duration * 100;
            _duration = duration;
        }
        /**************** BUTTON CLICK EVENTS ****************************/
        private function onPrev(event:MouseEvent):void {
        }

       /********************** UI Stuffs *******************************/
        private function initProgressBar(bar:Canvas, playing:Boolean):void {
            bar.x = 0;
            bar.y = 0;
            bar.height = HEIGHT;
            bar.buttonMode = true;
            bar.setStyle("backgroundColor", Controller.ACTIVE_COLOR);
            bar.addEventListener(MouseEvent.CLICK, updatePlayPosition);
            bar.addEventListener(MouseEvent.MOUSE_OVER, updateTip);
            bar.addEventListener(MouseEvent.MOUSE_OUT, clearTip);
            bar.addEventListener(MouseEvent.MOUSE_MOVE, updateTip);
        }

        /******************** TOOL TIP METHODS ****************************/
        private function updatePlayPosition(event:MouseEvent):void {
            dispatchEvent(new SeekChangedEvent(event.localX / width * _duration));
        }

        private function updateTip(event:MouseEvent):void {
            if(tip) {
                clearTip(null);
            }
            tip = ToolTipManager.createToolTip(TimeInfo.formatTime(event.localX / width * _duration),
                            event.stageX - 20, parent.y - parent.height) as ToolTip;            
        }

        private function clearTip(event:MouseEvent):void {
            ToolTipManager.destroyToolTip(tip);
            tip = null;
        }
    }
}