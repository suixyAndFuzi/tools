package com.example.demo.serverImpl;


import com.example.demo.api.MyDemoServer;
import com.example.demo.bean.MyDemoBean;
import com.example.demo.elasticsearch.ElasticsearchClict;
import com.example.demo.elasticsearch.RestClientUtil;
import com.example.demo.http.HttpResult;
import com.example.demo.http.HttpUtil;
import com.example.demo.redis.RedissLockUtil;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class MyDemoServerImpl implements MyDemoServer {
//    @Autowired
//    private MyDemoMapper myDemoMapper;
//    @Autowired
//    private MyDemoFullMapper myDemoFullMapper;

    @Autowired
    private HttpUtil httpUtil;

    @Autowired
    private RedissLockUtil redissLockUtil;


    @Autowired
    private ElasticsearchClict elasticsearchClict;

    @Autowired
    private RestClientUtil restClientUtil;

    @Override
    public String selectDemo(MyDemoBean myDemoBean) {
//        String r = myDemoMapper.selectByUser(myDemoBean.getName());
//        System.out.println(r);
//        String rFull = myDemoFullMapper.selectByUser(myDemoBean.getName());
//        System.out.println(rFull);

        try {
            Map map = new HashMap();
            map.put("xml", "<root><patient> <patientNo>111111</patientNo><name>张三</name><birthday>1999-02-19</birthday><height>170cm</height><weight>60kg</weight><timeOfPreg></timeOfPreg><birthWeight>3000g</birthWeight><bsa></bsa><IDCard>330101201501010001</IDCard><address>杭州市西溪路525号浙大科技园A东402室</address><phoneNo>0571-88393830</phoneNo><department></department><departId></departId><presNo>25165280</presNo><presType>西药方</presType><eventNo>2015061500123</eventNo><payType>市医保</payType><diagnose>感冒</diagnose><main_diagnose></main_diagnose><totalAmount></totalAmount><areaNo></areaNo><bedNo></bedNo><notes></notes><noteAmount></noteAmount><icd10></icd10><drugSensivity></drugSensivity><scr></scr><ccr></ccr><ast></ast><alt></alt><allergyList></allergyList><docId>000123</docId><docName>李医生</docName><docTitle>主任医生</docTitle><sex>男</sex><pregnancy>否</pregnancy><breastFeeding>否</breastFeeding><postType>1</postType><zoneId>1</zoneId><presSource>门诊</presSource><dialysis>否</dialysis> <presDateTime>2018-11-13 09:44:40</presDateTime> </patient> <operation> </operation> <prescriptions> <prescription><drug_no>2</drug_no><groupNo>1</groupNo><type></type><drugName>阿莫西林克拉维酸钾(4:1)片</drugName><drug>17060109183365a9</drug><regName></regName><specification>0.3125g(4:1)</specification><transSpecification></transSpecification><prepForm>普通片</prepForm><manufacturerName>南京先声东元制药有限公司</manufacturerName><package>12</package><quantity>1</quantity><adminRoute>先煎</adminRoute><adminFrequency>3天1次</adminFrequency><adminDose>0.25g</adminDose><adminMethod></adminMethod><adminGoal></adminGoal><adminArea></adminArea><specialPromote></specialPromote><packUnit></packUnit><unitPrice></unitPrice><amount></amount><firstUse>否</firstUse><docID></docID><docName></docName><docTitle></docTitle><department></department><departID></departID><nurseName></nurseName><startTime>2018-11-13 09:44:06</startTime><endTime>2018-11-13 09:44:06</endTime></prescription> </prescriptions> <exams> </exams> </root>");

            String s = httpUtil.doGet("http://10.1.1.43:8080/engineAsync?engine_mode=0&post_type=1&initPostType=1&strCharset=UTF-8&charset=UTF-8", map);
            System.out.println(s);

            HttpResult str = httpUtil.doPost("http://10.1.1.43:8080/returnInfo_v2.jsp?presNo=25165280&hospitalCode=1001&post_type=1&patientNo=111111&name=张三&departId=&time=2018/11/13", null);
            System.out.println("body" + str.getBody());
        } catch (Exception e) {
            System.out.println(e);
        }

        return "";
    }

    /**
     * 模拟多线程调用 测试分布式锁
     */
    @Override
    public void selectDemo1() {
        for (Integer i = 0; i < 100; i++) {
            String key = "redisLock_" + "123";
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    //获得锁 返回lock对象
                    redissLockUtil.lock(key, 2);
                    try {
                        Thread.sleep(1000);
                        System.err.println("======获得锁后进行相应的操作======");
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        //根据lock对象手动释放锁，与java中的lock基本一致
                        redissLockUtil.unlock(key);
                        System.err.println("");
                    }
                }
            });
            t.start();
        }
    }

    /**
     * 调用 elasticsearch 增删改查 简单实现
     */
    @Override
    public void selectDemo2() {
        //9300 java Api调用  方式
        elasticsearchClict.save();
        elasticsearchClict.query();
        elasticsearchClict.update();
       // elasticsearchClict.delete();

        //9200 restful 调用方式
        restClientUtil.search();
    }
}
