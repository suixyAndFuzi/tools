package com.example.demo.xmlUtil;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import java.util.List;

public class Dom4jUtil {


    public static void main(String args[])  throws Exception{
        String str = "111";
        System.out.println("路径1："+str);//父节点名称
        aaa(str);
        System.out.println("路径3："+str);//父节点名称
    }
    public static void aaa(String str){
        str = str +"/"+ "222";
        System.out.println("路径2："+str);//父节点名称
    }

    /**
     * 获取文件的document对象，然后获取对应的根节点
     * @author chenleixing
     */
//    public static void main(String args[])  throws Exception{
//        String str = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> <root> <user editor=\"chenleixing\" date=\"2015-02-15\"> <name editor=\"zhangxiaochao\">张三<a>456</a></name> <year>24</year> <sex>男</sex> </user> <user editor=\"zhangxiaochao\" date=\"2015-02-15\"> 123 <name>李四</name> <year>24</year> <sex>女</sex> </user> </root> ";
//        Document  document = DocumentHelper.parseText(str);
//        Element root=document.getRootElement();//获取根节点
//        getNodes(root,"0","");//从根节点开始遍历所有节点
//    }

    /**
     * 从指定节点开始,递归遍历所有子节点
     * @author chenleixing
     */
    public static void getNodes(Element node,String pNode,String path){
        System.out.println("--------------------");

        //当前节点的名称、文本内容和属性
        path = path +"/"+ node.getName();
        System.out.println("路径："+path);//父节点名称
        System.out.println("当前父节点名称："+pNode);//父节点名称
        System.out.println("当前节点名称："+node.getName());//当前节点名称
        System.out.println("当前节点的内容："+node.getTextTrim());//当前节点的内容
        List<Attribute> listAttr=node.attributes();//当前节点的所有属性的list




        for(Attribute attr:listAttr){//遍历当前节点的所有属性
            System.out.println("- - - - - - - - - - -");
            System.out.println("属性路径："+path+"/"+attr.getName());//父节点名称
            System.out.println("属性父节点名称："+node.getName());//父节点名称
            String name=attr.getName();//属性名称
            String value=attr.getValue();//属性的值
            System.out.println("属性名称："+name);
            System.out.println("属性值："+value);
        }

        //递归遍历当前节点所有的子节点
        List<Element> listElement=node.elements();//所有一级子节点的list
        for(Element e:listElement){//遍历所有一级子节点
            getNodes(e,node.getName(),path);//递归
        }
    }
}
