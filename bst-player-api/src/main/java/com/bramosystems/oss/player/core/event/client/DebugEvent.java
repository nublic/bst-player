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
 * The event fired to convey different types of messages.
 *
 * @author Sikirulai Braheem
 */
public class DebugEvent extends GwtEvent<DebugHandler> {
    public static final Type<DebugHandler> TYPE = new Type<DebugHandler>();
    private MessageType type;
    private String message;

    /**
     * Contructs a new DebugEvent object
     *
     * @param type the type of message
     * @param message the message
     */
    protected DebugEvent(MessageType type, String message) {
        this.type = type;
        this.message = message;
    }

    /**
     * Fires DebugEvent on all registered handlers
     *
     * @param source the source of the event
     * @param type the type of message
     * @param message the message
     */
    public static void fire(HasMediaMessageHandlers source, MessageType type, String message) {
        source.fireEvent(new DebugEvent(type, message));
    }

    @Override
    public Type<DebugHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(DebugHandler handler) {
        handler.onDebug(this);
    }

    /**
     * Returns the type of message associated with the event
     *
     * @return the type of message
     */
    public MessageType getMessageType() {
        return type;
    }

    /**
     * Returns the message associated with the event
     *
     * @return the message
     */
    public String getMessage() {
        return message;
    }

    /**
     * An enum of message types associated with {@link DebugEvent}s
     */
    public enum MessageType {
        /**
         * Indicates an error message
         */
        Error,

        /**
         * Indicates an informational message
         */
        Info
    }

}
