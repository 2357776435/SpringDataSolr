package cn.sgwks.test;

import cn.sgwks.pojo.Item;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext-solr.xml")
public class TestIndexManager {
    @Autowired
    private SolrTemplate solrTemplate;

    /**
     * 添加单个对象数据
     */
    @Test
    public void test(){
        Item item = new Item();
        item.setId(1L);
        item.setTitle("Nokia诺基亚");
        item.setCategory("老人机");
        item.setPrice(new BigDecimal(998));
        item.setBrand("诺基亚");
        //保存
        solrTemplate.saveBean(item);
        //提交
        solrTemplate.commit();
    }

    /**
     * 添加集合对象数据
     */
    @Test
    public void testIndexCreatAndUpdate(){
        List<Item> itemList = new ArrayList<>();
        for (long i = 1; i < 100; i++) {
            /*
            *   <copyField source="item_title" dest="item_keywords"/>
                <copyField source="item_category" dest="item_keywords"/>
                <copyField source="item_seller" dest="item_keywords"/>
                <copyField source="item_brand" dest="item_keywords"/>
                * 根据配置文件设置值
            * */
            Item item = new Item();
            item.setId(i);
            item.setTitle("诺基亚老人手机"+i);
            item.setCategory("手机");
            item.setPrice(new BigDecimal(998));
            item.setBrand("诺基亚");
            //存放在集合中
            itemList.add(item);
        }
        //保存
        solrTemplate.saveBeans(itemList);
        //提交
        solrTemplate.commit();
    }
    @Test
    public void testIndexDelte() {
        //根据主键域id删除
        //solrTemplate.deleteById("1");

        //创建查询对象
        Query query = new SimpleQuery("*:*");
        //根据查询条件删除
        solrTemplate.delete(query);
        //提交
        solrTemplate.commit();
    }
}
