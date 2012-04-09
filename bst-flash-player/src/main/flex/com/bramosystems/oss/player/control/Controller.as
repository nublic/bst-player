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
    import com.bramosystems.oss.player.playlist.Playlist;

    import flash.display.*;
    import flash.events.*;
    import flash.utils.Timer;
    import mx.core.*;
    import mx.containers.*;
    import mx.controls.*;
    import mx.events.ResizeEvent;
    import mx.effects.*;

    public class Controller extends HBox {
        public static const ACTIVE_COLOR:uint = 0x1144ff;
        public static const INACTIVE_COLOR:uint = 0xbcbcbc;

        private var play:UIComponent, next:UIComponent;
        private var prev:UIComponent, toggler:UIComponent;
        private var playButtonShowPlay:Boolean = true, autoHideController:Boolean = false;
        private var showController:Boolean = true;

        private var player:Player;
        private var playlist:Playlist;
        private var seekbar:Seekbar;
        private var timeInfo:TimeInfo;

        private const _size:int = 18;
        private const _margin:int = 2;
        private const _ahTimer:Timer = new Timer(1500);

        public function Controller(_player:Player, _setting:Setting, _playlist:Playlist) {
            player = _player;
            playlist = _playlist;

            x = 0;
            percentWidth = 100;

            setStyle("verticalAlign", "middle");
            setStyle("horizontalGap", 4);
            setStyle("borderColor", 0xffffff);
            setStyle("borderSides", "top");
            setStyle("borderStyle", "solid");
            setStyle("borderThickness", 1.5);
            setStyle("backgroundColor", 0x000000);
            setStyle("bottom", 0);

            seekbar = new Seekbar();
            seekbar.addEventListener(SeekChangedEvent.SEEK_CHANGED, onSeekChanged);

            timeInfo = new TimeInfo();

            addChild(getPlay());
            addChild(getPrev());
            addChild(getNext());
            addChild(seekbar);
            addChild(timeInfo);
            addChild(new VolumeControl(_setting));
            addChild(getScreenToggler());

            FlexGlobals.topLevelApplication.stage.addEventListener(FullScreenEvent.FULL_SCREEN, onFullScreen);
            updateDisplay(false);
            _attachEffect();
            _ahTimer.addEventListener(TimerEvent.TIMER, onAHTimer);
        }

        private function enableButton(button:UIComponent, enable:Boolean):void {
            button.enabled = enable;
            button.mouseEnabled = enable;
        }

        private function updateDisplay(fullScreen:Boolean):void {
            setStyle("paddingTop", fullScreen ? 5 : 2);
            setStyle("paddingBottom", fullScreen ? 5 : 2);
            setStyle("paddingLeft", fullScreen ? 5 : 2);
            setStyle("paddingRight", fullScreen ? 5 : 2);

            renderPlay();
            renderNext();
            renderPrev();
            renderToggler(fullScreen);
        }
/*
        public function toggleScreen():void {
            toggler.dispatchEvent();
        }

        public function isFullScreenState():Boolean {
            return Application.application.stage.displayState == StageDisplayState.FULL_SCREEN;
        }
*/
        public function isControllerVisible():Boolean {
            return showController;
        }

        public function setControllerVisible(_visible:Boolean):void {
            showController = _visible;
            if(_visible) {
                _attachEffect();
            } else {
                clearStyle('hideEffect');
                clearStyle('showEffect');
            }
            visible = _visible;
        }

        public function onAHMouseMove(evt:MouseEvent):void {
            _triggerAutoHide();
        }

        public function setAutoHide(autoHide:Boolean):void {
            autoHideController = autoHide;
            _triggerAutoHide();
        }

        public function isAutoHide():Boolean {
            return autoHideController;
        }

        private function _triggerAutoHide():void {
            if(autoHideController && showController) {
                _ahTimer.reset();
                visible = true;
                _ahTimer.start();
            }
        }

        /********************** Events Callback Hooks *************************/
        public function onMediaStateChanged(state:int, listIndex:int):void {
            switch(state) {
                case 1: // loading started
                    onLoadingProgress(0.0);
                    break;
                case 2: // started
                    playButtonShowPlay = false;
                    break;
                case 3: // stopped
                    playButtonShowPlay = true;
                    break;
                case 4: // paused
                    playButtonShowPlay = true;
                    break;
                case 9: // finished
                    playButtonShowPlay = true;
                    onPlayingProgress(0);
                    break;
                case 10: // complete
                    onLoadingProgress(1.0);
                    break;
            }

            switch(state) {
                case 2: // started
                case 4: // paused
                    enableButton(next, listIndex < (playlist.size() - 1));
                    enableButton(prev, listIndex > 0);
                    break;
                case 1: // loading started
                case 9: // finished
                    enableButton(next, false);
                    enableButton(prev, false);
            }

            renderPlay();
            renderPrev();
            renderNext();
        }

        public function onLoadingProgress(progress:Number):void {
            seekbar.updateLoadingProgress(progress);
        }

        public function onPlayingProgress(progress:Number):void {
            seekbar.updatePlayingProgress(progress, player.getDuration());
            timeInfo.updateTime(progress, player.getDuration());
        }

        public function onMetadata():void {
            timeInfo.updateTime(0, player.getDuration());
        }

        /**************** BUTTON CLICK EVENTS ****************************/
        private function togglePlay(event:MouseEvent):void {
            playButtonShowPlay = !playButtonShowPlay;
            if(playButtonShowPlay) {
                player.stop(false);
            } else {
                player.play();
            }
        }

        private function onNext(event:MouseEvent):void {
            player.playNext();
        }

        private function onPrev(event:MouseEvent):void {
            player.playPrev();
        }

        private function onToggleScreen(event:MouseEvent):void {
            switch(FlexGlobals.topLevelApplication.stage.displayState) {
                case StageDisplayState.NORMAL:
                    FlexGlobals.topLevelApplication.stage.displayState = StageDisplayState.FULL_SCREEN;
                    break;
                case StageDisplayState.FULL_SCREEN:
                default:
                    FlexGlobals.topLevelApplication.stage.displayState = StageDisplayState.NORMAL;
            setAutoHide(false);
                    break;
            }
        }

        private function onFullScreen(event:FullScreenEvent):void {
            updateDisplay(event.fullScreen);
        }

        private function onSeekChanged(event:SeekChangedEvent):void {
            player.setPlayPosition(event.newValue);;
        }

        private function onAHTimer(event:TimerEvent):void {
            visible = false;
            _ahTimer.stop();
        }

        /********************** UI Stuffs *******************************/
        private function getPlay():UIComponent {
            play = new UIComponent();
            play.buttonMode = true;
            play.addEventListener(MouseEvent.CLICK, togglePlay);
            play.width = _size * 3/4;
            play.height = _size;
            return play;
        }

        private function getScreenToggler():UIComponent {
            toggler = new UIComponent();
            toggler.width = _size;
            toggler.height = _size;
            toggler.addEventListener(MouseEvent.CLICK, onToggleScreen);
            toggler.buttonMode = true;
            return toggler;
        }

        private function getNext():UIComponent {
            next = new UIComponent();
            next.addEventListener(MouseEvent.CLICK, onNext);
            next.buttonMode = true;
            next.width = _size * 3 / 4;
            next.height = _size;
            return next;
        }

        private function getPrev():UIComponent {
            prev = new UIComponent();
            prev.addEventListener(MouseEvent.CLICK, onPrev);
            prev.width = _size * 3 / 4;
            prev.height = _size;
            prev.buttonMode = true;
            return prev;
        }

        private function _attachEffect():void {
            var heffct:AnimateProperty = new AnimateProperty();
            heffct.property = "bottom";
            heffct.fromValue = 0;
            heffct.toValue = -18;
            heffct.roundValue = true;
            heffct.isStyle = true;
            heffct.duration = 2000;
            heffct.startDelay = 2000;

            var seffct:AnimateProperty = new AnimateProperty();
            seffct.property = "bottom";
            seffct.fromValue = -18;
            seffct.toValue = 0;
            seffct.isStyle = true;
            seffct.duration = 2;

            setStyle('hideEffect', heffct);
            setStyle('showEffect', seffct);
        }

        /******************** RENDERING METHODS ****************************/
        private function renderPlay():void {
            var _color:uint = play.enabled ? ACTIVE_COLOR : INACTIVE_COLOR;

            play.graphics.clear();
            play.graphics.beginFill(_color);
            play.graphics.lineStyle(1, _color);
            if(playButtonShowPlay) {
                play.graphics.moveTo(_margin, _margin);
                play.graphics.lineTo(play.width - (_margin * 2), (play.height - (_margin * 2)) / 2);
                play.graphics.lineTo(_margin, play.height - (_margin * 2));
                play.graphics.lineTo(_margin, _margin);
                play.graphics.endFill();
            } else { // show pause
                play.graphics.drawRoundRect(_margin, _margin, (play.width - (_margin * 2)) / 3,
                        play.height - (_margin * 2), 1);
                play.graphics.drawRoundRect(_margin + ((play.width - (_margin * 2)) * 2 / 3),
                        _margin, (play.width - (_margin * 2)) / 3, play.height - (_margin * 2), 1);
            }
            play.graphics.endFill();
        }

        private function renderNext():void {
            var _color:uint = next.enabled ? ACTIVE_COLOR : INACTIVE_COLOR;

            next.graphics.lineStyle(1, _color);
            next.graphics.beginFill(_color);
            next.graphics.moveTo(_margin, _margin);
            next.graphics.lineTo(next.width - (_margin * 2), (next.height - (_margin * 2)) / 2);
            next.graphics.lineTo(_margin, next.height - (_margin * 2));
            next.graphics.lineTo(_margin, _margin);

            next.graphics.moveTo(next.width - (_margin * 2), _margin);
            next.graphics.lineTo(next.width - (_margin * 2), next.height - (_margin * 2));
            next.graphics.endFill();
        }

        private function renderPrev():void {
            var _color:uint = prev.enabled ? ACTIVE_COLOR : INACTIVE_COLOR;

            prev.graphics.lineStyle(1, _color);
            prev.graphics.beginFill(_color);
            prev.graphics.moveTo(prev.width - (_margin * 2), _margin);
            prev.graphics.lineTo(prev.width - (_margin * 2), prev.height - (_margin * 2));
            prev.graphics.lineTo(_margin, (prev.height - (_margin * 2)) / 2);
            prev.graphics.lineTo(prev.width - (_margin * 2), _margin);

            prev.graphics.moveTo(_margin, _margin);
            prev.graphics.lineTo(_margin, prev.height - (_margin * 2));
            prev.graphics.endFill();
        }

        private function renderToggler(fullScreen:Boolean):void {
            toggler.graphics.lineStyle(1, 0xffffff);
            toggler.graphics.beginFill(fullScreen ? INACTIVE_COLOR : ACTIVE_COLOR);
            toggler.graphics.drawRoundRect(_margin, _margin, toggler.height - (2 * _margin),
                        toggler.width - (2 * _margin), 1);

            toggler.graphics.beginFill(ACTIVE_COLOR);
            toggler.graphics.drawRoundRect(_margin, _margin,
                        (toggler.height - (2 * _margin)) * 2 / 3,
                        (toggler.width - (2 * _margin)) * 2 / 3, 1);
            toggler.graphics.endFill();
        }
    }
}