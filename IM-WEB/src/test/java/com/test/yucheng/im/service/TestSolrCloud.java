package com.test.yucheng.im.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.CloudSolrClient;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;

public class TestSolrCloud {
	private static String zkHost = "192.168.135.133:2181,192.168.135.134:2181,192.168.135.135:2181";
	
	private static String[] userId = { "ZYYH001", "HN001", "ZX001", "TXYH001" };
	private static String[] userName = { "张三", "李四", "王五", "张肖" };
	private static String[] custNo = { "khh001", "khh002", "khh003", "khh005" };
	private static String[] custName = { "郑州银行", "华夏银行", "中信银行", "天下银行" };

	public static void addDocument(List<SolrInputDocument> documents,
			String collections) {
		CloudSolrClient solr = new CloudSolrClient.Builder().withZkHost(zkHost)
				.build();
		solr.setDefaultCollection(collections);
		try {
			solr.add(documents);
			UpdateResponse response = solr.commit();
			System.out.println("response:" + response);
			System.out.println("status:" + response.getStatus() + "\theader:"
					+ response.getStatus());
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				solr.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	public static ArrayList<SolrInputDocument> createList() {
		ArrayList<SolrInputDocument> arrayList = new ArrayList<SolrInputDocument>();
		for (int i = 0; i < userId.length; i++) {

			SolrInputDocument document = new SolrInputDocument();
			document.addField("id", userId[i]);
			document.addField("userName", userName[i]);
			document.addField("custNo", custNo[i]);
			document.addField("custName", custName[i]);
			arrayList.add(document);
		}
		return arrayList;
	}

	public static void deleteIndex(String id) {
		CloudSolrClient solr = new CloudSolrClient.Builder().withZkHost(zkHost)
				.build();
		solr.setDefaultCollection("solr_collections");
		try {
			solr.deleteById(id);
			solr.commit();
			solr.close();
		} catch (SolrServerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void modifyIndexById() {
		CloudSolrClient solr = new CloudSolrClient.Builder().withZkHost(zkHost)
				.build();
		solr.setDefaultCollection("solr_collections");
		// solr.
	}

	public static void queryIndex(){
		CloudSolrClient solr = new CloudSolrClient.Builder().withZkHost(zkHost)
				.build();
		solr.setDefaultCollection("solr_collections");
		SolrQuery query = new SolrQuery();
		
		
		query.setRows(100000);
		
		query.setFields("id,custUserNo,custUserName");
		
		query.setHighlight(true);
		
		query.setQuery("custUserName:中文");
		
		query.setStart(0);
		
//		query.setFilterQueries("custName:华夏2");
		
		try {
			 SolrDocumentList documentList	= 	solr.query(query).getResults();
			 System.out.println("size:"+documentList.size());
			 solr.commit();
			 for (SolrDocument solrDocument : documentList) {
				System.out.println("id:"+solrDocument.get("id")+"\t custUserName:"+solrDocument.get("custUserName")+"\t custUserNo:"+solrDocument.get("custUserNo"));
			}
		} catch (SolrServerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				solr.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
	}
	
	public static void findById(){
		
		CloudSolrClient solr = new CloudSolrClient.Builder().withZkHost(zkHost)
				.build();
		solr.setDefaultCollection("solr_collections");
		
		
	}
	
	public static void main(String[] args) throws SolrServerException,
			IOException {
		// addDocument(createList(),"solr_collections");
		// deleteIndex("TXYH001");
		queryIndex();
	}

}
