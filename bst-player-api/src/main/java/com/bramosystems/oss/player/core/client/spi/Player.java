/*
 * Copyright 2011 Sikirulai Braheem <sbraheem at bramosystems.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bramosystems.oss.player.core.client.spi;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation for player widgets.  It provides basic information about
 * the player widget
 *
 * @author Sikirulai Braheem <sbraheem at bramosystems.com>
 * @since 1.3
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Player {
    
    /**
     * The name of the player.
     * 
     * @return the name of the player
     */
    String name();
    
    /**
     * The PlayerProviderFactory implementation that handles instantiation and
     * media plugin detection for the player
     * 
     * @return PlayerProviderFactory implementation class
     * @see PlayerProviderFactory
     */
    Class providerFactory();
    
    /**
     * The minimum version of the required media plugin
     * 
     * @return the minimum plugin version in the format <code>&lt;major&gt;.&lt;minor&gt;.&lt;revision&gt;</code>
     */
    String minPluginVersion();
}
