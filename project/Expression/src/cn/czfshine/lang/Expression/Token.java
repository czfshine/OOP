package cn.czfshine.lang.Expression;


public class Token {
	enum Type { NUM,OPERATOR }
	
	public Type type;
	
	public String value;
}
