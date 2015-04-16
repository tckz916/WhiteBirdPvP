package com.github.niwaniwa.whitebird.pvp.xml;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.github.niwaniwa.whitebird.pvp.util.MapXmlReader;

public abstract class Builder {

	MapXmlReader xml;

	public Builder(MapXmlReader xml){
		this.xml = xml;
	}

	public abstract void get() throws ParserConfigurationException, SAXException, IOException;
}
