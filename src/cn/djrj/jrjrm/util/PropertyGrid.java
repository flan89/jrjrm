package cn.djrj.jrjrm.util;

/**
 * 辅助类 用户构建符合Jquery EasyUi 中，
 * 	propertygrid插件所需要的数据类型
 * @author lynn
 */
public class PropertyGrid {
	
	private String name;
	private Object value;
	private String group;
	private Object editor;
	
	public PropertyGrid() {}
	
	public PropertyGrid(String name, Object value, String group, Object editor) {
		this.name = name;
		this.value = value;
		this.group = group;
		this.editor = editor;
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}
	public String getGroup() {
		return group;
	}
	public void setGroup(String group) {
		this.group = group;
	}
	public Object getEditor() {
		return editor;
	}
	public void setEditor(Object editor) {
		this.editor = editor;
	}
	
}
