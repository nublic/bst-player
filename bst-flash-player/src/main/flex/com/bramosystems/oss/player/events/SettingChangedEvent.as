package com.bramosystems.oss.player.events {

    import flash.events.*;

    public class SettingChangedEvent extends Event {

        public static const VOLUME_CHANGED:String = "volume";
        public static const SHUFFLE_CHANGED:String = "shuffle";
        public static const LOOP_COUNT_CHANGED:String = "loopCount";
        public static const REPEAT_MODE_CHANGED:String = "repeatMode";

        private var evtType:String;
        public var newValue:String;

        public function SettingChangedEvent(evtType:String, newValue:String) {
            super(evtType);
            this.evtType = evtType;
            this.newValue = newValue;
        }

        override public function clone():Event {
            return new SettingChangedEvent(evtType, newValue);
        }
    }
}