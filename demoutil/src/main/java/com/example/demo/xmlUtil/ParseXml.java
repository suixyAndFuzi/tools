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


        String str = "<?xml version=\"1.0\" encoding=\"UTF-8\"?> <ClinicalDocument xmlns=\"urn:hl7-org:v3\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:hl7-org:v3 ../coreschemas/CDA.xsd\"> <!--===================================--> <!-- 检验报告 --> <!--===================================--> <!-- **************************************************************************** CDA Header **************************************************************************** --> <!-- 文档适用范围编码 --> <realmCode code=\"CN\" /> <!-- 文档信息模型类别-标识符 --> <!-- 固定值 --> <typeId root=\"2.16.840.1.113883.1.3\" extension=\"POCD_HD000040\" /> <!-- 文档标识-报告号 --> <id root=\"S008\" extension=\"报告号\" /> <!-- 文档标识-名称 / 文档标识-类别编码 --> <!-- 固定值 --> <code code=\"04\" codeSystem=\"1.2.156.112703.1.1.60\" displayName=\"检查检验记录\"/> <!-- 文档标题文本 --> <title>检验报告</title> <!-- 文档生效日期 --> <effectiveTime value=\"文档生效日期\" /> <!-- 文档保密程度-代码 @code: N默认值, 没有特殊限制/加密. 其他值可以是R, V @displayName: normal，对@code的解释, 表示文档的保密级别名称 @codeSystem: 2.16.840.1.113883.5.25，固定值，表示HL7 OID @codeSystemName: Confidentiality，固定值，表示HL7注册的编码系统名称 --> <confidentialityCode code=\"文档保密程度\" codeSystem=\"2.16.840.1.113883.5.25\" codeSystemName=\"Confidentiality\" displayName=\"normal\" /> <!-- 文档语言编码 --> <!-- 固定值 --> <languageCode code=\"zh-CN\" /> <!--服务ID--> <setId extension=\"BS319\"/> <!-- 文档的操作版本:0表示新增, 1表示修改,2表示再批准 --> <versionNumber value=\"0\"/> <!-- 文档记录对象 --> <recordTarget> <!-- 病人信息 --> <patientRole> <!-- 域ID --> <id root=\"1.2.156.112703.1.2.1.2\" extension=\"域ID\" /> <!-- 患者ID --> <id root=\"1.2.156.112703.1.2.1.3\" extension=\"患者ID\" /> <!-- 就诊号 --> <id root=\"1.2.156.112703.1.2.1.12\" extension=\"就诊号\" /> <!-- 病人基本信息 --> <patient> <!-- 病人名称 --> <name>病人名称</name> <!-- 性别编码/性别名称 --> <administrativeGenderCode code=\"性别编码\" codeSystem=\"1.2.156.112703.1.1.3\" displayName=\"性别名称\" /> <!-- 出生日期 --> <birthTime value=\"出生日期\" /> </patient> </patientRole> </recordTarget> <!-- 报告人信息 --> <author> <!-- 报告日期 --> <time value=\"报告日期\" /> <assignedAuthor> <!-- 报告人编码 --> <id root=\"1.2.156.112703.1.1.2\" extension=\"报告人编码\" /> <assignedPerson> <!-- 报告人名称 --> <name>报告人名称</name> </assignedPerson> </assignedAuthor> </author> <!-- 文档保管者(CDA中custodian为必填项) --> <custodian> <assignedCustodian> <representedCustodianOrganization> <!-- 医疗机构编码 --> <id root=\"1.2.156.112703\" extension=\"医疗机构编码\" /> <!-- 医疗机构名称 --> <name>医疗机构名称</name> </representedCustodianOrganization> </assignedCustodian> </custodian> <!-- 电子签章信息 --> <legalAuthenticator> <time /> <signatureCode code=\"S\" /> <assignedEntity> <id extension=\"电子签章号\" /> </assignedEntity> </legalAuthenticator> <!-- 审核人信息 --> <authenticator> <!-- 审核日期 --> <time value=\"审核日期\" /> <signatureCode code=\"S\" /> <assignedEntity> <!-- 审核者编码 --> <id root=\"1.2.156.112703.1.1.2\" extension=\"审核者编码\" /> <assignedPerson> <!-- 审核者名称 --> <name>审核者名称</name> </assignedPerson> </assignedEntity> </authenticator> <!-- 送检医生信息 --> <participant typeCode=\"DIST\"> <associatedEntity classCode=\"ASSIGNED\"> <!-- 送检医生编码 --> <id root=\"1.2.156.112703.1.1.2\" extension=\"送检医生编码\"/> <associatedPerson> <!-- 送检医生名称 --> <name>送检医生名称</name> </associatedPerson> </associatedEntity> </participant> <!-- 检验科室信息(执行科室) --> <participant typeCode=\"PRF\"> <associatedEntity classCode=\"ASSIGNED\"> <associatedPerson/> <scopingOrganization> <!-- 检验科室编码 --> <id root=\"1.2.156.112703.1.1.1\" extension=\"检验科室编码\"/> <!-- 检验科室名称 --> <name>检验科室名称</name> </scopingOrganization> </associatedEntity> </participant> <!-- 申请科室信息 --> <participant typeCode=\"AUT\"> <!-- 申请时间 --> <time value=\"申请时间\"/> <associatedEntity classCode=\"ASSIGNED\"> <scopingOrganization> <!-- 申请科室编码 --> <id root=\"1.2.156.112703.1.1.1\" extension=\"申请科室编码\"/> <!-- 申请科室名称 --> <name>申请科室名称</name> </scopingOrganization> </associatedEntity> </participant> <!-- 其他参与者 @code: 可以用此属性表明是不同身份人员 --> <!-- 关联医嘱信息 --> <inFulfillmentOf> <order> <!-- 关联医嘱号(可多个) --> <id extension=\"关联医嘱号\" /> </order> </inFulfillmentOf> <!-- 文档中医疗卫生事件的就诊场景 --> <componentOf> <encompassingEncounter> <!-- 就诊次数 --> <id root=\"1.2.156.112703.1.2.1.7\" extension=\"\"/> <!-- 就诊流水号 --> <id root=\"1.2.156.112703.1.2.1.6\" extension=\"\"/> <!-- 就诊类别编码/就诊类别名称 --> <code code=\"\" codeSystem=\"1.2.156.112703.1.1.80\" displayName=\"\" /> <!-- 必须项 --> <effectiveTime /> <!-- 病人位置 --> <location> <healthCareFacility> <serviceProviderOrganization> <asOrganizationPartOf classCode=\"PART\"> <!-- 病床号 --> <wholeOrganization classCode=\"ORG\" determinerCode=\"INSTANCE\"> <id extension=\"\"/> <!-- 病房号 --> <asOrganizationPartOf classCode=\"PART\"> <wholeOrganization classCode=\"ORG\" determinerCode=\"INSTANCE\"> <id extension=\"\"/> <!--病人科室编码/名称 --> <asOrganizationPartOf classCode=\"PART\"> <wholeOrganization classCode=\"ORG\" determinerCode=\"INSTANCE\"> <id root=\"1.2.156.112703.1.1.1\" extension=\"\"/> <name></name> <!-- 病人病区编码/名称 --> <asOrganizationPartOf classCode=\"PART\"> <wholeOrganization classCode=\"ORG\" determinerCode=\"INSTANCE\"> <id root=\"1.2.156.112703.1.1.33\" extension=\"\"/> <name></name> </wholeOrganization> </asOrganizationPartOf> </wholeOrganization> </asOrganizationPartOf> </wholeOrganization> </asOrganizationPartOf> </wholeOrganization> </asOrganizationPartOf> </serviceProviderOrganization> </healthCareFacility> </location> </encompassingEncounter> </componentOf> <!-- **************************************************************************** CDA Body **************************************************************************** --> <!-- 结构化信息 --> <component> <structuredBody> <!-- ******************************************************** 文档中患者相关信息 ******************************************************** --> <component> <section> <code code=\"34076-0\" codeSystem=\"2.16.840.1.113883.6.1\" codeSystemName=\"LOINC\" displayName=\"Information for patients section\" /> <title>文档中患者相关信息</title> <!-- 患者年龄 --> <entry> <observation classCode=\"OBS\" moodCode=\"EVN\"> <code code=\"397669002\" codeSystem=\"2.16.840.1.113883.6.96\" codeSystemName=\"SNOMED CT\" displayName=\"age\" /> <value xsi:type=\"ST\"></value> </observation> </entry> </section> </component> <!-- **************************************************************************** #检验章节(Labs section) **************************************************************************** --> <component> <section> <code code=\"11502-2\" codeSystem=\"2.16.840.1.113883.6.1\" codeSystemName=\"LOINC\" displayName=\"Laboratory report\" /> <title>检验</title> <!-- 相关备注说明 --> <text> <!-- 报告备注/报告备注/结果提示 --> <content ID=\"a1\">内容填在这</content> <!-- 技术备注 --> <content ID=\"a2\">内容填在这</content> <!-- 表现现象(指报告的一些备注内容) --> <content ID=\"a3\">内容填在这</content> <!-- HIS相关备注(门诊申请备注, 住院医嘱嘱托。例如: 透析前, 透析后) --> <content ID=\"a4\">内容填在这</content> </text> <!-- 一个条目对应一个大的检验结果 --> <entry> <observation classCode=\"OBS\" moodCode=\"EVN\"> <!-- 检验类别编码(1.2.156.112703.1.1.44) --> <code /> <statusCode code=\"completed\" /> <!-- 优先级别:紧急/优先/普通 --> <priorityCode code=\"优先级别\" /> <!-- 方法 --> <methodCode displayName=\"\"/> <!-- 相关信息 --> <entryRelationship typeCode=\"COMP\"> <organizer classCode=\"BATTERY\" moodCode=\"EVN\"> <code code=\"310388008\" codeSystem=\"2.16.840.1.113883.6.96\" codeSystemName=\"SNOMED CT\" displayName=\"relative information status\" /> <statusCode code=\"completed\" /> <!-- 整张报告图片信息 --> <component> <observationMedia classCode=\"OBS\" moodCode=\"EVN\"> <!-- 图片信息(要求编码为BASE64), @mediaType: 图片格式(JPG格式: image/jpeg PDF格式为: application/pdf) --> <value xsi:type=\"ED\" mediaType=\"\"></value> <entryRelationship typeCode=\"XCRPT\"> <act classCode=\"INFRM\" moodCode=\"PRP\"> <code/> <!-- 提示信息 --> <text></text> </act> </entryRelationship> </observationMedia> </component> </organizer> </entryRelationship> <entryRelationship typeCode=\"COMP\"> <!-- 检验报告条目 --> <organizer classCode=\"BATTERY\" moodCode=\"EVN\"> <!-- 检验项编码/检验项名称(血细胞分析24项类型编码) --> <code code=\"检验项编码\" codeSystem=\"1.2.156.112703.1.1.46\" displayName=\"检验项名称\" /> <statusCode code=\"completed\" /> <!-- 检验子项信息 --> <component> <!-- 显示序号 --> <sequenceNumber value=\"显示序号\"/> <observation classCode=\"OBS\" moodCode=\"EVN\"> <!-- 检验子项编码/检验子项简称/检验子项全称(白细胞检验类型编码) --> <code code=\"检验子项编码\" codeSystem=\"1.2.156.112703.1.1.108\" displayName=\"检验子项简称\"> <!-- @displayName是简称, originalText是全称 --> <originalText>检验子项全称</originalText> </code> <statusCode code=\"completed\" /> <!-- 检验结果(结果只采用PQ, ST, SC类型) --> <!-- PQ: <value xsi:type=\"PQ\" value=\"19.1\" unit=\"10^9/L\" /> 数值类型的结果+单位(没有单位去掉@unit, 没有结果去掉@value) ST: <value xsi:type=\"ST\">阳性(+)</value> 文本类型结果 SC: <value xsi:type=\"SC\" code=\"个/LPF\">未见</value> 文本类型结果+单位 --> <value xsi:type=\"PQ\" value=\"检验结果\" unit=\"检验结果单位\" /> <!-- 结果是否正常-, 偏高↑或偏低↓ --> <interpretationCode code=\"高低值判断编码\" displayName=\"高低值判断内容\"> <originalText>高低值判断内容</originalText> <translation code=\"数值标识\" /> </interpretationCode> <!-- codeSystem决定使用参考说明的系统标识 --> <interpretationCode code=\"危险值判断编码\" displayName=\"危险值判断内容\"> <translation code=\"危险标识\" /> </interpretationCode> <!-- 范围类型编码(固定值 - @code: 01 参考范围值 02 危险范围值 03 卵泡期 04 排卵期 05 黄体期 06 绝经期 07 成年男性) --> <!-- 参考范围值 --> <referenceRange> <observationRange> <code code=\"范围类型编码\" displayName=\"范围类型名称\"/> <!--参考范围 文本说明--> <text></text> <value xsi:type=\"IVL_PQ\" unit=\"参考范围值单位\"> <low value=\"参考范围低值\" /> <high value=\"参考范围高值\" /> </value> </observationRange> </referenceRange> </observation> </component> <!-- 其他项目按上面结构和格式添加 --> </organizer> </entryRelationship> <!-- 如果报告上有相关检验项, 可以按上面结构组织添加. --> <!-- 标本及其图像信息 --> <entryRelationship typeCode=\"SAS\" inversionInd=\"true\"> <procedure classCode=\"PROC\" moodCode=\"EVN\"> <code /> <statusCode code=\"completed\" /> <!-- 标本采集日期(采血时间) --> <effectiveTime value=\"采集日期\" /> <!-- 标本信息 --> <specimen> <specimenRole> <!-- 标本条码号 --> <id extension=\"标本条码号\" /> <specimenPlayingEntity> <!-- 标本类型编码/标本类型名称(标本来源) --> <code code=\"标本类型编码\" codeSystem=\"1.2.156.112703.1.1.45\" displayName=\"标本类型名称\" /> </specimenPlayingEntity> </specimenRole> </specimen> <!-- 采集人/采集机构信息 --> <performer> <assignedEntity> <!-- 采集人编码 --> <id root=\"1.2.156.112703.1.1.2\" extension=\"采集人编码\" /> <assignedPerson> <!-- 采集人名称 --> <name>采集人名称</name> </assignedPerson> <!-- 采集地点编码/采集地点名称 --> <representedOrganization> <id root=\"1.2.156.112703.1.1.1\" extension=\"采集地点编码\"/> <name>采集地点名称</name> </representedOrganization> </assignedEntity> </performer> <!-- 标本接收人信息 --> <participant typeCode=\"RCV\"> <!-- 接收时间/送检时间 --> <time value=\"接收时间\" /> <participantRole> <!-- 接收人编码 --> <id root=\"1.2.156.112703.1.1.2\" extension=\"接收人编码\" /> <playingEntity> <!-- 接收人名称 --> <name>接收人名称</name> </playingEntity> </participantRole> </participant> <!-- 标本容器信息 --> <participant typeCode=\"SBJ\"> <participantRole> <playingDevice> <!-- 容器编码/容器名称 --> <code code=\"容器编码\" displayName=\"容器名称\" /> </playingDevice> </participantRole> </participant> <!-- 图像信息 --> <entryRelationship typeCode=\"SPRT\"> <observationMedia classCode=\"OBS\" moodCode=\"EVN\"> <!-- 影像信息(要求编码为BASE64), @mediaType: 影像格式 --> <value xsi:type=\"ED\" mediaType=\"影像格式\">影像信息</value> <entryRelationship typeCode=\"XCRPT\"> <act classCode=\"INFRM\" moodCode=\"PRP\"> <code /> <!-- 提示信息 --> <text>提示信息</text> </act> </entryRelationship> </observationMedia> </entryRelationship> <!-- 其他图像按以上格式添加 --> </procedure> </entryRelationship> </observation> </entry> </section> </component> <!-- **************************************************************************** #诊断章节(Diagnosis section) **************************************************************************** --> <component> <section> <code code=\"29308-4\" codeSystem=\"2.16.840.1.113883.6.1\" codeSystemName=\"LOINC\" displayName=\"Diagnosis\"/> <title>诊断</title> <entry typeCode=\"DRIV\"> <act classCode=\"ACT\" moodCode=\"EVN\"> <code nullFlavor=\"NA\"/> <entryRelationship typeCode=\"SUBJ\"> <observation classCode=\"OBS\" moodCode=\"EVN\"> <!-- 诊断类别编码/诊断类别名称 --> <code code=\"诊断类别编码\" codeSystem=\"1.2.156.112703.1.1.29\" displayName=\"诊断类别名称\" /> <statusCode code=\"completed\"/> <!-- 疾病编码/疾病名称(没有编码去掉@code) --> <value xsi:type=\"CD\" code=\"疾病编码\" codeSystem=\"1.2.156.112703.1.1.30\" displayName=\"疾病名称\" /> </observation> </entryRelationship> </act> </entry> </section> </component> <!-- **************************************************************************** #药观章节 **************************************************************************** --> <component> <section> <entry> <observation classCode=\"OBS\" moodCode=\"EVN\"> <!-- 药观编码/药观名称 --> <code code=\"药观编码\" displayName=\"药观名称\"/> </observation> </entry> </section> </component> </structuredBody> </component> </ClinicalDocument>";

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