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

import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.GwtEvent.Type;

/**
 * The event fired when the state of media playback changes
 *
 * @author Sikirulai Braheem
 */
public class PlayStateEvent extends GwtEvent<PlayStateHandler> {

    public static final Type<PlayStateHandler> TYPE = new Type<PlayStateHandler>();
    private State state;
    private int itemIndex;

    /**
     * Creates a new play state event
     *
     * @param state the new state
     * @param itemIndex the playlist index of the media
     */
    protected PlayStateEvent(State state, int itemIndex) {
        this.state = state;
        this.itemIndex = itemIndex;
    }

    /**
     * Fires play state events on all registered handlers
     *
     * @param source the source of the event
     * @param state the new state of the media
     * @param itemIndex the playlist index of the media
     */
    public static void fire(HasPlayStateHandlers source, State state, int itemIndex) {
        source.fireEvent(new PlayStateEvent(state, itemIndex));
    }

    @Override
    public Type<PlayStateHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(PlayStateHandler handler) {
        handler.onPlayStateChanged(this);
    }

    /**
     * Retrieves the new state of media playback
     *
     * @return the state of the playback
     */
    public State getPlayState() {
        return state;
    }

    /**
     * Retrieves the index of the media item in the playlist
     *
     * @return the index of the media item in the playlist
     */
    public int getItemIndex() {
        return itemIndex;
    }

    /**
     * An enum of playback states
     */
    public enum State {

        /**
         * Media playback has started
         */
        Started,
        /**
         * Media playback has finished
         */
        Finished,
        /**
         * Media playback is currently paused
         */
        Paused,
        /**
         * Media playback is currently stopped
         */
        Stopped
    }
}
