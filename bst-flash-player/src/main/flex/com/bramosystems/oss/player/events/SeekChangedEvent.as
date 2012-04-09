package com.bramosystems.oss.player.events {

    import flash.events.*;

    public class SeekChangedEvent extends Event {

        public static const SEEK_CHANGED:String = "seek_changed";

        public var newValue:Number;

        public function SeekChangedEvent(newValue:Number) {
            super(SEEK_CHANGED);
            this.newValue = newValue;
        }

        override public function clone():Event {
            return new SeekChangedEvent(newValue);
        }
    }
}