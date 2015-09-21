#ifndef DATALOG_CPP
#define DATALOG_CPP
#include <iostream>
#include "DatalogProgram.h"
#include "Parameter.cpp"
#include "Rule.cpp"
#include "Token.h"


using namespace std;


	DatalogProgram::DatalogProgram()
	{
		
	}
	DatalogProgram::DatalogProgram(vector<Token> Tokens)
	{
		this->Tokens = Tokens;
	}
	//Return true if parsed successfully, otherwise, false
	bool DatalogProgram::parseTokens()
	{
		try
		{
			Tokens = removeComments();
			current = 0;
			return checkDATALOG();
		}
		catch (Token badToken)
		{
			cout << "Failure!" << endl;
			cout << "  ";
			badToken.toString();
			return false;
		}
	}
	
	
	
	
	
	/*													*
	 * 					CHECK FUNCTIONS					*
	 * 													*/
	bool DatalogProgram::checkDATALOG()
	{
		return checkLINEONE() && checkLINETWO() && checkLINETHREE() && checkLINEFOUR();
	}
	
	bool DatalogProgram::checkLINEONE()
	{
		return matchSCHEME() && matchCOLON() && checkSCHEME() && checkSCHEMELIST();
	}
	
	bool DatalogProgram::checkLINETWO()
	{
		return matchFACTS() && matchCOLON() && checkFACTLIST();
	}
	
	bool DatalogProgram::checkLINETHREE()
	{
		return matchRULES() && matchCOLON() && checkRULELIST();
	}
	
	bool DatalogProgram::checkLINEFOUR()
	{
		return matchQUERIES() && matchCOLON() && checkQUERY() && checkQUERYLIST();
	}
	
		

	bool DatalogProgram::checkSCHEME()
	{
		//return matchID() && matchLEFTPAREN() && matchID() && checkIDLIST() && 
		//		matchRIGHTPAREN();
		if (matchID())
		{
			predTemp.setHead(Tokens[current-1].getValue());
			if (matchLEFTPAREN() && matchID())
			{
				temp.setParameter(Tokens[current-1].getValue());
				predTemp.addParameter(temp);
				if (checkIDLIST())
				{
					Schemes.push_back(predTemp);
					predTemp.reset();
					return matchRIGHTPAREN();
				}
			}
		}
		throw Tokens[current];
	}
	
	bool DatalogProgram::checkSCHEMELIST()
	{
		if (Tokens[current].getTokenType() == "ID")
			return checkSCHEME() && checkSCHEMELIST();
		else if (Tokens[current].getTokenType() == "FACTS")
			return true;
		throw Tokens[current];
	}
	
	bool DatalogProgram::checkIDLIST()
	{
		if (Tokens[current].getTokenType() == "COMMA")
		{
			//return matchCOMMA() && matchID() && checkIDLIST();
			if (matchCOMMA() && matchID())
			{
				temp.setParameter(Tokens[current-1].getValue());
				predTemp.addParameter(temp);
				return checkIDLIST();
			}
		}
		else if (Tokens[current].getTokenType() == "RIGHT_PAREN")
			return true;
		throw Tokens[current];
	}

	bool DatalogProgram::checkFACT()
	{
		//return matchID() && matchLEFTPAREN() && matchSTRING() && checkSTRINGLIST() &&
		//	matchRIGHTPAREN() && matchPERIOD();
		if (matchID())
		{
			predTemp.setHead(Tokens[current-1].getValue());
			if (matchLEFTPAREN() && matchSTRING())
			{
				temp.setParameter(Tokens[current-1].getValue());
				predTemp.addParameter(temp);
				Domain.push_back(temp);
				if (checkSTRINGLIST())
				{
					Facts.push_back(predTemp);
					predTemp.reset();
					return matchRIGHTPAREN() && matchPERIOD();
				}
			}
		}
		throw Tokens[current];
	}


	bool DatalogProgram::checkFACTLIST()
	{
		if (Tokens[current].getTokenType() == "ID")
			return checkFACT() && checkFACTLIST();
		else if (Tokens[current].getTokenType() == "RULES")
			return true;
		throw Tokens[current];
	}

	bool DatalogProgram::checkSTRINGLIST()
	{
		if (Tokens[current].getTokenType() == "COMMA")
		{
			if (matchCOMMA() && matchSTRING())
			{
				temp.setParameter(Tokens[current-1].getValue());
				predTemp.addParameter(temp);
				Domain.push_back(temp);
				return checkSTRINGLIST();
			}
		}
		else if (Tokens[current].getTokenType() == "RIGHT_PAREN")
			return true;
		throw Tokens[current];
	}

	bool DatalogProgram::checkRULE()
	{
		return checkHEADPREDICATE() && matchCOLONDASH() && checkPREDICATERULES() &&
				checkPREDICATELIST() && matchPERIOD();
	}

	bool DatalogProgram::checkRULELIST()
	{
		if (Tokens[current].getTokenType() == "ID")
		{
			return checkRULE() && checkRULELIST();
		}
		else if (Tokens[current].getTokenType() == "QUERIES")
			return true;
		throw Tokens[current];
	}
	
	bool DatalogProgram::checkHEADPREDICATE()
	{
		//return matchID() && matchLEFTPAREN() && matchID() && checkIDLIST() &&
			//	matchRIGHTPAREN();
		if (matchID())
		{
			predTemp.setHead(Tokens[current-1].getValue());
			if (matchLEFTPAREN() && matchID())
			{
				temp.setParameter(Tokens[current-1].getValue());
				predTemp.addParameter(temp);
				if (checkIDLIST())
				{
					Rules.push_back(predTemp);
					predTemp.reset();
					return matchRIGHTPAREN();
				}
			}
		}
		throw Tokens[current];
	}
	
	bool DatalogProgram::checkPREDICATERULES()
	{
		//return matchID() && matchLEFTPAREN() && checkPARAMETER() && 
		//		checkPARAMETERLIST() && matchRIGHTPAREN();
		if (matchID())
		{
			predTemp.setHead(Tokens[current-1].getValue());
			if (matchLEFTPAREN() && checkPARAMETER() && checkPARAMETERLIST())
			{
				Rules[Rules.size()-1].addPredicate(predTemp);
				predTemp.reset();
				return matchRIGHTPAREN();
			}
		}
		throw Tokens[current];
	}
	
	bool DatalogProgram::checkPREDICATEQUERY()
	{
		//return matchID() && matchLEFTPAREN() && checkPARAMETER() && 
		//		checkPARAMETERLIST() && matchRIGHTPAREN();
		if (matchID())
		{
			predTemp.setHead(Tokens[current-1].getValue());
			if (matchLEFTPAREN() && checkPARAMETER() && checkPARAMETERLIST())
			{
				Queries.push_back(predTemp);
				predTemp.reset();
				return matchRIGHTPAREN();
			}
		}
		throw Tokens[current];
	}
	
	bool DatalogProgram::checkPREDICATELIST()
	{
		if (Tokens[current].getTokenType() == "COMMA")
		{
			return matchCOMMA() && checkPREDICATERULES() && checkPREDICATELIST();
		}
		else if (Tokens[current].getTokenType() == "PERIOD")
			return true;
		throw Tokens[current];
	}
	
	bool DatalogProgram::checkPARAMETER()
	{
		if (Tokens[current].getTokenType() == "STRING")
		{
			expressionTemp += Tokens[current].getValue();
			return matchSTRING();
		}
		else if (Tokens[current].getTokenType() == "ID")
		{
			expressionTemp += Tokens[current].getValue();
			return matchID();
		}
		else if (Tokens[current].getTokenType() == "LEFT_PAREN")
		{
			return checkEXPRESSION();
		}
		throw Tokens[current];
	}
	
	bool DatalogProgram::checkPARAMETERLIST()
	{
		if (Tokens[current].getTokenType() == "COMMA")
		{
			temp.setParameter(expressionTemp);
			if (expressionTemp.find("'") != string::npos)
			{
				temp.setString(true);
			}
			predTemp.addParameter(temp);
			temp.setString(false);
			expressionTemp = "";
			return matchCOMMA() && checkPARAMETER() && checkPARAMETERLIST();
		}
		else if (Tokens[current].getTokenType() == "RIGHT_PAREN")
		{
			temp.setParameter(expressionTemp);
			if (expressionTemp.find("'") != string::npos)
			{
				temp.setString(true);
			}
			predTemp.addParameter(temp);
			temp.setString(false);
			expressionTemp = "";
			return true;
		}
		throw Tokens[current];
	}
	
	bool DatalogProgram::checkEXPRESSION()
	{
		if (matchLEFTPAREN())
		{
			expressionTemp += Tokens[current-1].getValue();
			if (checkPARAMETER() && checkOPERATOR() && checkPARAMETER())
			{
				if (matchRIGHTPAREN())
				{
					expressionTemp += Tokens[current-1].getValue();
					return true;
				}
			}
		}
		throw Tokens[current];
	}
	
	bool DatalogProgram::checkOPERATOR()
	{
		if (Tokens[current].getTokenType() == "ADD")
		{
			expressionTemp += Tokens[current].getValue();
			return matchADD();
		}
		else if (Tokens[current].getTokenType() == "MULTIPLY")
		{
			expressionTemp += Tokens[current].getValue();
			return matchMULTIPLY();
		}
		throw Tokens[current];
	}
	
	bool DatalogProgram::checkQUERY()
	{
		return checkPREDICATEQUERY() && matchQMARK();
		throw Tokens[current];
		
	}
	
	bool DatalogProgram::checkQUERYLIST()
	{
		if (Tokens[current].getTokenType() == "ID")
			return checkQUERY() && checkQUERYLIST();
		else if (Tokens[current].getTokenType() == "EOF")
			return true;
		throw Tokens[current];
	}
			







	/* 											*				
	 * 				MATCH FUNCTIONS				*
	 *											*/
	bool DatalogProgram::matchADD()
	{
		if (Tokens[current].getTokenType() == "ADD")
		{
			current++;
			return true;
		}
		throw Tokens[current];
		return false;
	}
	
	bool DatalogProgram::matchCOLONDASH()
	{
		if (Tokens[current].getTokenType() == "COLON_DASH")
		{
			current++;
			return true;
		}
		throw Tokens[current];
	}


	bool DatalogProgram::matchSCHEME()
	{
		if (Tokens[current].getTokenType() == "SCHEMES")
		{
			current++;
			return true;
		}
		throw Tokens[current];
	}
	
	bool DatalogProgram::matchCOLON()
	{
		if (Tokens[current].getTokenType() == "COLON")
		{
			current++;
			return true;
		}
		throw Tokens[current];
	}
	
	bool DatalogProgram::matchCOMMA()
	{
		if (Tokens[current].getTokenType() == "COMMA")
		{
			current++;
			return true;
		}
		throw Tokens[current];
	}
	
	bool DatalogProgram::matchFACTS()
	{
		if (Tokens[current].getTokenType() == "FACTS")
		{
			current++;
			return true;
		}
		throw Tokens[current];
	}
	bool DatalogProgram::matchID()
	{
		if (Tokens[current].getTokenType() == "ID")
		{
			current++;
			return true;
		}
		throw Tokens[current];
	}
	
	bool DatalogProgram::matchLEFTPAREN()
	{
		if (Tokens[current].getTokenType() == "LEFT_PAREN")
		{
			current++;
			return true;
		}
		throw Tokens[current];
	}
	
	bool DatalogProgram::matchMULTIPLY()
	{
		if (Tokens[current].getTokenType() == "MULTIPLY")
		{
			current++;
			return true;
		}
		throw Tokens[current];
		return false;
	}
	
	bool DatalogProgram::matchPERIOD()
	{
		if (Tokens[current].getTokenType() == "PERIOD")
		{
			current++;
			return true;
		}
		throw Tokens[current];
	}
	
	bool DatalogProgram::matchQMARK()
	{
		if (Tokens[current].getTokenType() == "Q_MARK")
		{
			current++;
			return true;
		}
		throw Tokens[current];
	}
	
	bool DatalogProgram::matchQUERIES()
	{
		if (Tokens[current].getTokenType() == "QUERIES")
		{
			current++;
			return true;
		}
		throw Tokens[current];
	}
	
	bool DatalogProgram::matchRIGHTPAREN()
	{
		if (Tokens[current].getTokenType() == "RIGHT_PAREN")
		{
			current++;
			return true;
		}
		throw Tokens[current];
	}
	
	bool DatalogProgram::matchRULES()
	{
		if (Tokens[current].getTokenType() == "RULES")
		{
			current++;
			return true;
		}
		throw Tokens[current];
	}
	
	bool DatalogProgram::matchSTRING()
	{
		if (Tokens[current].getTokenType() == "STRING")
		{
			current++;
			return true;
		}
		throw Tokens[current];
	}


	

	vector<Predicate> DatalogProgram::getQueries()
	{
		return Queries;
	}
	
	vector<Predicate> DatalogProgram::getFacts()
	{
		return Facts;
	}

	vector<Predicate> DatalogProgram::getSchemes()
	{
		return Schemes;
	}


	void DatalogProgram::toString()
	{
			
			for (unsigned int i = 0; i < Domain.size(); i++)
			{
				DomainStrings.insert(Domain[i].getParameter());
			}
			
			cout << "Success!" << endl;
			cout << "Schemes(" << Schemes.size() << "):\n";
			for (unsigned int i = 0; i < Schemes.size(); i++)
			{
				cout << "  ";
				Schemes[i].toString();
				cout << endl;
			}
			cout << "Facts(" << Facts.size() << "):\n";
			for (unsigned int i = 0; i < Facts.size(); i++)
			{
				cout << "  ";
				Facts[i].toString();
				cout << "." << endl;
			}
			cout << "Rules(" << Rules.size() << "):\n";
			for (unsigned int i = 0; i < Rules.size(); i++)
			{
				cout << "  ";
				Rules[i].toString();
				cout << endl;
			}
			cout << "Queries(" << Queries.size() << "):\n";
			for (unsigned int i = 0; i < Queries.size(); i++)
			{
				cout << "  ";
				Queries[i].toString();
				cout << "?" << endl;
			}
			cout << "Domain(" << DomainStrings.size() << "):\n";
			set<string>::iterator iter;
			int ii = 0;
			for(iter=DomainStrings.begin(); iter != DomainStrings.end(); ++iter)
			{
				//cout<<(*iter)<<endl;
				ii += 1;
				cout << "  " << *iter << endl;
			}
	}
	
	
	void DatalogProgram::addScheme(Predicate p1)
	{
		Schemes.push_back(p1);
	}
	
	
	//This function returns the next Token in the vector
	//If one tries to access the Token after the last one, it gives back an "ERROR"
	Token DatalogProgram::peek()
	{
		if (current < Tokens.size())
		{
			return Tokens[current+1];
		}
		else
		{
			Token error;
			error.setTokenType("ERROR");
			return error;
		}
	}
	
	vector<Token> DatalogProgram::removeComments()
	{
		vector<Token> noCommentTokens;
		for (unsigned int i = 0; i < Tokens.size(); i++)
		{
			if (Tokens[i].getTokenType() != "COMMENT")
			{
				noCommentTokens.push_back(Tokens[i]);
			}
		}
		return noCommentTokens;
	}



#endif
