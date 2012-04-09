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
package com.bramosystems.oss.player.playlist.client.asx;

import com.bramosystems.oss.player.core.client.PlayTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Describes an ASX playlist entry
 *
 * @author Sikirulai Braheem <sbraheem at bramosystems.com>
 * @since 1.3
 */
public class ASXEntry {
    private boolean clientSkip, skipIfRef;
    
    private String _abstract, author, baseHref, copyright, moreInfoHref, title;
    private PlayTime duration, startTime;
    private HashMap<String, String> params;
    private List<Ref> refs;

    /**
     * Creates an empty ASX entry
     */
    public ASXEntry() {
        clientSkip = true;
        skipIfRef = false;
        params = new HashMap<String, String>();
        refs = new ArrayList<Ref>();
    }
    
    /**
     * Returns the abstract describing this entry
     * 
     * @return the abstract
     */
    public String getAbstract() {
        return _abstract;
    }

    /**
     * Sets the abstract element that describes this entry
     * 
     * @param _abstract the abstract
     */
    public void setAbstract(String _abstract) {
        this._abstract = _abstract;
    }

    /**
     * Returns the author of this entry
     * 
     * @return the author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Sets the author of this entry
     * 
     * @param author the author of this entry
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /*
     * 
     *
    public String getBaseHref() {
        return baseHref;
    }

    public void setBaseHref(String baseHref) {
        this.baseHref = baseHref;
    }
     * 
     */

    /**
     * Checks if this entry can be skipped during playback
     * 
     * @return {@code true} if this entry can be skipped, {@code false} otherwise
     */
    public boolean isClientSkip() {
        return clientSkip;
    }

    /**
     * Sets if this entry can be skipped during playback
     * 
     * @param clientSkip {@code true} if this entry can be skipped, {@code false} otherwise
     */
    public void setClientSkip(boolean clientSkip) {
        this.clientSkip = clientSkip;
    }

    /**
     * Returns the copyright information of this playlist entry
     * 
     * @return the copyright information
     */
    public String getCopyright() {
        return copyright;
    }

    /**
     * Sets the copyright information of this playlist entry
     * 
     * @param copyright the copyright information
     */
    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    /**
     * Returns the playback duration of the playlist entry
     * 
     * @return the duration of playback 
     */
    public PlayTime getDuration() {
        return duration;
    }

    /**
     * Sets the playback duration of the playlist entry
     * 
     * @param duration the duration of playback
     */
    public void setDuration(PlayTime duration) {
        this.duration = duration;
    }

   /**
     * Returns the URL or e-mail address that provides more information about this playlist entry
     * 
     * @return URL or e-mail address that provides more information about this playlist entry
     */
    public String getMoreInfoHref() {
        return moreInfoHref;
    }

    /**
     * Sets the URL or e-mail address that provides more information about this playlist entry
     * 
     * @param moreInfoHref the URL or e-mail address
     */
    public void setMoreInfoHref(String moreInfoHref) {
        this.moreInfoHref = moreInfoHref;
    }

   /**
     * Returns the custom parameters associated with this playlist entry
     * 
     * @return custom parameters in the playlist entry
     */
    public HashMap<String, String> getParams() {
        return params;
    }

    /**
     * Associates custom paramters with this playlist entry
     * 
     * @param params custom parameters for the playlist entry
     */
    public void setParams(HashMap<String, String> params) {
        this.params = params;
    }

    /**
     * Returns the URL references of this media entry.  The URL may point to the same media content
     * probably in different formats.
     * 
     * @return the URL references of this media entry
     */
    public List<Ref> getRefs() {
        return refs; 
    }

    /**
     * Sets the URL references of this media entry.
     * 
     * @param refs the URL references of this media entry
     */
    public void setRefs(List<Ref> refs) {
        this.refs = refs;
    }

    /**
     * Checks if this entry should be skipped by a player when referenced by another playlist
     * 
     * @return {@code true} if this entry should be skipped when referenced by another playlist,
     * {@code false} otherwise
     */
    public boolean isSkipIfRef() {
        return skipIfRef;
    }

    /**
     * Sets if this entry should be skipped by a player when referenced by another playlist
     * 
     * @param skipIfRef {@code true} if this entry should be skipped when referenced by another playlist,
     * {@code false} otherwise
     */
    public void setSkipIfRef(boolean skipIfRef) {
        this.skipIfRef = skipIfRef;
    }

    /**
     * Returns the time when playback should start
     * 
     * @return the time when playback should start
     */
    public PlayTime getStartTime() {
        return startTime;
    }

    /**
     * Sets the time when playback should start
     * 
     * @param startTime the time when playback should start
     */
    public void setStartTime(PlayTime startTime) {
        this.startTime = startTime;
    }

    /**
     * Returns the title of this entry
     * 
     * @return the title of this playlist entry
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of this playlist entry
     * 
     * @param title the title of this playlist entry
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns a more descriptive representation of this entry. Useful during debugging
     */
    @Override
    public String toString() {
        return "ASXEntry{" + "_abstract=" + _abstract + ", author=" + author + ", copyright=" + copyright + 
                ", moreInfoHref=" + moreInfoHref + ", title=" + title + ", duration=" + duration +
                ", startTime=" + startTime + ", params=" + params + ", refs=" + refs + '}';
    }
}
