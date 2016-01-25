package dbmsPachage;
 
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
 
public class Parser {
	Boolean isSelected = false;
	String TableName = "";
	String Columns[] = new String[10000];
	String Values[] = new String[10000];
	String Types[] = new String[10000];
	String[] Column_to_be_updated = new String[10000];
	String[] values_to_be_updated = new String[10000];
	String[] parts = new String[10000];
	String[] o = new String[10000];
	String[][] output = new String[1000][1000];
	String Type = "";
	String Condition = "";
	String Column = "";
	String Value = "";
	int Operation;
	String DataBaseName = "";
	String Column_condition = "";
	String Values_condition = "";
	DBMS D;
	Boolean Equ = true,isArray;
 
	public void init() {
		Arrays.fill(Columns, "");
		Arrays.fill(Values, "");
		Arrays.fill(Types, "");
		Arrays.fill(Column_to_be_updated, "");
		Arrays.fill(values_to_be_updated, "");
		Arrays.fill(parts, "");
		Arrays.fill(o, "");
		for (int i = 0; i < 1000; i++) {
			Arrays.fill(output[i], "");
		}
		TableName = "";
		Column = "";
		Type = "";
		Condition = "";
		Value = "";
		Column_condition = "";
		Values_condition = "";
		DataBaseName = "";
		Equ = true;
	}
 
	public Parser(DBMS D) {
		this.D = D;
		init();
	}
 
	public String getTableName() {
		return TableName;
	}
 
	public String parser(String s) {
		init();
		isSelected = false;
		if (SelectFrom(s)) {
			isSelected = true;
			try {
				return D.selectAllTable(TableName);
			} catch (Exception e) {
				e.printStackTrace();
				return DBMS.PARSING_ERROR;
			}
		} else if (SelectColumnFrom(s)) {
			isSelected = true;
			try {
				return D.selectColumnfromTable(TableName, Columns);
			} catch (Exception e) {
				e.printStackTrace();
				return DBMS.PARSING_ERROR;
			}
		} else if (SelectFromWhere(s)) {
			isSelected = true;
			try {
				return D.selectrow(TableName, Column, Value, Type, Operation);
			} catch (Exception e) {
				e.printStackTrace();
				return DBMS.PARSING_ERROR;
			}
		} else if (SelectColumnFromWhere(s)) {
			isSelected = true;
			try {
				return D.selectRowFromColumn(TableName, Columns, Column, Value,
						Type, Operation);
			} catch (Exception e) {
				e.printStackTrace();
				return DBMS.PARSING_ERROR;
			}
		} else if (Delete(s)) {
			try {
				return D.delete(TableName, Column, Value, Type, Operation,
						false);
			} catch (Exception e) {
				e.printStackTrace();
				return DBMS.PARSING_ERROR;
			}
		} else if (DeleteAllTable(s)) {
			try {
				return D.delete(TableName);
			} catch (Exception e) {
				e.printStackTrace();
				return DBMS.PARSING_ERROR;
			}
 
		} else if (InsertIntoTableWithout(s)) {
			try {
				return D.insertRow(TableName, Columns, Values, Types, false);
			} catch (Exception e) {
				e.printStackTrace();
				return DBMS.PARSING_ERROR;
			}
		} else if (InsertIntoTableWith(s)) {
			try {
				return D.insertRow(TableName, Columns, Values, Types, false);
			} catch (Exception e) {
				e.printStackTrace();
				return DBMS.PARSING_ERROR;
			}
 
		} else if (CreateDataBase(s)) {
			try {
				return D.CreateDatabase(DataBaseName);
			} catch (Exception e) {
				e.printStackTrace();
				return DBMS.PARSING_ERROR;
			}
 
		} else if (CreateTable(s)) {
			try {
				return D.CreateTable(TableName, Columns, Types);
			} catch (Exception e) {
				e.printStackTrace();
				return DBMS.PARSING_ERROR;
			}
		} else if (Update(s)) {
			try {
				return D.update(TableName, Column_to_be_updated,
						values_to_be_updated, Types, Column_condition,
						Values_condition, Type);
			} catch (Exception e) {
				e.printStackTrace();
				return DBMS.PARSING_ERROR;
			}
		}
		return DBMS.PARSING_ERROR;
	}
 
	public Boolean SelectFrom(String s) {
		Pattern P = Pattern
				.compile("^(\\s*)((?i)select)(\\s+)(\\*)(\\s+)((?i)from)(\\s+)(\\[|\\(|)([a-zA-Z0-9\\s]+)(\\]|\\)|)(\\s*)(\\;|)(\\s*)$");
		Matcher M = P.matcher(s);
		if (M.find()) {
			ParseSelectFrom(s);
			return true;
		}
		return false;
	}
 
	public Boolean SelectColumnFrom(String s) {
		Pattern P = Pattern
				.compile("^(\\s*)((?i)select)(\\s+)(\\w+)((\\s*))((,(\\s*))(\\w+)(\\s*)){0,}(\\s+)((?i)from)(\\s+)(\\[|\\(|)([a-zA-Z0-9\\s]+)(\\]|\\)|)(\\s*)(\\;|)(\\s*)$");
		Matcher M = P.matcher(s);
		if (M.find()) {
			ParseSelectColumnFrom(s);
			return true;
		}
		return false;
	}
 
	public Boolean SelectFromWhere(String s) {
		Pattern P = Pattern
				.compile("^(\\s*)((?i)select)(\\s+)(\\*)(\\s+)((?i)from)(\\s+)(\\[|\\(|)([a-zA-Z0-9\\s]+)(\\]|\\)|)(\\s+)((?i)where)(\\s+)([a-zA-Z0-9\\s]+)(\\s*)([=<>])(\\s*)(([\'|\"][a-zA-Z0-9\\s]+[\'|\"])|([0-9]{1,19}(\\.[0-9]*)?)|(([\'][{])\\s*(([\'|\"][a-zA-Z0-9\\s]+[\'|\"])|([0-9]{1,19}(\\.[0-9]*)?))(\\s*,\\s*(([\'|\"][a-zA-Z0-9\\s]+[\'|\"])|([0-9]{1,19}(\\.[0-9]*)?)))*\\s*([}][\'])))\\s*(\\;|)(\\s*)$");
		Matcher M = P.matcher(s);
		if (M.find()) {
			if (!ParseSelectFromWhere(s))
				return false;
			return true;
		}
		return false;
	}
 
	public Boolean SelectColumnFromWhere(String s) {
		Pattern P = Pattern
				.compile("^(\\s*)((?i)select)(\\s+)(\\w+)(\\s*)((,(\\s*))(\\w+)(\\s*)){0,}(\\s+)((?i)from)(\\s+)(\\[|\\(|)([a-zA-Z0-9\\s]+)(\\]|\\)|)(\\s+)((?i)where)(\\s+)(\\w+)(\\s*)([=<>])(\\s*)(([\'|\"][a-zA-Z0-9\\s]+[\'|\"])|([0-9]{1,19}(\\.[0-9]*)?)|(([\'][{])\\s*(([\'|\"][a-zA-Z0-9\\s]+[\'|\"])|([0-9]{1,19}(\\.[0-9]*)?))(\\s*,\\s*(([\'|\"][a-zA-Z0-9\\s]+[\'|\"])|([0-9]{1,19}(\\.[0-9]*)?)))*\\s*([}][\'])))\\s*(\\;|)(\\s*)$");
		Matcher M = P.matcher(s);
		if (M.find()) {
			if (!ParseSelectColumnFromWhere(s)) {
				return false;
			}
			return true;
		}
		return false;
	}
 
	public Boolean Delete(String s) {
		Pattern P = Pattern
				.compile("^(\\s*)((?i)delete)(\\s+)((?i)from)(\\s+)(\\[|\\(|)(\\w+)(\\]|\\)|)(\\s+)((?i)where)(\\s+)(\\w+)(\\s*)([=<>])(\\s*)(([\'|\"][a-zA-Z0-9\\s]+[\'|\"])|([0-9]{1,19}(\\.[0-9]*)?)|(([\'][{])\\s*(([\'|\"][a-zA-Z0-9\\s]+[\'|\"])|([0-9]{1,19}(\\.[0-9]*)?))(\\s*,\\s*(([\'|\"][a-zA-Z0-9\\s]+[\'|\"])|([0-9]{1,19}(\\.[0-9]*)?)))*\\s*([}][\'])))\\s*(\\;|)(\\s*)$");
		Matcher M = P.matcher(s);
		if (M.find()) {
			if (!ParseDelete(s))
				return false;
			return true;
		}
		return false;
	}
 
	public Boolean InsertIntoTableWithout(String s) {
		Pattern P = Pattern
				.compile("^\\s*((?i)insert)(\\s+)((?i)into)(\\s+)(\\[|)(\\w+)(\\]|)(\\s+)((?i)values)(\\s*)([(])\\s*((('[a-zA-Z0-9\\s]+')|([0-9]{1,19}(\\.[0-9]*)?))|(([\'][{])\\s*(([\'|\"][a-zA-Z0-9\\s]+[\'|\"])|([0-9]{1,19}(\\.[0-9]*)?))(\\s*,\\s*(([\'|\"][a-zA-Z0-9\\s]+[\'|\"])|([0-9]{1,19}(\\.[0-9]*)?)))*\\s*([}][\']))\\s*)(\\s*,\\s*(('[a-zA-Z0-9\\s]+')|([0-9]{1,19}(\\.[0-9]*)?)|(([\'][{])\\s*(([\'|\"][a-zA-Z0-9\\s]+[\'|\"])|([0-9]{1,19}(\\.[0-9]*)?))(\\s*,\\s*(([\'|\"][a-zA-Z0-9\\s]+[\'|\"])|([0-9]{1,19}(\\.[0-9]*)?)))*\\s*([}][\'])))\\s*)*([)])(\\s*)(\\;|)(\\s*)$");
		Matcher M = P.matcher(s);
		if (M.find()) {
			if(ParseInsertIntoWithout(s))return true;
			else return false;
		}
		return false;
	}
 
	public Boolean InsertIntoTableWith(String s) {
		Pattern P = Pattern
				.compile("^\\s*((?i)insert)(\\s+)((?i)into)(\\s+)(\\[|)(\\w+)(\\]|)(\\s*)([(])(\\s*\\w+\\s*)(,\\s*\\w+\\s*)*([)])\\s*((?i)values)(\\s*)([(])\\s*((('[a-zA-Z0-9\\s]+')|([0-9]{1,19}(\\.[0-9]*)?))|(([\'][{])\\s*(([\'|\"][a-zA-Z0-9\\s]+[\'|\"])|([0-9]{1,19}(\\.[0-9]*)?))(\\s*,\\s*(([\'|\"][a-zA-Z0-9\\s]+[\'|\"])|([0-9]{1,19}(\\.[0-9]*)?)))*\\s*([}][\']))\\s*)(\\s*,\\s*(('[a-zA-Z0-9\\s]+')|([0-9]{1,19}(\\.[0-9]*)?)|(([\'][{])\\s*(([\'|\"][a-zA-Z0-9\\s]+[\'|\"])|([0-9]{1,19}(\\.[0-9]*)?))(\\s*,\\s*(([\'|\"][a-zA-Z0-9\\s]+[\'|\"])|([0-9]{1,19}(\\.[0-9]*)?)))*\\s*([}][\'])))\\s*)*([)])(\\s*)(\\;|)(\\s*)$");
		Matcher M = P.matcher(s);
		if (M.find()) {
			if(ParseInsertIntoWith(s))return true;
			else return false;
		}
		return false;
	}
 
	public void ParseDeleteAllTable(String s) {
		for (int i = s.toLowerCase().indexOf("from") + 4; i < s.length(); i++) {
			if (s.charAt(i) != ' ') {
				for (int j = i; j < s.length() && s.charAt(j) != ';'; j++) {
					TableName += s.charAt(j);
					i++;
				}
			}
		}
		TableName = Clean(TableName, true);
 
	}
 
	public void ParseSelectFrom(String s) {
		for (int i = s.toLowerCase().indexOf("from") + 4; i < s.length(); i++) {
			if (s.charAt(i) != ' ') {
				for (int j = i; j < s.length() && s.charAt(j) != ';'; j++) {
					TableName += s.charAt(j);
					i++;
				}
			}
		}
		TableName = Clean(TableName, true);
	}
 
	public void ParseSelectColumnFrom(String s) {
		int ind = 0;
		for (int i = s.toLowerCase().indexOf("select") + 6; i < s.toLowerCase()
				.indexOf("from"); i++) {
			if (s.charAt(i) != ' ' && s.charAt(i) != ',') {
				String alt = "";
				for (int j = i; j < s.toLowerCase().indexOf("from")
						&& s.charAt(j) != ','; j++) {
					alt += s.charAt(j);
					i++;
				}
				Columns[ind] = alt;
				Columns[ind] = Clean(Columns[ind], true);	
				ind++;
			}
		}
		for (int i = s.toLowerCase().indexOf("from") + 4; i < s.length(); i++) {
			if (s.charAt(i) != ' ') {
				for (int j = i; j < s.length() && s.charAt(j) != ';'; j++) {
					TableName += s.charAt(j);
					i++;
				}
			}
		}
		TableName = Clean(TableName, true);
	}
 
	public Boolean ParseSelectFromWhere(String s) {
 
		for (int i = s.toLowerCase().indexOf("from") + 4; i < s.toLowerCase()
				.indexOf("where"); i++) {
			if (s.charAt(i) != ' ') {
				for (int j = i; j < s.toLowerCase().indexOf("where"); j++) {
					TableName += s.charAt(j);
					i++;
				}
			}
		}
		TableName = Clean(TableName, true);
 
		for (int i = s.toLowerCase().indexOf("where") + 5; i < s.length(); i++) {
			if (s.charAt(i) != ' ') {
				for (int j = i; j < s.length() && s.charAt(j) != ';'; j++) {
					Condition += s.charAt(j);
					i++;
				}
			}
		}
		int in = 0;
		for (int i = 0; i < Condition.length(); i++) {
			if (Condition.charAt(i) == '>') {
				in = i;
				Operation = 1;
				break;
			} else if (Condition.charAt(i) == '<') {
				in = i;
				Operation = -1;
				break;
			} else if (Condition.charAt(i) == '=') {
				in = i;
				Operation = 0;
				break;
			}
			Column += Condition.charAt(i);
		}
 
		Column = Clean(Column, true);
 
		Value = Condition.substring(in + 1, Condition.length());
 
		if(Value.contains("{") || Value.contains("}")){
			Equ = true;
			Type = Determin_Type(Value);
			Value=  Clean(Value, false);
			if(!Equ)return false;
		}else {
			Value=  Clean(Value, false);
			Type = Determin_Type(Value);
		}
 
		if (Type != "int" &&Type!="long" &&Type !="float" && (Operation == 1 || Operation == -1))
			return false;
		return true;
	}
 
	public Boolean ParseSelectColumnFromWhere(String s) {
 
		int ind = 0;
		for (int i = s.toLowerCase().indexOf("select") + 6; i < s.toLowerCase()
				.indexOf("from"); i++) {
			if (s.charAt(i) != ' ' && s.charAt(i) != ',') {
				String alt = "";
				for (int j = i; j < s.toLowerCase().indexOf("from")
						&& s.charAt(j) != ','; j++) {
					alt += s.charAt(j);
					i++;
				}
				Columns[ind] = alt;
				Columns[ind] = Clean(Columns[ind], true);
				ind++;
			}
		}
		for (int i = s.toLowerCase().indexOf("from") + 4; i < s.toLowerCase().indexOf("where"); i++) {
			TableName += s.charAt(i);
		}
 
		TableName = Clean(TableName, true);
 
		for (int i = s.toLowerCase().indexOf("where") + 5; i < s.length()
				&& s.charAt(i) != ';'; i++) {
			if (s.charAt(i) != ' ') {
				for (int j = i; j < s.length() && s.charAt(j) != ';'; j++) {
					Condition += s.charAt(j);
					i++;
				}
			}
		}
 
		int in = 0;
		for (int i = 0; i < Condition.length(); i++) {
			if (Condition.charAt(i) == '=') {
				in = i;
				Operation = 0;
				break;
			} else if (Condition.charAt(i) == '>') {
				in = i;
				Operation = 1;
				break;
			} else if (Condition.charAt(i) == '<') {
				in = i;
				Operation = -1;
				break;
			}
			Column += Condition.charAt(i);
		}
		Column = Clean(Column, true);
 
		Value = Condition.substring(in + 1, Condition.length());
		if(Value.contains("{") || Value.contains("}")){
			Equ = true;
			Type = Determin_Type(Value);
			Value=  Clean(Value, false);
			if(!Equ)return false;
		}else {
			Value=  Clean(Value, false);
			Type = Determin_Type(Value);
		}
		if (Type != "int" &&Type!="long" &&Type !="float" && (Operation == 1 || Operation == -1))
			return false;
		return true;
	}
 
	public Boolean ParseDelete(String s) {
 
		for (int i = s.toLowerCase().indexOf("from") + 4; i < s.toLowerCase()
				.indexOf("where"); i++) {
			if (s.charAt(i) != ' ') {
				for (int j = i; j < s.toLowerCase().indexOf("where"); j++) {
					TableName += s.charAt(j);
					i++;
				}
			}
		}
		TableName = Clean(TableName, true);
 
		for (int i = s.toLowerCase().indexOf("where") + 5; i < s.length(); i++) {
			if (s.charAt(i) != ' ') {
				for (int j = i; j < s.length() && s.charAt(j) != ';'; j++) {
					Condition += s.charAt(j);
					i++;
				}
			}
		}
 
		int in = 0;
		for (int i = 0; i < Condition.length(); i++) {
			if (Condition.charAt(i) == '=') {
				in = i;
				Operation = 0;
				break;
			} else if (Condition.charAt(i) == '>') {
				in = i;
				Operation = 1;
				break;
			} else if (Condition.charAt(i) == '<') {
				in = i;
				Operation = -1;
				break;
			}
			Column += Condition.charAt(i);
		}
		Column = Clean(Column, true);
 
 
		Value = Condition.substring(in + 1, Condition.length());
		if(Value.contains("{") || Value.contains("}")){
			Equ = true;
			Type = Determin_Type(Value);
			Value=  Clean(Value, false);
			if(!Equ)return false;
		}else {
			Value=  Clean(Value, false);
			Type = Determin_Type(Value);
		}
 
		if (Type != "int" &&Type!="long" &&Type !="float" && (Operation == 1 || Operation == -1))
			return false;
		return true;
	}
 
	public Boolean ParseInsertIntoWithout(String s) {
		for (int i = s.toLowerCase().indexOf("into") + 4; i < s.toLowerCase()
				.indexOf("values"); i++) {
			if (s.charAt(i) != ' ') {
				for (int j = i; j < s.toLowerCase().indexOf("values"); j++) {
					TableName += s.charAt(j);
					i++;
				}
			}
		}
 
		TableName = Clean(TableName, true);
 
		int ind = 0;
		for (int i = s.indexOf("(") + 1; i < s.indexOf(")"); i++) {
			if (s.charAt(i) != ' ' && s.charAt(i) != '\'' && s.charAt(i) != ','
					&& s.charAt(i) != ')' &&s.charAt(i)!=';') {
				if(s.charAt(i)=='{'){
					i++;
					for (int j = i;s.charAt(j) != '}'; j++) {
						Values[ind] += s.charAt(j);
						i++;
					}
					Equ = true;
					Types[ind] = Determin_Type(Values[ind] +"}" );
					if(!Equ)return false;
					i++;
				}else{
					for (int j = i; s.charAt(j) != ',' && s.charAt(j) != '\''
							&& s.charAt(j) != ')'; j++) {
						Values[ind] += s.charAt(j);
						i++;
					}
					Values[ind] = Clean(Values[ind], false);
					Types[ind] = Determin_Type(Values[ind]);
				}
				ind++;
			}
		}
		Columns[0] = "sa3ed";
		return true;
	}
 
	public Boolean ParseInsertIntoWith(String s) {
		for (int i = s.toLowerCase().indexOf("into") + 4; i < s.toLowerCase()
				.indexOf("values"); i++) {
			if (s.charAt(i) != ' ') {
				for (int j = i; j < s.toLowerCase().indexOf("("); j++) {
					TableName += s.charAt(j);
					i++;
				}
			}
		}
		TableName = Clean(TableName, true);
 
		int ind = 0;
		for (int i = s.indexOf("(") + 1; i < s.indexOf(")"); i++) {
			if (s.charAt(i) != ' ' && s.charAt(i) != ',' && s.charAt(i) != ')') {
				for (int j = i; s.charAt(j) != ',' && s.charAt(j) != ')'; j++) {
					Columns[ind] += s.charAt(j);
					i++;
				}
				Columns[ind] =Clean(Columns[ind], true);
 
				ind++;
			}
		}
		ind = 0;
		for (int t = s.toLowerCase().indexOf("values") + 6; t < s.length(); t++) {
			if (s.charAt(t) == '(') {
				for (int i = t + 1; i < s.length() && s.charAt(i)!=')'; i++) {
					if (s.charAt(i) != ' ' && s.charAt(i) != '\'' && s.charAt(i) != ','
							&& s.charAt(i) != ')' && s.charAt(i)!=';') {
						if(s.charAt(i)=='{'){
							i++;
							for (int j = i;s.charAt(j) != '}'; j++) {
								Values[ind] += s.charAt(j);
								i++;
							}
							Equ = true;
							Types[ind] = Determin_Type(Values[ind] + "}");
							if(!Equ)return false;
							i++;
						}else{
							for (int j = i; s.charAt(j) != ',' && s.charAt(j) != '\''
									&& s.charAt(j) != ')'; j++) {
								Values[ind] += s.charAt(j);
								i++;
							}
							Values[ind] = Clean(Values[ind], false);
							Types[ind] = Determin_Type(Values[ind]);
						}
						ind++;
					}
				}
				break;
			}
		}
		return true;
	}
 
	/*********** create data base and create table ************/
 
	public Boolean CreateDataBase(String s) {
		Pattern P = Pattern
				.compile("^(\\s*)((?i)create)(\\s+)((?i)database)(\\s+)([a-zA-Z0-9\\s]+)(\\s*)(\\;|)$");
 
		Matcher M = P.matcher(s);
		if (M.find()) {
			CDB_Parsing(s);
			return true;
		}
		return false;
	}
 
	public String CDB_Parsing(String original) {
		String array[] = original.split("\\s+");
		String DB_Name;
		if (array[0].matches("^\\s*$"))
			DB_Name = array[3];
		else
			DB_Name = array[2];
		if (DB_Name.charAt(DB_Name.length() - 1) == ';')
			DB_Name = DB_Name.substring(0, DB_Name.length() - 1);
		DataBaseName = DB_Name;
		return DB_Name;
	}
 
	public Boolean DeleteAllTable(String s) {
		Pattern P = Pattern
				.compile("^(\\s*)((?i)delete)(\\s+)((\\*\\s+)|(\\s*))((?i)from)(\\s+)(\\[|\\(|)(\\w+)(\\]|\\)|)(\\s*)(\\;|)(\\s*)$");
		Matcher M = P.matcher(s);
		if (M.find()) {
			ParseDeleteAllTable(s);
			return true;
		}
		return false;
	}
 
	public Boolean Update(String s) {
		Pattern P = Pattern
				.compile("^(\\s*)((?i)update)(\\s+)([a-zA-Z0-9\\s\\.\\-\\_]+)(\\s+)((?i)set)(\\s+)([a-zA-Z0-9\\s\\.\\-\\_]+)(\\s*)(=)(\\s*)(')(\\s*)(\\{)?(\\s*)([a-zA-Z0-9\\s\\.\\-\\_]+)(\\s*)(\\s*,\\s*[a-zA-Z0-9\\.\\-\\_]+\\s*)*(\\})?(\\s*)(')(\\s*)(((\\s*),(\\s*))([a-zA-Z0-9\\s\\.\\-\\_]+)(\\s*)(=)(\\s*)(')?(\\{)?(\\s*)([a-zA-Z0-9\\s\\.]+)(\\s*)(\\s*,\\s*[a-zA-Z0-9\\s\\.\\-\\_]+\\s*)*(\\})?(')?){0,}(\\s*)(((?i)where)(\\s+)([a-zA-Z0-9\\s\\.\\-\\_]+)(\\s*)(=)(\\s*)(')?(\\s*)([a-zA-Z0-9\\s\\.\\-\\_]+)(\\s*)(')?(\\s*))?(\\s*)(\\;|)\\s*$");
		Matcher M = P.matcher(s);
		if (M.find()) {
			parts = s
					.split("(?i)(update\\s*|\\s*set\\s*|\\s*where\\s*|\\s*;\\s*)");
			U_Parsing();
			return true;
		}
		return false;
	}
 
	public void U_Parsing() {
		int ind = 0;
		TableName = parts[1];
		String output[] = parts[2].split("(\\s*=\\s*)|(\\s*'\\s*)");
		for (int i = 0; i < output.length; i++) {
			if (!output[i].matches("\\s*"))
				o[ind++] = output[i];
		}
		for (int i = 0; i < ind; i++) {
			if (i % 2 == 0)
			{
				String temp="";
    			for(int j=0;j<o[i].length();j++)
    				if(o[i].charAt(j)>='A'&&o[i].charAt(j)<='Z'||o[i].charAt(j)>='a'&&o[i].charAt(j)<='z'||o[i].charAt(j)>='-'||o[i].charAt(j)>='_')
    				{
    					temp+=o[i].charAt(j);
    				}
    			Column_to_be_updated[i/2]=temp.toLowerCase();
			}
			else
			{
				String temp="'";
    			for(int j=0;j<o[i].length();j++)
    				if(o[i].charAt(j)=='{'||o[i].charAt(j)=='}')
    				{
    					continue;
    				}else
    					temp+=o[i].charAt(j);
    			temp+="'";
    			values_to_be_updated[i/2]=temp;
    			values_to_be_updated[i/2] = Clean(values_to_be_updated[i/2], false);
			}
		}
		for (int i = 0; i < values_to_be_updated.length
				&& values_to_be_updated[i] != ""; i++) {
//			if (values_to_be_updated[i].matches("[0-9]+"))
//				Types[i] = "int";
//			else
//				Types[i] = "varchar";
			Types[i] = Determin_Type(values_to_be_updated[i]);
		}
 
		ind = 0;
		output = parts[3].split("(\\s*,\\s*)|(\\s*=\\s*)|(\\s*'\\s*)");
		for (int i = 0; i < output.length; i++) {
			if (!output[i].matches("\\s*"))
				o[ind++] = output[i];
		}
		Column_condition = o[0].toLowerCase();
		Values_condition =o[1];
//		if (o[1].matches("[0-9]+"))
//			Type = "int";
//		else
//			Type = "varchar";
		Type = Determin_Type(o[1]);
	}
 
	public Boolean CreateTable(String s) {
		Pattern P = Pattern
				.compile("^(\\s*)((?i)create)(\\s+)((?i)table)(\\s+)([a-zA-Z0-9\\s\\.\\-\\_]+)(\\s*)(\\()"
						+ "(\\s*)([a-zA-Z0-9\\s\\.\\-\\_]+)(\\s+)([a-zA-Z0-9\\s\\.\\-\\_]+)(\\s*\\[[\\s0-9\\s\\-\\_]*\\]\\s*)?((\\s*)(\\()(\\s*)(\\d+)(\\s*)(\\)))?"
						+ "((\\s*)(,)(\\s*)([a-zA-Z0-9\\s\\.\\-\\_]+)(\\s+)([a-zA-Z0-9\\s\\.\\-\\_]+)(\\s*\\[[\\s0-9\\s\\.\\-\\_]*\\]\\s*)?((\\s*)(\\()(\\s*)(\\d+)(\\s*)(\\)))?){0,}(\\s*)(\\))(\\s*)(\\;|)\\s*$");
 
		Matcher M = P.matcher(s);
		if (M.find()) {
			parts = s
					.split("(\\s*,\\s*)|(?i)(\\s*create\\s*|\\s*table\\s*|\\s*\\(\\s*|\\s*;\\s*|\\s*\\)\\s*)");
			CT_Parsing();
			return true;
		}
		return false;
	}
 
	public void CT_Parsing() {
		int ind = 0, counter = 0, tt = 0;
		TableName = parts[2];
 
		for (int i = 3; i < parts.length; i++) {
			if (!parts[i].matches("\\d+|\\s*")) {
				counter++;
				output[ind++] = parts[i].split("(\\s+)");
			}
		}
		for (int i = 0; i < counter; i++)
			for (int j = 0; j < output[i].length; j++) {
				if (output[i][j] == null)
					continue;
				o[tt++] = output[i][j];
			}
		for (int i = 0; i < tt; i++) {
			if (i % 2 == 0)
				Columns[i / 2] = o[i].toLowerCase();
			else
    		{
    			String temp="";
    			if(o[i].contains("["))
    			{
    				isArray=true;
    				temp+="array_";
    			}
    			for(int j=0;j<o[i].length();j++)
    				if(o[i].charAt(j)>='A'&&o[i].charAt(j)<='Z'||o[i].charAt(j)>='a'&&o[i].charAt(j)<='z')
    				{
    					temp+=o[i].charAt(j);
    				}
    			Types[i/2]=temp.toLowerCase();
    		}
		}
	}
 
	/*********************************************************/
	public Boolean isInteger(String s) {
		try {
			Integer.parseInt(s);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
 
	public Boolean isDouble(String s) {
		try {
			Double.parseDouble(s);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}
 
	public Boolean isLong(String s){
		try{
			Long.parseLong(s);
		}catch (NumberFormatException e){
			return false;
		}
		return true;
	}
	public Boolean is_selected() {
		return isSelected;
	}
	public String Determin_Type(String s){
		String T = "" ;
		if(isInteger(s))T = "int";
		else if(isLong(s))T = "long";
		else if(isDouble(s))T = "float";
		else if(s.equals("true")||s.equals("false"))T= "boolean";
		else if(s.contains(",") || s.contains("}"))T = Det_array(s);
		else T = "varchar";
		return T ;
	}
	public Boolean check(String s){
		if(!Delete(s)&&!DeleteAllTable(s)&&!InsertIntoTableWith(s)&&!InsertIntoTableWithout(s)&&!Update(s)&&!CreateTable(s)&&!CreateDataBase(s))return false;
		return true;
	}
	public String Clean(String s , Boolean lower){
		s= s.replaceAll("^\\s+", "");
		s= s.replaceAll("\\s+$", "");
		s = s.replaceAll("'$", "");
		s = s.replaceAll("^'", "");
		s = s.replaceAll("\\{", "");
		s = s.replaceAll("\\}", "");
		if(lower)s = s.toLowerCase();
		return s ;
	}
	public String Det_array(String s){
		s = s.replaceAll("}", "");
		String a[] = s.split(",");
		a[0] = Clean(a[0], true);
		String T;
		if(isInteger(a[0]))T = "array_int";
		else if(isLong(a[0]))T = "array_long";
		else if(isDouble(a[0]))T = "array_float";
		else if(s == "true" || s == "false")T= "array_boolean";
		else T = "array_varchar";
		array_checker(a);
		return T ;
	}
	public void array_checker(String arr[]){
		for(int i = 1 ; i < arr.length;i++){
			if(Determin_Type(arr[i])!=Determin_Type(arr[i-1])){
				Equ = false;
				return ;
			}
		}
	}
}