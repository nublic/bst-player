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
package com.bramosystems.oss.player.core;

import com.bramosystems.oss.player.core.client.TxtPlayTime;
import com.bramosystems.oss.player.core.client.TxtPlayerUtil;
import com.bramosystems.oss.player.core.client.TxtTransformationMatrix;
import com.google.gwt.junit.tools.GWTTestSuite;
import junit.framework.Test;

/**
 *
 * @author Sikirulai Braheem <sbraheem at bramosystems dot com>
 */
public class GwtCoreTestSuite extends GWTTestSuite {

    public static Test suite() {
        GWTTestSuite suite = new GWTTestSuite("Test Core Module");
        suite.addTestSuite(TxtPlayerUtil.class);
        suite.addTestSuite(TxtPlayTime.class);
        suite.addTestSuite(TxtTransformationMatrix.class);
        return suite;
    }
}