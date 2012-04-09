/*
 * Copyright 2010 Max Berger
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
package com.bramosystems.oss.player.dev.client;

import com.bramosystems.oss.player.core.client.AbstractMediaPlayer;
import com.bramosystems.oss.player.core.client.LoadException;
import com.bramosystems.oss.player.core.client.PlayException;
import com.bramosystems.oss.player.core.client.PlaylistSupport;
import java.util.ArrayList;
import java.util.List;

import com.bramosystems.oss.player.core.event.client.PlayStateEvent;
import com.bramosystems.oss.player.core.event.client.PlayStateHandler;
import com.google.gwt.user.client.Random;

/**
 * Abstract implementation of a media player with added support for playlists.
 * 
 * @author Max Berger
 */
public abstract class AbstractMediaPlayerWithPlaylist extends
        AbstractMediaPlayer implements PlaylistSupport {

    private boolean shuffleOn = false;
    private int pos = -1;
    private List<String> playList = new ArrayList<String>();

    public AbstractMediaPlayerWithPlaylist() {
        super();
        addPlayStateHandler(new PlayStateHandler() {

            @Override
            public void onPlayStateChanged(PlayStateEvent event) {
                if (PlayStateEvent.State.Finished.equals(event.getPlayState())) {
                    if (!AbstractMediaPlayerWithPlaylist.this.playList
                            .isEmpty()) {
                        try {
                            AbstractMediaPlayerWithPlaylist.this.playNext();
                        } catch (PlayException e) {
                            fireError(e.getMessage());
                        }
                    }
                }
            }
        });
    }

    private void loadSafely(String mediaURL) {
        try {
            this.loadMedia(mediaURL);
        } catch (LoadException e) {
            fireError(e.getMessage());
        }
    }

    @Override
    public void addToPlaylist(String mediaURL) {
        playList.add(mediaURL);
        if (playList.size() == 1) {
            pos = 0;
            loadSafely(mediaURL);
        }
    }

    @Override
    public void clearPlaylist() {
        this.stopMedia();
        playList.clear();
        pos = -1;
    }

    @Override
    public int getPlaylistSize() {
        return playList.size();
    }

    @Override
    public boolean isShuffleEnabled() {
        return shuffleOn;
    }

    @Override
    public void play(int index) throws IndexOutOfBoundsException {
        if (index == playList.size()) {
            this.stopMedia();
            pos = -1;
        } else {
            String url = playList.get(index);
            pos = index;
            loadSafely(url);
            try {
                this.playMedia();
            } catch (PlayException e) {
                fireError(e.getMessage());
            }
        }
    }

    private void playPos() throws PlayException {
        loadSafely(playList.get(pos));
        this.playMedia();
    }

    @Override
    public void playNext() throws PlayException {
        if (this.getPlayPosition() < 0.01) {
            try {
                this.playMedia();
                return;
            } catch (PlayException e) {
                fireDebug(e.getMessage());
            }
        }
        if (playList.isEmpty()) {
            pos = -1;
        } else if (isShuffleEnabled()) {
            pos = Random.nextInt(playList.size());
        } else {
            pos++;
        }
        if (pos < 0 || pos >= playList.size()) {
            this.stopMedia();
            pos = -1;
        } else {
            playPos();
        }
    }

    @Override
    public void playPrevious() throws PlayException {
        if (playList.isEmpty()) {
            pos = -1;
        }
        if (isShuffleEnabled() || (pos <= 0)) {
            this.setPlayPosition(0);
        } else {
            pos--;
            playPos();
        }
    }

    @Override
    public void removeFromPlaylist(int index) {
        playList.remove(index);
        if (index == pos) {
            this.play(index);
        }
    }

    @Override
    public void setShuffleEnabled(boolean enable) {
        shuffleOn = enable;
    }

}
