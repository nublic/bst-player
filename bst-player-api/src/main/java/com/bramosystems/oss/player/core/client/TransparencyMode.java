/*
 *  Copyright 2009 Sikiru Braheem <sbraheem at bramosystems . com>.
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
 * An enum of transparency types for a player. The transparency mode is used to
 * allow images, text, or popup windows to overlay on top of the player.
 *
 * @author Scott Selikoff
 * @since 1.1
 */
public enum TransparencyMode implements ConfigValue {

    /**
     * The background of the page shows through all transparent
     * portions of the player.
     */
    TRANSPARENT,
    /**
     * The video is placed in its own rectangular window on a page.
     */
    WINDOW,
    /**
     * Hides everything on the page behind the player.
     */
    OPAQUE
}
