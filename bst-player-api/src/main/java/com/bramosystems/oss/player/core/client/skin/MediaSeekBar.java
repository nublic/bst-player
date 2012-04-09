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
package com.bramosystems.oss.player.core.client.skin;

import com.bramosystems.oss.player.core.event.client.HasSeekChangeHandlers;
import com.bramosystems.oss.player.core.event.client.SeekChangeEvent;
import com.bramosystems.oss.player.core.event.client.SeekChangeHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 * Abstract base class for seek bar implementations.
 *
 * <p>Provides means of controlling <b>loading</b> and <b>playing</b> progress
 * indicators during media loading and playback respectively.
 *
 * @author Sikirulai Braheem
 */
public abstract class MediaSeekBar extends Composite implements MouseUpHandler, HasSeekChangeHandlers {

    private Widget playing, loading;
    protected AbsolutePanel seekTrack;
    private double loadingProgress, playingProgress;

    /**
     * Constructs <code>MediaSeekBar</code> of the specified height.
     *
     * @param height the height of the seek bar in pixels
     */
    public MediaSeekBar(int height) {
        seekTrack = new AbsolutePanel();
        seekTrack.setSize("100%", height + "px");
        super.initWidget(seekTrack);
    }

    /**
     * Overridden to prevent subclasses from changing the wrapped widget.
     * Subclasses should call <code>initSeekBar</code> instead.
     *
     * @see #initSeekBar(com.google.gwt.user.client.ui.Widget, com.google.gwt.user.client.ui.Widget) 
     */
    @Override
    protected void initWidget(Widget widget) {
    }

    /**
     * Initialize the seek bar with the widgets that will be used to indicate
     * media loading and playback progress respectively.
     *
     * <p>Subclasses should call this method before calling any Widget methods
     * on this object.
     *
     * @param loadingWidget loading progress indicator widget
     * @param playingWidget playback progress indicator widget
     */
    protected final void initSeekBar(Widget loadingWidget, Widget playingWidget) {
        loading = loadingWidget;
        playing = playingWidget;

        seekTrack.add(this.loading, 0, 0);
        seekTrack.add(this.playing, 0, 0);

        setLoadingProgress(0);
        setPlayingProgress(0);
    }

    /**
     * Set the progress of the media loading operation.
     *
     * @param loadingProgress progress should be between {@code 0} (the minimum)
     * and {@code 1} (the maximum). Any value outside the range will be ignored.
     */
    public final void setLoadingProgress(double loadingProgress) {
        if ((loadingProgress >= 0) && (loadingProgress <= 1.0)) {
            this.loadingProgress = loadingProgress;
            loadingProgress *= 100;
            loading.setWidth(loadingProgress + "%");
        }
    }

    /**
     * Returns the progress of the media loading operation
     *
     * @return the progress of the loading operation between {@code 0} (the minimum)
     * and {@code 1} (the maximum)
     * @since 1.1
     */
    public final double getLoadingProgress() {
        return loadingProgress;
    }

    /**
     * Returns the progress of the media playback operation
     * 
     * @return the progress of the playback operation between {@code 0} (the minimum)
     * and {@code 1} (the maximum)
     * @since 1.1
     */
    public final double getPlayingProgress() {
        return playingProgress;
    }

    /**
     * Set the progress of the media playback operation.
     *
     * @param playingProgress progress should be between {@code 0} (the minimum)
     * and {@code 1} (the maximum). Any value outside the range will be ignored.
     */
    public final void setPlayingProgress(double playingProgress) {
        if ((playingProgress >= 0) && (playingProgress <= 1.0)) {
            this.playingProgress = playingProgress;
            playingProgress *= 100;
            playing.setWidth(playingProgress + "%");
        }
    }

    @Override
    public void onMouseUp(MouseUpEvent event) {
        double value = event.getX() / (double) getOffsetWidth();
        SeekChangeEvent.fire(this, value);
    }

    /**
     * Adds the specified handler to the player.  The handler is called
     * whenever the state of the seek bar changes.
     *
     * @param handler the handler
     * @return the HandlerRegistration used to remove the handler
     * @see SeekChangeHandler
     */
    @Override
    public final HandlerRegistration addSeekChangeHandler(SeekChangeHandler handler) {
        return addHandler(handler, SeekChangeEvent.TYPE);
    }
}
