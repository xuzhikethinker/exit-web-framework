package org.exitsoft.showcase.common;

import java.util.Iterator;
import java.util.List;

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.exitsoft.showcase.common.enumeration.SystemDictionaryCode;
import org.exitsoft.showcase.entity.foundation.variable.DataDictionary;
import org.exitsoft.showcase.service.foundation.SystemVariableManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 系统变量工具类
 * 
 * @author vincent
 *
 */
@Component
public class SystemVariableUtils {
	
	static public String DEFAULT_DICTIONARY_VALUE = "无";
	
	static private SystemVariableManager systemVariableManager;
	
	@Autowired
	public void setSystemVariableManager(SystemVariableManager systemDictionaryManager) {
		SystemVariableUtils.systemVariableManager = systemDictionaryManager;
	}
	
	/**
	 * 为了能够借助Spring自动注入systemDictionaryManager这个Bean.写一个空方法借助@PostConstruct注解注入
	 */
	@PostConstruct
	public void init() {
		
	}

	/**
	 * 获取数据字典名称
	 * 
	 * @param systemDictionaryCode 类别代码
	 * @param value 值
	 * 
	 * @return String
	 */
	public static String getName(SystemDictionaryCode systemDictionaryCode,Object value) {
		
		if (value == null || systemDictionaryCode == null) {
			return DEFAULT_DICTIONARY_VALUE;
		}
		
		if (value instanceof String && StringUtils.isEmpty(value.toString())) {
			return DEFAULT_DICTIONARY_VALUE;
		}
		
		List<DataDictionary> dataDictionaries = systemVariableManager.getDataDictionariesByCategoryCode(systemDictionaryCode);
		
		for (Iterator<DataDictionary> iterator = dataDictionaries.iterator(); iterator.hasNext();) {
			DataDictionary dataDictionary = iterator.next();
			
			if (StringUtils.equals(dataDictionary.getValue(), value.toString())) {
				return dataDictionary.getName();
			}
		}
		return DEFAULT_DICTIONARY_VALUE; 
	}

	/**
	 * 通过字典类别代码获取数据字典集合
	 * 
	 * @param code 字典类别
	 * @param ignoreValue 忽略字典的值
	 * 
	 * @return List
	 */
	public static List<DataDictionary> getVariables(SystemDictionaryCode code, String... ignoreValue) {
		return systemVariableManager.getDataDictionariesByCategoryCode(code, ignoreValue);
	}
	
	/**
	 * 获取当前安全模型
	 * 
	 * @return {@link SessionVariable}
	 */
	public static SessionVariable getSessionVariable() {
		
		Subject subject = SecurityUtils.getSubject();
		
		if (subject != null && subject.getPrincipal() != null && subject.getPrincipal() instanceof SessionVariable) {
			return (SessionVariable) subject.getPrincipal();
		}
		
		return null;
	}
	
	/**
	 * 判断当前会话是否登录
	 * 
	 * @return boolean
	 */
	public static boolean isAuthenticated() {
		return SecurityUtils.getSubject().isAuthenticated();
	}
	
}
