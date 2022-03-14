import java.io.File;


import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.regex.PatternSyntaxException;



public class CFG {
	 	 
	public static void main(String[] args) throws FileNotFoundException {
		
		ArrayList<String> nonterminals=new ArrayList<String>();
		ArrayList<ArrayList> variable_list=new ArrayList<ArrayList>();
		ArrayList<String> derivation_list=new ArrayList<String>();
		
		
		
		File file = new File("CFG.txt");	
		Scanner sc = new Scanner(file);
	    System.out.println("ENTER AN INPUT: ");
	    Scanner scanner=new Scanner(System.in);
	    String input=scanner.next();
		
		
		   // parse CFG.txt
		   while (sc.hasNextLine()) {
				
				String st=sc.next();
				ArrayList<String> variables=new ArrayList<String>();
				String[] arr=st.split(">");
				String[] var=arr[1].split("\\|");
				var=sortdescending(var);
				nonterminals.add(arr[0]);
				for(int i=0;i<var.length;i++) {
					
					variables.add(var[i]);
				}
				variable_list.add(variables);
		   }
		   
		
		   Tree root =new Tree(nonterminals.get(0).toString());
		    System.out.println();
		   CFG(input,variable_list,nonterminals);	  
		 
	}
	public static Tree ParseTree(ArrayList<String> derivation_list,ArrayList<ArrayList> variable_list,ArrayList<String> nonterminals,Tree tree) {
		   
		   for(int i=0;i<derivation_list.size()-1;i++) {
			   boolean ifChecked=false;
			   for(int j=0;j<nonterminals.size();j++) {
				   if(derivation_list.get(i).contains(nonterminals.get(j)) && ifChecked) {
					   ifChecked=true;
					   ArrayList<String> possibilities=new ArrayList<String>();
					   for(int m=0;m<variable_list.get(j).size();m++) {
						  String st= derivation_list.get(i).replaceFirst(nonterminals.get(j).toString(),variable_list.get(j).get(m).toString());
						   possibilities.add(st); 
					   }
					   for(int k=0;k<possibilities.size();k++) {
						   if(derivation_list.get(i+1).toString().equals(possibilities.get(k).toString())) {
							   
						   }
					   }
					  
				   }
			   }
		   }
		   return tree;
	   }
	
	 
	public static void CFG(String input,ArrayList<ArrayList> variable_list,ArrayList<String> nonterminals) {

		if(ifAccepted(input, variable_list,nonterminals)) {
			System.out.print("String is accepted "+ input+" belongs to language G1");
			System.out.println();
			System.out.println("----DERIVATION----");
			
			print_derivation_list(input,variable_list,nonterminals);
			//ParseTree();		   
		}
		else {
			System.out.print("String is rejected"+ input+" does not belong to language G1");
		}
		
	}
	// returns if given input belongs to the given context free grammar G1
	public static boolean ifAccepted(String input,ArrayList<ArrayList> variable_list,ArrayList<String> nonterminals) {
		boolean ifAccepted=false;
		if(ifhasEmpty(variable_list)) {
			if((finalTransform(input,variable_list,nonterminals)).equals(nonterminals.get(0))) {
				return ifAccepted=true;
			}
			return ifAccepted;
		}
		else {
			if((finalTransform_Emptystring(input,variable_list,nonterminals)).equals(nonterminals.get(0))) {
				return ifAccepted=true;
			}
			return ifAccepted;
		}
		
	}
	// recursion function returns final transform of the given input by using grammar rules
	public static String finalTransform(String input,ArrayList<ArrayList> variable_list,ArrayList<String> nonterminals) {
		String temp="";
		temp=input;
		for(int i=(variable_list.size()-1);i>=0;i--) {
			for(int j=0;j<variable_list.get(i).size();j++) {
				if(input.contains(variable_list.get(i).get(j).toString())) {
					temp=finalTransform(transformed(input,variable_list.get(i).get(j).toString(),nonterminals.get(i).toString()),variable_list,nonterminals);
					return temp;
				}
			}
		}
		return temp;
	}
	//do not forget to find terminals can turn to empty string
	public static String finalTransform_Emptystring(String input,ArrayList<ArrayList> variable_list,ArrayList<String> nonterminals) {
		String temp="";
		temp=input;
		for(int i=(variable_list.size()-1);i>=0;i--) {
			for(int j=0;j<variable_list.get(i).size();j++) {
				if(input.contains(variable_list.get(i).get(j).toString())) {
					temp=finalTransform(transformed(input,variable_list.get(i).get(j).toString(),nonterminals.get(i).toString()),variable_list,nonterminals);
					return temp;
				}
				// a+a*a                B> #| aA
				else if(variable_list.get(i).get(j).equals("#")){
					ArrayList<String> list= emptystring_control(temp,nonterminals.get(i));
					for(int k=0;k<list.size();k++) {
						if(temp.contains(list.get(k))) {
							temp=finalTransform(transformed(input,variable_list.get(i).get(j).toString(),nonterminals.get(i).toString()),variable_list,nonterminals);
							return temp;
						}
					}
					
				}
			}
		}
		return temp;
	}
	public static boolean ifhasEmpty(ArrayList<ArrayList> variable_list) {
		boolean flag=false;
		for(int i=0;i<variable_list.size();i++) {
			for(int j=0;j<variable_list.get(i).size();j++) {
				if(variable_list.get(i).contains("#")) {
					return flag=true;
				}
			}
		}
		return flag;
	}
	// creates all values adding an empty string to input string's indexes' any location and returns all of them in a list 
	//this method will be used in empty_string _derivation_check() method
	public static ArrayList<String> emptystring_control(String input,String target){
		String tempstring="";
		
		ArrayList<String> list=new ArrayList();
		for(int i=0;i<input.length();i++) {
			if(i==0) {
				tempstring=target.concat(input);
				list.add(tempstring);
			}
			else if(i==input.length()-1){
				tempstring=input.concat(target);
				list.add(tempstring);
			}
			else {
				tempstring=input.substring(0,i).concat(target).concat(input.substring(i,input.length()));
				list.add(tempstring);
			}
		   
		}
		return list;
	}
	//derivation part returns derivation steps
	public static ArrayList<String> derivation_list(String input,ArrayList<ArrayList> variable_list,ArrayList<String> nonterminals){

		int count=0;
		String temp_string=input;
		ArrayList<String> arr=new ArrayList<String>();
		if(ifhasEmpty(variable_list)){
           while(count<1000) {
				
				arr.add(temp_string);
				temp_string=empty_string_derivation_check(temp_string,variable_list,nonterminals);
				if(temp_string.equals(nonterminals.get(0).toString())) {
					
					arr.add(temp_string);
					break;
				}
			}
			return reverselist(arr);
		}
		else{
			while(count<1000) {
				
				arr.add(temp_string);
				temp_string=derivation_check(temp_string,variable_list,nonterminals);
				if(temp_string.equals(nonterminals.get(0).toString())) {
					
					arr.add(temp_string);
					break;
				}
			}
			return reverselist(arr);
		}
		
	}
	//prints derivation steps
	public static void print_derivation_list(String input,ArrayList<ArrayList> variable_list,ArrayList<String> nonterminals) {
		for(int i = 0; i <derivation_list(input,variable_list,nonterminals).size(); i++) {
            System.out.print(derivation_list(input,variable_list,nonterminals).get(i));
                if(i<derivation_list(input,variable_list,nonterminals).size()-1) {
                	System.out.print(" --> ");
                }
                
            
        }
	}
	

	// returns only one step change of the given string
	public static String derivation_check(String input,ArrayList<ArrayList> variable_list,ArrayList<String> nonterminals) {
		String temp="";
		temp=input;
		for(int i=(variable_list.size()-1);i>=0;i--) {
			for(int j=0;j<variable_list.get(i).size();j++) {
				if(input.contains(variable_list.get(i).get(j).toString())) {
					return temp=transformed(input,variable_list.get(i).get(j).toString(),nonterminals.get(i).toString());
					
				}
			}
		}
		return temp;
	}
	//returns one step derivation with if the language has empty string transfrom possibility
	public static String empty_string_derivation_check(String input,ArrayList<ArrayList> variable_list,ArrayList<String> nonterminals) {
		String temp="";
		temp=input;
		for(int i=(variable_list.size()-1);i>=0;i--) {
			for(int j=0;j<variable_list.get(i).size();j++) {
				if(input.contains(variable_list.get(i).get(j).toString())) {
					return temp=transformed(input,variable_list.get(i).get(j).toString(),nonterminals.get(i).toString());
					
				}
				else if(variable_list.get(i).get(j).equals("#")){
					ArrayList<String> list= emptystring_control(temp,nonterminals.get(i));
					for(int k=0;k<list.size();k++) {
						if(temp.contains(list.get(k))) {
							return temp=transformed(input,variable_list.get(i).get(j).toString(),nonterminals.get(i).toString());
							
						}
					}
					
				}
			}
		}
		return temp;
	}
	
	// replacefirst() method including all special characters [\\,.,[,],{,},(,),<,>,*,+,-,=,!,?,^,$,|]
	public static String alter(String input,int start_index, int end_index,String target) {

		String temp="";
		int counter=0;
		if(start_index==0) {
			for(int j=0;j<input.length();j++) {
				if(j<=end_index) {
					if(j==end_index) {
						for(int i=0;i<target.length();i++) {
							temp+=target.charAt(i);
						}
					}
					
				}
				else if(j>end_index) {
										
					temp+=input.charAt(j);
				}
			
			}
		}
		else if(end_index==(input.length()-1)) {
			for(int j=0;j<input.length();j++) {
				if(j<start_index) {
					temp+=input.charAt(j);
					
				}
				else if(j==start_index) {
									
					for(int i=0;i<target.length();i++) {
						temp+=target.charAt(i);
					}
				}
			
			}
		}
		else {
			for(int j=0;j<input.length();j++) {
				if(j<start_index) {
					temp+=input.charAt(j);
				}
				else if(j>end_index) {
					if((j-end_index)==1) {
						for(int i=0;i<target.length();i++) {
							temp+=target.charAt(i);
						}
					}
					
					temp+=input.charAt(j);
				}
			
			}
		}
		
		return temp;
	}
	// returns transformed string for one step change
	public static String transformed(String input,String searched,String target) { 
		if(input.contains(searched)) {
			return alter(input,input.indexOf(searched),input.indexOf(searched)+(searched.length()-1),target);		
		}
		return input;
		
		
	}
	 
	
    
    // sort string array as descending order
    public static String[] sortdescending(String[] arr) {
    	   String temp = "";  
    	 //Sort the array in descending order    
           for (int i = 0; i < arr.length; i++) {     
               for (int j = i+1; j < arr.length; j++) {     
                  if(arr[i].length() < arr[j].length()) {    
                      temp = arr[i];    
                      arr[i] = arr[j];    
                      arr[j] = temp;    
                  }     
               }     
           }    
    	return arr;
    }
    public static boolean ifHaveNonterminal(String input,ArrayList<String> nonterminals) {
    	boolean flag=false;
    	for(int i=0;i<nonterminals.size();i++) {
    		if(input.contains(nonterminals.get(i).toString())) {
    			return flag=true;
    		}
    	}
    	return flag;
    }
    public static int nonterminal_counter(String input,ArrayList<String> nonterminals) {
    	int counter=0;
    	boolean flag=false;
    	for(int i=0;i<nonterminals.size();i++) {
    		if(input.contains(nonterminals.get(i).toString())) {
    			counter++;
    		}
    	}
    	return counter;
    }
    static void printArraystring(String str[], int n)
    {
        for (int i=0; i<n; i++)
            System.out.print(str[i]+" ");
    }
    public static ArrayList<String> reverselist(ArrayList<String> derivation_list){
    	ArrayList<String> arr=new ArrayList<String>();
    	for(int i=derivation_list.size()-1;i>=0;i--) {
    		arr.add(derivation_list.get(i));
    	}
    	return arr;
    }
    
}
