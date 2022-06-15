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
import com.onekin.insideSpl.businessLogic.FeatureBLInterface;
import com.onekin.insideSpl.businessLogic.GenericBLInterface;
import com.onekin.insideSpl.businessLogic.ProductBLInterface;
import com.onekin.insideSpl.domain.Feature;
import com.onekin.insideSpl.domain.SPL;
import com.onekin.insideSpl.domain.VariantComponent;
import com.onekin.insideSpl.domain.VariantFeature;
import com.onekin.insideSpl.domain.VariantModel;
import com.onekin.insideSpl.utils.Triplet;

@Controller
@RequestMapping(value = "/puml")
public class PumlController {
	private static ArrayList<File> folders = new ArrayList<File>();
	@Autowired
	private ProductBLInterface productBL;
	
	@Autowired
	private FeatureBLInterface featureBL;

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
		if(products.size()==0) {
			System.out.println("tamano"+products.size());
			return "redirect:/";
		}
		ArrayList<String> list = new ArrayList<String>();
		Iterator<Entry<String, String>> it = allParams.entrySet().iterator();
		while(it.hasNext()) {
			Entry<String, String> entry = it.next();
			String e =entry.getKey();
			list.add(e);
		}
		File f = new File(path);
		ArrayList<String> prods = PumlGenerator.getVariantsOfFolders(f, list);
		model.addAttribute("splId",id);
		model.addAttribute("products",prods);
		model.addAttribute("folders",list);
		System.out.println(list.size());
		return "pumlView/productsSelection";
	}
	@PostMapping(value ="/caracteristics/{id}")
	public String caracteristicsSelectorWithProduct(Model model,@PathVariable(name = "id",required = true) String id,@RequestParam Map<String,String> allParams/*@RequestParam("splId") String id*/) {
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
	
	/*@RequestMapping(value ="/caracteristics/{id}")
	public String caracteristicsSelector(Model model, @PathVariable(name = "id",required = true) String id) {
		
		List<Feature> features = featureBL.getAllFeaturesOfSpl(id);
		if(features.size()==0) {
			return "redirect:/";
		}
		
		model.addAttribute("splId",id);
		model.addAttribute("features",features);
		
		return "pumlView/caracteristicsSelection";
	}*/
	@PostMapping(value ="/showDiagram/{id}")
	public String DiagramShower(Model model, @PathVariable(name = "id",required = true) String id,@RequestParam Map<String,String> allParams) throws IOException {
		Iterator<Entry<String, String>> it = allParams.entrySet().iterator();
		ArrayList<String> variants =  new ArrayList<String>();
		ArrayList<String> featureSelected = new ArrayList<String>();
		ArrayList<String> folders = new ArrayList<String>();
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
				folders.add(e);
			}
			else {
				System.out.println("HE AÑADIDO a features "+e);
				featureSelected.add(e);
			}
		}
		File f = new File(path);
		List<File> fList = classList.getFolders(f);
		PumlGenerator.createPumlByVariant2AndFeatureInfo(f, variants, featureSelected, false, (ArrayList<File>) fList);
		String fotopath ="src/main/webapp/static/img/diagramWithoutFunctionInfo.png";
		
		model.addAttribute("splId",id);
		model.addAttribute("path",fotopath);
		model.addAttribute("products2",variants);
		model.addAttribute("features2",featureSelected);
		model.addAttribute("folders2",folders);
		
		return "pumlView/showDiagram";
	}
	
	/*public static ArrayList<File> getFolders(final File folder) {
		for (final File fileEntry : folder.listFiles()) {
			if (fileEntry.isDirectory() && !fileEntry.toString().contains(".git")) {
				folders.add(fileEntry);
				System.out.println("añadida la carpeta : " + fileEntry);
				getFolders(fileEntry);
			}
		}
		// System.out.println(clases.size());
		return folders;
	}*/
	/*
	@RequestMapping(value ="/compare/{id1}/{id2}")
	public String compare(Model model, @PathVariable(name = "id1",required = true) String id1, @PathVariable(name = "id2",required = true) String id2) {
		
		if(MainController.session().getAttribute("selectedSplId") == null)
			return "redirect:/";
		
		VariantModel product1 = productBL.getProductById(id1);
		VariantModel product2 = productBL.getProductById(id2);
		
		if(product1 == null || product2 == null) {
			return "redirect:/";
		}
		
		Map<String, String> info = new HashMap<String, String>();
		
		VariantModel[] plist = {product1,product2};
		
		for(VariantModel p : plist ) {
			int smf = 0;
			int sof = 0;
			int tf = 0;
			
			for(VariantComponent vc : p.getVariants()) {
				if(vc instanceof VariantFeature) {
					if(vc.isSelected()) {
						VariantFeature vf = (VariantFeature) vc;
						if(vf.getFeature().getType().contentEquals("MANDATORY")) {
							smf++;
						}else {
							sof++;
						}
					}
					
					tf++;
				}
			}

			info.put(p.getId() + "_smf",Integer.toString(smf));
			info.put(p.getId() + "_sof",Integer.toString(sof));
			info.put(p.getId() + "_tf",Integer.toString(tf));
		}
		
		String comparationJson = productBL.compareProducts(product1,product2);
		
		model.addAttribute("prod1", product1);
		model.addAttribute("prod1data",productBL.getProductConfigurationJsonById(id1));
		
		model.addAttribute("prod2", product2);
		model.addAttribute("prod2data",productBL.getProductConfigurationJsonById(id2));
		
		model.addAttribute("info",info);
		model.addAttribute("similarityData", comparationJson);
		
		return "productView/compare";
	}*/

}
