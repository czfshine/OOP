package cn.czfshine.lang.Expression;

import java.util.Scanner;

public class ArithmeticExpression extends Expression {
	
	private String exp;
	public ArithmeticExpression() {
		exp="";
	}
	
	public  ArithmeticExpression(String exp) {
		this.exp=exp;
	}
	
	private Token[] paser() {
		Scanner s=new Scanner(exp);
	
		return null;
		
	}
}
