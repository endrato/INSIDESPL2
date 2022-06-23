package com.onekin.classVisualizer.generator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;

public class classList {
	private static ArrayList<File> clases = new ArrayList<File>();
	private static ArrayList<File> folders = new ArrayList<File>();

	public static ArrayList<File> getClases(final File folder) {
		String name = "";
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()) {
				getClases(fileEntry);
			} else {
				name = fileEntry.getName();
				if (name.endsWith(".js")) {
					clases.add(fileEntry);
					System.out.println("añadido : " + fileEntry);
				}
			}
		}
		// System.out.println(clases.size());
		return clases;
	}
	public static ArrayList<File> getFolders(final File folder) {
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory() && !fileEntry.toString().contains(".git")) {
				folders.add(fileEntry);
				System.out.println("añadida la carpeta : " + fileEntry);
				getFolders(fileEntry);
			}
		}
		// System.out.println(clases.size());
		return folders;
	}
	public static ArrayList<File> getFoldersByLevel(final File folder,int currentLevel,int maxLevel) {
		currentLevel++;
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory() && !fileEntry.toString().contains(".git")) {
				folders.add(fileEntry);
				System.out.println("añadida la carpeta : " + fileEntry);
				if(currentLevel!=maxLevel) {
					getFoldersByLevel(fileEntry,currentLevel,maxLevel);
				}
			}
		}
		// System.out.println(clases.size());
		return folders;
	}
	public static int getFoldersMaxLevel(final File folder,int currentLevel) {
		int MaxLevel = currentLevel;
		currentLevel++;
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory()&& !fileEntry.toString().contains(".git")) {
				int level = getFoldersMaxLevel(fileEntry,currentLevel);
				if(MaxLevel < level) {
					MaxLevel =level;
				}
			}
		}
		// System.out.println(clases.size());
		return MaxLevel;
	}
	public static ArrayList<File> getClases(ArrayList<File> carpetas) {
		String name = "";
		Iterator<File> wr = carpetas.iterator();
		ArrayList<File> clases = new ArrayList<File>();
		while(wr.hasNext())
		 {
			File var = wr.next();
			for (final File fileEntry : var.listFiles()) {
				if (fileEntry.isDirectory()) {
				} else {
					name = fileEntry.getName();
					if (name.endsWith(".js")) {
						clases.add(fileEntry);
					}
				}
			}
		 }
		// System.out.println(clases.size());
		return clases;
	}
	public static ArrayList<String> getClasesOf1Folder(File carpeta) {
		String name = "";
		ArrayList<String> clases = new ArrayList<String>();
			File var = carpeta;
			for (final File fileEntry : var.listFiles()) {
				if (fileEntry.isDirectory()) {
				} else {
					name = fileEntry.getName();
					if (name.endsWith(".js")) {
						clases.add(fileEntry.getName());
					}
				}
			}
		// System.out.println(clases.size());
		return clases;
	}

	public static ArrayList<String> getFeatures(final File file)
	{
		ArrayList<String> features = new ArrayList<String>();
		try {   
			FileInputStream fis=new FileInputStream(file);       
			Scanner sc=new Scanner(fis); 
			while(sc.hasNextLine())  {
				String linea =sc.nextLine();
				if(linea.contains("PVSCL:IFCOND")) {
					String feature = linea.split("\\(")[1];
					String feature1 = "";
					feature = feature.split("\\)")[0];
					feature = feature.split(", LINE")[0];
					if(feature.contains(" or ")) {
						feature1 = feature.split(" or ")[0];
						feature = feature.split(" or ")[1];
					}
					if(!features.contains(feature)) {
						features.add(feature);
						System.out.println("He añadido la feature: " +feature);
					}
					if(!features.contains(feature1) && !feature1.equals("")) {
						features.add(feature1);
						System.out.println("He añadido la feature: " +feature1);
					}
				}
			}  
			sc.close();
		}  
		catch(IOException e)  
		{  
		e.printStackTrace();  
		}  
	return features;
	}
	public static HashMap<String, ArrayList<String>> getFunctionWithFeatures(final File file)
	{
		ArrayList<String> features = new ArrayList<String>();
		String funcion ="";
		HashMap<String, ArrayList<String>> list = new HashMap<String, ArrayList<String>>();
		try {   
			FileInputStream fis=new FileInputStream(file);       
			Scanner sc=new Scanner(fis);
			String feature = "";
			String feature2="";
			while(sc.hasNextLine())  {  
				String linea =sc.nextLine();
				if(linea.contains("function ") && !linea.contains("*") && !linea.contains("/")) {
					if(!funcion.equals("")) {
						System.out.println("Features mide ahora : " + features.size());
						list.put(funcion, features);
					}
					System.out.println("function precorte -> " + linea);
					System.out.println("funcion2 precorte-> " + linea.split("function ")[0]);
					funcion = linea.split("function ")[1];
					System.out.println("tras el corte queda " +funcion);
					funcion = funcion.replaceAll("[(){]", "");
					System.out.println("y tras el segundo corte queda " + funcion);
					features = new ArrayList<String>();
					System.out.println("Acabo de limpiar la lista y estoy trabajando con : " + funcion);
					System.out.println("Tras limpiar la lista, esta mide: " + features.size());
				}
				if(linea.contains("PVSCL:IFCOND")) {
					feature = linea.split("\\(")[1];
					feature = feature.split("\\)")[0];
					if(feature.contains(" or ")) {
						feature2 = feature.split(" or ")[0];
						feature = feature.split(" or ")[1];							
					}
				}
				if(linea.contains("PVSCL:ENDCOND")) {
					if(!features.contains(feature)) {
						features.add(feature);
						System.out.println("He añadido la feature: " +feature + " a la function : " +funcion);
					}
					if(!features.contains(feature2)) {
						features.add(feature2);
					}
				}
			}
			if(!funcion.equals("")) {
				System.out.println("Features mide ahora : " + features.size());
				list.put(funcion, features);
			}
			sc.close();
		}  
		catch(IOException e)  
		{  
		e.printStackTrace();  
		}
	list.put(funcion, features);
	return list;
	}
	public static ArrayList<File> getImports(File file){//Falta añadir que el import pueda ser .. en vez de .
		ArrayList<File> imports = new ArrayList<File>();
		Boolean encontrado = false;
		Boolean acabado = false;
		try {   
			FileInputStream fis=new FileInputStream(file);       
			Scanner sc=new Scanner(fis); 
			while(sc.hasNextLine() && !acabado)  {  
				String linea =sc.nextLine();
				if(linea.contains("import")) {
					encontrado = true;
					System.out.println("estoy en el fichero " + file);
					try {
					String importe = linea.split("'")[1];
					importe = importe.split("'")[0];
					File importfile = file;
						while(importe.contains("..")) {
							System.out.println("el importe es " + linea);
							importfile = importfile.getParentFile();
							System.out.println("Ahora estoy en " + importfile);
							importe = importe.split("..",2)[1];
						}
						String directorio = importfile.getParent();
						System.out.println("el directorio es " +directorio);
						System.out.println("antes del corte el import es " + importe);
						importe = importe.split("/",2)[1];
						File archivo = new File(directorio+"/"+importe+".js");
						if(!imports.contains(archivo) && archivo.exists()) {
							imports.add(archivo);
							System.out.println("He añadido el import: " +archivo);
						}	
					}
					catch(Exception e) {
						System.out.println("TOMA FALLO BRO");
					}
				}
				else {
					//System.out.println("esta linea no tiene import :" +linea);
					if(encontrado || linea.contains("function")) {
						acabado=true;
						System.out.println("acabe");
					}
				}
			}  
			sc.close();
		}  
		catch(IOException e)  
		{  
		e.printStackTrace();  
		}  
	return imports;
	}
}