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
package com.bramosystems.oss.player.util.client;

import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.core.client.JavaScriptObject;

/**
 * Utility class for regular expressions
 *
 * @author Sikirulai Braheem
 * @since 1.1
 */
public class RegExp extends JavaScriptObject {

    /**
     * Create a new RegExp object
     */
    protected RegExp() {
    }

    /**
     * Returns a RegExp instance for the specified pattern and flags.
     *
     * <p>
     * The flags can include the following:
     * <ul>
     * <li>g - performs a global match, that is all matches are found rather than stopping
     * after the first match</li>
     * <li>i - performs a match without case sensitivity</li>
     * <li>m - performs multiline matching, that is the caret (^) character and dollar sign
     * ($) match before and after new-line characters</li>
     * </ul>
     * </p>
     *
     * @param pattern the pattern of the regular expression
     * @param flags the modifiers of the expression
     * @return the RegExp instance
     * @throws RegexException if any error occurs, such as invalid flags
     */
    public static final RegExp getRegExp(String pattern, String flags) throws RegexException {
        try {
            return _getRegExp(pattern, flags);
        } catch (JavaScriptException ex) {
            throw new RegexException(ex.getMessage());
        }
    }

    private static final native RegExp _getRegExp(String pattern, String flags) /*-{
    return new RegExp(pattern, flags);
    }-*/;

    /**
     * Checks if this RegExp object performs a global match.  A match is global if all
     * matches are found rather than stopping after the first match
     *
     * @return <code>true</code> if the global property is set, <code>false</code> otherwise
     */
    public final native boolean isGlobal() /*-{
    return this.global;
    }-*/;

    /**
     * Checks if this RegExp object performs a match without case sensitivity
     *
     * @return <code>true</code> if the ignoreCase property is set, <code>false</code> otherwise
     */
    public final native boolean isIgnoreCase() /*-{
    return this.ignoreCase;
    }-*/;

    /**
     * Checks if this RegExp object performs multiline matching.
     * 
     * @return <code>true</code> if the multiline property is set, <code>false</code> otherwise
     */
    public final native boolean isMultiline() /*-{
    return this.multiline;
    }-*/;

    /**
     * Returns the index position at which the next search starts.
     *
     * @return the index position
     * @see #setLastIndex(int)
     */
    public final native int getLastIndex() /*-{
    return this.lastIndex;
    }-*/;

    /**
     * Sets the index position at which to start the next search.
     *
     * This property affects the {@link #exec(java.lang.String)} and {@link #test(java.lang.String)}
     * methods.
     *
     * @param lastIndex the new index position
     */
    public final native void setLastIndex(int lastIndex) /*-{
    this.lastIndex = lastIndex;
    }-*/;

    /**
     * Returns the <code>pattern</code> of the regular expression
     *
     * @return the <code>pattern</code> of the regular expression
     */
    public final native String getSource() /*-{
    return this.source;
    }-*/;

    /**
     * Tests for the match of the regular expression in the specified {@code exp}.
     *
     * @return <code>true</code> if there is a match, <code>false</code> otherwise
     */
    public final native boolean test(String exp) /*-{
    return this.test(exp);
    }-*/;

    /**
     * Performs a search for the regular expression on the specified {@code exp}
     *
     * @param exp the string to search
     * @return the result of the search or <code>null</code> if there is no match
     */
    private final native RegexResult _exec(String exp) /*-{
    return this.exec(exp);
    }-*/;

    /**
     * Performs a search for the regular expression on the specified {@code exp}
     *
     * @param exp the string to search
     * @return the result of the search
     * @throws RegexException if there is no match
     */
    public final RegexResult exec(String exp) throws RegexException {
        RegexResult res = _exec(exp);
        if (res != null) {
            return res;
        } else {
            throw new RegexException("No match found");
        }
    }

    /**
     * The RegExpResult wraps the result of the search performed by the {@link RegExp#exec(java.lang.String)}
     * method
     *
     * @since 1.1
     * @author Sikirulai Braheem
     */
    public static class RegexResult extends JavaScriptObject {

        /**
         * The constructor
         */
        protected RegexResult() {
        }

        /**
         * Returns the character position of the matched substring within the string
         *
         * @return the position of the matched substring
         */
        public final native int getIndex() /*-{
        return this.index;
        }-*/;

        /**
         * Returns the input string
         *
         * @return the input string
         */
        public final native String getInput() /*-{
        return this.input;
        }-*/;

        /**
         * Gets the matching string at the specified index.
         *
         * <p>Index 0 contains the complete matching substring while other elements
         * of the array (1 through n) contain substrings that match parenthetical groups
         * in the regular expression
         *
         * @param index the index to be retrieved
         * @return the value at the given index, or <code>null</code> if none exists
         */
        public final native String getMatch(int index) /*-{
        return this[index];
        }-*/;
    }

    /**
     * Thrown to indicate a regular expression related exception
     *
     * @since 1.1
     * @author Sikirulai Braheem
     */
    public static class RegexException extends Exception {

        /**
         * Constructs a new RegexException
         */
        public RegexException() {
            super();
        }

        /**
         * Constructs a new RegexException with the specified message
         *
         * @param message the message
         */
        public RegexException(String message) {
            super(message);
        }
    }
}
