package com.example.dataobject;

import java.io.Serializable;

public class PrintDO implements Serializable {

	private long id;
	private String title;
	private String description;
	private String imgsrc;
	private float price;
	private int quan;
	private static final long serialVersionUID = 1L;

	public PrintDO() {
		super();
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImgsrc() {
		return imgsrc;
	}

	public void setImgsrc(String imgsrc) {
		this.imgsrc = imgsrc;
	}

	public float getPrice() {
		return this.price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public int getQuan() {
		return this.quan;
	}

	public void setQuan(int quan) {
		this.quan = quan;
	}

	@Override
	public String toString() {
		return "PrintDO [id=" + id + ", title=" + title + ", description=" + description + ", imgsrc=" + imgsrc
				+ ", price=" + price + ", quan=" + quan + "]";
	}

	
}
