package com.github.anno4j.rdf_generation.building;

import java.util.List;
import java.util.Map;

import com.github.anno4j.rdf_generation.fragments.Fragment;
import com.github.anno4j.rdf_generation.fragments.Fragments;
import com.github.anno4j.rdf_generation.types.Type;
import com.github.anno4j.rdf_generation.types.Types;

import ch.qos.logback.classic.pattern.ClassNameOnlyAbbreviator;

public class Mapper {

	/**
	 * Returns the range for a property which was generated by mapping the java
	 * return value to the RDFS equivalent.
	 * 
	 * @param propID   The ID of the method whose return value should be mapped to a
	 *                 RDFS range.
	 * @param rangeMap The map where all methods and their return values are stored.
	 * @return The range of the property.
	 */
	public static String mapJavaReturn(Integer propID, Map<Integer, String> rangeMap) {
		for (Map.Entry<Integer, String> e : rangeMap.entrySet()) {
			if (e.getKey() == propID) {
				return mapToRDFRange(e.getValue());
			}
		}
		return null;
	}

	/**
	 * Maps a specific java return value to a RDFS range uri. If the extracted
	 * return value has a relation to a primitive datatype in java, the equivalent
	 * datatype uri will be returned. If the java return value matches one of the
	 * names of the converted classes, the annotation value of that class will be
	 * returned.
	 * 
	 * @param javavalue The java return type of a method.
	 * @return The range of the property converted from the method.
	 */
	private static String mapToRDFRange(String javavalue) {
		// fraglist contains all fragments of primitive datatypes.
		List<Fragment> fraglist = Fragments.getFragments();
		for (int i = 0; i < fraglist.size(); i++) {
			// if the return value matches a primitive datatype
			if (fraglist.get(i).hasRelationTo(javavalue)) {
				// return the datatype uri
				return fraglist.get(i).getURI();
			} else {
				// if the return value is a complex datatype
				for (Map.Entry<Integer, String> e : Extractor.getClassNames().entrySet()) {
					// if the return value is one of the classes in the package...
					if (javavalue.endsWith(e.getValue())) {
						for (Map.Entry<Integer, String> e1 : Extractor.getClassValues().entrySet()) {
							// ...then find its annotation value and return it a the range of the property
							if (e.getKey() == e1.getKey()) {
								return e1.getValue(); // may be a self defined type eg Player or Pet.
							}
						}
					}
				}
			}
		}
		return null;
	}

	public static String mapType(String type) {
		List<Type> typelist = Types.getTypes();
		for (int i = 0; i < typelist.size(); i++) {
			if (typelist.get(i).hasRelationTo(type)) {
				return typelist.get(i).getURI();
			}
		}
		return null;
	}
}
