package com.haitai.seal.biz;

public interface WebPushSealStateBiz {
	/**
	 * 同步印章状态，通过 401协议
	 * @param idArray 需要同步状态的印章列表id集合
	 * @return 返回同步状态	0：成功、1：失败
	 */
	 String querySealState(String[] idArray) throws Exception;
}
