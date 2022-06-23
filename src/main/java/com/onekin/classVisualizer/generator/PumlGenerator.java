package com.onekin.classVisualizer.generator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import net.sourceforge.plantuml.GeneratedImage;
import net.sourceforge.plantuml.SourceFileReader;

import com.onekin.classVisualizer.variantMiner.*;


public class PumlGenerator {
	
	private static void generatePng(File source) throws IOException {
		SourceFileReader reader = new SourceFileReader(source);
		List<GeneratedImage> list = reader.getGeneratedImages();
		// Ficheros generados
		File png = list.get(0).getPngFile();
		System.out.println(png.getPath());
	}
	
	public static ArrayList<String> getVariantsOfFolders(final File folder,ArrayList<String> folders){
		ArrayList<String> finalList = new ArrayList<String>();
		ArrayList<String> variables = new ArrayList<String>();
		ArrayList<File> carpetas = new ArrayList<File>();
		Iterator<String> it = folders.iterator();
		while(it.hasNext()) {
			String e = it.next();
			File f = new File(e);
			carpetas.add(f);
		}
		ArrayList<String> variantes = VariantProcessor.getAllVariants(folder);
		ArrayList<File> clases = classList.getClases(carpetas);
		Iterator<File> it2 = clases.iterator();
		while(it2.hasNext() && finalList.size()!=variantes.size()) {
			File f2 = it2.next();
			variables = VariantProcessor.getVariants(f2.getName(), folder);
			Iterator<String> wr2 = variables.iterator();
			 while(wr2.hasNext() && finalList.size()!=variantes.size())
			 {
				 String var = wr2.next();
				 if(!finalList.contains(var)) {
					 finalList.add(var);
				 }
				 
			 }
		}
		return finalList;
	}

	
	public static HashMap<File, ArrayList<String>> generateVariantDict(final File folder, ArrayList<String> variantes) {
		ArrayList<File> clases = classList.getClases(folder);
		HashMap<File, ArrayList<String>> list = new HashMap<File, ArrayList<String>>();
		ArrayList<String> variables = new ArrayList<String>();
		Iterator<File> wr = clases.iterator();
        File actual;
        String var;
		 while(wr.hasNext())
		 {
			 ArrayList<String> listaFinal = new ArrayList<String>();
			 variables.clear();
			 actual = wr.next();
			 variables = VariantProcessor.getVariants(actual.getName(), folder);
			 Iterator<String> wr2 = variables.iterator();
			 while(wr2.hasNext())
			 {
				 var = wr2.next();
				 if(variantes.contains(var)) {
					 var= var.split(".vdm")[0];
					 if(!listaFinal.contains(var)) {
						 listaFinal.add(var);
					 } 
				 }
				 
			 }
			 if(listaFinal.size()!=0) {
				 list.put(actual,listaFinal);
			 }
		 }
		 return list;
	}
	public static void createPumlByVariant2AndFeatureInfo(final File file, ArrayList<String> variantes,ArrayList<String> featureSelected,Boolean featureInfo, ArrayList<File> SelectedFolder) throws IOException {
		File puml = null;
		try {
			   if(featureInfo) {
				   puml = new File("src/main/webapp/static/img/diagramWithFunctionInfo.puml"); 
			   }
			   else {
				   puml = new File("src/main/webapp/static/img/diagramWithoutFunctionInfo.puml");
			   }
		      if (puml.createNewFile()) {
		        System.out.println("File created: " + puml.getName());
		      } else {
		        System.out.println("File already exists.");
		      }
		    } catch (IOException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
		FileWriter fw = new FileWriter(puml);
		HashMap<File, ArrayList<String>> list = generateVariantDict(file,variantes);
		//Main.leerHashMap2(list);
		ArrayList<File> clasesSelected = classList.getClases(SelectedFolder);
		//ArrayList<File> clasesSelected = SelectedFolder;
		fw.write("@startuml \n scale max 1080 width\r\n" + 
		"left to right direction \n"); 
		String var;
		String escritura = "";
		ArrayList<File> checked = new ArrayList<File>();
		ArrayList<File> imports = new ArrayList<File>();
        for (File ind : list.keySet()) {
        	if(clasesSelected.contains(ind)) {
	        	imports=classList.getImports(ind);
	        	if(!checked.contains(ind) || imports.size()>0) {
	        		escritura = getEscritura(ind,list,featureSelected,checked,featureInfo);
	        		ArrayList<String> lista = list.get(ind);
					 fw.write(escritura);
					 if (lista.size()==variantes.size()) {
						 fw.write(" #White");
					 }
					 else {
						 fw.write(" #Grey");
					 }
					 fw.write("\n");
					if(imports.size()>0) {
						 Iterator<File> wr3 = imports.iterator();
						 String importe;
						 while(wr3.hasNext())
						 {
							 File clase = wr3.next();
							 if(list.keySet().contains(clase)) {
								ArrayList<String> lista2 = list.get(clase);
								importe = getEscritura(clase,list,featureSelected,checked,featureInfo);
								fw.write(importe);
								if (lista2.size()==variantes.size()) {
									 fw.write(" #White");
								 }
								 else {
									 fw.write(" #Grey");
								 }
								fw.write("\n");
							 	fw.write(escritura);//AQUI HAY Q METER EL COLOR
							 	fw.write("-->");
							 	fw.write(importe);
							 	fw.write("\n");
							 }
						 } 
					 }
	        	}
        	}else {System.out.println("la clase "+ ind +"no se encuentra en la lista");}
    	 }
		 fw.write("@enduml"); 
		fw.close();
		generatePng(puml);
		System.out.println("He acabado");
	}

	private static String getEscritura(File clase, HashMap<File, ArrayList<String>> list, ArrayList<String> featureSelected, ArrayList<File> checked,Boolean featureInfo) {
		String escritura ="";
		String actual = clase.getName();
		String var ="";
		 //System.out.println("clase actual: " + clase);
		 escritura = ("[<b>"+actual+"</b>");
		 if(featureInfo) {
			 HashMap<String, ArrayList<String>> functions = classList.getFunctionWithFeatures(clase);
			 int functionCount = 0;
			 for (String function : functions.keySet()) {
				 if(functionCount==15) {
					 break;
				 }
				 ArrayList<String> features = functions.get(function);
				 System.out.println("La" + function + " tiene size " + features.size());
				 if(!function.equals("")) {
					 escritura = escritura + "\\n\\n<i>"+ function + "</i>";
					 Iterator<String> wr = features.iterator();
					 while(wr.hasNext())
					 {
						 var = wr.next();
						 //System.out.println("la " +function +"tiene la feature" + var);
						 if(featureSelected.contains(var)) {
						 //System.out.println("Estoy en la clase : " + actual +" Y en la funcion"+ function + " y he añadido la feature : " + var);
						 escritura = escritura + "\\n Uses Feature:" + var;
						 }
					 }
				 }
				 functionCount++;
			 } 
		 }
		 else {
			 System.out.println(clase);
			 ArrayList<String> features = classList.getFeatures(clase);
			 System.out.println("--------------------------------------"+features.size());
			 Iterator<String> wr = features.iterator();
			 while(wr.hasNext())
			 {
				 var = wr.next();
				 System.out.println("FINAL -> estoy procesando la feature: " +var);
				 if(featureSelected.contains(var)) {
					 System.out.println("LA AÑADO!!");
					 escritura = escritura +"\\n Feature:" + var;
				 }
				 else {
					 System.out.println("RESULTA QUE NO ESTA!");
				 }
			 }
		 }
		 escritura = escritura + "\\n";
		 ArrayList<String> lista = list.get(clase);
		 Iterator<String> wr2 = lista.iterator();
			 while(wr2.hasNext())
			 {
				 var = wr2.next();
				 var= var.split(".vdm")[0];
				 System.out.println("Estoy en la clase : " + actual + " y he añadido : " + var);
				 escritura = escritura + ("\\n Variant:" + var);
			 }
			 escritura = escritura + "]";
		return escritura;
	}
}

