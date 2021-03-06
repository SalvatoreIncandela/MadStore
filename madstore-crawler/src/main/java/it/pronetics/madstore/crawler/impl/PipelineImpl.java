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
package it.pronetics.madstore.crawler.impl;

import it.pronetics.madstore.crawler.Pipeline;
import it.pronetics.madstore.crawler.Stage;
import it.pronetics.madstore.crawler.model.Page;
import java.util.LinkedList;
import java.util.List;
import java.util.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default {@link it.pronetics.madstore.crawler.Pipeline} implementation.
 *
 * @author Salvatore Incandela
 * @author Sergio Bossa
 * @author Christian Mongillo
 */
public class PipelineImpl implements Pipeline {

    private static final transient Logger LOG = LoggerFactory.getLogger(PipelineImpl.class);
    private List<Stage> stages = new LinkedList<Stage>();

    public Page execute(Page page) {
        if (stages == null || stages.isEmpty()) {
            return null;
        } else {
            LOG.debug("Processing pipeline for : {}", page.getLink());
            Page processedPage = page;
            for (Stage stage : stages) {
                LOG.debug("Executing stage : {}", stage);
                processedPage = stage.execute(processedPage);
                if (processedPage == null) {
                    LOG.warn("Pipeline ABORTED by stage : {}", stage);
                    break;
                }
            }
            return processedPage;
        }
    }

    public void setStages(List<Stage> stages) {
        this.stages = stages;
    }

    public List<Stage> getStages() {
        return Collections.unmodifiableList(stages);
    }
}
