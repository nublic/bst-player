/*
 * Copyright 2011 User.
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
package com.bramosystems.oss.player.youtube.client.impl;

import com.bramosystems.oss.player.core.client.PlayException;
import com.bramosystems.oss.player.core.client.PlaylistSupport;
import com.bramosystems.oss.player.core.client.playlist.MRL;
import com.bramosystems.oss.player.util.client.RegExp;
import com.bramosystems.oss.player.util.client.RegExp.RegexException;
import com.bramosystems.oss.player.util.client.RegExp.RegexResult;
import java.util.ArrayList;
import java.util.List;

/**
 * Cache video IDs for youtube playlist.
 *
 * @author Sikiru Braheem
 * @since 1.3
 */
public class YouTubePlaylistManager implements PlaylistSupport {
    
    public static interface CallbackHandler {

        public void onError(String message);

        public void onInfo(String info);

        public YouTubePlayerImpl getPlayerImpl();
    }
    private String utubeRegex = "http(s)?\\\\www\\.youtube\\.com\\v\\(\\w+)(\\?\\w+)?";
    private boolean isShuffled;
    private ArrayList<String> videoIds = new ArrayList<String>();
    private CallbackHandler callback;
    
    public YouTubePlaylistManager(CallbackHandler callback) {
        this.callback = callback;
    }
    
    private boolean isYouTubeURL(String url) {
        try {
            RegExp reg = RegExp.getRegExp(utubeRegex, "gi");
            return reg.test(url);
        } catch (RegexException ex) {
            return false;
        }
    }
    
    private String getVideoId(String url) throws RegexException {
        RegExp reg = RegExp.getRegExp(utubeRegex, "gi");
        RegexResult rr = reg.exec(url);
        return rr.getMatch(2);
    }
    
    public void commitPlaylist() {
        for (String vid : videoIds) {
            callback.onInfo("Added to playlist : '" + vid + "'");
            callback.getPlayerImpl().getPlaylist().push(vid);
        }
        callback.onInfo("- Playlist Size : "+ callback.getPlayerImpl().getPlaylist().length());        
        callback.onInfo(callback.getPlayerImpl().getPlaylist().toString());        
    }
    
    @Override
    public void setShuffleEnabled(boolean enable) {
        isShuffled = enable;
    }
    
    @Override
    public boolean isShuffleEnabled() {
        return isShuffled;
    }
    
    @Override
    public void removeFromPlaylist(int index) {
        videoIds.remove(index);
    }
    
    @Override
    public void clearPlaylist() {
        videoIds.clear();
    }
    
    @Override
    public void addToPlaylist(String mediaURL) {
        if (isYouTubeURL(mediaURL)) {  // URL is complete YouTube video URL
            try {
                videoIds.add(getVideoId(mediaURL));  // get videoId from URL
            } catch (RegexException ex) {
                callback.onError("URL not added to playlist - " + ex.getMessage());
            }
        } else {  // url assumed to be YouTube video Id ...
            videoIds.add(mediaURL);
        }
    }
    
    @Override
    public void addToPlaylist(String... mediaURLs) {
        addToPlaylist(mediaURLs[0]);
    }
    
    @Override
    public void addToPlaylist(MRL mediaLocator) {
        addToPlaylist(mediaLocator.getNextResource(true));
    }
    
    @Override
    public void addToPlaylist(List<MRL> mediaLocators) {
        for (MRL mrl : mediaLocators) {
            addToPlaylist(mrl);
        }
    }
    
    @Override
    public void playNext() throws PlayException {
        throw new UnsupportedOperationException("Not supported here.");
    }
    
    @Override
    public void playPrevious() throws PlayException {
        throw new UnsupportedOperationException("Not supported here.");
    }
    
    @Override
    public void play(int index) throws IndexOutOfBoundsException {
        throw new UnsupportedOperationException("Not supported here.");
    }
    
    @Override
    public int getPlaylistSize() {
        return videoIds.size();
    }
}
