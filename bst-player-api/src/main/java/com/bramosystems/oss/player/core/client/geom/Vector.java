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
 * Represents a location or point in three-dimensional space using the Cartesian coordinates x, y, and z.
 * The x property represents the horizontal axis, the y property represents the vertical axis while
 * the z property represents depth.
 *
 * The matrix notation is shown below:
 * <pre>
 *        [ x ]
 *        [ y ]
 *        [ z ]
 * </pre>
 *
 * @author Sikiru Braheem <sbraheem at bramosystems . com>
 * @since 1.1
 */
public class Vector {

    private double x, y, z;

    /**
     * Constructs a Vector object
     */
    public Vector() {
    }

    /**
     * Constructs a Vector object with values (x,y,z)
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     */
    public Vector(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Returns the x coordinate of this vector
     *
     * @return the x coordinate
     */
    public double getX() {
        return x;
    }

    /**
     * Sets the x coordinate of this vector
     *
     * @param x the x coordinate
     */
    public void setX(double x) {
        this.x = x;
    }

    /**
     * Returns the y coordinate of this vector
     *
     * @return the y coordinate
     */
    public double getY() {
        return y;
    }

    /**
     * Sets the y coordinate of this vector
     *
     * @param y the y coordinate
     */
    public void setY(double y) {
        this.y = y;
    }

    /**
     * Returns the z coordinate of this vector
     *
     * @return the z coordinate
     */
    public double getZ() {
        return z;
    }

    /**
     * Sets the z coordinate of this vector
     *
     * @param z the z coordinate
     */
    public void setZ(double z) {
        this.z = z;
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
        final Vector other = (Vector) obj;
        if (this.x != other.x) {
            return false;
        }
        if (this.y != other.y) {
            return false;
        }
        if (this.z != other.z) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        long hash = 3;
        hash = 11 * hash + ((long) x ^ (long) x >>> 32);
        hash = 11 * hash + ((long) y ^ (long) y >>> 32);
        hash = 11 * hash + ((long) z ^ (long) z >>> 32);
        return (int) hash;
    }

    /**
     * Returns the vector elements as a String
     *
     * @return the vector in the form <code>x, y, z</code>
     */
    @Override
    public String toString() {
        return x + ", " + y + ", " + z;
    }
}
