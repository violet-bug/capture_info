package com.violet;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.violet.pojo.CaptureInfo;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@MapperScan("com.violet.mapper")
class DemoApplicationTests {

    @Autowired
    private BaseMapper baseMapper;

    @Test
    void contextLoads() {

        for (int j = 1; j < 6; j++) {
            final WebClient webClient = new WebClient(BrowserVersion.CHROME);//新建一个模拟谷歌Chrome浏览器的浏览器客户端对象

            webClient.getOptions().setThrowExceptionOnScriptError(false);//当JS执行出错的时候是否抛出异常, 这里选择不需要
            webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);//当HTTP的状态非200时是否抛出异常, 这里选择不需要
            webClient.getOptions().setActiveXNative(false);
            webClient.getOptions().setCssEnabled(false);//是否启用CSS, 因为不需要展现页面, 所以不需要启用
            webClient.getOptions().setJavaScriptEnabled(true); //很重要，启用JS
            webClient.setAjaxController(new NicelyResynchronizingAjaxController());//很重要，设置支持AJAX

            HtmlPage page = null;
            try {
                //尝试加载上面图片例子给出的网页
                page = webClient.getPage("https://chaoyingcailiao.b2b168.com/shicaiyanghu/taoci/" + "l-" + j + ".html");
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                webClient.close();
            }

            //异步JS执行需要耗时,所以这里线程要阻塞30秒,等待异步JS执行结束
            webClient.waitForBackgroundJavaScript(30000);

            //直接将加载完成的页面转换成xml格式的字符串
            String pageXml = page.asXml();

            //TODO 下面的代码就是对字符串的操作了,常规的爬虫操作,用到了比较好用的Jsoup库

            Document document = Jsoup.parse(pageXml);//获取html文档
            List<Element> infoListEle = document.getElementsByClass("floorC02");//获取元素节点等

            //详情信息
            List<String> infoList = new ArrayList<>();
            //title信息
            List<String> titleList = new ArrayList<>();
            //图片地址
            List<String> imageUrlList = new ArrayList<>();
            //价格
            List<String> priceList = new ArrayList<>();
            //公司名称
            List<String> companyList = new ArrayList<>();
            //公司主页
            List<String> companyAddressList = new ArrayList<>();

            infoListEle.forEach(element -> {
//            System.out.println("售卖地址: " + element.getElementsByTag("li").first().getElementsByTag("a").attr("href"));
//            System.out.println("售卖的标题名字" + element.getElementsByTag("li").first().getElementsByTag("a").attr("title"));
//            System.out.println(element.getElementsByClass("lidivs").first().getElementsByClass("li04").text());
//            System.out.println(element.getElementsByClass("li04").first().getElementsByTag("a").attr("href"));

                for (int i = 1; i < 5; i++) {
                    if (i == 1) {
                        infoList.add(element.getElementsByClass("li0" + i).first().getElementsByTag("a").attr("href"));
                        titleList.add(element.getElementsByClass("li0" + i).first().getElementsByTag("a").attr("title"));
                        imageUrlList.add(element.getElementsByClass("li0" + i).first().getElementsByTag("img").attr("src"));
                    }
                    if (i == 2) continue;

                    if (i == 3) {
                        priceList.add(element.getElementsByClass("li0" + i).html());
                    }
                    if (i == 4) {
                        companyAddressList.add(element.getElementsByClass("li0" + i).first().getElementsByTag("a").attr("href"));
                        companyList.add(element.getElementsByClass("li0" + i).first().getElementsByTag("a").attr("title"));
                    }
                }
            });

            CaptureInfo captureInfo = new CaptureInfo();

            for (int i = 0; i < infoListEle.size(); i++) {
                captureInfo.setCompany(companyList.get(i));
                captureInfo.setCompany_address(companyAddressList.get(i));
                captureInfo.setImaguri(imageUrlList.get(i));
                captureInfo.setInfo(infoList.get(i));
                captureInfo.setPrice(priceList.get(i));
                captureInfo.setTitle(titleList.get(i));
                baseMapper.insert(captureInfo);
            }
        }
    }

}
