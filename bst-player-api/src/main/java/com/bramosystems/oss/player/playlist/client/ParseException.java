/*
 * Copyright 2011 Sikirulai Braheem <sbraheem at bramosystems.com>.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.bramosystems.oss.player.playlist.client;

/**
 * Thrown when an error occurs during parsing of data in various playlist formats
 *
 * @author Sikirulai Braheem <sbraheem at bramosystems.com>
 * @since 1.3
 */
public class ParseException extends Exception {

    /**
     * Constructs an instance of <code>ParseException</code> with the specified detail message.
     * 
     * @param msg the detail message.
     */
    public ParseException(String msg) {
        super(msg);
    }

    /**
     * Constructs an instance of <code>ParseException</code> with the specified message and cause.
     * 
     * @param message the detail message
     * @param cause the cause of the exception
     */
     public ParseException(String message, Throwable cause) {
        super(message, cause);
    }

}
