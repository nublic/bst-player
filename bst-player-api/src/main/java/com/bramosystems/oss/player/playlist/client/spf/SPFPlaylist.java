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

import com.bramosystems.oss.player.core.client.PlaylistSupport;
import com.bramosystems.oss.player.core.client.playlist.MRL;
import com.bramosystems.oss.player.core.client.playlist.Playlist;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.core.client.JsArrayString;
import com.google.gwt.core.client.JsDate;
import java.util.Date;

/**
 * Class represents an XSPF/JSPF playlist
 *
 * @author Sikirulai Braheem <sbraheem at bramosystems.com>
 * @since 1.3
 */
public final class SPFPlaylist extends JavaScriptObject {

    /**
     * Constructor
     */
    protected SPFPlaylist() {
    }

    /**
     * Returns the comment associated with this playlist
     * 
     * @return the comment associated with this playlist
     */
    public native String getAnnotation() /*-{
    return this.annotation;
    }-*/;

    /**
     * Associates a comment with this playlist
     * 
     * @param annotation the comment associated with this playlist
     */
    public native void setAnnotation(String annotation) /*-{
    this.annotation = annotation;
    }-*/;

    /**
     * Returns the attribution to the original playlist
     * 
     * @return the attribution to the original playlist
     */
    public native Attribution getAttribution() /*-{
    return this.attribution;
    }-*/;

    /**
     * Sets the attribution to the original playlist
     * 
     * @param attribution the attribution to the original playlist
     */
    public native void setAttribution(Attribution attribution) /*-{
    this.attribution = attribution;
    }-*/;

    /**
     * Returns the creator (author) of this playlist
     * 
     * @return the creator of this playlist
     */
    public native String getCreator() /*-{
    return this.creator;
    }-*/;

    /**
     * Sets the creator (author) of this playlist
     * 
     * @param creator the creator of this playlist
     */
    public native void setCreator(String creator) /*-{
    this.creator = creator;
    }-*/;

    /**
     * Returns the date this playlist was created
     * 
     * @return the creation date of this playlist
     */
    public final Date getDate() {
        return new Date((long)getDateImpl().getTime());
    }

    /**
     * Sets the date this playlist was created
     * 
     * @param date the creation date of this playlist
     */
    public final void setDate(Date date) {
        setDateImpl(JsDate.create(date.getTime()));
    }

    private native JsDate getDateImpl() /*-{
    return this.date;
    }-*/;

    private native void setDateImpl(JsDate date) /*-{
    this.date = date;
    }-*/;

    /**
     * Returns a canonical identifier for this playlist
     * 
     * @return a canonical identifier for this playlist
     */
    public native String getIdentifier() /*-{
    return this.identifier;
    }-*/;

    /**
     * Sets a canonical identifier for this playlist.
     * 
     * @param identifier a canonical identifier for this playlist
     */
    public native void setIdentifier(String identifier) /*-{
    this.identifier = identifier;
    }-*/;

    /**
     * Returns the URI of the image to display for the rendering duration of this playlist.
     * 
     * @return the URI of the general image for the playlist
     */
    public native String getImage() /*-{
    return this.image;
    }-*/;

    /**
     * Sets the URI of the image to display for the rendering duration of this playlist
     * 
     * @param image the URI of the image to use for this playlist
     */
    public native void setImage(String image) /*-{
    this.image = image;
    }-*/;

    /**
     * Returns the URI of a location containing more information about this playlist
     * 
     * @return the URI of a location containing more information about this playlist
     */
    public native String getInfo() /*-{
    return this.info;
    }-*/;

    /**
     * Sets the URI of a location containing more information about this playlist
     * 
     * @param info the URI of a location containing more information about this playlist
     */
    public native void setInfo(String info) /*-{
    this.info = info;
    }-*/;

    /**
     * Returns the URI of the license under which this playlist was released
     * 
     * @return the URI of the license
     */
    public native String getLicense() /*-{
    return this.license;
    }-*/;

    /**
     * Sets the URI of the license under which this playlist was released
     * 
     * @param license the URI of the license
     */
    public native void setLicense(String license) /*-{
    this.license = license;
    }-*/;

    /**
     * Returns the source URI of this playlist
     * 
     * @return the source URI of this playlist
     */
    public native String getLocation() /*-{
    return this.location;
    }-*/;

    /**
     * Sets the source URI of this playlist
     * 
     * @param location the source URI of this playlist
     */
    public native void setLocation(String location) /*-{
    this.location = location;
    }-*/;

    /**
     * Returns the title of this playlist
     * 
     * @return the title of this playlist
     */
    public native String getTitle() /*-{
    return this.title;
    }-*/;

    /**
     * Sets the title of this playlist
     * 
     * @param title the title of this playlist
     */
    public native void setTitle(String title) /*-{
    this.title = title;
    }-*/;

    /**
     * Returns the track entries in this playlist
     * 
     * @return the track entries in this playlist
     */
    public native JsArray<Track> getTracks() /*-{
    return this.track;
    }-*/;

    /**
     * Sets the track entries of this playlist
     * 
     * @param tracks the track entries of this playlist
     */
    public native void setTracks(JsArray<Track> tracks) /*-{
    this.track = tracks;
    }-*/;

    /**
     * Returns this playlist as a Playlist object that can be used with player widgets 
     * with {@link PlaylistSupport}
     * 
     * @return Playlist object
     */
    public final Playlist toPlaylist() {
        Playlist p = new Playlist();
        p.setName(getTitle());
        p.setAuthor(getCreator());

        JsArray<Track> ts = getTracks();
        for (int i = 0; i < ts.length(); i++) {
            Track t = ts.get(i);

            MRL m = new MRL(t.getTitle(), t.getCreator());
            JsArrayString js = t.getLocation();
            for (int j = 0; j < js.length(); j++) {
                m.addURL(js.get(i));
            }
            p.add(m);
        }
        return p;
    }
}
