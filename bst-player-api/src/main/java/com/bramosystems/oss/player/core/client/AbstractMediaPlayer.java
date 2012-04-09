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
package com.bramosystems.oss.player.core.client;

import com.bramosystems.oss.player.core.client.impl.plugin.PlayerManager;
import com.bramosystems.oss.player.core.client.ui.Logger;
import com.bramosystems.oss.player.core.event.client.*;
import com.bramosystems.oss.player.core.client.spi.PlayerProviderFactory;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Composite;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;

/**
 * Abstract implementation of a media player.  It implements the handling
 * of MediaStateListeners.
 *
 * @author Sikiru Braheem
 */
public abstract class AbstractMediaPlayer extends Composite implements HasMediaStateHandlers,
        HasMouseMoveHandlers, HasMouseDownHandlers, HasMouseUpHandlers, HasKeyDownHandlers,
        HasKeyUpHandlers, HasKeyPressHandlers {

    private HashMap<String, Command> readyCmdQueue;
    private ArrayList<String> cmdKeys;

    /**
     * Constructor method.
     */
    public AbstractMediaPlayer() {
        readyCmdQueue = new HashMap<String, Command>();
        cmdKeys = new ArrayList<String>();

        addPlayerStateHandler(new PlayerStateHandler() {

            @Override
            public void onPlayerStateChanged(final PlayerStateEvent event) {
                Timer t = new Timer() {

                    @Override
                    public void run() {
                        if (event.getPlayerState().equals(PlayerStateEvent.State.Ready)) {
                            // ensure commands are executed in the same sequence as added ...
                            Iterator<String> keys = cmdKeys.iterator();
                            while (keys.hasNext()) {
                                readyCmdQueue.get(keys.next()).execute();
                            }
                            cmdKeys.clear();
                            readyCmdQueue.clear();
                        }
                    }
                };
                t.schedule(500);
            }
        });
    }

    /**
     * Loads the media at the specified URL into the player.
     *
     * <p>In respect of the <code>same domain</code> policy of some plugins,
     * the URL should point to a destination on the same domain
     * where the application is hosted.
     *
     * @param mediaURL the URL of the media to load into the player.
     * @throws LoadException if an error occurs while loading the media
     * @throws IllegalStateException if the player is not available, this is the case
     * if the player is not attached to the DOM.
     */
    public abstract void loadMedia(String mediaURL) throws LoadException;

    /**
     * Plays the media loaded into the player.
     *
     * @throws PlayException if an error occurs
     * during media playback.
     * @throws IllegalStateException if the player is not available, this is the case
     * if the player is not attached to the DOM.
     */
    public abstract void playMedia() throws PlayException;

    /**
     * Stops the media playback.
     *
     * @throws IllegalStateException if the player is not available, this is the case
     * if the player is not attached to the DOM.
     */
    public abstract void stopMedia();

    /**
     * Pause the media playback
     *
     * @throws IllegalStateException if the player is not available, this is the case
     * if the player is not attached to the DOM.
     */
    public abstract void pauseMedia();

    /**
     * Returns the duration of the loaded media in milliseconds. An IllegalStateException is
     * thrown is the player is not available
     *
     * @return the duration of the loaded media in milliseconds.
     *
     * @throws IllegalStateException if the player is not available, this is the case
     * if the player is not attached to the DOM.
     */
    public abstract long getMediaDuration();

    /**
     * Gets the current position in the media that is being played.
     *
     * @return the current position of the media being played.
     *
     * @throws IllegalStateException if the player is not available, this is the case
     * if the player is not attached to the DOM.
     */
    public abstract double getPlayPosition();

    /**
     * Sets the playback position of the current media
     *
     * @param position the new position from where to start playback
     *
     * @throws IllegalStateException if the player is not available, this is the case
     * if the player is not attached to the DOM.
     */
    public abstract void setPlayPosition(double position);

    /**
     * Gets the volume ranging from {@code 0} (silent) to {@code 1} (full volume).
     *
     * @return volume.
     *
     * @throws IllegalStateException if the player is not available, this is the case
     * if the player is not attached to the DOM.
     */
    public abstract double getVolume();

    /**
     * Sets the volume.
     *
     * @param volume {@code 0} (silent) to {@code 1} (full volume).
     *
     * @throws IllegalStateException if the player is not available, this is the case
     * if the player is not attached to the DOM.
     */
    public abstract void setVolume(double volume);

    /**
     * Convenience method to fire <code>PlayStateEvent</code>'s on registered handlers.
     *
     * @param state the state of the player
     * @see PlayStateEvent
     */
    protected final void firePlayStateEvent(PlayStateEvent.State state, int itemIndex) {
        PlayStateEvent.fire(this, state, itemIndex);
    }

    /**
     * Convenience method to fire <code>PlayerStateEvent</code>'s on registered handlers.
     *
     * @param state the state of the player
     * @see PlayerStateEvent
     */
    protected final void firePlayerStateEvent(PlayerStateEvent.State state) {
        PlayerStateEvent.fire(this, state);
    }

    /**
     * Convenience method to fire error type <code>DebugEvent</code>'s on registered handlers.
     *
     * @param description the description of the error
     * @see DebugEvent
     */
    protected final void fireError(String description) {
        DebugEvent.fire(this, DebugEvent.MessageType.Error, description);
    }

    /**
     * Convenience method to fire <code>DebugEvent</code>'s with informational messages
     * on registered handlers.
     *
     * @param message the message
     * @see DebugEvent
     */
    protected final void fireDebug(String message) {
        DebugEvent.fire(this, DebugEvent.MessageType.Info, message);
    }

    /**
     * Convenience method to fire <code>LoadingProgressEvent</code>'s on registered handlers.
     *
     * @param progress the progress of the loading operation
     * @see LoadingProgressEvent
     */
    protected final void fireLoadingProgress(double progress) {
        LoadingProgressEvent.fire(this, progress);
    }

    /**
     * Convenience method to fire <code>MediaInfoEvent</code>'s on registered handlers.
     *
     * @param info the metadata of the loaded media
     * @see MediaInfoEvent
     */
    protected final void fireMediaInfoAvailable(MediaInfo info) {
        MediaInfoEvent.fire(this, info);
    }

    /**
     * Displays or hides the players' logger widget.  The logger widget logs debug messages which can
     * be useful during development.
     *
     * @param show <code>true</code> to make the logger widget visible, <code>false</code> otherwise.
     * @see Logger
     * @since 0.6
     */
    public void showLogger(boolean show) {
    }

    /**
     * Displays or hides the player controls.  This implementation does nothing.  Subclasses that
     * permit showing/hiding of controls should override this method and implement accordingly.
     *
     * @param show <code>true</code> to make the player controls visible, <code>false</code> otherwise.
     * @since 0.6
     */
    public void setControllerVisible(boolean show) {
    }

    /**
     * Checks whether the player controls are visible.  This implementation return true.  Subclasses that
     * permit showing/hiding of controls should override this method and implement accordingly.
     *
     * @return <code>true</code> if player controls are visible, <code>false</code> otherwise.
     * @since 0.6
     */
    public boolean isControllerVisible() {
        return true;
    }

    /**
     * Sets the number of times the current media file should repeat playback before stopping.
     * This implementation does nothing, subclasses should override and implement accordingly.
     *
     * <p><b>NB:</b> As of version 1.2, the behaviour of this property depends on
     * the {@link #setRepeatMode(RepeatMode)} property
     *
     * @param loop number of times to repeat playback. A negative value makes playback repeat forever!.
     * @since 0.6
     */
    public void setLoopCount(int loop) {
    }

    /**
     * Returns the number of times this player repeats playback before stopping. This
     * implementation returns <code>0</code>, subclasses should override and implement
     * accordingly.
     *
     * @return the number of times this player will repeat playback before stopping.
     * @since 0.6
     */
    public int getLoopCount() {
        return 0;
    }

    /**
     * Adds the specified command to this players' command queue.  The command queue
     * is a hash-map of commands that are schedule for execution as soon as the underlying
     * plugin is ready for javascript interaction.
     *
     * <p>The execution of the commands is tied to the <code>Ready</code> state of the
     * <code>PlayerState</code> event of the underlying plugin.
     * All scheduled commands are executed exactly once.
     *
     * <p><b>Note:</b> If multiple commands use the same key, only the last command is executed.
     *
     * @param key key with which the specified command is to be associated
     * @param command the command to execute when the player plugin is ready for interaction
     * @since 1.0
     */
    protected final void addToPlayerReadyCommandQueue(String key, Command command) {
        if (cmdKeys.contains(key)) {
            cmdKeys.remove(key);
        }
        cmdKeys.add(key);   // ensure commands are executed in the same sequence as added ...
        readyCmdQueue.put(key, command);
    }

    /**
     * Removes a queued command with the specified <code>key</code> from this players'
     * command queue.
     *
     * @param key key whose command is to be removed
     * @since 1.0
     * @see #addToPlayerReadyCommandQueue(java.lang.String, com.google.gwt.user.client.Command)
     */
    protected final void removeFromPlayerReadyCommandQueue(String key) {
        cmdKeys.remove(key);
        readyCmdQueue.remove(key);
    }

    /**
     * Adds the specified loading progress handler to the player
     *
     * @param handler the handler
     * @return the HandlerRegistration used to remove the handler
     * @since 1.0
     * @see LoadingProgressHandler
     */
    @Override
    public final HandlerRegistration addLoadingProgressHandler(LoadingProgressHandler handler) {
        return addHandler(handler, LoadingProgressEvent.TYPE);
    }

    /**
     * Adds the specified MediaInfo handler to the player
     *
     * @param handler the handler
     * @return the HandlerRegistration used to remove the handler
     * @since 1.0
     * @see MediaInfoHandler
     */
    @Override
    public final HandlerRegistration addMediaInfoHandler(MediaInfoHandler handler) {
        return addHandler(handler, MediaInfoEvent.TYPE);
    }

    /**
     * Adds the specified play-state handler to the player
     *
     * @param handler the handler
     * @return the HandlerRegistration used to remove the handler
     * @see PlayStateHandler
     * @since 1.0
     */
    @Override
    public final HandlerRegistration addPlayStateHandler(PlayStateHandler handler) {
        return addHandler(handler, PlayStateEvent.TYPE);
    }

    /**
     * Adds the specified player-state handler to the player
     *
     * @param handler the handler
     * @return the HandlerRegistration used to remove the handler
     * @since 1.0
     * @see PlayerStateHandler
     */
    @Override
    public final HandlerRegistration addPlayerStateHandler(PlayerStateHandler handler) {
        return addHandler(handler, PlayerStateEvent.TYPE);
    }

    /**
     * Adds the specified debug handler to the player
     *
     * @param handler the handler
     * @return the HandlerRegistration used to remove the handler
     * @see DebugHandler
     * @since 1.0
     */
    @Override
    public final HandlerRegistration addDebugHandler(DebugHandler handler) {
        return addHandler(handler, DebugEvent.TYPE);
    }

    /**
     * If the current media is a video, sets the player to adjust its size to match the
     * dimensions of the video
     *
     * @param resize <code>true</code> if player should adjust its size,
     * <code>false</code> otherwise
     *
     * @since 1.0
     */
    public void setResizeToVideoSize(boolean resize) {
    }

    /**
     * Checks if player is set to adjust its size to match the dimensions of a video.
     *
     * @return <code>true</code> if player adjusts its size, <code>false</code> otherwise
     * @since 1.0
     */
    public boolean isResizeToVideoSize() {
        return false;
    }

    /**
     * Returns the height of the current media
     *
     * @return the height in pixels
     * @since 1.0
     */
    public int getVideoHeight() {
        return 0;
    }

    /**
     * Returns the width of the current media
     *
     * @return the width in pixels
     * @since 1.0
     */
    public int getVideoWidth() {
        return 0;
    }

    /**
     * Checks if the player plugin with the specified playerId is attached to the
     * browsers' DOM
     *
     * <p>This method is provided for the convenience of player implementation classes.
     *
     * @param playerId the object ID of the player plugin
     * @return <code>true</code> if player plugin is attached, <code>false</code>
     * otherwise.
     * @since 1.0
     */
    protected static final native boolean isPlayerOnPage(String playerId) /*-{
    return ($doc.getElementById(playerId) != null);
    }-*/;

    /**
     * Sets the specified player parameter to the specified value IF AND ONLY IF the
     * parameter is applicable on the player
     *
     * <p><b>Note:</b> The parameter-value pairs are applied as HTML param tags
     * on the underlying player plugin, therefore this method should be called
     * before adding this player to a panel otherwise the method call will have
     * no effect.</p>
     *
     * <p><h4>Overriding in a subclass</h4>
     * This method should be called first by any subclass that overrides it. This
     * implementation checks if the specified value is a valid type for the specified
     * parameter.</p>
     *
     * @param param the configuration parameter
     * @param value the parameter value
     * @param <T> the paramter value type
     * @throws IllegalArgumentException if {@code value} is not of the required type
     * @since 1.1
     */
    public <T extends ConfigValue> void setConfigParameter(ConfigParameter param, T value) {
        if ((value != null) && (!Arrays.asList(param.getValueType()).contains(value.getClass()))) {
            throw new IllegalArgumentException("Found ConfigParameter type "
                    + value.getClass() + ", Permissible value types : " + Arrays.asList(param.getValueType()));
        }
    }

    /**
     * Sets the specified player parameter to the specified value IF AND ONLY IF the
     * parameter is applicable on the player
     *
     * <p><h4>Overriding in a subclass</h4>
     * This method should be called first by any subclass that overrides it. This
     * implementation checks if the specified value is a valid type for the specified
     * parameter.</p>
     *
     * @param param the configuration parameter
     * @param value the numeric value
     * @throws IllegalArgumentException if {@code param} is not useable with a number
     * @since 1.2
     * @see #setConfigParameter(ConfigParameter, ConfigValue)
     */
    public void setConfigParameter(ConfigParameter param, Number value) {
        if ((value != null) && (!Arrays.asList(param.getValueType()).contains(value.getClass()))) {
            throw new IllegalArgumentException("Found ConfigParameter type "
                    + value.getClass() + ", Permissible value types : " + Arrays.asList(param.getValueType()));
        }
    }

    /**
     * Sets the specified player parameter to the specified value IF AND ONLY IF the
     * parameter is applicable on the player
     *
     * <p><h4>Overriding in a subclass</h4>
     * This method should be called first by any subclass that overrides it. This
     * implementation checks if the specified value is a valid type for the specified
     * parameter.</p>
     *
     * @param param the configuration parameter
     * @param value the String value
     * @throws IllegalArgumentException if {@code param} is not useable with a String
     * @since 1.2.1
     * @see #setConfigParameter(ConfigParameter, ConfigValue)
     */
    public void setConfigParameter(ConfigParameter param, String value) {
        if ((value != null) && (!Arrays.asList(param.getValueType()).contains(value.getClass()))) {
            throw new IllegalArgumentException("Found ConfigParameter type "
                    + value.getClass() + ", Permissible value types : " + Arrays.asList(param.getValueType()));
        }
    }

    /**
     * Sets the playback rate.
     *
     * <p>If this player is not available on the panel, this method call is added to the
     * command-queue for later execution.
     *
     * @param rate the playback rate. A rate of 1 implies normal playback rate,
     * fractional values are slow motion, and values greater than one are fast-forward
     * @since 1.1
     */
    public void setRate(double rate) {
    }

    /**
     * Returns the current playback rate
     *
     * @return the playback rate
     * @see #setRate(double)
     * @since 1.1
     */
    public double getRate() {
        return 1;
    }

    /**
     * Adds the mouse move handler to the player
     *
     * @param handler the handler
     * @return the HandlerRegistration used to remove the handler
     * @since 1.1
     */
    @Override
    public HandlerRegistration addMouseMoveHandler(MouseMoveHandler handler) {
        return addDomHandler(handler, MouseMoveEvent.getType());
    }

    /**
     * Adds the mouse down handler to the player
     *
     * @param handler the handler
     * @return the HandlerRegistration used to remove the handler
     * @since 1.1
     */
    @Override
    public HandlerRegistration addMouseDownHandler(MouseDownHandler handler) {
        return addDomHandler(handler, MouseDownEvent.getType());
    }

    /**
     * Adds the mouse up handler to the player
     *
     * @param handler the handler
     * @return the HandlerRegistration used to remove the handler
     * @since 1.1
     */
    @Override
    public HandlerRegistration addMouseUpHandler(MouseUpHandler handler) {
        return addDomHandler(handler, MouseUpEvent.getType());
    }

    /**
     * Adds the key down handler to the player
     *
     * @param handler the handler
     * @return the HandlerRegistration used to remove the handler
     * @since 1.1
     */
    @Override
    public HandlerRegistration addKeyDownHandler(KeyDownHandler handler) {
        return addDomHandler(handler, KeyDownEvent.getType());
    }

    /**
     * Adds the key up handler to the player
     *
     * @param handler the handler
     * @return the HandlerRegistration used to remove the handler
     * @since 1.1
     */
    @Override
    public HandlerRegistration addKeyUpHandler(KeyUpHandler handler) {
        return addDomHandler(handler, KeyUpEvent.getType());
    }

    /**
     * Adds the key press handler to the player
     *
     * @param handler the handler
     * @return the HandlerRegistration used to remove the handler
     * @since 1.1
     */
    @Override
    public HandlerRegistration addKeyPressHandler(KeyPressHandler handler) {
        return addDomHandler(handler, KeyPressEvent.getType());
    }

    /**
     * Puts the player in the specified repeat mode.
     *
     * <p>The effect of the specified mode depends on the {@link #setLoopCount(int)} property as
     * shown in the table:</p>
     *
     * <table border="1" cellspacing="0" cellpadding="3">
     * <colgroup>
     * <col width="25%">
     * <col width="25%">
     * <col width="*">
     * </colgroup>
     * <thead>
     * <tr>
     * <th>Value of {@code loopCount}</th>
     * <th>Value of {@code repeatMode}</th>
     * <th>What it means?</th>
     * </tr>
     * </thead>
     * <tbody>
     * <tr>
     * <td>Any value &lt; 0</td>
     * <td>Sets {@code repeatMode} to {@link RepeatMode#REPEAT_ALL}</td>
     * <td>Playlist is repeated continously</td>
     * </tr>
     *
     * <tr>
     * <td rowspan='3'>1</td>
     * <td>{@link RepeatMode#REPEAT_OFF}</td>
     * <td>Entire playlist is played only once</td>
     * </tr>
     * <tr>
     * <td>{@link RepeatMode#REPEAT_ONE}</td>
     * <td>The current playlist entry is repeated continously</td>
     * </tr>
     * <tr>
     * <td>{@link RepeatMode#REPEAT_ALL}</td>
     * <td>Entire playlist is repeated continously</td>
     * </tr>
     *
     * <tr>
     * <td rowspan='3'>Any value &gt; 1</td>
     * <td>{@link RepeatMode#REPEAT_OFF}</td>
     * <td>The {@code loopCount} property is ignored, and the entire playlist is played only once</td>
     * </tr>
     * <tr>
     * <td>{@link RepeatMode#REPEAT_ONE}</td>
     * <td>Player repeats each playlist entry for the number of times specified by {@code loopCount}
     * and moves on the next until all entries are played and stops</td>
     * </tr>
     * <tr>
     * <td>{@link RepeatMode#REPEAT_ALL}</td>
     * <td>Player repeats entire playlist for the number of times specified by {@code loopCount} and stops</td>
     * </tr>
     * </tbody>
     * </table>
     *
     * @param mode the new repeat mode
     * @since 1.2
     * @see RepeatMode
     */
    public void setRepeatMode(RepeatMode mode) {
    }

    /**
     * Returns the current playback repeat mode
     *
     * @return repeat mode
     * @see RepeatMode
     * @since 1.2
     */
    public RepeatMode getRepeatMode() {
        return RepeatMode.REPEAT_OFF;
    }

    /**
     * Returns a reference to the {@linkplain PlayerProviderFactory} implementation with the 
     * specified {@code providerName}
     * 
     * <p>This method is provided for the convenience of player implementation classes.
     * 
     * @param providerName the provider name
     * @return PlayerProviderFactory implementation with the specified {@code providerName} or {@code null} if no such provider exists.
     * @since 1.3
     */
    protected final PlayerProviderFactory getWidgetFactory(String providerName) {
        return PlayerManager.getInstance().getProviderFactory(providerName);
    }
}
