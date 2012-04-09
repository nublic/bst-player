/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bramosystems.oss.player.core.client;

import com.bramosystems.oss.player.core.client.ui.FlashMediaPlayer;
import com.bramosystems.oss.player.core.client.ui.NativePlayer;
import com.bramosystems.oss.player.core.client.ui.QuickTimePlayer;
import com.bramosystems.oss.player.core.client.ui.VLCPlayer;
import com.bramosystems.oss.player.core.client.ui.WinMediaPlayer;
import com.google.gwt.junit.client.GWTTestCase;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Sikirulai Braheem <sbraheem at gmail.com>
 */
public class TxtPlayerUtil extends GWTTestCase {

    public TxtPlayerUtil() {
    }

    @BeforeClass
    public static void setUpClass() throws Exception {
    }

    @AfterClass
    public static void tearDownClass() throws Exception {
    }

    @Test
    public void testFormatMediaTime() {
        System.out.println("formatMediaTime");
        assertEquals("00:00", PlayerUtil.formatMediaTime(0));
        assertEquals("1:00:00", PlayerUtil.formatMediaTime(3600000));
    }

    @Test
    public void testGetFlashPlayerVersion() {
        try {
            System.out.println("getFlashPlayerVersion");
            PluginVersion expResult = new PluginVersion(9, 0, 0);
            PluginVersion result = PlayerUtil.getFlashPlayerVersion();
            assertTrue(result.compareTo(expResult) >= 0);
        } catch (PluginNotFoundException ex) {
            System.out.println("Exception : " + ex.getMessage());
        }
    }

    @Test
    public void testGetQuickTimePluginVersion() {
        try {
            System.out.println("getQuickTimePluginVersion");
            PluginVersion expResult = new PluginVersion(7, 2, 1);
            PluginVersion result = PlayerUtil.getQuickTimePluginVersion();
            assertTrue(result.compareTo(expResult) >= 0);
        } catch (PluginNotFoundException ex) {
            System.out.println("Exception : " + ex.getMessage());
        }
    }

    @Test
    public void testGetWindowsMediaPlayerPluginVersion() {
        try {
            System.out.println("getWindowsMediaPlayerPluginVersion");
            PluginVersion expResult = new PluginVersion(11, 0, 0);
            PluginVersion result = PlayerUtil.getWindowsMediaPlayerPluginVersion();
            assertTrue(result.compareTo(expResult) >= 0);
        } catch (PluginNotFoundException ex) {
            System.out.println("Exception : " + ex.getMessage());
        }
    }

    @Test
    public void testExtraction() {
        System.out.println("Extract Extension");
//        assertTrue("Extract Extension: ", "ext".equals(PlayerUtil.extractExt("files/file.ext")));
//        System.out.println("Extract Protocol : " + PlayerUtil.extractProtocol("files/file.ext"));
    }

    @Test
    public void testGetPlayer() {
        AbstractMediaPlayer result;
        try {
            System.out.print("getPlayer: MP3 format - ");
            result = PlayerUtil.getPlayer("folder/foo.mp3", false, "0", "0");
            System.out.println(result.getClass());
            assertTrue((result instanceof QuickTimePlayer)
                    || (result instanceof WinMediaPlayer)
                    || (result instanceof FlashMediaPlayer)
                    || (result instanceof VLCPlayer)
                    || (result instanceof NativePlayer));
        } catch (LoadException ex) {
            System.out.println("Exception : " + ex.getMessage());
        } catch (PluginNotFoundException ex) {
            System.out.println("Exception : " + ex.getMessage());
        } catch (PluginVersionException ex) {
            System.out.println("Exception : " + ex.getMessage());
        }

        try {
            System.out.print("getPlayer: MOV format - ");
            result = PlayerUtil.getPlayer("foo.mov", false, "0", "0");
            System.out.println(result.getClass());
            assertTrue((result instanceof QuickTimePlayer)
                    || (result instanceof VLCPlayer));
        } catch (LoadException ex) {
            System.out.println("Exception : " + ex.getMessage());
        } catch (PluginNotFoundException ex) {
            System.out.println("Exception : " + ex.getMessage());
        } catch (PluginVersionException ex) {
            System.out.println("Exception : " + ex.getMessage());
        }

        try {
            System.out.print("getPlayer: WMA format - ");
            result = PlayerUtil.getPlayer("foo.wma", false, "0", "0");
            System.out.println(result.getClass());
            assertTrue((result instanceof WinMediaPlayer)
                    || (result instanceof VLCPlayer));
        } catch (LoadException ex) {
            System.out.println("Exception : " + ex.getMessage());
        } catch (PluginNotFoundException ex) {
            System.out.println("Exception : " + ex.getMessage());
        } catch (PluginVersionException ex) {
            System.out.println("Exception : " + ex.getMessage());
        }

        try {
            System.out.print("getPlayer: VOB format - ");
            result = PlayerUtil.getPlayer("foo.vob", false, "0", "0");
            System.out.println(result.getClass());
            assertTrue((result instanceof VLCPlayer) || (result instanceof NativePlayer));
        } catch (LoadException ex) {
            System.out.println("Exception : " + ex.getMessage());
        } catch (PluginNotFoundException ex) {
            System.out.println("Exception : " + ex.getMessage());
        } catch (PluginVersionException ex) {
            System.out.println("Exception : " + ex.getMessage());
        }
    }

    @Test
    public void testSuggestPlayer() throws Exception {
        System.out.println("suggestPlayer");

//        assertTrue("FLV: ", PlayerUtil.canHandleMedia(Plugin.FlashPlayer, null, "flv"));
//        assertTrue("QT : ", instance.canHandleMedia(Plugin.QuickTimePlayer, null, "m4a"));
//        assertTrue("WMP: ", instance.canHandleMedia(Plugin.WinMediaPlayer, null, "wma"));
//        assertTrue("VLC: ", instance.canHandleMedia(Plugin.VLCPlayer, null, "vob"));
    }

    @Override
    public String getModuleName() {
        return "com.bramosystems.oss.player.core.Core";
    }
}
