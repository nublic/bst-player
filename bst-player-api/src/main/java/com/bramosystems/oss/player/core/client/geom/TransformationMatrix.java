/*
 * Copyright 2009 Sikirulai Braheem
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a11 copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.bramosystems.oss.player.core.client.geom;

/**
 * Represents a transformation matrix that determines how to map points from one coordinate space
 * to another. The matrix can be used to perform various two-dimensional graphical transformations
 * including translation (x and y repositioning), rotation, scaling, and skewing.
 *
 * <p>A transformation matrix object is a 3x3 matrix with the following contents:
 *
 * <pre>
 *      [ a   b   u ]
 *      [ c   d   v ]
 *      [ tx  ty  w ]
 * </pre>
 *
 * <b>Note:</b> Property values <code>u</code> and <code>v</code> are always 0.0,
 * while <code>w</code> is always 1.0.
 *
 * <p>
 * <b>Note:</b>The 3x3 matrix is implemented with a Matrix2D object.
 *
 * @since 1.1
 * @author Sikirulai Braheem <sbraheem at bramosystems.com>
 * @see Matrix2D
 */
public class TransformationMatrix {

    private Matrix2D matrix;

    /**
     * Constructs a new identity TransformationMatrix
     */
    public TransformationMatrix() {
        matrix = new Matrix2D();
    }

    /**
     * Constructs a TransformationMatrix using the specified matrix.
     *
     * @param matrix the matrix
     */
    public TransformationMatrix(Matrix2D matrix) {
        this.matrix = matrix;
    }

    /**
     * Returns the backing 3x3 matrix
     *
     * @return the backing 3x3 matrix
     */
    public Matrix2D getMatrix() {
        return matrix;
    }

    /**
     * Performs a displacement transformation on this matrix along the x and y axes
     *
     * @param x displacement along the x-axis (in pixels)
     * @param y displacement along the y-axis (in pixels)
     */
    public void translate(double x, double y) {
        Matrix2D d = new Matrix2D();
        d.getVx().setZ(x);
        d.getVy().setZ(y);
        matrix = MatrixUtil.multiply(matrix, d);  // Works fine on both plugins...
//        matrix = MatrixUtil.multiply(d, matrix); // incorrect on Flash Matrix ...
    }

    /**
     * Performs a scalling transformation on this matrix along the x and y axes. The
     * x-axis is multiplied by <code>x</code> and the y-axis is multiplied by <code>y</code>.
     *
     * @param x multiplier used to scale the matrix along the x-axis
     * @param y multiplier used to scale the matrix along the y-axis
     */
    public void scale(double x, double y) {
        Matrix2D d = new Matrix2D();
        d.getVx().setX(x);
        d.getVy().setY(y);
        matrix = MatrixUtil.multiply(d, matrix);  // scales only the dimension
//      matrix = MatrixUtil.multiply(matrix, d);  // scales all parameters including position: DON'T USE!
    }

    /**
     * Applies a rotation transformation to this matrix.
     *
     * @param angle angle of rotation in radians
     */
    public void rotate(double angle) {
        double sin = Math.sin(angle), cos = Math.cos(angle);
        Matrix2D d = new Matrix2D();
        d.getVx().setX(cos);
        d.getVx().setY(sin * -1);
        d.getVy().setX(sin);
        d.getVy().setY(cos);
//        matrix = MatrixUtil.multiply(matrix, d);
        matrix = MatrixUtil.multiply(d, matrix);
    }

/*
 * TODO: Use for TransformationMatrix3D only...
 *
    public void rotateY(double angle) {
        double sin = Math.sin(angle), cos = Math.cos(angle);
        Matrix2D d = new Matrix2D();
        d.getVx().setX(cos);
        d.getVx().setZ(-1 * sin);
        d.getVz().setX(sin);
        d.getVz().setZ(cos);
//        matrix = MatrixUtil.multiply(matrix, d);
        matrix = MatrixUtil.multiply(d, matrix);
    }

    public void rotateX(double angle) {
        double sin = Math.sin(angle), cos = Math.cos(angle);
        Matrix2D d = new Matrix2D();
        d.getVy().setY(cos);
        d.getVy().setZ(sin);
        d.getVz().setY(sin * -1);
        d.getVz().setZ(cos);
//        matrix = MatrixUtil.multiply(matrix, d);
        matrix = MatrixUtil.multiply(d, matrix);
    }
*/
    /**
     * Applies a skewing transformation to this matrix
     *
     * @param ax skew angle along the x-axis (in radians)
     * @param ay skew angle along the y-axis (in radians)
     */
    public void skew(double ax, double ay) {
        Matrix2D d = new Matrix2D();
        d.getVx().setY(Math.tan(ax));
        d.getVy().setX(Math.tan(ay));
//        matrix = MatrixUtil.multiply(matrix, d);
        matrix = MatrixUtil.multiply(d, matrix);
    }

    /**
     * Performs matrix multiplication on this matrix with the specified transformation matrix and
     * keeps the result in this matrix
     *
     * @param m the matrix to multipy with this matrix
     */
    public void multiply(TransformationMatrix m) {
        matrix = MatrixUtil.multiply(this.matrix, m.matrix);
    }

    /**
     * Performs an inversion on this matrix
     */
    public void invert() {
        matrix = MatrixUtil.invert(matrix);
    }

    /**
     * Converts this matrix to an identity matrix
     */
    public void toIdentity() {
        matrix = new Matrix2D();
    }

    /**
     * Returns the matrix elements as a String
     *
     * @return the matrix in the form <code>a, b, u, c, d, v, tx, ty, w</code>
     */
    @Override
    public String toString() {
        return matrix.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TransformationMatrix other = (TransformationMatrix) obj;
        if (this.matrix != other.matrix && (this.matrix == null || !this.matrix.equals(other.matrix))) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + (this.matrix != null ? this.matrix.hashCode() : 0);
        return hash;
    }

}
