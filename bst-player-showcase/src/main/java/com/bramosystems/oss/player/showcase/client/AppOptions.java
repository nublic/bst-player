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

/**
 *
 * @author Sikiru
 */
public enum AppOptions {
    home("Introduction", "Introduction"),
    plugins("Browser Plug-ins", "All installed plug-ins"),
    mimes("Media File Types", "Player widget mime-type/extension mapping"),
    core("Core Player Widgets", "Core Player Widgets"),
    capsule("Custom Audio Player", "Custom audio player controls"),
    flat("Custom Video Player", "Custom video player control"),
    matrix("Matrix Transforms", "Transforming videos with matrices"),
    ytube("YouTube Videos", "Embedding YouTube videos"),
    notices("Notices", "Notices");

    private AppOptions(String title, String desc) {
        this.title = title;
        this.desc = desc;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return desc;
    }
    
    private String title, desc;
}
