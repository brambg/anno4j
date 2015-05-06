package com.github.anno4j.model.impl.agent;

import com.github.anno4j.model.ontologies.FOAF;
import com.github.anno4j.model.ontologies.PROV;
import org.openrdf.annotations.Iri;

/**
 * Conforms to http://www.w3.org/ns/prov#SoftwareAgent
 *
 * A software agent is running software.
 */
@Iri(PROV.SOFTWARE_AGENT)
public class AgentSoftware extends AgentDefault {

    @Iri(FOAF.HOMEPAGE) private String homepage;

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    @Override
    public String toString() {
        return "AgentSoftware{" +
                "homepage='" + homepage + '\'' +
                ", resource='" + getResource() +
                '}';
    }
}
