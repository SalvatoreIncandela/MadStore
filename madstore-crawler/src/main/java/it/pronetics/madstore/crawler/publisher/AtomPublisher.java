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
package it.pronetics.madstore.crawler.publisher;

import it.pronetics.madstore.crawler.model.Page;

/**
 * AtomPublisher manages publishing of Atom documents to the repository.
 *
 * @author Salvatore Incandela
 */
public interface AtomPublisher {

    /**
     * Publish an Atom document.
     * 
     * @param page The Atom document to publish, as a {@link it.pronetics.madstore.crawler.model.Page} object.
     */
    public void publish(Page page);
}
