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
package it.pronetics.madstore.server.jaxrs.atom.search.impl;

import it.pronetics.madstore.common.AtomConstants;
import it.pronetics.madstore.repository.util.PagingList;
import it.pronetics.madstore.server.HttpConstants;
import it.pronetics.madstore.server.jaxrs.atom.impl.AbstractResourceHandler;
import it.pronetics.madstore.server.jaxrs.atom.resolver.ResourceName;
import it.pronetics.madstore.server.jaxrs.atom.search.CollectionSearchResourceHandler;
import it.pronetics.madstore.server.jaxrs.atom.resolver.ResourceUriFor;
import java.net.URL;
import java.util.Arrays;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import org.apache.abdera.Abdera;
import org.apache.abdera.ext.opensearch.OpenSearchConstants;
import org.apache.abdera.ext.opensearch.model.IntegerElement;
import org.apache.abdera.factory.Factory;
import org.apache.abdera.model.Entry;
import org.apache.abdera.model.Feed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * {@link it.pronetics.madstore.server.jaxrs.atom.search.CollectionSearchResourceHandler} implementation based on
 * JBoss Resteasy and Abdera atom model.
 *
 * @author Sergio Bossa
 */
@Path("/")
public class DefaultCollectionSearchResourceHandler extends AbstractResourceHandler implements CollectionSearchResourceHandler<Feed> {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultCollectionSearchResourceHandler.class);
    private String collectionKey;
    private int maxNumberOfEntries;
    private int pageNumberOfEntries;
    private String searchTitle;
    private String searchTerms;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    @ResourceUriFor(resource = ResourceName.COLLECTION_SEARCH)
    @GET
    @Path("/search/{collectionKey}")
    @Produces(AtomConstants.ATOM_MEDIA_TYPE)
    public Response getCollectionSearchResource() {
        try {
            String[] termsArray = searchTerms.split(" ");
            int max = maxNumberOfEntries;
            int offset = (pageNumberOfEntries - 1) * max;
            //
            Factory abderaFactory = Abdera.getInstance().getFactory();
            Feed feed = abderaFactory.newFeed();
            PagingList<Entry> entries = findEntriesFromRepository(collectionKey, Arrays.asList(termsArray), offset, max);
            configureFeed(feed, entries);
            Response response = buildOkResponse(feed);
            return response;
        } catch (Exception ex) {
            LOG.error(ex.getMessage(), ex);
            throw new WebApplicationException(Response.serverError().build());
        }
    }

    @PathParam("collectionKey")
    public void setCollectionKey(String collectionKey) {
        this.collectionKey = collectionKey;
    }

    @QueryParam(HttpConstants.MAX_PARAMETER)
    @DefaultValue("10")
    public void setMaxNumberOfEntries(int maxNumberOfEntries) {
        this.maxNumberOfEntries = maxNumberOfEntries;
    }

    @QueryParam(HttpConstants.PAGE_PARAMETER)
    @DefaultValue("1")
    public void setPageNumberOfEntries(int pageNumberOfEntries) {
        this.pageNumberOfEntries = pageNumberOfEntries;
    }

    @QueryParam(HttpConstants.TITLE_PARAMETER)
    @DefaultValue("")
    public void setSearchTitle(String searchTitle) {
        this.searchTitle = searchTitle;
    }

    @QueryParam(HttpConstants.TERMS_PARAMETER)
    @DefaultValue("")
    public void setSearchTerms(String searchTerms) {
        this.searchTerms = searchTerms;
    }

    private void configureFeed(Feed feed, PagingList<Entry> entries) throws Exception {
        URL baseUrl = resourceResolver.resolveResourceUriFor(
                ResourceName.COLLECTION_SEARCH,
                uriInfo.getBaseUri().toString(),
                collectionKey);
        URL nextUrl = UriBuilder.fromUri(
                baseUrl.toURI()).queryParam(HttpConstants.TITLE_PARAMETER, searchTitle).queryParam(HttpConstants.TERMS_PARAMETER, searchTerms).queryParam(HttpConstants.PAGE_PARAMETER, new Integer(pageNumberOfEntries + 1)).queryParam(HttpConstants.MAX_PARAMETER, maxNumberOfEntries).build().toURL();
        URL prevUrl = UriBuilder.fromUri(
                baseUrl.toURI()).queryParam(HttpConstants.TITLE_PARAMETER, searchTitle).queryParam(HttpConstants.TERMS_PARAMETER, searchTerms).queryParam(HttpConstants.PAGE_PARAMETER, new Integer(pageNumberOfEntries - 1)).queryParam(HttpConstants.MAX_PARAMETER, maxNumberOfEntries).build().toURL();
        String id = resourceResolver.resolveResourceIdFor(
                uriInfo.getBaseUri().toString(),
                ResourceName.COLLECTION_SEARCH,
                collectionKey);

        feed.setId(id);
        feed.setTitle(searchTitle);
        feed.addAuthor(Abdera.getInstance().getFactory().newAuthor().getText());

        for (Entry entry : entries) {
            feed.addEntry(entry);
        }

        if (entries.size() > 0) {
            int currentLastResult = ((pageNumberOfEntries - 1) * maxNumberOfEntries) + entries.size();
            if (currentLastResult < entries.getTotal()) {
                feed.addLink(nextUrl.toString(), "next");
            }
            if (pageNumberOfEntries > 1) {
                feed.addLink(prevUrl.toString(), "previous");
            }
            IntegerElement totalResults = feed.addExtension(OpenSearchConstants.TOTAL_RESULTS);
            totalResults.setValue(entries.getTotal());
            IntegerElement itemsPerPage = feed.addExtension(OpenSearchConstants.ITEMS_PER_PAGE);
            itemsPerPage.setValue(entries.getMax());
            IntegerElement startIndex = feed.addExtension(OpenSearchConstants.START_INDEX);
            startIndex.setValue(entries.getOffset() + 1);
        }
    }
}
