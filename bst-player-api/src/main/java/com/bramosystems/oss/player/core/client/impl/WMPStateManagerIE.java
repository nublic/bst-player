/*
 * Copyright 2009 Sikirulai Braheem
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
package com.bramosystems.oss.player.core.client.impl;

/**
 * IE specific native implementation of the WMPStateManager class. It is not recommended to
 * interact with this class directly.
 *
 * @author Sikirulai Braheem
 */
public class WMPStateManagerIE extends WMPStateManager {

    WMPStateManagerIE() {
    }

    @Override
    public boolean shouldRunResizeQuickFix() {
        return false;   // fix not required in IE. resizing works just fine...
    }

    @Override
    protected void initGlobalEventListeners(WMPStateManager impl) {
        // override this and do nothing to avoid IE errors. Method is just a
        // workaround for non IE browsers.
    }

    @Override
    public void registerMediaStateHandlers(WinMediaPlayerImpl player) {
        registerMediaStateHandlerImpl(this, player);
    }

    private native void registerMediaStateHandlerImpl(WMPStateManagerIE impl, WinMediaPlayerImpl player) /*-{
    player.attachEvent('playStateChange', function(NewState) {
    impl.@com.bramosystems.oss.player.core.client.impl.WMPStateManagerIE::firePlayStateChanged(Ljava/lang/String;I)(player.id, NewState);
    });
    player.attachEvent('buffering', function(Start) {
    impl.@com.bramosystems.oss.player.core.client.impl.WMPStateManagerIE::fireBuffering(Ljava/lang/String;Z)(player.id, Start);
    });
    player.attachEvent('error', function() {
    impl.@com.bramosystems.oss.player.core.client.impl.WMPStateManagerIE::fireError(Ljava/lang/String;)(player.id);
    });
    impl.@com.bramosystems.oss.player.core.client.impl.WMPStateManagerIE::firePlayStateChanged(Ljava/lang/String;I)(player.id, player.playState);
    }-*/;

    @SuppressWarnings("unused")
    private void firePlayStateChanged(String playerId, int state) {
        cache.get(playerId).processPlayState(state);
    }

    @SuppressWarnings("unused")
    private void fireBuffering(String playerId, boolean buffering) {
        cache.get(playerId).doBuffering(buffering);
    }

    @SuppressWarnings("unused")
    private void fireError(String playerId) {
        cache.get(playerId).checkError();
    }
}
