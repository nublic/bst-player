package com.bramosystems.oss.player.events {

    import flash.events.*;

    public class PlayStateEvent extends Event {

        public static const PLAY_STARTED:String = "playstarted";
        public static const PLAY_PAUSED:String = "playpaused";
        public static const PLAY_STOPPED:String = "playstopped";
        public static const PLAY_FINISHED:String = "playfinished";

        private var evtType:String;

        public function PlayStateEvent(evtType:String) {
            super(evtType);
            this.evtType = evtType;
        }

        override public function clone():Event {
            return new PlayStateEvent(evtType);
        }
    }
}