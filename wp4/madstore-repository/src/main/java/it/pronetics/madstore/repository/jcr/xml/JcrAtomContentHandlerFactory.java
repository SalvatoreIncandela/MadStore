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
package it.pronetics.madstore.repository.jcr.xml;

import java.util.HashMap;
import java.util.Map;
import org.w3c.dom.Document;
import org.xml.sax.ContentHandler;

/**
 * {@link JcrContentHandlerFactory} implementation for exporting Atom contents.
 * 
 * @author Sergio Bossa
 */
public class JcrAtomContentHandlerFactory implements JcrContentHandlerFactory {

    private Map<String, String> allowedNamespaces = new HashMap<String, String>(0);
    
    public ContentHandler makeExportContentHandler(Document document) {
        return new JcrAtomExportContentHandler(document, allowedNamespaces);
    }

    public void setAllowedNamespaces(Map<String, String> allowedNamespaces) {
        this.allowedNamespaces = allowedNamespaces;
    }
}
