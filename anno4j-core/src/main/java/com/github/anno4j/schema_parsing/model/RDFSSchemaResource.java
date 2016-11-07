package com.github.anno4j.schema_parsing.model;

import com.github.anno4j.model.impl.ResourceObject;
import com.github.anno4j.model.namespaces.RDFS;
import org.openrdf.annotations.Iri;

import java.util.Set;

/**
 * Top level (Java) superclass for the RDFSClazz and RDFSProperty interfaces.
 */
public interface RDFSSchemaResource extends ResourceObject {

    /**
     * https://www.w3.org/TR/rdf-schema/#ch_label
     * rdfs:label is an instance of rdf:Property that may be used to provide a human-readable version of a resource's name.
     */
    @Iri(RDFS.LABEL)
    void setLabels(Set<String> labels);

    /**
     * https://www.w3.org/TR/rdf-schema/#ch_label
     * rdfs:label is an instance of rdf:Property that may be used to provide a human-readable version of a resource's name.
     */
    @Iri(RDFS.LABEL)
    Set<String> getLabels();

    /**
     * https://www.w3.org/TR/rdf-schema/#ch_label
     * rdfs:label is an instance of rdf:Property that may be used to provide a human-readable version of a resource's name.
     */
    void addLabel(String label);

    /**
     * Refers to https://www.w3.org/TR/rdf-schema/#ch_comment
     * rdfs:comment is an instance of rdf:Property that may be used to provide a human-readable description of a resource.
     */
    @Iri(RDFS.COMMENT)
    void setComment(String comment);

    /**
     * Refers to https://www.w3.org/TR/rdf-schema/#ch_comment
     * rdfs:comment is an instance of rdf:Property that may be used to provide a human-readable description of a resource.
     */
    @Iri(RDFS.COMMENT)
    String getComment();
}
