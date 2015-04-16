package com.github.niwaniwa.whitebird.pvp.xml;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.bukkit.Location;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.github.niwaniwa.whitebird.pvp.util.MapXmlReader;

public class LocatonBuilder extends Builder {

	public LocatonBuilder(MapXmlReader xml) {
		super(xml);
	}

	String node;
	NodeList nodeList;
	private Location spawn1;
	private Location spawn2;

	@Override
	public void get() throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = factory.newDocumentBuilder();
		Document document = documentBuilder.parse(xml.getPath());

		Element root = document.getDocumentElement();

		this.node = root.getNodeName();
		this.nodeList = root.getChildNodes();

		for(int i=0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if(node.getNodeType() == Node.ELEMENT_NODE){

				NodeList authorsList = node.getChildNodes();

				for (int j=0; j < authorsList.getLength(); j++) {

					Node authorNode = authorsList.item(j);

					if(authorNode.getNodeType() == Node.ELEMENT_NODE){

						Element ele = (Element)authorNode;

						if(authorNode.getNodeName().equals("spawn")){

							if(ele.getAttribute("type").equalsIgnoreCase("spawn1")){

								String[] temp = ele.getAttributeNode("location").getValue().split(",");
								String yaw = ele.getAttributeNode("yaw").getValue();
								this.spawn1 = new Location(null, getDouble(temp[0]), getDouble(temp[1]), getDouble(temp[2]),  (float) getDouble(yaw), 0);

							} else {
								String[] temp = ele.getAttributeNode("location").getValue().split(",");
								String yaw = ele.getAttributeNode("yaw").getValue();
								this.spawn2 = new Location(null, getDouble(temp[0]), getDouble(temp[1]), getDouble(temp[2]), (float) getDouble(yaw), 0);
							}
						}
					}
				}
			}
		}
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

}
