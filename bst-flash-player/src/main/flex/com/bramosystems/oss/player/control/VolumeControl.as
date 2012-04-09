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

    import com.bramosystems.oss.player.Setting;
    import com.bramosystems.oss.player.events.SettingChangedEvent;

    import flash.display.*;
    import flash.events.*;
    import mx.core.UIComponent;
    import mx.containers.*;
    import mx.controls.*;
    import mx.events.ResizeEvent;

    public class VolumeControl extends HBox {

        private var b20:UIComponent, b40:UIComponent;
        private var b60:UIComponent, b80:UIComponent;
        private var b100:UIComponent;
        private var setting:Setting;

        private const BAR_WIDTH:uint = 1;

        public function VolumeControl(_setting:Setting) {
            setting = _setting;

            setStyle("horizontalGap", BAR_WIDTH + 1);
            
            b20 = new UIComponent();
            b40 = new UIComponent();
            b60 = new UIComponent();
            b80 = new UIComponent();
            b100 = new UIComponent();

            initBar(b20, 20);
            initBar(b40, 40);
            initBar(b60, 60);
            initBar(b80, 80);
            initBar(b100, 100);

            renderBars(null);

            addChild(b20);
            addChild(b40);
            addChild(b60);
            addChild(b80);
            addChild(b100);

            setting.addEventListener(SettingChangedEvent.VOLUME_CHANGED, renderBars);
        }

        private function initBar(comp:UIComponent, level:uint):void {
            comp.width = BAR_WIDTH;
            comp.height = 16;
            comp.buttonMode = true;
            comp.addEventListener(MouseEvent.CLICK, function(event:MouseEvent):void {
                setting.setVolume(level / 100.0);
            });
        }

        private function renderBars(event:SettingChangedEvent):void {
            renderBar(b20, 20);
            renderBar(b40, 40);
            renderBar(b60, 60);
            renderBar(b80, 80);
            renderBar(b100, 100);
        }

        /********************** UI Stuff *******************************/
        private function renderBar(comp:UIComponent, level:uint):void {
            var _barHeight:uint = comp.height * level / 100;
            var _color:uint = setting.getVolume() >= (level / 100.0) ? Controller.ACTIVE_COLOR :
                    Controller.INACTIVE_COLOR;

            comp.graphics.lineStyle(1, _color);
            comp.graphics.beginFill(_color);
            comp.graphics.drawRoundRect(0, comp.height - _barHeight, BAR_WIDTH, _barHeight, 1);
            comp.graphics.endFill();
        }
    }
}