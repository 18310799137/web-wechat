package com.yucheng.im.service.web.dwr.manager;

import java.util.Collection;

import org.directwebremoting.ScriptSession;
import org.directwebremoting.impl.DefaultScriptSessionManager;

import com.yucheng.im.service.web.listener.HandleScriptSessionLinstener;

/**
 * 
* @Title: DwrScriptSessionManager.java
* @Package com.yucheng.im.service.web.dwr.manager
* @Description: DWR管理session所需的辅助类
* @author zhanggh@yusys.com.cn
* @date 2017年8月22日 下午5:41:26
* @version V1.0
*
 */
 public class DwrScriptSessionManager extends DefaultScriptSessionManager {  
     /** 
      * 注入自己的scriptsession管理 
      */  
     public DwrScriptSessionManager() {  
        this.addScriptSessionListener(new HandleScriptSessionLinstener());  
    }
    /**
     * 
     * @Description: 获取服务器中所有的session数据
     * @author zhanggh@yusys.com.cn
     * @date 2017年8月22日 下午5:44:28
     * @version V1.0
     * @return
     */
    @Override  
    public Collection<ScriptSession> getAllScriptSessions() {  
         return HandleScriptSessionLinstener.getAllSctiptSessions();
     }  
 } 
