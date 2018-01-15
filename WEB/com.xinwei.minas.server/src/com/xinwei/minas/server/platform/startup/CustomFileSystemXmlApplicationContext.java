package com.xinwei.minas.server.platform.startup;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.support.FileSystemXmlApplicationContext;


/**
 * Comments : 加载spring配置文件类，主要用于设置AllowBeanDefinitionOverriding属性为false
 * 避免加载同名bean
 * Author : yanghongliang
 * Create Date : 2012-4-20
 * Modification history : 
 *   Sr Date Modified By Why & What is modified
 * <pre>如：1. 2007.1.1 zhangsan  修改了addUser方法</pre>
 * 
 * @version 
 */
public class CustomFileSystemXmlApplicationContext extends FileSystemXmlApplicationContext
{
	/**
	 * @param springConfigFile
	 */
	public CustomFileSystemXmlApplicationContext(String springConfigFile)
	{
		super(springConfigFile);
	}
	
	/* (non-Javadoc)
	 * @see org.springframework.context.support.AbstractRefreshableApplicationContext#customizeBeanFactory(org.springframework.beans.factory.support.DefaultListableBeanFactory)
	 */
	protected void customizeBeanFactory(DefaultListableBeanFactory beanFactory) {
		super.customizeBeanFactory(beanFactory);
		beanFactory.setAllowBeanDefinitionOverriding(false);
	}
}
