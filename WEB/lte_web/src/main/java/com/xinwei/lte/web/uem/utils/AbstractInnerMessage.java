package com.xinwei.lte.web.uem.utils;

public class AbstractInnerMessage extends AbstractMessage<String> {
	private String replyQueueName = null;
	private String selfQueueName = null;

	@Override
	public int getSeq() {
		return 0;
	}

	@Override
	public String getTarget() {
		return this.replyQueueName;
	}

	@Override
	public void setTarget(String target) {
		this.replyQueueName = target;
	}

	@Override
	public String getSource() {
		return this.selfQueueName;
	}

	@Override
	public void setSource(String source) {
		this.selfQueueName = source;
	}

}
