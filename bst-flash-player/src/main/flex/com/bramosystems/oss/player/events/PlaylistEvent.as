package com.bramosystems.oss.player.events {

    import flash.events.*;

    public class PlaylistEvent extends Event {

        public static const CLEARED:String = "listCleared";
        public static const ADDED:String = "listAdded";
        public static const REMOVED:String = "listRemoved";

        private var evtType:String;

        public function PlaylistEvent(evtType:String) {
            super(evtType);
            this.evtType = evtType;
        }

        override public function clone():Event {
            return new PlaylistEvent(evtType);
        }
    }
}