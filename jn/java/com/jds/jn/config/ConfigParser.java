package com.jds.jn.config;

import org.w3c.dom.*;
import org.w3c.dom.ls.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.jds.jn.config.properties.ObjectParse;
import com.jds.jn.config.properties.PropertyValue;
import com.jds.jn.network.listener.types.ListenerType;
import com.jds.jn.network.listener.types.ReceiveType;
import com.jds.jn.network.profiles.*;
import com.sun.org.apache.xerces.internal.impl.Constants;

/**
 * Author: VISTALL
 * Company: J Develop Station
 * Date:  21:24:55/05.06.2010
 */
public class ConfigParser
{
	private static ConfigParser _instance;

	// caches
	private Map<String, RValues> _rValues = new HashMap<String, RValues>();
	private Map<RValues, String> _rValues2 = new HashMap<RValues, String>();

	private Map<String, Method> _methodValues = new HashMap<String, Method>();
	private Map<Method, String> _methodValues2 = new HashMap<Method, String>();

	public static ConfigParser getInstance()
	{
		if (_instance == null)
		{
			_instance = new ConfigParser();
		}
		return _instance;
	}

	private ConfigParser()
	{
		parseAnnotations();

		load();
	}

	private void parseAnnotations()
	{
		for (Field rv : RValues.class.getFields())
		{
			if (rv.isAnnotationPresent(PropertyValue.class))
			{
				String t = rv.getAnnotation(PropertyValue.class).value();
				RValues val = RValues.valueOf(rv.getName());
				_rValues.put(t, val);
				_rValues2.put(val, t);
			}
		}

		for (Method getMethod : NetworkProfilePart.class.getMethods())
		{
			if (getMethod.isAnnotationPresent(PropertyValue.class))
			{
				try
				{
					String t = getMethod.getAnnotation(PropertyValue.class).value();
					String nameSet = getMethod.getName().replace("get", "set");
					Method setMethod = NetworkProfilePart.class.getMethod(nameSet, getMethod.getReturnType());

					_methodValues.put(t, setMethod);
					_methodValues2.put(getMethod, t);
				}
				catch (NoSuchMethodException e)
				{
					System.out.println("Not find 'set' method.");
				}
			}
		}
	}

	public void load()
	{
		File file = new File("./properties.xml");
		if (!file.exists())
		{
			return;
		}

		try
		{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			factory.setValidating(false);
			factory.setIgnoringComments(true);
			Document document = factory.newDocumentBuilder().parse(file);

			NamedNodeMap map;
			for (Node s1 = document.getFirstChild(); s1 != null; s1 = s1.getNextSibling())
			{
				if ("config".equalsIgnoreCase(s1.getNodeName()))
				{
					for (Node start0 = s1.getFirstChild(); start0 != null; start0 = start0.getNextSibling())
					{
						if ("property".equalsIgnoreCase(start0.getNodeName()))
						{
							map = start0.getAttributes();
							if (map.getNamedItem("name") == null || map.getNamedItem("val") == null)
							{
								continue;
							}
							RValues rv = _rValues.get(map.getNamedItem("name").getNodeValue());

							if (rv != null)
							{
								rv.setVal(ObjectParse.parse(rv.getType(), map.getNamedItem("val").getNodeValue()));
							}
						}
						else if ("file".equalsIgnoreCase(start0.getNodeName()))
						{
							map = start0.getAttributes();
							if (map.getNamedItem("val") == null)
							{
								continue;
							}

							LastFiles.addLastFile(map.getNamedItem("val").getNodeValue());
						}
						else if ("profile".equalsIgnoreCase(start0.getNodeName()))
						{
							map = start0.getAttributes();
							if (map.getNamedItem("name") == null || map.getNamedItem("type") == null)
							{
								continue;
							}

							NetworkProfile profile = NetworkProfiles.getInstance().newProfile(map.getNamedItem("name").getNodeValue(), ReceiveType.valueOf(map.getNamedItem("type").getNodeValue()));

							for (Node partNode = start0.getFirstChild(); partNode != null; partNode = partNode.getNextSibling())
							{
								if ("part".equalsIgnoreCase(partNode.getNodeName()))
								{
									map = partNode.getAttributes();

									if (map.getNamedItem("type") == null)
									{
										continue;
									}

									ListenerType type = ListenerType.valueOf(map.getNamedItem("type").getNodeValue());

									NetworkProfilePart part = profile.getPart(type);
									if (part == null)
									{
										continue;
									}

									for(int i = 0; i < map.getLength(); i++)
									{
										Node node = map.item(i);

										if(node != null)
										{
											Method method = _methodValues.get(node.getNodeName());
											if(method != null)
											{
												Class<?> p = method.getParameterTypes()[0];
												method.invoke(part, ObjectParse.parse(p, node.getNodeValue()));
											}
										}
									}
								}
							}

						}
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void shutdown() throws Exception
	{
		File file = new File("./properties.xml");


		if (file.exists())
		{
			file.delete();
		}

		file.createNewFile();

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setValidating(true);
		factory.setIgnoringComments(true);
		DocumentBuilder docBuilder = factory.newDocumentBuilder();
		Document doc = docBuilder.newDocument();
		DOMImplementationLS implLS = (DOMImplementationLS) docBuilder.getDOMImplementation();
		LSSerializer domWriter = implLS.createLSSerializer();
		LSOutput output = implLS.createLSOutput();
		domWriter.getDomConfig().setParameter(Constants.DOM_FORMAT_PRETTY_PRINT, Boolean.TRUE);

		Element root = doc.createElement("config");

		for (RValues val : RValues.values())
		{
			if (val != null && val.getVal() != null)
			{
				Element property = doc.createElement("property");
				property.setAttribute("name", _rValues2.get(val));
				property.setAttribute("val", String.valueOf(val.getVal()));
				root.appendChild(property);
			}
		}

		for (String last : LastFiles.getLastFiles())
		{
			if (last != null)
			{
				Element lastFile = doc.createElement("file");
				lastFile.setAttribute("val", last);
				root.appendChild(lastFile);
			}
		}

		for (NetworkProfile b : NetworkProfiles.getInstance().profiles())
		{
			if (b == null)
			{
				continue;
			}

			Element profileNode = doc.createElement("profile");
			profileNode.setAttribute("name", b.getName());
			profileNode.setAttribute("type", b.getType().name());

			for (NetworkProfilePart part : b.parts())
			{
				if (part == null)
				{
					continue;
				}

				Element partNode = doc.createElement("part");
				partNode.setAttribute("type", part.getType().name());

				for (Method f : NetworkProfilePart.class.getMethods())
				{

					if (_methodValues2.containsKey(f))
					{
						Object obj = f.invoke(part);
						if(obj != null)
							partNode.setAttribute(_methodValues2.get(f), String.valueOf(obj));
					}
				}

				profileNode.appendChild(partNode);
			}

			root.appendChild(profileNode);
		}

		doc.appendChild(root);
		OutputStream outputStream = new FileOutputStream(file);
		output.setByteStream(outputStream);
		output.setEncoding("UTF-8");
		domWriter.write(doc, output);
		outputStream.close();
	}

}
