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

package com.bramosystems.oss.player.core.client.geom;

/**
 * Interface for players that have two-dimensional transformation matrix support.
 *
 * <p>The transformation matrix allows graphical operations like translation, scaling,
 * rotation and skewing.
 *
 * @see TransformationMatrix
 * @author Sikirulai Braheem <sbraheem at bramosystems dot com>
 * @since 1.1
 */
public interface MatrixSupport {

    /**
     * Sets the transformation matrix on the player
     *
     * @param matrix the transformation matrix
     */
    public void setMatrix(TransformationMatrix matrix);

    /**
     * Retrieves the current transformation matrix of the player.
     *
     * <p><b>Note:</b> Changing the properties of the returned object has no
     * effect on the transformation matrix of the underlying plugin.  You will
     * have to call {@link #setMatrix(TransformationMatrix)} to effect any change.
     *
     * @return the current transformation matrix
     */
    public TransformationMatrix getMatrix();

}
