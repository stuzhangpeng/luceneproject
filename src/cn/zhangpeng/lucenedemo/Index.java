package cn.zhangpeng.lucenedemo;
import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.hunspell.Dictionary;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.jupiter.api.Test;

import javax.print.Doc;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
public class Index {
    /**
    *功能描述：创建索引库
    *@param
    *@return:
    *@author:
    */
    @Test
    public void testLucene() throws IOException {
        // 1 获得indexwriter对象
        IndexWriter writer = getIndexWriter();
        // 通过io流读取文件
        File fileDr =new File("C:\\Users\\Administrator\\Desktop\\lucenetext");
        File[] files = fileDr.listFiles();
        for (File file:files) {
            //创建文档对象
            Document document =new Document();
            //创建Field域，不同的实现类有不同的策略（是否分词，是否保存到索引库，是否索引）
            /*Field类型有：
             * 1： StringFiled   字符串类型 不分词 索引  保存|不保存索引库
             * 2 一系列的(int|float|double|long) point  不分词  索引  不保存索引库
             * 2 Stored Filed     支持多种数据类型 不分词 不索引 保存到索引库
             * 3 TextFiled         字符串或流           分词   索引  保存|不保存索引库
             * */
            //获取文件的名字
            String name = file.getName();
            //创建field域
            //使用TextFiled         字符串或流           分词   索引  保存|不保存索引库
            TextField textField =new TextField("fileName", name, Field.Store.YES);
            //获取文件的字节大小
            long size = FileUtils.sizeOf(file);
            //创建field域
            LongPoint point =new LongPoint("fileSize",size);
            //获得文件的路径
            String path = file.getPath();
            //创建field域
            StoredField field = new StoredField("filePath", path);
            //获得文件的内容
            String content = FileUtils.readFileToString(file, "gbk");
            //创建field域
            TextField textField1 =new TextField("fileContent", content, Field.Store.YES);
            //将field添加到documment对象
            document.add(textField);
            document.add(textField1);
            document.add(field);
            document.add(point);
            //将文档对象写入到索引库
            writer.addDocument(document);
        }
        //关闭流
        writer.close();
    }
  /**
  *功能描述：获得indexwriter对象
  *@param:
  *@return:
  *@author:
  */
    private IndexWriter getIndexWriter() throws IOException {
        Directory dictory = FSDirectory.open(Paths.get("F:\\luceneindexs"));
        //    创建标准分词器对象
//        Analyzer analyzer = new StandardAnalyzer();
        Analyzer analyzer =new SmartChineseAnalyzer();
        // 创建indexwriter的config对象并指定分词器
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);
        // 创建index writer对象
        return new IndexWriter(dictory, indexWriterConfig);
    }
    /**
    *功能描述：删除全部索引
    *@param:
    *@return:
    *@author:
    */
    @Test
    public void testDeleteIndex() throws IOException {
        //获得indexwriter对象
        IndexWriter indexWriter = getIndexWriter();
        //删除全部索引
         indexWriter.deleteAll();
         indexWriter.close();
    }
    /**
    *功能描述：根据条件删除索引
    *@param:
    *@return:
    *@author:
    */
    @Test
    public void testDeleteIndexByCondition() throws IOException {
        //获得indexwriter对象
        IndexWriter indexWriter = getIndexWriter();
        //根据条件删除索引
        Term term =new Term("fileContent","php");
        Query query =new TermQuery(term);
        indexWriter.deleteDocuments(query);
        indexWriter.close();
    }
    /**
    *功能描述：添加索引
    *@param:
    *@return:
    *@author:
    */
    @Test
    public void addIndex() throws IOException {
        //获得indexwriter对象
        IndexWriter indexWriter = getIndexWriter();
        //添加索引
        Document document =new Document();
        document.add(new TextField("fileContent", "this is add java document", Field.Store.YES));
       indexWriter.addDocument(document);
       indexWriter.close();
    }
    /**
    *功能描述：更新索引，删除指定文档并添加文档即为更新索引，注意！！！更新文档和删除文档，源文档的id仍然保存
    *@param:
    *@return:
    *@author:
    */
    @Test
    public void updateIndex() throws IOException {
        //获得indexwriter对象
        IndexWriter indexWriter = getIndexWriter();
        //创建文档对象
        Document document =new Document();
        document.add(new TextField("fileContent", "this is aaa text", Field.Store.YES));
        document.add(new TextField("fileName", "aaa.txt", Field.Store.YES));
       //创建term对象
        Term term =new Term("fileName","html");
        //根据term更新索引
        indexWriter.updateDocument(term, document);
        indexWriter.close();
    }
}
