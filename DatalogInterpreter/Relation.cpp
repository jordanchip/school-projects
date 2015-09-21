#ifndef RELATION_CPP
#define RELATION_CPP
#include <string>
#include <vector>
#include <set>
#include <map>
#include "Predicate.cpp"
#include <utility>



class Relation
{
	public:
		Relation()
		{
		}
		Relation(string name, vector<string> schema, set<vector<string>> tuples)
		{
			this->name = name;
			this->schema = schema;
			this->tuples = tuples;
		}
		
		
		//Selects on a certain position for a certain string
		Relation select(Relation temp, unsigned int pos, string match)
		{
			set<vector<string>> newTuples;
			Relation returnRelation(temp.getName(), temp.getSchema(), newTuples);
			for (set<vector<string>>::iterator it = temp.tuples.begin(); it != temp.tuples.end(); ++it)
			{
				for (unsigned int i = 0; i < it->size(); i++)
				{
					if (i == pos)
					{
						if(it->at(i) == match)
						{
							returnRelation.tuples.insert(*it);
						}
					}
				}	
			}
			return returnRelation;
		}
		
		void select(unsigned int pos, string match)
		{
			set<vector<string>> newTuples;
			for (set<vector<string>>::iterator it = tuples.begin(); it != tuples.end(); ++it)
			{
				for (unsigned int i = 0; i < it->size(); i++)
				{
					if (i == pos)
					{
						if(it->at(i) == match)
						{
							newTuples.insert(*it);
						}
					}
				}	
			}
			tuples = newTuples;
		}
		
		Relation select(Relation temp, unsigned int pos)
		{
			return temp;
		}
		
		//Renames schema to newName at pos.
		/*
		Relation rename(unsigned int pos, string newName)
		{
			this->schema.at(pos) = newName;
			return *this;
		}
		*/
		void rename(unsigned int pos, string newName)
		{
			schema.at(pos) = newName;
		}

		//Matches on 2 positions and then selects there
		Relation match(Relation &temp, unsigned int pos1, unsigned int pos2)
		{
			set<vector<string>> newTuples;
			Relation returnRelation(temp.getName(), temp.getSchema(), newTuples);
			for (set<vector<string>>::iterator it = temp.tuples.begin(); it != temp.tuples.end(); ++it)
			{
				if(it->at(pos1) == it->at(pos2))
				{
					
					returnRelation.tuples.insert(*it);
				}
			}
			return returnRelation;
		}
		
		void match(unsigned int pos1, unsigned int pos2)
		{
			set<vector<string>> newTuples;
			for (set<vector<string>>::iterator it = tuples.begin(); it != tuples.end(); ++it)
			{
				if(it->at(pos1) == it->at(pos2))
				{
					newTuples.insert(*it);
				}
			}
			tuples = newTuples;
		}
		
		//Returns a relation containing tuples whose parameters only match
		//one of the uniqueIDpos
		Relation project(Relation &temp, vector<unsigned int> uniqueIDpos)
		{
			set<vector<string>> newTuples;
			vector<string> Tuple;
			vector<string> newSchema;
			Relation returnRelation(temp.getName(), temp.getSchema(), newTuples);
			for (set<vector<string>>::iterator it = temp.tuples.begin(); it != temp.tuples.end(); ++it)
			{
				for (unsigned int i = 0; i < uniqueIDpos.size(); i++)
				{
					for (unsigned int j = 0; j < it->size(); j++)
					{
						if(uniqueIDpos[i] == j)
						{
							Tuple.push_back(it->at(j));
						}
					}
				}
				returnRelation.tuples.insert(Tuple);
				Tuple.clear();
			}
			
			for (unsigned int i = 0; i < uniqueIDpos.size(); i++)
			{
				newSchema.push_back(temp.getSchema().at(uniqueIDpos.at(i)));
			}
			returnRelation.setSchema(newSchema);
			
			return returnRelation;
		}
		
		void project(vector<unsigned int> uniqueIDpos)
		{
			set<vector<string>> newTuples;
			vector<string> Tuple;
			vector<string> newSchema;
			for (set<vector<string>>::iterator it = tuples.begin(); it != tuples.end(); ++it)
			{
				for (unsigned int i = 0; i < uniqueIDpos.size(); i++)
				{
					for (unsigned int j = 0; j < it->size(); j++)
					{
						if(uniqueIDpos[i] == j)
						{
							Tuple.push_back(it->at(j));
						}
					}
				}
				tuples.insert(Tuple);
				Tuple.clear();
			}
			
			for (unsigned int i = 0; i < uniqueIDpos.size(); i++)
			{
				newSchema.push_back(schema.at(uniqueIDpos.at(i)));
			}
			setSchema(newSchema);
			for (unsigned int i = 0; i < schema.size(); i++)
			{
				cout << schema.at(i) << endl;
			}
		}
		
		Relation join(Relation &r1, Relation &r2)
		{
			//PosToCheck contains all the positions where the r1 schema matches the r2 schema
			vector<pair<unsigned int, unsigned int>> posToCheck;
			vector<string> r1Param = r1.getSchema();
			vector<string> r2Param = r2.getSchema();
			
			for (unsigned int i = 0; i < r1Param.size(); i++)
			{
				for (unsigned int j = 0; j < r2Param.size(); j++)
				{
					if (r1Param.at(i) == r2Param.at(j))
					{
						posToCheck.push_back(make_pair(i, j));
					}
				}
			}
			
			
			//Now go through the tuples and get all relations where we need
			//set<vector<string>> r1Tuples = r1.tuples;
			//set<vector<string>> r2Tuples = r2.tuples;
			set<vector<string>> newTuples;
			
			vector<string> newSchema = joinSchema(r1Param, r2Param, posToCheck);
			
			for (set<vector<string>>::iterator it = r1.tuples.begin(); 
					it != r1.tuples.end(); ++it)
			{
				for (set<vector<string>>::iterator it2 = r2.tuples.begin(); 
					it2 != r2.tuples.end(); ++it2)
				{
					if (joinable(posToCheck, *it, *it2))
					{
						vector<string> newTuple;
						//Create a new Schema.
						//First copy the whole schema from r1
						//Then get every unique parameter from schema of r2
						newTuple = *it;
						/*
						for (unsigned int i = 0; i < r1Param.size(); i++)
						{
							//newSchema.push_back(r1Param.at(i));
							newTuple.push_back(it->at(i));
						}
						*/
						unsigned int temp = r2Param.size();
						for (unsigned int i = 0; i < temp; i++)
						{
							bool okToAdd = true;
							for (unsigned int j = 0; j < posToCheck.size(); j++)
							{
								if (i == posToCheck.at(j).second)
								{
									okToAdd = false;
								}
							}
							if (okToAdd)
							{
								//newSchema.push_back(r2Param.at(i));
								newTuple.push_back(it2->at(i));
							}
						}
						newTuples.insert(newTuple);
					}
				}
			}
			//r1.tuples = newTuples;
			//r1.setSchema;
			Relation returnRelation("temp",newSchema,newTuples);		
			return returnRelation;
		}
		
		void join(Relation &r2)
		{
			//PosToCheck contains all the positions where the r1 schema matches the r2 schema
			vector<pair<unsigned int, unsigned int>> posToCheck;
			vector<string> r2Param = r2.getSchema();
			
			for (unsigned int i = 0; i < schema.size(); i++)
			{
				for (unsigned int j = 0; j < r2Param.size(); j++)
				{
					if (schema.at(i) == r2Param.at(j))
					{
						posToCheck.push_back(make_pair(i, j));
					}
				}
			}
			
			//Now go through the tuples and get all relations where we need
			set<vector<string>> newTuples;
			
			vector<string> newSchema = joinSchema(schema, r2Param, posToCheck);
			
			for (set<vector<string>>::iterator it = tuples.begin(); 
					it != tuples.end(); ++it)
			{
				for (set<vector<string>>::iterator it2 = r2.tuples.begin(); 
					it2 != r2.tuples.end(); ++it2)
				{
					if (joinable(posToCheck, *it, *it2))
					{
						vector<string> newTuple = *it;
						//Create a new Schema.
						//First copy the whole schema from r1
						//Then get every unique parameter from schema of r2
						//newTuple = *it;
						/*
						for (unsigned int i = 0; i < r1Param.size(); i++)
						{
							//newSchema.push_back(r1Param.at(i));
							newTuple.push_back(it->at(i));
						}
						*/
						for (unsigned int i = 0; i < r2Param.size(); i++)
						{
							bool okToAdd = true;
							for (unsigned int j = 0; j < posToCheck.size(); j++)
							{
								if (i == posToCheck.at(j).second)
								{
									okToAdd = false;
								}
							}
							if (okToAdd)
							{
								//newSchema.push_back(r2Param.at(i));
								newTuple.push_back(it2->at(i));
							}
						}
						newTuples.insert(newTuple);
					}
				}
			}
			setSchema(newSchema);
			tuples = newTuples;
			//Relation returnRelation("temp",newSchema,newTuples);		
			//return returnRelation;
		}
		
		bool joinable(vector<pair<unsigned int, unsigned int>> &posToCheck, vector<string> r1,
						vector<string> r2)
		{
			for (unsigned int i = 0; i < posToCheck.size(); i++)
			{
				if (r1.at(posToCheck.at(i).first) != r2.at(posToCheck.at(i).second))
				{
					return false;
				}
			}
			return true;
		}
		
		vector<string> joinSchema(vector<string> &r1, vector<string> &r2, 
									vector<pair<unsigned int, unsigned int>> &posToCheck)
		{
			vector<string> returnSchema;
			for (unsigned int i = 0; i < r1.size(); i++)
			{
				returnSchema.push_back(r1.at(i));
			}
			for (unsigned int i = 0; i < r2.size(); i++)
			{
				bool addSchema = true;
				for (unsigned int j = 0; j < posToCheck.size(); j++)
				{
					if (i == posToCheck.at(j).second)
					{
						addSchema = false;
					}
				}
				if (addSchema)
					returnSchema.push_back(r2.at(i));
			}
			return returnSchema;
		}				
		
		
		bool unionFacts(Relation &newFacts, unsigned int initialSize)
		{
			for (set<vector<string>>::iterator it = newFacts.tuples.begin(); 
							it != newFacts.tuples.end(); ++it)
			{
				addFact(*it);
			}
			if (getSizeOfTuples() == initialSize)
				return false;
			else
				return true;
			return false;
		}
			
		
		void toString()
		{
			for (set<vector<string>>::iterator it = tuples.begin(); it != tuples.end(); ++it)
			{
				cout << "  ";
				for (unsigned int i = 0; i < it->size(); i++)
				{
					cout << schema[i] << "=";
					cout << it->at(i);
					if (i < it->size() -1)
						cout << ", ";
						
				}
				cout << "\n";
			}
		}
		
		void toSchemaString()
		{
			cout << name << "(";
			for (unsigned int i = 0; i < schema.size(); i++)
			{
				cout << schema.at(i);
				if (i < schema.size() - 1)
					cout << ",";
			}
			cout << ")\n";
			for (set<vector<string>>::iterator it = tuples.begin(); it != tuples.end(); ++it)
			{
				cout << "  ";
				for (unsigned int i = 0; i < it->size(); i++)
				{
					cout << schema[i] << "=";
					cout << it->at(i);
					if (i < it->size() -1)
						cout << ", ";
						
				}
				cout << "\n";
			}
		}
		
		string getName()
		{
			return name;
		}
		void setName(string newName)
		{
			name = newName;
		}
		vector<string> getSchema()
		{
			return schema;
		}
		
		void setSchema(vector<string> schema)
		{
			this->schema = schema;
		}
		
		set<vector<string>> getTuples()
		{
			return tuples;
		}
		
		set<vector<string>>* getTuplesPointer()
		{
			return &tuples;
		}
		
		void addFact(vector<string> newFact)
		{
			tuples.insert(newFact);
		}
		
		unsigned int getSizeOfTuples()
		{
			return tuples.size();
		}
		
		bool areTuplesEmpty()
		{
			return tuples.empty();
		}
	
	private:
		string name;
		vector<string> schema;
		set<vector<string>> tuples;
	
	
	
};





#endif
