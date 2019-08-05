package cn.zhangpeng.lucenedemo;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.LongPoint;
import org.apache.lucene.document.LongRange;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.nio.file.Paths;
/**索引查询类
 * @Auther:zhangpeng
 * @Date:2019/8/5
 * @Description:cn.zhangpeng.lucenedemo
 * @Version:1.0
 */
public class IndexQuery {

    /**
     *功能描述：精确查询索引
     *@param:
     *@return:
     *@author:
     */
    @Test
    public void testSearchIndex1() throws IOException {
        IndexReader indexReader = getIndexReader();
        //创建索引查询对象 indexSearcher对象
        IndexSearcher searcher =new IndexSearcher(indexReader);
        //创建查询对象query和term
        Term term =new Term("fileContent", "java");
        Query query =new TermQuery(term);
        //执行查询,返回评分最高的前n条结果
        TopDocs docs = searcher.search(query, 5);
        //获得查询到的评分之后的文档
        ScoreDoc[] scoreDocs = docs.scoreDocs;
        //遍历文档数组
        for (ScoreDoc doc:scoreDocs ) {
            //获得查询到的文档的id
            int docId = doc.doc;
            //通过indexsearcher对象查询对应id的文档
            Document document = searcher.doc(docId);
            //通过文档对象。获得指定内容
            String fileSize = document.get("fileSize");
            String fileContent = document.get("fileContent");
            String filePath = document.get("filePath");
            String fileName = document.get("fileName");
            System.out.println(fileContent);
            System.out.println(".........................");
        }
        //关流
        indexReader.close();
    }

    private IndexReader getIndexReader() throws IOException {
        //指定索引库位置
        Directory dictory = FSDirectory.open(Paths.get("F:\\luceneindexs"));
        //创建indexreader对象
        IndexReader indexReader = DirectoryReader.open(dictory);
        return indexReader;
    }

    /**
     *功能描述：全部查询索引
     *@param:
     *@return:
     *@author:
     */
    @Test
    public void testSearchIndex() throws IOException {
        IndexReader indexReader = getIndexReader();
        //创建索引查询对象 indexSearcher对象
        IndexSearcher searcher =new IndexSearcher(indexReader);
        //创建查询对象query和term
      Query query =new MatchNoDocsQuery();
        //执行查询,返回评分最高的前n条结果
        TopDocs docs = searcher.search(query, 8);
        //获得查询到的评分之后的文档
        ScoreDoc[] scoreDocs = docs.scoreDocs;
        //遍历文档数组
        for (ScoreDoc doc:scoreDocs ) {
            //获得查询到的文档的id
            int docId = doc.doc;
            //通过indexsearcher对象查询对应id的文档
            Document document = searcher.doc(docId);
            //通过文档对象。获得指定内容
            String fileSize = document.get("fileSize");
            String fileContent = document.get("fileContent");
            String filePath = document.get("filePath");
            String fileName = document.get("fileName");
            System.out.println(fileContent);
            System.out.println(".........................");
        }
        //关流
        indexReader.close();
    }
    /**
     *功能描述：组合查询索引
     *@param:
     *@return:
     *@author:
     */
    @Test
    public void testCombinationSearchIndex() throws IOException {
        IndexReader indexReader = getIndexReader();
        //创建索引查询对象 indexSearcher对象
        IndexSearcher searcher =new IndexSearcher(indexReader);
        //创建查询TermQuery对象
        Query query1 = new TermQuery(new Term("fileName", "java.txt"));
        Query query2 = new TermQuery(new Term("fileName", "php.txt"));
        //创建BooleanClause对象
        BooleanClause  booleanClause =new BooleanClause(query1, BooleanClause.Occur.SHOULD);
        BooleanClause  booleanClause1 =new BooleanClause(query2, BooleanClause.Occur.SHOULD);
        //创建BooleanQuery对象
       BooleanQuery query =new BooleanQuery.Builder().add(booleanClause).add(booleanClause1).build();
        //执行查询,返回评分最高的前n条结果
        TopDocs docs = searcher.search(query, 4);
        //获得查询到的评分之后的文档
        ScoreDoc[] scoreDocs = docs.scoreDocs;
        //遍历文档数组
        for (ScoreDoc doc:scoreDocs ) {
            //获得查询到的文档的id
            int docId = doc.doc;
            //通过indexsearcher对象查询对应id的文档
            Document document = searcher.doc(docId);
            //通过文档对象。获得指定内容
            String fileSize = document.get("fileSize");
            String fileContent = document.get("fileContent");
            String filePath = document.get("filePath");
            String fileName = document.get("fileName");
            System.out.println(fileName);
            System.out.println(".........................");
        }
        //关流
        indexReader.close();
    }
    /**
    *功能描述：解析查询
    *@param:
    *@return:
    *@author:
    */
    @Test
    public void testParseSearchIndex() throws IOException, ParseException {
        IndexReader indexReader = getIndexReader();
        //创建索引查询对象 indexSearcher对象
        IndexSearcher searcher =new IndexSearcher(indexReader);
        //创建queryparser对象
        //指定解析查询的默认查询域和解析器
        QueryParser queryParser=new QueryParser("fileContent",new StandardAnalyzer());
        //执行查询,返回评分最高的前n条结果
        Query parse = queryParser.parse("*:*");
        TopDocs docs = searcher.search(parse, 8);
        //获得查询到的评分之后的文档
        ScoreDoc[] scoreDocs = docs.scoreDocs;
        //遍历文档数组
        for (ScoreDoc doc:scoreDocs ) {
            //获得查询到的文档的id
            int docId = doc.doc;
            //通过indexsearcher对象查询对应id的文档
            Document document = searcher.doc(docId);
            //通过文档对象。获得指定内容
            String fileSize = document.get("fileSize");
            String fileContent = document.get("fileContent");
            String filePath = document.get("filePath");
            String fileName = document.get("fileName");
            System.out.println(fileContent);
            System.out.println(".........................");
        }
        //关流
        indexReader.close();
    }
    /**
    *功能描述：多默认域解析查询
    *@param:
    *@return:
    *@author:
    */
    @Test
    public void testMultiParseSearchIndex() throws IOException, ParseException {
        IndexReader indexReader = getIndexReader();
        //创建索引查询对象 indexSearcher对象
        IndexSearcher searcher =new IndexSearcher(indexReader);
        //创建queryparser对象
        //指定解析查询的多个默认查询域和解析器
        MultiFieldQueryParser queryParser=new MultiFieldQueryParser(new String[]{"fileContent","fileName"},new StandardAnalyzer() );
        //执行查询,返回评分最高的前n条结果
        Query parse = queryParser.parse("java");
        TopDocs docs = searcher.search(parse, 8);
        //获得查询到的评分之后的文档
        ScoreDoc[] scoreDocs = docs.scoreDocs;
        //遍历文档数组
        for (ScoreDoc doc:scoreDocs ) {
            //获得查询到的文档的id
            int docId = doc.doc;
            //通过indexsearcher对象查询对应id的文档
            Document document = searcher.doc(docId);
            //通过文档对象。获得指定内容
            String fileSize = document.get("fileSize");
            String fileContent = document.get("fileContent");
            String filePath = document.get("filePath");
            String fileName = document.get("fileName");
            System.out.println(fileContent);
            System.out.println(".........................");
        }
        //关流
        indexReader.close();
    }
    /**
    *功能描述：范围查询
    *@param:
    *@return:
    *@author:
    */
    @Test
    public void testNumberRangeSearchIndex() throws IOException, ParseException {
        IndexReader indexReader = getIndexReader();
        //创建索引查询对象 indexSearcher对象
        IndexSearcher searcher =new IndexSearcher(indexReader);
        //创建范围查询条件
        Query query = LongPoint.newRangeQuery("fileSize",200,300);
        TopDocs docs = searcher.search(query, 8);
        //获得查询到的评分之后的文档
        ScoreDoc[] scoreDocs = docs.scoreDocs;
        //遍历文档数组
        for (ScoreDoc doc:scoreDocs ) {
            //获得查询到的文档的id
            int docId = doc.doc;
            //通过indexsearcher对象查询对应id的文档
            Document document = searcher.doc(docId);
            //通过文档对象。获得指定内容
            String fileSize = document.get("fileSize");
            String fileContent = document.get("fileContent");
            String filePath = document.get("filePath");
            String fileName = document.get("fileName");
            System.out.println(fileName);
            System.out.println(fileSize);
            System.out.println(".........................");
        }
        //关流
        indexReader.close();
    }


}
