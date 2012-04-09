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

import com.bramosystems.oss.player.core.event.client.HasVolumeChangeHandlers;
import com.bramosystems.oss.player.core.event.client.VolumeChangeEvent;
import com.bramosystems.oss.player.core.event.client.VolumeChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseUpEvent;
import com.google.gwt.event.dom.client.MouseUpHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Widget to control the volume of a player.  The control is placed on a player
 * as an icon which when clicked upon, opens a popup panel containing the
 * slider widget for the volume.
 *
 * <p>{@code VolumeChangeHandler}s are notified whenever the slider is adjusted.
 *
 * <h4>CSS Styles</h4>
 * <code><pre>
 * .player-VolumeControl { the slider widget }
 * .player-VolumeControl .volume { the volume level indicator }
 * .player-VolumeControl .track  { the sliders' track indicator }
 * </pre></code>
 *
 * @see VolumeChangeHandler
 * @author Sikirulai Braheem
 */
public class VolumeControl extends Composite implements MouseUpHandler, HasVolumeChangeHandlers {

    private Label volume,  track;
    private PopupPanel volumePanel;
    private AbsolutePanel seekTrack;

    private void initVolumeControl(int sliderHeight) {
        addDomHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                showVolumeSlider();
            }
        }, ClickEvent.getType());

        volume = new Label();
        volume.addMouseUpHandler(this);
        volume.setStyleName("volume");

        track = new Label();
        track.addMouseUpHandler(this);
        track.setStyleName("track");

        seekTrack = new AbsolutePanel();

        volumePanel = new PopupPanel(true);
        volumePanel.setStyleName("");
        volumePanel.setWidget(seekTrack);
        volumePanel.setStylePrimaryName("player-VolumeControl");

        String sizze = String.valueOf(sliderHeight) + "px";
        seekTrack.setSize("50px", sizze);
        track.setSize("50px", sizze);
        volume.setHeight(sizze);
        volumePanel.setWidth("50px");

        seekTrack.add(track, 0, 0);
        seekTrack.add(volume, 0, 0);

        setVolume(0);
    }

    /**
     * Constructs <code>VolumeControl</code>.  The control is displayed as the
     * specified {@code icon}.  An horizontal slider of height {@code sliderHeight} is
     * displayed when the {@code icon} is clicked on.
     *
     * <p>The slider popup panel has a fixed width of 50px.
     *
     * @param icon represents the volume control object.
     * @param sliderHeight the height of the volume slider control.
     */
    public VolumeControl(Image icon, int sliderHeight) {
        super.initWidget(icon);
        initVolumeControl(sliderHeight);
    }

    /**
     * Constructs <code>VolumeControl</code>.  This constructor is provided for complete CSS styling
     * support.  An horizontal slider of height {@code sliderHeight} is
     * displayed when this control is clicked on.
     *
     * <p>The slider popup panel has a fixed width of 50px.
     *
     * @param sliderHeight the height of the volume slider control.
     * @since 1.2
     */
    public VolumeControl(int sliderHeight) {
        super.initWidget(new Label());
        initVolumeControl(sliderHeight);
    }

    /**
     * Sets the level of the volume slider control.
     *
     * <p><b>Note:</b> {@code VolumeChangeListener}s are not notified by this
     * method.
     *
     * @param volume value between {@code 0} (silent) and {@code 1} (the maximum).
     * Any value outside the range will be ignored.
     */
    public final void setVolume(double volume) {
        if ((volume >= 0) && (volume <= 1.0)) {
            volume *= 100;
            this.volume.setWidth(volume + "%");
        }
    }

    @Override
    public void onMouseUp(MouseUpEvent event) {
        double vol = 0;
        vol = event.getX() / (double) seekTrack.getOffsetWidth();
        setVolume(vol);
        VolumeChangeEvent.fire(this, vol);
    }

    /**
     * Overridden to prevent subclasses from changing the wrapped widget.
     *
     */
    @Override
    protected void initWidget(Widget widget) {
    }

    /**
     * Adds the specified handler to the player.  The handler is called whenever the state
     * of the volume slider changes.
     *
     * @param handler the handler
     * @return the HandlerRegistration used to remove the handler
     * @see VolumeChangeHandler
     */
    @Override
    public HandlerRegistration addVolumeChangeHandler(VolumeChangeHandler handler) {
        return addHandler(handler, VolumeChangeEvent.TYPE);
    }

    /**
     * Assigns a CSS style class name to the volume slider popup panel.
     *
     * @param styleName CSS style class name
     */
    public void setPopupStyleName(String styleName) {
        volumePanel.setStylePrimaryName(styleName);
    }

    private void showVolumeSlider() {
                volumePanel.setPopupPositionAndShow(new PopupPanel.PositionCallback() {

                    @Override
                    public void setPosition(int offsetWidth, int offsetHeight) {
                        volumePanel.setPopupPosition(getAbsoluteLeft(), getAbsoluteTop() + getOffsetHeight());
                    }
                });
    }
}
