package cn.zhangpeng.lucenedemo;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
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
     *功能描述：查询索引
     *@param:
     *@return:
     *@author:
     */
    @Test
    public void testSearchIndex() throws IOException {
        //指定索引库位置
        Directory dictory = FSDirectory.open(Paths.get("F:\\luceneindexs"));
        //创建indexreader对象
        IndexReader indexReader = DirectoryReader.open(dictory);
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


}
