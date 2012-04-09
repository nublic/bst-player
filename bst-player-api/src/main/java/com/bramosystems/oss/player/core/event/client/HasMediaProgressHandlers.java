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

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.event.shared.HasHandlers;

/**
 * Interface definition for widgets that provide registration of media progress handlers.
 *
 * @author Sikirulai Braheem
 * @since 1.2
 */
public interface HasMediaProgressHandlers extends HasHandlers {

    /**
     * Adds a {@link LoadingProgressHandler} handler
     * @param handler the  loading progress handler
     * @return {@link HandlerRegistration} used to remove the handler
     */
    public HandlerRegistration addLoadingProgressHandler(LoadingProgressHandler handler);
}
