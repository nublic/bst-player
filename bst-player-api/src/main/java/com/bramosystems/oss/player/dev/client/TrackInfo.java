/*
 *  Copyright 2010 Administrator.
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

package com.bramosystems.oss.player.dev.client;

/**
 *
 * @author Administrator
 */
public class TrackInfo {
    public static enum Type {
        Subtitles, Captions, Descriptions, Chapters, Metadata
    }

    private Type kind;
    private String src, label, srcLanguage, srcCharSet;
}
