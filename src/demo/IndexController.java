package demo;
import java.util.List;

import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;


public class IndexController extends Controller {

		
		public void index(){
			//��ʼλ�� start_row  ÿҳ��ʾ����pagesize
			int page_size = 10;
			int start_row = 1;
			String current_page = getPara("current_page");
			if(current_page==null){
				current_page = "0"; //�����ǰҳ�޷���ȡ��Ĭ��Ϊ��ҳ
			}
			start_row = Integer.parseInt(current_page)*page_size;
			String sql = "select distinct company_name from  company_news_data limit "+ start_row+","+page_size;
			
			List<Record> company =  Db.find(sql);
			System.out.print(current_page);	
			String total = "select  count( distinct company_name) as total from  company_news_data";
			List<Record> totals =  Db.find(total);
			setAttr("newsPage", totals.get(0));
			setAttr("dataList", company);			
			render("index.jsp");
		}
		public void search(){
			//String sql = "select news_title,content,publish_data,source from  company_news_data where company_name="+"company";
			render("company/company.jsp");
			
		}
		public void page(){
			
			String sql = "select distinct count(company_name) from  company_news_data";
			List<Record> count_company =  Db.find(sql);
			
			render("index.jsp");
			
			
		}
		
}
