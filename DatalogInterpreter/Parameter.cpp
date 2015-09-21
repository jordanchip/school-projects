#ifndef PARAMETER_CPP
#define PARAMETER_CPP
#include <string>

using namespace std;

class Parameter {
	
	public:
	
		Parameter()
		{
		}
		
		Parameter(string parameter)
		{
			this->parameter = parameter;
		}
		
		string getParameter()
		{
			return parameter;
		}
		
		void setParameter(string parameter)
		{
			this->parameter = parameter;
		}
	
		void setString(bool temp)
		{
			isString = temp;
		}
		
		bool isAString()
		{
			return isString;
		}
		
		void reset()
		{
			parameter = "";
		}
	
	private:
	
	
		string parameter;
		bool isString = false;
	
	
	
};



#endif
