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

import java.util.ArrayList;
import java.util.List;

/**
 * Defines the number of times the contained media entries should be played.
 *
 * @author Sikirulai Braheem <sbraheem at bramosystems.com>
 * @since 1.3
 */
public class Repeat {
    private int count;
    private List<ASXEntry> entry;
    private List<String> entryRef;

    /**
     * Constructor
     */
    public Repeat() {
        count = Integer.MAX_VALUE;
        entry = new ArrayList<ASXEntry>();
        entryRef = new ArrayList<String>();
    }

    /**
     * Returns the repeat count
     * 
     * @return the repeat count
     */
    public int getCount() {
        return count;
    }

    /**
     * Sets the repeat count
     * 
     * @param count the repeat count
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     * Returns the playlist entries to be repeated
     * 
     * @return the entries to be repeated
     */
    public List<ASXEntry> getEntries() {
        return entry;
    }

    /**
     * Sets the playlist entries to be repeated
     * 
     * @param entries the entries to be repeated
     */
    public void setEntries(List<ASXEntry> entries) {
        this.entry = entries;
    }

    /**
     * Returns the URLs to external ASX playlists linked to this playlist
     * 
     * @return URLs of external ASX playlists
     */
    public List<String> getEntryRefs() {
        return entryRef;
    }

    /**
     * Sets the URLs of external ASX playlists linked to this playlist
     * 
     * @param entryRefs URLs of external ASX playlists
     */
    public void setEntryRefs(List<String> entryRefs) {
        this.entryRef = entryRefs;
    }

    /**
     * Returns a more descriptive representation of this . Useful during debugging
     */
    @Override
    public String toString() {
        return "Repeat{" + "count=" + count + ", entry=" + entry + ", entryRef=" + entryRef + '}';
    }
}
