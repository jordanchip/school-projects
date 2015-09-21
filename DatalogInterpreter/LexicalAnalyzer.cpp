#include <fstream>
#include <iostream>
#include <iomanip>
#include <cstring>
#include <string>
#include <vector>
#include <cstdlib>
#include <ctype.h>
#include "LexicalAnalyzer.h"
#include "Token.h"
#include "Scanner.cpp"
#include <vector>
#include "DatalogProgram.h"
#include "Predicate.cpp"
#include "Parameter.cpp"
#include "Database.cpp"

using namespace std;

int main(int argc, char* argv[])
{
	string inputFile;
	//These variables are for scanning in the file
	vector<Token> Tokens;
	Scanner scan1;
	
	ifstream inStream1;
	inStream1.open(argv[1]);

	if (inStream1.is_open())
	{
		scan1.scan(Tokens, inStream1);
		inStream1.close();
	}
	//tokensToString(Tokens);
	
	//Now we want to Parse in the Tokens and see if they are a valid member of the language
	DatalogProgram dP1(Tokens);
	if (dP1.parseTokens())
	{
		//dP1.toString();
		Database dB1(dP1);
		dB1.answerQueries();
	}

	return 0;
}
