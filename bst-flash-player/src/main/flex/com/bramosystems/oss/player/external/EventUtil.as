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

package com.bramosystems.oss.player.external {
    import flash.external.*;
    import flash.events.*;
    import flash.geom.Rectangle;
    import flash.media.ID3Info;
    import mx.core.Application;

    import com.bramosystems.oss.player.control.Controller;
    import com.bramosystems.oss.player.PlayerOptions;

    public class EventUtil {
        private static var playerId:String = PlayerOptions.playerId;
        public static var controller:Controller;

        public static function fireApplicationInitialized():void {
            try {
                ExternalInterface.call("bstplayer.handlers.swf." + playerId + ".onInit");
            } catch(err:SecurityError) {
            } catch(err:Error) {
            }            
        }

        /**
         * state IDs:
         *  1: loading started...
         *  2: play started...
         *  3: play stopped...
         *  4: play paused...
         *  9: play finished...
         * 10: loading complete ...
         */
        public static function fireMediaStateChanged(state:int, playlistIndex:int = 0):void {
            controller.onMediaStateChanged(state, playlistIndex);
            try {
                ExternalInterface.call("bstplayer.handlers.swf." + playerId + ".onStateChanged", state, playlistIndex);
            } catch(err:SecurityError) {
            } catch(err:Error) {
            }            
        }

        public static function fireLoadingProgress(progress:Number):void {
            controller.onLoadingProgress(progress);
            try {
                ExternalInterface.call("bstplayer.handlers.swf." + playerId + ".onLoadingProgress", progress);
            } catch(err:SecurityError) {
            } catch(err:Error) {
            }            
        }

        public static function fireFullScreenChanged(fullscreen:Boolean):void {
            try {
                ExternalInterface.call("bstplayer.handlers.swf." + playerId + ".onFullscreen", fullscreen);
            } catch(err:SecurityError) {
            } catch(err:Error) {
            }            
        }

        public static function firePlayingProgress(progress:Number):void {
            controller.onPlayingProgress(progress);
            try {
                ExternalInterface.call("bstplayer.handlers.swf." + playerId + ".onPlayingProgress", progress);
            } catch(err:SecurityError) {
            } catch(err:Error) {
            }            
        }

        public static function fireID3Metadata(info:ID3Info):void {
            controller.onMetadata();

            // parse into CSV like values ...
            // year[$]albumTitle[$]artists[$]comment[$]genre[$]title[$]
            // contentProviders[$]copyright[$]duration[$]hardwareSoftwareRequirements[$]
            // publisher[$]internetStationOwner[$]internetStationName[$]videoWidth[$]videoHeight

            var id3:String = info.year + "[$]" + info.album + "[$]" + info.artist  + "[$]" +
                                info.comment + "[$]" + info.genre + "[$]" + info.songName + "[$]" +
                                info.TOLY + "[$]" + info.TOWN + "[$]" + info.TLEN + "[$]" +
                                info.TSSE + "[$]" + info.TPUB + "[$]" + info.TRSO + "[$]" +
                                info.TRSN + "[$]0[$]0";
            try {
                ExternalInterface.call("bstplayer.handlers.swf." + playerId + ".onMetadata", id3);
            } catch(err:SecurityError) {
            } catch(err:Error) {
            }            
        }

        public static function fireVideoMetadata(duration:Number, info:String, width:Number, height:Number):void {
            controller.onMetadata();

            // parse into CSV like values ...
            // year[$]albumTitle[$]artists[$]comment[$]genre[$]title[$]
            // contentProviders[$]copyright[$]duration[$]hardwareSoftwareRequirements[$]
            // publisher[$]internetStationOwner[$]internetStationName[$]videoWidth[$]videoHeight

            var id3:String = "0[$] [$] [$] [$] [$] [$] [$] [$]" + (duration * 1000) +
                             "[$]" + info + "[$] [$] [$] [$]" + (isNaN(width) ? 0 : width) + "[$]" +
                            (isNaN(height) ? 0 : height);
            try {
                ExternalInterface.call("bstplayer.handlers.swf." + playerId + ".onMetadata", id3);
            } catch(err:SecurityError) {
            } catch(err:Error) {
            }            
        }

        public static function fireMouseDownEvent(event:MouseEvent):void {
            try {
                if(PlayerOptions.isMouseEventsEnabled)
                    ExternalInterface.call("bstplayer.handlers.swf." + playerId + ".onEvent", 1, event.buttonDown,
                        event.altKey, event.ctrlKey, event.shiftKey, false, //event.commandKey,
                        int(event.stageX), int(event.stageY));
            } catch(err:SecurityError) {
            } catch(err:Error) {
            }            
        }
        public static function fireMouseUpEvent(event:MouseEvent):void {
            try {
                if(PlayerOptions.isMouseEventsEnabled)
                    ExternalInterface.call("bstplayer.handlers.swf." + playerId + ".onEvent", 2, event.buttonDown,
                        event.altKey, event.ctrlKey, event.shiftKey, false, //event.commandKey,
                        int(event.stageX), int(event.stageY));
            } catch(err:SecurityError) {
            } catch(err:Error) {
            }            
        }
        public static function fireMouseMoveEvent(event:MouseEvent):void {
            try {
                if(PlayerOptions.isMouseEventsEnabled)
                    ExternalInterface.call("bstplayer.handlers.swf." + playerId + ".onEvent", 3, event.buttonDown,
                        event.altKey, event.ctrlKey, event.shiftKey, false, //event.commandKey,
                        int(event.stageX), int(event.stageY));
            } catch(err:SecurityError) {
            } catch(err:Error) {
            }
        }
        public static function fireClickEvent(event:MouseEvent):void {
            try {
                if(PlayerOptions.isMouseEventsEnabled)
                    ExternalInterface.call("bstplayer.handlers.swf." + playerId + ".onEvent", 10, event.buttonDown,
                        event.altKey, event.ctrlKey, event.shiftKey, false, //event.commandKey,
                        int(event.stageX), int(event.stageY));
            } catch(err:SecurityError) {
            } catch(err:Error) {
            }            
        }
        public static function fireDoubleClickEvent(event:MouseEvent):void {
            try {
                if(PlayerOptions.isMouseEventsEnabled)
                    ExternalInterface.call("bstplayer.handlers.swf." + playerId + ".onEvent", 11, event.buttonDown,
                        event.altKey, event.ctrlKey, event.shiftKey, false, //event.commandKey,
                        int(event.stageX), int(event.stageY));
            } catch(err:SecurityError) {
            } catch(err:Error) {
            }            
        }
        public static function fireKeyDownEvent(event:KeyboardEvent):void {
            Log.debug("Firing KeyDown Event : " + event.charCode);
            try {
                ExternalInterface.call("bstplayer.handlers.swf." + playerId + ".onEvent", 20, false, //event.buttonDown,
                    event.altKey, event.ctrlKey, event.shiftKey, false, //event.commandKey,
                    event.keyCode, event.charCode);
                ExternalInterface.call("bstplayer.handlers.swf." + playerId + ".onEvent", 21, false, //event.buttonDown,
                    event.altKey, event.ctrlKey, event.shiftKey, false, //event.commandKey,
                    event.keyCode, event.charCode);
            } catch(err:SecurityError) {
            } catch(err:Error) {
            }            
        }
        public static function fireKeyUpEvent(event:KeyboardEvent):void {
            try {
                ExternalInterface.call("bstplayer.handlers.swf." + playerId + ".onEvent", 22, false, //event.buttonDown,
                    event.altKey, event.ctrlKey, event.shiftKey, false, //event.commandKey,
                    event.keyCode, event.charCode);
            } catch(err:SecurityError) {
            } catch(err:Error) {
            }            
        }
    }
}