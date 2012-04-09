/*
 *  Copyright 2010 Sikiru Braheem.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package com.bramosystems.oss.player.core.client;

/**
 * An enum of playback repeat modes
 *
 * @author Sikiru Braheem
 * @since 1.2
 */
public enum RepeatMode {

    /**
     * Stop playback when end of playlist is reached
     */
    REPEAT_OFF,

    /**
     * Repeats the current playlist item continously
     */
    REPEAT_ONE,

    /**
     * Repeats the entire playlist continously
     */
    REPEAT_ALL
}
