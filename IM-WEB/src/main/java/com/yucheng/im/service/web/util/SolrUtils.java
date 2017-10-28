package com.yucheng.im.service.web.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;

import com.yucheng.im.service.entity.UserInfoBean;
import com.yucheng.im.service.entity.UserInfoSolrIndex;

/**
 * 
 * @Title: SolrUtils.java
 * @Package com.yucheng.im.service.util.solrs
 * @Description:solr连接的API TODO 导入数据时判断数据是否已经存在
 * @author zhanggh@yusys.com.cn
 * @date 2017年8月21日 下午1:33:07
 * @version V1.0
 *
 */
public class SolrUtils {

	private static Logger logger = Logger.getLogger(SolrUtils.class);

	private static String zkHost;

	private  static String SOLR_COLLECTIONS;

	private static CloudSolrClient cloudSolrClient = null;

/**
 * 
* @Title: initZookeeperConfig 
* @Description: 读取zookeeper配置文件 solr集合名称信息
* @return void
* @throws
* @author 18310799137@gmail.com
* @date 2017年9月1日 上午11:11:54 
* @version V1.0
 */
	static{
		InputStream  inputStream =	SolrUtils.class.getResourceAsStream(WebConstants.Config.ZOOKEEPER_CONF_FILE_PATH);
		Properties properties = new Properties();
			try {
				properties.load(inputStream);
				for (String key : properties.stringPropertyNames()) {
					if(WebConstants.Config.ZOOKEEPER_HOST.equalsIgnoreCase(key)) {
						zkHost=properties.getProperty(key);
					}
					else if(WebConstants.Config.SOLR_COLLECTIONS.equalsIgnoreCase(key)) {
						SOLR_COLLECTIONS=properties.getProperty(key);
					}
				}
			} catch (IOException e) {
				logger.error("读取配置文件错误  Path:"+WebConstants.Config.ZOOKEEPER_CONF_FILE_PATH+"\tCause:"+e);
			}finally {
				if(null!=inputStream) {
					try {
						inputStream.close();
					} catch (IOException e) {
						logger.error("Close ReadZookeeper Stream Error - "+e);
					}
				}
			}
		logger.debug("ReadZookeeperConfig - Path:"+WebConstants.Config.ZOOKEEPER_CONF_FILE_PATH+" ZookeeperHost: "+properties);
		logger.debug("ReadZookeeperConfig - zkHost:"+zkHost+" - SOLR_COLLECTIONS:"+SOLR_COLLECTIONS);
		if (null == cloudSolrClient) {
			cloudSolrClient = new CloudSolrClient.Builder().withZkHost(zkHost).build();
			cloudSolrClient.setDefaultCollection(SOLR_COLLECTIONS);
			logger.info("初始化sorl连接 >>solrCollection<<>>" + cloudSolrClient + "<<");
		}
	}




	public void deleteIndex() {

	}

	/**
	 * 此方法会清空所有的索引数据,只开发使用,生产模式禁用
	 */
	public static void emptyIndex() {
		cloudSolrClient.setDefaultCollection(SOLR_COLLECTIONS);
		try {
			UpdateResponse response = cloudSolrClient.deleteByQuery("*:*");
			System.out.println("status:" + response.getStatus() + "\tResponseHeader:" + response.getResponseHeader());
			cloudSolrClient.commit();
		} catch (SolrServerException e) {
			logger.error("  SolrServer exception - "+e);
		} catch (IOException e) {
			logger.error("  IOException exception - "+e);
		} 

	}

 

	public static void addUserInfo(List<UserInfoBean> lists) {
		ArrayList<SolrInputDocument> documentList = new ArrayList<SolrInputDocument>();
		for (UserInfoBean userInfo : lists) {
			SolrInputDocument document = new SolrInputDocument();
			document.addField("id",userInfo.getId());
			document.addField("custUserName", userInfo.getNameCn() + userInfo.getUserName());
			documentList.add(document);
		}
		try {
			logger.info("正在添加索引到Solr:" + lists);
			cloudSolrClient.add(SOLR_COLLECTIONS, documentList);
			UpdateResponse response = cloudSolrClient.commit();
			logger.info("索引创建完成 - ServerResponse:"+response);
		} catch (SolrServerException e) {
			logger.error("  SolrServer exception - "+e);
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("  Solr IOException - "+e);
			e.printStackTrace();
		} 
	}

	/**
	 * 
	 * @Description: TODO
	 * @author zhanggh@yusys.com.cn
	 * @date 2017年8月2日 下午3:40:12
	 * @version V1.0
	 * @param fields
	 * @param query
	 * @param start
	 * @param size
	 * @return
	 */
	public static List<UserInfoSolrIndex> queryUser(String fields, String query, int start, int size) {
		List<UserInfoSolrIndex> userInfos = null;
		SolrQuery solrQuery = new SolrQuery();
		solrQuery.setRows(size);
		solrQuery.setFields(fields);
		solrQuery.setHighlight(true);
		solrQuery.setHighlightSimplePre("<font color='red'>");
		solrQuery.setHighlightSimplePost("</font>");
		solrQuery.setQuery(query);
		solrQuery.setStart(start);
		try {
			SolrDocumentList documentList = cloudSolrClient.query(solrQuery).getResults();
			if (documentList.size() > 0) {
				userInfos = new ArrayList<UserInfoSolrIndex>();
			}
			// userInfos = client.query(solrQuery).getBeans(UserInfoSolrIndex.class);
			for (SolrDocument solrDocument : documentList) {
				String userId = String.valueOf(solrDocument.get("id"));
				String custUserName = String.valueOf(solrDocument.get("custUserName"));

				UserInfoSolrIndex solrIndex = new UserInfoSolrIndex(userId, custUserName);
				userInfos.add(solrIndex);
			}
		} catch (Exception e) {
			logger.error("Use solr query Exception with - "+e);
			return null;
		} 

		return userInfos;
	}
	public static List<UserInfoSolrIndex> queryUserFilter(String fields, String query, int start, int size,String ...filterQuery) {
		List<UserInfoSolrIndex> userInfos = null;
		SolrQuery solrQuery = new SolrQuery();
		solrQuery.setRows(size);
		solrQuery.setFields(fields);
		solrQuery.setHighlight(true);//开启高亮功能
		solrQuery.addHighlightField("custUserName");//高亮字段 
//		solrQuery.setHighlightSimplePre("<font color='red'>").setHighlightSimplePost("</font>");//渲染标签
		
		solrQuery.setFilterQueries( filterQuery );
		
		solrQuery.setHighlightSimplePre("<font color='red'>");
		solrQuery.setHighlightSimplePost("</font>");
		solrQuery.setQuery(query);
		solrQuery.setStart(start);
		try {
			SolrDocumentList documentList = cloudSolrClient.query(solrQuery).getResults();
//			System.out.println(documentList);
			if (documentList.size() > 0) {
				userInfos = new ArrayList<UserInfoSolrIndex>();
			}
			// userInfos = client.query(solrQuery).getBeans(UserInfoSolrIndex.class);
			for (SolrDocument solrDocument : documentList) {
				String userId = String.valueOf(solrDocument.get("id"));
				
//				String custUserName = String.valueOf(solrDocument.get("custUserName"));
				//打印高亮字段
//				System.out.println(cloudSolrClient.query(solrQuery).getHighlighting().get(userId).get("custUserName"));
				String custUserName = cloudSolrClient.query(solrQuery).getHighlighting().get(userId).get("custUserName").toString();
				
				UserInfoSolrIndex solrIndex = new UserInfoSolrIndex(userId, custUserName);
				userInfos.add(solrIndex);
			}
		} catch (Exception e) {
			logger.error("Use solr query Exception with - "+e);
			return null;
		} 
		
		return userInfos;
	}

	public static void main(String[] args) {
		System.out.println(SolrUtils.queryUser("id,custUserName", "custUserName:中原银行", 1,10));;
		System.out.println(SolrUtils.queryUserFilter("*", "custUserName:银行", 0,10,""));;
//		solrUtils.emptyIndex();
	}
}
