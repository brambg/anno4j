package com.github.anno4j.rdf_generation.building;

public class RDFTemplate {

	public String insertHead() {
		return "<?xml version=''1.0''encoding=''utf-8''?>";
	}

	public String insertNamespaceTemp(int numberNS, String abbreviation, String namespace) {
		return "<rdf:RDF xmlns:" + insertNamespaces(numberNS, abbreviation, namespace) + ">";
	}

	public String insertNamespaces(int numberNS, String abbreviation, String namespace) {
		return abbreviation + "=''" + namespace + "''";
	}

	public String insertEndRDF() {
		return "</rdf:RDF>";
	}

	public String insertClass(String classValue) {
		return "<rdfs:Class rdf:about=''" + classValue + "''>";
	}

	public String insertEndClass() {
		return "</rdfs:Class>";
	}

	public String insertSubclass(String subclass) {
		return "<rdfs:subClassOf rdfs:resource=''" + subclass + "''/>";
	}

	public String insertProperty(String methodValue) {
		return "<rdf:Property rdf:about=''" + methodValue + "''>";
	}

	public String insertEndProperty() {
		return "</rdf:Property>";
	}

	public String insertType(String resource) {
		return "<rdf:type rdf:resource=''" + resource + "''/>";
	}

	public String insertDomain(String resource) {
		return "<rdf:domain rdf:resource=''" + resource + "''/>";
	}

	public String insertRange(String resource) {
		return "<rdf:range rdf:resource=''" + resource + "''/>";
	}

}
