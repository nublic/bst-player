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
package com.bramosystems.oss.player.showcase.client;

import com.bramosystems.oss.player.core.client.*;
import com.bramosystems.oss.player.core.client.geom.MatrixSupport;
import com.bramosystems.oss.player.core.client.geom.TransformationMatrix;
import com.bramosystems.oss.player.core.client.ui.QuickTimePlayer.Scale;
import com.bramosystems.oss.player.core.event.client.*;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;


/**
 *
 * @author Sikirulai Braheem <sbraheem at bramosystems dot com>
 */
public class MatrixStagePane extends Composite implements HasMediaMessageHandlers {

    private AbstractMediaPlayer player;
    private TransformationMatrix matrixCache;

    public MatrixStagePane(Plugin plugin, String width, String height) throws
            PluginNotFoundException, PluginVersionException, LoadException {
        initWidget(mBinder.createAndBindUi(this));

        String url = null;
        switch (plugin) {
            case QuickTimePlayer:
                url = PlaylistPane.baseURL + "big-buck-bunny.mp4";
                break;
            case FlashPlayer:
                url = PlaylistPane.baseURL + "traffic.flv";
                break;
            default:
                plugin = Plugin.MatrixSupport;
                url = PlaylistPane.baseURL + "traffic.flv";
                break;
        }

        player = PlayerUtil.getPlayer(plugin, url, false, height, width);
        player.setConfigParameter(ConfigParameter.QTScale, Scale.Aspect);
        player.addMediaInfoHandler(new MediaInfoHandler() {

            @Override
            public void onMediaInfoAvailable(MediaInfoEvent event) {
                matrixCache = ((MatrixSupport) player).getMatrix();
            }
        });
        player.addDebugHandler(new DebugHandler() {

            @Override
            public void onDebug(DebugEvent event) {
                fireEvent(event);
            }
        });
        video.setWidget(player);

        fillListBoxes(_Option.Rotate);
        fillListBoxes(_Option.Scale);
        fillListBoxes(_Option.Skew);
        fillListBoxes(_Option.Translate);
    }

    @Override
    public HandlerRegistration addDebugHandler(DebugHandler handler) {
        return addHandler(handler, DebugEvent.TYPE);
    }

    @Override
    public HandlerRegistration addMediaInfoHandler(MediaInfoHandler handler) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private void fillListBoxes(final _Option option) {
        ListBox box = null;

        switch (option) {
            case Scale:
                box = scale;
                box.addItem("0.5x", "0.5");
                box.addItem("1.0x", "1.0");
                box.addItem("1.5x", "1.5");
                box.addItem("2.0x", "2.0");
                box.setSelectedIndex(1);
                break;
            case Rotate:
                box = rotate;
                box.addItem("-90 deg", "-90");
                box.addItem("-45 deg", "-45");
                box.addItem("-30 deg", "-30");
                box.addItem("-15 deg", "-15");
                box.addItem("-10 deg", "-10");
                box.addItem("0 deg", "0");
                box.addItem("10 deg", "10");
                box.addItem("15 deg", "15");
                box.addItem("30 deg", "30");
                box.addItem("45 deg", "45");
                box.addItem("90 deg", "90");
                box.setSelectedIndex(5);
                break;
            case Skew:
                box = skew;
                box.addItem("-10 deg", "-10");
                box.addItem("-5 deg", "-5");
                box.addItem("0 deg", "0");
                box.addItem("5 deg", "5");
                box.addItem("10 deg", "10");
//                box.addItem("15 deg", "15");
//                box.addItem("25 deg", "25");
//                box.addItem("30 deg", "30");
//                box.addItem("45 deg", "45");
                box.setSelectedIndex(2);
                break;
            case Translate:
                box = xlate;
                box.addItem("-50, -50", "-50");
                box.addItem("-30, -30", "-30");
                box.addItem("-20, -20", "-20");
                box.addItem("0, 0", "0");
                box.addItem("20, 20", "20");
                box.addItem("30, 30", "30");
                box.addItem("50, 50", "50");
                box.setSelectedIndex(3);
                break;
        }
        box.addChangeHandler(new ChangeHandler() {

            @Override
            public void onChange(ChangeEvent event) {
                ListBox b = (ListBox)event.getSource();
                doTransform(option, Double.parseDouble(b.getValue(b.getSelectedIndex())));
            }
        });
    }

    private void doTransform(_Option option, double value) {
        TransformationMatrix matrix = ((MatrixSupport) player).getMatrix();

        switch (option) {
            case Rotate:
                matrix.rotate(Math.toRadians(value));
                break;
            case Scale:
                matrix.scale(value, value);
                break;
            case Skew:
                matrix.skew(Math.toRadians(value),
                        Math.toRadians(value));
                break;
            case Translate:
                matrix.translate(value, value);
        }
        ((MatrixSupport) player).setMatrix(matrix);
    }

    @UiHandler("xforms")
    void onClearTransforms(ClickEvent event) {
        ((MatrixSupport) player).setMatrix(matrixCache);
    }

    private enum _Option {

        Scale, Translate, Rotate, Skew;
    }

    @UiField ListBox xlate, rotate, skew, scale;
    @UiField Button xforms;
    @UiField SimplePanel video;

    MatrixBinder mBinder = GWT.create(MatrixBinder.class);
    
    @UiTemplate("xml/MatrixStage.ui.xml")
    interface MatrixBinder extends UiBinder<Widget, MatrixStagePane>{}
}
