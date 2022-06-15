package com.onekin.classVisualizer.variantMiner;

import java.io.File;
import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class VariantProcessor {
	private static ArrayList<String> variants = new ArrayList<String>();
	private static ArrayList<String> xfm = new ArrayList<String>();
	private static ArrayList<String> ccfm = new ArrayList<String>();
	private static File path;

	public static ArrayList<String> getAllVariants(final File folder)
	{
		String name = "";
		//variants.clear();
		for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	        	getAllVariants(fileEntry);
	        } else {
	        	name=fileEntry.getName();
	        	if(name.endsWith(".vdm")) {
	        		variants.add(fileEntry.getName());
		            //System.out.println("Variante : " + fileEntry.getName());
	        	} 
	        }
	    }
		return variants;
	}
	public static ArrayList<String> getXFM(final File folder) {
			String name = "";
			for (final File fileEntry : folder.listFiles()) {
		        if (fileEntry.isDirectory()) {
		        	getXFM(fileEntry);
		        } else {
		        	name=fileEntry.getName();
		        	if(name.endsWith(".xfm")) {
		        		xfm.add(fileEntry.getName());
			            //System.out.println("xfm : " + fileEntry.getName());
		        	} 
		        }
		    }
			return xfm;
		}
	public static ArrayList<String> getCCFM(final File folder) {
		String name = "";
		for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	        	getCCFM(fileEntry);
	        } else {
	        	name=fileEntry.getName();
	        	if(name.endsWith(".ccfm")) {
	        		ccfm.add(fileEntry.getName());
		            //System.out.println("xfm : " + fileEntry.getName());
	        	} 
	        }
	    }
		return ccfm;
	}
	public static File getVariantPath(final File folder,String vName) {
		String name = "";
		for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	        	path = getVariantPath(fileEntry,vName);
	        } else {
	        	name=fileEntry.getAbsolutePath();
	        	//System.out.println(name);
	        	if(name.contains(vName)) {
	        		//System.out.println("el fichero "+name +"se parece a " + vName);
	        		path = new File(name);
	        		break;
	        	} 
	        }
	    }
		//System.out.println(path);
		return path;
	}
	
	public static ArrayList<String> getClassByIDS(ArrayList<String> idlist,File file) {
		ArrayList<String> clases = new ArrayList<String>();
		ArrayList<String> class2 = new ArrayList<String>();
		ccfm.clear();
		ArrayList<String> ccfm = getCCFM(file);
		Iterator<String> wr = ccfm.iterator();
		 while(wr.hasNext())
		 {
			 String var = wr.next();
			 File inputFile = new File(file + "/"+ var);
			 class2 = getClassByIDS2(idlist,inputFile);
			 clases.addAll(class2);
		 }
		 return clases;
	}
	
	private static ArrayList<String> getClassByIDS2(ArrayList<String> idlist,File inputFile) {
		String clase = "";
		ArrayList<String> clases = new ArrayList<String>();
		try {
	         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	         Document doc = dBuilder.parse(inputFile);
	         doc.getDocumentElement().normalize();
	         NodeList nList = doc.getElementsByTagName("cm:element");
	         for (int temp = 0; temp < nList.getLength(); temp++) {
	            Node nNode = nList.item(temp);
	            Element e = (Element)nNode;
	            String atributo1 = e.getAttribute("cm:id");
	            //System.out.println("atributo :" + atributo1);
	            if (idlist.contains(atributo1)){
	            	Integer i = 0;
	            	Boolean checked = false;
	            	while(!checked) {
	            		Node nChild = nNode.getChildNodes().item(i);
	            		//System.out.println("nodename: " + nChild.getNodeName());
	            		if(nChild.getNodeName().equals("cm:vname")) {
	            			clase = nChild.getChildNodes().item(1).getTextContent();
	            			if(clase.contains(".js")) {
	            				//System.out.println("clase añadida " + clase);
		            			clases.add(clase);
	            			}
	            			checked = true;
	            		}
	            		i++;
	            	}
	            }
	         }
	      } catch (Exception e) {
	         e.printStackTrace();
	      }
		//System.out.println("La clase del id : " + id + " es : "  + clase);
		return clases;
	}
	
	
		
	//CHECKED
	//Dada una clase devuelve su id para comprobar despues si una variante tiene esa id.
	public static String getClassByID(String id,File file) {
		ArrayList<String> xfm = getCCFM(file);
		Iterator<String> wr = xfm.iterator();
		String name = "";
		String clase = "";
		 while(wr.hasNext())
		 {
			 String var = wr.next();
			 File inputFile = new File(file + "/"+ var);
			 name = getClassByID2(id,inputFile);
			 //System.out.println(name);
			 if(!name.equals("")) {
				 clase = name;
			 }
		 }
		return clase;
	}
	private static String getClassByID2(String id,File inputFile) {
		String clase = "";
		try {
	         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	         Document doc = dBuilder.parse(inputFile);
	         doc.getDocumentElement().normalize();
	         NodeList nList = doc.getElementsByTagName("cm:element");
	         for (int temp = 0; temp < nList.getLength(); temp++) {
	            Node nNode = nList.item(temp);
	            Element e = (Element)nNode;
	            String atributo1 = e.getAttribute("cm:id");
	            //System.out.println("atributo :" + atributo1);
	            if (atributo1.equals(id)){
	            	Integer i = 0;
	            	
	            	Boolean checked = false;
	            	while(!checked) {
	            		Node nChild = nNode.getChildNodes().item(i);
	            		//System.out.println("nodename: " + nChild.getNodeName());
	            		if(nChild.getNodeName().equals("cm:vname")) {
	            			clase = nChild.getChildNodes().item(1).getTextContent();
	            			checked = true;
	            		}
	            		i++;
	            	}
	            }
	         }
	      } catch (Exception e) {
	         e.printStackTrace();
	      }
		//System.out.println("La clase del id : " + id + " es : "  + clase);
		return clase;
	}
	//INCHECK->Funciona PERO ES MEJOR HACER QUE COJA TODAS LAS IDS Y LUEGO recorrer los xfm para ver si cada feature esta contenida en la lista de ids.
	public static void getFeaturesOfVariant(String Variant,File file,ArrayList<String> list){
		File var = new File(file+"/Variants");
		File inputFile = getVariantPath(var, Variant);
		try {
	         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	         Document doc = dBuilder.parse(inputFile);
	         doc.getDocumentElement().normalize();
	         NodeList nList = doc.getElementsByTagName("cm:element");
	         //System.out.println("hola");
	         for (int temp = 0; temp < nList.getLength(); temp++) {
	            Node nNode = nList.item(temp);
	            Element e = (Element)nNode;
	            String atributo1 = e.getAttribute("cm:type");
	            if (atributo1.equals("ps:selected")){
	            	Node nChild = null;
	            	String id = "";
	            	try {
	            		nChild = nNode.getChildNodes().item(1);
	            		id = nChild.getTextContent().split("/")[1];
	            	}catch(Exception h){
	            		nChild = nNode.getChildNodes().item(0);
	            		id = nChild.getTextContent().split("/")[1];
	            	}
	            	id = id.split("\n")[0];
	            	if(id.length()==17) {
	            		System.out.println("PROCEDO A BUSCAR EL FEATURE DE ID " +id);
	            		String feature = getFeatureByID(id,file);
	            		if(!list.contains(feature) && !feature.equals("")) {
		            		System.out.println("-----------------------------------");
		            		System.out.println("FEATURE " +feature);
		            		System.out.println("-----------------------------------");
		            		list.add(feature);
		            	}
	            	}
	            }
	         }
	      } catch (Exception e) {
	         e.printStackTrace();
	      }
		System.out.println("la lista tiene "+list.size()+ "elementos");
	}
	//CHECKED
	public static ArrayList<String> getFeaturesOfVariants(ArrayList<String> variants,File file){
		ArrayList<String> features = new ArrayList<String>();
		Iterator<String> wr = variants.iterator();
		 while(wr.hasNext())
		 {
			 String var = wr.next();
			 getFeaturesOfVariant(var,file,features);
		 }
		 //System.out.println("el size es de : "+features.size());
		return features;
	}
	
	public static String getFeatureByID(String id,File file) {
		xfm.clear();
		ArrayList<String> xfm = getXFM(file);
		Iterator<String> wr = xfm.iterator();
		String name = "";
		String clase = "";
		 while(wr.hasNext())
		 {
			 String var = wr.next();
			 File inputFile = new File(file + "/"+ var);
			 name = getFeatureByID2(id,inputFile);
			 //System.out.println(name);
			 if(!name.equals("")) {
				 clase = name;
			 }
		 }
		return clase;
	}
	
	//INCHECK->FUNCIONA PERO SE PUEDE OPTIMIZAR
	private static String getFeatureByID2(String id, File inputFile) {
		String feature = "";
		try {
	         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	         Document doc = dBuilder.parse(inputFile);
	         doc.getDocumentElement().normalize();
	         NodeList nList = doc.getElementsByTagName("cm:element");
	         for (int temp = 0; temp < nList.getLength(); temp++) {
	            Node nNode = nList.item(temp);
	            Element e = (Element)nNode;
	            String atributo1 = e.getAttribute("cm:id");
	            String featureGiven = e.getAttribute("cm:name");
	            //System.out.println("atributo :" + atributo1);
	            if (atributo1.equals(id)){
	            	feature = featureGiven;
	            	break;
	            }
	         }
	      } catch (Exception e) {
	         e.printStackTrace();
	      }
		//System.out.println("La clase del id : " + id + " es : "  + clase);
		return feature;
	}
	//CHECKED
	public static ArrayList<String> getClasses(String Variant,File file){
		ArrayList<String> clases = new ArrayList<String>();
		ArrayList<String> ID = new ArrayList<String>();
		File var = new File(file+"/Variants");
		File inputFile = getVariantPath(var, Variant);
		try {
	         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	         Document doc = dBuilder.parse(inputFile);
	         doc.getDocumentElement().normalize();
	         NodeList nList = doc.getElementsByTagName("cm:element");
	         //System.out.println("hola");
	         for (int temp = 0; temp < nList.getLength(); temp++) {
	            Node nNode = nList.item(temp);
	            Element e = (Element)nNode;
	            String atributo1 = e.getAttribute("cm:type");
	            if (atributo1.equals("ps:selected")){
	            	Node nChild = null;
	            	String id = "";
	            	try {
	            		nChild = nNode.getChildNodes().item(1);
	            		id = nChild.getTextContent().split("/")[1];
	            	}catch(Exception h){
	            		nChild = nNode.getChildNodes().item(0);
	            		id = nChild.getTextContent().split("/")[1];
	            	}
	            	id = id.split("\n")[0];
	            	//System.out.println("nodename: " +id);
	            	ID.add(id);
	            	/*String clase = getClassByID(id,file);
	            	if(clase.endsWith(".js")) {
	            		clases.add(clase);
		            	System.out.println("clase añadida: " + clase);
	            	}*/
	            }
	         }
	      } catch (Exception e) {
	         e.printStackTrace();
	      }
		return getClassByIDS(ID,file);
	}
	//CHECKED
	public static ArrayList<String> getVariants(String clase,final File folder) {
		File variantFol = new File(folder+"/Variants/");
		variants.clear();
		ArrayList<String> variantes = getAllVariants(variantFol);
		ArrayList<String> clases = new ArrayList<String>();
		ArrayList<String> variants = new ArrayList<String>();
		Iterator<String> wr = variantes.iterator();
        String actual;
		 while(wr.hasNext())
		 {
			 actual = wr.next();
			 clases = getClasses(actual,folder);
			 if(clases.contains(clase)) {
				 //System.out.println("la variante" +actual+ " esta en la clase " + clase);
				 variants.add(actual);
			 }
		 }
		 System.out.println(variants.size());
		return variants;
	}
	//checked
		public static ArrayList<String> getFeatures(final File folder) {
			ArrayList<String> features = new ArrayList<String>();
			ArrayList<String> features2 = new ArrayList<String>();
			xfm.clear();
			ArrayList<String> xfm = getXFM(folder);
			Iterator<String> wr = xfm.iterator();
			 while(wr.hasNext())
			 {
				 String var = wr.next();
				 File inputFile = new File(folder + "/"+ var);
				 features2 = getFeatures2(inputFile);
				 //System.out.println(name);
				 features.addAll(features2);
			 }
			return features;
		}
	
	//checked
	public static ArrayList<String> getFeatures2(final File inputFile) {
		ArrayList<String> features = new ArrayList<String>();
		try {
	         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	         Document doc = dBuilder.parse(inputFile);
	         doc.getDocumentElement().normalize();
	         NodeList nList = doc.getElementsByTagName("cm:element");
	         for (int temp = 0; temp < nList.getLength(); temp++) {
	            Node nNode = nList.item(temp);
	            Element e = (Element)nNode;
	            String atributo1 = e.getAttribute("cm:name");
	            features.add(atributo1);
	         }
	      } catch (Exception e) {
	         e.printStackTrace();
	      }
		return features;
	}
	//UNUSED
	public static ArrayList<String> getClassOfFeature(String clase,final File folder){
		ArrayList<String> clases = new ArrayList<String>();
		File inputFile = new File(folder+"/WS.ccfm");
		try {
	         DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	         DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
	         Document doc = dBuilder.parse(inputFile);
	         doc.getDocumentElement().normalize();
	         NodeList nList = doc.getElementsByTagName("cm:element");
	         for (int temp = 0; temp < nList.getLength(); temp++) {
	            Node nNode = nList.item(temp);
	            Element e = (Element)nNode;
	            String atributo1 = e.getAttribute("cm:name");
	         }
	      } catch (Exception e) {
	         e.printStackTrace();
	      }
		return clases;
	}
}
