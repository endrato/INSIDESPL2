package com.onekin.insideSpl.domain;

import java.util.ArrayList;

public class Folder {
	
	// Attributes
		private Integer id;
		private String name;
		private ArrayList<String> files = new ArrayList<String>();

		public Folder(String name,Integer id) {
			this.id = id;
			this.name = name;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}
		
		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public ArrayList<String> getFiles() {
			return files;
		}

		public void setFiles(ArrayList<String> files) {
			this.files = files;
		}
		public void addToFiles(String file) {
			files.add(file);
		}

}
