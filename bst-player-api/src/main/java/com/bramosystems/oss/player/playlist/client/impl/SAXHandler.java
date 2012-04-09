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
package com.bramosystems.oss.player.playlist.client.impl;

import com.bramosystems.oss.player.playlist.client.ParseException;
import java.util.HashMap;

/**
 *
 * @author Sikirulai Braheem <sbraheem at bramosystems.com>
 */
public interface SAXHandler {

    public void onNodeStart(String nodeName, HashMap<String, String> attr, String namespaceURI) throws ParseException;

    public void setNodeValue(String nodeName, String value) throws ParseException;

    public void onNodeEnd(String nodeName) throws ParseException;
}
