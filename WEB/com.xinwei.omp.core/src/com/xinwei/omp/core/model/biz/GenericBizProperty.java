
package com.xinwei.omp.core.model.biz;

/**
 * 
 * 通用业务属性
 * 
 * @author chenjunhua
 * 
 */

public class GenericBizProperty implements java.io.Serializable {

	// 属性名称
	private String name;

	// 属性类型
	private Class type;

	// 属性值
	private Object value;

	public GenericBizProperty() {

	}

	public GenericBizProperty(String name, Object value) {
		super();
		this.name = name;
		this.value = value;
	}

	public GenericBizProperty(String name, Class type, Object value) {
		this.name = name;
		this.type = type;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public Class getType() {
		return type;
	}

	public Object getValue() {
		return value;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setType(Class type) {
		this.type = type;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GenericBizProperty other = (GenericBizProperty) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		// if (type == null) {
		// if (other.type != null)
		// return false;
		// } else if (!type.equals(other.type))
		// return false;
		try {
			if (value == null) {
				if (other.value != null)
					return false;
			} else {
				if (other.value == null)
					return false;
				if (value instanceof String) {
					if (!value.toString().equals(other.value.toString())) {
						return false;
					}
				} else if (value instanceof Long) {
					if (Long.valueOf(value.toString()).longValue() != (Long
							.valueOf(other.value.toString()).longValue())) {
						return false;
					}
				} else if (value instanceof Integer) {
					if (Integer.valueOf(value.toString()).intValue() != (Integer
							.valueOf(other.value.toString()).intValue()))
						return false;
				} else if (value instanceof Double) {
					if (Double.valueOf(value.toString()).doubleValue() != (Double
							.valueOf(other.value.toString()).doubleValue()))
						return false;
				} else {
					return String.valueOf(value).equals(
							String.valueOf(other.value));
				}
			}
		} catch (Exception e) {
			Throwable t = new Throwable(e);
			t.printStackTrace();
		}
		return true;
	}
}
