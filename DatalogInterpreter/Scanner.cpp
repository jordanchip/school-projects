#ifndef SCANNER_H
#define SCANNER_H
#include <iostream>
#include "Token.h"
#include <string>
#include <vector>

using namespace std;

class Scanner
{
	public:
		Scanner()
		{
		}		
		
		/* TOKENS = The vector of tokens that will be added to
		 * inStream = The file which we are working with
		 * 
		 * This function goes through the whole stream, taking
		 * in characters until it identifies a token.  Then, its adds it
		 * to the running vector.  It will continue until it scans the
		 * whole file
		 */
		void scan(vector<Token> &Tokens, istream &inStream)
		{
			Token newToken;
			int lineNum = 1;
			char temp;
			char temp2;
			string currentString = "";	
			
			//Continue until the file is done
			
			while (inStream.get(temp))
			{
				currentString = "";
				temp2 = inStream.peek();
				//checks if temp is a letter.  if so, checks what type
				//of token it could be.
				if (isalpha(temp))
				{
					currentString = getAlphas(temp, inStream);
					if (currentString == "Schemes")
					{
						newToken.setAll("SCHEMES", "Schemes", lineNum);
						Tokens.push_back(newToken);
					}
					else if (currentString == "Facts")
					{
						newToken.setAll("FACTS", "Facts", lineNum);
						Tokens.push_back(newToken);
					}
					else if (currentString == "Rules")
					{
						newToken.setAll("RULES", "Rules", lineNum);
						Tokens.push_back(newToken);
					}
					else if (currentString == "Queries")
					{
						newToken.setAll("QUERIES", "Queries", lineNum);
						Tokens.push_back(newToken);
					}
					else
					{
						newToken.setAll("ID", currentString, lineNum);
						Tokens.push_back(newToken);
					}
				}
				else if (temp == '#')
				{
					if (temp2 == '|')
					{
						inStream.get(temp);
						temp2 = inStream.peek();
						bool isUndef = false;
						int tempLineNum = lineNum;
						currentString = getBlockComment(temp, inStream, isUndef, lineNum);
						if (!isUndef)
						{
							newToken.setAll("COMMENT", currentString, tempLineNum);
							Tokens.push_back(newToken);
						}
						else
						{
							newToken.setAll("UNDEFINED", currentString, tempLineNum);
							Tokens.push_back(newToken);
						}
					}
					else
					{
						currentString = getLineComment(temp, inStream);
						newToken.setAll("COMMENT", currentString, lineNum);
						Tokens.push_back(newToken);
					}
				}
				else if (temp == '\'')
				{
					bool isUndef = false;
					int tempLineNum = lineNum;
					currentString = getString(temp, inStream, isUndef, lineNum);
					if (!isUndef)
					{
						newToken.setAll("STRING", currentString, tempLineNum);
						Tokens.push_back(newToken);
					}
					else
					{
						newToken.setAll("UNDEFINED", currentString, tempLineNum);
						Tokens.push_back(newToken);
					}
				}
				else if (temp == '\n')
				{
					lineNum++;
				}
				else if (isspace(temp))
				{
					//Do nothing.
				}
				else if (temp == ',')
				{
					newToken.setAll("COMMA", ",", lineNum);
					Tokens.push_back(newToken);
				}
				else if (temp == '.')
				{
					newToken.setAll("PERIOD", ".", lineNum);
					Tokens.push_back(newToken);
				}
				else if (temp == '?')
				{
					newToken.setAll("Q_MARK", "?", lineNum);
					Tokens.push_back(newToken);
				}
				else if (temp == '(')
				{
					newToken.setAll("LEFT_PAREN", "(", lineNum);
					Tokens.push_back(newToken);
				}
				else if (temp == ')')
				{
					newToken.setAll("RIGHT_PAREN", ")", lineNum);
					Tokens.push_back(newToken);
				}
				else if (temp == ':')
				{
					if (temp2 == '-')
					{
						inStream.get(temp);
						temp2 = inStream.peek();
						newToken.setAll("COLON_DASH", ":-", lineNum);
						Tokens.push_back(newToken);
					}
					else
					{
						newToken.setAll("COLON", ":", lineNum);
						Tokens.push_back(newToken);
					}
				}
				else if (temp == '*')
				{
					newToken.setAll("MULTIPLY", "*", lineNum);
					Tokens.push_back(newToken);
				}
				else if (temp == '+')
				{
					newToken.setAll("ADD", "+", lineNum);
					Tokens.push_back(newToken);
				}
				else
				{
					currentString.push_back(temp);
					newToken.setAll("UNDEFINED", currentString, lineNum);
					Tokens.push_back(newToken);
				}
			}
			newToken.setAll("EOF", "", lineNum);
			Tokens.push_back(newToken);
		}
		
		string getAlphas(char &temp, istream &inStream)
		{
			string returnString;
			returnString.push_back(temp);
			char temp2 = inStream.peek();
			while (isalpha(temp2) || isdigit(temp2))
			{
				inStream.get(temp);
				returnString.push_back(temp);
				temp2 = inStream.peek();
			}
			return returnString;
		}
		
		string getLineComment(char &temp, istream &inStream)
		{
			string returnString;
			returnString.push_back(temp);
			char temp2 = inStream.peek();
			while (temp2 != '\n')
			{
				inStream.get(temp);
				returnString.push_back(temp);
				temp2 = inStream.peek();
			}
			return returnString;
		}
		
		string getBlockComment(char &temp, istream &inStream, bool &isUndef, int &lineNum)
		{
			string returnString = "#";
			returnString.push_back(temp);
			char temp2 = inStream.peek();
			while (temp2 != EOF)
			{
				if (temp2 == '|')
				{
					inStream.get(temp);
					returnString.push_back(temp);
					temp2 = inStream.peek();
					if (temp2 == '#')
					{
						inStream.get(temp);
						returnString.push_back(temp);
						return returnString;
					}
				}
				else
				{
					if (temp2 == '\n')
					{
						lineNum++;
					}
					inStream.get(temp);
					temp2 = inStream.peek();
					returnString.push_back(temp);
				}
			}
			isUndef = true;
			return returnString;
		}
		
		
		//A token has started with a apostrphe ('), find the rest of the string
		//Return the full string with a flag that found string is undefined.
		string getString(char &temp, istream &inStream, bool &isUndef, int &lineNum)
		{
			string returnString;
			returnString.push_back(temp);
			char temp2 = inStream.peek();
			while (temp2 != EOF)
			{
				if (temp2 == '\'')
				{
					inStream.get(temp);
					returnString.push_back(temp);
					temp2 = inStream.peek();
					if (temp2 == '\'')
					{
						inStream.get(temp);
						returnString.push_back(temp);
						temp2 = inStream.peek();
					}
					else
					{
						return returnString;
					}
				}
				else
				{
					if (temp2 == '\n')
					{	
						lineNum++;
					}
					inStream.get(temp);
					temp2 = inStream.peek();
					returnString.push_back(temp);
				}
		
			}
			isUndef = true;
			return returnString;
		}
			
};

#endif
