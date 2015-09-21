#ifndef TOKEN_H
#define TOKEN_H
#include <string>
#include <iostream>

using namespace std;

class Token 
{
	private:
		
		string Token_Type;
		string Value;
		int Line_Num;
		
	public:
	
		Token()
		{
			Token_Type = "UNDEFINED";
			Value = "";
			Line_Num = 0;
		}
		Token(string temp, int lineNum)
		{
			Token_Type = temp;
			Line_Num = lineNum;
		}
		Token(string newType, string newValue, int lineNum)
		{
			Token_Type = newType;
			Value = newValue;
			Line_Num = lineNum;
		}
		void setTokenType(string x)
		{
			Token_Type = x;
		}
		void setValue(string x)
		{
			Value = x;
		}
		void setLineNum(int x)
		{
			Line_Num = x;
		}
		void setAll(string x, string y, int z)
		{
			Token_Type = x;
			Value = y;
			Line_Num = z;
		}
		string getTokenType()
		{
			return Token_Type;
		}
		string getValue()
		{
			return Value;
		}
		int getLineNumber()
		{
			return Line_Num;
		}
		void toString()
		{
			cout << "(" + Token_Type + ",\"" + Value +
			"\"," << Line_Num << ")" << endl;
		}
};
	
#endif
