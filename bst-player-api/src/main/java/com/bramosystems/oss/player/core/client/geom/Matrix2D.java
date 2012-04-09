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
 * Implements a 3x3 matrix.  The matrix is represented as an array of Vectors
 * Vx, Vy, Vz.  The matrix notation is shown below:
 * 
 * <pre>
 *         [ VxX  VyX  VzX ]
 *         [ VxY  VyY  VzY ]
 *         [ VxZ  VyZ  VzZ ]
 * </pre>
 *
 * @author Sikiru Braheem
 * @since 1.1
 * @see Vector
 */
public class Matrix2D {

    private Vector vx, vy, vz;

    /**
     * Constructs an identity 3x3 matrix
     */
    public Matrix2D() {
        vx = new Vector(1, 0, 0);
        vy = new Vector(0, 1, 0);
        vz = new Vector(0, 0, 1);
    }

    /**
     * Constructs a 3x3 matrix using the specified vectors
     *
     * @param vx the vector x
     * @param vy the vector y
     * @param vz the vector z
     */
    public Matrix2D(Vector vx, Vector vy, Vector vz) {
        this.vx = vx;
        this.vy = vy;
        this.vz = vz;
    }

    /**
     * Converts this matrix to an identity matrix
     *
     * @return the identity matrix
     */
    public Matrix2D toIdentity() {
        vx = new Vector(1, 0, 0);
        vy = new Vector(0, 1, 0);
        vz = new Vector(0, 0, 1);
        return this;
    }

    /**
     * Returns the vector Vx
     *
     * @return the vector Vx
     */
    public Vector getVx() {
        return vx;
    }

    /**
     * Sets the vector Vx
     *
     * @param vx the vector Vx
     */
    public void setVx(Vector vx) {
        this.vx = vx;
    }

    /**
     * Returns the vector Vy
     *
     * @return the vector Vy
     */
    public Vector getVy() {
        return vy;
    }

    /**
     * Sets the vector Vy
     *
     * @param vy the vector Vy
     */
    public void setVy(Vector vy) {
        this.vy = vy;
    }

    /**
     * Returns the vector Vz
     *
     * @return the vector Vz
     */
    public Vector getVz() {
        return vz;
    }

    /**
     * Sets the vector Vz
     *
     * @param vz the vector Vz
     */
    public void setVz(Vector vz) {
        this.vz = vz;
    }

    /**
     * Tests the specified object {@code obj} for equality with this matrix
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Matrix2D other = (Matrix2D) obj;
        if (this.vx != other.vx && (this.vx == null || !this.vx.equals(other.vx))) {
            return false;
        }
        if (this.vy != other.vy && (this.vy == null || !this.vy.equals(other.vy))) {
            return false;
        }
        if (this.vz != other.vz && (this.vz == null || !this.vz.equals(other.vz))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + (this.vx != null ? this.vx.hashCode() : 0);
        hash = 47 * hash + (this.vy != null ? this.vy.hashCode() : 0);
        hash = 47 * hash + (this.vz != null ? this.vz.hashCode() : 0);
        return hash;
    }

    /**
     * Returns the matrix elements as a String
     *
     * @return the matrix in the form <code>VxX, VyX, VzX, VxY, VyY, VzY, VxZ, VyZ, VzZ</code>
     */
    @Override
    public String toString() {
        return vx.getX() + ", " + vy.getX() + ", " + vz.getX() + ", " +
                vx.getY() + ", " + vy.getY() + ", " + vz.getY() + ", "
                + vx.getZ() + ", " + vy.getZ() + ", " + vz.getZ();
    }
}
