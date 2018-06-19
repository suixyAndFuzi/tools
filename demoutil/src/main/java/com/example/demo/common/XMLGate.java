package com.example.demo.common;

import org.dom4j.util.XMLErrorHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.util.StreamReaderDelegate;
import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stax.StAXSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.*;

/**
 * Created by MichaelHLHZ on 2017/1/16. XML Schema validation for XML with
 * stream pull style
 */

public class XMLGate {

    private static Logger logger = LoggerFactory.getLogger(XMLGate.class);
    private static final int maxErrNo = 1000;



	public static boolean validation(String strXMLFileName, String strXSD) throws Exception {

    	boolean answer = true;

        // prepare XSD Validator
        StringReader srXSD = new StringReader(strXSD);
        InputSource isXSD = new InputSource(srXSD);
        Source sXSD = new SAXSource(isXSD);
        SchemaFactory sfXSD = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        FileInputStream fileInputStream = null;
        XMLStreamReader staxReader = null;
        try
        {
        	Schema scmXSD = sfXSD.newSchema(sXSD);
            Validator validator = scmXSD.newValidator();
            // prepare XSD ErrorHandler
            XMLErrorHandler xehXML = new XMLErrorHandler();
            validator.setErrorHandler(xehXML);

            // prepare XML object
            File fXML = new File(strXMLFileName);
            
            fileInputStream = new FileInputStream(fXML);
            XMLInputFactory staxFactory = XMLInputFactory.newInstance();
            staxReader = staxFactory.createXMLStreamReader(fileInputStream);
            
            // 这里做错误统计，以便于控制内存占用避免OOM
//            staxReader = new StreamReaderDelegate(staxReader) {
//            	public int next() throws XMLStreamException {
//            		int errCount = xehXML.getErrors().nodeCount();
//            		if(errCount > maxErrNo) { // xml源文件中错误超出最大可容忍个数，则抛出异常不再做处理
//            			String msg = "当前xml文件 " + strXMLFileName + " 中数据格式错误超出 " + maxErrNo + " 个，不再做处理，请检查报错err文件并修正xml源文件后重新上报";
//            			logger.error(msg);
//            			throw new XMLStreamException(msg);
//            		}
//            		int n = super.next();
//            		return n;
//            	}
//            };
            
            StAXSource staxXML = new StAXSource(staxReader);
            
        	// valid XML by XSD
            try {
            	validator.validate(staxXML);
            } finally {
	            // when checking error occur, save it to file
		        if(xehXML.getErrors().hasContent()) {
		           // writeXMLError(obj, xehXML.getErrors().asXML());
		            answer = false;
		        }
            }
        }
        catch (SAXException e)
        {
        	String msg="文件不是标准xml文件，不能校验，文件路径："+strXMLFileName+"。"+e.getMessage();
        	logger.error(msg);
        	throw new Exception(msg);
        }
        catch (FileNotFoundException e)
        {
        	String msg="文件校验，校验文件没有找到错误，文件路径："+strXMLFileName+"。"+e.getMessage();
        	logger.error(msg);
        	throw new Exception(msg);
        }
        catch (XMLStreamException e)
        {
        	String msg="文件校验，xml流数据错误，文件路径："+strXMLFileName+"。"+e.getMessage();
        	logger.error(msg);
        	throw new Exception(msg);
        }
        catch (IOException e)
        {
        	String msg="xml文件不能校验,I/O错误，文件路径："+strXMLFileName+"。"+e.getMessage();
        	logger.error(msg);
        	throw new Exception(msg);
        }finally{
        	if(null != staxReader){
        		staxReader.close();
        	}
        	if(null != fileInputStream){
        		fileInputStream.close();
        	}
        }

        return answer;
    }


	public static void main(String[] args){

		try {
			validation("C:\\Users\\T450\\Desktop\\ipt_diagnose.txt","\n" +
					"    <xs:schema xmlns:xs=\"http://www.w3.org/2001/XMLSchema\" elementFormDefault=\"unqualified\">\n" +
					"    <xs:element name=\"ROOT\"><xs:complexType><xs:sequence>\n" +
					"    <xs:element name=\"HIS_ENG_ORDER_DIAGNOSE\" maxOccurs=\"unbounded\">\n" +
					"    \t<xs:complexType>\n" +
					"    \t\t<xs:all>\n" +
					"    \n" +
					"\t\t\t<xs:element name=\"ID\" type=\"xs:string\" minOccurs=\"1\" maxOccurs=\"1\"/>\n" +
					"\t\t\t<xs:element name=\"ORDER_ID\" type=\"string-or-empty\" minOccurs=\"0\" maxOccurs=\"1\"/>\n" +
					"\t\t\t<xs:element name=\"DATE\" type=\"dateTime-or-empty\" minOccurs=\"0\" maxOccurs=\"1\"/>\n" +
					"\t\t\t<xs:element name=\"NAME\" type=\"string-or-empty\" minOccurs=\"0\" maxOccurs=\"1\"/>\n" +
					"\t\t\t<xs:element name=\"TYPE\" type=\"string-or-empty\" minOccurs=\"0\" maxOccurs=\"1\"/>\n" +
					"\t\t\t<xs:element name=\"DSCHARGE_TYPE\" type=\"string-or-empty\" minOccurs=\"0\" maxOccurs=\"1\"/>\n" +
					"\t\t\t<xs:element name=\"ICD10\" type=\"string-or-empty\" minOccurs=\"0\" maxOccurs=\"1\"/>\n" +
					"\t\t\t<xs:element name=\"CREATE_DATE\" type=\"xs:string\" minOccurs=\"0\" maxOccurs=\"1\"/>\n" +
					"\t\t\t<xs:element name=\"UPDATE_DATE\" type=\"dateTime-or-empty\" minOccurs=\"0\" maxOccurs=\"1\"/>\n" +
					"\t\t\t<xs:element name=\"ZONE_ID\" type=\"xs:string\" minOccurs=\"1\" maxOccurs=\"1\"/>\n" +
					"\t\t\t<xs:element name=\"HOSPITAL_CODE\" type=\"xs:string\" minOccurs=\"0\" maxOccurs=\"1\"/>\n" +
					"\n" +
					"    \t\t</xs:all>\n" +
					"    \t</xs:complexType>\n" +
					"    </xs:element>\n" +
					"    </xs:sequence></xs:complexType></xs:element>\n" +
					"\n" +
					"    <xs:simpleType name=\"empty-string\"><xs:restriction base=\"xs:string\"><xs:enumeration value=\"\" /></xs:restriction></xs:simpleType>\n" +
					"    <xs:simpleType name=\"string-or-empty\"><xs:union memberTypes=\"xs:string empty-string\" /></xs:simpleType>\n" +
					"    <xs:simpleType name=\"date-or-empty\"><xs:union memberTypes=\"xs:date empty-string\" /></xs:simpleType>\n" +
					"    <xs:simpleType name=\"dateTime-or-empty\"><xs:union memberTypes=\"xs:dateTime empty-string\" /></xs:simpleType>\n" +
					"    <xs:simpleType name=\"integer-or-empty\"><xs:union memberTypes=\"xs:integer empty-string\" /></xs:simpleType>\n" +
					"    <xs:simpleType name=\"double-or-empty\"><xs:union memberTypes=\"xs:double empty-string\" /></xs:simpleType>\n" +
					"    <xs:simpleType name=\"long-or-empty\"><xs:union memberTypes=\"xs:long empty-string\" /></xs:simpleType>\n" +
					"    <xs:simpleType name=\"time-or-empty\"><xs:union memberTypes=\"xs:time empty-string\" /></xs:simpleType>\n" +
					"\n" +
					"    </xs:schema>\n" +
					"    ");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
