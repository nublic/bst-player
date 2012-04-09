package com.bramosystems.oss.player.capsule.client.skin;

import com.bramosystems.oss.player.capsule.client.Capsule;
import com.bramosystems.oss.player.capsule.client.Capsule.CapsuleUIResource;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/**
 * Sample UI resource pack for {@link Capsule} widget
 *
 * @since 1.2
 * @author Berger Max
 */
public interface CapsuleResourcePack extends ClientBundle {

    @Source("capsule-ui-style.css")
    public CapsuleUIResource uiResource();

    public ImageResource pause();

    public ImageResource pauseHover();

    public ImageResource play();

    public ImageResource playHover();

    public ImageResource playDisabled();

    public ImageResource stop();

    public ImageResource stopDisabled();

    public ImageResource stopHover();

    public ImageResource volume();

    public ImageResource prev();

    public ImageResource prevDisabled();

    public ImageResource prevHover();

    public ImageResource next();

    public ImageResource nextDisabled();

    public ImageResource nextHover();

    public ImageResource lEdge();

    public ImageResource rEdge();
}
