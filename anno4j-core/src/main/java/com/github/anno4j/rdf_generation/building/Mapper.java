package com.github.anno4j.rdf_generation.building;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.anno4j.rdf_generation.fragments.Fragment;
import com.github.anno4j.rdf_generation.fragments.Fragments;
import com.google.common.reflect.ClassPath;

public class Mapper {
	
	/**
	 * The Logger for printing progress and user messages.
	 */
	private final static Logger logger = LoggerFactory.getLogger(Builder.class);

	/**
	 * Returns the range for a property which was generated by mapping the java
	 * return value to the RDFS equivalent.
	 * 
	 * @param propID   The ID of the method whose return value should be mapped to a
	 *                 RDFS range.
	 * @param rangeMap The map where all methods and their return values are stored.
	 * @return The range of the property.
	 * @throws IOException
	 */
	public static String mapJavaReturn(Integer propID, Map<Integer, String> rangeMap) throws IOException {
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
	 * @throws IOException
	 */
	private static String mapToRDFRange(String javavalue) throws IOException {
		List<Fragment> fraglist = Fragments.getFragments();
		if (isDatatypePrimitive(javavalue, fraglist)) {
			for (int i = 0; i < fraglist.size(); i++) {
				String javaequiv = fraglist.get(i).getJavaEquiv();
				if (javaequiv.equals(javavalue)) {
					return javaequiv;
				} else {
					if (fraglist.get(i).hasRelationTo(javavalue)) {
						return fraglist.get(i).getURI();
					}
				}
			}
		} else { // datatype is complex
			if (getComplexDatatypeByList(javavalue) != null) {
				return getComplexDatatypeByList(javavalue);
			} else {
				return getComplexDatatypeBySearch(javavalue);
			}
		}
		return null;
	}

	/**
	 * Starts the search for a complex datatype in a complete input package.
	 * 
	 * @param javavalue The unknown complex datatype.
	 * @return The @Iri annotation value of the class, which is the rdfs equivalent
	 *         of the complex datatype.
	 * @throws IOException
	 */
	private static String getComplexDatatypeBySearch(String javavalue) throws IOException {
		logger.debug("A separate search was started to find an unknown complex datatype inside the input package.");
		return searchAnnotFromClass(Extractor.getPackages(), Extractor.extractLastName(javavalue));
	}

	/**
	 * Starts looking for an unknown complex datatype in all other classes contained
	 * in the input package.
	 * 
	 * @param javavalue The unkown complex datatype.
	 * @return The @Iri annotation value of the class, which is the rdfs equivalent
	 *         of the complex datatype.
	 */
	private static String getComplexDatatypeByList(String javavalue) {
		for (Map.Entry<Integer, String> e : Extractor.getClassNames().entrySet()) {
			// if the return value is one of the classes in the package...
			if (javavalue.endsWith(e.getValue())) {
				for (Map.Entry<Integer, String> e1 : Extractor.getClassValues().entrySet()) {
					// ...then find its annotation value and return it a the range of the property
					if (e.getKey() == e1.getKey()) {
						return e1.getValue();
					}
				}
			}
		}
		return null;
	}

	/**
	 * Checks if the input return value matches a return value in the list of
	 * fragments, wich contains all primitive datatypes that can be converted.
	 * 
	 * @param javavalue The return value.
	 * @param fraglist  A list of all primitive datatypes, which can be converted.
	 * @return true, if the input datatype is primitive, false otherwise.
	 */
	private static boolean isDatatypePrimitive(String javavalue, List<Fragment> fraglist) {
		for (int i = 0; i < fraglist.size(); i++) {
			if (fraglist.get(i).hasRelationTo(javavalue)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if a class with an extracted classname is also part of the intput
	 * packagestructure.
	 * 
	 * @param pathname  The input packagestructure.
	 * @param classname The classname which we search for.
	 * @return null, if a class with the given classname is not found in the
	 *         package. The @Iri annotation value of the class with the given class
	 *         name, if it was found in the package.
	 * @throws IOException
	 */
	private static String searchAnnotFromClass(String pathname, String classname) throws IOException {
		String packageabove = extractPackage(pathname);
		String newclassname = extendPath(packageabove, classname);
		Class<?> clazz = loadClass(newclassname);
		if (clazz != null) {
			return Extractor.extractClassAnnotValue(clazz);
		} else {
			return null;
		}
	}

	/**
	 * Returns only the package without the last classname.
	 * 
	 * @param pathname The complete input package sturcture.
	 * @return The packagestructure without the classname.
	 */
	private static String extractPackage(String pathname) {
		int beginIndex = 0;
		int endIndex = pathname.lastIndexOf(".");
		return pathname.substring(beginIndex, endIndex);
	}

	/**
	 * Extends a packagestructure with a new classname.
	 * 
	 * @param pathname  The package structure without a classname at the end.
	 * @param classname The new classname
	 * @return The concatenated package structure with the classname at the end.
	 */
	private static String extendPath(String pathname, String classname) {
		return pathname + "." + classname;
	}

	/**
	 * Loads a class with the name searchname, if it can be found.
	 * 
	 * @param searchname The name of the class or package to be loaded.
	 * @return The Class Object of the corresponding classname.
	 * @throws IOException
	 */
	private static Class<?> loadClass(String searchname) throws IOException {
		final ClassLoader loader = Thread.currentThread().getContextClassLoader();
		for (final ClassPath.ClassInfo info : ClassPath.from(loader).getTopLevelClasses()) {
			if (info.getName().matches(searchname)) {
				final Class<?> clazz = info.load();
				return clazz;
			}
		}
		return null;
	}
}
