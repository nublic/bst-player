/*
 *  Copyright 2009 Sikiru Braheem <sbraheem at bramosystems . com>.
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

/**
 * Provides classes and interface to export the API as a Javascript library in non-GWT applications
 * such as traditional page-based web applications.  This enables the players to be
 * used as widgets on websites.
 *
 * <h3><a name="export">Exporting the player widgets</a></h3>
 * Exporting widgets requires some simple steps:
 * <ul>
 * <li>inherit the <code>Script</code> module in your application</li>
 * <li>implement the <code>ExportProvider</code> interface</li>
 * <li>add a GWT type-replacement mapping for the ExportProvider implementation</li>
 * <li>use the <code>ExportUtil</code> class to export the widgets when ready</li>
 * </ul>
 *
 * These steps are further explained:
 *
 * <h4>Inherit the Script module</h4>
 * As required by the GWT compiler, the <code>com.bramosystems.oss.player.script.Script</code>
 * module should be inherited to use the class and interface in this package.
 *
 * <pre><code>
 *    &lt;!-- MyGWTApp.gwt.xml --&gt;
 *    &lt;inherits name="com.bramosystems.oss.player.script.Script" /&gt;
 * </code></pre>
 *
 * <h4>Implement the ExportProvider interface</h4>
 * The <code>ExportProvider</code> interface defines methods to retrieve the widgets to be exported as
 * Javascript objects.  Therefore, create a class that implements the interface and return
 * the player widgets of your choice. The following sample shows a basic implementation:
 *
 * <pre><code>
 *    public class MyCoolProvider implements ExportProvider {
 *       private Plugin plugin;
 *
 *       public AbstractMediaPlayer getPlayer(Plugin plugin, String mediaURL, boolean autoplay, String width,
 *                 String height, HashMap&lt;String, String&gt; options) throws LoadException,
 *                 PluginNotFoundException, PluginVersionException {
 *          this.plugin = plugin;
 *          return PlayerUtil.getPlayer(plugin, mediaURL, autoplay, height, width);
 *       }
 *
 *       public Widget getMissingPluginWidget() {
 *          return PlayerUtil.getMissingPluginNotice(plugin);
 *       }
 *
 *       public Widget getMissingPluginVersionWidget() {
 *          return PlayerUtil.getMissingPluginNotice(plugin);
 *       }
 *
 *       public MediaSeekBar getSeekBar(int height, HashMap&lt;String, String&gt; options) {
 *          return new CSSSeekBar(height);
 *       }
 *    }
 * </code></pre>
 *
 *
 * <h4>Add a GWT type-replacement mapping</h4>
 * The <code>ExportProvider</code> implementation is instantiated by the <code>ExportUtil</code> class
 * using deferred binding while exporting the players.  For the process to be a success, a type-replacement
 * mapping is required in your modules' definition.
 *
 * <p>
 * A sample is shown below:
 * </p>
 * <pre><code>
 *   &lt;!-- MyGWTApp.gwt.xml --&gt;
 *   &lt;replace-with class="com.example.MyCoolProvider"&gt;
 *      &lt;when-type-is class="com.bramosystems.oss.player.script.client.ExportProvider"/&gt;
 *   &lt;/replace-with&gt;
 * </code></pre>
 *
 * Of course, other GWT type-replacement conditions could be used as required.
 *
 * <h4>Export the widgets</h4>
 * The <code>ExportUtil</code> class defines static methods to export the player and seekbar widgets.  The
 * class could be used in module entry implementations as shown below:
 *
 * <pre><code>
 *   public class Xporter implements EntryPoint {
 *
 *     public Xporter() {
 *        ExportUtil.exportPlayer();
 *        ExportUtil.exportSeekBar();
 *     }
 *
 *     public void onModuleLoad() {
 *        ExportUtil.signalAPIReady();
 *     }
 *   }
 * </code></pre>
 *
 * The <code>ExportUtil.exportPlayer()</code> method instantiates the <code>ExportProvider</code> implementation
 * and exports the player widget returned by the <code>getPlayer()</code> method.  If the required plugin is
 * not found the <code>getMissingPluginWidget()</code> method is called while the
 * <code>getMissingPluginVersionWidget()</code> is called when the required plugin version is not found.
 *
 * <p>
 * The player widget is exported as a <code>bstplayer.Player(plugin,mediaURL,autoplay,width,height,options)</code>
 * object where:
 * <ul>
 * <li><code>plugin</code>   [String]  - is one of the defined Plugins (Note: case-sensitive)</li>
 * <li><code>mediaURL</code> [String]  - the URL of the media</li>
 * <li><code>autoplay</code> [boolean] - true to autoplay the media, false otherwise</li>
 * <li><code>width</code>    [String]  - the width of the widget (in CSS units)</li>
 * <li><code>height</code>   [String]  - the height of the widget (in CSS units)</li>
 * <li><code>options</code>  [Javascript Object] - used to pass user-defined map of values to the
 * <code>ExportProvider</code> implementation</li>
 * </ul>
 *
 * See <a href="#usage">sample usage</a> below.
 * </p>
 * 
 * <p>
 * Similary, the <code>ExportUtil.exportSeekBar()</code> method instantiates the <code>ExportProvider</code>
 * implementation and exports the seekbar widget returned by the <code>getSeekBar()</code> method.
 * </p>
 *
 * The seekbar widget is exported as a <code>bstplayer.SeekBar(height,containerId,options)</code> object
 * where:
 * <ul>
 * <li><code>height</code> [String] - the height of the widget (in CSS units)</li>
 * <li><code>containerId</code> [String] - the HTML element <code>id</code>, where the widget will be placed</li>
 * <li><code>options</code> [Javascript Object] - used to pass user-defined map of values to the
 * <code>ExportProvider</code> implementation</li>
 * </ul>
 *
 * See <a href="#usage">sample usage</a> below.
 *
 * <p>
 * The <code>ExportUtil.signalAPIReady()</code> method notifies the host page that the Javascript objects are
 * now available and can be created and used as required.  This method calls the 
 * <code>onBSTPlayerReady()</code> Javascript function, which SHOULD be defined on the host page.
 * The <code>Player</code> and/or <code>SeekBar</code> objects SHOULD be created within this function.
 * </p>
 *
 * <h3><a name="usage">Using the exported widgets</a></h3>
 * The exported API should be available as a GWT compilation in its module folder/directory. Using the widgets
 * is thereafter simple:
 *
 * <h4>Add the module script to the host page</h4>
 * GWT applications end up as an <code>module-name.nocache.js</code> javascript file, hence add this to
 * the HTML host page.
 *
 * <pre><code>
 *      &lt;script type="text/javascript" src="module-name/module-name.nocache.js"&gt;&lt;/script&gt;
 * </code></pre>
 *
 * <h4>Create the widgets HTML container elements</h4>
 * Create the HTML elements that will contain the widgets.  This can be as simple as defining HTML
 * &lt;div&gt; tags at required places.
 *
 * <pre><code>
 *      &lt;div id="my-player" /&gt;
 *      &lt;div id="_progress" /&gt;
 * </code></pre>
 *
 * <h4>Define the <code>onBSTPlayerReady()</code> function</h4>
 * This function will be called when the <code>Player</code> and/or <code>SeekBar</code> objects are bound
 * to the host page
 * 
 * <pre><code>
 *      &lt;script type="text/javascript"&gt;
 *          var onBSTPlayerReady = function() {
 *          }
 *      &lt;/script&gt;
 * </code></pre>
 *
 *
 * <h4>Create the widgets and use as required</h4>
 * Create the <code>Player</code> object within the defined <code>onBSTPlayerReady()</code> function.  Once
 * the object is created, the widget can be attached to the page by calling the <code>inject()</code> method.
 * The <code>inject()</code> method takes the <code>id</code> of the HTML element as a parameter.  Following
 * is an example:
 *
 * <pre><code>
 *      &lt;script type="text/javascript"&gt;
 *          var onBSTPlayerReady = function() {
 *              player = new bstplayer.Player("Auto", "some-cool-sound.mp3", false, "100%", "50px", null);
 *              player.inject('my-player');
 *
 *              seekbar = new bstplayer.SeekBar(10, '_progress', null);
 *          }
 *      &lt;/script&gt;
 * </code></pre>
 *
 * The <code>Player</code> object supports all the public methods defined in the
 * <a href="../../core/client/AbstractMediaPlayer.html">AbstractMediaPlayer</a> class, except all the
 * <code>addXXXHandler</code> methods.
 *
 * <p>
 * Instead of the <code>addXXXHandler</code> methods, the <code>Player</code> object defines an
 * <code>addEventListener(name, function)</code> method with the following event names:
 * <ul>
 * <li><code>onPlayerState</code>  - for PlayerStateEvent events</li>
 * <li><code>onPlayState</code> - for PlayStateEvent events</li>
 * <li><code>onLoadingProgress</code> - for LoadingProgressEvent events</li>
 * <li><code>onMediaInfo</code> - for MediaInfoEvent events</li>
 * <li><code>onError</code> - for DebugEvent events of the Error type</li>
 * <li><code>onDebug</code> - for DebugEvent events of the Info type</li>
 * </ul>
 * </p>
 *
 * The <code>SeekBar</code> object supports the following methods:
 * <ul>
 * <li><code>setLoadingProgress(loadingProgress)</code> - set the progress of the media loading operation</li>
 * <li><code>setPlayingProgress(playingProgress)</code> - set the progress of the media playback operation</li>
 * </ul>
 *
 * Also, an <code>addEventListener(name, function)</code> method is defined with the following event name:
 * <ul>
 * <li><code>onSeekChanged</code> - for SeekChangeEvent events</li>
 * </ul>
 *
 * The example below illustrates a custom player implementation:
 * <pre><code>
 *         &lt;script type="text/javascript"&gt;
 *             var player;
 *             var seekbar;
 *             var onBSTPlayerReady = function() {
 *                 seekbar = new bstplayer.SeekBar(10, '_progress', null);
 *                 seekbar.addEventListener("onSeekChanged", function(evt){
 *                     player.setPlayPosition(evt.seekPosition * player.getMediaDuration());
 *                 });
 *
 *                 player = new bstplayer.Player("Auto", "nice.mp3", false, null, null, null);
 *                 player.addEventListener("onPlayState", function(evt){
 *                     switch(evt.playState) {
 *                         case 'Paused':
 *                             document.getElementById("playButton").disabled = false;
 *                             document.getElementById("pauseButton").disabled = true;
 *                             document.getElementById("stopButton").disabled = false;
 *                             break;
 *                         case 'Started':
 *                             document.getElementById("playButton").disabled = true;
 *                             document.getElementById("pauseButton").disabled = false;
 *                             document.getElementById("stopButton").disabled = false;
 *                             break;
 *                         case 'Stopped':
 *                         case 'Finished':
 *                             document.getElementById("playButton").disabled = false;
 *                             document.getElementById("pauseButton").disabled = true;
 *                             document.getElementById("stopButton").disabled = true;
 *                             break;
 *                     }
 *                 });
 *                 player.addEventListener("onPlayerState", function(evt){
 *                     if(evt.playerState == 'Ready') {
 *                         document.getElementById("playButton").disabled = false;
 *                         document.getElementById("pauseButton").disabled = true;
 *                         document.getElementById("stopButton").disabled = true;
 *
 *                         playTimer = window.setInterval(function(){
 *                             seekbar.setPlayingProgress(player.getPlayPosition() / player.getMediaDuration());
 *                         }, 1000);
 *                     }
 *                 });
 *                 player.addEventListener("onLoadingProgress", function(evt){
 *                     seekbar.setLoadingProgress(evt.progress);
 *                 });
 *                 player.inject('_pid');
 *             }
 *         &lt;/script&gt;
 *
 *         &lt;div style="width:350px"&gt;
 *             &lt;div id="_pid"&gt;&lt;/div&gt;
 *             &lt;div&gt;
 *                 &lt;button id="playButton" onclick="player.playMedia()" disabled &gt;Play&lt;/button&gt;
 *                 &lt;button id="pauseButton" onclick="player.pauseMedia()" disabled &gt;Pause&lt;/button&gt;
 *                 &lt;button id="stopButton" onclick="player.stopMedia()" disabled &gt;Stop&lt;/button&gt;
 *             &lt;/div&gt;
 *             &lt;div id="_progress"&gt;&lt;/div&gt;
 *         &lt;/div&gt;
 * </code></pre>
 *
 */
package com.bramosystems.oss.player.script.client;

