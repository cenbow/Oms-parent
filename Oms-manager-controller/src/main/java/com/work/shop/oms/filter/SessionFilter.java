package com.work.shop.oms.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.work.shop.oms.bean.SystemOmsResource;
import com.work.shop.oms.bean.SystemOmsRole;
import com.work.shop.oms.service.CommonService;
import com.work.shop.oms.service.SystemResourceService;
import com.work.shop.oms.utils.Constant;
import com.work.shop.oms.utils.StringUtil;
import com.work.shop.oms.vo.AuthResource;
import com.work.shop.united.client.dataobject.Role;
import com.work.shop.united.client.dataobject.User;
import com.work.shop.united.client.facade.AuthCenterFacade;
import com.work.shop.united.client.facade.UserStore;
import com.work.shop.united.client.filter.config.Config;

public class SessionFilter implements Filter {
	
	private static Logger logger = Logger.getLogger(SessionFilter.class);

	private ApplicationContext context;

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletRequest hreq = (HttpServletRequest) request;
		String userName = "";
		try {
			// 用户已经登录,获取用户权限信息并将之存入session中
			User user = UserStore.get(hreq);
			if(user != null && StringUtil.isNotEmpty(user.getUserName())) {
				userName = user.getUserName() == null ? "": user.getUserName();
				HttpSession session = hreq.getSession();
				if (null != session) {
					if (null == session.getAttribute(Constant.SESSION_USER_KEY)) {
						logger.debug("写入session userName =" + userName);
						// 根据统一登录权限信息获取订单管理用户权限信息
						session.setAttribute(Constant.SESSION_USER_KEY, user);
						getUserAuth(userName, session);
					} else {
						User tempUser = (User) session.getAttribute(Constant.SESSION_USER_KEY);
						if (!tempUser.getUserName().equals(user.getUserName())) {
							logger.debug("写入session userName =" + userName);
							// 根据统一登录权限信息获取订单管理用户权限信息
							session.setAttribute(Constant.SESSION_USER_KEY, user);
							getUserAuth(userName, session);
						} else if (session.getAttribute(Constant.SESSION_ROLE_KEY) == null) {
							getUserAuth(userName, session);
						}
					}
				}
			}
			chain.doFilter(request, response);
		} catch (Exception e) {
			logger.error("用户信息存入Session异常", e);
		}

	}

	public void init(FilterConfig filterConfig) throws ServletException {
		context = WebApplicationContextUtils.getWebApplicationContext(filterConfig.getServletContext());
	}

	public void destroy() {
		// TODO Auto-generated method stub
	}
	
	
	private void getUserAuth(String userName, HttpSession session) {
		AuthCenterFacade auth = Config.getAuthCenterFacade();
		List<Role> roles = auth.queryUserRole(userName, Config.getAppId());
		if (!StringUtil.isListNotNull(roles)) {
			return;
		}
		List<String> roleCodes = new ArrayList<String>();
//		session.setAttribute(Constant.SESSION_UNLOCK_KEY, Constant.OS_NO);
		for (Role role : roles) {
			roleCodes.add(role.getCode());
//			if (Constant.SESSION_UNLOCK_KEY.equals(role.getCode())) {
//				session.setAttribute(Constant.SESSION_UNLOCK_KEY, Constant.OS_YES);
//			}
		}
		// 获取用户角色信息
		SystemResourceService systemResourceService = context.getBean(SystemResourceService.class);
		List<SystemOmsRole> omsRoles = systemResourceService.getSystemOmsRolesByRoleCode(roleCodes);
		session.setAttribute(Constant.SESSION_ROLE_KEY, omsRoles);

		// 获取权限资源信息
		List<SystemOmsResource> resources = systemResourceService.getSystemResourceByRole(roleCodes);
		Map<String, SystemOmsResource> resourceMap = new HashMap<String, SystemOmsResource>();
		for (SystemOmsResource resource :resources) {
			resourceMap.put(resource.getResourceId().toString(), resource);
		}
		// 根据权限资源显示需要加载的内容
		List<SystemOmsResource> desktopList= null;
		List<AuthResource> authResources = null;

		SystemOmsResource resource = new SystemOmsResource();
		resource.setIsShow((byte)1);
		CommonService commonService = context.getBean(CommonService.class);
		desktopList = commonService.selectSystemOmsResource(resource);
		// 查询全部有效资源项
		List<SystemOmsResource> actResources = new ArrayList<SystemOmsResource>();
		if (StringUtil.isListNotNull(desktopList)) {
			authResources = new ArrayList<AuthResource>();
			for (SystemOmsResource obj : desktopList) {
				SystemOmsResource tempObj = resourceMap.get(obj.getResourceId().toString());
				// 权限资源里面不含改资源不显示
				if (tempObj == null) {
					continue;
				}
				AuthResource pAuthResource = new AuthResource();
				if ("desktop_group".equals(obj.getResourceType())) {
					if (!Constant.SITE_MANAGER_KEY.equals(obj.getResourceCode())) {
						pAuthResource.setResourceName(obj.getResourceName());
						pAuthResource.setIconCls("grid-shortcut");
						pAuthResource.setResourceCode(obj.getResourceCode());
						pAuthResource.setModule("tree-win-001");
						pAuthResource.setMenu(true);
						pAuthResource.setLeaf(false);
						pAuthResource.setFlag(2);
						pAuthResource.setResourceUrl("");
						authResources.add(pAuthResource);
					}
				} else if ("url".equals(obj.getResourceType())) {
					if (Constant.SITE_MANAGER_KEY.equals(obj.getParentCode())) {
						List<String> strings = (List<String>) session.getAttribute(Constant.SITE_MANAGER_KEY);
						if (StringUtil.isListNull(strings)) {
							strings = new ArrayList<String>();
						}
						boolean falg = true;
						for (int i = 0 ; i < strings.size(); i++) {
							if (obj.getResourceCode().equals(strings.get(i))) {
								falg = false;
								break;
							}
						}
						if (falg) {
							strings.add(obj.getResourceCode());
							session.setAttribute(Constant.SITE_MANAGER_KEY, strings);
						}
					} else {
						pAuthResource.setResourceName(obj.getResourceName());
						pAuthResource.setIconCls("cpu-shortcut");
						pAuthResource.setResourceCode(obj.getParentCode() + "-" + obj.getResourceCode());
						pAuthResource.setModule("");
						pAuthResource.setMenu(false);
						pAuthResource.setLeaf(true);
						pAuthResource.setFlag(3);
						pAuthResource.setResourceUrl(obj.getResourceUrl());
						authResources.add(pAuthResource);
					}
				} else if ("act".equals(obj.getResourceType())) {
					actResources.add(obj);
				}
			}
		}
		session.setAttribute(Constant.SESSION_RES_KEY, authResources);
		// 页面按钮权限控制
		if (!StringUtil.isListNotNull(actResources)) {
			return ;
		}
		// Map List
		Map<String, List<SystemOmsResource>> map = new HashMap<String, List<SystemOmsResource>>();
		for (SystemOmsResource obj : actResources) {
			String key = obj.getParentCode();
			List<SystemOmsResource> mapList = map.get(key);
			if (StringUtil.isListNotNull(mapList)) {
				mapList.add(obj);
			} else {
				mapList = new ArrayList<SystemOmsResource>();
				mapList.add(obj);
			}
			map.put(key, mapList);
		}
		for (String key : map.keySet()) {
			List<SystemOmsResource> mapList = map.get(key);
			session.setAttribute(key, mapList);
		}
	}
}
