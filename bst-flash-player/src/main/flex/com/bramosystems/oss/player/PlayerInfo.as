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
    import flash.ui.*;

    import mx.core.*;
    import mx.events.*;

    public class PlayerInfo {
        private var ctxMenu:ContextMenu;

//        public function PlayerMenu(setting:Setting, _playlist:Playlist) {
        public function PlayerInfo() {
            ctxMenu = new ContextMenu();
            ctxMenu.hideBuiltInItems();

            ctxMenu.customItems.push(new ContextMenuItem(getPlayerInfo()));
//            ctxMenu.customItems.push(new ContextMenuItem("Repeat"));
//            ctxMenu.customItems.push(new ContextMenuItem("Shuffle"));
        }

        public function getMenu():ContextMenu {
            return ctxMenu;
        }

        public static function getPlayerInfo():String {
            return "BST Player 1.3";
        }

        public static function getPlayerVersion():String {
            return "1.3";
        }
    }
}