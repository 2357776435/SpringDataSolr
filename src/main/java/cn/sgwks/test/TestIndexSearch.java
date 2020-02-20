package cn.sgwks.test;

import cn.sgwks.pojo.Item;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Criteria;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.ScoredPage;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:applicationContext-solr.xml")
public class TestIndexSearch {
    @Autowired
    private SolrTemplate solrTemplate;
    @Autowired

    @Test
    public void testSolrAll(){
        Map<String,Object> paramMap = new HashMap<>();
        paramMap.put("keywords","");
        paramMap.put("category","");
        paramMap.put("brand","");
        paramMap.put("spec","{}");
        paramMap.put("price","");
        paramMap.put("pageNo",1);
        paramMap.put("pageSize",40);
        paramMap.put("sort",40);
        paramMap.put("sortField",40);
        //如果传递尽力的参数为null
        if (paramMap.get("keywords")==null || "".equals(paramMap.get("keywords"))){
            Map<String, Object> resultMap = new HashMap<>();
            //创建查询对象
            Query query = new SimpleQuery("*:*");
            //从第几条开始查询
            query.setOffset(0);
            //设置每页查询多少条数据
            query.setRows(Integer.parseInt(String.valueOf(paramMap.get("pageSize"))));
            ScoredPage<Item> items = solrTemplate.queryForPage(query, Item.class);
            for (Item item : items) {
                resultMap.put(item.getTitle(),item);
            }
            System.out.println(resultMap.size());
        }else {
            System.out.println("22");
        }
    }

    @Test
    public void testGoodsId() {
        Long id = 149187842867965L;
        Query query = new SimpleQuery();
        Criteria criteria = new Criteria("item_goodsid").is(id);
        query.addCriteria(criteria);
        Item item = solrTemplate.queryForObject(query, Item.class);
        System.out.println(item);
        if (item == null) {
            System.out.println("不存在");
        } else {
            System.out.println("存在");
        }
    }

    /**
     * 查询所有数据
     */
    @Test
    public void testSearchAll() {
        //创建查询对象
        Query query = new SimpleQuery("*:*");
        ScoredPage<Item> items = solrTemplate.queryForPage(query, Item.class);
        for (Item item : items) {
            System.out.println(item);
        }
    }

    /**
     * 根据条件查询
     */
    @Test
    public void testSearch() {
        Query query = new SimpleQuery();
        //创建查询条件对象, 注意这里的Criteria对象和mybatis中的那个不是同一个, 只是同名而已,根据标题查询利用分词查询是否有手机存在
        Criteria criteria = new Criteria("item_title").contains("手机");
        //查询对象中放入查询条件
        query.addCriteria(criteria);
        //从第几条开始查询
        query.setOffset(10);
        //设置每页查询多少条数据
        query.setRows(5);
        //查询并返回结果
        ScoredPage<Item> items = solrTemplate.queryForPage(query, Item.class);
//        for (Item item : items) {
//            System.out.println(item);
//        }
        //总页数
        System.out.println("总页数= " + items.getTotalPages());
        //查询到的总记录数
        System.out.println("总记录数= " + items.getTotalElements());
        //查询到的数据集合
        System.out.println(items.getContent());
        //每页有多少条数据
        System.out.println("每页有多少条数据= " + items.getSize());
        //每页有多少条数据
        //System.out.println(items.getNumberOfElements());
    }
}
