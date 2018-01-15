package com.xinwei.lte.web.uem.utils;

public abstract class AbstractMessage<T> {
	private String messageId = null;
	private String body = null;

    public abstract int getSeq();

	public abstract T getTarget() ;

	public abstract void setTarget(T target);

	public abstract T getSource();

	public abstract void setSource(T source);

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}	
}