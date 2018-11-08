package com.example.demo.xmlUtil;


import org.apache.commons.io.IOUtils;
import org.dom4j.*;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * 解析xml数据
 *
 * @author commuli
 * @date 2017年8月27日
 */
public class ParseXml {

    public List<Map<String, Object>> parseData(String str, Object obj) {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Document doc = null;
        try {
            // 转为可解析对象
            doc = DocumentHelper.parseText(str);
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        Map<String, Object> map = new HashMap<String, Object>();
        if (doc == null)
            return list;
        // 获取根节点
        Element rootElement = doc.getRootElement();
        // 转换map
        element2map(rootElement, map);
        list.add(map);
        return list;
    }

    public List<Map<String, Object>> parseData(InputStream in, Object obj) {
        return parseData(in, "utf-8", null);
    }

    public List<Map<String, Object>> parseData(InputStream in, String charsetName, Object obj) {
        String str = null;
        try {
            str = IOUtils.toString(in, charsetName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!StringUtils.isEmpty(str)) {
            return parseData(str, obj);
        }
        return null;
    }

    /**
     * xml元素转map
     *
     * @param elmt
     * @param map
     */
    private void element2map(Element elmt, Map<String, Object> map) {
        if (null == elmt) {
            return;
        }
        String name = elmt.getName();
        // 当前元素是最小元素
        if (elmt.isTextOnly()) {
            // 查看map中是否已经有当前节点
            Object f = map.get(name);
            // 用于存放元素属性
            Map<String, Object> m = new HashMap<String, Object>();
            // 遍历元素中的属性
            Iterator ai = elmt.attributeIterator();
            // 用于第一次获取该元素数据
            boolean aiHasNex = false;
            while (ai.hasNext()) {
                aiHasNex = true;
                // 拿到属性值
                Attribute next = (Attribute) ai.next();
                m.put(name + "." + next.getName(), next.getValue());
            }
            // 第一次获取该元素
            if (f == null) {
                // 判断如果有属性
                if (aiHasNex) {
                    // 将属性map存入解析map中
                    m.put(name, elmt.getText());
                    map.put(name, m);
                } else {
                    // 没有属性，直接存入相应的值
                    map.put(name, elmt.getText());
                }
            } else {
                // 解析map中已经有相同的节点
                // 如果当前值是list
                if (f instanceof List<?>) {
                    // list中添加此元素
                    m.put(name, elmt.getText());
                    ((List) f).add(m);
                } else {
                    // 如果不是，说明解析map中只存在一个与此元素名相同的对象
                    // 存放元素
                    List<Object> listSub = new ArrayList<Object>();
                    // 如果解析map中的值为string，说明第一个元素没有属性
                    if (f instanceof String) {
                        // 转换为map对象，
                        Map<String, Object> m1 = new HashMap<String, Object>();
                        m1.put(name, f);
                        // 添加到list中
                        listSub.add(m1);
                    } else {
                        // 否则直接添加值
                        listSub.add(f);
                    }
                    // 将当前的值包含的属性值放入list中
                    m.put(name, elmt.getText());
                    listSub.add(m);
                    // 解析map中存入list
                    map.put(name, listSub);
                }

            }
        } else {
            // 存放子节点元素
            Map<String, Object> mapSub = new HashMap<String, Object>();
            // 遍历当前元素的属性存入子节点map中
            attributeIterator(elmt, mapSub);
            // 获取所有子节点
            List<Element> elements = (List<Element>) elmt.elements();
            // 遍历子节点
            for (Element elmtSub : elements) {
                // 递归调用转换map
                element2map(elmtSub, mapSub);
            }
            // 当前元素没有子节点后 获取当前map中的元素名所对应的值
            Object first = map.get(name);
            if (null == first) {
                // 如果没有将值存入map中
                map.put(name, mapSub);
            } else {
                // 如果有，则为数组对象
                if (first instanceof List<?>) {
                    attributeIterator(elmt, mapSub);
                    ((List) first).add(mapSub);
                } else {
                    List<Object> listSub = new ArrayList<Object>();
                    listSub.add(first);
                    attributeIterator(elmt, mapSub);
                    listSub.add(mapSub);
                    map.put(name, listSub);
                }
            }
        }
    }

    /**
     * 遍历元素属性
     *
     * @param elmt
     * @param map
     */
    private void attributeIterator(Element elmt, Map<String, Object> map) {
        if (elmt != null) {
            Iterator ai = elmt.attributeIterator();
            while (ai.hasNext()) {
                Attribute next = (Attribute) ai.next();
                map.put(elmt.getName() + "." + next.getName(), next.getValue());
            }
        }
    }

    public static void main(String args[]) throws DocumentException {


        String str = " <recordTarget> <!-- 病人信息 --> <patientRole> <!-- 域ID --> <id root=\"1.2.156.112703.1.2.1.2\" extension=\"域ID\" /> <!-- 患者ID --> <id root=\"1.2.156.112703.1.2.1.3\" extension=\"患者ID\" /> <!-- 就诊号 --> <id root=\"1.2.156.112703.1.2.1.12\" extension=\"就诊号\" > <name>123456</name> </id> <!-- 病人基本信息 --> <patient> <!-- 病人名称 --> <name>病人名称</name> <!-- 性别编码/性别名称 --> <administrativeGenderCode code=\"性别编码\" codeSystem=\"1.2.156.112703.1.1.3\" displayName=\"性别名称\" /> <!-- 出生日期 --> <birthTime value=\"出生日期\" /> </patient> </patientRole> </recordTarget>";

        List<Map<String, Object>> ps = new ParseXml().parseData(str, null);
        for (Map<String, Object> m : ps) {

            Iterator<Map.Entry<String, Object>> it = m.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, Object> entry = it.next();
                System.out.println("key= " + entry.getKey() + " and value= " + entry.getValue());
            }
        }


    }
}