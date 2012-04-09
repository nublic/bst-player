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

import com.google.gwt.animation.client.Animation;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.TabPanel;

/**
 *
 * @author Sikiru
 */
public class ControlPane extends Composite {

    private final int MAX_HEIGHT = 150, ANIMATION_TIME = 500;
    private boolean fullState = true;
    private Animation animator;
    private TabPanel tp;
    private int selectedTab = 1;
    private PluginPane pluginPane;
    private PlayerOptionsPane optionPane;
    private PlaylistPane playlistPane;
    private PlayerLogPane playerLog;

    public ControlPane() {
        animator = new Animation() {

            @Override
            protected void onUpdate(double progress) {
                onAnimatorUpdate(progress);
            }
        };

        pluginPane = new PluginPane();
        optionPane = PlayerOptionsPane.singleton;
        playlistPane = PlaylistPane.singleton;
        playerLog = new PlayerLogPane();

        History.addValueChangeHandler(pluginPane);
        History.addValueChangeHandler(playlistPane);

        tp = new TabPanel();
        tp.add(pluginPane, "Plug-in");
        tp.add(playlistPane, "Playlist");
        tp.add(optionPane, "Player Options");
        tp.add(playerLog, "Player Log");
        tp.addBeforeSelectionHandler(new BeforeSelectionHandler<Integer>() {

            @Override
            public void onBeforeSelection(BeforeSelectionEvent<Integer> event) {
                if ((event.getItem() == selectedTab) || !fullState) {
                    toggle();
                }
                selectedTab = event.getItem();
            }
        });
        initWidget(tp);
    }

    public PlayerLogPane getPlayerLogPane() {
        return playerLog;
    }

    public void addChangeHandlers(StageController handler) {
        pluginPane.addChangeHandler(handler);
        optionPane.addChangeHandler(handler);
        playlistPane.addChangeHandler(handler);
    }

    @Override
    protected void onLoad() {
        int tab = tp.getTabBar().getOffsetHeight();
        for (int i = 0; i < tp.getWidgetCount(); i++) {
            tp.getWidget(i).setHeight((MAX_HEIGHT - tab) + "px");
        }
        toggle();
    }
    
    private void toggle() {
        fullState = !fullState;
        tp.getDeckPanel().setVisible(false);
        animator.run(ANIMATION_TIME);
    }

    private void onAnimatorUpdate(double progress) {
        double pos = fullState ? progress * MAX_HEIGHT : (1 - progress) * MAX_HEIGHT;
        tp.setHeight((int) pos + "px");
        tp.getDeckPanel().setVisible(fullState && (progress == 1.0));
    }
}
