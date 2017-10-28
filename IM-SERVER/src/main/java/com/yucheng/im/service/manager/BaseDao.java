package com.yucheng.im.service.manager;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.session.RowBounds;
import org.apache.ibatis.session.SqlSession;

public class BaseDao {
	@Resource
	protected SqlSession sqlSession;

	protected boolean add(String statement) {
		return checkResult(sqlSession.insert(statement));
	}

	protected boolean add(String statement, Object parameter) {
		return checkResult(sqlSession.insert(statement, parameter));
	}

	protected boolean remove(String statement) {
		return checkResult(sqlSession.delete(statement));
	}

	protected boolean remove(String statement, Object parameter) {
		return checkResult(sqlSession.delete(statement, parameter));
	}

	protected boolean modify(String statement) {
		return checkResult(sqlSession.update(statement));
	}

	protected boolean modify(String statement, Object parameter) {
		return checkResult(sqlSession.update(statement, parameter));
	}

	protected List<?> queryForList(String statement) {
		return sqlSession.selectList(statement);
	}

	protected List<?> queryForList(String statement, Object parameter) {
		return sqlSession.selectList(statement, parameter);
	}

	protected List<?> queryForList(String statement, Object parameter, RowBounds rowBounds) {
		return sqlSession.selectList(statement, parameter, rowBounds);
	}

	protected Map<String, Object> selectMap(String statement, String mapKey) {
		return sqlSession.selectMap(statement, mapKey);
	}

	protected Map<String, Object> selectMap(String statement, Object parameter, String mapKey) {
		return sqlSession.selectMap(statement, parameter, mapKey);
	}

	protected Map<String, Object> selectMap(String statement, Object parameter, String mapKey, RowBounds rowBounds) {
		return sqlSession.selectMap(statement, parameter, mapKey, rowBounds);
	}

	protected Object queryForObject(String statement) {
		return sqlSession.selectOne(statement);
	}

	protected Object queryForObject(String statement, Object parameter) {
		return sqlSession.selectOne(statement, parameter);
	}

	private boolean checkResult(int i) {
		return i == 1 ? true : false;
	}
	/*
	private PageHelper getCount(PageHelper pageHelper) throws SQLException{
		Map<String, String> params = pageHelper.getParams();
		params.put("tableName", pageHelper.getTableName());
		params.put("conditions", pageHelper.getConditions().toString());
		Integer allCount = (Integer) queryForObject("page_help.queryAllCount", params);
		pageHelper.setAllCount(allCount);
		pageHelper.setParams(params);
		return  pageHelper;
	}
	
	protected List<?> selectNowPageData(PageHelper pageHelper) throws PageErrorException, SQLException{
		
		//对pageHelpMap类中的 map进行重新装载(存入tableName,和查出的总行数)
		pageHelper = getCount(pageHelper);
		Map<String, String> params = pageHelper.getParams();
		//从map中取出 刚查出的总行数
		int allCount =pageHelper.getAllCount();
		String conditions = pageHelper.getConditions().toString();
		if(!"".equals(conditions)){
			params.put("conditions", conditions);
		}
		//取出当前页
		int nowPage = pageHelper.getNowPage();
		//取出每页显示条数
		int pageSize = pageHelper.getPageSize();
		//计算出总共有多少页
		int allPage = (allCount%pageSize==0)?allCount/pageSize:(allCount/pageSize)+1;
		pageHelper.setAllPage(allPage);
		if(nowPage>allPage)throw new PageErrorException(this.getClass().getSimpleName()+"请求的页数 大于总页数");
		System.out.println("params.........................."+params);
		
		StringBuilder pages = pageHelper.getPages();
		System.out.println("allPage............33333........"+allPage);
			if(allPage>1){
				if(nowPage<=3){
					for (int i = 1; i <=allPage&&i <= 7; i++) {
						if(i==nowPage){
							pages.append("<a class=disabledPage>"+i+"</a>");
						}else{
							pages.append("<a class=page  href=\""+pageHelper.getAction()+"?nowPage="+i+"\">"+i+"</a>");
						}				}
				}else{
					for (int i = 1; i <=nowPage+3&&i<=allPage; i++) {
						if(i==1||i==2||((i>=nowPage-1)&&(i<=nowPage+3))||i>=allPage-4){
								if(i==nowPage){
									pages.append("<a class=disabledPage  >"+i+"</a>");
								}else{
									pages.append("<a class=page  href=\""+pageHelper.getAction()+"?nowPage="+i+"\">"+i+"</a>");
								}
						}else{
								if(i==3){
									pages.append("...");
								}
						}
					}
				}
			}
		params.put("nowPage", String.valueOf(nowPage));
		params.put("pageSize", String.valueOf(pageSize));
		return (List<?>) queryForList("page_help.queryAllData", params);
	}*/
}
