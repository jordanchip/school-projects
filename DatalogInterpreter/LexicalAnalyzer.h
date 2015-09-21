#ifndef LexicalAnalyzer_H
#define LexicalAnalyzer_H
#include <string>
#include <vector>
#include <iostream>
#include "Token.h"

using namespace std;
	
	void tokensToString(vector<Token> Tokens)
	{
		for (unsigned int i = 0; i < Tokens.size(); i++)
		{
			Tokens[i].toString();
		}
		cout << "Total Tokens = " << Tokens.size();
	}

	
#endif
