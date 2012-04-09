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

import com.bramosystems.oss.player.core.client.PlaylistSupport;
import com.bramosystems.oss.player.core.client.playlist.MRL;
import com.bramosystems.oss.player.core.client.playlist.Playlist;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * Class represents an ASX playlist.
 *
 * @author Sikirulai Braheem <sbraheem at bramosystems.com>
 * @since 1.3
 */
public final class ASXPlaylist  {
    private double version;
    private boolean previewMode;
//    private BannerBar bannerBar;        
    private String _abstract, author, baseHref, copyright, moreInfoHref, title;
    private Repeat repeat;
    private List<ASXEntry> entries;
    private List<String> entryRefs;
    private HashMap<String, String> params;

    /**
     * Creates an empty ASX playlist
     */
    public ASXPlaylist() {
        previewMode = false;
//        bannerBar = BannerBar.Auto;
        params = new HashMap<String, String>();
        entries = new ArrayList<ASXEntry>();
        entryRefs = new ArrayList<String>();
    }

    /**
     * Returns the abstract element.  The abstract gives a discription of the playlist.
     * 
     * @return the abstract
     */
    public String getAbstract() {
        return _abstract;
    }

    /**
     * Sets the abstract element of the playlist
     * 
     * @param _abstract describes the playlist
     */
    public void setAbstract(String _abstract) {
        this._abstract = _abstract;
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
     * @param author the author of the playlist
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /*
    public BannerBar getBannerBar() {
        return bannerBar;
    }

    public void setBannerBar(BannerBar bannerBar) {
        this.bannerBar = bannerBar;
    }

    public String getBaseHref() {
        return baseHref;
    }

    public void setBaseHref(String baseHref) {
        this.baseHref = baseHref;
    }
     * 
     */

    /**
     * Checks if the player should enter preview mode before playing the first clip
     * 
     * @return {@code true} if player should preview the first clip before playing it,
     * {@code false} otherwise
     */
    public boolean isPreviewMode() {
        return previewMode;
    }

    /**
     * Sets if the player should preview the first clip before playing it.
     * 
     * @param previewMode {@code true} if player should preview the first clip, {@code false}
     * otherwise
     */
    public void setPreviewMode(boolean previewMode) {
        this.previewMode = previewMode;
    }

    /**
     * Returns the copyright information of the playlist
     * 
     * @return the copyright information
     */
    public String getCopyright() {
        return copyright;
    }

    /**
     * Sets the copyright information of the playlist
     * 
     * @param copyright the copyright information
     */
    public void setCopyright(String copyright) {
        this.copyright = copyright;
    }

    /**
     * Returns the entries in this playlist
     * 
     * @return the entries in this playlist
     */
    public List<ASXEntry> getEntries() {
        return entries;
    }

    /**
     * Sets the entries of this playlist
     * 
     * @param entries the playlist entries
     */
    public void setEntries(List<ASXEntry> entries) {
        this.entries = entries;
    }

    /**
     * Returns the URLs to external ASX playlists linked to this playlist
     * 
     * @return URLs of external ASX playlists
     */
    public List<String> getEntryRefs() {
        return entryRefs;
    }

    /**
     * Sets the URLs of external ASX playlists linked to this playlist
     * 
     * @param entryRefs URLs of external ASX playlists
     */
    public void setEntryRefs(List<String> entryRefs) {
        this.entryRefs = entryRefs;
    }

    /**
     * Returns the URL or e-mail address that provides more information about this playlist
     * 
     * @return URL or e-mail address that provides more information about this playlist
     */
    public String getMoreInfoHref() {
        return moreInfoHref;
    }

    /**
     * Sets the URL or e-mail address that provides more information about this playlist
     * 
     * @param moreInfoHref the URL or e-mail address
     */
    public void setMoreInfoHref(String moreInfoHref) {
        this.moreInfoHref = moreInfoHref;
    }

    /**
     * Returns the custom parameters associated with this playlist
     * 
     * @return custom parameters in the playlist
     */
    public HashMap<String, String> getParams() {
        return params;
    }

    /**
     * Associates custom paramters with this playlist
     * 
     * @param params custom parameters for the playlist
     */
    public void setParams(HashMap<String, String> params) {
        this.params = params;
    }

    /**
     * Returns the playlist entries setup for playback repeats
     * 
     * @return playlist entries setup for playback repeats
     */
    public Repeat getRepeat() {
        return repeat;
    }

    /**
     * Sets the playlist entries with playback repeats
     * 
     * @param repeat entries with playback repeats
     */
    public void setRepeat(Repeat repeat) {
        this.repeat = repeat;
    }

    /**
     * Returns the title of this playlist
     * 
     * @return the playlist title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the title of this playlist
     * 
     * @param title the playlist title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns the version of the ASX metafile from which this playlist is derived
     * 
     * @return the version of the ASX metafile
     */
    public double getVersion() {
        return version;
    }

    /**
     * Sets the version of the ASX metafile
     * 
     * @param version the version of the ASX metafile
     */
    public void setVersion(double version) {
        this.version = version;
    }

    /**
     * Returns a more descriptive representation of this playlist. Useful during debugging
     */
    @Override
    public String toString() {
        return "ASXPlaylist{" + "version=" + version + ", _abstract=" + _abstract + ", author=" + author + 
                ", copyright=" + copyright + ", moreInfoHref=" + moreInfoHref + ", title=" + title + 
                ", entries=" + entries + ", entryRefs=" + entryRefs + ", params=" + params + '}';
    }
    
    /*
    public static enum BannerBar {
        Auto, Fixed;
    }
     * 
     */
    
    /**
     * Returns this playlist as a Playlist object that can be used with player widgets 
     * with {@link PlaylistSupport}
     * 
     * @return Playlist object
     */
    public Playlist toPlaylist() {
        Playlist p = new Playlist();
        p.setName(title);
        p.setAuthor(author);
        
        Iterator<ASXEntry> it = entries.iterator();
        while(it.hasNext()) {
            ASXEntry ae = it.next();
            MRL m = new MRL(ae.getTitle(), ae.getAuthor());
            Iterator<Ref> refs = ae.getRefs().iterator();
            while(refs.hasNext()) {
                Ref ref = refs.next();
                m.addURL(ref.getHref());
            }
            p.add(m);
        }
        return p;
    }
}
