package com.upn.entidades;

import java.util.ArrayList;

public class JsTree {
	private String id;
	private String text;
	private String parent;
	private String icon;
	private ArrayList<JsTree> children;
	private JsTreeState state;
	private JsTreeAttr a_attr;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	
	public String getIcon() {
		return icon;
	}
	public void setIcon(String icon) {
		this.icon = icon;
	}
	public String getParent() {
		return parent;
	}
	public void setParent(String parent) {
		this.parent = parent;
	}
	public ArrayList<JsTree> getChildren() {
		return children;
	}
	public void setChildren(ArrayList<JsTree> children) {
		this.children = children;
	}
	public JsTreeState getState() {
		return state;
	}
	public void setState(JsTreeState state) {
		this.state = state;
	}
	public JsTreeAttr getA_attr() {
		return a_attr;
	}
	public void setA_attr(JsTreeAttr a_attr) {
		this.a_attr = a_attr;
	}
}
