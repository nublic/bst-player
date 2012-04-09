/*
 *  Copyright 2009 Sikirulai Braheem <sbraheem at bramosystems dot com>.
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

package com.bramosystems.oss.player.core.client;

import com.bramosystems.oss.player.core.client.geom.TransformationMatrix;
import com.google.gwt.junit.client.GWTTestCase;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Sikirulai Braheem <sbraheem at bramosystems dot com>
 */
public class TxtTransformationMatrix extends GWTTestCase {

    public TxtTransformationMatrix() {
    }

    @Test
    public void testTranslate() {
        System.out.println("translate");
        double x = 2.0;
        double y = 2.0;
        TransformationMatrix instance = new TransformationMatrix();
        instance.translate(x, y);

        TransformationMatrix instance2 = new TransformationMatrix();
        instance2.getMatrix().getVx().setZ(x);
        instance2.getMatrix().getVy().setZ(y);

        assertEquals(instance, instance2);
    }

    @Test
    public void testScale() {
        System.out.println("scale");
        double x = 4.0;
        double y = 4.0;
        TransformationMatrix instance = new TransformationMatrix();
        instance.scale(x, y);

        TransformationMatrix instance2 = new TransformationMatrix();
        instance2.getMatrix().getVx().setX(x);
        instance2.getMatrix().getVy().setY(y);

        assertEquals(instance, instance2);
    }

    @Test
    public void testRotate() {
        System.out.println("rotate");
        double angle = 6.0;
        TransformationMatrix instance = new TransformationMatrix();
        instance.rotate(angle);

        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        TransformationMatrix instance2 = new TransformationMatrix();
        instance2.getMatrix().getVx().setX(cos);
        instance2.getMatrix().getVy().setX(sin);
        instance2.getMatrix().getVx().setY(-1 * sin);
        instance2.getMatrix().getVy().setY(cos);
        assertEquals(instance, instance2);
    }

    @Test
    public void testSkew() {
        System.out.println("skew");
        double ax = 2.0;
        double ay = 3.0;
        TransformationMatrix instance = new TransformationMatrix();
        instance.skew(ax, ay);

        TransformationMatrix instance2 = new TransformationMatrix();
        instance2.getMatrix().getVy().setX(Math.tan(ay));
        instance2.getMatrix().getVx().setY(Math.tan(ax));
        assertEquals(instance, instance2);
    }

    @Test
    public void testScaleAndTranslate() {
        System.out.println("scaleAndTranslate");
        double x = 2.0;
        double y = 3.0;
        TransformationMatrix instance = new TransformationMatrix();
        instance.scale(x, y);
        instance.translate(x, y);

        TransformationMatrix instance2 = new TransformationMatrix();
        instance2.getMatrix().getVx().setZ(x);
        instance2.getMatrix().getVy().setZ(y);
        instance2.getMatrix().getVx().setX(x);
        instance2.getMatrix().getVy().setY(y);

        assertEquals(instance2, instance);
    }

    @Test
    public void testTranslateAndScale() {
        System.out.println("translateAndScale");
        double x = 2.0;
        double y = 3.0;
        TransformationMatrix instance = new TransformationMatrix();
        instance.translate(x, y);
        instance.scale(x, y);

        TransformationMatrix instance2 = new TransformationMatrix();
        instance2.getMatrix().getVx().setZ(x);
        instance2.getMatrix().getVy().setZ(y);
        instance2.getMatrix().getVx().setX(x);
        instance2.getMatrix().getVy().setY(y);

        assertEquals(instance2, instance);
    }

    @Override
    public String getModuleName() {
        return "com.bramosystems.oss.player.core.Core";
    }

}