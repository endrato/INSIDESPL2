package com.onekin.insideSpl.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.onekin.classVisualizer.generator.PumlGenerator;
import com.onekin.classVisualizer.generator.classList;
import com.onekin.classVisualizer.variantMiner.VariantProcessor;
import com.onekin.insideSpl.businessLogic.ProductBLInterface;
import com.onekin.insideSpl.domain.Folder;
import com.onekin.insideSpl.domain.VariantModel;

@Controller
@RequestMapping(value = "/puml")
public class PumlController {
	@Autowired
	private ProductBLInterface productBL;

	@RequestMapping(value = "")
	public String index(Model model) {
		
		if(MainController.session().getAttribute("selectedSplId") == null)
			return "redirect:/";
		
		String splId = (String) MainController.session().getAttribute("selectedSplId");
		
		//String path = pumlBL.getPath(splId);
		
		//model.addAttribute("path", path);
		model.addAttribute("splId",splId);
		
		return "pumlView/index";
	}
	@RequestMapping(value ="/folderLevel/{id}")
	public String folderLevel(Model model, @PathVariable(name = "id",required = true) String id) {
		
		System.out.println(id);
		String path = null;
		try {
			List<VariantModel> products = productBL.orderBySelectedFeaturesSize(productBL.getAllProductsOfSpl(id));
			path = products.get(0).getPath().split("/Variants")[0];
		}catch(NullPointerException e) {
			e.printStackTrace();
		}
		
		if(path == null) {
			return "redirect:/";
		}
		File f = new File(path);
		Integer maxLevel = classList.getFoldersMaxLevel(f, 0);
		Integer i = 0;
		ArrayList<Integer> list = new ArrayList<Integer>();
		while(i<maxLevel) {
			i++;
			list.add(i);
		}
		model.addAttribute("splId",id);
		model.addAttribute("maxLevelList",list);
		return "pumlView/folderLevel";
	}
	@RequestMapping(value ="/folder/{id}")
	public String folder(Model model, @PathVariable(name = "id",required = true) String id,@RequestParam int level) {
		
		System.out.println(id);
		String path = null;
		try {
			List<VariantModel> products = productBL.orderBySelectedFeaturesSize(productBL.getAllProductsOfSpl(id));
			path = products.get(0).getPath().split("/Variants")[0];
		}catch(NullPointerException e) {
			e.printStackTrace();
		}
		
		if(path == null) {
			return "redirect:/";
		}
		model.addAttribute("splId",id);
		model.addAttribute("path",path);
		File f = new File(path);
		List<File> fList = classList.getFoldersByLevel(f,0,level);
		model.addAttribute("folders",fList);
		
		return "pumlView/folderSelection";
	}
	@RequestMapping(value ="/products/{id}")
	public String productSelector(Model model, @PathVariable(name = "id",required = true) String id,@RequestParam Map<String,String> allParams) {
		
		List<VariantModel> products = productBL.orderBySelectedFeaturesSize(productBL.getAllProductsOfSpl(id));
		String path = products.get(0).getPath().split("/Variants")[0];
		//ArrayList<File> folders2 = new ArrayList<File>();
		if(products.size()==0) {
			System.out.println("tamano"+products.size());
			return "redirect:/";
		}
		ArrayList<Folder> list = new ArrayList<Folder>();
		ArrayList<String> list2 = new ArrayList<String>();
		Integer i = 0;
		Iterator<Entry<String, String>> it = allParams.entrySet().iterator();
		while(it.hasNext()) {
			i++;
			Entry<String, String> entry = it.next();
			String e =entry.getKey();
			Folder f = new Folder(e,i);
			File f1 = new File(e);
			f.setFiles(classList.getClasesOf1Folder(f1));
			list.add(f);
			list2.add(e);
			//folders2.add(f1);
		}
		File f = new File(path);
		ArrayList<String> prods = PumlGenerator.getVariantsOfFolders(f, list2);
		//List<File> fList = classList.getClases(folders2);
		model.addAttribute("splId",id);
		//model.addAttribute("files",fList);
		model.addAttribute("products",prods);
		model.addAttribute("folders",list);
		return "pumlView/productsSelection";
	}
	@PostMapping(value ="/caracteristics/{id}")
	public String caracteristicsSelectorWithProduct(Model model,@PathVariable(name = "id",required = true) String id,@RequestParam Map<String,String> allParams) {
		ArrayList<String> listProducts = new ArrayList<String>();
		ArrayList<String> folders = new ArrayList<String>();
		Iterator<Entry<String, String>> it = allParams.entrySet().iterator();
		while(it.hasNext()) {
			Entry<String, String> entry = it.next();
			String e =entry.getKey();
			if(e.contains(".vdm")) {
				listProducts.add(e);
			}
			else {
				folders.add(e);
			}
		}
		List<VariantModel> products = productBL.orderBySelectedFeaturesSize(productBL.getAllProductsOfSpl(id));
		String path = products.get(0).getPath().split("/Variants")[0];
		File f = new File(path);
		List<String> features = VariantProcessor.getFeaturesOfVariants(listProducts, f);
		if(features.size()==0) {
			return "redirect:/";
		}
		
		model.addAttribute("splId",id);
		model.addAttribute("products",listProducts);
		model.addAttribute("features",features);
		model.addAttribute("folders",folders);
		
		return "pumlView/caracteristicsSelection";
	}
	@PostMapping(value ="/showDiagram/{id}")
	public String DiagramShower(Model model, @PathVariable(name = "id",required = true) String id,@RequestParam Map<String,String> allParams) throws IOException {
		Iterator<Entry<String, String>> it = allParams.entrySet().iterator();
		ArrayList<String> variants =  new ArrayList<String>();
		ArrayList<String> featureSelected = new ArrayList<String>();
		ArrayList<String> folders = new ArrayList<String>();
		ArrayList<File> folders2 = new ArrayList<File>();
		List<VariantModel> products = productBL.orderBySelectedFeaturesSize(productBL.getAllProductsOfSpl(id));
		String path = products.get(0).getPath().split("/Variants")[0];
		while(it.hasNext()) {
			Entry<String, String> entry = it.next();
			String e =entry.getKey();
			if(e.contains("vdm")) {
				System.out.println("HE AÑADIDO a variants "+e);
				variants.add(e);
			}
			else if(e.contains("\\") || e.contains("/")) {
				File f1 = new File(e);
				folders2.add(f1);
				folders.add(e);
				System.out.println("He añadido a folders" + e);
			}
			else {
				System.out.println("HE AÑADIDO a features "+e);
				featureSelected.add(e);
			}
		}
		File f = new File(path);
		//List<File> fList = classList.getClases(folders2);
		//System.out.println(fList.size() + "--------------------");
		PumlGenerator.createPumlByVariant2AndFeatureInfo(f, variants, featureSelected, false, folders2);
		String fotopath ="src/main/webapp/static/img/diagramWithoutFunctionInfo.png";
		
		model.addAttribute("splId",id);
		model.addAttribute("path",fotopath);
		model.addAttribute("products2",variants);
		model.addAttribute("features2",featureSelected);
		model.addAttribute("folders2",folders);
		
		return "pumlView/showDiagram";
	}
	

}
