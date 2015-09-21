#ifndef DATALOG_H
#define DATALOG_H
#include <vector>
#include <string>
#include "Rule.cpp"
#include "Parameter.cpp"
#include "Token.h"
#include "Rule.cpp"
#include "Predicate.cpp"
#include <set>

using namespace std;

class DatalogProgram {
	
	private:
		//vector<Predicate> Schemes;
		//vector<Predicate> Facts;
		//vector<Rule> Rules;
		//vector<Predicate> Queries;
		//vector<Parameter> Domain;
		//set<string> DomainStrings;
		vector<Token> Tokens;
		Predicate predTemp;
		Predicate headPredicateTemp;
		Rule ruleTemp;
		Parameter temp;
		string expressionTemp;
		
	
	public:
	
		vector<Predicate> Schemes;
		vector<Predicate> Facts;
		vector<Rule> Rules;
		vector<Predicate> Queries;
		vector<Parameter> Domain;
		set<string> DomainStrings;
		
		
		bool parseTokens();
		
		//These are the functions which represent the various parts of the grammar
		bool checkDATALOG();
		bool checkLINEONE();
		bool checkLINETWO();
		bool checkLINETHREE();
		bool checkLINEFOUR();
		bool checkSCHEME();
		bool checkSCHEMELIST();
		bool checkIDLIST();
		bool checkFACT();
		bool checkFACTLIST();
		bool checkRULE();
		bool checkRULELIST();
		bool checkHEADPREDICATE();
		bool checkPREDICATERULES();
		bool checkPREDICATEQUERY();
		bool checkPREDICATELIST();
		bool checkPARAMETER();
		bool checkPARAMETERLIST();
		bool checkEXPRESSION();
		bool checkOPERATOR();
		bool checkQUERY();
		bool checkQUERYLIST();
		bool checkSTRINGLIST();
		
		
		bool matchADD();
		bool matchCOLON();
		bool matchCOLONDASH();
		bool matchCOMMA();
		bool matchFACTS();
		bool matchID();
		bool matchLEFTPAREN();
		bool matchMULTIPLY();
		bool matchPERIOD();
		bool matchQMARK();
		bool matchQUERIES();
		bool matchRIGHTPAREN();
		bool matchRULES();
		bool matchSCHEME();
		bool matchSTRING();

		//End functions
		
		//Gets and sets
		vector<Predicate> getSchemes();
		vector<Predicate> getQueries();
		vector<Predicate> getFacts();
		
		void toString();
		DatalogProgram();
		DatalogProgram(vector<Token> Tokens);
		void parse(Token Tokens);
		void addScheme(Predicate p1);
		Token peek();
		vector<Token> removeComments();
		
		unsigned int current;
};


#endif
