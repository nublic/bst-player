package com.bramosystems.oss.player.core.client.skin.flat;

import com.bramosystems.oss.player.core.client.skin.CustomPlayerControl;
import com.bramosystems.oss.player.core.client.skin.CustomPlayerControl.UIStyleResource;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

/**
 * Simple ImageBundle definition for the {@link CustomPlayerControl} widget.
 *
 * @since 1.2
 * @author Berger Max
 */
public interface FlatImagePack extends ClientBundle {

    @Source("flat-control-style.css")
    public UIStyleResource css();

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
}
