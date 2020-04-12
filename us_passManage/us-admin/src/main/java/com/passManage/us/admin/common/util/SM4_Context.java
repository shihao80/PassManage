package com.passManage.us.admin.common.util;

public class SM4_Context
{
	public int mode;
	
	public long[] sk;
	
	public boolean isPadding;

	public SM4_Context() 
	{
		this.mode = 1;
		this.isPadding = false;
		this.sk = new long[32];
	}
}
