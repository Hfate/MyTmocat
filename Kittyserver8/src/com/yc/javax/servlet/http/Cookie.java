package com.yc.javax.servlet.http;

public class Cookie {
	private String name;
	private String value;
	
	private String domain;
	private int maxAge;
	private String path="/";
	
	public Cookie(String name,String value){
		super();
		this.name=name;
		this.value=value;
	}
	
	@Override
	public String toString() {
		return name+"="+value+"; \r";
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getDomain() {
		return domain;
	}
	public void setDomain(String domain) {
		this.domain = domain;
	}
	public int getMaxAge() {
		return maxAge;
	}
	public void setMaxAge(int maxAge) {
		this.maxAge = maxAge;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	
	
}
