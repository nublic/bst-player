/*
 * Copyright 2011 Sikirulai Braheem <sbraheem at bramosystems.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bramosystems.oss.player.playlist.client.spf;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArrayString;

/**
 * Represents an XSPF/JSPF track entry
 *
 * @author Sikirulai Braheem <sbraheem at bramosystems.com>
 * @since 1.3
 */
public final class Track extends JavaScriptObject {

    /**
     * Constructor
     */
    protected Track() {
    }

    /**
     * Returns the name of the album to which this track belongs
     * 
     * @return the name of the album
     */
    public native String getAlbum() /*-{
        return this.album;
    }-*/;

    /**
     * Sets the name of the album to which this track belongs
     * 
     * @param album the name of the album
     */
    public native void setAlbum(String album) /*-{
        this.album = album;
    }-*/;

    /**
     * Returns the comment attached to this track
     * 
     * @return a comment attached to this track
     */
    public native String getAnnotation() /*-{
        return this.annotation;
    }-*/;

    /**
     * Sets a comment on this track
     * 
     * @param annotation the comment to set on this track
     */
    public native void setAnnotation(String annotation) /*-{
        this.annotation = annotation;
    }-*/;

    /**
     * Returns the creator (author) of this track
     * 
     * @return the creator of this track
     */
    public native String getCreator() /*-{
    return this.creator;
    }-*/;

    /**
     * Sets the creator (author) of this track
     * 
     * @param creator the creator of this track
     */
    public native void setCreator(String creator) /*-{
    this.creator = creator;
    }-*/;

    /**
     * Returns the time (in milliseconds) to render this track
     * 
     * @return the rendering time
     */
    public native double getDuration() /*-{
        return this.duration;
    }-*/;

    /**
     * Sets the time (in milliseconds) to render this track
     * 
     * @param duration the rendering time
     */
    public native void setDuration(double duration) /*-{
        this.duration = duration;
    }-*/;

    /**
     * Returns canonical IDs for this track.  The identifier may be a hash or other 
     * location-independent name.
     * 
     * @return canonical IDs for this track
     */
    public native JsArrayString getIdentifier() /*-{
        return this.identifier;
    }-*/;

    /**
     * Sets the canonical IDs for this track.
     * 
     * @param identifier canonical IDs for this track
     */
    public native void setIdentifier(JsArrayString identifier) /*-{
        this.identifier = identifier;
    }-*/;

    /**
     * Returns the URI of the image to display for the duration of the track
     * 
     * @return the URI of the image to display
     */
    public native String getImage() /*-{
        return this.image;
    }-*/;

    /**
     * Sets the URI of the image to display for the duration of the track
     * 
     * @param image the URI of the image to display
     */
    public native void setImage(String image) /*-{
        this.image = image;
    }-*/;

    /**
     * Returns the URI of the location where more information about this track can be
     * retrieved.
     * 
     * @return the URI of the location with more information
     */
    public native String getInfo() /*-{
        return this.info;
    }-*/;

    /**
     * Sets the URI of the location where more information about this track can be 
     * retrieved.
     * 
     * @param info the URI of the location with more information
     */
    public native void setInfo(String info) /*-{
        this.info = info;
    }-*/;

    /**
     * Returns the URI(s) of the media to be rendered.
     * 
     * <p>The track may be located with multiple URIs probably in different formats
     * 
     * @return the URI(s) of the media
     */
    public native JsArrayString getLocation() /*-{
        return this.location;
    }-*/;

    /**
     * Sets the URI(s) of the media to be rendered.
     * 
     * @param location the URI(s) of the media
     */
    public native void setLocation(JsArrayString location) /*-{
        this.location = location;
    }-*/;

    /**
     * Returns the title of this track
     * 
     * @return the title of this track
     */
    public native String getTitle() /*-{
        return this.title;
    }-*/;

    /**
     * Sets the title of this track
     * 
     * @param title the title of this track
     */
    public native void setTitle(String title) /*-{
        this.title = title;
    }-*/;

    /**
     * Returns the ordinal position of this track on its album.  The number
     * is usually greater than zero.
     * 
     * @return the ordinal position of this track on its album.
     */
    public native double getTrackNumber() /*-{
        return this.trackNum;
    }-*/;

    /**
     * Sets the ordinal position of this track on its album
     * 
     * @param trackNum the ordinal position of this track on its album
     */
    public native void setTrackNumber(double trackNum) /*-{
        this.trackNum = trackNum;
    }-*/;    
}