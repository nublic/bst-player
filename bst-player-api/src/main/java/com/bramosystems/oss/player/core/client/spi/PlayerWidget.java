/*
 *  Copyright 2010 Sikiru Braheem <sbraheem at bramosystems . com>.
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
package com.bramosystems.oss.player.core.client.spi;

import com.bramosystems.oss.player.core.client.impl.plugin.PlayerManager;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Widget;
import java.util.HashMap;

/**
 * High level helper class to create widget for players.
 * 
 * <p>The widget manages the DOM object of players as created by the {@link PlayerElement} helper
 * class.
 *
 * @author Sikiru Braheem <sbraheem at bramosystems . com>
 * @since 1.3
 * @see PlayerElement
 */
public class PlayerWidget extends Widget {
    
    private HashMap<String, String> params;
    private String playerName, playerProvider, playerId, mediaURL, _height, _width;
    private boolean autoplay;
    
    private PlayerWidget() {
        setElement(DOM.createDiv());
        params = new HashMap<String, String>();
        _width = "100%";
        _height = "10px";
    }

    /**
     * Creates a PlayerWidget
     * 
     * @param playerProvider the player provider to consult for browser-specific implementations
     * @param playerName the name of the player to be created
     * @param playerId the DOM ID of the player
     * @param mediaURL the URL of the media to be embedded
     * @param autoplay {@code true} to autoplay the media, {@code false} otherwise.
     */
    public PlayerWidget(String playerProvider, String playerName, String playerId, String mediaURL, boolean autoplay) {
        this();
        this.playerId = playerId;
        this.autoplay = autoplay;
        this.mediaURL = mediaURL;
        this.playerName = playerName;
        this.playerProvider = playerProvider;
    }

    /**
     * Adds the specified parameters to the element.  The parameters are used as HTML params tags in the
     * eventual HTML DOM object
     * 
     * @param name the name of the parameter
     * @param value the value of the parameter
     */
    public void addParam(String name, String value) {
        params.put(name, value);
    }

    /**
     * Removes the specified parameter from the parameter map.
     * 
     * @param name the parameter to be removed from the parameter map
     */
    public void removeParam(String name) {
        params.remove(name);
    }

    /**
     * Returns the value of the specified parameter
     * 
     * @param name the parameter
     * @return the value of the parameter
     */
    public String getParam(String name) {
        return params.get(name);
    }
    
    @Override
    protected void onLoad() {
        injectWidget(false);
    }
    
    @Override
    public void setHeight(String height) {
        super.setHeight(height);
        if (getElement().hasChildNodes()) {
            getElement().getFirstChildElement().setAttribute("height", height);
            getElement().getFirstChildElement().getStyle().setProperty("height", height);
        } else {
            _height = height;
        }
    }
    
    @Override
    public void setWidth(String width) {
        super.setWidth(width);
        if (getElement().hasChildNodes()) {
            getElement().getFirstChildElement().setAttribute("width", width);
            getElement().getFirstChildElement().getStyle().setProperty("width", width);
        } else {
            _width = width;
        }
    }

    /**
     * Replaces the DOM object of this widget with another created with the specified information
     * 
     * @param playerProvider the player provider to consult for browser-specific implementations
     * @param playerName the name of the player to be created
     * @param playerId the DOM ID of the player
     * @param mediaURL the URL of the media to be embedded
     * @param autoplay {@code true} to autoplay the media, {@code false} otherwise.
     */
    public void replace(String playerProvider, String playerName, String playerId, String mediaURL, boolean autoplay) {
        this.playerId = playerId;
        this.autoplay = autoplay;
        this.mediaURL = mediaURL;
        this.playerProvider = playerProvider;
        this.playerName = playerName;
        injectWidget(true);
    }
    
    private void injectWidget(boolean updateDimension) {
        Element e = PlayerManager.getInstance().getProviderFactory(playerProvider).getPlayerElement(
                playerName, playerId, mediaURL, autoplay, params).getElement();
        if (updateDimension) {
            String curHeight = getElement().getFirstChildElement().getAttribute("height");
            String curWidth = getElement().getFirstChildElement().getAttribute("width");
            setElementSize(e, curWidth, curHeight);
        } else {
            setElementSize(e, _width, _height);
        }
        getElement().setInnerHTML(e.getString());
    }
    
    private void setElementSize(Element e, String width, String height) {
        e.setAttribute("height", height);
        e.getStyle().setProperty("height", height);
        e.setAttribute("width", width);
        e.getStyle().setProperty("width", width);
    }
}
