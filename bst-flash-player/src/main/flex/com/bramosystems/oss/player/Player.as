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

package com.bramosystems.oss.player {
    import com.bramosystems.oss.player.playlist.*;
    import com.bramosystems.oss.player.external.*;
    import com.bramosystems.oss.player.events.*;

    import flash.display.*;
    import flash.errors.*;
    import flash.events.*;
    import flash.geom.*;
    import flash.media.*;
    import flash.net.*;

    import mx.core.*;
    import mx.events.*;

    public class Player extends LayoutContainer {
        private const PLAYING:int = 1;
        private const STOPPED:int = 2;
        private const PAUSED:int = 3;
        private const LOADED:int = 4;
        private const FINISHED:int = 5;

        private var mediaURL:String;
        private var state:int = STOPPED;
        private var player:Engine, mp3:MP3Engine, vdu:VideoEngine;
        private var playlist:Playlist;

        public function Player(setting:Setting, _playlist:Playlist) {
            x = 0;
            percentWidth = 100;
            percentHeight = 100;
            setStyle("horizontalAlign", "center");
            setStyle("verticalAlign", "middle");

            // init engines ...
            mp3 = new MP3Engine();
            mp3.addEventListener(PlayStateEvent.PLAY_STARTED, playStarted);
            mp3.addEventListener(PlayStateEvent.PLAY_STOPPED, playStopped);
            mp3.addEventListener(PlayStateEvent.PLAY_PAUSED, playPaused);
            mp3.addEventListener(PlayStateEvent.PLAY_FINISHED, playFinished);

            vdu = new VideoEngine();
            vdu.addEventListener(PlayStateEvent.PLAY_STARTED, playStarted);
            vdu.addEventListener(PlayStateEvent.PLAY_STOPPED, playStopped);
            vdu.addEventListener(PlayStateEvent.PLAY_PAUSED, playPaused);
            vdu.addEventListener(PlayStateEvent.PLAY_FINISHED, playFinished);
            vdu.addEventListener(MetadataEvent.METADATA_RECEIVED, metaDoVDUSize);

            setting.addEventListener(SettingChangedEvent.VOLUME_CHANGED, onUpdateVolumeEvent); // add vol mgmt on settings
            updateVolume(setting.getVolume());

            playlist = _playlist;
            FlexGlobals.topLevelApplication.stage.addEventListener(FullScreenEvent.FULL_SCREEN, onFullScreen);
        }

        public function setDebugEnabled(enabled:Boolean):void {
//            debug = enabled;
        }

        /************************ PLAYER FUNCTIONS *******************************/
        public function load(mediaURL:String):void {
            playlist.addEventListener(PlaylistEvent.ADDED, playlistAddComplete);
            playlist.clear();
            playlist.add(mediaURL);
        }

        private function playlistAddComplete(event:PlaylistEvent):void {
            playlist.removeEventListener(PlaylistEvent.ADDED, playlistAddComplete);
            loadNext();
        }

        public function loadNext():void {
            var url:String = playlist.getNext(false);
            _load(url);
        }

        public function play():void {
            switch(state) {
                case LOADED:
                    playNext();
                    break;
                case PAUSED:
                case STOPPED:
                    player.play();
                    break;
                case FINISHED:
                    playNext();
                    break;
            }
        }

        public function playNext():Boolean {
            var url:String = playlist.getNext(true);
            if(url != null) {
                _load(url);
                player.play();
                return true;
            } else {
                state = STOPPED;
                return false;
            }
        }

        private function playNextLoop():Boolean {
            var url:String = playlist.getNext(false);
            if(url != null) {
                _load(url);
                player.play();
                return true;
            } else {
                state = STOPPED;
                return false;
            }
        }

        public function playPrev():Boolean {
            var url:String = playlist.getPrev(true);
            if(url != null) {
                _load(url);
                player.play();
                return true;
            } else {
                state = STOPPED;
                return false;
            }
        }

        public function playIndex(index:int):Boolean {
            var url:String = playlist.getEntry(index).getFileName();
            if(url != null) {
                if (playlist.isShuffleOn()) {
                    playlist.shufflePuttingFirst(index);
                    
                } 
                playlist.setIndex(index);
                _load(url);
                player.play();
                return true;
            } else {
                state = STOPPED;
                return false;
            }
        }

        public function stop(rewind:Boolean):void {
            if(rewind) {
                player._stop(true);
            } else {
                player.pause();
            }
        }

        public function getState():int {
            return state;
        }

        public function getDuration():Number {
            return player.getDuration();
        }

        public function getPlayPosition():Number {
            return player.getPlayPosition();
        }

        public function setPlayPosition(pos:Number):void {
            if(state == PLAYING) {
                player.setPlayPosition(pos);
            }
        }

        public function close():void {
            if(player != null) {
               player.close();
            }
        }

        public function getVideoHeight():Number {
            if((player is VideoEngine) && (vdu.metadata.videodatarate)) {
                return vdu.metadata.height;
            }
            return 0;
        }

        public function getVideoWidth():Number {
            if((player is VideoEngine) && (vdu.metadata.videodatarate)) {
                return vdu.metadata.width;
            }
            return 0;
        }

        /************************ MATRIX FUNCTIONS *******************************/
        public function getMatrix():String {
            var _matrix:Matrix = transform.matrix;
            return _matrix.a + "," + _matrix.b + "," + _matrix.c + "," + _matrix.d +
                    "," + _matrix.tx + "," + _matrix.ty;
        }

        public function setMatrix(a:Number, b:Number, c:Number, d:Number, tx:Number, ty:Number):void {
            var _matrix:Matrix = new Matrix(a, b, c, d, tx, ty);
            transform.matrix = _matrix;
        }

        /*************************** PLAY STATE HANDLERS *********************/
        private var __pIndex:int;
        private function playFinished(event:PlayStateEvent):void {
            EventUtil.fireMediaStateChanged(9, __pIndex);
            if(!playNextLoop()) {
                Log.info("Media playback finished");
                state = FINISHED;
            }
        }
 
        private function playStopped(event:PlayStateEvent):void {
            state = STOPPED;
            Log.info("Playback stopped");
            EventUtil.fireMediaStateChanged(3, __pIndex);
        }

        private function playPaused(event:PlayStateEvent):void {
            state = PAUSED;
            Log.info("Playback paused");
            EventUtil.fireMediaStateChanged(4, __pIndex);
        }

        private function playStarted(event:PlayStateEvent):void {
            switch(state) {
                case PAUSED:
                    Log.info("Playback resumed");
                    break;
                default:
                    __pIndex = playlist.getIndex();
                    Log.info("Playlist #" + __pIndex + " playback started");
            }
            state = PLAYING;
            EventUtil.fireMediaStateChanged(2, __pIndex);
        }

        /************************** HELPER FUNCTIONS *****************************/
        private function _load(mediaURL:String):void {
            if(player != null) {
                player._stop(false); // stop playback if any ...
                removeChildAt(0);
            }
            state = STOPPED;

            if(mediaURL.search(".mp3") >= 0) {
                player = mp3
                addChild(mp3);
                Log.info("MP3Engine: Loading media at '" + mediaURL + "'");
            } else {
                player = vdu;
                addChild(vdu);
                Log.info("VideoEngine: Loading media at '" + mediaURL + "'");
            }
            
            try {
                player._load(mediaURL);
                state = LOADED;
            } catch(err:SecurityError) {
                Log.error("SEVERE: " + err.message);
            } catch(err:Error) {
                Log.error("SEVERE: " + err.message);
            }
        }

        public function onUpdateVolumeEvent(event:SettingChangedEvent):void {
            updateVolume(Number(event.newValue));
        }

        public function updateVolume(vol:Number):void {
            mp3.setVolume(vol);
            vdu.setVolume(vol);
        }

        /******* fix streaching/cropping issue, let app auto-adjust video size *******/
        private function metaDoVDUSize(event:MetadataEvent):void {
             Log.debug("update from metadata event ... ");
           updateVDUSize();
        }

        public function resizeDoVDUSize(event:ResizeEvent):void {
            if(player && (player is VideoEngine)) {
            Log.debug("update from resize event ... ");
                updateVDUSize();
            }
        }

        public function updateVDUSize():void {
            var _vduHeight:int = vdu.metadata ? vdu.metadata.height : vdu.height;
            var _vduWidth:int = vdu.metadata ? vdu.metadata.width : vdu.width;
            var _aHeight:int = FlexGlobals.topLevelApplication.height;
            var _aWidth:int = FlexGlobals.topLevelApplication.width;

            if((_aHeight < _vduHeight) || (_aWidth < _vduWidth)) {  // avoid cropping
                vdu.percentHeight = 100;
                vdu.percentWidth = 100;
            Log.debug("avoid cropping ... ");
            } else if((_aHeight > _vduHeight) || (_aWidth > _vduWidth)) {  // avoid steching
                    vdu.height = _vduHeight;
                    vdu.width = _vduWidth;
            Log.debug("avoid streching ... ");
            } else if((_aHeight > _vduHeight) && (_aWidth > _vduWidth)) {  // fit to window
                vdu.percentHeight = 100;
                vdu.percentWidth = 100;
            Log.debug("fit to window ... ");
            }
            Log.debug("dim vdu : " + vdu.height + ", " + vdu.width);
            Log.debug("dim vd% : " + vdu.percentHeight + ", " + vdu.percentWidth);
            Log.debug("dim app : " + _aHeight + ", " + _aWidth);
            Log.debug("dim _vd : " + _vduHeight + ", " + _vduWidth);
            if(vdu.metadata)
            Log.debug("dim mtd : " + vdu.metadata.height + ", " + vdu.metadata.width);
        }

        /************************** FS effects **********************/
        private var _visible:Boolean = true;
        private function onFullScreen(event:FullScreenEvent):void {
            if(event.fullScreen) {
                _visible = visible;
                visible = true;
            } else {
                visible = _visible;
            }
            EventUtil.fireFullScreenChanged(event.fullScreen);
       }
    }
}
