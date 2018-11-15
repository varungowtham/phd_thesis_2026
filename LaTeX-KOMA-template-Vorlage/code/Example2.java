package de.dbta.mosaic.model.syntax.writer;

import de.dbta.mosaic.model.EquationSystem;
import de.dbta.mosaic.model.Transformation;
import de.dbta.mosaic.model.VariableNaming;
import de.dbta.mosaic.model.syntax.NameTable;
import de.dbta.mosaic.net.client.ModelingClient;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 *
 */
public class ModelTransformationWriter extends ModelWriter {
       
    /**
     * Creates a correct XML document for the given <code>Transformation</code> and
     * stores it at the given location.
     *
     * @param modClient data base / data storage exchange client
     * @param transformation The transformation to be stored.
     * @param location The location / name of the xml file.
     */
    public static void writeTransformation(ModelingClient modClient, Transformation trafo, String location) {
        
        Document doc = supplyDocument(modClient);
        if (doc == null){
            return;
        }
 
        // Document element
        Element docElem = (Element) doc.appendChild(doc.createElement(NameTable.NameTransformation));
        //*****************
        // Description
        //*****************
        Element descriptionElem = (Element) docElem.appendChild(createNodeWithTextContent(doc, NameTable.NameTransformationDescription, trafo.getDescription()));
        //*****************
        // Mapping section
        //*****************
        EquationSystem discrEQS = trafo.getDiscretizationEQS();
        String deqsLocation = discrEQS==null?null:discrEQS.getLocation();
        Element mappingElem = (Element) docElem.appendChild(doc.createElement(NameTable.NameTransformationMapping));
        //*****************
        // discr_eqs
        //*****************
        Element subNotaElem = (Element) mappingElem.appendChild(createNodeWithTextContent(doc, NameTable.NameTransformationDiscrEQS, deqsLocation));
        //*****************
        // super_notation
        //*****************
        Element superNotaElem = (Element) mappingElem.appendChild(createNodeWithTextContent(doc, NameTable.NameTransformationSuperNotation, trafo.getSuperNotation().getLocation()));
        
        //*****************
        // Matching section
        //*****************
        Element matchingElem = (Element) docElem.appendChild(doc.createElement(NameTable.NameTransformationMatchingSection));
        
        //*****************
        // variables
        //*****************
        Element variablesElem = (Element) matchingElem.appendChild(doc.createElement(NameTable.NameTransformationVariablesSection));
        Map<VariableNaming,VariableNaming> explMatching = trafo.getIndependentVariableMatching();
        List<VariableNaming> subNamings = new ArrayList<VariableNaming>(explMatching.keySet());
        // loop over all independent variables
        for (VariableNaming subIndependentVarNmg : subNamings){
            //*****************
            // variable
            //*****************
            variablesElem.appendChild(ModelTransformationWriter.createPartVariable(doc, subIndependentVarNmg, explMatching.get(subIndependentVarNmg)));
        }
        
        //*****************
        // indices
        //*****************
        Element indicesElem = (Element) matchingElem.appendChild(doc.createElement(NameTable.NameTransformationIndexSection));
        
        Map<VariableNaming,VariableNaming> fullGenericIndexMatchingTable = trafo.getGenericIndexRenaming();
        //loop over all matched indices
        for(VariableNaming myIdxVarNmg : fullGenericIndexMatchingTable.keySet()){
            //*****************
            // index
            //*****************
             indicesElem.appendChild(ModelTransformationWriter.createPartIndex(doc, myIdxVarNmg, fullGenericIndexMatchingTable.get(myIdxVarNmg)));
        }
        //*****************
        // Predetermined section
        //*****************
        Element predetElem = (Element) docElem.appendChild(doc.createElement(NameTable.NameTransformationPredeterminedSection));
        //*****************
        // state variables
        //*****************
        Element stateVarsElem = (Element) predetElem.appendChild(doc.createElement(NameTable.NameTransformationStateVariables));
        Collection<VariableNaming> stateVarNamings = trafo.getPredeterminedStateVariables();//new ArrayList<VariableNaming>(explMatching.keySet());
        // loop over all predetermined state variables
        for (VariableNaming stateVarNmg : stateVarNamings){
            //*****************
            // variable
            //*****************
            stateVarsElem.appendChild(ModelTransformationWriter.createPartStateVariable(doc,stateVarNmg));
        }
        //*****************
        // default variables
        //*****************
        Element defaultVarsElem = (Element) predetElem.appendChild(doc.createElement(NameTable.NameTransformationDefaultVariables));
        Collection<VariableNaming> defaultVarNamings = trafo.getPredeterminedDefaultVariables();
        // loop over all predetermined default variables
        for (VariableNaming defaultVarNmg : defaultVarNamings){
            //*****************
            // variable
            //*****************
            defaultVarsElem.appendChild(ModelTransformationWriter.createPartDefaultVariable(doc,defaultVarNmg));
        }
        
        // write to desired location
        modClient.writeXHTMLDocument(doc, location, true);
        
    }
}
