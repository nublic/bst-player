/*
 *  Copyright 2009 Sikiru.
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
 * Represents a location or point in three-dimensional space similar to a {@link Vector}, but in
 * addition, consists of a fourth property {@code w} which can be used to represent the angle of rotation.
 *
 * The matrix notation is shown below:
 * <pre>
 *        [ x ]
 *        [ y ]
 *        [ z ]
 *        [ w ]
 * </pre>
 *
 * @author Sikiru Braheem <sbraheem at bramosystems . com>
 * @since 1.1
 */
public class Vector3D extends Vector {

    private double w;

    /**
     * Constructs a Vector3D object
     */
    public Vector3D() {
    }

    /**
     * Constructs a Vector3D object with values (x,y,z,w)
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     * @param w the w coordinate
     */
    public Vector3D(double x, double y, double z, double w) {
        super(x, y, z);
        this.w = w;
    }

    /**
     * Returns the w coordinate of this vector
     *
     * @return the w coordinate
     */
    public double getW() {
        return w;
    }

    /**
     * Sets the w coordinate of this vector
     *
     * @param w the w coordinate
     */
    public void setW(double w) {
        this.w = w;
    }

    /**
     * Tests the specified object {@code obj} for equality with this vector
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Vector3D other = (Vector3D) obj;
        if (this.w != other.w) {
            return false;
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        long hash = 3;
        hash = 59 * hash + super.hashCode();
        hash = 59 * hash + ((long) w ^ (long) w >>> 32);
        return (int) hash;
    }

    /**
     * Returns the vector elements as a String
     *
     * @return the vector in the form <code>x, y, z, w</code>
     */
    @Override
    public String toString() {
        return super.toString() + ", " + w;
    }
}
