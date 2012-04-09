/*
 *  Copyright 2010 Sikiru.
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

package com.bramosystems.oss.player.showcase.client.event;

import com.bramosystems.oss.player.showcase.client.MRL;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.GwtEvent.Type;

/**
 *
 * @author Sikiru
 */
public class PlaylistChangeEvent extends GwtEvent<PlaylistChangeHandler> {

    public static Type<PlaylistChangeHandler> TYPE = new Type<PlaylistChangeHandler>();
    private MRL playlistItem;
    private boolean added;
    private int index;

    public PlaylistChangeEvent(MRL playlistItem, int index, boolean added) {
        this.playlistItem = playlistItem;
        this.added = added;
        this.index = index;
    }

    public void setPlaylistItem(MRL playlistItem) {
        this.playlistItem = playlistItem;
    }

    public MRL getPlaylistItem() {
        return playlistItem;
    }

    public boolean isAdded() {
        return added;
    }

    public int getIndex() {
        return index;
    }

    @Override
    public Type<PlaylistChangeHandler> getAssociatedType() {
        return TYPE;
    }

    @Override
    protected void dispatch(PlaylistChangeHandler handler) {
        handler.onPlaylistChanged(this);
    }
}
