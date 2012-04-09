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

import com.bramosystems.oss.player.core.client.skin.VolumeControl;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.GwtEvent.Type;

/**
 * The volume change event fired by {@link VolumeControl} widgets when the
 * volumes' slider has changed.
 *
 * @author Sikirulai Braheem
 */
public class VolumeChangeEvent extends GwtEvent<VolumeChangeHandler> {
    private double volume;
    public static Type<VolumeChangeHandler> TYPE = new Type<VolumeChangeHandler>();

    /**
     * Fires volume change event on all registered handlers
     *
     * @param source the source of the handlers
     * @param volume the new volume
     */
    public static void fire(HasVolumeChangeHandlers source, double volume) {
        source.fireEvent(new VolumeChangeEvent(volume));
    }

    /**
     * Creates a new volume change event
     *
     * @param volume the new volume in the range {@code 0} silence to {@code 1} maximum volume
     */
    protected VolumeChangeEvent(double volume) {
        this.volume = volume;
    }

    /**
     * Retrieves the new volume.  The volume ranges between {@code 0} silence and
     * {@code 1} maximum volume
     *
     * @return the volume
     */
    public double getNewVolume() {
        return volume;
    }

    @Override
    public Type<VolumeChangeHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(VolumeChangeHandler handler) {
        handler.onVolumeChanged(this);
    }
}
