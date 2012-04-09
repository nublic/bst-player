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
 * Implements a 4x4 matrix.  The matrix is represented as an array of Vector3Ds
 * Vx, Vy, Vz, Vw.  The matrix notation is shown below:
 *
 * <pre>
 *         [ VxX  VyX  VzX  VwX ]
 *         [ VxY  VyY  VzY  VwY ]
 *         [ VxZ  VyZ  VzZ  VwZ ]
 *         [ VxW  VyW  VzW  VwW ]
 * </pre>
 *
 * @author Sikiru Braheem <sbraheem at bramosystems . com>
 * @since 1.1
 * @see Vector3D
 */
public class Matrix3D {

    private Vector3D vx, vy, vz, vw;

    /**
     * Constructs an identity 4x4 matrix
     */
    public Matrix3D() {
        vx = new Vector3D(1, 0, 0, 0);
        vy = new Vector3D(0, 1, 0, 0);
        vz = new Vector3D(0, 0, 1, 0);
        vw = new Vector3D(0, 0, 0, 1);
    }

    /**
     * Constructs a 4x4 matrix using the specified vectors
     *
     * @param vx the vector x
     * @param vy the vector y
     * @param vz the vector z
     * @param vw the vector w
     */
    public Matrix3D(Vector3D vx, Vector3D vy, Vector3D vz, Vector3D vw) {
        this.vx = vx;
        this.vy = vy;
        this.vz = vz;
        this.vw = vw;
    }

    /**
     * Converts this matrix to an identity matrix
     *
     * @return the identity matrix
     */
    public Matrix3D toIdentity() {
        vx = new Vector3D(1, 0, 0, 0);
        vy = new Vector3D(0, 1, 0, 0);
        vz = new Vector3D(0, 0, 1, 0);
        vw = new Vector3D(0, 0, 0, 1);
        return this;
    }

    /**
     * Returns the vector Vw
     *
     * @return the vector Vw
     */
    public Vector3D getVw() {
        return vw;
    }

    /**
     * Sets the vector Vw
     *
     * @param vw the vector Vw
     */
    public void setVw(Vector3D vw) {
        this.vw = vw;
    }

    /**
     * Returns the vector Vx
     *
     * @return the vector Vx
     */
    public Vector3D getVx() {
        return vx;
    }

    /**
     * Sets the vector Vx
     *
     * @param vx the vector Vx
     */
    public void setVx(Vector3D vx) {
        this.vx = vx;
    }

    /**
     * Returns the vector Vy
     *
     * @return the vector Vy
     */
    public Vector3D getVy() {
        return vy;
    }

    /**
     * Sets the vector Vy
     *
     * @param vy the vector Vy
     */
    public void setVy(Vector3D vy) {
        this.vy = vy;
    }

    /**
     * Returns the vector Vz
     *
     * @return the vector Vz
     */
    public Vector3D getVz() {
        return vz;
    }

    /**
     * Sets the vector Vz
     *
     * @param vz the vector Vz
     */
    public void setVz(Vector3D vz) {
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
        final Matrix3D other = (Matrix3D) obj;
        if (this.vx != other.vx && (this.vx == null || !this.vx.equals(other.vx))) {
            return false;
        }
        if (this.vy != other.vy && (this.vy == null || !this.vy.equals(other.vy))) {
            return false;
        }
        if (this.vz != other.vz && (this.vz == null || !this.vz.equals(other.vz))) {
            return false;
        }
        if (this.vw != other.vw && (this.vw == null || !this.vw.equals(other.vw))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + (this.vx != null ? this.vx.hashCode() : 0);
        hash = 47 * hash + (this.vy != null ? this.vy.hashCode() : 0);
        hash = 47 * hash + (this.vz != null ? this.vz.hashCode() : 0);
        hash = 47 * hash + (this.vw != null ? this.vw.hashCode() : 0);
        return hash;
    }

    /**
     * Returns the matrix elements as a String
     *
     * @return the matrix in the form <code>VxX, VyX, VzX, VwX, VxY, VyY, VzY, VwY,
     * VxZ, VyZ, VzZ, VwZ, VxW, VyW, VzW, VwW</code>
     */
    @Override
    public String toString() {
        return vx.getX() + ", " + vy.getX() + ", " + vz.getX() + ", " + vw.getX() + ", "
                + vx.getY() + ", " + vy.getY() + ", " + vz.getY() + ", " + vw.getY() + ", "
                + vx.getZ() + ", " + vy.getZ() + ", " + vz.getZ() + ", " + vw.getZ() + ", "
                + vx.getW() + ", " + vy.getW() + ", " + vz.getW() + ", " + vw.getW();
    }
}
