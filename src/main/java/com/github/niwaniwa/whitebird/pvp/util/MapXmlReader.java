package com.github.niwaniwa.whitebird.pvp.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.bukkit.Location;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.github.niwaniwa.whitebird.pvp.xml.LocatonBuilder;

public class MapXmlReader {

	public MapXmlReader(String file){
		this.file = file;
	}

	String node;
	NodeList nodeList;

	String proto;
	String version;
	String mapName;
	List<String> author = new ArrayList<String>();
	Location spawn1;
	Location spawn2;
	Location observer;
	String file;

	public void domRead() throws SAXException, IOException, ParserConfigurationException ,NullPointerException{
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = factory.newDocumentBuilder();
		Document document = documentBuilder.parse(file);

		Element root = document.getDocumentElement();

		this.node = root.getNodeName();
		this.proto = root.getAttribute("proto");
		this.nodeList = root.getChildNodes();

		for(int i=0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if(node.getNodeType() == Node.ELEMENT_NODE){
				Element element = (Element)node;
				if(element.getNodeName().equals("name")){
					this.mapName = element.getTextContent();
				} else if(element.getNodeName().equals("authors")){
					NodeList authorsList = node.getChildNodes();
					for (int j=0; j < authorsList.getLength(); j++) {
						Node authorNode = authorsList.item(j);
						if(authorNode.getNodeType() == Node.ELEMENT_NODE){
							if(authorNode.getNodeName().equals("author")){
								author.add(authorNode.getTextContent());
							}
						}
					}
				} else if(element.getNodeName().equals("version")){
					this.version = element.getTextContent();
				}else if(element.getNodeName().equals("spawns")){
					LocatonBuilder locB = new LocatonBuilder(this);
					locB.get();
					this.spawn1 = locB.getLocation("spawn1");
					this.spawn2 = locB.getLocation("2");
				}
			}
		}
	}

	public String getMapName(){
		return mapName;
	}

	public String getVersion(){
		return version;
	}

	public List<String> getAuthor(){
		return author;
	}

	public String getProto(){
		return proto;
	}

	public double getDouble(String value){
		return Double.valueOf(value);
	}

	public Location getLocation(String value){
		if(value == "spawn1"){
			return spawn1;
		} else {
			return spawn2;
		}
	}

	public String getPath(){
		return file;
	}

}
