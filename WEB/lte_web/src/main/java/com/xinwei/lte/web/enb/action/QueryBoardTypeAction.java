package com.xinwei.lte.web.enb.action;

import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;

public class QueryBoardTypeAction extends ActionSupport {

	private int rackNo;

	private int shelfNo;

	private int slotNo;

	public String getBoardType() {
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html; charset=UTF-8");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = null;
		try {
			// 无需操作，只需"握手"
			out = response.getWriter();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (out != null) {
				JSONObject json = new JSONObject();
				// json.put("", );
				out.println(json.toString());
				out.flush();
				out.close();
			}
		}
		return NONE;
	}

	public int getRackNo() {
		return rackNo;
	}

	public void setRackNo(int rackNo) {
		this.rackNo = rackNo;
	}

	public int getShelfNo() {
		return shelfNo;
	}

	public void setShelfNo(int shelfNo) {
		this.shelfNo = shelfNo;
	}

	public int getSlotNo() {
		return slotNo;
	}

	public void setSlotNo(int slotNo) {
		this.slotNo = slotNo;
	}

}
