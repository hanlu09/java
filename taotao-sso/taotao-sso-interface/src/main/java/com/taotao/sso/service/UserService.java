package com.taotao.sso.service;

import com.taotao.common.pojo.TaotaoResult;
import com.taotao.pojo.TbUser;

public interface UserService {

	TaotaoResult checkUserData(String data, int type);
	TaotaoResult addUser(TbUser user);
	TaotaoResult userLogin(String username, String password);
	TaotaoResult getUserByToken(String token);
}
