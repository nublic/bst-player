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
package com.bramosystems.oss.player.script.client.impl;

import com.bramosystems.oss.player.script.client.ExportUtil;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.Composite;

/**
 * Export the BST Player API
 *
 * @author Sikirulai Braheem <sbraheem at gmail.com>
 */
public class Exporter extends Composite implements EntryPoint {

    public Exporter() {
        ExportUtil.exportPlayer();
        ExportUtil.exportSeekBar();
    }

    @Override
    public void onModuleLoad() {
        ExportUtil.signalAPIReady();
    }
}
