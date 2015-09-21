#ifndef DATABASE_CPP
#define DATABASE_CPP
#include "Relation.cpp"
#include "DatalogProgram.h"
//#include "Query.cpp"
#include <vector>
#include <string>
#include <map>

using namespace std;

class Database
{
	public:
	
		Database();
		Database(DatalogProgram dp1)
		{
			this->dp1 = dp1;
			for (unsigned int i = 0; i < dp1.getSchemes().size(); i++)
			{
				vector<string> strings;
				set<vector<string>> tuples;
				string head = dp1.Schemes[i].getHead();
				
				vector<string> schema = createSchemaFromParameters(dp1.Schemes[i].getParameters());
				
				//This gets all facts related with the current scheme
				for (unsigned int l = 0; l < dp1.Facts.size(); l++)
				{
					if (dp1.Facts[l].getHead() == head)
					{
						for (unsigned int k = 0; k < dp1.Facts[l].getParameters().size(); k++)
						{
							strings.push_back(dp1.Facts[l].getParameters()[k].getParameter());
						}
						tuples.insert(strings);
						strings.clear();
					}
				}
				
				Relation r1(head, schema, tuples);
				dbMap[head] = r1;
			}
			
			originalMap = getRuleMap();
			ruleMap = reverseRuleMap(originalMap);
			
			
			initializeDFS();
			postOrderNum = 0;
			for (map<int,set<int>>::iterator it = ruleMap.begin();
				it != ruleMap.end(); it++)
			{
				if (visitedMap[it->first] == false)
					DFS(it->first);
			}
			
			initializeDFS();
			while (!order.empty())
			{
				set<int> currentSCC;
				if (visitedMap[order.back()] == false)
				{
					//cout << "order: " << order.back() << endl;
					SCC.push_back(DFSWithSCC(order.back(), currentSCC));
				}
				order.pop_back();
			}
			
			//cout << "SCC: " << SCC.size() << endl;

		}
		
		//This creates the schema for a particular scheme (For snap(X,Y), would store X and Y
		//in schema
		vector<string> createSchemaFromParameters(vector<Parameter> parameters)
		{
			vector<string> schema;
			for (unsigned int i = 0; i < parameters.size(); i++)
			{
				schema.push_back(parameters[i].getParameter());
			}
			return schema;
		}
		
		
		//This creates a "tree" (so to speak) of the rules and how they are connected
		//RETURN: a map where FIRST = the rule number, SECOND = the rules that FIRST is related to
		map<int, set<int>> getRuleMap()
		{
			map<int, set<int>> ruleMap;
			for (unsigned int i = 0; i < dp1.Rules.size(); i++)
			{
				Rule currentRule = dp1.Rules.at(i);
				set<int> dependentRuleNums;
				for (unsigned int j = 0; j < currentRule.getPredList().size(); j++)
				{
					string nameOfDependentRule = currentRule.getPredList().at(j).getHead();
					for (unsigned int k = 0; k < dp1.Rules.size(); k++)
					{
						if (nameOfDependentRule == dp1.Rules.at(k).getRuleHead().getHead())
						{
							dependentRuleNums.insert(k);
						}
					}
				}
				ruleMap[i] = dependentRuleNums;
			}
			return ruleMap;
		}
		
		//This takes in a map and completely reverses the direction of the map
		map<int, set<int>> reverseRuleMap(map<int,set<int>> ruleMap)
		{
			map<int, set<int>> newRuleMap;
			for (map<int,set<int>>::iterator it = ruleMap.begin();
					it != ruleMap.end(); it++)
			{
				set<int> emptySet;
				newRuleMap[it->first] = emptySet;
				//set<int> dependentRuleNums;
				for (set<int>::iterator it1 = it->second.begin();
						it1 != it->second.end(); it1++)
				{
					newRuleMap[*it1].insert(it->first);
				}
			}
			return newRuleMap;
		}
					
		
		//Depth First Search
		/* DFS
		 * mark x as visited
		 * for its dependents
		 * 		if (y!visited)
		 * 			dfs(y)
		 * setpostorder
		 * postorder++;
		 */
		 
		 void initializeDFS()
		 {
			 for (map<int,set<int>>::iterator it = ruleMap.begin();
					it != ruleMap.end(); it++)
			 {
				visitedMap[it->first] = false;
			 }
		 }
			
		 
		 void DFS(int currentNode)
		 {
			 visitedMap[currentNode] = true;
			 for (set<int>::iterator it = ruleMap[currentNode].begin();
					it != ruleMap[currentNode].end(); it++)
			 {
				 if (visitedMap[*it] == false)
					DFS(*it);
			 }
			 postOrder[currentNode] = postOrderNum;
			 if (postOrderNum == order.size())
				order.push_back(currentNode);
			 postOrderNum++;
		 }
		 
		 set<int> DFSWithSCC(int currentNode, set<int> &currentSet)
		 {
			 visitedMap[currentNode] = true;
			 for (set<int>::iterator it = originalMap[currentNode].begin();
					it != originalMap[currentNode].end(); it++)
			 {
				 if (visitedMap[*it] == false)
					DFSWithSCC(*it, currentSet);
			 }
			 currentSet.insert(currentNode);
			 return currentSet;
		 }
		
		
		vector<int> solveOptimizedRules(vector<set<int>>)
		{
			vector<int> numTimesSCC;
			int numRuleEvaluations = 0;
			bool newFactAdded = false;
			
			//i = which rule we are on
			for (unsigned int i = 0; i < SCC.size(); i++)
			{
				numRuleEvaluations = 0;
				
				if (isTrivial(SCC.at(i)))
				{
					Rule currentRule = dp1.Rules.at(*SCC.at(i).begin());
					Relation finalRelation = answerRule(currentRule);
					unionFactsToDatabase(finalRelation);
					numRuleEvaluations++;
				}
				else
				{
					set<int>::iterator it = SCC.at(i).begin();
					do
					{
						numRuleEvaluations++;
						newFactAdded = false;
						for(set<int>::iterator it = SCC.at(i).begin(); it != SCC.at(i).end(); it++)
						{
							Rule currentRule = dp1.Rules.at(*it);
							
							Relation finalRelation = answerRule(currentRule);
							if (unionFactsToDatabase(finalRelation))
								newFactAdded = true;
						}
					} while (newFactAdded == true);
				}
				numTimesSCC.push_back(numRuleEvaluations);
			}
			return numTimesSCC;
		}
		
		
		bool isTrivial(set<int> SCC)
		{
			if (SCC.size() == 1)
			{
				set<int>::iterator it = SCC.begin();
				//cout << "size: " << originalMap[*it].size();
				set<int>::iterator it1 = originalMap[*it].begin();
				for(unsigned int i = 0; i < originalMap[*it].size(); i++)
				{
					if (*it == *it1)
					{
						return false;
					}
					it1++;
				}
				return true;
			}
			return false;
		}
		
		//Takes all the rules and adds facts to the database until there is nothing left to add
		//RETURN: number of times taken to evaluate the rules
		int createFactsFromRules()
		{
			
			int numRuleEvaluations = 0;
			bool newFactAdded = false;
			do {
				numRuleEvaluations++;
				newFactAdded = false;
				
				//i = which rule we are on
				for (unsigned int i = 0; i < dp1.Rules.size(); i++)
				{
					Rule currentRule = dp1.Rules.at(i);
					
					Relation finalRelation = answerRule(currentRule);
					if (unionFactsToDatabase(finalRelation))
						newFactAdded = true;
				}
				
			} while(newFactAdded);
			return numRuleEvaluations;
		}
		
		Relation answerRule(Rule currentRule)
		{
			vector<Predicate> predList = currentRule.getPredList();
			
			//Gets the first Predicate in this rule.
			//It will then continue joining new rules to this rule until it is the only
			//remaining
			Predicate tempPred = predList.at(0);
			Relation finalRelation = answerRelation(tempPred);
			
			for (unsigned int j = 1; j < predList.size(); j++)
			{
				Relation tempRelation = answerRelation(predList[j]);
				//finalRelation = finalRelation.join(finalRelation,tempRelation);
				finalRelation.join(tempRelation);
			}
			finalRelation.setName(currentRule.getRuleHead().getHead());
			
			
			set<string> uniqueID;
			vector<unsigned int> uniqueIDpos;
			vector<Parameter> parameters = currentRule.getRuleHead().getParameters();
			//Get where we need to project.
			for (unsigned int j = 0; j < parameters.size(); j++)
			{
				for (unsigned int k = 0; k < finalRelation.getSchema().size(); k++)
				{
					if (finalRelation.getSchema().at(k) == parameters[j].getParameter())
					{
						uniqueIDpos.push_back(k);
					}
				}
			}
			finalRelation = finalRelation.project(finalRelation, uniqueIDpos);
			//finalRelation.project(uniqueIDpos);
			return finalRelation;
		}
		
		bool unionFactsToDatabase(Relation &newFacts)
		{
			string name = newFacts.getName();
			unsigned int initialSize = dbMap[name].getSizeOfTuples();
			map<string, Relation>::iterator it1 = dbMap.find(name);
			//set<vector<string>> tempTuples = newFacts.getTuples();
			
			bool addedNewFacts = it1->second.unionFacts(newFacts, initialSize);
			
			/*
			for (set<vector<string>>::iterator it = tempTuples.begin(); 
							it != tempTuples.end(); ++it)
			{
				it1->second.addFact(*it);
			}
			if (it1->second.getSizeOfTuples() == initialSize)
				return false;
			else
				return true;
			return false;
			*/
			return addedNewFacts;
		}
		
		void answerQueries()
		{
			//Comment/uncomment these next two lines for AWESOME functionality
			vector<int> numRuleEvaluations = solveOptimizedRules(SCC);
			//int numRuleEvaluations = createFactsFromRules();
			
			//DEBUG PURPOSES
			//OUTPUTS GRAPH
			
			cout << "Dependency Graph\n";
			for (map<int,set<int>>::iterator it = originalMap.begin();
					it != originalMap.end(); it++)
			{
				cout << "R" << it->first << ":";
				for (set<int>::iterator it1 = it->second.begin();
						it1 != it->second.end(); it1++)
				{
					if (it1 != it->second.begin())
						cout << ",";
					cout << "R" << *it1;
				}
				cout << endl;
			}
			
			cout << "\nRule Evaluation\n";
			for (unsigned int i = 0; i < SCC.size(); i++)
			{
				cout << numRuleEvaluations.at(i) << " passes: ";
				for (set<int>::iterator it = SCC.at(i).begin(); it != SCC.at(i).end(); it++)
				{
					if (it != SCC.at(i).begin())
					{
						cout << ",";
					}
					cout << "R" << *it;
				}
				cout << endl;
			}
			
			cout << "\nQuery Evaluation\n";
			//cout << "Schemes populated after " << numRuleEvaluations <<
			//		" passes through the Rules.\n";
			//Go through each Query and answer them.
			for (unsigned int i = 0; i < dp1.Queries.size(); i++)
			{
				Predicate pred = dp1.Queries[i];
				//string head = dp1.Queries[i].getHead();
				//Relation returnRelation(head, dbMap[head].getSchema(), dbMap[head].getTuples());
				Relation returnRelation = answerRelation(pred);
				printResult(i, returnRelation, projected);
			}
		}
		
		Relation answerRelation(Predicate &pred)
		{
			map<string, int> uniqueIDs;
			vector<unsigned int> uniqueIDpos;
			set<string> uniqueID;
			
			string head = pred.getHead();
			set<vector<string>>* tuplePointer = dbMap[head].getTuplesPointer();
			Relation returnRelation(head, dbMap[head].getSchema(), *tuplePointer);
			
			//Check each individual element of the query.
			//If string select.
			//If ID look for repeats and select if found.  Remeber later for projecting
			for (unsigned int i = 0; i < pred.getParameters().size(); i++)
			{
								
				Parameter tempParam = pred.getParameters().at(i);
								
				if (tempParam.isAString())
				{
					//returnRelation = returnRelation.select(returnRelation, i,
													//pred.getParameters().at(i).getParameter());
					returnRelation.select(i,pred.getParameters().at(i).getParameter());
				}
				else
				{
					if(uniqueID.find(tempParam.getParameter()) == uniqueID.end())
					{
						uniqueID.insert(tempParam.getParameter());
						uniqueIDpos.push_back(i);
						//Go forward and look for repeats
						for (unsigned int j = i+1; j < pred.getParameters().size(); j++)
						{
							if (tempParam.getParameter() == pred.getParameters().at(j).getParameter())
							{
								//returnRelation = returnRelation.match(returnRelation,
									//i, j);
								  returnRelation.match(i,j);
							}
						}
					}
					returnRelation.rename(i, tempParam.getParameter());
				}
			}
			projected = false;
			
			if (!uniqueID.empty())
			{
				returnRelation = returnRelation.project(returnRelation, uniqueIDpos);
				projected = true;
			}	
			return returnRelation;
		}
		
		void printResult(int i, Relation returnRelation, bool projected)
		{
			if (!returnRelation.areTuplesEmpty())
			{
				dp1.Queries[i].toString();
				cout << "? Yes(" << returnRelation.getSizeOfTuples() << ")\n";
				if (projected)
					returnRelation.toString();
			}
			else
			{
				dp1.Queries[i].toString();
				cout << "? No\n";
			}
		}
		
			
		bool projected = false;
		
		
	private:
		DatalogProgram dp1;
	    map<string, Relation> dbMap;
		map<vector<string>, vector<Predicate>> queryMap;
		map<int, bool> visitedMap;
		map<int, set<int>> originalMap;
		map<int, set<int>> ruleMap;
		map<int, int> postOrder;
		vector<set<int>> SCC;
		unsigned int postOrderNum;
		vector<int> order;
	
	
};








#endif
