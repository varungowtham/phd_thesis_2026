package de.dbta.mosaic.model.syntax.parser;

import de.dbta.mosaic.common.syntax.SharedNameTable;
import de.dbta.mosaic.model.EquationSystem;
import de.dbta.mosaic.model.Notation;
import de.dbta.mosaic.model.Transformation;
import de.dbta.mosaic.model.VariableNaming;
import de.dbta.mosaic.model.syntax.NameTable;
import de.dbta.mosaic.net.client.ModelingClient;
import java.io.IOException;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * Noninstantiable utility class
 */
public final class ModelTransformationParser /*extends ModelParser*/{
    
    
    private static final String textDummy = "#text";

    
    // Suppress default constructor for noninstantiability
    private ModelTransformationParser() {
        throw new AssertionError();
    }
    
    /**
     * Creates a <code>Transformation</code> according to the description in an XML
     * file.
     * @return the <code>Transformation</code> corresponding to the information in 
     * the XML file.
     */
    public static Transformation parseTransformation(String fileName, DocumentBuilder docBuilder, ModelingClient modClient) {
        return parseTransformationFromFile(fileName,docBuilder,modClient);
    }
    /*****************************************************************
     * 
     *      Private Methods
     * 
     * ***************************************************************
     */
    
    //<editor-fold defaultstate="collapsed" desc="private methods">
    private static Transformation parseTransformationFromFile(String fileName, DocumentBuilder docBuilder, ModelingClient modClient){
        Document trafoDoc;
        Node trafoNode;
        Boolean mappingSectionParsed;
        Boolean matchingSectionParsed;
        Boolean predeterminedSectionParsed;
        try {
            //read out the file of the transformation
            //the document contains the transformation information as a tree of nodes
            trafoDoc = ModelParserUtil.parseFile(docBuilder, fileName, modClient);
        } catch (IOException ex) {
            Logger.getLogger(ModelTransformationParser.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (SAXException ex) {
            Logger.getLogger(ModelTransformationParser.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        
        //get the name of the notation
        trafoNode = trafoDoc.getDocumentElement();
        
        NodeList childNodes = trafoNode.getChildNodes();
        Node descriptionNode = null;
        Node mappingNode = null;
        Node matchingNode = null;
        Node predeterminedNode = null;
        for(int i = 0;i < childNodes.getLength();i++){
            if(childNodes.item(i).getNodeName().equals(NameTable.NameTransformationDescription)){
                descriptionNode = childNodes.item(i);
            }else if(childNodes.item(i).getNodeName().equals(NameTable.NameTransformationMapping)){
                mappingNode = childNodes.item(i);
            }else if(childNodes.item(i).getNodeName().equals(NameTable.NameTransformationMatchingSection)){
                matchingNode = childNodes.item(i);
            }else if(childNodes.item(i).getNodeName().equals(NameTable.NameTransformationPredeterminedSection)){
                predeterminedNode = childNodes.item(i);
            }
        }
        if(mappingNode == null || matchingNode == null || predeterminedNode == null ){
            System.err.println("Something went wrong in 'ModelTransformationParser.parseTransformationFromFile()'.");
            if(mappingNode == null){
                System.err.println("Warning: No mapping node detected in 'parseTransformationFromFile' (level 1)");
                System.err.println("No mapping section identified.");
            }
            if(matchingNode == null){
                System.err.println("Warning: No matching node detected in 'parseTransformationFromFile' (level 1)");
                System.err.println("No matching section identified.");
            }
            if(predeterminedNode == null){
                System.err.println("Warning: No predetermined node detected in 'parseTransformationFromFile' (level 1)");
                System.err.println("No predetermined section identified.");
            }
        }
        
        Transformation transformation = new Transformation();
        transformation.setLocation(fileName);
        if(descriptionNode != null){
            String description = ModelParserUtil.getTextValueOfElement(descriptionNode);
            transformation.setDescription(description);
        }
        //parse mapping, matching, and predetermination section
        mappingSectionParsed = parseTrafoMappingSection(transformation,mappingNode,modClient);
        matchingSectionParsed = parseTrafoMatchingSection(transformation,matchingNode);
        predeterminedSectionParsed = parseTrafoPredeterminedSection(transformation,predeterminedNode);
        if(mappingSectionParsed && matchingSectionParsed){
            return transformation;
        }else{
            System.err.println("Something went wrong in 'ModelTransformationParser.parseTransformationFromFile()'.");
            System.err.println("Parsing of the transformation was not successful.");
            return null;
        }
    }
    
    //</editor-fold>   
}
