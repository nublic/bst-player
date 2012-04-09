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

import com.bramosystems.oss.player.core.client.ui.QuickTimePlayer.Scale;
import com.bramosystems.oss.player.core.client.ui.WinMediaPlayer.UIMode;

/**
 * An enum of player configuration parameters.
 *
 * <p>The parameters are applied as HTML param tags on the underlying player
 * plugin.</p>
 *
 * @author Sikiru Braheem <sbraheem at bramosystems . com>
 * @since 1.1
 * @see AbstractMediaPlayer#setConfigParameter(ConfigParameter, ConfigValue)
 */
public enum ConfigParameter {

    /**
     * Parameter for the transparency mode for a player (if available).
     *
     * <p>This parameter requires a {@linkplain TransparencyMode} value type
     */
    TransparencyMode(TransparencyMode.class),
    
    /**
     * Parameter for WinMediaPlayers' UI Mode property.
     *
     * <p>The mode indicates which controls are shown on the user interface.</p>
     * <p>This parameter requires a {@linkplain UIMode} value type
     */
    WMPUIMode(UIMode.class),
    
    /**
     * Parameter for QuickTimePlayers' Scale property.
     *
     * <p>This parameter is used to scale the dimensions of a QuickTime movie. It requires either a
     * {@linkplain Scale} value type or a double value</p>
     *
     * <p>A double value scales the movie by a factor of the value. For example, to play a movie at 
     * half its normal size, use QTScale with a value of 0.5</p>
     *
     * @see Scale
     * @since 1.2
     */
    QTScale(Scale.class, Double.class),
    
    /**
     * Parameter for the background color property or a player.
     *
     * <p>This parameter is used to specify the background color for the exposed part of a players' alloted space
     * The value should be specified as a CSS color value (i.e. hexadecimal RGB values)</p>
     *
     * @since 1.2.1
     */
    BackgroundColor(String.class);
    
    private Class[] valueType;

    private ConfigParameter(Class... valueType) {
        this.valueType = valueType;
    }

    /**
     * Returns the required value type for this parameter.
     *
     * @return the required type of value
     */
    public Class[] getValueType() {
        return valueType;
    }
}
