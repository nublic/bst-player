/*
 * Copyright 2011 sikiru.
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
 *
 * TODO: 2.x - Player module auto-injects module to bst-player based project ...
@PlayerModule("Core.gwt.xml")
@GWTModule(name = "Dev2", rename = "dev2", stylesheet = "customs.css",
inherits = {"com.bramosystems.oss.player.core.Core", "com.bramosystems.oss.player.playlist.Playlist",
    "com.bramosystems.oss.player.uibinder.UiBinder"},
entryPoint = Dev.class,
definedProperties = {
    @MultiValuedProperty(name = "bstplayer.enableLogger", values = "true,false")
},
configurationProperties = {
    @ConfigurationProperty(name = "bstplayer.media.mimeTypes", isMultiValued = false)
},
applyProperties = {
    @AppliedProperty(name = "bstplayer.enableLogger", value = "true")
},
sourcePath = {@Path(value = "clientz")},
publicPath = {@Path(value = "publiz")},
definedLinkers = {@Linker(implementationClass = "test.com", name = "test")},
linkers = {"test", "xo"},
collapsedProperties = {
    @Property(name = "test0", value = "lovely"),
    @Property(name = "test1", value = "lovely"),
    @Property(name = "test2", value = "lovely")
})

*/
package com.bramosystems.oss.player.dev;
