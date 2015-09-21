#ifndef PREDICATE_CPP
#define PREDICATE_CPP
#include <string>
#include <vector>
#include <iostream>
#include "Token.h"
#include "Parameter.cpp"

using namespace std;

class Predicate {
	
	public:
	
		Predicate ()
		{
			
		}
		Predicate(string head)
		{
			this->head = head;
		}
		
		string getHead()
		{
			return head;
		}
		
		void setHead(string head)
		{
			this->head = head;
		}
		
		vector<Parameter> getParameters()
		{
			return parameters;
		}
		
		void addParameter(Parameter p1)
		{
			parameters.push_back(p1);
		}
		
		void setParameters(vector<Parameter> parameters)
		{
			this->parameters = parameters;
		}
		
		void reset()
		{
			parameters.clear();
			head = "";
		}
		
		void toString()
		{
			cout << head << "(";
			for (unsigned int i = 0; i < parameters.size(); i++)
			{
				cout << parameters[i].getParameter();
				if (i < parameters.size()-1)
				{
					cout << ",";
				}
			}
			cout << ")";
		}
		
		vector<string> getNumID()
		{
			vector<string> IDs;
			for (unsigned int i = 0; i < parameters.size(); i++)
			{
				if (!parameters[i].isAString())
				{
					IDs.push_back(parameters[i].getParameter());
				}
			}
			return IDs;
		}
	
	
	private:
	
		string head;
		vector<Parameter> parameters;
	
	
};



#endif
