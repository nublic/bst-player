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

package com.bramosystems.oss.player.core.event.client;

import com.bramosystems.oss.player.core.client.skin.MediaSeekBar;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.GwtEvent.Type;

/**
 * The event fired by {@link MediaSeekBar} widgets when the position of the
 * seek bar has changed.
 * 
 * @author Sikirulai Braheem
 */
public class SeekChangeEvent extends GwtEvent<SeekChangeHandler> {
    private double seekPosition;
    public static Type<SeekChangeHandler> TYPE = new Type<SeekChangeHandler>();

    /**
     * Fires seek change event on all registered handlers
     *
     * @param handler the handler of the event
     * @param seekPosition the new position of the seek bar
     */
    public static void fire(HasSeekChangeHandlers handler, double seekPosition) {
        handler.fireEvent(new SeekChangeEvent(seekPosition));
    }

    /**
     * Creates a new seek change event
     *
     * @param seekPosition the new position of the seek bar
     */
    protected SeekChangeEvent(double seekPosition) {
        this.seekPosition = seekPosition;
    }
    
    @Override
    public Type<SeekChangeHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(SeekChangeHandler handler) {
        handler.onSeekChanged(this);
    }

    /**
     * Retrieves the new position of the seek bar
     *
     * @return the new position of the seek bar
     */
    public double getSeekPosition() {
        return seekPosition;
    }
}
