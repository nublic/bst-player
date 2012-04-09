/*
 * Copyright 2009 Sikirulai Braheem
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.bramosystems.oss.player.core.client.video;

/**
 *
 * @author Sikirulai Braheem <sbraheem at bramosystems dot com>
 */
public class AspectRatio {

    private int x,  y;

    public AspectRatio(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public String toString() {
        return x + ":" + y;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof AspectRatio) {
            AspectRatio ar = (AspectRatio)obj;
            return (ar.getX() == x) && (ar.getY() == y);
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 89 * hash + this.x;
        hash = 89 * hash + this.y;
        return hash;
    }

    public static AspectRatio parse(String aspectRatio) {
        if (aspectRatio.length() == 0) {
            throw new IllegalArgumentException("aspectRatio cannot be empty");
        }

        if (aspectRatio.matches("^\\d+:\\d+$")) {
            String[] xy = aspectRatio.split(":");
            return new AspectRatio(Integer.parseInt(xy[0]), Integer.parseInt(xy[1]));
        } else {
            throw new IllegalArgumentException("aspectRatio is invalid! " + aspectRatio);
        }
    }
}
