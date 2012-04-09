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

package com.bramosystems.oss.player {
    import com.bramosystems.oss.player.playlist.*;
    import com.bramosystems.oss.player.external.*;
    import com.bramosystems.oss.player.events.*;

    import flash.display.Graphics;
    import flash.errors.*;
    import flash.events.*;
    import flash.media.*;
    import flash.net.*;
    import flash.utils.*;

    import mx.core.UIComponent;

    public class MP3Engine extends UIComponent implements Engine {
            private var sound:Sound;
            private var channel:SoundChannel;
            private var sndTransform:SoundTransform;
            private var mediaUrl:String = "";
            private var soundDuration:Number = 0;
            private var position:Number = 0;
            private var isPlaying:Boolean = false, isPaused:Boolean = false;
            private var propagateID3:Boolean = false;
            private var playTimer:Timer;

            public function MP3Engine() {
                // add position timer ...
                playTimer = new Timer(500);
                playTimer.addEventListener(TimerEvent.TIMER, function(event:TimerEvent):void {
                    EventUtil.firePlayingProgress(getPlayPosition());
                });
            }

            public function _load(url:String):void {
                if((url != null) && (url != mediaUrl)) {
                    try {
                        this.mediaUrl = url;
                        position = 0;
                        propagateID3 = true;
                        sound = new Sound();
                        sound.addEventListener(Event.COMPLETE, loadingCompleteHandler);
                        sound.addEventListener(Event.OPEN, loadingStartedHandler);
                        sound.addEventListener(IOErrorEvent.IO_ERROR, loadingErrorHandler);
                        sound.addEventListener(ProgressEvent.PROGRESS, loadingProgressHandler);
                        sound.addEventListener(Event.ID3, id3Handler);
                        sound.load(new URLRequest(mediaUrl), new SoundLoaderContext(1000, true));

//                        addEventListener(Event.ENTER_FRAME, onEnterFrame);
                    } catch(err:Error){
                        Log.error(err.message);
                    }
                }
            }

            public function play():void {
                if(sound == null) {
                    Log.info("Player not loaded, load player first");
                    throw new Error("Player not loaded, load player first");
                }

                if(channel != null) {
                    channel.stop();
                }
                channel = sound.play(position, 0, sndTransform);
                channel.addEventListener(Event.SOUND_COMPLETE, _playFinishedHandler);
                _playStartedHandler();
           }

            public function pause():void {
                if(channel == null) {
                    Log.info("Pause playback call ignored, sound not played yet!");
                    return;
                }

                position = channel.position;
                isPaused = true;

                channel.stop();
                isPlaying = false;
                playTimer.stop();
                dispatchEvent(new PlayStateEvent(PlayStateEvent.PLAY_PAUSED));
            }

            public function _stop(fireEvent:Boolean):void {
                if(channel == null) {
                    Log.info("Stop playback call ignored, sound not played yet!");
                    return;
                }

                position = 0;
                isPaused = false;

                channel.stop();
                isPlaying = false;
                playTimer.stop();
                if(fireEvent) {
                    dispatchEvent(new PlayStateEvent(PlayStateEvent.PLAY_STOPPED));
                }
            }

            public function setPlayPosition(channelPosition:Number):void {
                if(isPaused || isPlaying) {
                    position = channelPosition;
                    if(isPlaying) {
                        play();
                    }
                }
            }

            public function getPlayPosition():Number {
                if(channel == null) {
                    return 0;
                }

                if(isPlaying) {
                    position = channel.position;
                }

                return position;
            }

            public function getDuration():Number {
                return soundDuration;
            }

            public function setVolume(volume:Number):void {
                this.sndTransform = new SoundTransform(volume);
                if(channel == null)
                    return;

                channel.soundTransform = sndTransform;
            }

            public function close():void {
                try {
                    sound.close();
                    Log.info("Player closed");
                } catch(err:IOError) {
                }
            }

            /********************* Javascript call impls. *************************/
            private function loadingStartedHandler(event:Event):void {
                EventUtil.fireMediaStateChanged(1);
            }

            private function _playStartedHandler():void {
                isPlaying = true;
                isPaused = false;
                dispatchEvent(new PlayStateEvent(PlayStateEvent.PLAY_STARTED));
                playTimer.start();
            }

            private function loadingCompleteHandler(event:Event):void {
               if(sound != null)
                    soundDuration = sound.length;

               EventUtil.fireMediaStateChanged(10);
               Log.info("Loading complete");
            }

            private function loadingProgressHandler(event:ProgressEvent):void {
               if(sound != null)
                    soundDuration = sound.length;

               EventUtil.fireLoadingProgress(event.bytesLoaded / event.bytesTotal);
            }

            private function loadingErrorHandler(event:IOErrorEvent):void {
                var txt:String = event.text.toLowerCase();
                if(txt.search("2032") >= 0) {
                    Log.error("Error loading media at " + mediaUrl +
                        ".  The stream could not be opened, please check your network connection.");
                    dispatchEvent(new PlayStateEvent(PlayStateEvent.PLAY_FINISHED));
                } else {
                    Log.error(event.text);
                }
            }

            private function id3Handler(event:Event):void {
                try {
                    if(propagateID3) {  // workarround for multiple firing ...
                        Log.info("ID3 data available");
                        EventUtil.fireID3Metadata(sound.id3);
                        propagateID3 = false;
                    }
                } catch(err:Error) {
                    Log.info(err.message);
                }
            }

            private function _playFinishedHandler(event:Event):void {
                position = 0;
                playTimer.stop();
//                removeEventListener(Event.ENTER_FRAME, onEnterFrame);
                dispatchEvent(new PlayStateEvent(PlayStateEvent.PLAY_FINISHED));
            }



        private function onEnterFrame(event:Event):void {
            var bytes:ByteArray = new ByteArray();
            const PLOT_HEIGHT:int = 180;
            const CHANNEL_LENGTH:int = 256;

            SoundMixer.computeSpectrum(bytes, false, 0);

            var g:Graphics = this.graphics;
            g.clear();

            g.lineStyle(0, 0x6600CC);
            g.beginFill(0x6600CC);
            g.moveTo(0, PLOT_HEIGHT);

            var n:Number = 0;

            for (var i:int = 0; i < CHANNEL_LENGTH; i++) {
                n = (bytes.readFloat() * PLOT_HEIGHT);
                g.lineTo(i * 2, PLOT_HEIGHT - n);
            }

            g.lineTo(CHANNEL_LENGTH * 2, PLOT_HEIGHT);
            g.endFill();

            g.lineStyle(0, 0xCC0066);
            g.beginFill(0xCC0066, 0.5);
            g.moveTo(CHANNEL_LENGTH * 2, PLOT_HEIGHT);

            for (i = CHANNEL_LENGTH; i > 0; i--) {
                n = (bytes.readFloat() * PLOT_HEIGHT);
                g.lineTo(i * 2, PLOT_HEIGHT - n);
            }

            g.lineTo(0, PLOT_HEIGHT);
            g.endFill();
        }
    }
}