package com.xinwei.lte.web.enb.action;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.xinwei.lte.web.MinasSession;
import com.xinwei.lte.web.enb.cache.HttpSessionRegistry;
import com.xinwei.lte.web.enb.model.LogListModel;
import com.xinwei.lte.web.enb.model.SystemLogModel;
import com.xinwei.lte.web.enb.model.UserModel;
import com.xinwei.minas.core.facade.MinasServerFacade;
import com.xinwei.minas.core.facade.secu.AuthorityManageFacade;
import com.xinwei.minas.core.facade.secu.UserSecuFacade;
import com.xinwei.minas.core.model.ActionTypeDD;
import com.xinwei.minas.core.model.secu.LoginUser;
import com.xinwei.minas.core.model.secu.User;
import com.xinwei.minas.core.model.secu.syslog.LogQueryCondition;
import com.xinwei.minas.core.model.secu.syslog.SystemLog;
import com.xinwei.minas.core.model.secu.syslog.SystemLogQueryResult;
import com.xinwei.system.action.web.WebConstants;

/**
 * 安全管理action
 * 
 * @author zhangqiang
 * 
 */
public class SecuAction extends ActionSupport {

	/**
	 * 用户名
	 */
	private String userName;

	/**
	 * 密码
	 */
	private String passWord;

	/**
	 * 角色名
	 */
	private int roleId;

	/**
	 * 是否暂停使用
	 */
	private int canuse;

	/**
	 * 是否永久有效
	 */
	private int ispermanentuser;

	/**
	 * 有效时间
	 */
	private String validtime;

	/**
	 * 描述
	 */
	private String desc;

	/**
	 * 新增标识，1：进行新增
	 */
	private int isAdd;

	/**
	 * 修改标识，1：进行修改
	 */
	private int isModify;

	/**
	 * 修改标识，1：进行修改密码
	 */
	private int isModifyPassWord;

	/**
	 * 删除标识，1：进行删除
	 */
	private int isDeleted;

	/**
	 * 是否有权限新增管理员,1：有
	 */
	private int canAddAdm;

	/**
	 * 被操作的用户
	 */
	private String operatedUserName;

	/**
	 * 当前用户对被操作用户是否有权限，0：否 1：是
	 */
	private int hasPrivilege;

	/**
	 * 用户登陆的sessionID，用户登陆唯一标识
	 */
	private String sessionId;

	/**
	 * 查询到的单个用户
	 */
	private LoginUser loginUser = new LoginUser();

	/**
	 * 错误
	 */
	private String error = "";

	/**
	 * 所有用户
	 */
	private List<UserModel> loginUserList = new ArrayList<UserModel>();

	/**
	 * 起始时间
	 */
	private String beginTime;

	/**
	 * 结束时间
	 */
	private String endTime;

	/**
	 * 查询的当前页
	 */
	private int currentPage;

	/**
	 * 业务类型
	 */
	private String workType;

	/**
	 * 操作用户
	 */
	private String operUser;

	/**
	 * 操作对象类型
	 */
	private String operObjectType;

	/**
	 * 操作对象ID
	 */
	private String operObjectId;

	/**
	 * 操作类型
	 */
	private String operType;

	/**
	 * 查询出的日志列表
	 */
	private LogListModel result = new LogListModel();

	/**
	 * 根据session里用户情况跳往登陆页面或是进入网管
	 * 
	 * @return
	 */
	public String loginRedirect() {

		LoginUser loginUser = ((LoginUser) ActionContext.getContext()
				.getSession().get(WebConstants.KEY_LOGIN_USER_OBJECT));
		// session无用户，转登陆
		if (loginUser == null) {
			return LOGIN;
		} else {
			try {
				// session里的用户在服务器处为在线状态，转网管
				sessionId = ((LoginUser) ActionContext.getContext()
						.getSession().get(WebConstants.KEY_LOGIN_USER_OBJECT))
						.getSessionId();
				MinasServerFacade facade = MinasSession.getInstance()
						.getMinasServerFacade();
				if (facade == null) {
					error = "failed to connect minas server";
					return LOGIN;
				}
				facade.handshake(sessionId);
				return SUCCESS;
			} catch (Exception e) {
				e.printStackTrace();
				// session里的用户在服务器处为离线状态，转登陆
				return LOGIN;
			}
		}
	}

	/**
	 * 登陆
	 * 
	 * @return
	 */
	public String login() {
		try {
			LoginUser loginUser = ((LoginUser) ActionContext.getContext()
					.getSession().get(WebConstants.KEY_LOGIN_USER_OBJECT));
			// session无用户，进行登陆
			if (loginUser == null) {
				userLogin();
			} else {
				// session有用户，且为同一用户
				if (loginUser.getUsername().equals(userName)) {
					try {
						if (!passWord.equals(loginUser.getPassword())) {
							error = "Incorrect password!";
							return LOGIN;
						} else {
							// session里的用户在服务器处为在线状态，转网管
							sessionId = ((LoginUser) ActionContext.getContext()
									.getSession()
									.get(WebConstants.KEY_LOGIN_USER_OBJECT))
									.getSessionId();
							MinasServerFacade facade = MinasSession
									.getInstance().getMinasServerFacade();
							if (facade == null) {
								error = "failed to connect minas server";
								return LOGIN;
							}
							facade.handshake(sessionId);
							return SUCCESS;
						}
					} catch (Exception e) {
						e.printStackTrace();
						ServletActionContext.getRequest().getSession()
								.invalidate();
						if (e.getLocalizedMessage() != null) {
							String[] errorArray = e.getLocalizedMessage()
									.split(";");
							error = errorArray[0];
						}
						// session里的用户在服务器处为离线状态，转登陆
						return LOGIN;
					}
				} else {
					userLogin();
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			if (e.getLocalizedMessage() != null) {
				String[] errorArray = e.getLocalizedMessage().split(";");
				error = errorArray[0];
			}
			return LOGIN;
		}
		return SUCCESS;
	}

	/**
	 * 登陆
	 * 
	 * @throws Exception
	 */
	private void userLogin() throws Exception {
		LoginUser user = new LoginUser();
		user.setUsername(userName);
		user.setPassword(passWord);
		// 获取登录的tomcat的ip地址
		HttpServletRequest request = ServletActionContext.getRequest();
		String loginIp = request.getLocalAddr();
		user.setLoginIp(loginIp);
		// 登陆并获取sessionId
		// sessionId = MinasSession.getInstance().getMinasServerFacade()
		// .login(user, null, "", null);

		MinasServerFacade facade = MinasSession.getInstance()
				.getMinasServerFacade();
		if (facade == null) {
			throw new Exception("failed to connect minas server");
		} else {
			sessionId = facade.login(user, null, "", null);
			user.setSessionId(sessionId);
			UserSecuFacade userFacade = MinasSession.getInstance().getFacade(
					sessionId, UserSecuFacade.class);
			int roleId = userFacade.queryUser(user.getUsername()).getRoleId();
			user.setRoleId(roleId);
			// 将user放入session
			ActionContext.getContext().getSession()
					.put(WebConstants.KEY_LOGIN_USER_OBJECT, user);
		}
	}

	/**
	 * 登出
	 * 
	 * @return
	 */
	public String logout() {
		try {
			// 获取facade
			sessionId = ((LoginUser) ActionContext.getContext().getSession()
					.get(WebConstants.KEY_LOGIN_USER_OBJECT)).getSessionId();
			// 注销登陆
			MinasSession.getInstance().getMinasServerFacade().logout(sessionId);
			// 清除session
			ServletActionContext.getRequest().getSession().invalidate();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return LOGIN;
		}
		return LOGIN;

	}

	/**
	 * 查询单个用户
	 * 
	 * @return
	 */
	public String querySingleUser() {
		try {
			// 获取facade
			sessionId = ((LoginUser) ActionContext.getContext().getSession()
					.get(WebConstants.KEY_LOGIN_USER_OBJECT)).getSessionId();
			UserSecuFacade facade = MinasSession.getInstance().getFacade(
					sessionId, UserSecuFacade.class);
			loginUser = facade.queryUser(operatedUserName);
			LoginUser operUser = facade.queryUser(userName);
			if (operUser.getRoleId() == 1
					&& operUser.getUsername().equals("admin")) {
				canAddAdm = 1;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			error = e.getLocalizedMessage();
		}
		return SUCCESS;
	}

	/**
	 * 查询所有用户
	 * 
	 * @return
	 */
	public String queryAllUser() {
		try {
			// 获取facade
			sessionId = ((LoginUser) ActionContext.getContext().getSession()
					.get(WebConstants.KEY_LOGIN_USER_OBJECT)).getSessionId();
			UserSecuFacade facade = MinasSession.getInstance().getFacade(
					sessionId, UserSecuFacade.class);
			// 新增
			if (isAdd == 1) {
				// 构建新增的user
				User user = new User();
				user.setUsername(userName);
				user.setPassword(passWord);
				user.setRoleId(roleId);
				user.setCanuse(canuse);
				user.setIspermanentuser(ispermanentuser);
				if (ispermanentuser == 1) {
					user.setValidtime(Long.valueOf(validtime));
				}
				user.setDesc(new String(desc.getBytes("iso-8859-1"), "utf-8"));
				facade.addUser(user);
			}
			// 修改
			if (isModify == 1) {
				// 构建修改后的user
				User user = new User();
				user.setUsername(userName);
				user.setRoleId(roleId);
				user.setCanuse(canuse);
				user.setIspermanentuser(ispermanentuser);
				if (ispermanentuser == 1) {
					user.setValidtime(Long.valueOf(validtime));
				}
				user.setDesc(new String(desc.getBytes("iso-8859-1"), "utf-8"));
				facade.modUser(user);
			}
			// 修改密码
			if (isModifyPassWord == 1) {
				facade.modUserPassword(operatedUserName, passWord);
			}
			// 删除
			if (isDeleted == 1) {
				List<String> list = new ArrayList<String>();
				list.add(operatedUserName);
				facade.delUser(list);
			}
			List<LoginUser> list = facade.queryAllUser();
			for (LoginUser user : list) {
				if (user.getUsername().equals(
						((LoginUser) ActionContext.getContext().getSession()
								.get(WebConstants.KEY_LOGIN_USER_OBJECT))
								.getUsername())) {
					loginUser = user;
				}
				UserModel model = new UserModel(user);
				loginUserList.add(model);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			error = e.getLocalizedMessage();
			return ERROR;
		}
		return SUCCESS;
	}

	/**
	 * 跳转至操作日志
	 * 
	 * @return
	 */
	public String turnUserLog() {
		try {
			// 从session里获取在线user
			LoginUser loginUser = ((LoginUser) ActionContext.getContext()
					.getSession().get(WebConstants.KEY_LOGIN_USER_OBJECT));
			sessionId = loginUser.getSessionId();
			UserSecuFacade facade = MinasSession.getInstance().getFacade(
					sessionId, UserSecuFacade.class);
			roleId = facade.queryUser(loginUser.getUsername()).getRoleId();
			if (roleId == 1) {
				List<LoginUser> list = facade.queryAllUser();
				for (LoginUser user : list) {
					if (user.getUsername().equals(loginUser.getUsername())) {
						loginUser = user;
					}
					UserModel model = new UserModel(user);
					loginUserList.add(model);
				}
			} else {
				userName = loginUser.getUsername();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return SUCCESS;
	}

	/**
	 * 查询操作日志
	 * 
	 * @return
	 */
	public String queryUserLog() {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = null;

		try {
			out = response.getWriter();

			// 获取facade
			sessionId = ((LoginUser) ActionContext.getContext().getSession()
					.get(WebConstants.KEY_LOGIN_USER_OBJECT)).getSessionId();
			AuthorityManageFacade facade = MinasSession.getInstance()
					.getFacade(sessionId, AuthorityManageFacade.class);
			// 构建查询条件
			LogQueryCondition condition = new LogQueryCondition();
			SimpleDateFormat format = new SimpleDateFormat(
					"yyyy-MM-dd hh:mm:ss");
			// 开始时间
			if (beginTime != null && !beginTime.equals("")) {
				condition.setStartTime(format.parse(beginTime));
			}
			// 结束时间
			if (endTime != null && !endTime.equals("")) {
				condition.setEndTime(format.parse(endTime));
			}
			// 查询的当前页
			condition.setCurrentPage(currentPage);
			// 操作对象类别
			if (!operObjectType.equals("none")) {
				condition.setOperObjType(operObjectType);
			}
			// 操作对象ID
			if (!operObjectId.equals("") && operObjectId != null) {
				condition.setObjectId(operObjectId);
			}
			// 操作用户
			if (!operUser.equals("none")) {
				condition.setUsername(operUser);
			}
			// 操作类型
			if (!operType.equals("none")) {
				condition.setActionType(operType);
			}
			// 业务类型
			if (!workType.equals("none")) {
				condition.setOperType(workType);
			}
			// 每页数目
			condition.setPageSize(10);
			// 查询出的当页日志
			SystemLogQueryResult logResult = facade.queryLog(condition);
			if (logResult != null) {
				result.setCurrentPage(logResult.getCurrentPage());
				result.setTotalPage(logResult.getTotalPage());
				List<SystemLog> logList = logResult.getLogList();
				List<SystemLogModel> logModelList = new ArrayList<SystemLogModel>();
				for (SystemLog log : logList) {
					SystemLogModel model = new SystemLogModel();
					model.setLog(log);
					model.setOperTimeString(new SimpleDateFormat(
							"yyyy-MM-dd HH:mm:ss").format(log.getOperTime()));
//					 String str = log.getDataDesc();
//					 String dataDesc = str.replaceAll("}", "").replace("{",
//					 "");
					 model.setDataDesc(log.getDataDesc());  
					logModelList.add(model);
				}
				result.setLogModelList(logModelList);
			}
			JSONObject json = new JSONObject();
			JSONObject object = new JSONObject();
			object = JSONObject.fromObject(result);

			json.put("result", object);
			out.println(json.toString());
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.close();
			}
		}
		return NONE;
	}

	/**
	 * 异步获取当前用户对被操作用户权限
	 * 
	 * @return
	 */
	public String checkPrivilege() {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = null;

		try {
			out = response.getWriter();

			// 获取facade
			sessionId = ((LoginUser) ActionContext.getContext().getSession()
					.get(WebConstants.KEY_LOGIN_USER_OBJECT)).getSessionId();
			UserSecuFacade facade = MinasSession.getInstance().getFacade(
					sessionId, UserSecuFacade.class);
			LoginUser loginUser = facade.queryUser(userName);
			LoginUser operatedUser = facade.queryUser(operatedUserName);
			if (facade.hasPrivilege(loginUser, operatedUser,
					ActionTypeDD.MODIFY)) {
				hasPrivilege = 1;
			} else {
				hasPrivilege = 0;
			}

			JSONObject json = new JSONObject();
			json.put("hasPrivilege", hasPrivilege);
			out.println(json.toString());
			out.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				out.close();
			}
		}
		return NONE;
	}

	/**
	 * 踢用户下线
	 * 
	 * @return
	 */
	public String kickUser() {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = null;
		try {
			out = response.getWriter();

			// 获取facade
			sessionId = ((LoginUser) ActionContext.getContext().getSession()
					.get(WebConstants.KEY_LOGIN_USER_OBJECT)).getSessionId();
			UserSecuFacade facade = MinasSession.getInstance().getFacade(
					sessionId, UserSecuFacade.class);
			facade.kickUser(sessionId, operatedUserName);
			// 清除该用户的session
			HttpSessionRegistry instance = HttpSessionRegistry.getInstance();
			HttpSession session = instance
					.findSessionByUsername(operatedUserName);
			session.invalidate();
		} catch (Exception e) {
			error = e.getLocalizedMessage();
		} finally {
			if (out != null) {
				JSONObject json = new JSONObject();
				json.put("error", error);
				out.println(json.toString());
				out.flush();
				out.close();
			}
		}
		return NONE;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassWord() {
		return passWord;
	}

	public void setPassWord(String passWord) {
		this.passWord = passWord;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public List<UserModel> getLoginUserList() {
		return loginUserList;
	}

	public void setLoginUserList(List<UserModel> loginUserList) {
		this.loginUserList = loginUserList;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public LoginUser getLoginUser() {
		return loginUser;
	}

	public void setLoginUser(LoginUser loginUser) {
		this.loginUser = loginUser;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public int getCanuse() {
		return canuse;
	}

	public void setCanuse(int canuse) {
		this.canuse = canuse;
	}

	public int getIspermanentuser() {
		return ispermanentuser;
	}

	public void setIspermanentuser(int ispermanentuser) {
		this.ispermanentuser = ispermanentuser;
	}

	public String getValidtime() {
		return validtime;
	}

	public void setValidtime(String validtime) {
		this.validtime = validtime;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getIsAdd() {
		return isAdd;
	}

	public void setIsAdd(int isAdd) {
		this.isAdd = isAdd;
	}

	public String getOperatedUserName() {
		return operatedUserName;
	}

	public void setOperatedUserName(String operatedUserName) {
		this.operatedUserName = operatedUserName;
	}

	public int getHasPrivilege() {
		return hasPrivilege;
	}

	public void setHasPrivilege(int hasPrivilege) {
		this.hasPrivilege = hasPrivilege;
	}

	public int getIsModify() {
		return isModify;
	}

	public void setIsModify(int isModify) {
		this.isModify = isModify;
	}

	public int getCanAddAdm() {
		return canAddAdm;
	}

	public void setCanAddAdm(int canAddAdm) {
		this.canAddAdm = canAddAdm;
	}

	public int getIsModifyPassWord() {
		return isModifyPassWord;
	}

	public void setIsModifyPassWord(int isModifyPassWord) {
		this.isModifyPassWord = isModifyPassWord;
	}

	public int getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(int isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getWorkType() {
		return workType;
	}

	public void setWorkType(String workType) {
		this.workType = workType;
	}

	public String getOperUser() {
		return operUser;
	}

	public void setOperUser(String operUser) {
		this.operUser = operUser;
	}

	public String getOperObjectType() {
		return operObjectType;
	}

	public void setOperObjectType(String operObjectType) {
		this.operObjectType = operObjectType;
	}

	public String getOperObjectId() {
		return operObjectId;
	}

	public void setOperObjectId(String operObjectId) {
		this.operObjectId = operObjectId;
	}

	public String getOperType() {
		return operType;
	}

	public void setOperType(String operType) {
		this.operType = operType;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public LogListModel getResult() {
		return result;
	}

	public void setResult(LogListModel result) {
		this.result = result;
	}

}
