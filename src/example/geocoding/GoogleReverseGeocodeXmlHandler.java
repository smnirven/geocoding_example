package example.geocoding;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class GoogleReverseGeocodeXmlHandler extends DefaultHandler 
{
	private boolean inLocalityName = false;
	private boolean finished = false;
	private StringBuilder builder;
	private String localityName;
	
	public String getLocalityName()
	{
		return this.localityName;
	}
	
	@Override
	public void characters(char[] ch, int start, int length)
	       throws SAXException {
	    super.characters(ch, start, length);
	    if (this.inLocalityName && !this.finished)
	    {
	    	if ((ch[start] != '\n') && (ch[start] != ' '))
	    	{
	    		builder.append(ch, start, length);
	    	}
	    }
	}

	@Override
	public void endElement(String uri, String localName, String name)
	        throws SAXException 
	{
	    super.endElement(uri, localName, name);
	    
	    if (!this.finished)
	    {
	    	if (localName.equalsIgnoreCase("LocalityName"))
	    	{
	    		this.localityName = builder.toString();
	    		this.finished = true;
	    	}
	    	
	    	if (builder != null)
	    	{
	    		builder.setLength(0);
	    	}
	    }
    }

    @Override
    public void startDocument() throws SAXException 
    {
        super.startDocument();
        builder = new StringBuilder();
    }

    @Override
    public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException
    {
    	super.startElement(uri, localName, name, attributes);
    	
    	if (localName.equalsIgnoreCase("LocalityName"))
    	{
    		this.inLocalityName = true;
    	}
    }
}
