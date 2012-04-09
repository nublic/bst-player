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
    import com.bramosystems.oss.player.external.Log;
    import com.bramosystems.oss.player.events.SeekChangedEvent;

    import flash.events.*;

    import mx.containers.*;
    import mx.controls.*;
    import mx.controls.scrollClasses.ScrollThumb;
    import mx.events.FlexEvent;
    import mx.managers.ToolTipManager;

    public class SeekbarX extends Canvas {
        private const HEIGHT:uint = 8;
        private var loading:Canvas, playing:Canvas;
        private var tip:ToolTip;
        private var _duration:Number;
        private var thumb:ScrollThumb;
        private var thumbDragging:Boolean;

        public function SeekbarX() {
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

            thumb = new ScrollThumb();
            thumb.autoRepeat = true;
            thumb.width = 10;
            thumb.height = 10;
            thumb.addEventListener(MouseEvent.MOUSE_DOWN, onThumbDrag);
            thumb.addEventListener(MouseEvent.MOUSE_UP, onThumbDrag);
            thumb.addEventListener(MouseEvent.MOUSE_MOVE, onThumbDrag);
 //           thumb.addEventListener(FlexEvent.BUTTON_DOWN, onThumbDown);
            addChild(thumb);
 
            loading.addEventListener(MouseEvent.MOUSE_UP, onThumbDrag);
            playing.addEventListener(MouseEvent.MOUSE_UP, onThumbDrag);
            loading.addEventListener(MouseEvent.MOUSE_MOVE, onThumbDrag);
            playing.addEventListener(MouseEvent.MOUSE_MOVE, onThumbDrag);
       }

        /********************** Events Callback Hooks *************************/
        public function updateLoadingProgress(progress:Number):void {
            loading.percentWidth = progress * 100;
        }

        public function updatePlayingProgress(progress:Number, duration:Number):void {
            playing.percentWidth = progress / duration * 100;
            var tbpos:Number = progress / duration * width;
            if(tbpos < 5)
                thumb.x = 0;
            else if(tbpos >= (width - 5))
                thumb.x = tbpos - 10;
            else
                thumb.x = tbpos - 5;
            _duration = duration;
        }

        /**************** BUTTON CLICK EVENTS ****************************/
        private function onPrev(event:MouseEvent):void {
        }

        private function onThumbDrag(evt:MouseEvent):void {
            var pos:int = 0;
            switch(evt.type) {
                case MouseEvent.MOUSE_DOWN:
                    thumbDragging = true;
                    break;
                case MouseEvent.MOUSE_UP:
                    thumbDragging = false;
                    break;
                case MouseEvent.MOUSE_MOVE:
                        pos = evt.stageX - x - evt.localX;
                    if(thumbDragging) {
                      thumb.x = pos;
                    }
                    break;
            }
            Log.info("Seek: dragging - " + thumbDragging + "; pos - " + pos);
        }

         private function onThumbDown(evt:FlexEvent):void {
             Log.info("Seek: button down - ");
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