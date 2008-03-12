/*
 *  Copyright 2007, Plutext Pty Ltd.
 *   
 *  This file is part of docx4j.

    docx4j is free software: you can use it, redistribute it and/or modify
    it under the terms of version 3 of the GNU Affero General Public License 
    as published by the Free Software Foundation.

    docx4j is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License   
    along with docx4j.  If not, see <http://www.fsf.org/licensing/licenses/>.
    
 */

package org.docx4j.openpackaging.parts;


import java.io.File;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.MarshalException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.ValidationEventHandler;

import org.docx4j.jaxb.Context;
import org.docx4j.openpackaging.exceptions.InvalidFormatException;
import org.dom4j.Document;

/** OPC Parts are either XML, or binary (or text) documents.
 * 
 *  Most are XML documents.
 *  
 *  docx4j aims to represent XML parts using JAXB.  
 *  
 *  Any XML Part for which we have a JAXB representation (eg the main
 *  document part) should extend this Part.  
 *  
 *  This class provides only one of the methods for serializing (marshalling) the 
 *  Java content tree back into XML data found in 
 *  javax.xml.bind.Marshaller interface.  You can always use 
 *  any of the others by getting the jaxbElement required by those
 *  methods.
 *  
 *  Insofar as unmarshalling is concerned, at present it doesn't 
 *  contain all the methods in javax.xml.bind.unmarshaller interface.
 *  This is because the content always comes from the same place
 *  (ie from a zip file or JCR via org.docx4j.io.*).  
 *  TODO - what is the best thing to unmarshall from?
 *  
 *  
 * */
public abstract class JaxbXmlPart extends Part {
	
	public JaxbXmlPart(PartName partName) throws InvalidFormatException {
		super(partName);
		setJAXBContext(Context.jc);						
	}

	public JaxbXmlPart(PartName partName, JAXBContext jc) throws InvalidFormatException {
		super(partName);
		setJAXBContext(jc);
	}
	
	protected JAXBContext jc;
	
	public void setJAXBContext(JAXBContext jc) {
		this.jc = jc;
	}
	
	
	/** The content tree (ie JAXB representation of the Part) */
	public Object jaxbElement = null;

	public Object getJaxbElement() {
		return jaxbElement;
	}

	public void setJaxbElement(Object jaxbElement) {
		this.jaxbElement = jaxbElement;
	}
	
	
	
    /**
     * Marshal the content tree rooted at <tt>jaxbElement</tt> into a DOM tree.
     * 
     * @param node
     *      DOM nodes will be added as children of this node.
     *      This parameter must be a Node that accepts children
     *      ({@link org.w3c.dom.Document},
     *      {@link  org.w3c.dom.DocumentFragment}, or
     *      {@link  org.w3c.dom.Element})
     * 
     * @throws JAXBException
     *      If any unexpected problem occurs during the marshalling.
     */
    public void marshal(org.w3c.dom.Node node) throws JAXBException {

		try {
			Marshaller marshaller = jc.createMarshaller();

			marshaller.setProperty("com.sun.xml.internal.bind.namespacePrefixMapper", 
					new org.docx4j.jaxb.NamespacePrefixMapper() ); // Must use 'internal' for Java 6
			
			marshaller.marshal(jaxbElement, node);

		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
    
    /**
	 * Marshal the content tree rooted at <tt>jaxbElement</tt> into an output
	 * stream.
	 * 
	 * @param os
	 *            XML will be added to this stream.
	 * 
	 * @throws JAXBException
	 *             If any unexpected problem occurs during the marshalling.
	 */
    public void marshal(java.io.OutputStream os) throws JAXBException {

		try {
			Marshaller marshaller = jc.createMarshaller();

			marshaller.setProperty("com.sun.xml.internal.bind.namespacePrefixMapper", 
					new org.docx4j.jaxb.NamespacePrefixMapper() ); // Must use 'internal' for Java 6
			
			System.out.println("marshalling " + this.getClass().getName() + " ..." );									
			
			marshaller.marshal(jaxbElement, os);
			
			System.out.println(this.getClass().getName() + " marshalled \n\n" );									

		} catch (JAXBException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


    /**
	 * Unmarshal XML data from the specified InputStream and return the
	 * resulting content tree. Validation event location information may be
	 * incomplete when using this form of the unmarshal API.
	 * 
	 * <p>
	 * Implements <a href="#unmarshalGlobal">Unmarshal Global Root Element</a>.
	 * 
	 * @param is
	 *            the InputStream to unmarshal XML data from
	 * @return the newly created root object of the java content tree
	 * 
	 * @throws JAXBException
	 *             If any unexpected errors occur while unmarshalling
	 */
    public abstract Object unmarshal( java.io.InputStream is ) throws JAXBException;


	
	
}
