/*
 * Copyright 2011 Sikiru Braheem.
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

package com.bramosystems.oss.player.core.client.playlist;

import java.util.ArrayList;

/**
 * Represents a playlist of MRLs
 *
 * @author Sikiru Braheem
 * @since 1.3
 */
public class Playlist extends ArrayList<MRL> {
    
    private String name, author;

    /**
     * Creates a Playlist object
     */
    public Playlist() {
    }

    /**
     * Returns the author of the playlist
     * 
     * @return the author of the playlist
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Sets the author of the playlist
     * 
     * @param author the playlist author
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * Returns the name of the playlist
     * 
     * @return the name of the playlist
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the playlist
     * 
     * @param name the name of the playlist
     */
    public void setName(String name) {
        this.name = name;
    }
}
