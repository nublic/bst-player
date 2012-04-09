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
 * The event fired when the state of the player changes
 *
 * @author Sikirulai Braheem <sbraheem at bramosystems.com>
 */
public class PlayerStateEvent extends GwtEvent<PlayerStateHandler> {
    public static final Type<PlayerStateHandler> TYPE = new Type<PlayerStateHandler>();
    private State state;

    /**
     * Creates a new player state event
     *
     * @param state the new state
     */
    protected PlayerStateEvent(State state) {
        this.state = state;
    }

    /**
     * Fires player state event on all registered handlers
     *
     * @param source the source the event
     * @param state the new player state
     */
    public static void fire(HasPlayStateHandlers source, State state) {
        source.fireEvent(new PlayerStateEvent(state));
    }

    @Override
    public Type<PlayerStateHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(PlayerStateHandler handler) {
        handler.onPlayerStateChanged(this);
    }

    /**
     * Retrieves the new player state
     *
     * @return the new player state
     */
    public State getPlayerState() {
        return state;
    }

    /**
     * An enum of media player states
     */
    public enum State {
        /**
         * The player is initialized and ready
         */
        Ready, 
        
        /**
         * The player has started buffering
         */
        BufferingStarted,
        /**
         * The player has stopped buffering
         */
        BufferingFinished,

        /**
         * The dimension of the player has changed.
         *
         * <p>This state is raised when the dimension of the player changes to match the
         * size of the current media (especially video).
         */
        DimensionChangedOnVideo,

        /**
         * The player has entered fullscreen display mode
         *
         * @since 1.2
         */
        FullScreenStarted,

        /**
         * The player has exited fullscreen display mode
         *
         * @since 1.2
         */
        FullScreenFinished
    }
}
