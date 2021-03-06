/**
 * Copyright 2008 - 2009 Pro-Netics S.P.A.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package it.pronetics.madstore.common.test.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;

public class Utils {

    public static byte[] getUtf8BytesFromFile(String fileName) throws IOException {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
        Reader reader = new InputStreamReader(inputStream);
        StringBuffer stringBuffer = new StringBuffer();
        char[] buffer = new char[4096];
        int len;
        while ((len = reader.read(buffer)) > 0) {
            stringBuffer.append(buffer, 0, len);
        }
        return stringBuffer.toString().getBytes("UTF-8");
    }

    public static Document getDoc(String docFile) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(docFile);
            Document doc = builder.parse(inputStream);
            return doc;
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
