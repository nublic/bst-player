/*  Copyright 2009 Sikiru Braheem.
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
 * Utility class to perform various matrix calculations
 *
 * @author Sikiru Braheem <sbraheem at bramosystems . com>
 */
public class MatrixUtil {

    /**
     * Multiplies matrix {@code m} by matrix {@code n}.  The returned value
     * is calculated as:
     * <pre>
     *             x = M x N
     * </pre>
     *
     * @param m the first matrix
     * @param n the second matrix
     * @return the resulting vector
     */
    public static Matrix3D multiply(Matrix3D m, Matrix3D n) {
        /*    ---->
         *  |vxx vyx vzx vwx|   | |vxx vyx vzx vwx|
         *  |vxy vyy vzy vwy| x | |vxy vyy vzy vwy|
         *  |vxz vyz vzz vwz|   | |vxz vyz vzz vwz|
         *  |vxw vyw vzw vww|   | |vxw vyw vzw vww|
         *                      V
         */
        Matrix3D temp = new Matrix3D();
        temp.getVw().setX((m.getVx().getX() * n.getVw().getX()) + (m.getVy().getX() * n.getVw().getY()) + (m.getVz().getX() * n.getVw().getZ())
                + (m.getVw().getX() * n.getVw().getW()));
        temp.getVw().setY((m.getVx().getY() * n.getVw().getX()) + (m.getVy().getY() * n.getVw().getY()) + (m.getVz().getY() * n.getVw().getZ())
                + (m.getVw().getY() * n.getVw().getW()));
        temp.getVw().setZ((m.getVx().getZ() * n.getVw().getX()) + (m.getVy().getZ() * n.getVw().getY()) + (m.getVz().getZ() * n.getVw().getZ())
                + (m.getVw().getZ() * n.getVw().getW()));
        temp.getVw().setW((m.getVx().getW() * n.getVw().getX()) + (m.getVy().getW() * n.getVw().getY()) + (m.getVz().getW() * n.getVw().getZ())
                + (m.getVw().getW() * n.getVw().getW()));

        temp.getVz().setX((m.getVx().getX() * n.getVz().getX()) + (m.getVy().getX() * n.getVz().getY()) + (m.getVz().getX() * n.getVz().getZ())
                + (m.getVw().getX() * n.getVz().getW()));
        temp.getVz().setY((m.getVx().getY() * n.getVz().getX()) + (m.getVy().getY() * n.getVz().getY()) + (m.getVz().getY() * n.getVz().getZ())
                + (m.getVw().getY() * n.getVz().getW()));
        temp.getVz().setZ((m.getVx().getZ() * n.getVz().getX()) + (m.getVy().getZ() * n.getVz().getY()) + (m.getVz().getZ() * n.getVz().getZ())
                + (m.getVw().getZ() * n.getVz().getW()));
        temp.getVz().setW((m.getVx().getW() * n.getVz().getX()) + (m.getVy().getW() * n.getVz().getY()) + (m.getVz().getW() * n.getVz().getZ())
                + (m.getVw().getW() * n.getVz().getW()));

        temp.getVy().setX((m.getVx().getX() * n.getVy().getX()) + (m.getVy().getX() * n.getVy().getY()) + (m.getVz().getX() * n.getVy().getZ())
                + (m.getVw().getX() * n.getVy().getW()));
        temp.getVy().setY((m.getVx().getY() * n.getVy().getX()) + (m.getVy().getY() * n.getVy().getY()) + (m.getVz().getY() * n.getVy().getZ())
                + (m.getVw().getY() * n.getVy().getW()));
        temp.getVy().setZ((m.getVx().getZ() * n.getVy().getX()) + (m.getVy().getZ() * n.getVy().getY()) + (m.getVz().getZ() * n.getVy().getZ())
                + (m.getVw().getZ() * n.getVy().getW()));
        temp.getVy().setW((m.getVx().getW() * n.getVy().getX()) + (m.getVy().getW() * n.getVy().getY()) + (m.getVz().getW() * n.getVy().getZ())
                + (m.getVw().getW() * n.getVy().getW()));

        temp.getVx().setX((m.getVx().getX() * n.getVx().getX()) + (m.getVy().getX() * n.getVx().getY()) + (m.getVz().getX() * n.getVx().getZ())
                + (m.getVw().getX() * n.getVx().getW()));
        temp.getVx().setY((m.getVx().getY() * n.getVx().getX()) + (m.getVy().getY() * n.getVx().getY()) + (m.getVz().getY() * n.getVx().getZ())
                + (m.getVw().getY() * n.getVx().getW()));
        temp.getVx().setZ((m.getVx().getZ() * n.getVx().getX()) + (m.getVy().getZ() * n.getVx().getY()) + (m.getVz().getZ() * n.getVx().getZ())
                + (m.getVw().getZ() * n.getVx().getW()));
        temp.getVx().setW((m.getVx().getW() * n.getVx().getX()) + (m.getVy().getW() * n.getVx().getY()) + (m.getVz().getW() * n.getVx().getZ())
                + (m.getVw().getW() * n.getVx().getW()));
        return temp;
    }

    /**
     * Multiplies matrix {@code m} by vector {@code n}.  The returned value
     * is calculated as:
     * <pre>
     *             x = M x N
     * </pre>
     *
     * @param m the matrix
     * @param n the vector
     * @return the resulting vector
     */
    public static Vector3D multiply(Matrix3D m, Vector3D n) {
        /*    ---->
         *  |vxx vyx vzx vwx|   | |x|
         *  |vxy vyy vzy vwy| x | |y|
         *  |vxz vyz vzz vwz|   | |z|
         *  |vxw vyw vzw vww|   | |w|
         *                      V
         */
        Vector3D temp = new Vector3D();
        temp.setX((m.getVx().getX() * n.getX()) + (m.getVy().getX() * n.getY()) + (m.getVz().getX() * n.getZ())
                + (m.getVw().getX() * n.getW()));
        temp.setY((m.getVx().getY() * n.getX()) + (m.getVy().getY() * n.getY()) + (m.getVz().getY() * n.getZ())
                + (m.getVw().getY() * n.getW()));
        temp.setZ((m.getVx().getZ() * n.getX()) + (m.getVy().getZ() * n.getY()) + (m.getVz().getZ() * n.getZ())
                + (m.getVw().getZ() * n.getW()));
        temp.setW((m.getVx().getW() * n.getX()) + (m.getVy().getW() * n.getY()) + (m.getVz().getW() * n.getZ())
                + (m.getVw().getW() * n.getW()));
        return temp;
    }

    /**
     * Multiplies matrix {@code m} by matrix {@code n}.  The returned value
     * is calculated as:
     * <pre>
     *             x = M x N
     * </pre>
     *
     * @param m the first matrix
     * @param n the second matrix
     * @return the resulting matrix
     */
    public static Matrix2D multiply(Matrix2D m, Matrix2D n) {
        /*    ---->
         *  |vxx vyx vzx|   | |vxx vyx vzx|
         *  |vxy vyy vzy| x | |vxy vyy vzy|
         *  |vxz vyz vzz|   | |vxz vyz vzz|
         *                  V
         */
        Matrix2D temp = new Matrix2D();
        temp.getVz().setX((m.getVx().getX() * n.getVz().getX()) + (m.getVy().getX() * n.getVz().getY())
                + (m.getVz().getX() * n.getVz().getZ()));
        temp.getVz().setY((m.getVx().getY() * n.getVz().getX()) + (m.getVy().getY() * n.getVz().getY())
                + (m.getVz().getY() * n.getVz().getZ()));
        temp.getVz().setZ((m.getVx().getZ() * n.getVz().getX()) + (m.getVy().getZ() * n.getVz().getY())
                + (m.getVz().getZ() * n.getVz().getZ()));

        temp.getVy().setX((m.getVx().getX() * n.getVy().getX()) + (m.getVy().getX() * n.getVy().getY())
                + (m.getVz().getX() * n.getVy().getZ()));
        temp.getVy().setY((m.getVx().getY() * n.getVy().getX()) + (m.getVy().getY() * n.getVy().getY())
                + (m.getVz().getY() * n.getVy().getZ()));
        temp.getVy().setZ((m.getVx().getZ() * n.getVy().getX()) + (m.getVy().getZ() * n.getVy().getY())
                + (m.getVz().getZ() * n.getVy().getZ()));

        temp.getVx().setX((m.getVx().getX() * n.getVx().getX()) + (m.getVy().getX() * n.getVx().getY())
                + (m.getVz().getX() * n.getVx().getZ()));
        temp.getVx().setY((m.getVx().getY() * n.getVx().getX()) + (m.getVy().getY() * n.getVx().getY())
                + (m.getVz().getY() * n.getVx().getZ()));
        temp.getVx().setZ((m.getVx().getZ() * n.getVx().getX()) + (m.getVy().getZ() * n.getVx().getY())
                + (m.getVz().getZ() * n.getVx().getZ()));
        return temp;
    }

    /**
     * Multiplies matrix {@code m} by vector {@code n}.  The returned value
     * is calculated as:
     * <pre>
     *             x = M x N
     * </pre>
     *
     * @param m the matrix
     * @param n the vector
     * @return the resulting vector
     */
    public static Vector multiply(Matrix2D m, Vector n) {
        /*    ---->
         *  |vxx vyx vzx|   | |x|
         *  |vxy vyy vzy| x | |y|
         *  |vxz vyz vzz|   | |z|
         *                  V
         */
        Vector temp = new Vector();
        temp.setX((m.getVx().getX() * n.getX()) + (m.getVy().getX() * n.getY())
                + (m.getVz().getX() * n.getZ()));
        temp.setY((m.getVx().getY() * n.getX()) + (m.getVy().getY() * n.getY())
                + (m.getVz().getY() * n.getZ()));
        temp.setZ((m.getVx().getZ() * n.getX()) + (m.getVy().getZ() * n.getY())
                + (m.getVz().getZ() * n.getZ()));
        return temp;
    }

    /**
     * Returns the addition of matrices {@code m} and {@code n}
     *
     * @param m the first matrix
     * @param n the second matrix
     * @return the addition of the two matrices
     */
    public static Matrix2D add(Matrix2D m, Matrix2D n) {
        /*
         *  |pxx pyx pzx|   |pxx pyx pzx|
         *  |pxy pyy pzy| + |pxy pyy pzy|
         *  |pxz pyz pzz|   |pxz pyz pzz|
         *
         */
        Matrix2D temp = new Matrix2D();
        temp.getVx().setX((m.getVx().getX() + n.getVx().getX()));
        temp.getVx().setY((m.getVx().getY() + n.getVx().getY()));
        temp.getVx().setZ((m.getVx().getZ() + n.getVx().getZ()));

        temp.getVy().setX((m.getVy().getX() + n.getVy().getX()));
        temp.getVy().setY((m.getVy().getY() + n.getVy().getY()));
        temp.getVy().setZ((m.getVy().getZ() + n.getVy().getZ()));

        temp.getVz().setX((m.getVz().getX() + n.getVz().getX()));
        temp.getVz().setY((m.getVz().getY() + n.getVz().getY()));
        temp.getVz().setZ((m.getVz().getZ() + n.getVz().getZ()));
        return temp;
    }

    /**
     * Returns the addition of matrices {@code m} and {@code n}
     *
     * @param m the first matrix
     * @param n the second matrix
     * @return the addition of the two matrices
     */
    public static Matrix3D add(Matrix3D m, Matrix3D n) {
        /*
         *  |pxx pyx pzx|   |pxx pyx pzx|
         *  |pxy pyy pzy| + |pxy pyy pzy|
         *  |pxz pyz pzz|   |pxz pyz pzz|
         *
         */
        Matrix3D temp = new Matrix3D();
        temp.getVx().setX((m.getVx().getX() + n.getVx().getX()));
        temp.getVx().setY((m.getVx().getY() + n.getVx().getY()));
        temp.getVx().setZ((m.getVx().getZ() + n.getVx().getZ()));
        temp.getVx().setW((m.getVx().getW() + n.getVx().getW()));

        temp.getVy().setX((m.getVy().getX() + n.getVy().getX()));
        temp.getVy().setY((m.getVy().getY() + n.getVy().getY()));
        temp.getVy().setZ((m.getVy().getZ() + n.getVy().getZ()));
        temp.getVy().setW((m.getVy().getW() + n.getVy().getW()));

        temp.getVz().setX((m.getVz().getX() + n.getVz().getX()));
        temp.getVz().setY((m.getVz().getY() + n.getVz().getY()));
        temp.getVz().setZ((m.getVz().getZ() + n.getVz().getZ()));
        temp.getVz().setW((m.getVz().getW() + n.getVz().getW()));

        temp.getVw().setX((m.getVw().getX() + n.getVw().getX()));
        temp.getVw().setY((m.getVw().getY() + n.getVw().getY()));
        temp.getVw().setZ((m.getVw().getZ() + n.getVw().getZ()));
        temp.getVw().setW((m.getVw().getW() + n.getVw().getW()));
        return temp;
    }

    /**
     * Returns the determinant of matrix {@code m}
     *
     * @param m the matrix
     * @return the determinant
     */
    public static double getDeterminant(Matrix2D m) {
        /*
         * |pxx pyx pzx|
         * |pxy pyy pzy|
         * |pxz pyz pzz|
         */
        double a = m.getVx().getX() * ((m.getVy().getY() * m.getVz().getZ())
                - (m.getVy().getZ() * m.getVz().getY()));
        double b = m.getVy().getX() * ((m.getVx().getY() * m.getVz().getZ())
                - (m.getVx().getZ() * m.getVz().getY()));
        double c = m.getVz().getX() * ((m.getVx().getY() * m.getVy().getZ())
                - (m.getVx().getZ() * m.getVy().getY()));
        return a - b + c;
    }

    /**
     * Returns the trace of matrix {@code m}
     *
     * @param m the matrix
     * @return the trace of matrix m
     */
    public static double getTrace(Matrix2D m) {
        /*
         * |pxx pyx pzx|
         * |pxy pyy pzy|
         * |pxz pyz pzz|
         */
        return m.getVx().getX() + m.getVy().getY() + m.getVz().getZ();
    }

    /**
     * Returns the transpose matrix of matrix {@code m}
     *
     * @param m the matrix
     * @return the transpose of the matrix
     */
    public static Matrix2D toTranspose(Matrix2D m) {
        /*
         *  |pxx pyx pzx|    |pxx pxy pxz|
         *  |pxy pyy pzy| => |pyx pyy pyz|
         *  |pxz pyz pzz|    |pzx pzy pzz|
         *
         */
        Matrix2D temp = new Matrix2D();
        temp.getVx().setX(m.getVx().getX());
        temp.getVx().setY(m.getVy().getX());
        temp.getVx().setZ(m.getVz().getX());

        temp.getVy().setX(m.getVx().getY());
        temp.getVy().setY(m.getVy().getY());
        temp.getVy().setZ(m.getVz().getY());

        temp.getVz().setX(m.getVx().getZ());
        temp.getVz().setY(m.getVy().getZ());
        temp.getVz().setZ(m.getVz().getZ());
        return temp;
    }

    /**
     * Calculates the cofactors of matrix {@code m}
     *
     * @param m the matrix
     * @return the matrix of cofactors
     */
    public static Matrix2D toCofactor(Matrix2D m) {
        /*
         * | +   -   + |
         * |pxx pyx pzx|
         * | -   +   - |
         * |pxy pyy pzy|
         * | +   -   + |
         * |pxz pyz pzz|
         */
        Matrix2D temp = new Matrix2D();
        temp.getVx().setX((m.getVy().getY() * m.getVz().getZ())
                - (m.getVy().getZ() * m.getVz().getY()));
        temp.getVx().setY(-1 * ((m.getVy().getX() * m.getVz().getZ())
                - (m.getVy().getZ() * m.getVz().getX())));
        temp.getVx().setZ((m.getVy().getX() * m.getVz().getY())
                - (m.getVy().getY() * m.getVz().getX()));

        temp.getVy().setX(-1 * ((m.getVx().getY() * m.getVz().getZ())
                - (m.getVx().getZ() * m.getVz().getY())));
        temp.getVy().setY((m.getVx().getX() * m.getVz().getZ())
                - (m.getVx().getZ() * m.getVz().getX()));
        temp.getVy().setZ(-1 * ((m.getVx().getX() * m.getVz().getY())
                - (m.getVx().getY() * m.getVz().getX())));

        temp.getVz().setX((m.getVx().getY() * m.getVy().getZ())
                - (m.getVx().getZ() * m.getVy().getY()));
        temp.getVz().setY(-1 * ((m.getVx().getX() * m.getVy().getZ())
                - (m.getVx().getZ() * m.getVy().getX())));
        temp.getVz().setZ((m.getVx().getX() * m.getVy().getY())
                - (m.getVx().getY() * m.getVy().getX()));
        return temp;
    }

    /**
     * Performs a matrix inversion
     *
     * @param m the matrix to be inverted
     * @return the inverted matrix
     */
    public static Matrix2D invert(Matrix2D m) {
        Matrix2D temp = null;

        double det = getDeterminant(m);
        if (det != 0) {
            temp = toCofactor(m);
            temp = toTranspose(temp);

            temp.getVx().setX(temp.getVx().getX() / det);
            temp.getVx().setY(temp.getVx().getY() / det);
            temp.getVx().setZ(temp.getVx().getZ() / det);
            temp.getVy().setX(temp.getVy().getX() / det);
            temp.getVy().setY(temp.getVy().getY() / det);
            temp.getVy().setZ(temp.getVy().getZ() / det);
            temp.getVz().setX(temp.getVz().getX() / det);
            temp.getVz().setY(temp.getVz().getY() / det);
            temp.getVz().setZ(temp.getVz().getZ() / det);
        } else {
            temp = new Matrix2D();
        }
        return temp;
    }
}

