#ifndef RULE_CPP
#define RULE_CPP
#include <string>
#include <iostream>
#include <vector>
#include "Predicate.cpp"

using namespace std;


class Rule {
	
	private:
	
		Predicate head;
		vector<Predicate> PredicateList;
	
	public:
	
		Rule()
		{
		}
		
		Rule(Predicate head)
		{
			this->head = head;
		}
		
		void toString()
		{
			head.toString();
			cout << " :- ";
			for (unsigned int i = 0; i < PredicateList.size(); i++)
			{
				PredicateList[i].toString();
				if (i < PredicateList.size()-1)
					cout << ",";
			}
			cout << ".";
		}
		
		vector<Predicate> getPredList()
		{
			return PredicateList;
		}
		
		void addPredicate(Predicate newRule)
		{
			PredicateList.push_back(newRule);
		}
		
		Predicate getRuleHead()
		{
			return head;
		}
};




#endif
