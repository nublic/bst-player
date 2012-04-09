/*
 *  Copyright 2010 Sikiru.
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
package com.bramosystems.oss.player.showcase.client;

import com.bramosystems.oss.player.core.client.RepeatMode;

/**
 *
 * @author Sikiru
 */
public class PlayerOptions {

    private boolean showControls, resizeToVideo, showLogger, shuffleOn;
    private RepeatMode repeatMode;

    public PlayerOptions() {
        showControls = true;
        repeatMode = RepeatMode.REPEAT_OFF;
    }

    public boolean isShuffleOn() {
        return shuffleOn;
    }

    public void setShuffleOn(boolean shuffleOn) {
        this.shuffleOn = shuffleOn;
    }

    public boolean isResizeToVideo() {
        return resizeToVideo;
    }

    public void setResizeToVideo(boolean resizeToVideo) {
        this.resizeToVideo = resizeToVideo;
    }

    public boolean isShowControls() {
        return showControls;
    }

    public void setShowControls(boolean showControls) {
        this.showControls = showControls;
    }

    public boolean isShowLogger() {
        return showLogger;
    }

    public RepeatMode getRepeatMode() {
        return repeatMode;
    }

    public void setRepeatMode(RepeatMode repeatMode) {
        this.repeatMode = repeatMode;
    }

    public void setShowLogger(boolean showLogger) {
        this.showLogger = showLogger;
    }
}
